package logbook.internal;

import java.util.function.Predicate;

import logbook.internal.gui.Item;
import lombok.Builder;

/**
 * 所有装備一覧のフィルター
 *
 */
@FunctionalInterface
public interface ItemFilter extends Predicate<Item> {

    @Builder
    public static class DefaultFilter implements ItemFilter {

        /** 種類 */
        private boolean typeFilter;

        /** 種類 */
        private String typeValue;

        @Override
        public boolean test(Item item) {
            if (this.typeFilter) {
                return this.typeValue.equals(item.typeProperty().get());
            }
            return true;
        }
    }
}
