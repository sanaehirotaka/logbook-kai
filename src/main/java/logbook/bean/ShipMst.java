package logbook.bean;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 艦娘の名前と種別を表します
 *
 */
@Data
public class ShipMst implements Serializable {

    private static final long serialVersionUID = 4329488719132098164L;

    /** id */
    private Integer id;

    /** 図鑑番号 */
    private Integer sortno;

    /** 名前 */
    private String name;

    /** ふりがな/flagship */
    private String yomi;

    /** 艦種 */
    private Integer stype;

    /** 艦型 */
    private Integer ctype;

    /** 改レベル */
    private Integer afterlv;

    /** 改装後id */
    private Integer aftershipid;

    /** スロット数 */
    private Integer slotNum;

    /** 搭載機数 */
    private List<Integer> maxeq;

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
                .setInteger("api_ctype", bean::setCtype)
                .setInteger("api_afterlv", bean::setAfterlv)
                .setInteger("api_slot_num", bean::setSlotNum)
                .set("api_maxeq", bean::setMaxeq, JsonHelper::toIntegerList)
                .setInteger("api_aftershipid", bean::setAftershipid)
                .setInteger("api_afterfuel", bean::setAfterfuel)
                .setInteger("api_afterbull", bean::setAfterbull)
                .setInteger("api_fuel_max", bean::setFuelMax)
                .setInteger("api_bull_max", bean::setBullMax);
        return bean;
    }
}
