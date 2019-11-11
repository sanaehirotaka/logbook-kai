package logbook.proxy;

import java.io.InputStream;
import java.util.Optional;

/**
 * レスポンスに含まれている情報を ContentListener に提供するオブジェクト
 *
 */
public interface ResponseMetaData {

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
     * レスポンスに含まれるメッセージボディを返します
     * @return レスポンスに含まれるメッセージボディ
     */
    Optional<InputStream> getResponseBody();
}
