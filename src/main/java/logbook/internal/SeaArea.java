package logbook.internal;

/**
 * 海域
 *
 */
public enum SeaArea {

    警戒部隊("警戒部隊", 1),
    海峡派遣艦隊("海峡派遣艦隊", 2),
    西方作戦部隊("西方作戦部隊", 3),
    欧州特務艦隊("欧州特務艦隊", 4),
    ForceH("ForceH", 5),
    ライン演習部隊("ライン演習部隊", 6);

    /** 名前 */
    private String name;

    /** 海域(イベント海域のお札) */
    private int area;

    SeaArea(String name, int area) {
        this.name = name;
        this.area = area;
    }

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 海域(イベント海域のお札)を取得します。
     * @return 海域(イベント海域のお札)
     */
    public int getArea() {
        return this.area;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * イベント海域を取得します
     *
     * @param area お札
     * @return 海域
     */
    public static SeaArea fromArea(int area) {
        switch (area) {
        case 1:
            return SeaArea.警戒部隊;
        case 2:
            return SeaArea.海峡派遣艦隊;
        case 3:
            return SeaArea.西方作戦部隊;
        case 4:
            return SeaArea.欧州特務艦隊;
        case 5:
            return SeaArea.ForceH;
        case 6:
            return SeaArea.ライン演習部隊;
        default:
            return null;
        }
    }
}
