package logbook.internal;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Time {

    /**
     * 時間のテキスト表現
     *
     * @param d 期間
     * @param message {@code Duration}が0または負の場合のテキスト
     * @return 時間のテキスト表現
     */
    public static String toString(Duration d, String message) {
        if (d.isZero() || d.isNegative()) {
            return message;
        }
        // Durationの秒未満を切り上げた秒数
        long duration = d.getSeconds() + Math.min(1, d.getNano());

        long days = TimeUnit.SECONDS.toDays(duration);
        long hours = TimeUnit.SECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(duration) % 60;
        long seconds = duration % 60;

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
        return sb.toString();
    }
}
