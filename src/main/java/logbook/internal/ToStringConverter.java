package logbook.internal;

import java.util.function.Function;

import javafx.util.StringConverter;

/**
 * StringConverterのtoStringを実装します
 *
 * @param <T> 型パラメーター
 */
public class ToStringConverter<T> extends StringConverter<T> {

    private Function<T, String> converter;

    public ToStringConverter(Function<T, String> converter) {
        this.converter = converter;
    }

    @Override
    public String toString(T object) {
        if (object != null) {
            return converter.apply(object);
        } else {
            return "";
        }
    }

    @Override
    public T fromString(String string) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@link Object#toString()}を使用して{@link StringConverter#toString(Object)}を実装します。
     * これは次の記述と同等です
     * <blockquote>
     *     <code>ToStringConverter.of(Object::toString)</code>
     * </blockquote>
     *
     * @return {@link Object#toString()}によって{@link StringConverter#toString(Object)}が実装された{@link StringConverter}
     */
    public static <T> StringConverter<T> of() {
        return new ToStringConverter<T>(Object::toString);
    }

    /**
     * {@code converter}で指定された{@link Function}を使用して{@link StringConverter#toString(Object)}を実装します
     * @param converter {@link Function}
     * @return {@code converter}によって{@link StringConverter#toString(Object)}が実装された{@link StringConverter}
     */
    public static <T> StringConverter<T> of(Function<T, String> converter) {
        return new ToStringConverter<T>(converter);
    }
}
