package logbook.proxy;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;

/**
 * リクエストに含まれている情報を ContentListener に提供するオブジェクト
 *
 */
public interface RequestMetaData extends Serializable {

    /**
     * リクエストに含まれるメッセージボディの MIME タイプを返します
     * @return メッセージボディの MIME タイプ
     */
    String getContentType();

    /**
     * このリクエストと一緒にクライアントから送られてきた全ての Cookie オブジェクトのコレクションを返します
     * @return クライアントから送られてきた Cookie のコレクション。 リクエストに Cookie が付加されていない場合は 空のコレクション
     */
    Collection<Cookie> getCookies();

    /**
     * リクエストヘッダを返します
     * @return リクエストヘッダのMap
     */
    Map<String, Collection<String>> getHeaders();

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
     * リクエストのプロトコル名とバージョンを返します
     * @return リクエストのプロトコル名とバージョン
     */
    String getProtocol();

    /**
     * リクエストされた URL のパスの後ろに含まれているクエリ文字列を返します
     * @return クエリ文字列
     */
    String getQueryString();

    /**
     * リクエストを送ってきたクライアントまたは最後のプロキシの IP アドレスを返します
     * @return リクエストを送ってきたクライアントの IP アドレス
     */
    String getRemoteAddr();

    /**
     * リクエストを送ったクライアントまたは最後のプロキシの送信元 IP ポートを返します
     * @return リクエストを送ってきたクライアントの IP ポート
     */
    int getRemotePort();

    /**
     * この HTTP リクエストの最初の行にある、リクエストの URL のうちプロトコル名からクエリ文字列までの部分を返します
     * @return URI
     */
    String getRequestURI();

    /**
     * クライアントがこのリクエストを生成するのに使ったURLを返します
     * @return URL
     */
    String getRequestURL();

    /**
     * リクエストのスキーム名を返します
     * @return リクエストのスキーム名
     */
    String getScheme();

    /**
     * リクエストが送られたサーバのホスト名を返します
     * @return リクエストが送られたサーバのホスト名
     */
    String getServerName();

    /**
     * リクエストが送られたサーバのポート番号を返します
     * @return リクエストが送られたサーバのポート番号
     */
    int getServerPort();

    /**
     * リクエストに含まれるメッセージボディを返します
     * @return リクエストに含まれるメッセージボディ
     */
    Optional<InputStream> getRequestBody();
}
