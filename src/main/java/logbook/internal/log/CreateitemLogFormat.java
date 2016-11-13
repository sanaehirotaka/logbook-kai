package logbook.internal.log;

import java.util.Optional;
import java.util.StringJoiner;

import logbook.bean.Basic;
import logbook.bean.Createitem;
import logbook.bean.ShipMst;
import logbook.bean.SlotitemEquiptypeCollection;
import logbook.bean.SlotitemMst;
import logbook.internal.Items;
import logbook.internal.Ships;

/**
 * 開発報告書
 *
 */
public class CreateitemLogFormat extends LogFormatBase<Createitem> {

    @Override
    public String name() {
        return "開発報告書";
    }

    @Override
    public String header() {
        return new StringJoiner(",")
                .add("日付")
                .add("開発装備")
                .add("種別")
                .add("燃料")
                .add("弾薬")
                .add("鋼材")
                .add("ボーキ")
                .add("秘書艦")
                .add("司令部Lv")
                .toString();
    }

    @Override
    public String format(Createitem value) {
        StringJoiner joiner = new StringJoiner(",");
        // 日付
        joiner.add(nowString());

        // 開発装備
        Optional<SlotitemMst> itemMst = Items.slotitemMst(value.getSlotItem());
        joiner.add(itemMst.map(SlotitemMst::getName)
                .orElse(""));
        // 種別
        joiner.add(itemMst.map(mst -> SlotitemEquiptypeCollection.get()
                .getEquiptypeMap()
                .get(mst.getType().get(2))
                .getName())
                .orElse(""));
        // 燃料
        joiner.add(String.valueOf(value.getItem1()));
        // 弾薬
        joiner.add(String.valueOf(value.getItem2()));
        // 鋼材
        joiner.add(String.valueOf(value.getItem3()));
        // ボーキ
        joiner.add(String.valueOf(value.getItem4()));
        // 秘書艦
        Optional<ShipMst> mst = Ships.shipMst(value.getSecretary());
        joiner.add(mst.map(ShipMst::getName)
                .map(n -> n + "(Lv" + value.getSecretary().getLv() + ")")
                .orElse(""));
        // 司令部Lv
        joiner.add(String.valueOf(Basic.get().getLevel()));

        return joiner.toString();
    }

}
