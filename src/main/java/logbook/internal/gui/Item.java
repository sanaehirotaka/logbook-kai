package logbook.internal.gui;

import java.util.Comparator;
import java.util.StringJoiner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemEquiptypeCollection;
import logbook.bean.SlotitemMst;

/**
 * 所有装備
 *
 */
public class Item implements Comparable<Item> {

    /** 装備定義 */
    private IntegerProperty id;

    /** ソート用 種別 */
    private Integer type3;

    /** 名称 */
    private String name;

    /** 種別 */
    private StringProperty type;

    /** 個数 */
    private IntegerProperty count;

    /** 火力 */
    private IntegerProperty houg;

    /** 命中 */
    private IntegerProperty houm;

    /** 射程 */
    private IntegerProperty leng;

    /** 運 */
    private IntegerProperty luck;

    /** 回避 */
    private IntegerProperty houk;

    /** 爆装 */
    private IntegerProperty baku;

    /** 雷装 */
    private IntegerProperty raig;

    /** 索敵 */
    private IntegerProperty saku;

    /** 対潜 */
    private IntegerProperty tais;

    /** 対空 */
    private IntegerProperty tyku;

    /** 装甲 */
    private IntegerProperty souk;

    /**
     * 装備定義を取得します。
     * @return 装備定義
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    /**
     * 装備定義を設定します。
     * @param id 装備定義
     */
    public void setId(Integer id) {
        this.id = new SimpleIntegerProperty(id);
    }

    /**
     * ソート用 種別を取得します。
     * @return ソート用 種別
     */
    public Integer getType3() {
        return this.type3;
    }

    /**
     * ソート用 種別を設定します。
     * @param type3 ソート用 種別
     */
    public void setType3(Integer type3) {
        this.type3 = type3;
    }

    /**
     * 名称を取得します。
     * @return 名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名称を設定します。
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 種別を取得します。
     * @return 種別
     */
    public StringProperty typeProperty() {
        return this.type;
    }

    /**
     * 種別を設定します。
     * @param type 種別
     */
    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }

    /**
     * 個数を取得します。
     * @return 個数
     */
    public IntegerProperty countProperty() {
        return this.count;
    }

    /**
     * 個数を取得します。
     * @return 個数
     */
    public Integer getCount() {
        return this.count.get();
    }

    /**
     * 個数を設定します。
     * @param count 個数
     */
    public void setCount(Integer count) {
        this.count = new SimpleIntegerProperty(count);
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public IntegerProperty hougProperty() {
        return this.houg;
    }

    /**
     * 火力を設定します。
     * @param houg 火力
     */
    public void setHoug(Integer houg) {
        this.houg = new SimpleIntegerProperty(houg);
    }

    /**
     * 命中を取得します。
     * @return 命中
     */
    public IntegerProperty houmProperty() {
        return this.houm;
    }

    /**
     * 命中を設定します。
     * @param houm 命中
     */
    public void setHoum(Integer houm) {
        this.houm = new SimpleIntegerProperty(houm);
    }

    /**
     * 射程を取得します。
     * @return 射程
     */
    public IntegerProperty lengProperty() {
        return this.leng;
    }

    /**
     * 射程を設定します。
     * @param leng 射程
     */
    public void setLeng(Integer leng) {
        this.leng = new SimpleIntegerProperty(leng);
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public IntegerProperty luckProperty() {
        return this.luck;
    }

    /**
     * 運を設定します。
     * @param luck 運
     */
    public void setLuck(Integer luck) {
        this.luck = new SimpleIntegerProperty(luck);
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public IntegerProperty houkProperty() {
        return this.houk;
    }

    /**
     * 回避を設定します。
     * @param houk 回避
     */
    public void setHouk(Integer houk) {
        this.houk = new SimpleIntegerProperty(houk);
    }

    /**
     * 爆装を取得します。
     * @return 爆装
     */
    public IntegerProperty bakuProperty() {
        return this.baku;
    }

    /**
     * 爆装を設定します。
     * @param baku 爆装
     */
    public void setBaku(Integer baku) {
        this.baku = new SimpleIntegerProperty(baku);
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public IntegerProperty raigProperty() {
        return this.raig;
    }

    /**
     * 雷装を設定します。
     * @param raig 雷装
     */
    public void setRaig(Integer raig) {
        this.raig = new SimpleIntegerProperty(raig);
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public IntegerProperty sakuProperty() {
        return this.saku;
    }

    /**
     * 索敵を設定します。
     * @param saku 索敵
     */
    public void setSaku(Integer saku) {
        this.saku = new SimpleIntegerProperty(saku);
    }

    /**
     * 対潜を取得します。
     * @return 対潜
     */
    public IntegerProperty taisProperty() {
        return this.tais;
    }

    /**
     * 対潜を設定します。
     * @param tais 対潜
     */
    public void setTais(Integer tais) {
        this.tais = new SimpleIntegerProperty(tais);
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public IntegerProperty tykuProperty() {
        return this.tyku;
    }

    /**
     * 対空を設定します。
     * @param tyku 対空
     */
    public void setTyku(Integer tyku) {
        this.tyku = new SimpleIntegerProperty(tyku);
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public IntegerProperty soukProperty() {
        return this.souk;
    }

    /**
     * 装甲を設定します。
     * @param souk 装甲
     */
    public void setSouk(Integer souk) {
        this.souk = new SimpleIntegerProperty(souk);
    }

    @Override
    public int hashCode() {
        return this.id.get();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Item) && this.id.get() == ((Item) obj).id.get();
    }

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(this.name)
                .add(this.type.get())
                .add(Integer.toString(this.count.get()))
                .add(Integer.toString(this.houg.get()))
                .add(Integer.toString(this.houm.get()))
                .add(Integer.toString(this.leng.get()))
                .add(Integer.toString(this.luck.get()))
                .add(Integer.toString(this.houk.get()))
                .add(Integer.toString(this.baku.get()))
                .add(Integer.toString(this.raig.get()))
                .add(Integer.toString(this.saku.get()))
                .add(Integer.toString(this.tais.get()))
                .add(Integer.toString(this.tyku.get()))
                .add(Integer.toString(this.souk.get()))
                .toString();
    }

    @Override
    public int compareTo(Item o) {
        return Comparator.comparing(Item::getName).compare(this, o);
    }

    /**
     * 装備定義から所有装備を生成します
     *
     * @param slotitem 装備定義
     * @return 所有装備
     */
    public static Item toItem(SlotitemMst slotitem) {
        String type = SlotitemEquiptypeCollection.get()
                .getEquiptypeMap()
                .get(slotitem.getType().get(2))
                .getName();
        Integer count = (int) SlotItemCollection.get()
                .getSlotitemMap()
                .values()
                .stream()
                .filter(e -> e.getSlotitemId().equals(slotitem.getId()))
                .count();
        Item item = new Item();
        item.setId(slotitem.getId());
        item.setType3(slotitem.getType().get(3));
        item.setName(slotitem.getName());
        item.setType(type);
        item.setCount(count);
        item.setHoug(slotitem.getHoug());
        item.setHoum(slotitem.getHoum());
        item.setLeng(slotitem.getLeng());
        item.setLuck(slotitem.getLuck());
        item.setHouk(slotitem.getHouk());
        item.setBaku(slotitem.getBaku());
        item.setRaig(slotitem.getRaig());
        item.setSaku(slotitem.getSaku());
        item.setTais(slotitem.getTais());
        item.setTyku(slotitem.getTyku());
        item.setSouk(slotitem.getSouk());
        return item;
    }
}
