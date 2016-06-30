package logbook.internal;

import java.time.Duration;

public class Time {

    /**
     * 時間のテキスト表現
     *
     * @param d 期間
     * @param message メッセージ
     * @return 時間のテキスト表現
     */
    public static String toString(Duration d, String message) {
        long days = d.toDays();
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        long seconds = d.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days + "日");
        }
        if (hours > 0) {
            sb.append(hours + "時間");
        }
        if (minutes > 0) {
            sb.append(minutes + "分");
        }
        if (seconds > 0 && days == 0 && hours == 0) {
            sb.append(seconds + "秒");
        }
        if (d.isZero() || d.isNegative()) {
            sb.append(message);
        }
        return sb.toString();
    }
}
