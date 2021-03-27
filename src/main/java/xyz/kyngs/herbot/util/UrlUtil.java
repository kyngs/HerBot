package xyz.kyngs.herbot.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtil {
    public static String tryNormalizeUrl(String url) {
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException ex) {
            return url;
        }

        URI normalizedUri;
        try {
            var scheme = uri.getScheme();
            if (scheme.equals("http")) {
                scheme = "https";
            }

            normalizedUri = new URI(scheme, uri.getHost(), uri.getPath(), null);
        } catch (URISyntaxException e) {
            return url;
        }

        return normalizedUri.toString();
    }
}
