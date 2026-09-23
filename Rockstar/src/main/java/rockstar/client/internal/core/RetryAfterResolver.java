package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import javax.annotation.Nullable;
import rockstar.client.internal.core.RetryDelay;
import rockstar.client.internal.core.HttpRetryHandler;
import rockstar.client.network.RockstarHttpResponse;

public class RetryAfterResolver
implements HttpRetryHandler {
    @Override
    public RetryDelay internalMethod07102(RockstarHttpResponse typedValue035) {
        Long l;
        Optional<String> optional = typedValue035.internalMethod04855("Retry-After");
        if (optional.isPresent() && (l = this.internalMethod04177(optional.get())) != null && l > 0L) {
            return RetryDelay.internalMethod05625(l);
        }
        return RetryDelay.internalField0873;
    }

    @Nullable
    private Long internalMethod04177(String string) {
        try {
            Instant instant = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(string));
            return instant.toEpochMilli() - Instant.now().toEpochMilli();
        }
        catch (DateTimeParseException dateTimeParseException) {
            try {
                int n = Integer.parseInt(string);
                return (long)n * 1000L;
            }
            catch (NumberFormatException numberFormatException) {
                return null;
            }
        }
    }
}

