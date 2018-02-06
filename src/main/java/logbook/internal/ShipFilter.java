package logbook.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import logbook.bean.DeckPortCollection;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
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
            if (ship == null)
                return false;
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
            if (ship == null)
                return false;
            return this.conditionType.compare(ship.getCond(), this.conditionValue);
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
            if (ship == null)
                return false;
            return this.levelType.compare(ship.getLv(), this.levelValue);
        }
    }

    @Builder
    public static class LabelFilter implements ShipFilter {

        /** ラベル */
        private String labelValue;

        @Override
        public boolean test(ShipItem ship) {
            if (ship == null)
                return false;
            return ship.getLabel().contains(this.labelValue);
        }
    }

    @Builder
    public static class SlotExFilter implements ShipFilter {

        /** 補強増設 */
        private boolean slotEx;

        @Override
        public boolean test(ShipItem ship) {
            if (ship == null)
                return false;
            return this.slotEx == (ship.getSlotEx() != 0);
        }
    }

    @Builder
    public static class TextFilter implements ShipFilter {

        /** テキスト */
        private String text;

        @Override
        public boolean test(ShipItem ship) {
            if (ship == null)
                return false;
            if (this.text.isEmpty())
                return true;
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                    .getSlotitemMap();
            List<Supplier<String>> texts = Arrays.asList(
                    () -> Ships.shipMst(ship.getShip()).map(ShipMst::getName).orElse(""),
                    () -> ship.getType(),
                    () -> Items.slotitemMst(itemMap.get(ship.getSlot1())).map(SlotitemMst::getName).orElse(""),
                    () -> Items.slotitemMst(itemMap.get(ship.getSlot2())).map(SlotitemMst::getName).orElse(""),
                    () -> Items.slotitemMst(itemMap.get(ship.getSlot3())).map(SlotitemMst::getName).orElse(""),
                    () -> Items.slotitemMst(itemMap.get(ship.getSlot4())).map(SlotitemMst::getName).orElse(""),
                    () -> Items.slotitemMst(itemMap.get(ship.getSlotEx())).map(SlotitemMst::getName).orElse(""));
            for (Supplier<String> supplier : texts) {
                if (supplier.get().contains(this.text)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Builder
    public static class MissionFilter implements ShipFilter {

        /** 遠征 */
        private boolean mission;

        @Override
        public boolean test(ShipItem ship) {
            if (ship == null)
                return false;
            return this.mission == DeckPortCollection.get()
                    .getMissionShips()
                    .contains(ship.getId());
        }
    }
}