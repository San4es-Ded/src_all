package rockstar.client.network;


import rockstar.client.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import org.jetbrains.annotations.ApiStatus;
import rockstar.client.network.ByteArrayRequestBody;
import rockstar.client.network.FileRequestBody;
import rockstar.client.network.InputStreamRequestBody;
import rockstar.client.network.MultipartRequestBody;
import rockstar.client.network.StringRequestBody;
import rockstar.client.network.FormRequestBody;
import rockstar.client.network.MediaType;

public abstract class RequestBody {
    private final MediaType internalField0270;
    private int internalField0227 = 1024;
    private boolean internalField0277 = false;
    private byte[] internalField0609;

    public static RequestBody internalMethod04743(byte[] byArray) {
        return new ByteArrayRequestBody(byArray);
    }

    public static RequestBody internalMethod01613(byte[] byArray, int n, int n2) {
        return new ByteArrayRequestBody(byArray, n, n2);
    }

    public static RequestBody internalMethod01581(String string) {
        return new StringRequestBody(string);
    }

    public static RequestBody internalMethod07036(String string, Charset charset) {
        return new StringRequestBody(string, charset);
    }

    public static RequestBody internalMethod06469(File file) {
        return new FileRequestBody(file);
    }

    public static RequestBody internalMethod04779(String string, String string2) {
        return new FormRequestBody().internalMethod02627(string, string2);
    }

    public static RequestBody internalMethod01010(Map<String, String> map) {
        return new FormRequestBody(map);
    }

    public static MultipartRequestBody internalMethod02023() {
        return new MultipartRequestBody();
    }

    public static InputStreamRequestBody internalMethod06895(MediaType typedValue038, InputStream inputStream, int n) {
        return new InputStreamRequestBody(typedValue038, inputStream, n);
    }

    public RequestBody(MediaType typedValue038) {
        this.internalField0270 = typedValue038;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public final MediaType internalMethod03748() {
        return this.internalMethod05122();
    }

    public final MediaType internalMethod05122() {
        return this.internalField0270;
    }

    public final int internalMethod05813() {
        return this.internalField0227;
    }

    public final RequestBody internalMethod05889(int n) {
        this.internalField0227 = n;
        return this;
    }

    public final void internalMethod07373(@WillNotClose OutputStream outputStream) throws IOException {
        try (InputStream inputStream = this.internalMethod02044();){
            int n;
            byte[] byArray = new byte[this.internalField0227];
            while ((n = inputStream.read(byArray)) >= 0) {
                outputStream.write(byArray, 0, n);
            }
        }
    }

    public final InputStream internalMethod02044() throws IOException {
        if (this.internalField0609 != null) {
            return new ByteArrayInputStream(this.internalField0609);
        }
        if (!this.internalField0277 || this.internalMethod02204()) {
            this.internalField0277 = true;
            return this.internalMethod04357(this.internalMethod00691());
        }
        throw new IOException("This content cannot be streamed multiple times");
    }

    @Nonnull
    public final byte[] internalMethod03451() throws IOException {
        if (this.internalField0609 == null) {
            int n = this.internalMethod02203() > 0 ? this.internalMethod02203() : this.internalField0227;
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(n);){
                this.internalMethod07373(byteArrayOutputStream);
                this.internalField0609 = byteArrayOutputStream.toByteArray();
            }
        }
        return this.internalField0609;
    }

    @Nonnull
    public final String internalMethod04320() throws IOException {
        return this.internalMethod06795(StandardCharsets.UTF_8);
    }

    @Nonnull
    public final String internalMethod06795(Charset charset) throws IOException {
        return new String(this.internalMethod03451(), charset);
    }

    protected final void internalMethod05790() {
        this.internalField0609 = null;
        this.internalField0277 = false;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public final int internalMethod08368() {
        return this.internalMethod02203();
    }

    public abstract boolean internalMethod02204();

    public abstract int internalMethod02203();

    @Nonnull
    protected abstract InputStream internalMethod00691() throws IOException;

    protected InputStream internalMethod04357(InputStream inputStream) throws IOException {
        return inputStream;
    }
}

