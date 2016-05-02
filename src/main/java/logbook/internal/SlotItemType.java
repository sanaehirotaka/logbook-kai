package logbook.internal;

import logbook.bean.SlotitemMst;

/**
 * 装備種定数
 *
 */
public enum SlotItemType {

    小口径主砲(1),
    中口径主砲(2),
    大口径主砲(3),
    副砲(4),
    魚雷(5),
    艦上戦闘機(6),
    艦上爆撃機(7),
    艦上攻撃機(8),
    艦上偵察機(9),
    水上偵察機(10),
    水上爆撃機(11),
    小型電探(12),
    大型電探(13),
    ソナー(14),
    爆雷(15),
    追加装甲(16),
    機関部強化(17),
    対空強化弾(18),
    対艦強化弾(19),
    VT信管(20),
    対空機銃(21),
    特殊潜航艇(22),
    応急修理要員(23),
    上陸用舟艇(24),
    オートジャイロ(25),
    対潜哨戒機(26),
    追加装甲中型(27),
    追加装甲大型(28),
    探照灯(29),
    簡易輸送部材(30),
    艦艇修理施設(31),
    潜水艦魚雷(32),
    照明弾(33),
    司令部施設(34),
    航空要員(35),
    高射装置(36),
    対地装備(37),
    大口径主砲II(38),
    水上艦要員(39),
    大型ソナー(40),
    大型飛行艇(41),
    大型探照灯(42),
    戦闘糧食(43),
    補給物資(44),
    水上戦闘機(45),
    特型内火艇(46),
    陸上攻撃機(47),
    局地戦闘機(48),
    大型電探II(93),
    艦上偵察機II(94),

    不明(-1);

    private final int item;

    private SlotItemType(int item) {
        this.item = item;
    }

    /**
     * この定数がitemと等しい場合trueを返します
     *
     * @param item 装備
     * @return 装備がこの定数と同じ場合はtrue
     */
    public boolean equals(SlotitemMst item) {
        return item != null && this.item == item.getType().get(2);
    }

    /**
     * この定数がitemと等しい場合trueを返します
     *
     * @param item 装備(api_type[2])
     * @return 装備がこの定数と同じ場合はtrue
     */
    public boolean equals(Integer item) {
        return item != null && this.item == item;
    }

    /**
     * 装備種定数を返します
     *
     * @param item 装備
     * @return 装備種定数
     */
    public static SlotItemType toSlotItemType(SlotitemMst item) {
        int type = item.getType().get(2);
        for (SlotItemType e : values()) {
            if (e.item == type) {
                return e;
            }
        }
        return 不明;
    }
}
