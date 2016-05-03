package logbook.internal;

/**
 * 海域
 *
 */
public enum SeaArea {

    鎮守府正面海域("1-1", "鎮守府正面海域", 30),
    南西諸島沖("1-2", "南西諸島沖", 50),
    製油所地帯沿岸("1-3", "製油所地帯沿岸", 80),
    南西諸島防衛線("1-4", "南西諸島防衛線", 100),
    鎮守府近海("1-5", "鎮守府近海", 150),
    鎮守府近海航路("1-6", "鎮守府近海航路"),
    カムラン半島("2-1", "カムラン半島", 120),
    バシー島沖("2-2", "バシー島沖", 150),
    東部オリョール海("2-3", "東部オリョール海", 200),
    沖ノ島海域("2-4", "沖ノ島海域", 300),
    沖ノ島沖("2-5", "沖ノ島沖", 250),
    モーレイ海("3-1", "モーレイ海", 310),
    キス島沖("3-2", "キス島沖", 320),
    アルフォンシーノ方面("3-3", "アルフォンシーノ方面", 330),
    北方海域全域("3-4", "北方海域全域", 350),
    北方AL海域("3-5", "北方AL海域", 400),
    ジャム島攻略作戦("4-1", "ジャム島攻略作戦", 310),
    カレー洋制圧戦("4-2", "カレー洋制圧戦", 320),
    リランカ島空襲("4-3", "リランカ島空襲", 330),
    カスガダマ沖海戦("4-4", "カスガダマ沖海戦", 340),
    カレー洋リランカ島沖("4-5", "カレー洋リランカ島沖"),
    南方海域前面("5-1", "南方海域前面", 360),
    珊瑚諸島沖("5-2", "珊瑚諸島沖", 380),
    サブ島沖海域("5-3", "サブ島沖海域", 400),
    サーモン海域("5-4", "サーモン海域", 420),
    サーモン海域北方("5-5", "サーモン海域北方", 450),
    中部海域哨戒線("6-1", "中部海域哨戒線", 380),
    MS諸島沖("6-2", "MS諸島沖", 420),
    グアノ環礁沖海域("6-3", "グアノ環礁沖海域"),
    中部北海域ピーコック島沖("6-4", "中部北海域ピーコック島沖"),

    連合艦隊(null, "連合艦隊", -1, 1),
    設営部隊(null, "設営部隊", -1, 2),
    南方部隊(null, "南方部隊", -1, 3),
    機動部隊(null, "機動部隊", -1, 4);

    /** 短い名前 */
    private String shortName;

    /** 名前 */
    private String name;

    /** 経験値 */
    private int seaExp;

    /** 海域(イベント海域のお札) */
    private int area;

    SeaArea(String shortName, String name) {
        this(shortName, name, -1);
    }

    SeaArea(String shortName, String name, int seaExp) {
        this(shortName, name, seaExp, -1);
    }

    SeaArea(String shortName, String name, int seaExp, int area) {
        this.shortName = shortName;
        this.name = name;
        this.seaExp = seaExp;
        this.area = area;
    }

    /**
     * 短い名前を取得します。
     * @return 短い名前
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 経験値を取得します。
     * @return 経験値
     */
    public int getSeaExp() {
        return this.seaExp;
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
        if (this.shortName != null) {
            return this.name + " (" + this.shortName + ")";
        } else {
            return this.name;
        }
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
            return SeaArea.連合艦隊;
        case 2:
            return SeaArea.設営部隊;
        case 3:
            return SeaArea.南方部隊;
        case 4:
            return SeaArea.機動部隊;
        default:
            return null;
        }
    }
}
