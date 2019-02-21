package logbook.plugin.gui;

import logbook.bean.DeckPort;

/**
 * 艦隊タブの注釈に追加するプラグインのインターフェイス<br>
 * {@link #getContent()}の戻りの型は{@link javafx.scene.Node}またはtoStringメソッド
 * によって表現可能である必要があります。
 * 
 */
public interface FleetTabRemark extends Plugin<Updateable<DeckPort>> {

}
