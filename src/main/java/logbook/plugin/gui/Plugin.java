package logbook.plugin.gui;

import java.util.List;
import java.util.stream.Collectors;

import logbook.plugin.PluginServices;

/**
 * GUIを持つプラグインのインターフェイスです
 *
 * @param <T> プラグインが返すGUI要素の型
 */
public interface Plugin<T> {

    /**
     * GUI要素を返します
     *
     * @return StageやMenuItemなどのGUI要素
     */
    T getContent();


    /**
     * clazzで指定されたプラグインからGUI要素を取得します。
     *
     * @param clazz プラグインのインターフェイス
     * @return GUI要素
     */
    public static <T extends Plugin<R>, R> List<R> getContent(Class<T> clazz) {
        return PluginServices.instances(clazz)
                .map(Plugin::getContent)
                .collect(Collectors.toList());
    }
}
