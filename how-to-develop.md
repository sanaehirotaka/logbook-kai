# プラグイン開発

航海日誌ではプラグイン開発のために以下のインターフェイスを用意しています。

これらの実装クラスのは[ServiceLoader](http://docs.oracle.com/javase/jp/8/docs/api/java/util/ServiceLoader.html)より取得されます。
実装クラスが呼び出されるようにするには  META-INF/services/**インターフェイス名** に実装クラスの完全修飾名を記述します

メイン画面へのGUI要素の追加
* [logbook.plugin.gui.MainCalcMenu](src/main/java/logbook/plugin/gui/MainCalcMenu.java)
* [logbook.plugin.gui.MainCommandMenu](src/main/java/logbook/plugin/gui/MainCommandMenu.java)
* [logbook.plugin.gui.MainExtMenu](src/main/java/logbook/plugin/gui/MainExtMenu.java)

通信のキャプチャ
* [logbook.proxy.ContentListenerSpi](src/main/java/logbook/proxy/ContentListenerSpi.java)

APIのキャプチャ
* [logbook.api.APIListenerSpi](src/main/java/logbook/api/APIListenerSpi.java)

