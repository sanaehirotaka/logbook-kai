package logbook.bean;

import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 艦娘画像設定
 *
 */
@Data
public class Shipgraph {

    /** api_id */
    private Integer id;

    /** api_sortno */
    private Integer sortno;

    /** api_battle_d */
    private List<Integer> battleD;

    /** api_battle_n */
    private List<Integer> battleN;

    /** api_boko_d */
    private List<Integer> bokoD;

    /** api_boko_n */
    private List<Integer> bokoN;

    /** api_ensyue_n */
    private List<Integer> ensyueN;

    /** api_ensyuf_d */
    private List<Integer> ensyufD;

    /** api_ensyuf_n */
    private List<Integer> ensyufN;

    /** api_filename */
    private String filename;

    /** api_kaisyu_d */
    private List<Integer> kaisyuD;

    /** api_kaisyu_n */
    private List<Integer> kaisyuN;

    /** api_kaizo_d */
    private List<Integer> kaizoD;

    /** api_kaizo_n */
    private List<Integer> kaizoN;

    /** api_map_d */
    private List<Integer> mapD;

    /** api_map_n */
    private List<Integer> mapN;

    /** api_pa */
    private List<Integer> pa;

    /** api_version */
    private List<String> version;

    /** api_weda */
    private List<Integer> weda;

    /** api_wedb */
    private List<Integer> wedb;

    public static Shipgraph toShipgraph(JsonObject json) {
        Shipgraph bean = new Shipgraph();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_sortno", bean::setSortno)
                .set("api_battle_d", bean::setBattleD, JsonHelper::toIntegerList)
                .set("api_battle_n", bean::setBattleN, JsonHelper::toIntegerList)
                .set("api_boko_d", bean::setBokoD, JsonHelper::toIntegerList)
                .set("api_boko_n", bean::setBokoN, JsonHelper::toIntegerList)
                .set("api_ensyue_n", bean::setEnsyueN, JsonHelper::toIntegerList)
                .set("api_ensyuf_d", bean::setEnsyufD, JsonHelper::toIntegerList)
                .set("api_ensyuf_n", bean::setEnsyufN, JsonHelper::toIntegerList)
                .setString("api_filename", bean::setFilename)
                .set("api_kaisyu_d", bean::setKaisyuD, JsonHelper::toIntegerList)
                .set("api_kaisyu_n", bean::setKaisyuN, JsonHelper::toIntegerList)
                .set("api_kaizo_d", bean::setKaizoD, JsonHelper::toIntegerList)
                .set("api_kaizo_n", bean::setKaizoN, JsonHelper::toIntegerList)
                .set("api_map_d", bean::setMapD, JsonHelper::toIntegerList)
                .set("api_map_n", bean::setMapN, JsonHelper::toIntegerList)
                .set("api_pa", bean::setPa, JsonHelper::toIntegerList)
                .set("api_version", bean::setVersion, JsonHelper::toStringList)
                .set("api_weda", bean::setWeda, JsonHelper::toIntegerList)
                .set("api_wedb", bean::setWedb, JsonHelper::toIntegerList);

        return bean;
    }
}
