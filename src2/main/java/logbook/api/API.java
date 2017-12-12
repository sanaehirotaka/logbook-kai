package logbook.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code APIListenerSpi}が受け入れるAPIのURIを表す注釈です。<br>
 * 例えば次の記述は開発と建造時のみに受け入れることを表します。<br>
 * <pre><code>
 * {@literal @API({"/kcsapi/api_req_kousyou/createitem", "/kcsapi/api_req_kousyou/createship"})}
 * </code></pre>
 *
 * @see logbook.api.APIListenerSpi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface API {

    /**
     * APIのURIを返します
     *
     * @return APIのURI
     */
    String[] value();

}
