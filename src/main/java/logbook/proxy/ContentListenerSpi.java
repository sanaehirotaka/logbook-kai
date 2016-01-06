package logbook.proxy;

/**
 * レスポンスボディを受け入れるサービス・プロバイダ・インタフェース(SPI)です<br>
 * <br>
 * まずリクエストに対して{@link #test(RequestMetaData)}が呼び出されます。{@link #test(RequestMetaData)}がtrueを返してかつ、リクエストに対する
 * レスポンスが正常に返ってきた場合に{@link #accept(RequestMetaData, ResponseMetaData)}が呼び出されます。<br>
 * <br>
 * リクエストは並列処理される可能性があるため同期化が必要になることがあります。
 */
public interface ContentListenerSpi {

    /**
     * レスポンスを受け入れるかをテストします
     * @param requestMetaData リクエストに含まれている情報
     * @return 受け入れる場合true
     */
    boolean test(RequestMetaData requestMetaData);

    /**
     * レスポンスを処理します
     * @param requestMetaData リクエストに含まれている情報
     * @param responseMetaData レスポンスに含まれている情報
     */
    void accept(RequestMetaData requestMetaData, ResponseMetaData responseMetaData);
}
