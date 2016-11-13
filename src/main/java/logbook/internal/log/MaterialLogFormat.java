package logbook.internal.log;

import java.util.Map;
import java.util.StringJoiner;

import logbook.bean.Material;

/**
 * 資材ログ
 *
 */
public class MaterialLogFormat extends LogFormatBase<Map<Integer, Material>> {

    @Override
    public String name() {
        return "資材ログ";
    }

    @Override
    public String header() {
        return new StringJoiner(",")
                .add("日付")
                .add("燃料")
                .add("弾薬")
                .add("鋼材")
                .add("ボーキ")
                .add("高速修復材")
                .add("高速建造材")
                .add("開発資材")
                .add("改修資材")
                .toString();
    }

    @Override
    public String format(Map<Integer, Material> material) {
        StringJoiner joiner = new StringJoiner(",");
        // 日付
        joiner.add(nowString());
        // 燃料
        joiner.add(String.valueOf(material.get(1).getValue()));
        // 弾薬
        joiner.add(String.valueOf(material.get(2).getValue()));
        // 鋼材
        joiner.add(String.valueOf(material.get(3).getValue()));
        // ボーキ
        joiner.add(String.valueOf(material.get(4).getValue()));
        // 高速修復材=6
        joiner.add(String.valueOf(material.get(6).getValue()));
        // 高速建造材=5
        joiner.add(String.valueOf(material.get(5).getValue()));
        // 開発資材
        joiner.add(String.valueOf(material.get(7).getValue()));
        // 改修資材
        joiner.add(String.valueOf(material.get(8).getValue()));

        return joiner.toString();
    }

}
