package logbook.internal;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.internal.gui.BattleLogCollect;

/**
 * 戦闘ログに関するクラス
 *
 */
public class BattleLogs {

    /**
     * 戦闘ログを書き込みます
     *
     * @param log 戦闘ログ
     */
    public static void write(BattleLog log) {
        Path dir = Paths.get(AppConfig.get().getBattleLogDir());
        // yyyy-MM-dd hh:mm:ss -> yyyy-MM-dd hh-mm-ss
        String name = toName(log.getTime());
        Path path = dir.resolve(name);

        // 書き込み
        write(path, log);
        // 期限切れの削除
        deleteExpiration(dir);
    }

    private static void write(Path path, BattleLog log) {
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path))) {
                try (XMLEncoder encoder = new XMLEncoder(out)) {
                    encoder.writeObject(log);
                }
            }
        } catch (IOException e) {
            LoggerHolder.LOG.warn("戦闘ログの書き込み中に例外", e);
        }
    }

    private static void deleteExpiration(Path dir) {
        try {
            // 期限(自身を含まない)
            ZonedDateTime exp = unitToday()
                    .minusDays(AppConfig.get().getBattleLogExpires())
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
            // 比較するためのファイル名
            String expName = toName(Logs.DATE_FORMAT.format(exp));

            PathMatcher xmlFilter = dir.getFileSystem()
                    .getPathMatcher("glob:*.xml");

            Consumer<Path> deleteAction = p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    LoggerHolder.LOG.warn("戦闘ログの削除中に例外", e);
                }
            };

            try (Stream<Path> ds = Files.list(dir)) {
                ds.filter(p -> xmlFilter.matches(p.getFileName()))
                        .filter(p -> p.getFileName().toString().compareTo(expName) < 0)
                        .forEach(deleteAction);
            }
        } catch (IOException e) {
            LoggerHolder.LOG.warn("戦闘ログの削除中に例外", e);
        }
    }

    /**
     * 日付を指定して戦闘ログを取得します。
     *
     * @param dateString 日付文字列
     * @return 戦闘ログ、存在しない又は入出力例外の場合null
     */
    public static BattleLog read(String dateString) {
        String name = toName(dateString);
        Path dir = Paths.get(AppConfig.get().getBattleLogDir());
        return read(dir.resolve(name));
    }

    private static BattleLog read(Path path) {
        try {
            if (Files.isReadable(path)) {
                try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
                    try (XMLDecoder decoder = new XMLDecoder(in)) {
                        Object obj = decoder.readObject();

                        if (obj instanceof BattleLog) {
                            return (BattleLog) obj;
                        }
                    }
                }
            }
        } catch (IOException e) {
            LoggerHolder.LOG.warn("戦闘ログの読み込み中に例外", e);
        }
        return null;
    }

    private static String toName(String dateTimeString) {
        return dateTimeString.replace(':', '-') + ".xml";
    }

    /**
     * 出撃統計のベースになるリストを取得します。
     *
     * @return 出撃統計のベースになるリスト
     */
    public static Map<Unit, List<SimpleBattleLog>> readSimpleLog() {
        try {
            Function<String, SimpleBattleLog> mapper = line -> {
                try {
                    return new SimpleBattleLog(line);
                } catch (Exception e) {
                    LoggerHolder.LOG.warn("海戦・ドロップ報告書の読み込み中に例外", e);
                }
                return null;
            };
            Path dir = Paths.get(AppConfig.get().getReportPath());
            Path path = dir.resolve(Logs.BATTLE_RESULT);

            // 今日
            ZonedDateTime now = unitToday();
            // ログ読み込み制限
            ZonedDateTime limit = now.minusMonths(2);

            if (Files.exists(path)) {
                List<SimpleBattleLog> all = Files.lines(path, LogWriter.DEFAULT_CHARSET)
                        .skip(1)
                        .map(mapper)
                        .filter(Objects::nonNull)
                        .filter(log -> log.getDate().compareTo(limit) > 0)
                        .collect(Collectors.toList());

                EnumMap<Unit, List<SimpleBattleLog>> map = new EnumMap<>(Unit.class);
                for (Unit unit : Unit.values()) {
                    map.put(unit, all.stream()
                            .filter(log -> unit.accept(log.getDate(), now))
                            .collect(Collectors.toList()));
                }
                return map;
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("海戦・ドロップ報告書の読み込み中に例外", e);
        }
        return Collections.emptyMap();
    }

    /**
     * 集計します
     *
     * @param logs 出撃統計のベースになるリスト
     * @param area 海域(nullの場合前開域)
     * @param bossOnly 集計対象をボスのみにする場合true
     * @return 出撃統計
     */
    public static BattleLogCollect collect(List<SimpleBattleLog> logs, String area, boolean bossOnly) {
        Predicate<SimpleBattleLog> anyFilter = e -> true;
        // 海域フィルタ
        Predicate<SimpleBattleLog> areaFilter = area != null ? e -> area.equals(e.getArea()) : anyFilter;
        // ボスフィルタ
        Predicate<SimpleBattleLog> bossFilter = bossOnly ? e -> e.getBoss().indexOf("ボス") != -1 : anyFilter;

        // 出撃
        long start = 0;
        if (!bossOnly) {
            // ボスのみの場合は出撃を集計しない
            start = logs.stream()
                    .filter(areaFilter)
                    .map(SimpleBattleLog::getBoss)
                    .filter("出撃"::equals)
                    .count();
        }
        // 評価(S-D)
        Map<String, Long> rank = logs.stream()
                .filter(areaFilter)
                .filter(bossFilter)
                .map(SimpleBattleLog::getRank)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        long s = rank.getOrDefault("S", 0L);
        long a = rank.getOrDefault("A", 0L);
        long b = rank.getOrDefault("B", 0L);
        long c = rank.getOrDefault("C", 0L);
        long d = rank.getOrDefault("D", 0L);
        long win = s + a + b;

        BattleLogCollect value = new BattleLogCollect();
        if (bossOnly) {
            value.setStart("-");
        } else {
            value.setStart(String.valueOf(start));
        }
        value.setWin(String.valueOf(win));
        value.setS(String.valueOf(s));
        value.setA(String.valueOf(a));
        value.setB(String.valueOf(b));
        value.setC(String.valueOf(c));
        value.setD(String.valueOf(d));
        return value;
    }

    private static ZonedDateTime unitToday() {
        return ZonedDateTime.now(ZoneId.of("GMT+04:00"))
                .truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * 出撃統計のベース
     *
     */
    public static class SimpleBattleLog {

        /** 日付 */
        private ZonedDateTime date;
        /** 海域 */
        private String area;
        /** マス */
        private String cell;
        /** ボス */
        private String boss;
        /** ランク */
        private String rank;
        /** 艦隊行動 */
        private String intercept;
        /** 味方陣形 */
        private String fformation;
        /** 敵陣形 */
        private String eformation;
        /** 制空権 */
        private String dispseiku;
        /** 味方触接 */
        private String ftouch;
        /** 敵触接 */
        private String etouch;
        /** 敵艦隊 */
        private String efleet;
        /** ドロップ艦種 */
        private String dropType;
        /** ドロップ艦娘 */
        private String dropShip;

        /**
         * 海戦・ドロップ報告書.csvから出撃統計のベースを作成します
         *
         * @param line 海戦・ドロップ報告書.csvの行
         */
        public SimpleBattleLog(String line) {
            String[] columns = line.split(",", -1);

            // 任務の更新時間が午前5時のため
            // 日付文字列を日本時間として解釈した後、GMT+04:00のタイムゾーンに変更します
            TemporalAccessor ta = Logs.DATE_FORMAT.parse(columns[0]);
            ZonedDateTime date = ZonedDateTime.of(LocalDateTime.from(ta), ZoneId.of("Asia/Tokyo"))
                    .withZoneSameInstant(ZoneId.of("GMT+04:00"));
            this.setDate(date);
            this.setArea(columns[1]);
            this.setCell(columns[2]);
            this.setBoss(columns[3]);
            this.setRank(columns[4]);
            this.setIntercept(columns[5]);
            this.setFformation(columns[6]);
            this.setEformation(columns[7]);
            this.setDispseiku(columns[8]);
            this.setFtouch(columns[9]);
            this.setEtouch(columns[10]);
            this.setEfleet(columns[11]);
            this.setDropType(columns[12]);
            this.setDropShip(columns[13]);
        }

        /**
         * 日付を取得します。
         * @return 日付
         */
        public ZonedDateTime getDate() {
            return this.date;
        }

        /**
         * 日付を設定します。
         * @param date 日付
         */
        public void setDate(ZonedDateTime date) {
            this.date = date;
        }

        /**
         * 海域を取得します。
         * @return 海域
         */
        public String getArea() {
            return this.area;
        }

        /**
         * 海域を設定します。
         * @param area 海域
         */
        public void setArea(String area) {
            this.area = area;
        }

        /**
         * マスを取得します。
         * @return マス
         */
        public String getCell() {
            return this.cell;
        }

        /**
         * マスを設定します。
         * @param cell マス
         */
        public void setCell(String cell) {
            this.cell = cell;
        }

        /**
         * ボスを取得します。
         * @return ボス
         */
        public String getBoss() {
            return this.boss;
        }

        /**
         * ボスを設定します。
         * @param boss ボス
         */
        public void setBoss(String boss) {
            this.boss = boss;
        }

        /**
         * ランクを取得します。
         * @return ランク
         */
        public String getRank() {
            return this.rank;
        }

        /**
         * ランクを設定します。
         * @param rank ランク
         */
        public void setRank(String rank) {
            this.rank = rank;
        }

        /**
         * 艦隊行動を取得します。
         * @return 艦隊行動
         */
        public String getIntercept() {
            return this.intercept;
        }

        /**
         * 艦隊行動を設定します。
         * @param intercept 艦隊行動
         */
        public void setIntercept(String intercept) {
            this.intercept = intercept;
        }

        /**
         * 味方陣形を取得します。
         * @return 味方陣形
         */
        public String getFformation() {
            return this.fformation;
        }

        /**
         * 味方陣形を設定します。
         * @param fformation 味方陣形
         */
        public void setFformation(String fformation) {
            this.fformation = fformation;
        }

        /**
         * 敵陣形を取得します。
         * @return 敵陣形
         */
        public String getEformation() {
            return this.eformation;
        }

        /**
         * 敵陣形を設定します。
         * @param eformation 敵陣形
         */
        public void setEformation(String eformation) {
            this.eformation = eformation;
        }

        /**
         * 制空権を取得します。
         * @return 制空権
         */
        public String getDispseiku() {
            return this.dispseiku;
        }

        /**
         * 制空権を設定します。
         * @param dispseiku 制空権
         */
        public void setDispseiku(String dispseiku) {
            this.dispseiku = dispseiku;
        }

        /**
         * 味方触接を取得します。
         * @return 味方触接
         */
        public String getFtouch() {
            return this.ftouch;
        }

        /**
         * 味方触接を設定します。
         * @param ftouch 味方触接
         */
        public void setFtouch(String ftouch) {
            this.ftouch = ftouch;
        }

        /**
         * 敵触接を取得します。
         * @return 敵触接
         */
        public String getEtouch() {
            return this.etouch;
        }

        /**
         * 敵触接を設定します。
         * @param etouch 敵触接
         */
        public void setEtouch(String etouch) {
            this.etouch = etouch;
        }

        /**
         * 敵艦隊を取得します。
         * @return 敵艦隊
         */
        public String getEfleet() {
            return this.efleet;
        }

        /**
         * 敵艦隊を設定します。
         * @param efleet 敵艦隊
         */
        public void setEfleet(String efleet) {
            this.efleet = efleet;
        }

        /**
         * ドロップ艦種を取得します。
         * @return ドロップ艦種
         */
        public String getDropType() {
            return this.dropType;
        }

        /**
         * ドロップ艦種を設定します。
         * @param dropType ドロップ艦種
         */
        public void setDropType(String dropType) {
            this.dropType = dropType;
        }

        /**
         * ドロップ艦娘を取得します。
         * @return ドロップ艦娘
         */
        public String getDropShip() {
            return this.dropShip;
        }

        /**
         * ドロップ艦娘を設定します。
         * @param dropShip ドロップ艦娘
         */
        public void setDropShip(String dropShip) {
            this.dropShip = dropShip;
        }
    }

    /**
     * 集計の単位
     *
     */
    public enum Unit {

        /** デイリー */
        DAILY("デイリー") {
            @Override
            public boolean accept(ZonedDateTime target, ZonedDateTime now) {
                TemporalField field = ChronoField.DAY_OF_YEAR;
                return now.get(field) == target.get(field);
            }
        },
        /** ウィークリー */
        WEEKLY("ウィークリー") {
            @Override
            public boolean accept(ZonedDateTime target, ZonedDateTime now) {
                TemporalField field = WeekFields.ISO.weekOfWeekBasedYear();
                return now.get(field) == target.get(field);
            }
        },
        /** マンスリー */
        MONTHLY("マンスリー") {
            @Override
            public boolean accept(ZonedDateTime target, ZonedDateTime now) {
                TemporalField field = ChronoField.MONTH_OF_YEAR;
                return now.get(field) == target.get(field);
            }
        },
        /** 先週 */
        LAST_WEEK("先週") {
            @Override
            public boolean accept(ZonedDateTime target, ZonedDateTime now) {
                TemporalField field = WeekFields.ISO.weekOfWeekBasedYear();
                return now.minusWeeks(1).get(field) == target.get(field);
            }
        },
        /** 先月 */
        LAST_MONTH("先月") {
            @Override
            public boolean accept(ZonedDateTime target, ZonedDateTime now) {
                TemporalField field = ChronoField.MONTH_OF_YEAR;
                return now.minusMonths(1).get(field) == target.get(field);
            }
        };

        /** 名前 */
        private String name;

        /**
         * 集計の単位を作成します
         *
         * @param name 名前
         */
        Unit(String name) {
            this.name = name;
        }

        /**
         * 名前を取得します。
         * @return 名前
         */
        public String getName() {
            return this.name;
        }

        /**
         * 集計するかを判定します
         *
         * @param target 集計対象の日付(タイムゾーンがGMT+04:00)
         * @param now 今日(タイムゾーンがGMT+04:00)
         * @return 集計する場合true
         */
        public boolean accept(ZonedDateTime target, ZonedDateTime now) {
            throw new UnsupportedOperationException();
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(BattleLogs.class);
    }
}
