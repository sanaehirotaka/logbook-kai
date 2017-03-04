package logbook.plugin;

import java.beans.ExceptionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Jarファイルベースのプラグインです
 *
 */
public class JarBasedPlugin {

    private final URL url;

    private final Manifest manifest;

    private final String digest;

    /**
     * 指定したpathからプラグインを作成します
     * @param path Jarファイルのパス
     * @throws IOException Jarファイルの解析中に入出力エラーが発生した場合
     */
    JarBasedPlugin(Path path) throws IOException {
        this.url = path.toUri().toURL();
        this.digest = new String(encodeHex(digest(path)));

        try (JarFile file = new JarFile(path.toFile())) {
            Manifest manifest = file.getManifest();
            if (manifest != null) {
                this.manifest = (Manifest) manifest.clone();
            } else {
                this.manifest = new Manifest();
            }
        }
    }

    /**
     * このプラグインのURL表現を返します
     * @return URL
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * このプラグインの名称を返します<br>
     * JARファイル・マニフェストの次の属性を順に探し最初に見つかった属性の値を返します<br>
     * <ul>
     * <li>Bundle-Name
     * <li>Implementation-Title
     * <li>Specification-Title
     * </ul>
     * @return 名称 見つからなかった場合、空の文字列
     */
    public String getName() {
        return this.getAttributeValue("Bundle-Name", "Implementation-Title", "Specification-Title"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * このプラグインのベンダー名を返します<br>
     * JARファイル・マニフェストの次の属性を順に探し最初に見つかった属性の値を返します<br>
     * <ul>
     * <li>Bundle-Vendor
     * <li>IImplementation-Vendor
     * <li>Specification-Vendor
     * </ul>
     * @return ベンダー名 見つからなかった場合、空の文字列
     */
    public String getVendor() {
        return this.getAttributeValue("Bundle-Vendor", "Implementation-Vendor", "Specification-Vendor"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * このプラグインのバージョンを返します<br>
     * JARファイル・マニフェストの次の属性を順に探し最初に見つかった属性の値を返します<br>
     * <ul>
     * <li>Bundle-Version
     * <li>Implementation-Version
     * <li>Specification-Version
     * </ul>
     * @return バージョン 見つからなかった場合、空の文字列
     */
    public String getVersion() {
        return this.getAttributeValue("Bundle-Version", "Implementation-Version", "Specification-Version"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * このプラグインのライセンスを返します<br>
     * JARファイル・マニフェストの次の属性を順に探し最初に見つかった属性の値を返します<br>
     * <ul>
     * <li>Bundle-License
     * </ul>
     * @return ライセンス 見つからなかった場合、空の文字列
     */
    public String getLicense() {
        return this.getAttributeValue("Bundle-License"); //$NON-NLS-1$
    }

    /**
     * このプラグインのSHA-256値を取得します。
     * @return digest SHA-256値
     */
    public String getDigest() {
        return this.digest;
    }

    private String getAttributeValue(String... atters) {
        if (this.manifest != null) {
            Attributes attributes = this.manifest.getMainAttributes();
            for (String name : atters) {
                Object value = attributes.getValue(name);
                if (value != null) {
                    return value.toString();
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    public static JarBasedPlugin toJarBasedPlugin(Path p, ExceptionListener listener) {
        try {
            return new JarBasedPlugin(p);
        } catch (IOException e) {
            listener.exceptionThrown(e);
            return null;
        }
    }

    private static byte[] digest(Path path) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = in.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
        }
        return digest.digest();
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; i++) {
            out[(j++)] = DIGITS_LOWER[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = DIGITS_LOWER[(0xF & data[i])];
        }
        return out;
    }

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };
}
