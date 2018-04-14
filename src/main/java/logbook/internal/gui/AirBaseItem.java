package logbook.internal.gui;

import java.util.Comparator;
import java.util.Optional;
import java.util.StringJoiner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.Messages;
import logbook.bean.SlotItem;
import logbook.bean.SlotitemEquiptypeCollection;
import logbook.bean.SlotitemMst;
import logbook.internal.AirBases;
import logbook.internal.Items;
import logbook.internal.SlotItemType;

/**
 * 基地航空隊装備
 *
 */
public class AirBaseItem implements Comparable<AirBaseItem> {

    /** 装備定義 */
    private IntegerProperty id;

    /** ソート用 種別 */
    private Integer type3;

    /** 名称 */
    private String name;

    /** 種別 */
    private StringProperty type;

    /** 熟練度 */
    private IntegerProperty alv;

    /** 改修Lv */
    private IntegerProperty level;

    /** 個数 */
    private IntegerProperty count;

    /** 出撃時制空 */
    private IntegerProperty seiku;

    /** 防空時制空 */
    private IntegerProperty interceptSeiku;

    /** 半径 */
    private IntegerProperty distance;

    /** 大艇入り半径 */
    private IntegerProperty distanceTaiteichan;

    /** Catalina入り半径 */
    private IntegerProperty distanceCatalina;

    /** 配置コスト */
    private IntegerProperty cost;

    /** 対空 */
    private IntegerProperty tyku;

    /** 対爆 */
    private IntegerProperty houm;

    /** 迎撃 */
    private IntegerProperty houk;

    /** 雷装 */
    private IntegerProperty raig;

    /** 爆装 */
    private IntegerProperty baku;

    /** 対潜 */
    private IntegerProperty tais;

    /** 索敵 */
    private IntegerProperty saku;

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
     * 個数を取得します。
     * @return 個数
     */
    public IntegerProperty countProperty() {
        return this.count;
    }

    /**
     * 個数を設定します。
     * @param count 個数
     */
    public void setCount(Integer count) {
        this.count = new SimpleIntegerProperty(count);
    }

    /**
     * 出撃時制空を取得します。
     * @return 出撃時制空
     */
    public IntegerProperty seikuProperty() {
        return this.seiku;
    }

    /**
     * 出撃時制空を設定します。
     * @param seiku 出撃時制空
     */
    public void setSeiku(Integer seiku) {
        this.seiku = new SimpleIntegerProperty(seiku);
    }

    /**
     * 防空時制空を取得します。
     * @return 防空時制空
     */
    public IntegerProperty interceptSeikuProperty() {
        return this.interceptSeiku;
    }

    /**
     * 防空時制空を設定します。
     * @param interceptSeiku 防空時制空
     */
    public void setInterceptSeiku(Integer interceptSeiku) {
        this.interceptSeiku = new SimpleIntegerProperty(interceptSeiku);
    }

    /**
     * 半径を取得します。
     * @return 半径
     */
    public IntegerProperty distanceProperty() {
        return this.distance;
    }

    /**
     * 半径を設定します。
     * @param distance 半径
     */
    public void setDistance(Integer distance) {
        this.distance = new SimpleIntegerProperty(distance);
    }

    /**
     * Catalina入り半径を取得します。
     * @return Catalina入り半径
     */
    public IntegerProperty distanceCatalinaProperty() {
        return this.distanceCatalina;
    }

    /**
     * Catalina入り半径を設定します。
     * @param distanceCatalina Catalina入り半径
     */
    public void setDistanceCatalina(Integer distanceCatalina) {
        this.distanceCatalina = new SimpleIntegerProperty(distanceCatalina);
    }

    /**
     * 大艇入り半径を取得します。
     * @return 大艇入り半径
     */
    public IntegerProperty distanceTaiteichanProperty() {
        return this.distanceTaiteichan;
    }

    /**
     * 大艇入り半径を設定します。
     * @param distanceTaiteichan 大艇入り半径
     */
    public void setDistanceTaiteichan(Integer distanceTaiteichan) {
        this.distanceTaiteichan = new SimpleIntegerProperty(distanceTaiteichan);
    }

    /**
     * 配置コストを取得します。
     * @return 配置コスト
     */
    public IntegerProperty costProperty() {
        return this.cost;
    }

    /**
     * 配置コストを設定します。
     * @param cost 配置コスト
     */
    public void setCost(Integer cost) {
        this.cost = new SimpleIntegerProperty(cost);
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
     * 対爆を取得します。
     * @return 対爆
     */
    public IntegerProperty houmProperty() {
        return this.houm;
    }

    /**
     * 対爆を設定します。
     * @param houm 対爆
     */
    public void setHoum(Integer houm) {
        this.houm = new SimpleIntegerProperty(houm);
    }

    /**
     * 迎撃を取得します。
     * @return 迎撃
     */
    public IntegerProperty houkProperty() {
        return this.houk;
    }

    /**
     * 迎撃を設定します。
     * @param houk 迎撃
     */
    public void setHouk(Integer houk) {
        this.houk = new SimpleIntegerProperty(houk);
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

    @Override
    public int hashCode() {
        return (this.id.get() << 16) | (this.level.get() << 8) | this.alv.get();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AirBaseItem)
                && this.id.get() == ((AirBaseItem) obj).id.get()
                && this.level.get() == ((AirBaseItem) obj).level.get()
                && this.alv.get() == ((AirBaseItem) obj).alv.get();
    }

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(this.name)
                .add(this.type.get())
                .add(Optional.ofNullable(this.alv.get())
                        .filter(v -> v > 0)
                        .map(v -> Messages.getString("item.alv", v)) //$NON-NLS-1$
                        .orElse(""))
                .add(Optional.ofNullable(this.level.get())
                        .filter(v -> v > 0)
                        .map(v -> Messages.getString("item.level", v)) //$NON-NLS-1$
                        .orElse(""))
                .add(Integer.toString(this.count.get()))
                .add(Integer.toString(this.seiku.get()))
                .add(Integer.toString(this.interceptSeiku.get()))
                .add(Integer.toString(this.distance.get()))
                .add(Integer.toString(this.distanceTaiteichan.get()))
                .add(Integer.toString(this.cost.get()))
                .add(Integer.toString(this.tyku.get()))
                .add(Integer.toString(this.houm.get()))
                .add(Integer.toString(this.houk.get()))
                .add(Integer.toString(this.raig.get()))
                .add(Integer.toString(this.baku.get()))
                .add(Integer.toString(this.tais.get()))
                .add(Integer.toString(this.saku.get()))
                .toString();
    }

    @Override
    public int compareTo(AirBaseItem o) {
        return Comparator.comparing(AirBaseItem::getName).compare(this, o);
    }

    /**
     * 装備から基地航空隊装備を生成します
     *
     * @param item 装備
     * @return 基地航空隊装備
     */
    public static AirBaseItem toAirBaseItem(SlotItem item) {
        SlotitemMst slotitem = Items.slotitemMst(item).get();
        String type = SlotitemEquiptypeCollection.get()
                .getEquiptypeMap()
                .get(slotitem.getType().get(2))
                .getName();
        int onslot = Items.isCombatAircraft(slotitem) ? 18 : 4;
        int distance = Optional.ofNullable(slotitem.getDistance()).orElse(0);

        AirBaseItem abitem = new AirBaseItem();
        abitem.setId(slotitem.getId());
        abitem.setType3(slotitem.getType().get(3));
        abitem.setName(slotitem.getName());
        abitem.setType(type);
        abitem.setAlv(Optional.ofNullable(item.getAlv()).orElse(0));
        abitem.setLevel(Optional.ofNullable(item.getLevel()).orElse(0));
        abitem.setCount(0);
        abitem.setSeiku(AirBases.airSuperiority(item, onslot, false));
        abitem.setInterceptSeiku(AirBases.airSuperiority(item, onslot, true));
        abitem.setDistance(distance);
        abitem.setDistanceTaiteichan(distance + AirBases.distanceAdditional(distance, 20));
        abitem.setDistanceCatalina(distance + AirBases.distanceAdditional(distance, 10));
        abitem.setCost(Optional.ofNullable(slotitem.getCost()).map(c -> c * onslot).orElse(0));
        abitem.setTyku(slotitem.getTyku());
        if (SlotItemType.局地戦闘機.equals(slotitem)) {
            abitem.setHoum(slotitem.getHoum());
            abitem.setHouk(slotitem.getHouk());
        } else {
            abitem.setHoum(0);
            abitem.setHouk(0);
        }
        abitem.setRaig(slotitem.getRaig());
        abitem.setBaku(slotitem.getBaku());
        abitem.setTais(slotitem.getTais());
        abitem.setSaku(slotitem.getSaku());
        return abitem;
    }

    /**
     * 基地航空隊に表示する対象かを調べます
     *
     * @param item 装備
     * @return 対象かどうか
     */
    public static boolean isTarget(SlotItem item) {
        SlotitemMst slotitem = Items.slotitemMst(item)
                .orElse(null);
        if (slotitem == null)
            return false;
        // 航空機である
        if (!Items.isAircraft(slotitem))
            return false;
        // 水上機ではない(載せれるけど)
        if (SlotItemType.水上偵察機.equals(slotitem))
            return false;
        if (SlotItemType.水上戦闘機.equals(slotitem))
            return false;
        if (SlotItemType.水上爆撃機.equals(slotitem))
            return false;
        // 半径1以上
        if (slotitem.getDistance() == null || slotitem.getDistance() == 0)
            return false;
        return true;
    }
}
