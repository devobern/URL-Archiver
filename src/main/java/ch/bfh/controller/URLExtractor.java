package ch.bfh.controller;

import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLExtractor {
    public URLExtractor() {
    }
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
