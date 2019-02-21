package logbook.plugin.gui;

/**
 * GUI要素が更新可能であることを表すインターフェイスです
 *
 * @param <T> 受け入れる型
 */
public interface Updateable<T> {

    void updateItem (T item);
}
