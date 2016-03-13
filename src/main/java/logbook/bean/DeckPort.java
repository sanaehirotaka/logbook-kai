package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_deck_port
 *
 */
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

    /**
     * api_flagshipを取得します。
     * @return api_flagship
     */
    public Integer getFlagship() {
        return this.flagship;
    }

    /**
     * api_flagshipを設定します。
     * @param flagship api_flagship
     */
    public void setFlagship(Integer flagship) {
        this.flagship = flagship;
    }

    /**
     * api_idを取得します。
     * @return api_id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * api_idを設定します。
     * @param id api_id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * api_missionを取得します。
     * @return api_mission
     */
    public List<Long> getMission() {
        return this.mission;
    }

    /**
     * api_missionを設定します。
     * @param mission api_mission
     */
    public void setMission(List<Long> mission) {
        this.mission = mission;
    }

    /**
     * api_nameを取得します。
     * @return api_name
     */
    public String getName() {
        return this.name;
    }

    /**
     * api_nameを設定します。
     * @param name api_name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * api_shipを取得します。
     * @return api_ship
     */
    public List<Integer> getShip() {
        return this.ship;
    }

    /**
     * api_shipを設定します。
     * @param ship api_ship
     */
    public void setShip(List<Integer> ship) {
        this.ship = ship;
    }

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
