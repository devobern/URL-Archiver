package ch.bfh.controller;

import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility for extracting URLs from a given text string.
 * This class uses a regular expression to identify and extract URLs, focusing primarily on HTTP and HTTPS formats.
 * It is capable of handling multiple URLs within a single text string and ensures uniqueness of the extracted URLs.
 */
public class URLExtractor {
    public URLExtractor() {
    }

    /**
     * Extracts a set of unique URLs from the provided text string.
     * The method uses a regular expression to match HTTP and HTTPS URLs within the text and collects them into a set to ensure uniqueness.
     *
     * @param text The string from which URLs are to be extracted.
     * @return A Set of unique URLs found in the provided text. If no URLs are found, an empty Set is returned.
     */
    public static Set<String> extractURLs(String text) {
        Set<String> urls = new HashSet<>();
        // Regular expression for URL
        Pattern pattern = Pattern.compile(
                "(https?://[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            urls.add(matcher.group());
        }

        return urls;
    }
}
