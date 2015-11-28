package logbook.internal;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;

import logbook.Messages;

/**
 * 設定書き込み
 */
final class ConfigWriter {

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
            try (XMLEncoder encoder = new XMLEncoder(Files.newOutputStream(this.path,
                    StandardOpenOption.CREATE))) {
                encoder.setExceptionListener(this.getListener());
                encoder.writeObject(instance);
            }
        } catch (IOException e) {
            this.getListener().exceptionThrown(e);
        }
    }

    ExceptionListener getListener() {
        return e -> LogManager.getLogger(Config.class).warn(Messages.getString("ConfigWriter.1"), e); //$NON-NLS-1$
    }
}