package logbook.internal.gui;

import java.util.Optional;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.StypeCollection;
import logbook.internal.Ships;

/**
 * 所有艦娘
 *
 */
public class ShipItem {

    /** ID */
    private IntegerProperty id;

    /** 艦娘 */
    private ObjectProperty<Ship> ship;

    /** 艦種 */
    private StringProperty type;

    /** Lv */
    private IntegerProperty lv;

    /** cond */
    private IntegerProperty cond;

    /** 海域 */
    private StringProperty area;

    /** 制空 */
    private IntegerProperty seiku;

    /** 砲戦火力 */
    private IntegerProperty hPower;

    /** 雷戦火力 */
    private IntegerProperty rPower;

    /** 夜戦火力 */
    private IntegerProperty yPower;

    /** 対潜火力 */
    private IntegerProperty tPower;

    /** 装備1 */
    private IntegerProperty slot1;

    /** 装備2 */
    private IntegerProperty slot2;

    /** 装備3 */
    private IntegerProperty slot3;

    /** 装備4 */
    private IntegerProperty slot4;

    /** 補強 */
    private IntegerProperty slotEx;

    /**
     * IDを取得します。
     * @return ID
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    /**
     * IDを設定します。
     * @param lv ID
     */
    public void setId(Integer id) {
        this.id = new SimpleIntegerProperty(id);
    }

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public ObjectProperty<Ship> shipProperty() {
        return this.ship;
    }

    /**
     * 艦娘を設定します。
     * @param id 艦娘
     */
    public void setShip(Ship ship) {
        this.ship = new SimpleObjectProperty<>(ship);
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public StringProperty typeProperty() {
        return this.type;
    }

    /**
     * 艦種を設定します。
     * @param type 艦種
     */
    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    public IntegerProperty lvProperty() {
        return this.lv;
    }

    /**
     * Lvを設定します。
     * @param lv Lv
     */
    public void setLv(Integer lv) {
        this.lv = new SimpleIntegerProperty(lv);
    }

    /**
     * condを取得します。
     * @return cond
     */
    public IntegerProperty condProperty() {
        return this.cond;
    }

    /**
     * condを設定します。
     * @param cond cond
     */
    public void setCond(Integer cond) {
        this.cond = new SimpleIntegerProperty(cond);
    }

    /**
     * 海域を取得します。
     * @return 海域
     */
    public StringProperty areaProperty() {
        return this.area;
    }

    /**
     * 海域を設定します。
     * @param area 海域
     */
    public void setArea(String area) {
        this.area = new SimpleStringProperty(area);
    }

    /**
     * 制空を取得します。
     * @return 制空
     */
    public IntegerProperty seikuProperty() {
        return this.seiku;
    }

    /**
     * 制空を設定します。
     * @param seiku 制空
     */
    public void setSeiku(Integer seiku) {
        this.seiku = new SimpleIntegerProperty(seiku);
    }

    /**
     * 砲戦火力を取得します。
     * @return 砲戦火力
     */
    public IntegerProperty hPowerProperty() {
        return this.hPower;
    }

    /**
     * 砲戦火力を設定します。
     * @param hPower 砲戦火力
     */
    public void sethPower(Integer hPower) {
        this.hPower = new SimpleIntegerProperty(hPower);
    }

    /**
     * 雷戦火力を取得します。
     * @return 雷戦火力
     */
    public IntegerProperty rPowerProperty() {
        return this.rPower;
    }

    /**
     * 雷戦火力を設定します。
     * @param rPower 雷戦火力
     */
    public void setrPower(Integer rPower) {
        this.rPower = new SimpleIntegerProperty(rPower);
    }

    /**
     * 夜戦火力を取得します。
     * @return 夜戦火力
     */
    public IntegerProperty yPowerProperty() {
        return this.yPower;
    }

    /**
     * 夜戦火力を設定します。
     * @param yPower 夜戦火力
     */
    public void setyPower(Integer yPower) {
        this.yPower = new SimpleIntegerProperty(yPower);
    }

    /**
     * 対潜火力を取得します。
     * @return 対潜火力
     */
    public IntegerProperty tPowerProperty() {
        return this.tPower;
    }

    /**
     * 対潜火力を設定します。
     * @param tPower 対潜火力
     */
    public void settPower(Integer tPower) {
        this.tPower = new SimpleIntegerProperty(tPower);
    }

    /**
     * 装備1を取得します。
     * @return 装備1
     */
    public IntegerProperty slot1Property() {
        return this.slot1;
    }

    /**
     * 装備1を設定します。
     * @param slot1 装備1
     */
    public void setSlot1(Integer slot1) {
        this.slot1 = new SimpleIntegerProperty(slot1);
    }

    /**
     * 装備2を取得します。
     * @return 装備2
     */
    public IntegerProperty slot2Property() {
        return this.slot2;
    }

    /**
     * 装備2を設定します。
     * @param slot2 装備2
     */
    public void setSlot2(Integer slot2) {
        this.slot2 = new SimpleIntegerProperty(slot2);
    }

    /**
     * 装備3を取得します。
     * @return 装備3
     */
    public IntegerProperty slot3Property() {
        return this.slot3;
    }

    /**
     * 装備3を設定します。
     * @param slot3 装備3
     */
    public void setSlot3(Integer slot3) {
        this.slot3 = new SimpleIntegerProperty(slot3);
    }

    /**
     * 装備4を取得します。
     * @return 装備4
     */
    public IntegerProperty slot4Property() {
        return this.slot4;
    }

    /**
     * 装備4を設定します。
     * @param slot4 装備4
     */
    public void setSlot4(Integer slot4) {
        this.slot4 = new SimpleIntegerProperty(slot4);
    }

    /**
     * 補強を取得します。
     * @return 補強
     */
    public IntegerProperty slotExProperty() {
        return this.slotEx;
    }

    /**
     * 補強を設定します。
     * @param slotEx 補強
     */
    public void setSlotEx(Integer slotEx) {
        this.slotEx = new SimpleIntegerProperty(slotEx);
    }

    /**
     * 艦娘から所有艦娘を生成します
     *
     * @param ship 艦娘
     * @return 所有艦娘
     */
    public static ShipItem toShipItem(Ship ship) {
        Optional<ShipMst> mst = Ships.shipMst(ship);
        String type;
        if (mst.isPresent()) {
            type = StypeCollection.get()
                    .getStypeMap()
                    .get(mst.get()
                            .getStype())
                    .getName();
        } else {
            type = "";
        }
        ShipItem shipItem = new ShipItem();
        shipItem.setId(ship.getId());
        shipItem.setShip(ship);
        shipItem.setType(type);
        shipItem.setLv(ship.getLv());
        shipItem.setCond(ship.getCond());
        shipItem.setArea(""); // TODO: 海域
        shipItem.setSeiku(0); // TODO: 制空値計算
        shipItem.sethPower(0); // TODO: 火力計算
        shipItem.setrPower(0);
        shipItem.setyPower(0);
        shipItem.settPower(0);
        shipItem.setSlot1(ship.getSlot().get(0));
        shipItem.setSlot2(ship.getSlot().get(1));
        shipItem.setSlot3(ship.getSlot().get(2));
        shipItem.setSlot4(ship.getSlot().get(3));
        shipItem.setSlotEx(ship.getSlotEx());
        return shipItem;
    }
}
