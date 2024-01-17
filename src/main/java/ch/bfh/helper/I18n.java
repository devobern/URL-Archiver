package ch.bfh.helper;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides internationalization (i18n) utility methods for retrieving localized strings.
 */
public class I18n {
    private static final String BASE_NAME = "messages";
    private static ResourceBundle bundle;

    /**
     * Retrieves a resource bundle for the specified locale. If the bundle is not already loaded
     * or if the provided locale differs from the currently loaded one, it reloads the bundle.
     *
     * @param locale the desired locale for the resource bundle
     * @return the resource bundle for the specified locale
     */
    public static ResourceBundle getResourceBundle(Locale locale) {
        if (bundle == null || !bundle.getLocale().equals(locale)) {
            bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        }
        return bundle;
    }

    /**
     * Retrieves a localized string for the given key from the current resource bundle.
     * If the key is not found, it returns the key enclosed in exclamation marks.
     *
     * @param key the key for the desired localized string
     * @return the localized string or the key enclosed in exclamation marks if not found
     */
    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    /**
     * Retrieves a localized formatted string for the given key from the current resource bundle.
     * The string can contain placeholders which will be replaced by the provided params.
     * If the key is not found, it returns the key enclosed in exclamation marks.
     *
     * @param key    the key for the desired localized formatted string
     * @param params the parameters to replace placeholders in the string
     * @return the localized formatted string or the key enclosed in exclamation marks if not found
     */
    public static String getString(String key, Object... params) {
        try {
            return MessageFormat.format(bundle.getString(key), params);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
