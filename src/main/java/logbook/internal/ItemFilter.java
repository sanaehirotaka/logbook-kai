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

        /** テキスト */
        private boolean typeFilter;

        /** テキスト */
        private String typeValue;

        @Override
        public boolean test(Item item) {
            if (this.typeFilter) {
                if (item.typeProperty().get().contains(this.typeValue))
                    return true;
                if (item.getName().contains(this.typeValue))
                    return true;

                return false;
            }
            return true;
        }
    }
}
