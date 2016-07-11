package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_deck_port
 *
 */
@Data
public class DeckPort implements Serializable, Cloneable {

    private static final long serialVersionUID = -7415061750561409381L;

    /** api_flagship */
    private Integer flagship;

    /** api_id */
    private Integer id;

    /** api_mission */
    private List<Long> mission;

    /** api_name */
    private String name;

    /** api_ship */
    private List<Integer> ship;

    @Override
    public DeckPort clone() {
        try {
            return (DeckPort) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * JsonObjectから{@link DeckPort}を構築します
     *
     * @param json JsonObject
     * @return {@link DeckPort}
     */
    public static DeckPort toDeckPort(JsonObject json) {
        DeckPort bean = new DeckPort();
        JsonHelper.bind(json)
                .setInteger("api_flagship", bean::setFlagship)
                .setInteger("api_id", bean::setId)
                .set("api_mission", bean::setMission, JsonHelper::toLongList)
                .setString("api_name", bean::setName)
                .set("api_ship", bean::setShip, JsonHelper::toIntegerList);
        return bean;
    }
}
