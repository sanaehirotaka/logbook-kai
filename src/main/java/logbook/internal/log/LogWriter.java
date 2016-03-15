package logbook.internal.log;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * ログ書き込みをサポートします
 *
 * @param <T> 書き込むオブジェクト
 */
public abstract class LogWriter<T> {

    /** CR+LF */
    protected static final String CRLF = "\r\n";

    /** LF */
    protected static final String LF = "\n";

    /**
     * ファイルの新規作成時に先頭に書き込まれるヘッダー
     *
     * @return ヘッダー
     */
    abstract protected String header();

    /**
     * ログファイルへのパス
     *
     * @return ファイルパス
     */
    abstract protected Path filePath();

    /**
     * 代替ログファイルへのパス(ログファイルへ書き込めない場合に使用)
     *
     * @return ファイルパス
     */
    abstract protected Path alterFilePath();

    /**
     * ログファイルの文字コード
     *
     * @return 文字コード
     */
    protected Charset charset() {
        return Charset.forName("MS932");
    }

    /**
     * ファイルを開く方法を指定するオプション
     *
     * @return オプション
     */
    protected OpenOption[] openOption() {
        return new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.APPEND };
    }

    /**
     * 行の区切り文字
     *
     * @return 区切り文字
     */
    protected String delimiter() {
        return CRLF;
    }

    /**
     * オブジェクトをログファイルに書き込みます
     *
     * @param obj 書き込むオブジェクト
     * @throws IOException 入出力エラーが発生した場合
     */
    protected void write(T obj) throws IOException {
        String line = obj.toString();

        try {
            this.write(this.filePath(), line);
        } catch (IOException e) {
            this.write(this.alterFilePath(), line);
        }
    }

    private void write(Path path, String line) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        }
        try (OutputStream writer = Files.newOutputStream(path, this.openOption())) {
            if (!Files.exists(path) || (Files.size(path) <= 0)) {
                writer.write(this.header().getBytes(this.charset()));
                writer.write(this.delimiter().getBytes(this.charset()));
            }
            writer.write(line.getBytes(this.charset()));
            writer.write(this.delimiter().getBytes(this.charset()));
        }
    }
}
