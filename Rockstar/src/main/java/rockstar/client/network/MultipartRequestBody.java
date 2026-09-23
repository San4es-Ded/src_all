package rockstar.client.network;


import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rockstar.client.internal.core.QueuedInputStream;
import rockstar.client.network.FluentObject;
import rockstar.client.network.RequestBody;
import rockstar.client.network.MediaType;

public class MultipartRequestBody
extends RequestBody {
    private final String internalField0248;
    private final List<InternalType0461> internalField0416 = new ArrayList<InternalType0461>();
    private boolean internalField0277 = false;

    public MultipartRequestBody() {
        this("---" + UUID.randomUUID() + "---");
    }

    public MultipartRequestBody(String string) {
        super(new MediaType("multipart/form-data; boundary=" + string));
        this.internalField0248 = string;
    }

    public MultipartRequestBody internalMethod05633(String string, RequestBody typedValue036) {
        if (typedValue036.internalMethod02203() < 0) {
            this.internalField0277 = true;
        }
        return this.internalMethod07249(new InternalType0461(string, typedValue036));
    }

    public MultipartRequestBody internalMethod01804(String string, RequestBody typedValue036, @Nullable String string2) {
        if (typedValue036.internalMethod02203() < 0) {
            this.internalField0277 = true;
        }
        return this.internalMethod07249(new InternalType0461(string, typedValue036, string2));
    }

    public MultipartRequestBody internalMethod07249(InternalType0461 nestedValue2056) {
        if (nestedValue2056.internalMethod05771().internalMethod02203() < 0) {
            this.internalField0277 = true;
        }
        this.internalField0416.add(nestedValue2056);
        return this;
    }

    @Override
    public boolean internalMethod02204() {
        return false;
    }

    @Override
    public int internalMethod02203() {
        if (this.internalField0277) {
            return -1;
        }
        int n = ("--" + this.internalField0248 + "\r\n").getBytes().length;
        int[] nArray = new int[]{0};
        for (InternalType0461 nestedValue2056 : this.internalField0416) {
            nArray[0] = nArray[0] + n;
            nestedValue2056.internalMethod06248(string -> {
                nArray[0] = nArray[0] + string.getBytes().length;
            });
            nArray[0] = nArray[0] + 2;
            nArray[0] = nArray[0] + nestedValue2056.internalMethod05771().internalMethod02203();
            nArray[0] = nArray[0] + 2;
        }
        return nArray[0] + n;
    }

    @Override
    @Nonnull
    protected InputStream internalMethod00691() throws IOException {
        ArrayDeque<InputStream> arrayDeque = new ArrayDeque<InputStream>(this.internalField0416.size() * 4 + 1);
        byte[] byArray = "\r\n".getBytes();
        byte[] byArray2 = ("--" + this.internalField0248 + "\r\n").getBytes();
        byte[] byArray3 = ("--" + this.internalField0248 + "--").getBytes();
        for (InternalType0461 nestedValue2056 : this.internalField0416) {
            arrayDeque.add(new ByteArrayInputStream(byArray2));
            nestedValue2056.internalMethod06248(string -> arrayDeque.add(new ByteArrayInputStream(string.getBytes())));
            arrayDeque.add(new ByteArrayInputStream(byArray));
            arrayDeque.add(nestedValue2056.internalMethod05771().internalMethod02044());
            arrayDeque.add(new ByteArrayInputStream(byArray));
        }
        arrayDeque.add(new ByteArrayInputStream(byArray3));
        return new QueuedInputStream(arrayDeque);
    }

    public static class InternalType0461
    extends FluentObject<InternalType0461> {
        private final RequestBody internalField0061;

        public InternalType0461(String string, RequestBody typedValue036) {
            this(string, typedValue036, null);
        }

        public InternalType0461(String string, RequestBody typedValue036, @Nullable String string2) {
            this.internalField0061 = typedValue036;
            this.internalMethod01193("Content-Disposition", "form-data; name=\"" + string + "\"" + (string2 == null ? "" : "; filename=\"" + string2 + "\""));
            this.internalMethod01193("Content-Type", typedValue036.internalMethod05122().toString());
        }

        public RequestBody internalMethod05771() {
            return this.internalField0061;
        }

        private void internalMethod06248(Consumer<String> consumer) {
            this.internalMethod01848((string, string2) -> consumer.accept(string + ": " + string2 + "\r\n"));
        }
    }
}

