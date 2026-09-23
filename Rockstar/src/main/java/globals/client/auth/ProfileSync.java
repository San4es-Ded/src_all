package globals.client.auth;


import rockstar.client.internal.game.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import moscow.rockstar.mixin.accessors.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rockstar.client.internal.game.DiscordRichPresenceService;
import rockstar.client.RockstarClient;
import rockstar.profile.Profile;
import rockstar.profile.Role;

public final class ProfileSync {
    private static final OkHttpClient HTTP = new OkHttpClient();
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ROOT);
    private static volatile long lastFetch;
    private static volatile long subscriptionEnd;

    private ProfileSync() {
    }

    public static void refresh(String string, String string2) {
        if (string == null || string2 == null) {
            return;
        }
        long l = System.currentTimeMillis();
        if (l - lastFetch < 300000L) {
            return;
        }
        lastFetch = l;
        Request request = new Request.Builder().url(string + "/auth/game/me").addHeader("Authorization", "Bearer " + string2).get().build();
        try (Response response = HTTP.newCall(request).execute();){
            if (!response.isSuccessful() || response.body() == null) {
                return;
            }
            ProfileSync.apply(JsonParser.parseString((String)response.body().string()).getAsJsonObject());
        }
        catch (Exception exception) {
            lastFetch = 0L;
        }
    }

    public static void invalidate() {
        lastFetch = 0L;
    }

    private static void apply(JsonObject jsonObject) {
        String string = ProfileSync.string(jsonObject, "username");
        if (string != null) {
            Profile.username = string;
        }
        if (jsonObject.has("uid") && !jsonObject.get("uid").isJsonNull()) {
            Profile.uid = jsonObject.get("uid").getAsInt();
        }
        Profile.role = ProfileSync.role(ProfileSync.string(jsonObject, "role"), ProfileSync.string(jsonObject, "subscription"));
        String string2 = ProfileSync.string(jsonObject, "expiresAt");
        subscriptionEnd = ProfileSync.millis(string2);
        Profile.subscriptionEndDate = ProfileSync.date(string2);
        ProfileSync.republish();
    }

    private static void republish() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient != null) {
            minecraftClient.execute(() -> {
                try {
                    ((MinecraftClientAccessor)(Object)minecraftClient).invokeUpdateWindowTitle();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            });
        }
        try {
            DiscordRichPresenceService typedValue133 = RockstarClient.getInstance().internalMethod03315();
            if (typedValue133 != null) {
                typedValue133.internalMethod00955();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Role role(String string, String string2) {
        if (string != null) {
            switch (string) {
                case "ADMIN": {
                    return Role.ADMIN;
                }
                case "MODERATOR": {
                    return Role.MODERATOR;
                }
                case "MEDIA": {
                    return Role.MEDIA;
                }
            }
        }
        if (string2 == null) {
            return Role.DEFAULT;
        }
        return switch (string2) {
            case "alpha" -> Role.ALPHA;
            case "beta" -> Role.BETA;
            default -> Role.USER;
        };
    }

    public static long subscriptionEnd() {
        return subscriptionEnd;
    }

    private static long millis(String string) {
        if (string == null) {
            return 0L;
        }
        try {
            return Instant.parse(string).toEpochMilli();
        }
        catch (Exception exception) {
            return 0L;
        }
    }

    private static String date(String string) {
        if (string == null) {
            return null;
        }
        try {
            Instant instant = Instant.parse(string);
            if (instant.isAfter(Instant.now().plus(1095L, ChronoUnit.DAYS))) {
                return "\u041d\u0430\u0432\u0441\u0435\u0433\u0434\u0430";
            }
            return DATE.format(instant.atZone(ZoneId.systemDefault()));
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String string(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && !jsonObject.get(string).isJsonNull() ? jsonObject.get(string).getAsString() : null;
    }
}
