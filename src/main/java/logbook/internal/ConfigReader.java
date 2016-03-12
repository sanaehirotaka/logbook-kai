package logbook.internal;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.Messages;

/**
 * 設定読み込み
 * @param <T> Bean型パラメーター
 */
final class ConfigReader<T> {

    private final Path path;

    ConfigReader(Path path) {
        this.path = path;
    }

    @SuppressWarnings("unchecked")
    T read(Class<T> clazz) {
        T instance = null;
        Path filepath = this.path;
        try {
            if (!Files.isReadable(filepath) || (Files.size(filepath) <= 0)) {
                // ファイルが読み込めないまたはサイズがゼロの場合バックアップファイルを読み込む
                filepath = filepath.resolveSibling(filepath.getFileName() + ".backup"); //$NON-NLS-1$
            }
            if (Files.isReadable(filepath)) {
                try (InputStream in = new BufferedInputStream(Files.newInputStream(filepath))) {
                    try (XMLDecoder encoder = new XMLDecoder(in, this.getListener())) {
                        instance = (T) encoder.readObject();
                    }
                }
            }
        } catch (IOException e) {
            this.getListener().exceptionThrown(e);
        }
        return instance;
    }

    private ExceptionListener getListener() {
        return e -> LoggerHolder.LOG.warn(Messages.getString("ConfigReader.1"), e); //$NON-NLS-1$
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ConfigReader.class);
    }
}