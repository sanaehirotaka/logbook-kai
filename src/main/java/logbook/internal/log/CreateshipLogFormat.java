package logbook.internal.log;

import java.util.Optional;
import java.util.StringJoiner;

import logbook.bean.Basic;
import logbook.bean.Createship;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.StypeCollection;
import logbook.internal.Ships;

public class CreateshipLogFormat extends LogFormatBase<Createship> {

    @Override
    public String name() {
        return "建造報告書";
    }

    @Override
    public String header() {
        return new StringJoiner(",")
                .add("日付")
                .add("種類")
                .add("名前")
                .add("艦種")
                .add("燃料")
                .add("弾薬")
                .add("鋼材")
                .add("ボーキ")
                .add("開発資材")
                .add("空きドック")
                .add("秘書艦")
                .add("司令部Lv")
                .toString();
    }

    @Override
    public String format(Createship value) {
        StringJoiner joiner = new StringJoiner(",");
        // 日付
        joiner.add(nowString());
        // 種類
        if (value.getLargeFlag() == 1) {
            joiner.add("大型艦建造");
        } else {
            joiner.add("通常艦建造");
        }
        Optional<ShipMst> ship = Optional.ofNullable(ShipMstCollection.get()
                .getShipMap()
                .get(value.getKdock().getCreatedShipId()));
        // 名前
        joiner.add(ship.map(ShipMst::getName).orElse(""));
        // 艦種
        joiner.add(ship.map(mst -> StypeCollection.get()
                .getStypeMap()
                .get(mst.getStype())
                .getName()).orElse(""));
        // 燃料
        joiner.add(String.valueOf(value.getItem1()));
        // 弾薬
        joiner.add(String.valueOf(value.getItem2()));
        // 鋼材
        joiner.add(String.valueOf(value.getItem3()));
        // ボーキ
        joiner.add(String.valueOf(value.getItem4()));
        // 開発資材
        joiner.add(String.valueOf(value.getItem5()));
        // 空きドック
        joiner.add(String.valueOf(value.getEmptyDock()));
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
