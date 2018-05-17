package logbook.internal.gui;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import logbook.bean.Ship;
import logbook.bean.ShipLabelCollection;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.Stype;
import logbook.internal.Items;
import logbook.internal.SeaArea;
import logbook.internal.Ships;

/**
 * 所有艦娘
 *
 */
public class ShipItem {

    /** ID */
    private IntegerProperty id = new SimpleIntegerProperty();

    /** 艦娘 */
    private ObjectProperty<Ship> ship = new SimpleObjectProperty<>();

    /** 艦種 */
    private StringProperty type = new SimpleStringProperty();

    /** Lv */
    private IntegerProperty lv = new SimpleIntegerProperty();

    /** 経験値 */
    private IntegerProperty exp = new SimpleIntegerProperty();

    /** Next */
    private IntegerProperty next = new SimpleIntegerProperty();

    /** cond */
    private IntegerProperty cond = new SimpleIntegerProperty();

    /** ラベル */
    private ObjectProperty<Set<String>> label = new SimpleObjectProperty<>();

    /** 制空 */
    private IntegerProperty seiku = new SimpleIntegerProperty();

    /** 砲戦火力 */
    private IntegerProperty hPower = new SimpleIntegerProperty();

    /** 雷戦火力 */
    private IntegerProperty rPower = new SimpleIntegerProperty();

    /** 夜戦火力 */
    private IntegerProperty yPower = new SimpleIntegerProperty();

    /** 対潜火力 */
    private IntegerProperty tPower = new SimpleIntegerProperty();

    /** 火力 */
    private IntegerProperty karyoku = new SimpleIntegerProperty();

    /** 雷装 */
    private IntegerProperty raisou = new SimpleIntegerProperty();

    /** 対空 */
    private IntegerProperty taiku = new SimpleIntegerProperty();

    /** 対潜 */
    private IntegerProperty tais = new SimpleIntegerProperty();

    /** 索敵 */
    private IntegerProperty sakuteki = new SimpleIntegerProperty();

    /** 運 */
    private IntegerProperty lucky = new SimpleIntegerProperty();

    /** 耐久 */
    private IntegerProperty maxhp = new SimpleIntegerProperty();

    /** 装甲 */
    private IntegerProperty soukou = new SimpleIntegerProperty();

    /** 回避 */
    private IntegerProperty kaihi = new SimpleIntegerProperty();

    /** 速力 */
    private IntegerProperty soku = new SimpleIntegerProperty();

    /** 射程 */
    private IntegerProperty leng = new SimpleIntegerProperty();

    /** 装備1 */
    private IntegerProperty slot1 = new SimpleIntegerProperty();

    /** 装備2 */
    private IntegerProperty slot2 = new SimpleIntegerProperty();

    /** 装備3 */
    private IntegerProperty slot3 = new SimpleIntegerProperty();

    /** 装備4 */
    private IntegerProperty slot4 = new SimpleIntegerProperty();

    /** 装備5 */
    private IntegerProperty slot5 = new SimpleIntegerProperty();

    /** 補強 */
    private IntegerProperty slotEx = new SimpleIntegerProperty();

    /**
     * IDを取得します。
     * @return ID
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    /**
     * IDを取得します。
     * @return ID
     */
    public Integer getId() {
        return this.id.get();
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(Integer id) {
        this.id.set(id);
    }

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public ObjectProperty<Ship> shipProperty() {
        return this.ship;
    }

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public Ship getShip() {
        return this.ship.get();
    }

    /**
     * 艦娘を設定します。
     * @param ship 艦娘
     */
    public void setShip(Ship ship) {
        this.ship.set(ship);
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public StringProperty typeProperty() {
        return this.type;
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public String getType() {
        return this.type.get();
    }

    /**
     * 艦種を設定します。
     * @param type 艦種
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    public IntegerProperty lvProperty() {
        return this.lv;
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    public Integer getLv() {
        return this.lv.get();
    }

    /**
     * Lvを設定します。
     * @param lv Lv
     */
    public void setLv(Integer lv) {
        this.lv.set(lv);
    }

    /**
     * 経験値を取得します。
     * @return 経験値
     */
    public IntegerProperty expProperty() {
        return this.exp;
    }

    /**
     * 経験値を取得します。
     * @return 経験値
     */
    public Integer getExp() {
        return this.exp.get();
    }

    /**
     * 経験値を設定します。
     * @param exp 経験値
     */
    public void setExp(Integer exp) {
        this.exp.set(exp);
    }

    /**
     * Nextを取得します。
     * @return Next
     */
    public IntegerProperty nextProperty() {
        return this.next;
    }

    /**
     * Nextを取得します。
     * @return Next
     */
    public Integer getNext() {
        return this.next.get();
    }

    /**
     * Nextを設定します。
     * @param next Next
     */
    public void setNext(Integer next) {
        this.next.set(next);
    }

    /**
     * condを取得します。
     * @return cond
     */
    public IntegerProperty condProperty() {
        return this.cond;
    }

    /**
     * condを取得します。
     * @return cond
     */
    public Integer getCond() {
        return this.cond.get();
    }

    /**
     * condを設定します。
     * @param cond cond
     */
    public void setCond(Integer cond) {
        this.cond.set(cond);
    }

    /**
     * ラベルを取得します。
     * @return ラベル
     */
    public ObjectProperty<Set<String>> labelProperty() {
        return this.label;
    }

    /**
     * ラベルを取得します。
     * @return ラベル
     */
    public Set<String> getLabel() {
        return this.label.get();
    }

    /**
     * ラベルを設定します。
     * @param label ラベル
     */
    public void setLabel(Set<String> label) {
        this.label.set(label);
    }

    /**
     * 制空を取得します。
     * @return 制空
     */
    public IntegerProperty seikuProperty() {
        return this.seiku;
    }

    /**
     * 制空を取得します。
     * @return 制空
     */
    public Integer getSeiku() {
        return this.seiku.get();
    }

    /**
     * 制空を設定します。
     * @param seiku 制空
     */
    public void setSeiku(Integer seiku) {
        this.seiku.set(seiku);
    }

    /**
     * 砲戦火力を取得します。
     * @return 砲戦火力
     */
    public IntegerProperty hPowerProperty() {
        return this.hPower;
    }

    /**
     * 砲戦火力を取得します。
     * @return 砲戦火力
     */
    public Integer getHPower() {
        return this.hPower.get();
    }

    /**
     * 砲戦火力を設定します。
     * @param hPower 砲戦火力
     */
    public void setHPower(Integer hPower) {
        this.hPower.set(hPower);
    }

    /**
     * 雷戦火力を取得します。
     * @return 雷戦火力
     */
    public IntegerProperty rPowerProperty() {
        return this.rPower;
    }

    /**
     * 雷戦火力を取得します。
     * @return 雷戦火力
     */
    public Integer getRPower() {
        return this.rPower.get();
    }

    /**
     * 雷戦火力を設定します。
     * @param rPower 雷戦火力
     */
    public void setRPower(Integer rPower) {
        this.rPower.set(rPower);
    }

    /**
     * 夜戦火力を取得します。
     * @return 夜戦火力
     */
    public IntegerProperty yPowerProperty() {
        return this.yPower;
    }

    /**
     * 夜戦火力を取得します。
     * @return 夜戦火力
     */
    public Integer getYPower() {
        return this.yPower.get();
    }

    /**
     * 夜戦火力を設定します。
     * @param yPower 夜戦火力
     */
    public void setYPower(Integer yPower) {
        this.yPower.set(yPower);
    }

    /**
     * 対潜火力を取得します。
     * @return 対潜火力
     */
    public IntegerProperty tPowerProperty() {
        return this.tPower;
    }

    /**
     * 対潜火力を取得します。
     * @return 対潜火力
     */
    public Integer getTPower() {
        return this.tPower.get();
    }

    /**
     * 対潜火力を設定します。
     * @param tPower 対潜火力
     */
    public void setTPower(Integer tPower) {
        this.tPower.set(tPower);
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public IntegerProperty karyokuProperty() {
        return this.karyoku;
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public Integer getKaryoku() {
        return this.karyoku.get();
    }

    /**
     * 火力を設定します。
     * @param karyoku 火力
     */
    public void setKaryoku(Integer karyoku) {
        this.karyoku.set(karyoku);
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public IntegerProperty raisouProperty() {
        return this.raisou;
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public Integer getRaisou() {
        return this.raisou.get();
    }

    /**
     * 雷装を設定します。
     * @param raisou 雷装
     */
    public void setRaisou(Integer raisou) {
        this.raisou.set(raisou);
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public IntegerProperty taikuProperty() {
        return this.taiku;
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public Integer getTaiku() {
        return this.taiku.get();
    }

    /**
     * 対空を設定します。
     * @param taiku 対空
     */
    public void setTaiku(Integer taiku) {
        this.taiku.set(taiku);
    }

    /**
     * 対潜を取得します。
     * @return 対潜
     */
    public IntegerProperty taisProperty() {
        return this.tais;
    }

    /**
     * 対潜を取得します。
     * @return 対潜
     */
    public Integer getTais() {
        return this.tais.get();
    }

    /**
     * 対潜を設定します。
     * @param tais 対潜
     */
    public void setTais(Integer tais) {
        this.tais.set(tais);
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public IntegerProperty sakutekiProperty() {
        return this.sakuteki;
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public Integer getSakuteki() {
        return this.sakuteki.get();
    }

    /**
     * 索敵を設定します。
     * @param sakuteki 索敵
     */
    public void setSakuteki(Integer sakuteki) {
        this.sakuteki.set(sakuteki);
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public IntegerProperty luckyProperty() {
        return this.lucky;
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public Integer getLucky() {
        return this.lucky.get();
    }

    /**
     * 運を設定します。
     * @param lucky 運
     */
    public void setLucky(Integer lucky) {
        this.lucky.set(lucky);
    }

    /**
     * 耐久を取得します。
     * @return 耐久
     */
    public IntegerProperty maxhpProperty() {
        return this.maxhp;
    }

    /**
     * 耐久を取得します。
     * @return 耐久
     */
    public Integer getMaxhp() {
        return this.maxhp.get();
    }

    /**
     * 耐久を設定します。
     * @param maxhp 耐久
     */
    public void setMaxhp(Integer maxhp) {
        this.maxhp.set(maxhp);
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public IntegerProperty soukouProperty() {
        return this.soukou;
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public Integer getSoukou() {
        return this.soukou.get();
    }

    /**
     * 装甲を設定します。
     * @param soukou 装甲
     */
    public void setSoukou(Integer soukou) {
        this.soukou.set(soukou);
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public IntegerProperty kaihiProperty() {
        return this.kaihi;
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public Integer getKaihi() {
        return this.kaihi.get();
    }

    /**
     * 回避を設定します。
     * @param kaihi 回避
     */
    public void setKaihi(Integer kaihi) {
        this.kaihi.set(kaihi);
    }

    /**
     * 速力を取得します。
     * @return 速力
     */
    public IntegerProperty sokuProperty() {
        return this.soku;
    }

    /**
     * 速力を取得します。
     * @return 速力
     */
    public Integer getSoku() {
        return this.soku.get();
    }

    /**
     * 速力を設定します。
     * @param soku 速力
     */
    public void setSoku(Integer soku) {
        this.soku.set(soku);
    }

    /**
     * 射程を取得します。
     * @return 射程
     */
    public IntegerProperty lengProperty() {
        return this.leng;
    }

    /**
     * 射程を取得します。
     * @return 射程
     */
    public Integer getLeng() {
        return this.leng.get();
    }

    /**
     * 射程を設定します。
     * @param leng 射程
     */
    public void setLeng(Integer leng) {
        this.leng.set(leng);
    }

    /**
     * 装備1を取得します。
     * @return 装備1
     */
    public IntegerProperty slot1Property() {
        return this.slot1;
    }

    /**
     * 装備1を取得します。
     * @return 装備1
     */
    public Integer getSlot1() {
        return this.slot1.get();
    }

    /**
     * 装備1を設定します。
     * @param slot1 装備1
     */
    public void setSlot1(Integer slot1) {
        this.slot1.set(slot1);
    }

    /**
     * 装備2を取得します。
     * @return 装備2
     */
    public IntegerProperty slot2Property() {
        return this.slot2;
    }

    /**
     * 装備2を取得します。
     * @return 装備2
     */
    public Integer getSlot2() {
        return this.slot2.get();
    }

    /**
     * 装備2を設定します。
     * @param slot2 装備2
     */
    public void setSlot2(Integer slot2) {
        this.slot2.set(slot2);
    }

    /**
     * 装備3を取得します。
     * @return 装備3
     */
    public IntegerProperty slot3Property() {
        return this.slot3;
    }

    /**
     * 装備3を取得します。
     * @return 装備3
     */
    public Integer getSlot3() {
        return this.slot3.get();
    }

    /**
     * 装備3を設定します。
     * @param slot3 装備3
     */
    public void setSlot3(Integer slot3) {
        this.slot3.set(slot3);
    }

    /**
     * 装備4を取得します。
     * @return 装備4
     */
    public IntegerProperty slot4Property() {
        return this.slot4;
    }

    /**
     * 装備4を取得します。
     * @return 装備4
     */
    public Integer getSlot4() {
        return this.slot4.get();
    }

    /**
     * 装備4を設定します。
     * @param slot4 装備4
     */
    public void setSlot4(Integer slot4) {
        this.slot4.set(slot4);
    }

    /**
     * 装備5を取得します。
     * @return 装備5
     */
    public IntegerProperty slot5Property() {
        return this.slot5;
    }

    /**
     * 装備5を取得します。
     * @return 装備5
     */
    public Integer getSlot5() {
        return this.slot5.get();
    }

    /**
     * 装備5を設定します。
     * @param slot5 装備5
     */
    public void setSlot5(Integer slot5) {
        this.slot5.set(slot5);
    }

    /**
     * 補強を取得します。
     * @return 補強
     */
    public IntegerProperty slotExProperty() {
        return this.slotEx;
    }

    /**
     * 補強を取得します。
     * @return 補強
     */
    public Integer getSlotEx() {
        return this.slotEx.get();
    }

    /**
     * 補強を設定します。
     * @param slotEx 補強
     */
    public void setSlotEx(Integer slotEx) {
        this.slotEx.set(slotEx);
    }

    @Override
    public String toString() {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();

        // 装備の名前
        Function<Integer, String> slotItemName = itemId -> {
            return Items.name(itemMap.get(itemId));
        };

        return new StringJoiner("\t")
                .add(Integer.toString(this.id.get()))
                .add(Optional.ofNullable(this.ship.get())
                        .map(s -> Ships.shipMst(s).map(ShipMst::getName).orElse(""))
                        .orElse(""))
                .add(this.type.get())
                .add(Integer.toString(this.lv.get()))
                .add(Integer.toString(this.exp.get()))
                .add(Integer.toString(this.next.get()))
                .add(Integer.toString(this.cond.get()))
                .add(this.label.get().stream().collect(Collectors.joining(" ")))
                .add(Integer.toString(this.seiku.get()))
                .add(Integer.toString(this.hPower.get()))
                .add(Integer.toString(this.rPower.get()))
                .add(Integer.toString(this.yPower.get()))
                .add(Integer.toString(this.tPower.get()))
                .add(Integer.toString(this.karyoku.get()))
                .add(Integer.toString(this.raisou.get()))
                .add(Integer.toString(this.taiku.get()))
                .add(Integer.toString(this.tais.get()))
                .add(Integer.toString(this.sakuteki.get()))
                .add(Integer.toString(this.lucky.get()))
                .add(Integer.toString(this.maxhp.get()))
                .add(Integer.toString(this.soukou.get()))
                .add(Integer.toString(this.kaihi.get()))
                .add(Ships.sokuText(this.soku.get()))
                .add(Ships.lengText(this.leng.get()))
                .add(slotItemName.apply(this.slot1.get()))
                .add(slotItemName.apply(this.slot2.get()))
                .add(slotItemName.apply(this.slot3.get()))
                .add(slotItemName.apply(this.slot4.get()))
                .add(slotItemName.apply(this.slot5.get()))
                .add(slotItemName.apply(this.slotEx.get()))
                .toString();
    }

    /**
     * 艦娘から所有艦娘を生成します
     *
     * @param ship 艦娘
     * @return 所有艦娘
     */
    public static ShipItem toShipItem(Ship ship) {
        String type = Ships.stype(ship)
                .map(Stype::getName)
                .orElse("");
        ShipItem shipItem = new ShipItem();
        shipItem.setId(ship.getId());
        shipItem.setShip(ship);
        shipItem.setType(type);
        shipItem.setLv(ship.getLv());
        shipItem.setExp(ship.getExp().get(0));
        shipItem.setNext(ship.getExp().get(1));
        shipItem.setCond(ship.getCond());
        Set<String> label = new LinkedHashSet<>();
        SeaArea area = SeaArea.fromArea(ship.getSallyArea());
        if (area != null) {
            label.add(area.toString());
        }
        Set<String> shipLabels = ShipLabelCollection.get()
                .getLabels()
                .get(ship.getId());
        if (shipLabels != null) {
            label.addAll(shipLabels);
        }

        shipItem.setLabel(FXCollections.observableSet(label));
        shipItem.setSeiku(Ships.airSuperiority(ship));
        shipItem.setHPower(Ships.hPower(ship));
        shipItem.setRPower(Ships.rPower(ship));
        shipItem.setYPower(Ships.yPower(ship));
        shipItem.setTPower(Ships.tPower(ship));

        shipItem.setKaryoku(ship.getKaryoku().get(0) - sumItemParam(ship, SlotitemMst::getHoug));
        shipItem.setRaisou(ship.getRaisou().get(0) - sumItemParam(ship, SlotitemMst::getRaig));
        shipItem.setTaiku(ship.getTaiku().get(0) - sumItemParam(ship, SlotitemMst::getTyku));
        shipItem.setTais(ship.getTaisen().get(0) - sumItemParam(ship, SlotitemMst::getTais));
        shipItem.setSakuteki(ship.getSakuteki().get(0) - sumItemParam(ship, SlotitemMst::getSaku));
        shipItem.setLucky(ship.getLucky().get(0) - sumItemParam(ship, SlotitemMst::getLuck));
        shipItem.setMaxhp(ship.getMaxhp());
        shipItem.setSoukou(ship.getSoukou().get(0) - sumItemParam(ship, SlotitemMst::getSouk));
        shipItem.setKaihi(ship.getKaihi().get(0) - sumItemParam(ship, SlotitemMst::getHouk));
        if (ship.getSoku() != null)
            shipItem.setSoku(ship.getSoku());
        shipItem.setLeng(ship.getLeng());

        int slotNum = ship.getSlotnum();
        shipItem.setSlot1(ship.getSlot().get(0) == -1 && slotNum <= 0 ? 0 : ship.getSlot().get(0));
        shipItem.setSlot2(ship.getSlot().get(1) == -1 && slotNum <= 1 ? 0 : ship.getSlot().get(1));
        shipItem.setSlot3(ship.getSlot().get(2) == -1 && slotNum <= 2 ? 0 : ship.getSlot().get(2));
        shipItem.setSlot4(ship.getSlot().get(3) == -1 && slotNum <= 3 ? 0 : ship.getSlot().get(3));
        shipItem.setSlot5(ship.getSlot().get(4) == -1 && slotNum <= 4 ? 0 : ship.getSlot().get(4));
        shipItem.setSlotEx(ship.getSlotEx());

        return shipItem;
    }

    /**
     * 装備のパラメータを合計する
     * @param ship 艦娘
     * @param mapper 合計するパラメータを返す mapper
     * @return
     */
    private static int sumItemParam(Ship ship, Function<SlotitemMst, Integer> mapper) {
        Map<Integer, SlotItem> items = SlotItemCollection.get().getSlotitemMap();
        return Stream.concat(ship.getSlot().stream(), Stream.of(ship.getSlotEx()))
                .map(items::get)
                .map(Items::slotitemMst)
                .mapToInt(e -> e.map(mapper).orElse(0))
                .sum();
    }
}
