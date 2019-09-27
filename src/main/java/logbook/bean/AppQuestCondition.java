package logbook.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logbook.internal.QuestCollect;
import logbook.internal.QuestCollect.Count;
import logbook.internal.QuestCollect.Rank;
import lombok.Data;

/**
 * 任務条件
 *
 */
@Data
public class AppQuestCondition implements Predicate<QuestCollect> {

    /** 条件 */
    private List<Condition> conditions = new ArrayList<>();

    /** 結果 */
    private Boolean result;

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

    /**
     * 条件
     */
    @Data
    public static class Condition implements Predicate<QuestCollect> {

        /** 海域 */
        private Set<String> area;

        /** 出撃 */
        private boolean start;

        /** ボス */
        private boolean boss;

        /** ランク */
        private Set<String> rank;

        /** 艦種 */
        private Set<String> stype;

        /** カウント */
        private int count;

        private Boolean result;

        private String current;

        @Override
        public boolean test(QuestCollect t) {
            int count = this.count(t);
            this.current = String.valueOf(count);
            return this.result = (count >= this.count);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (this.stype != null && !this.stype.isEmpty()) {
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
                if (this.start) {
                    sb.append("に").append(this.count).append("回出撃");
                } else {
                    sb.append("で");
                    if (this.boss) {
                        sb.append("ボス");
                    }
                    if (this.stype == null || this.stype.isEmpty()
                            || this.stype.contains("E")
                            || this.stype.contains("D")) {
                        // D以下
                        sb.append("戦闘");
                    } else if (this.stype.contains("C")) {
                        // C以上
                        sb.append("C敗北以上");
                    } else if (this.stype.contains("B")) {
                        // B以上
                        sb.append("B勝利以上");
                    } else if (this.stype.contains("A")) {
                        // A以上
                        sb.append("A勝利以上");
                    } else {
                        // S
                        sb.append("S勝利");
                    }
                    sb.append(this.count).append("回");
                }
            }
            if (this.current != null) {
                sb.append("(現在の値:" + this.current + ")");
            }
            return sb.toString();
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
                if (this.boss) {
                    rank = battleCount.getBoss();
                } else {
                    rank = battleCount.getAll();
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

    @Override
    public boolean test(QuestCollect t) {
        Predicate<QuestCollect> predicate = null;
        for (Condition condition : this.conditions) {
            if (predicate == null) {
                predicate = condition;
            } else {
                predicate = predicate.and(condition);
            }
        }
        return this.result = predicate.test(t);
    }
}
