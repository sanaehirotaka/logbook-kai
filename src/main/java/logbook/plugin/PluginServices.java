package logbook.plugin;

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
     * サービスプロバイダを取得します。
     *
     * @param clazz プラグインのインターフェイス
     * @return clazzで指定されたサービスプロバイダインスタンス
     */
    public static <T> Stream<T> instances(Class<T> clazz) {
        ClassLoader classLoader = PluginContainer.getInstance().getClassLoader();
        ServiceLoader<T> loader = ServiceLoader.load(clazz, classLoader);

        return StreamSupport.stream(loader.spliterator(), false);
    }
}
