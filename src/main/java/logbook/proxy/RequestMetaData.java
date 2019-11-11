package logbook.proxy;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * リクエストに含まれている情報を ContentListener に提供するオブジェクト
 *
 */
public interface RequestMetaData {

    /**
     * リクエストに含まれるメッセージボディの MIME タイプを返します
     * @return メッセージボディの MIME タイプ
     */
    String getContentType();

    /**
     * このリクエストを生成した HTTP メソッドの名前を返します
     * @return HTTP メソッド
     */
    String getMethod();

    /**
     * このリクエストから取得できるパラメータを返します
     * @return パラメータのMap
     */
    Map<String, List<String>> getParameterMap();

    /**
     * このリクエストから取得できるパラメータを返します
     *
     * @param key パラメータのキー
     * @return パラメータのキーに対応する値、存在しない場合null
     */
    default String getParameter(String key) {
        return this.getParameter(key, null);
    }

    /**
     * このリクエストから取得できるパラメータを返します
     *
     * @param key パラメータのキー
     * @param def 存在しない場合のデフォルト値
     * @return パラメータのキーに対応する値
     */
    default String getParameter(String key, String def) {
        List<String> v = this.getParameterMap().get(key);
        if (v != null && !v.isEmpty())
            return v.get(0);
        return def;
    }

    /**
     * リクエストされた URL のパスの後ろに含まれているクエリ文字列を返します
     * @return クエリ文字列
     */
    String getQueryString();

    /**
     * この HTTP リクエストの最初の行にある、リクエストの URL のうちプロトコル名からクエリ文字列までの部分を返します
     * @return URI
     */
    String getRequestURI();

    /**
     * リクエストに含まれるメッセージボディを返します
     * @return リクエストに含まれるメッセージボディ
     */
    Optional<InputStream> getRequestBody();
}
