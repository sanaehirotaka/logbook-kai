package logbook.internal.gui;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Predicate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import logbook.Messages;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.internal.Ships;

/**
 * 所有装備一覧の詳細
 *
 */
public class DetailItem {

    /** 装備ID */
    private Integer id;

    /** 熟練度 */
    private IntegerProperty alv;

    /** 改修Lv */
    private IntegerProperty level;

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
     * 熟練度を取得します。
     * @return 熟練度
     */
    public IntegerProperty alvProperty() {
        return this.alv;
    }

    /**
     * 熟練度を設定します。
     * @param alv 熟練度
     */
    public void setAlv(Integer alv) {
        this.alv = new SimpleIntegerProperty(alv);
    }

    /**
     * 改修Lvを取得します。
     * @return 改修Lv
     */
    public IntegerProperty levelProperty() {
        return this.level;
    }

    /**
     * 改修Lvを設定します。
     * @param level 改修Lv
     */
    public void setLevel(Integer level) {
        this.level = new SimpleIntegerProperty(level);
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

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(Optional.ofNullable(this.alv.get())
                        .filter(v -> v > 0)
                        .map(v -> Messages.getString("item.alv", v)) //$NON-NLS-1$
                        .orElse(""))
                .add(Optional.ofNullable(this.level.get())
                        .filter(v -> v > 0)
                        .map(v -> Messages.getString("item.level", v)) //$NON-NLS-1$
                        .orElse(""))
                .add(Optional.ofNullable(this.ship.get())
                        .map(Ships::toName)
                        .orElse("未所持"))
                .toString();
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

        // 熟練
        detail.setAlv(Optional.ofNullable(item.getAlv()).orElse(0));
        // 改修
        detail.setLevel(Optional.ofNullable(item.getLevel()).orElse(0));
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
        } else {
            // 艦娘
            detail.setShip(null);
            // 艦娘ID
            detail.setShipId(null);
        }
        return detail;
    }
}
