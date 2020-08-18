package logbook.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import logbook.internal.JsonHelper;
import logbook.internal.Ships;
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
     * 大破した艦娘を返します
     *
     * @return 大破した艦娘
     */
    @JsonIgnore
    public List<Ship> getBadlyShips() {
        return getBadlyShips(false);
    }
    
    /**
     * 大破した艦娘を返します
     *
     * @return 大破した艦娘
     */
    public List<Ship> getBadlyShips(boolean ignoreFlagship) {
        Map<Integer, Ship> shipMap = ShipCollection.get().getShipMap();
        return this.getShip()
                .stream()
                .map(shipMap::get)
                .skip(ignoreFlagship ? 1:0)
                .filter(Objects::nonNull)
                .filter(Ships::isBadlyDamage)
                .filter(s -> !Ships.isEscape(s))
                .collect(Collectors.toList());
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
                .setLongList("api_mission", bean::setMission)
                .setString("api_name", bean::setName)
                .setIntegerList("api_ship", bean::setShip);
        return bean;
    }

}
