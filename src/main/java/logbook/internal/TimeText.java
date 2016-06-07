/**
 * 
 */
package logbook.internal;

import java.time.Duration;

/**
 * @author turane_gaku
 *
 */
public class TimeText {
    /**
     * Durationを文字列にして返す
     *
     * @param d フォーマットする対象
     * @param t 0 以下のときの文字列
     * @return フォーマット結果
     */
    public static String format(Duration d, String t) {
        long days = d.toDays();
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        long seconds = d.getSeconds() % 60;

        if (d.isZero() || d.isNegative())
            return t;
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days + "日");
        }
        if (hours > 0) {
            sb.append(hours + "時間");
        }
        if (minutes > 0 || hours > 0) {
            if (minutes < 10)
                sb.append('0');
            sb.append(minutes + "分");
        }
        if (days == 0 && hours == 0) {
            if (seconds < 10 && minutes + hours != 0)
                sb.append('0');
            sb.append(seconds + "秒");
        }
        return sb.toString();

    }

}
