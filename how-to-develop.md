# プラグイン開発

航海日誌ではプラグイン開発のために以下のインターフェイスを用意しています。

これらの実装クラスのは[ServiceLoader](http://docs.oracle.com/javase/jp/8/docs/api/java/util/ServiceLoader.html)より取得されます。
実装クラスが呼び出されるようにするには  META-INF/services/**インターフェイス名** に実装クラスの完全修飾名を記述します

## 外部向けインターフェイス

起動時に1度だけ行う処理
 * [logbook.plugin.lifecycle.StartUp](src/main/java/logbook/plugin/lifecycle/StartUp.java)

メイン画面へのGUI要素の追加
 * [logbook.plugin.gui.MainCalcMenu](src/main/java/logbook/plugin/gui/MainCalcMenu.java)
 * [logbook.plugin.gui.MainCommandMenu](src/main/java/logbook/plugin/gui/MainCommandMenu.java)
 * [logbook.plugin.gui.MainExtMenu](src/main/java/logbook/plugin/gui/MainExtMenu.java)

艦隊タブの注釈へのGUI要素の追加
 * [logbook.plugin.gui.FleetTabRemark](src/main/java/logbook/plugin/gui/FleetTabRemark.java)

通信のキャプチャ
 * [logbook.proxy.ContentListenerSpi](src/main/java/logbook/proxy/ContentListenerSpi.java)
  * 関連クラス
    * [logbook.proxy.RequestMetaData](src/main/java/logbook/proxy/RequestMetaData.java)
    * [logbook.proxy.ResponseMetaData](src/main/java/logbook/proxy/ResponseMetaData.java)

APIのキャプチャ
 * [logbook.api.APIListenerSpi](src/main/java/logbook/api/APIListenerSpi.java)

プロキシサーバー
 * [logbook.proxy.ProxyServerSpi](src/main/java/logbook/proxy/ProxyServerSpi.java)
  * プロキシサーバーの実装が複数存在する場合、デフォルト実装以外の実装を1つだけ選択します。実装の選択方法は未定義です。
  * [デフォルト実装](src/main/java/logbook/internal/proxy/NettyProxyServer.java)
