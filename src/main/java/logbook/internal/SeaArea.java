package logbook.internal;

/**
 * 海域
 *
 */
public enum SeaArea {

    鼠輸送部隊("鼠輸送部隊", 1),
    ラバウル艦隊("ラバウル艦隊", 2),
    連合艦隊("連合艦隊", 3),
    ブイン派遣隊("ブイン派遣隊", 4);

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
            return SeaArea.鼠輸送部隊;
        case 2:
            return SeaArea.ラバウル艦隊;
        case 3:
            return SeaArea.連合艦隊;
        case 4:
            return SeaArea.ブイン派遣隊;
        default:
            return null;
        }
    }
}
