# ビルド手順

## ライブラリダウンロード

一部のライブラリがMavenのセントラルリポジトリで提供されていないため手動でダウンロードする必要があるライブラリがあります

* ffdec_lib.jar
* cmykjpeg.jar
* LZMA.jar

上記ライブラリは[JPEXS Free Flash Decompiler - Download](https://www.free-decompiler.com/flash/download/)よりZIP版の**lib**フォルダにある同名のファイルを航海日誌ソースの**lib**フォルダに配置します。


## ビルド

以下のコマンドを実行して、ビルドします

```
mvn package
```
