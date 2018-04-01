package logbook.internal;

import logbook.bean.AppConfig;
import logbook.plugin.lifecycle.StartUp;

/**
 * アップデートチェック
 *
 */
public class CheckUpdateStartUp implements StartUp {

    @Override
    public void run() {
        if (!AppConfig.get().isCheckUpdate()) {
            return;
        }

        CheckUpdate.run();
    }
}