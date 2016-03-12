package logbook.internal;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.Messages;

/**
 * 設定書き込み
 */
final class ConfigWriter {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;

    private final Path path;

    ConfigWriter(Path path) {
        this.path = path;
    }

    void write(Object instance) {
        try {
            if (!Files.exists(this.path)) {
                Path parent = this.path.getParent();
                if (parent != null) {
                    if (!Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                }
            }
            Path backup = this.path.resolveSibling(this.path.getFileName() + ".backup"); //$NON-NLS-1$
            if (Files.exists(this.path) && (Files.size(this.path) > 0)) {
                // ファイルが存在してかつサイズが0を超える場合、ファイルをバックアップにリネームする
                Files.move(this.path, backup, StandardCopyOption.REPLACE_EXISTING);
            }
            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(this.path,
                    StandardOpenOption.CREATE), DEFAULT_BUFFER_SIZE)) {
                try (XMLEncoder encoder = new XMLEncoder(out)) {
                    encoder.setExceptionListener(this.getListener());
                    encoder.writeObject(instance);
                }
            }
        } catch (IOException e) {
            this.getListener().exceptionThrown(e);
        }
    }

    private ExceptionListener getListener() {
        return e -> LoggerHolder.LOG.warn(Messages.getString("ConfigWriter.1"), e); //$NON-NLS-1$
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ConfigWriter.class);
    }
}