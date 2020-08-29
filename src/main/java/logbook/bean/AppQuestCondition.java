package logbook.bean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.internal.LoggerHolder;
import logbook.internal.Operator;
import logbook.internal.QuestCollect;
import logbook.internal.QuestCollect.Count;
import logbook.internal.QuestCollect.Rank;
import logbook.plugin.PluginServices;
import lombok.Data;

/**
 * 任務条件
 *
 */
@Data
public class AppQuestCondition implements Predicate<QuestCollect> {

    /** 任務のタイプ(enum: 出撃, 遠征) */
    private Type type;

    /** 任務期間(文字列:単発,デイリー,ウィークリー,マンスリー,クオータリー) */
    private String resetType;

    /** フィルター条件 */
    private FilterCondition filter;

    /** 条件 */
    private List<Condition> conditions = new ArrayList<>();

    /** 結果 */
    private Boolean result;

    public static AppQuestCondition loadFromResource(int questNo) {
        InputStream is = PluginServices.getQuestResourceAsStream(questNo);
        if (is != null) {
            try {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(Feature.ALLOW_COMMENTS);
                    return mapper.readValue(is, AppQuestCondition.class);
                } finally {
                    is.close();
                }
            } catch (Exception e) {
                LoggerHolder.get().info("任務設定ファイルが読み込めませんでした。", e);
            }
        }
        return null;
    }

    // internal use only
    public boolean isCollectStypeInternal() {
        for (Condition condition : this.conditions) {
            if ((condition.result == null || condition.result == false)
                    && (condition.stype != null && !condition.stype.isEmpty())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean test(QuestCollect t) {
        Predicate<QuestCollect> predicate = null;
        for (Condition condition : this.conditions) {
            condition.test(t);

            if (predicate == null) {
                predicate = condition;
            } else {
                predicate = predicate.and(condition);
            }
        }
        return this.result = predicate.test(t);
    }

    /**
     * フィルター条件
     *
     */
    @Data
    public static class FilterCondition {

        /** 海域条件 */
        private Set<String> area;

        /** 艦隊条件 */
        private FleetCondition fleet;
    }

    /**
     * フィルター条件:艦隊条件
     *
     */
    @Data
    public static class FleetCondition implements Predicate<List<ShipMst>> {

        /** 備考 */
        private String description;

        /** 艦種 */
        private LinkedHashSet<String> stype;

        /** 艦名 */
        private LinkedHashSet<String> name;

        /** 艦種もしくは艦名のリストに含まれないものに一致 */
        private boolean difference;

        /** カウント */
        private Integer count;

        /** 序列(1:旗艦) */
        private Integer order;

        /** 条件 */
        private List<FleetCondition> conditions;

        /** 演算子(AND,OR,NAND,NOR,EQ(等しい),GE(以上),GT(より大きい),LE(以下),LT(より小さい),NE(等しくない)) */
        private String operator;

        @Override
        public boolean test(List<ShipMst> ships) {
            if (this.count == null && this.operator != null) {
                return this.testOperator(ships);
            } else {
                return this.testShip(ships);
            }
        }

        @Override
        public String toString() {
            if (this.count == null && this.operator != null) {
                return this.toStringOperator();
            } else {
                return this.toStringShip();
            }
        }

        /**
         * 条件(type=艦娘)
         *
         * @param ships 艦隊
         * @return 条件に一致する場合true
         */
        private boolean testShip(List<ShipMst> ships) {
            if (this.count != null) {
                // 序列の指定無効
                int c = 0;
                for (int i = 0; i < ships.size(); i++) {
                    if (this.difference ^ this.testShip0(ships.get(i))) {
                        c++;
                    }
                }
                return Operator.valueOf(this.operator).compare(c, this.count);
            } else {
                if (this.order != null) {
                    // 序列の指定有り
                    if (ships.size() >= this.order) {
                        if (this.difference ^ this.testShip0(ships.get(this.order - 1))) {
                            ships.set(this.order - 1, null);
                            return true;
                        }
                    }
                } else {
                    // 序列の指定無し
                    for (int i = 0; i < ships.size(); i++) {
                        if (this.difference ^ this.testShip0(ships.get(i))) {
                            ships.set(i, null);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean testShip0(ShipMst ship) {
            if (ship == null) {
                return false;
            }
            if (this.stype != null) {
                String stype = ship.asStype()
                        .map(Stype::getName)
                        .orElse(null);
                return this.stype.contains(stype);
            }
            if (this.name != null) {
                for (String name : this.name) {
                    if (ship.getName().startsWith(name)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * 条件(論理演算)
         *
         * @param ships 艦隊
         * @return 条件に一致する場合true
         */
        private boolean testOperator(List<ShipMst> ships) {
            Predicate<List<ShipMst>> predicate = null;
            for (FleetCondition condition : this.conditions) {
                if (predicate == null) {
                    predicate = condition;
                } else {
                    predicate = this.operator.endsWith("AND")
                            ? predicate.and(condition)
                            : predicate.or(condition);
                }
            }
            if ("NAND".equals(this.operator) || "NOR".equals(this.operator)) {
                predicate = predicate.negate();
            }
            return predicate.test(new ArrayList<>(ships));
        }

        private String toStringShip() {
            StringBuilder sb = new StringBuilder();
            if (this.stype != null) {
                sb.append(this.stype.stream().collect(Collectors.joining("または")));
            }
            if (this.name != null) {
                sb.append(this.name.stream().collect(Collectors.joining("または")));
            }
            if (this.difference) {
                sb.append("以外");
            }
            if (this.count != null) {
                sb.append("が");
                sb.append(this.count);
                sb.append("隻");
                Operator ope = Operator.valueOf(this.operator);
                if (ope == Operator.EQ) {
                    // ～がX隻に等しい
                    sb.append("に");
                }
                sb.append(Operator.valueOf(this.operator).toString());
            } else {
                sb.append("を");
                if (this.order != null) {
                    if (this.order == 1) {
                        sb.append("旗艦");
                    } else {
                        sb.append("序列" + this.order + "位");
                    }
                } else {
                    sb.append("含む");
                }
            }
            if (this.description != null) {
                sb.append("(" + this.description + ")");
            }
            return sb.toString();
        }

        private String toStringOperator() {
            StringBuilder sb = new StringBuilder();
            switch (this.operator) {
            case "AND":
                sb.append("次の条件を全て満たす");
                break;
            case "OR":
                sb.append("次の条件のいずれか少なくとも1つを満たす");
                break;
            case "NAND":
                sb.append("次の条件のいずれか少なくとも1つを満たさない");
                break;
            case "NOR":
                sb.append("次の条件を全て満たさない");
                break;
            default:
                break;
            }
            if (this.description != null) {
                sb.append("(" + this.description + ")");
            }
            return sb.toString();
        }
    }

    public enum Type {
        出撃, 遠征
    }

    /**
     * 集計条件
     */
    @Data
    public static class Condition implements Predicate<QuestCollect> {

        /** 備考 */
        private String description;

        /** 海域 */
        private LinkedHashSet<String> area;

        /** マップセル(7-2の第1ボスなら7、第2ボスなら15) */
        private String cell;

        /** 出撃 */
        private boolean start;

        /** ボス */
        private boolean boss;

        /** ランク */
        private LinkedHashSet<String> rank;

        /** 艦種 */
        private LinkedHashSet<String> stype;

        /** カウント */
        private int count;
        
        /** 遠征名 */
        private List<String> missions;

        private Boolean result;

        private String current;

        @Override
        public boolean test(QuestCollect t) {
            int count;
            if (this.missions != null) {
                count = this.missionCount(t);
            } else {
                count = this.count(t);
            }
            this.current = String.valueOf(count);
            return this.result = (count >= this.count);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (this.missions != null) {
                if (this.missions.size() == 0) {
                    sb.append("任意の遠征");
                } else {
                    List<String> labels = this.missions.stream()
                        .map(name -> MissionCollection.get().getMissionMap().values().stream()
                                .filter(mission -> mission.getName().equals(name))
                                .map(mission -> "「" + mission.getDispNo() + " " + mission.getName() + "」")
                                .findAny()
                                .orElse(name))
                        .collect(Collectors.toList());
                    sb.append(String.join("または", labels));
                }
                sb.append("を").append(this.count).append("回成功");
            } else if (this.stype != null && !this.stype.isEmpty()) {
                sb.append(this.stype.stream().collect(Collectors.joining("または")))
                        .append("を")
                        .append(this.count)
                        .append("隻撃沈");
            } else {
                if (this.area != null && !this.area.isEmpty()) {
                    sb.append(this.area.stream().collect(Collectors.joining("または")));
                } else {
                    sb.append("任意の海域");
                }
                if (this.cell != null) {
                    sb.append("(セル" + this.cell + ")");
                }
                if (this.start) {
                    sb.append("に").append(this.count).append("回出撃");
                } else {
                    sb.append("で");
                    if (this.boss) {
                        sb.append("ボス");
                    }
                    if (this.rank == null || this.rank.isEmpty()
                            || this.rank.contains("E")
                            || this.rank.contains("D")) {
                        // D以下
                        sb.append("戦闘");
                    } else if (this.rank.contains("C")) {
                        // C以上
                        sb.append("C敗北以上");
                    } else if (this.rank.contains("B")) {
                        // B以上
                        sb.append("B勝利以上");
                    } else if (this.rank.contains("A")) {
                        // A以上
                        sb.append("A勝利以上");
                    } else {
                        // S
                        sb.append("S勝利");
                    }
                    sb.append(this.count).append("回");
                }
            }
            if (this.description != null) {
                sb.append("(" + this.description + ")");
            }
            if (this.current != null) {
                sb.append("(現在の値:" + this.current + ")");
            }
            return sb.toString();
        }

        /**
         * 遠征カウント
         * @param t 集計結果
         * @return カウント
         */
        private int missionCount(QuestCollect t) {
            Map<String, Long> map = t.getMissions();
            if (this.missions.size() == 0) {
                return map.values().stream().mapToInt(Long::intValue).sum();
            } else {
                return this.missions.stream().map(mission -> map.getOrDefault(mission, 0L)).mapToInt(Long::intValue).sum();
            }
        }

        
        /**
         * カウント
         * @param t 集計結果
         * @return カウント
         */
        private int count(QuestCollect t) {
            int count = 0;
            // 撃沈カウント
            if (this.stype != null) {
                for (String key : this.stype) {
                    Integer v = t.getStype().get(key);
                    if (v != null) {
                        count += v;
                    }
                }
                return count;
            }
            // 戦闘カウント
            Count battleCount = new Count();
            if (this.area != null) {
                for (String key : this.area) {
                    Count areaCount = t.getArea().get(key);
                    if (areaCount == null) {
                        continue;
                    }
                    battleCount.add(areaCount);
                }
            } else {
                battleCount = t.getTotal();
            }
            if (this.start) {
                // 出撃回数
                return battleCount.getStart();
            } else {
                // 評価
                Rank rank;
                if (this.cell != null) {
                    rank = battleCount.getCell().computeIfAbsent(this.area.stream().findFirst().get() + "-" + this.cell, i -> new Rank());
                } else {
                    if (this.boss) {
                        rank = battleCount.getBoss();
                    } else {
                        rank = battleCount.getAll();
                    }
                }
                boolean any = (this.rank == null || this.rank.isEmpty());

                if (any || this.rank.contains("S"))
                    count += rank.getS();
                if (any || this.rank.contains("A"))
                    count += rank.getA();
                if (any || this.rank.contains("B"))
                    count += rank.getB();
                if (any || this.rank.contains("C"))
                    count += rank.getC();
                if (any || this.rank.contains("D"))
                    count += rank.getD();
                if (any || this.rank.contains("E"))
                    count += rank.getE();
                return count;
            }
        }
    }
}
