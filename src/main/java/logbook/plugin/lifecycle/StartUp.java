package logbook.plugin.lifecycle;

/**
 * 開始処理の抽象クラスです。<br>
 * <br>
 * 実装クラスはServiceLoaderより取得されます。
 * 実装クラスが呼び出されるようにするには  META-INF/services/logbook.plugin.lifecycle.StartUp に
 * 実装クラスの完全修飾名を記述します
 * <br>
 * アプリケーションが開始されたタイミングで実装がデーモンスレッドとして実行されます。
 */
public interface StartUp extends Runnable {

}
