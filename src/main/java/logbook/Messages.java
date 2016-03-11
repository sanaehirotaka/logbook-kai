package logbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import logbook.plugin.PluginContainer;

/**
 * 国際化対応
 *
 */
public class Messages {

    private static final String BUNDLE_NAME = "logbook.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(
            BUNDLE_NAME,
            Locale.getDefault(),
            PluginContainer.getInstance().getClassLoader(),
            new UTF8Control());

    private Messages() {
    }

    /**
     * 指定されたキーの文字列を取得します
     *
     * @param key 目的の文字列のキー
     * @return 指定されたキーの文字列
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * 指定されたキーの文字列を使ってMessageFormatを作成し、それを使用して指定された引数をフォーマットします
     * これは次の記述と同等です
     * <blockquote>
     *     <code>MessageFormat.format(Messages.getString(key), args)</code>
     * </blockquote>
     *
     * @param key 目的の文字列のキー
     * @param args フォーマットするオブジェクト
     * @return 指定されたキーの文字列
     */
    public static String getString(String key, Object... args) {
        return MessageFormat.format(getString(key), args);
    }

    /**
     * リソース・バンドルを取得します
     *
     * @return リソース・バンドル
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

    /**
     * UTF-8でpropertiesファイルを読むためのResourceBundle.Control
     */
    private static final class UTF8Control extends ResourceBundle.Control {

        @Override
        public ResourceBundle newBundle(
                String baseName,
                Locale locale,
                String format,
                ClassLoader loader,
                boolean reload)
                        throws IOException {

            String bundleName = this.toBundleName(baseName, locale);
            String resourceName = this.toResourceName(bundleName, "properties"); //$NON-NLS-1$

            // get InputStream
            InputStream is;
            try {
                is = AccessController.doPrivileged((PrivilegedExceptionAction<InputStream>) () -> {
                    InputStream stream = null;
                    if (reload) {
                        URL url = loader.getResource(resourceName);
                        if (url != null) {
                            URLConnection con = url.openConnection();
                            con.setUseCaches(false);
                            stream = con.getInputStream();
                        }
                    } else {
                        stream = loader.getResourceAsStream(resourceName);
                    }
                    return stream;
                });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (is != null) {
                try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    try (BufferedReader reader = new BufferedReader(isr)) {
                        return new PropertyResourceBundle(reader);
                    }
                }
            } else {
                return null;
            }
        }
    }
}
