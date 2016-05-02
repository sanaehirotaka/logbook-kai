航海日誌 (logbook-kai)
--

**航海日誌 (logbook-kai)** は、「艦隊これくしょん ～艦これ～」をより遊びやすくするための外部ツールです。

画面がコンパクトなのが特徴です。

![メイン画面](https://kancolle.sanaechan.net/logbook-kai/overview.png "メイン画面")

### 航海日誌 について

航海日誌 では[Jetty](http://www.eclipse.org/jetty/) で通信内容をキャプチャして内容を解析／表示します。
プロキシ設定を行うことで別のツールと連携することも可能です。

**「艦隊これくしょん ～艦これ～」サーバーに対する通信内容の改変、追加の通信等は一切行っていません。**

MIT ライセンスの下で公開する、自由ソフトウェアです。

### 主な機能

* 遠征・入渠の通知機能 : 1分前になると自動的に通知します。
* 海戦・ドロップ報告書 : 戦闘の状況、ドロップ艦娘などの情報の収集を行えます。
* 所有装備一覧 : 誰がどの装備を持っているかを簡単に確認することが出来ます。
* 所有艦娘一覧 : 艦娘の各種パラメータ(コンディション、制空値、火力値等)の閲覧を行うことが出来ます。
* お風呂に入りたい艦娘 : 修理が必要な艦娘の時間と必要資材を一覧で見ることが出来ます。

### [ダウンロード](https://github.com/sanaehirotaka/logbook-kai/releases)

**ご注意ください**

**初期の状態では艦娘の画像が表示出来ません。必ず**[FAQ](faq.md)**をお読みください。**

### [ブラウザの設定(必須)](how-to-preference.md)

### 動作環境

Java 8u40以降がインストールされた環境で動作します

* Windows 7 以降
* Linux
* MacOS

### [FAQ](faq.md)

### スクリーンショット

* メイン画面

![メイン画面](https://kancolle.sanaechan.net/logbook-kai/overview.png)

![艦隊タブ](https://kancolle.sanaechan.net/logbook-kai/fleet-tab2.jpg)

* 所有装備一覧

![所有装備一覧](https://kancolle.sanaechan.net/logbook-kai/items.png)

* 所有艦娘一覧

![所有艦娘一覧](https://kancolle.sanaechan.net/logbook-kai/ships.png)

* 戦闘ログ

![戦闘ログ](https://kancolle.sanaechan.net/logbook-kai/battle_log.png)

![戦闘ログ詳細](https://kancolle.sanaechan.net/logbook-kai/battlelog-view1.jpg)

* 経験値計算機

![経験値計算機](https://kancolle.sanaechan.net/logbook-kai/calc_exp.png)

* 資材チャート

![資材チャート](https://kancolle.sanaechan.net/logbook-kai/resource_chart.png)

* 通知機能

![通知機能](https://kancolle.sanaechan.net/logbook-kai/notify.png)

### 開発者向け

#### [ビルド方法](how-to-build.md)

#### [プラグイン開発](how-to-develop.md)

### ライセンス

* [The MIT License (MIT)](LICENSE)

MIT ライセンスの下で公開する、自由ソフトウェアです。

### 使用ライブラリとライセンス

以下のライブラリを使用しています。

#### [JSON Processing(JSR 353)](https://jsonp.java.net/)

* COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL - Version 1.1)
* GNU General Public License (GPL - Version 2, June 1991) with the Classpath Exception
* **ライセンス全文 :** [https://jsonp.java.net/license.html](https://jsonp.java.net/license.html)

#### [Jetty](http://www.eclipse.org/jetty/)

* Apache License 2.0
* Eclipse Public License 1.0
* **ライセンス全文 :** [http://www.eclipse.org/jetty/licenses.php](http://www.eclipse.org/jetty/licenses.php)

#### [commons-logging](https://commons.apache.org/proper/commons-logging/)

* Apache License 2.0
* **ライセンス全文 :** [http://www.apache.org/licenses/](http://www.apache.org/licenses/)

#### [Apache Log4j 2](http://logging.apache.org/log4j/2.x/)

* Apache License 2.0
* **ライセンス全文 :** [http://logging.apache.org/log4j/2.x/license.html](http://logging.apache.org/log4j/2.x/license.html)

#### [ControlsFX](http://fxexperience.com/controlsfx/)

* The BSD 3-Clause License
* **ライセンス全文 :** [https://bitbucket.org/controlsfx/controlsfx/src/default/license.txt?fileviewer=file-view-default](https://bitbucket.org/controlsfx/controlsfx/src/default/license.txt?fileviewer=file-view-default)

#### [JPEXS Free Flash Decompiler](https://www.free-decompiler.com/flash/)

* LGPLv3
* **ライセンス全文 :** [https://www.free-decompiler.com/flash/license_library/](https://www.free-decompiler.com/flash/license_library/)

