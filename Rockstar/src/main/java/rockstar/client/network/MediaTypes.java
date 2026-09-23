package rockstar.client.network;


import rockstar.client.*;
import java.nio.charset.StandardCharsets;
import lombok.Generated;
import rockstar.client.network.MediaType;

public final class MediaTypes {
    public static final MediaType internalField0270 = new MediaType("*/*");
    public static final MediaType internalField0269 = new MediaType("application/atom+xml", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1096 = new MediaType("application/x-www-form-urlencoded", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1095 = new MediaType("application/json", StandardCharsets.UTF_8);
    public static final MediaType internalField1097 = new MediaType("application/octet-stream");
    public static final MediaType internalField1094 = new MediaType("application/soap+xml", StandardCharsets.UTF_8);
    public static final MediaType internalField1505 = new MediaType("application/svg+xml", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1508 = new MediaType("application/xhtml+xml", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1504 = new MediaType("application/xml", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1511 = new MediaType("image/bmp");
    public static final MediaType internalField1510 = new MediaType("image/gif");
    public static final MediaType internalField1509 = new MediaType("image/jpeg");
    public static final MediaType internalField1507 = new MediaType("image/png");
    public static final MediaType internalField1506 = new MediaType("image/svg+xml");
    public static final MediaType internalField1787 = new MediaType("image/tiff");
    public static final MediaType internalField1790 = new MediaType("image/webp");
    public static final MediaType internalField1791 = new MediaType("multipart/form-data", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1792 = new MediaType("text/html", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1788 = new MediaType("text/plain", StandardCharsets.ISO_8859_1);
    public static final MediaType internalField1789 = new MediaType("text/xml", StandardCharsets.ISO_8859_1);

    @Generated
    private MediaTypes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

