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
import lombok.Getter;
import lombok.Setter;

/**
 * 所有装備
 *
 */
public class Item implements Comparable<Item> {

    /** 装備定義 */
    private IntegerProperty id = new SimpleIntegerProperty();

    /** ソート用 種別 */
    @Getter
    @Setter
    private Integer type3;

    /** 名称 */
    @Getter
    @Setter
    private String name;

    /** 種別 */
    private StringProperty type = new SimpleStringProperty();

    /** 個数 */
    private IntegerProperty count = new SimpleIntegerProperty();

    /** 火力 */
    private IntegerProperty houg = new SimpleIntegerProperty();

    /** 命中 */
    private IntegerProperty houm = new SimpleIntegerProperty();

    /** 射程 */
    private IntegerProperty leng = new SimpleIntegerProperty();

    /** 運 */
    private IntegerProperty luck = new SimpleIntegerProperty();

    /** 回避 */
    private IntegerProperty houk = new SimpleIntegerProperty();

    /** 爆装 */
    private IntegerProperty baku = new SimpleIntegerProperty();

    /** 雷装 */
    private IntegerProperty raig = new SimpleIntegerProperty();

    /** 索敵 */
    private IntegerProperty saku = new SimpleIntegerProperty();

    /** 対潜 */
    private IntegerProperty tais = new SimpleIntegerProperty();

    /** 対空 */
    private IntegerProperty tyku = new SimpleIntegerProperty();

    /** 装甲 */
    private IntegerProperty souk = new SimpleIntegerProperty();


    /**
     * 装備定義を取得します。
     * @return 装備定義
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    /**
     * 装備定義を取得します。
     * @return 装備定義
     */
    public Integer getId() {
        return this.id.get();
    }

    /**
     * 装備定義を設定します。
     * @param id 装備定義
     */
    public void setId(Integer id) {
        this.id.set(id);
    }

    /**
     * 種別を取得します。
     * @return 種別
     */
    public StringProperty typeProperty() {
        return this.type;
    }

    /**
     * 種別を取得します。
     * @return 種別
     */
    public String getType() {
        return this.type.get();
    }

    /**
     * 種別を設定します。
     * @param type 種別
     */
    public void setType(String type) {
        this.type.set(type);
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
        this.count.set(count);
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public IntegerProperty hougProperty() {
        return this.houg;
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public Integer getHoug() {
        return this.houg.get();
    }

    /**
     * 火力を設定します。
     * @param houg 火力
     */
    public void setHoug(Integer houg) {
        this.houg.set(houg);
    }

    /**
     * 命中を取得します。
     * @return 命中
     */
    public IntegerProperty houmProperty() {
        return this.houm;
    }

    /**
     * 命中を取得します。
     * @return 命中
     */
    public Integer getHoum() {
        return this.houm.get();
    }

    /**
     * 命中を設定します。
     * @param houm 命中
     */
    public void setHoum(Integer houm) {
        this.houm.set(houm);
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
     * 運を取得します。
     * @return 運
     */
    public IntegerProperty luckProperty() {
        return this.luck;
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public Integer getLuck() {
        return this.luck.get();
    }

    /**
     * 運を設定します。
     * @param luck 運
     */
    public void setLuck(Integer luck) {
        this.luck.set(luck);
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public IntegerProperty houkProperty() {
        return this.houk;
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public Integer getHouk() {
        return this.houk.get();
    }

    /**
     * 回避を設定します。
     * @param houk 回避
     */
    public void setHouk(Integer houk) {
        this.houk.set(houk);
    }

    /**
     * 爆装を取得します。
     * @return 爆装
     */
    public IntegerProperty bakuProperty() {
        return this.baku;
    }

    /**
     * 爆装を取得します。
     * @return 爆装
     */
    public Integer getBaku() {
        return this.baku.get();
    }

    /**
     * 爆装を設定します。
     * @param baku 爆装
     */
    public void setBaku(Integer baku) {
        this.baku.set(baku);
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public IntegerProperty raigProperty() {
        return this.raig;
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public Integer getRaig() {
        return this.raig.get();
    }

    /**
     * 雷装を設定します。
     * @param raig 雷装
     */
    public void setRaig(Integer raig) {
        this.raig.set(raig);
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public IntegerProperty sakuProperty() {
        return this.saku;
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public Integer getSaku() {
        return this.saku.get();
    }

    /**
     * 索敵を設定します。
     * @param saku 索敵
     */
    public void setSaku(Integer saku) {
        this.saku.set(saku);
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
     * 対空を取得します。
     * @return 対空
     */
    public IntegerProperty tykuProperty() {
        return this.tyku;
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public Integer getTyku() {
        return this.tyku.get();
    }

    /**
     * 対空を設定します。
     * @param tyku 対空
     */
    public void setTyku(Integer tyku) {
        this.tyku.set(tyku);
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public IntegerProperty soukProperty() {
        return this.souk;
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public Integer getSouk() {
        return this.souk.get();
    }

    /**
     * 装甲を設定します。
     * @param souk 装甲
     */
    public void setSouk(Integer souk) {
        this.souk.set(souk);
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
