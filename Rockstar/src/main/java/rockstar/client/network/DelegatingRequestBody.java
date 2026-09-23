package rockstar.client.network;


import rockstar.client.*;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import rockstar.client.network.RequestBody;

public class DelegatingRequestBody
extends RequestBody {
    private final RequestBody internalField0061;

    public DelegatingRequestBody(RequestBody typedValue036) {
        super(typedValue036.internalMethod05122());
        this.internalField0061 = typedValue036;
    }

    @Override
    public boolean internalMethod02204() {
        return this.internalField0061.internalMethod02204();
    }

    @Override
    public int internalMethod02203() {
        return this.internalField0061.internalMethod02203();
    }

    @Override
    @Nonnull
    protected InputStream internalMethod00691() throws IOException {
        return this.internalField0061.internalMethod00691();
    }

    @Override
    protected InputStream internalMethod04357(InputStream inputStream) throws IOException {
        return this.internalField0061.internalMethod04357(inputStream);
    }
}

