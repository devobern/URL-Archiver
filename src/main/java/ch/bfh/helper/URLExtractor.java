package ch.bfh.helper;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility for extracting URLs from a given text string.
 * This class uses a regular expression to identify and extract URLs,
 * focusing primarily on HTTP and HTTPS formats. It is capable of handling
 * multiple URLs within a single text string and ensures uniqueness of the extracted URLs.
 */
public class URLExtractor {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?://[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Extracts a set of unique URLs from the provided text string using a default pattern.
     * The method uses a predefined regular expression to match HTTP and HTTPS URLs within the text
     * and collects them into a set to ensure uniqueness.
     *
     * @param text The string from which URLs are to be extracted.
     * @return A Set of unique URLs found in the provided text. If no URLs are found, an empty Set is returned.
     * @throws IllegalArgumentException if the input text is null.
     */
    public static Set<String> extractURLs(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        return extractURLs(text, URL_PATTERN);
    }

    /**
     * Extracts a set of unique URLs from the provided text string using the given pattern.
     * The method uses the specified regular expression to match URLs within the text
     * and collects them into a set to ensure uniqueness.
     *
     * @param text    The string from which URLs are to be extracted.
     * @param pattern The pattern to be used for matching URLs.
     * @return A Set of unique URLs found in the provided text according to the given pattern.
     * If no URLs are found, an empty Set is returned.
     */
    public static Set<String> extractURLs(String text, Pattern pattern) {
        Set<String> urls = new HashSet<>();
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            urls.add(matcher.group());
        }

        return urls;
    }
}
