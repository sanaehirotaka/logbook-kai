package logbook.update;

import java.util.concurrent.ScheduledExecutorService;

import logbook.internal.ThreadManager;

/**
 * アプリケーション
 *
 */
public final class Launcher {

    /**
     * アプリケーションの起動
     *
     * @param args アプリケーション引数
     */
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        try {
            launcher.initLocal(args);
            Runtime.getRuntime().addShutdownHook(new Thread(launcher::exitLocalThreadPool));
        } finally {
            launcher.exitLocalThreadPool();
        }
    }

    /**
     * アプリケーションの初期化処理
     *
     * @param args アプリケーション引数
     */
    void initLocal(String[] args) {
        Updater.main(args);
    }

    /**
     * スレッドプールの終了処理
     */
    private void exitLocalThreadPool() {
        ScheduledExecutorService executor = ThreadManager.getExecutorService();
        executor.shutdownNow();
    }
}
