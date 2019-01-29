package logbook.bean;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import logbook.internal.ShipType;
import logbook.internal.SlotItemType;
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

    /** api_taik */
    private List<Integer> taik;

    /** api_souk */
    private List<Integer> souk;

    /** api_houg */
    private List<Integer> houg;

    /** api_raig */
    private List<Integer> raig;

    /** api_tyku */
    private List<Integer> tyku;

    /** api_tais */
    private List<Integer> tais;

    /** api_luck */
    private List<Integer> luck;

    /** api_soku */
    private Integer soku;

    /** api_leng */
    private Integer leng;

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
     * この艦船定義が指定された艦種であるかを返します。
     * @param shipType 艦種定数
     * @return この艦船定義が指定された艦種である場合true
     */
    public boolean is(ShipType shipType) {
        if (this.getStype() == null)
            return false;
        return shipType.getStype() == this.getStype();
    }

    /**
     * この艦船定義が指定された艦種であるかを返します。
     * @param shipType1 艦種定数
     * @param shipType2 艦種定数
     * @return この艦船定義が指定された艦種である場合true
     */
    public boolean is(ShipType shipType1, ShipType shipType2) {
        if (this.getStype() == null)
            return false;
        int stype = this.getStype();
        return shipType1.getStype() == stype || shipType2.getStype() == stype;
    }

    /**
     * この艦船定義が指定された艦種であるかを返します。
     * @param shipTypes 艦種定数
     * @return この艦船定義が指定された艦種である場合true
     */
    public boolean is(ShipType... shipTypes) {
        if (this.getStype() == null)
            return false;
        int stype = this.getStype();
        for (ShipType shipType : shipTypes) {
            if (shipType.getStype() == stype) {
                return true;
            }
        }
        return false;
    }

    /**
     * この装備定義から{@link SlotItemType}を返します。
     * 
     * @return {@link SlotItemType}
     */
    public ShipType asShipType() {
        return ShipType.toShipType(this);
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
                .set("api_taik", bean::setTaik, JsonHelper::toIntegerList)
                .set("api_souk", bean::setSouk, JsonHelper::toIntegerList)
                .set("api_houg", bean::setHoug, JsonHelper::toIntegerList)
                .set("api_raig", bean::setRaig, JsonHelper::toIntegerList)
                .set("api_tyku", bean::setTyku, JsonHelper::toIntegerList)
                .set("api_tais", bean::setTais, JsonHelper::toIntegerList)
                .set("api_luck", bean::setLuck, JsonHelper::toIntegerList)
                .setInteger("api_soku", bean::setSoku)
                .setInteger("api_leng", bean::setLeng)
                .setInteger("api_afterfuel", bean::setAfterfuel)
                .setInteger("api_afterbull", bean::setAfterbull)
                .setInteger("api_fuel_max", bean::setFuelMax)
                .setInteger("api_bull_max", bean::setBullMax);
        return bean;
    }
}
