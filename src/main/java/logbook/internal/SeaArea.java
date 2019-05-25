package logbook.internal;

/**
 * 海域
 *
 */
public enum SeaArea {

    第百四戦隊("第百四戦隊", 1),
    第二艦隊("第二艦隊", 2),
    北方部隊("北方部隊", 3),
    機動部隊("機動部隊", 4),
    攻略部隊("攻略部隊", 5),
    ハワイ派遣艦隊("ハワイ派遣艦隊", 6);

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
            return SeaArea.第百四戦隊;
        case 2:
            return SeaArea.第二艦隊;
        case 3:
            return SeaArea.北方部隊;
        case 4:
            return SeaArea.機動部隊;
        case 5:
            return SeaArea.攻略部隊;
        case 6:
            return SeaArea.ハワイ派遣艦隊;
        default:
            return null;
        }
    }
}
