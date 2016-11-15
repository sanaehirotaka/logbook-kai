package logbook.proxy;

/**
 * プロキシサーバーの抽象クラスです。<br>
 * <br>
 * 実装クラスはServiceLoaderより取得されます。
 * 実装クラスが呼び出されるようにするには  META-INF/services/logbook.proxy.ProxyServerSpi に
 * 実装クラスの完全修飾名を記述します
 * <br>
 * プロキシサーバーの実装はデーモンスレッドとして実行され、
 * アプリケーションの終了要求時にプロキシサーバースレッドに対して割り込みを行います。<br>
 * プロキシサーバーの実装は割り込みに対して適切に終了処理を行うようにしてください。
 */
public interface ProxyServerSpi extends Runnable {

}
