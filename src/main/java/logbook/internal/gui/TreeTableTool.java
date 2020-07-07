package logbook.internal.gui;

import javafx.scene.control.TreeTableView;

public class TreeTableTool {
    /**
     * テーブル列の表示・非表示の設定を行う
     * @param table テーブル
     * @param key テーブルのキー名
     */
    static void setVisible(TreeTableView<?> table, String key) {
        //Tools.Trees.setVisible(table, key);   // not yet implemented
        Tools.Trees.setWidth(table, key);
        Tools.Trees.setSortOrder(table, key);
    }
}
