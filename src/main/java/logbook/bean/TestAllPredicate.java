package logbook.bean;

import java.util.Objects;
import java.util.function.Predicate;

public interface TestAllPredicate<T> extends Predicate<T> {
    @Override
    default public TestAllPredicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> {
            boolean result1 = test(t);
            boolean result2 = other.test(t);
            return result1 && result2;
        };
    }

    @Override
    default public TestAllPredicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> {
            boolean result1 = test(t);
            boolean result2 = other.test(t);
            return result1 || result2;
        };
    }
}