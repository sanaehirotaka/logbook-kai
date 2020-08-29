package logbook.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logbook.bean.AppConfig;
import logbook.internal.log.LogWriter;
import logbook.internal.log.MissionResultLogFormat;
import lombok.Data;

/**
 * 遠征ログに関するクラス
 */
public class MissionLogs {
    /**
     * ログを読み込む
     */
    public static List<SimpleMissionLog> readSimpleLog(Predicate<? super SimpleMissionLog> filter) {
        Function<String, SimpleMissionLog> mapper = line -> {
            try {
                return new SimpleMissionLog(line);
            } catch (Exception e) {
                LoggerHolder.get().warn("遠征報告書の読み込み中に例外", e);
            }
            return null;
        };
        Path dir = Paths.get(AppConfig.get().getReportPath());
        Path path = dir.resolve(new MissionResultLogFormat().fileName());

        Stream<String> tmp;
        if (Files.exists(path)) {
            try {
                tmp = Files.lines(path, LogWriter.DEFAULT_CHARSET);
            } catch (IOException e) {
                tmp = Stream.empty();
            }
        } else {
            tmp = Stream.empty();
        }
        try (Stream<String> lines = tmp) {
            return lines.skip(1)
                    .filter(l -> !l.isEmpty())
                    .map(mapper)
                    .filter(Objects::nonNull)
                    .filter(filter)
                    .collect(Collectors.toList());
        }
    }
    

    /**
     * 遠征統計のベース
     *
     */
    @Data
    public static class SimpleMissionLog {

        /** 日付文字列 */
        private String dateString;
        /** 日付 */
        private ZonedDateTime date;
        /** 結果 */
        private String result;
        /** 海域 */
        private String area;
        /** 遠征名 */
        private String name;
        /** 燃料 */
        private int fuel;
        /** 弾薬 */
        private int ammo;
        /** 鋼材 */
        private int metal;
        /** ボーキ */
        private int bauxite;
        /** アイテム1名前 */
        private String item1name = "";
        /** アイテム1個数 */
        private int item1count;
        /** アイテム2名前 */
        private String item2name = "";
        /** アイテム2個数 */
        private int item2count;
        /** 取得経験値計 */
        private int exp;

        /**
         * 遠征報告書.csvから遠征統計のベースを作成します
         *
         * @param line 遠征報告書.csvの行
         */
        public SimpleMissionLog(String line) {
            String[] columns = line.split(",", -1);
            
            this.setDateString(columns[0]);
            // 任務の更新時間が午前5時のため
            // 日付文字列を日本時間として解釈した後、GMT+04:00のタイムゾーンに変更します
            TemporalAccessor ta = Logs.DATE_FORMAT.parse(columns[0]);
            ZonedDateTime date = ZonedDateTime.of(LocalDateTime.from(ta), ZoneId.of("Asia/Tokyo"))
                    .withZoneSameInstant(ZoneId.of("GMT+04:00"));
            this.setDate(date);
            this.setResult(columns[1]);
            this.setArea(columns[2]);
            this.setName(columns[3]);
            this.setFuel(Integer.parseInt(columns[4]));
            this.setAmmo(Integer.parseInt(columns[5]));
            this.setMetal(Integer.parseInt(columns[6]));
            this.setBauxite(Integer.parseInt(columns[7]));
            if (columns.length > 8)
                this.setItem1name(columns[8]);
            if (columns.length > 9)
                this.setItem1count(columns[9].isEmpty() ? 0 : Integer.parseInt(columns[9]));
            if (columns.length > 10)
                this.setItem2name(columns[10]);
            if (columns.length > 11)
                this.setItem2count(columns[11].isEmpty() ? 0 : Integer.parseInt(columns[11]));
            if (columns.length > 12)
                this.setExp(columns[12].isEmpty() ? 0 : Integer.parseInt(columns[12]));
        }
    }

}
