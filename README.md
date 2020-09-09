航海日誌 (logbook-kai)
--
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/sanaehirotaka/logbook-kai)](https://github.com/sanaehirotaka/logbook-kai/releases/latest)
[![GitHub](https://img.shields.io/github/license/sanaehirotaka/logbook-kai)](LICENSE)
[![GitHub All Releases](https://img.shields.io/github/downloads/sanaehirotaka/logbook-kai/total)](https://github.com/sanaehirotaka/logbook-kai/releases)
[![GitHub Release Date](https://img.shields.io/github/release-date/sanaehirotaka/logbook-kai)](https://github.com/sanaehirotaka/logbook-kai/releases)

## ****重要なお知らせ****
v20.9.2 以降のバージョンはオリジナルの [sanaehirotaka さんのリポジトリ](https://github.com/sanaehirotaka/logbook-kai/)ではなく
[Sdk0815 の fork](https://github.com/Sdk0815/logbook-kai/)にて開発を行います。
最新バージョンも[こちら](https://github.com/Sdk0815/logbook-kai/releases)からダウンロードしてください。
今後は[Issue（問題報告・要望）](https://github.com/Sdk0815/logbook-kai/issues)や[Pull Request（変更要求）](https://github.com/Sdk0815/logbook-kai/pulls)などもそちらにオープンしていただきますようお願いします。

### 概要

**航海日誌 (logbook-kai)** は、「艦隊これくしょん ～艦これ～」をより遊びやすくするための外部ツールです。

画面がコンパクトなのが特徴です。

![メイン画面](images/overview.png)

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


### 動作環境
![Java](https://img.shields.io/badge/-Java-007396.svg?logo=java)
![Windows](https://img.shields.io/badge/-Windows-0078D6.svg?logo=windows)
![Debian](https://img.shields.io/badge/-Debian-A81D33.svg?logo=debian)
![Redhat](https://img.shields.io/badge/-Redhat-EE0000.svg?logo=red-hat)
![macOS](https://img.shields.io/badge/-macOS-333333.svg?logo=apple)

Java 8u40以降のJava8がインストールされたWindows,LinuxまたはmacOSが必要です。

**次のJavaVMで動作確認されています。**
- **[Liberica JDK version 8](https://bell-sw.com/pages/java-8u232/)**
   - 新規に導入する場合、こちらを推奨します。
- [Oracle JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
   - ダウンロードにOTNアカウントが必要です。

次のJavaVMではご利用いただけません。
- バージョンが8ではないJavaVM
- Amazon Corretto : 一部機能が動作しません(集計機能・自動アップデートが利用不可)。
- AdoptOpenJDK : ライブラリが不足しているため起動しません。

### [ダウンロード](https://github.com/sanaehirotaka/logbook-kai/releases)

**ご注意ください**

**初期の状態では艦娘の画像が表示出来ません。必ず**[FAQ](faq.md)**をお読みください。**

### [ブラウザの設定(必須)](how-to-preference.md)

### [FAQ](faq.md)

#### プラグイン
* [Pushbullet Plugin](https://github.com/rsky/logbook-kai-plugins)
  * 遠征・入渠の通知をiPhone/Android端末へプッシュ通知することが可能になります。

### スクリーンショット

* メイン画面

![メイン画面](images/overview.png)

* 所有装備一覧

![所有装備一覧そのいち](images/items1.png)
![所有装備一覧そのに](images/items2.png)

* 戦闘ログ

![戦闘ログそのいち](images/battlelog1.png)
![戦闘ログそのに](images/battlelog2.png)

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
