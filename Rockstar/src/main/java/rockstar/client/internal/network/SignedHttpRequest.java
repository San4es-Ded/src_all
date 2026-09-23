package rockstar.client.internal.network;





import rockstar.client.network.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.script.CryptoKeyUtils;
import rockstar.client.network.RequestBody;
import rockstar.client.internal.core.ServerTimeOffsetProvider;
import rockstar.client.network.CustomHttpRequest;

public abstract class SignedHttpRequest
extends HttpPostRequest {
    public SignedHttpRequest(String string) throws MalformedURLException {
        super(string);
    }

    public SignedHttpRequest(URL uRL) {
        super(uRL);
    }

    public void internalMethod05279(ECPrivateKey eCPrivateKey) {
        long l = (Instant.now().plus(ServerTimeOffsetProvider.internalMethod04170()).getEpochSecond() + 11644473600L) * 10000000L;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            RequestBody typedValue036;
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream2);
            dataOutputStream.writeInt(1);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeLong(l);
            dataOutputStream.writeByte(0);
            dataOutputStream.write(this.internalMethod06034().getBytes(StandardCharsets.UTF_8));
            dataOutputStream.writeByte(0);
            dataOutputStream.write((this.internalMethod03635().getPath() + (this.internalMethod03635().getQuery() != null ? this.internalMethod03635().getQuery() : "")).getBytes(StandardCharsets.UTF_8));
            dataOutputStream.writeByte(0);
            Optional<String> optional = this.internalMethod04855("Authorization");
            if (optional.isPresent()) {
                dataOutputStream.write(optional.get().getBytes(StandardCharsets.UTF_8));
            }
            dataOutputStream.writeByte(0);
            if (this instanceof CustomHttpRequest && (typedValue036 = this.internalMethod05540()) != null) {
                dataOutputStream.write(typedValue036.internalMethod03451());
            }
            dataOutputStream.writeByte(0);
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(1);
            dataOutputStream.writeLong(l);
            dataOutputStream.write(CryptoKeyUtils.internalMethod04027(eCPrivateKey, byteArrayOutputStream2.toByteArray()));
        }
        catch (Throwable throwable) {
            throw new RuntimeException("Failed to sign request", throwable);
        }
        this.internalMethod05702("Signature", Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
    }

    public JsonObject internalMethod05780(ECPublicKey eCPublicKey) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("kty", "EC");
        jsonObject.addProperty("alg", "ES256");
        jsonObject.addProperty("crv", "P-256");
        jsonObject.addProperty("use", "sig");
        jsonObject.addProperty("x", this.internalMethod04927(eCPublicKey.getParams().getCurve().getField().getFieldSize(), eCPublicKey.getW().getAffineX()));
        jsonObject.addProperty("y", this.internalMethod04927(eCPublicKey.getParams().getCurve().getField().getFieldSize(), eCPublicKey.getW().getAffineY()));
        return jsonObject;
    }

    private String internalMethod04927(int n, BigInteger bigInteger) {
        int n2;
        byte[] byArray = this.internalMethod05648(bigInteger);
        if (byArray.length >= (n2 = (n + 7) / 8)) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(byArray);
        }
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, n2 - byArray.length, byArray.length);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(byArray2);
    }

    private byte[] internalMethod05648(BigInteger bigInteger) {
        int n = bigInteger.bitLength();
        n = n + 7 >> 3 << 3;
        byte[] byArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && bigInteger.bitLength() / 8 + 1 == n / 8) {
            return byArray;
        }
        int n2 = 0;
        int n3 = byArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            n2 = 1;
            --n3;
        }
        int n4 = n / 8 - n3;
        byte[] byArray2 = new byte[n / 8];
        System.arraycopy(byArray, n2, byArray2, n4, n3);
        return byArray2;
    }
}

