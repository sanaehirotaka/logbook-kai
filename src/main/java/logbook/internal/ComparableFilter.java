package logbook.internal;

import java.util.function.Function;
import java.util.function.Predicate;

import lombok.Builder;

@Builder
/**
 * 任意の <code>Comparable</code> なパラメータによるフィルター
 *
 * @param <S> 比較するパラメータを持っているクラス
 * @param <T> 比較するパラメータの型
 */
public class ComparableFilter<S, T extends Comparable<T>> implements Predicate<S> {
    /** ShipItem からパラメータに変換する mapper */
    private Function<S, T> mapper;
    /** 比較対象の値 */
    private T value;
    /** 演算子 */
    private Operator type;

    @Override
    public boolean test(S obj) {
        if (obj == null) {
            return false;
        }
        return this.type.compare(this.mapper.apply(obj), this.value);
    }
}
