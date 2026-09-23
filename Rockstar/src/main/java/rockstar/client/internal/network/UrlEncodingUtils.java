package rockstar.client.internal.network;


import rockstar.client.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Generated;

public final class UrlEncodingUtils {
    public static String internalMethod06697(String string) {
        return UrlEncodingUtils.internalMethod04534(string, StandardCharsets.UTF_8);
    }

    public static String internalMethod04534(String string, Charset charset) {
        return URLEncoder.encode(string, charset);
    }

    public static String internalMethod00033(String string) {
        return UrlEncodingUtils.internalMethod02238(string, StandardCharsets.UTF_8);
    }

    public static String internalMethod02238(String string, Charset charset) {
        return URLDecoder.decode(string, charset);
    }

    @Generated
    private UrlEncodingUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
