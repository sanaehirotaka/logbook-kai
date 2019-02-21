package logbook.plugin.gui;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
     * ソート順を制御する数値を返します
     * 
     * @return ソート順に使われる数値
     */
    default int sortOrder () {
        return Integer.MAX_VALUE;
    }

    /**
     * clazzで指定されたプラグインからGUI要素を取得します。
     *
     * @param clazz プラグインのインターフェイス
     * @return GUI要素
     */
    public static <T extends Plugin<R>, R> List<R> getContent(Class<T> clazz) {
        return PluginServices.instances(clazz)
                .sorted(Comparator.comparingInt(Plugin::sortOrder))
                .map(Plugin::getContent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
