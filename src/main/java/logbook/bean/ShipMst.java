package logbook.bean;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 艦娘の名前と種別を表します
 *
 */
public class ShipMst implements Serializable {

    private static final long serialVersionUID = 4329488719132098164L;

    /** id */
    private Integer id;

    /** sortno */
    private Integer sortno;

    /** 名前 */
    private String name;

    /** ふりがな/flagship */
    private String yomi;

    /** 艦種 */
    private Integer stype;

    /** 改レベル */
    private Integer afterlv;

    /** 改装後id */
    private Integer aftershipid;

    /** 改装資材 燃料 */
    private Integer afterfuel;

    /** 改装資材 弾 */
    private Integer afterbull;

    /** 燃料 */
    private Integer fuelMax;

    /** 弾 */
    private Integer bullMax;

    /** shipgraph */
    private String graph;

    /** version */
    private String version;

    /**
     * idを取得します。
     * @return id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * idを設定します。
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * sortnoを取得します。
     * @return sortno
     */
    public Integer getSortno() {
        return this.sortno;
    }

    /**
     * sortnoを設定します。
     * @param sortno sortno
     */
    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return this.name;
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
        return this.yomi;
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
    public Integer getStype() {
        return this.stype;
    }

    /**
     * 艦種を設定します。
     * @param stype 艦種
     */
    public void setStype(Integer stype) {
        this.stype = stype;
    }

    /**
     * 改レベルを取得します。
     * @return 改レベル
     */
    public Integer getAfterlv() {
        return this.afterlv;
    }

    /**
     * 改レベルを設定します。
     * @param afterlv 改レベル
     */
    public void setAfterlv(Integer afterlv) {
        this.afterlv = afterlv;
    }

    /**
     * 改装後idを取得します。
     * @return 改装後id
     */
    public Integer getAftershipid() {
        return this.aftershipid;
    }

    /**
     * 改装後idを設定します。
     * @param aftershipid 改装後id
     */
    public void setAftershipid(Integer aftershipid) {
        this.aftershipid = aftershipid;
    }

    /**
     * 改装資材 燃料を取得します。
     * @return 改装資材 燃料
     */
    public Integer getAfterfuel() {
        return this.afterfuel;
    }

    /**
     * 改装資材 燃料を設定します。
     * @param afterfuel 改装資材 燃料
     */
    public void setAfterfuel(Integer afterfuel) {
        this.afterfuel = afterfuel;
    }

    /**
     * 改装資材 弾を取得します。
     * @return 改装資材 弾
     */
    public Integer getAfterbull() {
        return this.afterbull;
    }

    /**
     * 改装資材 弾を設定します。
     * @param afterbull 改装資材 弾
     */
    public void setAfterbull(Integer afterbull) {
        this.afterbull = afterbull;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public Integer getFuelMax() {
        return this.fuelMax;
    }

    /**
     * 燃料を設定します。
     * @param fuelMax 燃料
     */
    public void setFuelMax(Integer fuelMax) {
        this.fuelMax = fuelMax;
    }

    /**
     * 弾を取得します。
     * @return 弾
     */
    public Integer getBullMax() {
        return this.bullMax;
    }

    /**
     * 弾を設定します。
     * @param bullMax 弾
     */
    public void setBullMax(Integer bullMax) {
        this.bullMax = bullMax;
    }

    /**
     * shipgraphを取得します。
     * @return shipgraph
     */
    public String getGraph() {
        return this.graph;
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
        return this.version;
    }

    /**
     * versionを設定します。
     * @param version version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * 艦娘リソースファイルのディレクトリを取得します。
     * @param mst 艦船
     * @return 艦娘リソースファイルのディレクトリ
     */
    public static Path getResourcePathDir(ShipMst mst) {
        String shipDir = mst.getId() + "_" + mst.getName();
        return Paths.get(AppConfig.get().getResourcesDir(), "ships", shipDir);
    }

    /**
     * JsonObjectから{@link ShipMst}を構築します
     *
     * @param json JsonObject
     * @return {@link ShipMst}
     */
    public static ShipMst toShip(JsonObject json) {
        ShipMst bean = new ShipMst();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_sortno", bean::setSortno)
                .setString("api_name", bean::setName)
                .setString("api_yomi", bean::setYomi)
                .setInteger("api_stype", bean::setStype)
                .setInteger("api_afterlv", bean::setAfterlv)
                .setInteger("api_aftershipid", bean::setAftershipid)
                .setInteger("api_afterfuel", bean::setAfterfuel)
                .setInteger("api_afterbull", bean::setAfterbull)
                .setInteger("api_fuel_max", bean::setFuelMax)
                .setInteger("api_bull_max", bean::setBullMax);
        return bean;
    }
}
