package rockstar.client.internal.core;


import rockstar.client.*;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;

public class ClosingInputStream
extends InputStream {
    private final InputStream internalField0483;
    private final InternalType0485 internalField0679;

    public ClosingInputStream(InputStream inputStream, InternalType0485 nestedValue0173) {
        this.internalField0483 = inputStream;
        this.internalField0679 = nestedValue0173;
    }

    @Override
    public int read() throws IOException {
        return this.internalField0483.read();
    }

    @Override
    public int read(@Nonnull byte[] byArray) throws IOException {
        return this.internalField0483.read(byArray);
    }

    @Override
    public int read(@Nonnull byte[] byArray, int n, int n2) throws IOException {
        return this.internalField0483.read(byArray, n, n2);
    }

    @Override
    public long skip(long l) throws IOException {
        return this.internalField0483.skip(l);
    }

    @Override
    public int available() throws IOException {
        return this.internalField0483.available();
    }

    @Override
    public void close() throws IOException {
        this.internalField0483.close();
        this.internalField0679.close();
    }

    @FunctionalInterface
    public static interface InternalType0485 {
        public void close() throws IOException;
    }
}

