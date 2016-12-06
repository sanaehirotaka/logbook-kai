package logbook.plugin.lifecycle;

/**
 * 終了処理の抽象クラスです。<br>
 * <br>
 * 実装クラスはServiceLoaderより取得されます。
 * 実装クラスが呼び出されるようにするには  META-INF/services/logbook.plugin.lifecycle.Shutdown に
 * 実装クラスの完全修飾名を記述します
 * <br>
 * アプリケーションの終了がリクエストされたタイミングで実装が通常スレッドとして実行されます。
 */
public interface Shutdown extends Runnable {

}
