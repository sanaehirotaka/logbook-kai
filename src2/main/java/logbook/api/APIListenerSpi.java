package logbook.api;

import javax.json.JsonObject;

import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * JSONオブジェクトを受け取るリスナーです。<br>
 * <br>
 * 実装クラスはServiceLoaderより取得されます。
 * 実装クラスが呼び出されるようにするには  META-INF/services/logbook.api.APIListenerSpi に
 * 実装クラスの完全修飾名を記述します
 *
 * @see logbook.api.API
 * @see logbook.internal.APIListener
 */
public interface APIListenerSpi {

    /**
     * JSONオブジェクトを受け取ります。<br>
     *
     * 実装クラスに{@link API}注釈が付与されている場合、{@link API}注釈が持つURIに対してのみ実装クラスが呼び出されます。
     * 実装クラスに{@link API}注釈が付与されていない場合、すべてのURIに対して実装クラスが呼び出されます。
     *
     * @param json APIのレスポンスに含まれるJSONオブジェクト
     * @param req リクエスト
     * @param res レスポンス
     * @see logbook.api.API
     */
    void accept(JsonObject json, RequestMetaData req, ResponseMetaData res);

}
