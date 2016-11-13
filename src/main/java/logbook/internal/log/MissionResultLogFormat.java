package logbook.internal.log;

import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import logbook.bean.MissionResult;
import logbook.bean.Useitem;
import logbook.bean.UseitemCollection;

/**
 * 遠征報告書
 *
 */
public class MissionResultLogFormat extends LogFormatBase<MissionResult> {

    @Override
    public String name() {
        return "遠征報告書";
    }

    @Override
    public String header() {
        return new StringJoiner(",")
                .add("日付")
                .add("結果")
                .add("海域")
                .add("遠征名")
                .add("燃料")
                .add("弾薬")
                .add("鋼材")
                .add("ボーキ")
                .add("アイテム1名前")
                .add("アイテム1個数")
                .add("アイテム2名前")
                .add("アイテム2個数")
                .add("取得経験値計")
                .toString();
    }

    @Override
    public String format(MissionResult result) {
        StringJoiner joiner = new StringJoiner(",");
        // 日付
        joiner.add(nowString());
        // 結果
        if (result.getClearResult() == 0) {
            joiner.add("失敗");
        } else if (result.getClearResult() == 2) {
            joiner.add("大成功");
        } else {
            joiner.add("成功");
        }
        // 海域
        joiner.add(result.getMapareaName());
        // 遠征名
        joiner.add(result.getQuestName());
        // 燃料
        joiner.add(result.getGetMaterial().get(0).toString());
        // 弾薬
        joiner.add(result.getGetMaterial().get(1).toString());
        // 鋼材
        joiner.add(result.getGetMaterial().get(2).toString());
        // ボーキ
        joiner.add(result.getGetMaterial().get(3).toString());

        Map<Integer, Useitem> useitemMap = UseitemCollection.get().getUseitemMap();
        // アイテム1名前
        // アイテム1個数
        if (result.getGetItem1() != null) {
            Optional<Useitem> item;
            if (result.getUseitemFlag().get(0) != 4) {
                item = Optional.ofNullable(useitemMap.get(result.getUseitemFlag().get(0)));
            } else {
                item = Optional.ofNullable(useitemMap.get(result.getGetItem1().getUseitemId()));
            }
            joiner.add(item.map(Useitem::getName).orElse(""));
            joiner.add(result.getGetItem1().getUseitemCount().toString());
        } else {
            joiner.add("");
            joiner.add("");
        }
        // アイテム2名前
        // アイテム2個数
        if (result.getGetItem2() != null) {
            Optional<Useitem> item;
            if (result.getUseitemFlag().get(1) != 4) {
                item = Optional.ofNullable(useitemMap.get(result.getUseitemFlag().get(1)));
            } else {
                item = Optional.ofNullable(useitemMap.get(result.getGetItem2().getUseitemId()));
            }
            joiner.add(item.map(Useitem::getName).orElse(""));
            joiner.add(result.getGetItem2().getUseitemCount().toString());
        } else {
            joiner.add("");
            joiner.add("");
        }
        // 取得経験値計
        joiner.add(Integer.toString(result.getGetShipExp().stream().mapToInt(Integer::intValue).sum()));
        return joiner.toString();
    }

}
