package logbook.internal;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.internal.gui.BattleLogCollect;
import logbook.internal.log.BattleResultLogFormat;
import logbook.internal.log.LogWriter;
import lombok.Data;

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
        // 書き込み
        try {
            Path path = jsonPath(log.getTime());

            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(writer, log);
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("戦闘ログの書き込み中に例外", e);
        }
        // 期限切れの削除
        deleteExpiration();
    }

    private static void deleteExpiration() {
        try {
            Path dir = Paths.get(AppConfig.get().getBattleLogDir());

            // 期限(自身を含まない)
            ZonedDateTime exp = unitToday()
                    .minusDays(AppConfig.get().getBattleLogExpires())
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
            // 比較するためのファイル名(拡張子を含まない)
            String expName = fileNameSafeDateString(Logs.DATE_FORMAT.format(exp));

            PathMatcher logFilter = dir.getFileSystem()
                    .getPathMatcher("glob:*.{json}");

            Consumer<Path> deleteAction = p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    LoggerHolder.get().warn("戦闘ログの削除中に例外", e);
                }
            };
            Predicate<Path> compareAction = p -> {
                String fname = p.getFileName().toString();
                int idx = fname.lastIndexOf('.');
                if (idx != -1) {
                    return fname.substring(0, idx).compareTo(expName) < 0;
                }
                return false;
            };

            try (Stream<Path> ds = Files.list(dir)) {
                ds.filter(p -> logFilter.matches(p.getFileName()))
                        .filter(compareAction)
                        .forEach(deleteAction);
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("戦闘ログの削除中に例外", e);
        }
    }

    /**
     * 日付を指定して戦闘ログを取得します。
     *
     * @param dateString 日付文字列
     * @return 戦闘ログ、存在しない又は入出力例外の場合null
     */
    public static BattleLog read(String dateString) {
        try {
            Path path = jsonPath(dateString);
            if (Files.isReadable(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(reader, BattleLog.class);
                }
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("戦闘ログの読み込み中に例外", e);
        }
        return null;
    }

    private static Path jsonPath(String dateString) {
        Path dir = Paths.get(AppConfig.get().getBattleLogDir());
        return dir.resolve(fileNameSafeDateString(dateString) + ".json");
    }

    private static String fileNameSafeDateString(String dateString) {
        return dateString.replace(':', '-');
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
                    LoggerHolder.get().warn("海戦・ドロップ報告書の読み込み中に例外", e);
                }
                return null;
            };
            Path dir = Paths.get(AppConfig.get().getReportPath());
            Path path = dir.resolve(new BattleResultLogFormat().fileName());

            // 今日
            ZonedDateTime now = unitToday();
            // ログ読み込み制限
            ZonedDateTime limit = now.minusMonths(2);

            Stream<String> tmp;
            if (Files.exists(path)) {
                tmp = Files.lines(path, LogWriter.DEFAULT_CHARSET);
            } else {
                tmp = Stream.empty();
            }
            try (Stream<String> lines = tmp) {
                List<SimpleBattleLog> all = lines.skip(1)
                        .filter(l -> !l.isEmpty())
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
            LoggerHolder.get().warn("海戦・ドロップ報告書の読み込み中に例外", e);
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

        // 出撃回数
        String start;
        if (bossOnly) {
            // 集計対象をボスのみにする場合は出撃を集計しない
            start = "-";
        } else {
            start = Long.toString(logs.stream()
                    .filter(areaFilter)
                    .map(SimpleBattleLog::getBoss)
                    .filter(boss -> boss.indexOf("出撃") != -1)
                    .count());
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
        value.setStart(start);
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
    @Data
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
        /** ドロップアイテム */
        private String dropItem = "";

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
            if (columns.length > 62) {
                this.setDropItem(columns[62]);
            }
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
}
