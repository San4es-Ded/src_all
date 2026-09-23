package rockstar.client.internal.core;


import rockstar.client.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import javax.annotation.Nonnull;
import lombok.Generated;

public class QueuedInputStream
extends InputStream {
    private final Queue<InputStream> internalField0877;

    @Override
    public int read() throws IOException {
        if (this.internalField0877.isEmpty()) {
            return -1;
        }
        int n = this.internalField0877.peek().read();
        if (n == -1) {
            this.internalField0877.poll().close();
            return this.read();
        }
        return n;
    }

    @Override
    public int read(@Nonnull byte[] byArray, int n, int n2) throws IOException {
        if (this.internalField0877.isEmpty()) {
            return -1;
        }
        int n3 = this.internalField0877.peek().read(byArray, n, n2);
        if (n3 == -1) {
            this.internalField0877.poll().close();
            return this.read(byArray, n, n2);
        }
        return n3;
    }

    @Override
    public void close() throws IOException {
        while (!this.internalField0877.isEmpty()) {
            this.internalField0877.poll().close();
        }
    }

    @Generated
    public QueuedInputStream(Queue<InputStream> queue) {
        this.internalField0877 = queue;
    }
}

