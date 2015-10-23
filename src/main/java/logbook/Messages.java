package logbook;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import logbook.plugin.PluginContainer;

public class Messages {
    private static final String BUNDLE_NAME = "logbook.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,
            Locale.getDefault(),
            PluginContainer.getInstance().getClassLoader());

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
