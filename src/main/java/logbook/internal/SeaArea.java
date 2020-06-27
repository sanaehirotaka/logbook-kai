package logbook.internal;

/**
 * 海域
 *
 */
public enum SeaArea {

    識別札1("千島方面部隊", 1),
    識別札2("沖縄方面部隊", 2),
    識別札3("第二遊撃部隊", 3),
    識別札4("小笠原方面部隊", 4),
    識別札5("識別札5", 5),
    識別札6("識別札6", 6),
    識別札7("識別札7", 7);

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
            return SeaArea.識別札1;
        case 2:
            return SeaArea.識別札2;
        case 3:
            return SeaArea.識別札3;
        case 4:
            return SeaArea.識別札4;
        case 5:
            return SeaArea.識別札5;
        case 6:
            return SeaArea.識別札6;
        case 7:
            return SeaArea.識別札7;
        default:
            return null;
        }
    }
}
