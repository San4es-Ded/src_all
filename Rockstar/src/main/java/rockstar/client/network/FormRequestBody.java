package rockstar.client.network;


import rockstar.client.*;
import rockstar.client.internal.network.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.Generated;
import rockstar.client.internal.network.UrlEncodingUtils;
import rockstar.client.network.MediaTypes;
import rockstar.client.network.RequestBody;

public class FormRequestBody
extends RequestBody {
    private final List<InternalType0462> internalField0416;
    private final Charset internalField0091;

    public FormRequestBody() {
        this(StandardCharsets.UTF_8);
    }

    public FormRequestBody(Map<String, String> map) {
        this(map, StandardCharsets.UTF_8);
    }

    public FormRequestBody(Charset charset) {
        super(MediaTypes.internalField1096);
        this.internalField0416 = new ArrayList<InternalType0462>();
        this.internalField0091 = charset;
    }

    public FormRequestBody(Map<String, String> map, Charset charset) {
        super(MediaTypes.internalField1096);
        this.internalField0416 = map.entrySet().stream().map(entry -> new InternalType0462((String)entry.getKey(), (String)entry.getValue(), charset)).collect(Collectors.toList());
        this.internalField0091 = charset;
    }

    public FormRequestBody internalMethod02627(String string, String string2) {
        this.internalField0416.add(new InternalType0462(string, string2, this.internalField0091));
        this.internalMethod05790();
        return this;
    }

    @Override
    public boolean internalMethod02204() {
        return true;
    }

    @Override
    public int internalMethod02203() {
        int n = this.internalField0416.size() - 1;
        for (InternalType0462 nestedValue2057 : this.internalField0416) {
            n += nestedValue2057.internalMethod07170();
        }
        return n;
    }

    @Override
    @Nonnull
    protected InputStream internalMethod00691() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(this.internalMethod02203());
        for (int i = 0; i < this.internalField0416.size(); ++i) {
            InternalType0462 nestedValue2057 = this.internalField0416.get(i);
            byteArrayOutputStream.write(nestedValue2057.internalMethod06219());
            byteArrayOutputStream.write(61);
            byteArrayOutputStream.write(nestedValue2057.internalMethod06279());
            if (i >= this.internalField0416.size() - 1) continue;
            byteArrayOutputStream.write(38);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    static class InternalType0462 {
        private final byte[] internalField0609;
        private final byte[] internalField0610;

        private InternalType0462(String string, String string2, Charset charset) {
            this.internalField0609 = UrlEncodingUtils.internalMethod06697(string).getBytes(charset);
            this.internalField0610 = UrlEncodingUtils.internalMethod06697(string2).getBytes(charset);
        }

        public int internalMethod07170() {
            return this.internalField0609.length + 1 + this.internalField0610.length;
        }

        @Generated
        public byte[] internalMethod06219() {
            return this.internalField0609;
        }

        @Generated
        public byte[] internalMethod06279() {
            return this.internalField0610;
        }
    }
}

