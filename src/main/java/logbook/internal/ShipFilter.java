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
    public static class DefaultFilter implements ShipFilter {
        /** 艦種 */
        private boolean typeFilter;

        /** 艦種 */
        private Set<String> types;

        /** コンディション */
        private boolean condFilter;

        /** コンディション */
        private int conditionValue;

        /** コンディション条件 */
        private Operator conditionType;

        /**　レベル */
        private boolean levelFilter;

        /**　レベル */
        private int levelValue;

        /**　レベル条件 */
        private Operator levelType;

        /** ラベル */
        private boolean labelFilter;

        /** ラベル */
        private String labelValue;

        @Override
        public boolean test(ShipItem ship) {
            boolean result = true;

            if (result && this.typeFilter) {
                result = this.types.contains(ship.getType());
            }
            if (result && this.condFilter) {
                result = eval(ship.getCond(), this.conditionValue, this.conditionType);
            }
            if (result && this.levelFilter) {
                result = eval(ship.getLv(), this.levelValue, this.levelType);
            }
            if (result && this.labelFilter) {
                result = ship.getLabel().contains(labelValue);
            }
            return result;
        }

        private boolean eval(int v1, int v2, Operator ope) {
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
}
