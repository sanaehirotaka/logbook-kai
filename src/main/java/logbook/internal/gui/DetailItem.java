package logbook.internal.gui;

import java.util.Optional;
import java.util.function.Predicate;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipDescription;
import logbook.bean.SlotItem;
import logbook.internal.Ships;

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

    /** 所持艦娘名 */
    private StringProperty name;

    /** 所持艦娘ID */
    private Integer ship = 0;

    /**
     * 自身を取得します。
     * @return 自身
     */
    public Property<DetailItem> thisProperty() {
        return new SimpleObjectProperty<>(this);
    }

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
     * 所持艦娘名を取得します。
     * @return 所持艦娘名
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * 所持艦娘名を設定します。
     * @param name 所持艦娘名
     */
    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * 所持艦娘IDを取得します。
     * @return 所持艦娘ID
     */
    public Integer getShip() {
        return this.ship;
    }

    /**
     * 所持艦娘IDを設定します。
     * @param ship 所持艦娘ID
     */
    public void setShip(Integer ship) {
        this.ship = ship;
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
            // 艦娘
            Ship ship = op.get();
            // 艦船
            Optional<ShipDescription> desc = Ships.shipDescription(ship);

            detail.setShip(ship.getId());
            if (desc.isPresent()) {
                detail.setName(desc.get().getName() + "(Lv" + ship.getLv() + ")");
            } else {
                detail.setName("");
            }
        } else {
            detail.setName("未装備");
        }
        return detail;
    }
}
