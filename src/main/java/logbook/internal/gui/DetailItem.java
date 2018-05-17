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
import lombok.Getter;
import lombok.Setter;

/**
 * 所有装備一覧の詳細
 *
 */
public class DetailItem {

    /** 装備ID */
    @Getter
    @Setter
    private Integer id;

    /** 熟練度 */
    private IntegerProperty alv = new SimpleIntegerProperty();

    /** 改修Lv */
    private IntegerProperty level = new SimpleIntegerProperty();

    /** 所持艦娘 */
    private ObjectProperty<Ship> ship = new SimpleObjectProperty<Ship>();

    /** 所持艦娘ID */
    @Getter
    @Setter
    private Integer shipId;

    /**
     * 熟練度を取得します。
     * @return 熟練度
     */
    public IntegerProperty alvProperty() {
        return this.alv;
    }

    /**
     * 熟練度を取得します。
     * @return 熟練度
     */
    public Integer getAlv() {
        return this.alv.get();
    }

    /**
     * 熟練度を設定します。
     * @param alv 熟練度
     */
    public void setAlv(Integer alv) {
        this.alv.set(alv);
    }

    /**
     * 改修Lvを取得します。
     * @return 改修Lv
     */
    public IntegerProperty levelProperty() {
        return this.level;
    }

    /**
     * 改修Lvを取得します。
     * @return 改修Lv
     */
    public Integer getLevel() {
        return this.level.get();
    }

    /**
     * 改修Lvを設定します。
     * @param level 改修Lv
     */
    public void setLevel(Integer level) {
        this.level.set(level);
    }

    /**
     * 所持艦娘を取得します。
     * @return 所持艦娘
     */
    public ObjectProperty<Ship> shipProperty() {
        return this.ship;
    }

    /**
     * 所持艦娘を取得します。
     * @return 所持艦娘
     */
    public Ship getShip() {
        return this.ship.get();
    }

    /**
     * 所持艦娘を設定します。
     * @param ship 所持艦娘
     */
    public void setShip(Ship ship) {
        this.ship.set(ship);
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
