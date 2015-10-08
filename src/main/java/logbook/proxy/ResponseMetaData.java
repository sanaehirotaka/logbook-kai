package logbook.proxy;

import java.util.Collection;
import java.util.Map;

/**
 * レスポンスに含まれている情報を ContentListener に提供するオブジェクト
 *
 */
public interface ResponseMetaData {

    /**
     * レスポンスのステータスコードを取得します
     * @return レスポンスのステータスコード
     */
    public int getStatus();

    /**
     * コンテントタイプを取得します
     * @return コンテントタイプ
     */
    public String getContentType();

    /**
     * レスポンスヘッダを取得します
     * @return レスポンスヘッダのMap
     */
    public Map<String, Collection<String>> getHeaders();
}
