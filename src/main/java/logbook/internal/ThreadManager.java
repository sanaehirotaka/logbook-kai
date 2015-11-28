package logbook.internal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * スレッドを管理します
 *
 */
public final class ThreadManager {

    /** Executor */
    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(4);

    /**
     * アプリケーションで共有するExecutorService
     * <p>
     * 長時間実行する必要のあるスレッドを登録する場合、割り込みされたかを検知して適切に終了するようにしてください。
     * </p>
     *
     * @return ExecutorService
     */
    public static ScheduledExecutorService getExecutorService() {
        return EXECUTOR;
    }
}
