package logbook.internal;

import logbook.bean.ShipMst;
import logbook.bean.Stype;

/**
 * 艦種定数
 *
 */
public enum ShipType {

    海防艦(1),
    駆逐艦(2),
    軽巡洋艦(3),
    重雷装巡洋艦(4),
    重巡洋艦(5),
    航空巡洋艦(6),
    軽空母(7),
    巡洋戦艦(8),
    戦艦(9),
    航空戦艦(10),
    正規空母(11),
    超弩級戦艦(12),
    潜水艦(13),
    潜水空母(14),
    補給艦E(15),
    水上機母艦(16),
    揚陸艦(17),
    装甲空母(18),
    工作艦(19),
    潜水母艦(20),
    練習巡洋艦(21),
    補給艦(22);

    private final int stype;

    private ShipType(int stype) {
        this.stype = stype;
    }

    /**
     * この定数がstypeと等しい場合trueを返します
     *
     * @param stype 艦種
     * @return 艦種がこの定数と同じ場合はtrue
     */
    public boolean equals(Stype stype) {
        return stype != null && this.stype == stype.getId();
    }

    /**
     * この定数がstypeと等しい場合trueを返します
     *
     * @param stype 艦種
     * @return 艦種がこの定数と同じ場合はtrue
     */
    public boolean equals(Integer stype) {
        return stype != null && this.stype == stype;
    }

    /**
     * この定数がshipMstと等しい場合trueを返します
     *
     * @param shipMst 艦船
     * @return 艦船がこの定数と同じ場合はtrue
     */
    public boolean equals(ShipMst shipMst) {
        return shipMst != null && this.stype == shipMst.getStype();
    }
}
