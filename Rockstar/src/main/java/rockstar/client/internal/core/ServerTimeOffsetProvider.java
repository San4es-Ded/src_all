package rockstar.client.internal.core;




import rockstar.client.network.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import rockstar.client.network.HttpGetRequest;
import rockstar.client.internal.auth.MinecraftAuthHttpClientFactory;
import rockstar.client.internal.auth.MsaEnvironment;
import rockstar.client.network.RockstarHttpClient;
import rockstar.client.network.RockstarHttpResponse;

public class ServerTimeOffsetProvider {
    private static Duration internalField0590 = null;

    public static synchronized Duration internalMethod04170() {
        if (internalField0590 == null) {
            RockstarHttpClient typedValue034 = MinecraftAuthHttpClientFactory.internalMethod01056();
            typedValue034.internalMethod03231().internalMethod07052(3);
            try {
                RockstarHttpResponse typedValue035 = typedValue034.internalMethod03397(new HttpGetRequest(MsaEnvironment.internalField0726.internalMethod08402()));
                Instant instant = Instant.now();
                Instant instant2 = typedValue035.internalMethod04855("Date").map(string -> DateTimeFormatter.RFC_1123_DATE_TIME.parse((CharSequence)string, Instant::from)).get();
                internalField0590 = Duration.between(instant, instant2);
            }
            catch (Throwable throwable) {
                new RuntimeException("Failed to get client time offset. This may cause issues with authentication if the local clock is wrong", throwable).printStackTrace();
                internalField0590 = Duration.ZERO;
            }
        }
        return internalField0590;
    }
}

