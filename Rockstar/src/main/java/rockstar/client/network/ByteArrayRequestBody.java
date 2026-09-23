package rockstar.client.network;


import rockstar.client.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.annotation.Nonnull;
import rockstar.client.network.MediaTypes;
import rockstar.client.network.RequestBody;
import rockstar.client.network.MediaType;

public class ByteArrayRequestBody
extends RequestBody {
    private final byte[] internalField0609;
    private final int internalField0227;
    private final int internalField0228;

    public ByteArrayRequestBody(byte[] byArray) {
        this(byArray, 0, byArray.length);
    }

    public ByteArrayRequestBody(byte[] byArray, int n, int n2) {
        super(MediaTypes.internalField1097);
        this.internalField0609 = byArray;
        this.internalField0227 = n;
        this.internalField0228 = n2;
    }

    public ByteArrayRequestBody(MediaType typedValue038, byte[] byArray) {
        this(typedValue038, byArray, 0, byArray.length);
    }

    public ByteArrayRequestBody(MediaType typedValue038, byte[] byArray, int n, int n2) {
        super(typedValue038);
        this.internalField0609 = byArray;
        this.internalField0227 = n;
        this.internalField0228 = n2;
    }

    @Override
    public boolean internalMethod02204() {
        return true;
    }

    @Override
    public int internalMethod02203() {
        return this.internalField0609.length;
    }

    @Override
    @Nonnull
    protected InputStream internalMethod00691() {
        return new ByteArrayInputStream(this.internalField0609, this.internalField0227, this.internalField0228);
    }
}

