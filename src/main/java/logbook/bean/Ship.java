package logbook.bean;

/**
 * 艦娘の名前と種別を表します
 *
 */
public class Ship {

    /** 名前 */
    private String name;

    /** ふりがな/flagship */
    private String yomi;

    /** 艦種 */
    private String type;

    /** 改レベル */
    private int afterlv;

    /** 弾 */
    private int maxBull;

    /** 燃料 */
    private int maxFuel;

    /** shipgraph */
    private String graph;

    /** version */
    private String version;

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * @param name 名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ふりがな/flagshipを取得します。
     * @return ふりがな/flagship
     */
    public String getYomi() {
        return yomi;
    }

    /**
     * ふりがな/flagshipを設定します。
     * @param yomi ふりがな/flagship
     */
    public void setYomi(String yomi) {
        this.yomi = yomi;
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public String getType() {
        return type;
    }

    /**
     * 艦種を設定します。
     * @param type 艦種
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 改レベルを取得します。
     * @return 改レベル
     */
    public int getAfterlv() {
        return afterlv;
    }

    /**
     * 改レベルを設定します。
     * @param afterlv 改レベル
     */
    public void setAfterlv(int afterlv) {
        this.afterlv = afterlv;
    }

    /**
     * 弾を取得します。
     * @return 弾
     */
    public int getMaxBull() {
        return maxBull;
    }

    /**
     * 弾を設定します。
     * @param maxBull 弾
     */
    public void setMaxBull(int maxBull) {
        this.maxBull = maxBull;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public int getMaxFuel() {
        return maxFuel;
    }

    /**
     * 燃料を設定します。
     * @param maxFuel 燃料
     */
    public void setMaxFuel(int maxFuel) {
        this.maxFuel = maxFuel;
    }

    /**
     * shipgraphを取得します。
     * @return shipgraph
     */
    public String getGraph() {
        return graph;
    }

    /**
     * shipgraphを設定します。
     * @param graph shipgraph
     */
    public void setGraph(String graph) {
        this.graph = graph;
    }

    /**
     * versionを取得します。
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * versionを設定します。
     * @param version version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
