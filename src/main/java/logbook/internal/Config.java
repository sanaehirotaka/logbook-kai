package logbook.internal;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * アプリケーションの設定を読み書きします
 *
 */
public final class Config {

    private static final Path CONFIG_DIR = Paths.get("./config"); //$NON-NLS-1$

    private static final Config DEFAULT = new Config(CONFIG_DIR);

    private final Path dir;

    private final Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;

    /**
     * アプリケーション設定の読み書きを指定のディレクトリで行います
     *
     * @param dir アプリケーション設定ディレクトリ
     */
    public Config(Path dir) {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.dir = dir;
    }

    /**
     * clazzで指定された型からインスタンスを復元します
     *
     * @param <T> Bean型
     * @param clazz Bean型 Classオブジェクト
     * @param def デフォルト値を供給するSupplier
     * @return 設定
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz, Supplier<T> def) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(def);

        T instance = (T) this.map.computeIfAbsent(clazz, key -> {
            T v = this.read((Class<T>) key);
            if (v == null) {
                v = def.get();
            }
            return v;
        });
        return instance;
    }

    /**
     * 読み込まれたすべてのインスタンスをファイルに書き込みます
     */
    public void store() {
        this.map.entrySet()
                .forEach(this::store);
    }

    private void store(Entry<Class<?>, ?> entry) {
        this.write(entry.getKey(), entry.getValue());
    }

    private <T> T read(Class<T> clazz) {
        T instance = null;
        Path filepath;

        // try read from JSON
        filepath = this.jsonPath(clazz);
        try {
            if (!Files.isReadable(filepath) || (Files.size(filepath) <= 0)) {
                // ファイルが読み込めないまたはサイズがゼロの場合バックアップファイルを読み込む
                filepath = this.backupPath(filepath);
            }
            if (Files.isReadable(filepath)) {
                try (Reader reader = Files.newBufferedReader(filepath)) {
                    instance = this.mapper.readValue(reader, clazz);
                }
            }
        } catch (Exception e) {
            instance = null;
            LoggerHolder.get().warn("アプリケーションの設定を読み込み中に例外が発生", e); //$NON-NLS-1$
        }
        return instance;
    }

    private void write(Class<?> clazz, Object instance) {
        try {
            Path filepath = this.jsonPath(clazz);

            // create parent directory
            if (!Files.exists(filepath)) {
                Path parent = filepath.getParent();
                if (parent != null) {
                    if (!Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                }
            }

            // write JSON
            if (Files.exists(filepath) && (Files.size(filepath) > 0)) {
                Path backup = this.backupPath(filepath);
                // ファイルが存在してかつサイズが0を超える場合、ファイルをバックアップにリネームする
                Files.move(filepath, backup, StandardCopyOption.REPLACE_EXISTING);
            }
            try (Writer writer = Files.newBufferedWriter(filepath, StandardOpenOption.CREATE)) {
                this.mapper.writeValue(writer, instance);
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("アプリケーションの設定を読み込み中に例外が発生", e); //$NON-NLS-1$
        }
    }

    private Path jsonPath(Class<?> clazz) {
        return this.dir.resolve(clazz.getCanonicalName() + ".json"); //$NON-NLS-1$
    }

    private Path backupPath(Path filepath) {
        return filepath.resolveSibling(filepath.getFileName() + ".backup"); //$NON-NLS-1$
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから設定を取得します
     *
     * @return アプリケーションのデフォルト設定ディレクトリ
     */
    public static Config getDefault() {
        return DEFAULT;
    }
}
