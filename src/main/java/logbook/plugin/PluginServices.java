package logbook.plugin;

import java.io.InputStream;
import java.net.URL;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * サービスプロバイダに関するメソッドを集めたクラス
 *
 */
public final class PluginServices {

    private PluginServices() {
    }

    /**
     * プラグインコンテナーのクラスローダーを返します
     *
     * @return クラスローダー
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = PluginContainer.getInstance().getClassLoader();
        return classLoader;
    }

    /**
     * サービスプロバイダを取得します。
     *
     * @param <T> サービスプロバイダ
     * @param clazz プラグインのインターフェイス
     * @return clazzで指定されたサービスプロバイダインスタンス
     */
    public static <T> Stream<T> instances(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz, getClassLoader());

        return StreamSupport.stream(loader.spliterator(), false);
    }

    /**
     * 指定された名前を持つリソースを検索します。
     *
     * @param name リソース名
     * @return リソースを読み込むためのURL
     * @see ClassLoader#getResource(String)
     */
    public static URL getResource(String name) {
        return getClassLoader().getResource(name);
    }

    /**
     * 指定されたリソースを読み込む入力ストリームを返します。
     *
     * @param name リソース名
     * @return リソースを読み込むための入力ストリーム
     * @see ClassLoader#getResourceAsStream(String)
     */
    public static InputStream getResourceAsStream(String name) {
        return getClassLoader().getResourceAsStream(name);
    }

    /**
     * 任務のリソースを取得します。
     * （どのコードが任務のリソースを使用しているかわかりやすくするため専用のメソッドを用意）
     * 
     * @param questNo 任務No
     * @return リソースを読み込むためのURL
     */
    public static URL getQuestResource(int questNo) {
        return getResource("logbook/quest/" + questNo + ".json");
    }

    /**
     * 任務のリソースを読み込む入力ストリームを返します。
     *（どのコードが任務のリソースを使用しているかわかりやすくするため専用のメソッドを用意）
     *
     * @param questNo 任務No
     * @return リソースを読み込むための入力ストリーム
     * @see ClassLoader#getResourceAsStream(String)
     */
    public static InputStream getQuestResourceAsStream(int questNo) {
        return getResourceAsStream("logbook/quest/" + questNo + ".json");
    }

}
