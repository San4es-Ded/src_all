package rockstar.client.network;


import rockstar.client.*;
import java.io.InputStream;
import javax.annotation.Nonnull;
import rockstar.client.network.RequestBody;
import rockstar.client.network.MediaType;

public class InputStreamRequestBody
extends RequestBody {
    private final InputStream internalField0483;
    private final int internalField0227;

    public InputStreamRequestBody(MediaType typedValue038, InputStream inputStream, int n) {
        super(typedValue038);
        this.internalField0483 = inputStream;
        this.internalField0227 = n;
    }

    @Override
    public boolean internalMethod02204() {
        return false;
    }

    @Override
    public int internalMethod02203() {
        return this.internalField0227;
    }

    @Override
    @Nonnull
    protected InputStream internalMethod00691() {
        return this.internalField0483;
    }
}

