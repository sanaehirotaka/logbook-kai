package logbook.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * JsonObjectを扱うための補助クラスです
 *
 */
public final class JsonHelper {

    /**
     * JsonNumberをLongに変換します
     *
     * @see JsonNumber#longValue()
     * @see BigDecimal#longValue()
     * @param val 変換するJsonNumber
     * @return Long
     */
    public static Long toLong(JsonValue val) {
        return toObject((JsonNumber) val, JsonNumber::longValue);
    }

    /**
     * JsonNumberをIntegerに変換します
     *
     * @see JsonNumber#intValue()
     * @see BigDecimal#intValue()
     * @param val 変換するJsonNumber
     * @return Integer
     */
    public static Integer toInteger(JsonValue val) {
        return toObject((JsonNumber) val, JsonNumber::intValue);
    }

    /**
     * JsonNumberをDoubleに変換します
     *
     * @see JsonNumber#doubleValue()
     * @see BigDecimal#doubleValue()
     * @param val 変換するJsonNumber
     * @return Double
     */
    public static Double toDouble(JsonValue val) {
        return toObject((JsonNumber) val, JsonNumber::doubleValue);
    }

    /**
     * JsonNumberをBigDecimalに変換します
     *
     * @see JsonNumber#bigDecimalValue()
     * @param val 変換するJsonNumber
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(JsonValue val) {
        return toObject((JsonNumber) val, JsonNumber::bigDecimalValue);
    }

    /**
     * JsonValueをStringに変換します
     *
     * @see JsonValue#toString()
     * @param val 変換するJsonValue
     * @return String
     */
    public static String toString(JsonValue val) {
        return toObject(val, JsonValue::toString);
    }

    /**
     * JsonValueをBooleanに変換します
     *
     * @param val 変換するJsonValue
     * @return JsonValue.TRUEの場合にtrue、それ以外はfalse
     */
    public static Boolean toBoolean(JsonValue val) {
        return toObject(val, v -> v == JsonValue.TRUE);
    }

    /**
     * JsonValueを任意のオブジェクトに変換します
     *
     * @param val 変換するJsonValue
     * @param function 変換Function
     * @return 変換後のオブジェクト
     */
    public static <T extends JsonValue, R> R toObject(T val, Function<T, R> function) {
        if (val == null || val == JsonValue.NULL) {
            return null;
        }
        return function.apply(val);
    }

    /**
     * JsonArrayをLongのListに変換します<br>
     * JsonArrayの内容はすべてJsonNumberである必要があります
     *
     * @param val 変換するJsonArray
     * @return LongのList
     */
    public static List<Long> toLongList(JsonArray val) {
        return toList(val, JsonHelper::toLong);
    }

    /**
     * JsonArrayをIntegerのListに変換します<br>
     * JsonArrayの内容はすべてJsonNumberである必要があります
     *
     * @param val 変換するJsonArray
     * @return IntegerのList
     */
    public static List<Integer> toIntegerList(JsonArray val) {
        return toList(val, JsonHelper::toInteger);
    }

    /**
     * JsonArrayをDoubleのListに変換します<br>
     * JsonArrayの内容はすべてJsonNumberである必要があります
     *
     * @param val 変換するJsonArray
     * @return DoubleのList
     */
    public static List<Double> toDoubleList(JsonArray val) {
        return toList(val, JsonHelper::toDouble);
    }

    /**
     * JsonArrayをBigDecimalのListに変換します<br>
     * JsonArrayの内容はすべてJsonNumberである必要があります
     *
     * @param val 変換するJsonArray
     * @return BigDecimalのList
     */
    public static List<BigDecimal> toBigDecimalList(JsonArray val) {
        return toList(val, JsonHelper::toBigDecimal);
    }

    /**
     * JsonArrayをStringのListに変換します<br>
     *
     * @param val 変換するJsonArray
     * @return StringのList
     */
    public static List<String> toStringList(JsonArray val) {
        return toList(val, JsonHelper::toString);
    }

    /**
     * JsonArrayをListに変換します
     *
     * @param array 変換するJsonArray
     * @param function 変換Function
     * @return 変換後のList
     */
    @SuppressWarnings("unchecked")
    public static <T extends JsonValue, R> List<R> toList(JsonArray array, Function<T, R> function) {
        List<R> list = new ArrayList<>();
        for (JsonValue val : array) {
            list.add(function.apply((T) val));
        }
        return list;
    }

    /**
     * JsonArrayをMapに変換します
     *
     * @param array 変換するJsonArray
     * @param keyMapper valueMapperで変換したオブジェクトからキーを取り出すFunction
     * @param valueMapper array内のJsonValueを変換するFunction
     * @return 変換後のMap
     */
    @SuppressWarnings("unchecked")
    public static <T extends JsonValue, K, R> Map<K, R> toMap(JsonArray array, Function<R, K> keyMapper,
            Function<T, R> valueMapper) {
        Map<K, R> map = new HashMap<>();
        for (JsonValue val : array) {
            R r = valueMapper.apply((T) val);
            map.put(keyMapper.apply(r), r);
        }
        return map;
    }

    /**
     * JsonObjectから別のオブジェクトへの単方向バインディングを提供します。<br>
     * <br>
     * 次の例はbeanにJSONからの値を設定する例です。<br>
     * JSON例<br>
     * <pre><code>{"api_id" : 558, "api_name" : "深海復讐艦攻改", "api_type" : [ 3, 5, 8, 8 ]}</code></pre>
     * Javaコード例<br>
     * <pre><code>JsonHelper.bind(json)
     *      .set("api_id", bean::setId, JsonHelper::toInteger)
     *      .set("api_name", bean::setName, JsonHelper::toString)
     *      .set("api_type", bean::setType, JsonHelper::toIntegerList);</code></pre>
     *
     * @param json JsonObject
     * @return {@link Bind}
     */
    public static Bind bind(JsonObject json) {
        return new Bind(json);
    }

    /**
     * JsonObjectから別のオブジェクトへの単方向バインディングを提供します。<br>
     *
     */
    public static class Bind {

        private JsonObject json;

        /**
         * コンストラクター
         *
         * @param json JsonObject
         */
        private Bind(JsonObject json) {
            this.json = json;
        }

        /**
         * keyで取得したJsonValueをconverterで変換したものをconsumerへ設定します<br>
         *
         * @param <T> JsonObject#get(Object) の戻り値の型
         * @param <R> converterの戻り値の型
         * @param key JsonObjectから取得するキー
         * @param consumer converterの戻り値を消費する {@link Consumer}
         * @param converter JsonValueを変換する {@link Function}
         * @return {@link Bind}
         */
        @SuppressWarnings("unchecked")
        public <T extends JsonValue, R> Bind set(String key, Consumer<R> consumer, Function<T, R> converter) {
            JsonValue val = this.json.get(key);
            if (val != null) {
                consumer.accept(converter.apply((T) val));
            }
            return this;
        }
    }
}
