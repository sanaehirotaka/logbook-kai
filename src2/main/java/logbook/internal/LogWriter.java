package logbook.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.bean.AppConfig;

/**
 * ログ書き込みをサポートします
 *
 */
public class LogWriter {

    /** CR+LF */
    public static final String CRLF = "\r\n";

    /** LF */
    public static final String LF = "\n";

    /** デフォルトの文字コード  */
    public static final Charset DEFAULT_CHARSET = Charset.forName("MS932");

    /** デフォルトのファイルを開く方法を指定するオプション */
    private static final OpenOption[] DEFAULT_OPTION = new OpenOption[] { StandardOpenOption.CREATE,
            StandardOpenOption.APPEND };

    /** 文字コード */
    private Charset charset = DEFAULT_CHARSET;

    /** ファイルを開く方法を指定するオプション */
    private OpenOption[] options = DEFAULT_OPTION;

    /** 行の区切り文字 */
    private String delimiter = CRLF;

    /** ヘッダー */
    private String header;

    /** ファイルパス */
    private Path filePath;

    /** 代替ファイルパス */
    private Path alterFilePath;

    /**
     * ファイルの新規作成時に先頭に書き込まれるヘッダー
     *
     * @param header ヘッダー
     * @return LogWriter
     */
    public LogWriter header(String header) {
        this.header = header;
        return this;
    }

    /**
     * ログファイルへのパス
     *
     * @param file ファイルパス
     * @return LogWriter
     */
    public LogWriter file(String file) {
        this.filePath = Paths.get(AppConfig.get().getReportPath()).resolve(file);
        return this;
    }

    /**
     * ログファイルへのパス
     *
     * @param filePath ファイルパス
     * @return LogWriter
     */
    public LogWriter filePath(Path filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * 代替ログファイルへのパス(ログファイルへ書き込めない場合に使用)
     *
     * @param alterFile ファイルパス
     * @return LogWriter
     */
    public LogWriter alterFile(String alterFile) {
        this.alterFilePath = Paths.get(AppConfig.get().getReportPath()).resolve(alterFile);
        return this;
    }

    /**
     * 代替ログファイルへのパス(ログファイルへ書き込めない場合に使用)
     *
     * @param alterFilePath ファイルパス
     * @return LogWriter
     */
    public LogWriter alterFilePath(Path alterFilePath) {
        this.alterFilePath = alterFilePath;
        return this;
    }

    /**
     * ログファイルの文字コード
     *
     * @param charset 文字コード
     * @return LogWriter
     */
    public LogWriter charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * ファイルを開く方法を指定するオプション
     *
     * @param options オプション
     * @return LogWriter
     */
    public LogWriter openOption(OpenOption[] options) {
        this.options = options;
        return this;
    }

    /**
     * 行の区切り文字
     *
     * @param delimiter 区切り文字
     * @return LogWriter
     */
    public LogWriter delimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    /**
     * オブジェクトをファイルに書き込みます
     *
     * @param <T> 書き込むオブジェクトの型
     * @param obj 書き込むオブジェクト
     * @param converter オブジェクトをStringへ変換するコンバーター
     */
    public <T> void write(T obj, Function<T, String> converter) {
        try {
            String line = converter.apply(obj);

            try {
                this.write(this.filePath, line);
            } catch (IOException e) {
                if (this.alterFilePath != null) {
                    this.write(this.alterFilePath, line);
                } else {
                    throw e;
                }
            }
        } catch (IOException e) {
            LoggerHolder.LOG.warn(String.valueOf(this.filePath) + "に書き込めません", e);
        }
    }

    private void write(Path path, String line) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        }
        try (OutputStream writer = Files.newOutputStream(path, this.options)) {
            if (!Files.exists(path) || (Files.size(path) <= 0)) {
                writer.write(this.header.getBytes(this.charset));
                writer.write(this.delimiter.getBytes(this.charset));
            }
            writer.write(line.getBytes(this.charset));
            writer.write(this.delimiter.getBytes(this.charset));
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(LogWriter.class);
    }
}
