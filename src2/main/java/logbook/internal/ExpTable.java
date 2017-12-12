package logbook.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 経験値テーブル
 *
 */
public class ExpTable {

    /**
     * 経験値テーブルプリセット値
     */
    private static final Map<Integer, Integer> EXP_TABLE;

    static {
        EXP_TABLE = new LinkedHashMap<>();
        EXP_TABLE.put(1, 0);
        EXP_TABLE.put(2, 100);
        EXP_TABLE.put(3, 300);
        EXP_TABLE.put(4, 600);
        EXP_TABLE.put(5, 1000);
        EXP_TABLE.put(6, 1500);
        EXP_TABLE.put(7, 2100);
        EXP_TABLE.put(8, 2800);
        EXP_TABLE.put(9, 3600);
        EXP_TABLE.put(10, 4500);
        EXP_TABLE.put(11, 5500);
        EXP_TABLE.put(12, 6600);
        EXP_TABLE.put(13, 7800);
        EXP_TABLE.put(14, 9100);
        EXP_TABLE.put(15, 10500);
        EXP_TABLE.put(16, 12000);
        EXP_TABLE.put(17, 13600);
        EXP_TABLE.put(18, 15300);
        EXP_TABLE.put(19, 17100);
        EXP_TABLE.put(20, 19000);
        EXP_TABLE.put(21, 21000);
        EXP_TABLE.put(22, 23100);
        EXP_TABLE.put(23, 25300);
        EXP_TABLE.put(24, 27600);
        EXP_TABLE.put(25, 30000);
        EXP_TABLE.put(26, 32500);
        EXP_TABLE.put(27, 35100);
        EXP_TABLE.put(28, 37800);
        EXP_TABLE.put(29, 40600);
        EXP_TABLE.put(30, 43500);
        EXP_TABLE.put(31, 46500);
        EXP_TABLE.put(32, 49600);
        EXP_TABLE.put(33, 52800);
        EXP_TABLE.put(34, 56100);
        EXP_TABLE.put(35, 59500);
        EXP_TABLE.put(36, 63000);
        EXP_TABLE.put(37, 66600);
        EXP_TABLE.put(38, 70300);
        EXP_TABLE.put(39, 74100);
        EXP_TABLE.put(40, 78000);
        EXP_TABLE.put(41, 82000);
        EXP_TABLE.put(42, 86100);
        EXP_TABLE.put(43, 90300);
        EXP_TABLE.put(44, 94600);
        EXP_TABLE.put(45, 99000);
        EXP_TABLE.put(46, 103500);
        EXP_TABLE.put(47, 108100);
        EXP_TABLE.put(48, 112800);
        EXP_TABLE.put(49, 117600);
        EXP_TABLE.put(50, 122500);
        EXP_TABLE.put(51, 127500);
        EXP_TABLE.put(52, 132700);
        EXP_TABLE.put(53, 138100);
        EXP_TABLE.put(54, 143700);
        EXP_TABLE.put(55, 149500);
        EXP_TABLE.put(56, 155500);
        EXP_TABLE.put(57, 161700);
        EXP_TABLE.put(58, 168100);
        EXP_TABLE.put(59, 174700);
        EXP_TABLE.put(60, 181500);
        EXP_TABLE.put(61, 188500);
        EXP_TABLE.put(62, 195800);
        EXP_TABLE.put(63, 203400);
        EXP_TABLE.put(64, 211300);
        EXP_TABLE.put(65, 219500);
        EXP_TABLE.put(66, 228000);
        EXP_TABLE.put(67, 236800);
        EXP_TABLE.put(68, 245900);
        EXP_TABLE.put(69, 255300);
        EXP_TABLE.put(70, 265000);
        EXP_TABLE.put(71, 275000);
        EXP_TABLE.put(72, 285400);
        EXP_TABLE.put(73, 296200);
        EXP_TABLE.put(74, 307400);
        EXP_TABLE.put(75, 319000);
        EXP_TABLE.put(76, 331000);
        EXP_TABLE.put(77, 343400);
        EXP_TABLE.put(78, 356200);
        EXP_TABLE.put(79, 369400);
        EXP_TABLE.put(80, 383000);
        EXP_TABLE.put(81, 397000);
        EXP_TABLE.put(82, 411500);
        EXP_TABLE.put(83, 426500);
        EXP_TABLE.put(84, 442000);
        EXP_TABLE.put(85, 458000);
        EXP_TABLE.put(86, 474500);
        EXP_TABLE.put(87, 491500);
        EXP_TABLE.put(88, 509000);
        EXP_TABLE.put(89, 527000);
        EXP_TABLE.put(90, 545500);
        EXP_TABLE.put(91, 564500);
        EXP_TABLE.put(92, 584500);
        EXP_TABLE.put(93, 606500);
        EXP_TABLE.put(94, 631500);
        EXP_TABLE.put(95, 661500);
        EXP_TABLE.put(96, 701500);
        EXP_TABLE.put(97, 761500);
        EXP_TABLE.put(98, 851500);
        EXP_TABLE.put(99, 1000000);
        EXP_TABLE.put(100, 1000000);
        EXP_TABLE.put(101, 1010000);
        EXP_TABLE.put(102, 1011000);
        EXP_TABLE.put(103, 1013000);
        EXP_TABLE.put(104, 1016000);
        EXP_TABLE.put(105, 1020000);
        EXP_TABLE.put(106, 1025000);
        EXP_TABLE.put(107, 1031000);
        EXP_TABLE.put(108, 1038000);
        EXP_TABLE.put(109, 1046000);
        EXP_TABLE.put(110, 1055000);
        EXP_TABLE.put(111, 1065000);
        EXP_TABLE.put(112, 1077000);
        EXP_TABLE.put(113, 1091000);
        EXP_TABLE.put(114, 1107000);
        EXP_TABLE.put(115, 1125000);
        EXP_TABLE.put(116, 1145000);
        EXP_TABLE.put(117, 1168000);
        EXP_TABLE.put(118, 1194000);
        EXP_TABLE.put(119, 1223000);
        EXP_TABLE.put(120, 1255000);
        EXP_TABLE.put(121, 1290000);
        EXP_TABLE.put(122, 1329000);
        EXP_TABLE.put(123, 1372000);
        EXP_TABLE.put(124, 1419000);
        EXP_TABLE.put(125, 1470000);
        EXP_TABLE.put(126, 1525000);
        EXP_TABLE.put(127, 1584000);
        EXP_TABLE.put(128, 1647000);
        EXP_TABLE.put(129, 1714000);
        EXP_TABLE.put(130, 1785000);
        EXP_TABLE.put(131, 1860000);
        EXP_TABLE.put(132, 1940000);
        EXP_TABLE.put(133, 2025000);
        EXP_TABLE.put(134, 2115000);
        EXP_TABLE.put(135, 2210000);
        EXP_TABLE.put(136, 2310000);
        EXP_TABLE.put(137, 2415000);
        EXP_TABLE.put(138, 2525000);
        EXP_TABLE.put(139, 2640000);
        EXP_TABLE.put(140, 2760000);
        EXP_TABLE.put(141, 2887000);
        EXP_TABLE.put(142, 3021000);
        EXP_TABLE.put(143, 3162000);
        EXP_TABLE.put(144, 3310000);
        EXP_TABLE.put(145, 3465000);
        EXP_TABLE.put(146, 3628000);
        EXP_TABLE.put(147, 3799000);
        EXP_TABLE.put(148, 3978000);
        EXP_TABLE.put(149, 4165000);
        EXP_TABLE.put(150, 4360000);
        EXP_TABLE.put(151, 4564000);
        EXP_TABLE.put(152, 4777000);
        EXP_TABLE.put(153, 4999000);
        EXP_TABLE.put(154, 5230000);
        EXP_TABLE.put(155, 5470000);
    }

    /**
     * 経験値テーブルを取得します
     *
     * @return 経験値テーブル
     */
    public static Map<Integer, Integer> get() {
        return EXP_TABLE;
    }

    /**
     * 最大Lvを取得します
     *
     * @return 最大Lv
     */
    public static int maxLv() {
        return 155;
    }
}
