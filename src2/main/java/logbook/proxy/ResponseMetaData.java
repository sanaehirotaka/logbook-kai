package logbook.proxy;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * レスポンスに含まれている情報を ContentListener に提供するオブジェクト
 *
 */
public interface ResponseMetaData extends Serializable {

    /**
     * レスポンスのステータスコードを取得します
     * @return レスポンスのステータスコード
     */
    int getStatus();

    /**
     * コンテントタイプを取得します
     * @return コンテントタイプ
     */
    String getContentType();

    /**
     * レスポンスヘッダを取得します
     * @return レスポンスヘッダのMap
     */
    Map<String, Collection<String>> getHeaders();

    /**
     * レスポンスに含まれるメッセージボディを返します
     * @return レスポンスに含まれるメッセージボディ
     */
    Optional<InputStream> getResponseBody();
}
