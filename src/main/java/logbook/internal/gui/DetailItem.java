package logbook.internal.gui;

import java.util.Optional;
import java.util.function.Predicate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;

/**
 * 所有装備一覧の詳細
 *
 */
public class DetailItem {

    /** 装備ID */
    private Integer id;

    /** 改修Lv */
    private StringProperty level;

    /** 熟練度 */
    private StringProperty alv;

    /** 所持艦娘 */
    private ObjectProperty<Ship> ship;

    /** 所持艦娘ID */
    private Integer shipId;

    /**
     * 装備IDを取得します。
     * @return 装備ID
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 装備IDを設定します。
     * @param id 装備ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 改修Lvを取得します。
     * @return 改修Lv
     */
    public StringProperty levelProperty() {
        return this.level;
    }

    /**
     * 改修Lvを設定します。
     * @param level 改修Lv
     */
    public void setLevel(String level) {
        this.level = new SimpleStringProperty(level);
    }

    /**
     * 熟練度を取得します。
     * @return 熟練度
     */
    public StringProperty alvProperty() {
        return this.alv;
    }

    /**
     * 熟練度を設定します。
     * @param alv 熟練度
     */
    public void setAlv(String alv) {
        this.alv = new SimpleStringProperty(alv);
    }

    /**
     * 所持艦娘を取得します。
     * @return 所持艦娘
     */
    public ObjectProperty<Ship> shipProperty() {
        return this.ship;
    }

    /**
     * 所持艦娘を設定します。
     * @param ship 所持艦娘
     */
    public void setShip(Ship ship) {
        this.ship = new SimpleObjectProperty<>(ship);
    }

    /**
     * 所持艦娘IDを取得します。
     * @return 所持艦娘ID
     */
    public Integer getShipId() {
        return this.shipId;
    }

    /**
     * 所持艦娘IDを設定します。
     * @param shipId 所持艦娘ID
     */
    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    /**
     * 装備から装備詳細を生成します
     *
     * @param item 装備
     * @return 装備詳細
     */
    public static DetailItem toDetailItem(SlotItem item) {
        Integer id = item.getId();
        DetailItem detail = new DetailItem();
        detail.setId(id);
        if (item.getLevel() > 0) {
            detail.setLevel("★+" + item.getLevel());
        } else {
            detail.setLevel("");
        }
        if (item.getAlv() != null) {
            detail.setAlv("☆+" + item.getAlv());
        } else {
            detail.setAlv("");
        }
        // 装備している艦娘を探す
        Predicate<Ship> filter = e -> e.getSlot().contains(id) || id.equals(e.getSlotEx());
        Optional<Ship> op = ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .filter(filter)
                .findAny();
        if (op.isPresent()) {
            Ship ship = op.get();
            // 艦娘
            detail.setShip(ship);
            // 艦娘ID
            detail.setShipId(ship.getId());
        }
        return detail;
    }
}
