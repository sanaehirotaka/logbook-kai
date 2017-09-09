package logbook.internal;

import java.util.Set;
import java.util.function.Predicate;

import logbook.internal.gui.ShipItem;
import lombok.Builder;

/**
 * 所有艦娘一覧のフィルター
 *
 */
@FunctionalInterface
public interface ShipFilter extends Predicate<ShipItem> {

    @Builder
    public static class TypeFilter implements ShipFilter {

        /** 艦種 */
        private Set<String> types;

        @Override
        public boolean test(ShipItem ship) {
            return this.types.contains(ship.getType());
        }
    }

    @Builder
    public static class CondFilter implements ShipFilter {

        /** コンディション */
        private int conditionValue;

        /** コンディション条件 */
        private Operator conditionType;

        @Override
        public boolean test(ShipItem ship) {
            return eval(ship.getCond(), this.conditionValue, this.conditionType);
        }

        static boolean eval(int v1, int v2, Operator ope) {
            switch (ope) {
            case EQ:
                return v1 == v2;
            case GE:
                return v1 >= v2;
            case GT:
                return v1 > v2;
            case LE:
                return v1 <= v2;
            case LT:
                return v1 < v2;
            case NE:
                return v1 != v2;
            }
            return false;
        }
    }

    @Builder
    public static class LevelFilter implements ShipFilter {

        /** レベル */
        private int levelValue;

        /** レベル条件 */
        private Operator levelType;

        @Override
        public boolean test(ShipItem ship) {
            return CondFilter.eval(ship.getLv(), this.levelValue, this.levelType);
        }
    }

    @Builder
    public static class LabelFilter implements ShipFilter {

        /** ラベル */
        private String labelValue;

        @Override
        public boolean test(ShipItem ship) {
            return ship.getLabel().contains(this.labelValue);
        }
    }

    @Builder
    public static class SlotExFilter implements ShipFilter {

        /** 補強増設 */
        private boolean slotEx;

        @Override
        public boolean test(ShipItem ship) {
            return slotEx == (ship.getSlotEx() != 0);
        }
    }
}
