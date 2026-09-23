package rockstar.client.internal.config;



import rockstar.client.data.*;
import rockstar.client.*;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import lombok.Generated;
import rockstar.client.data.JsonNode;

public final class JsonParserUtils {
    public static JsonNode internalMethod05878(String string) {
        return JsonNode.internalMethod07323(JsonParser.parseString((String)string));
    }

    public static JsonNode internalMethod07106(Reader reader) {
        return JsonNode.internalMethod07323(JsonParser.parseReader((Reader)reader));
    }

    public static JsonNode internalMethod00526(JsonReader jsonReader) {
        return JsonNode.internalMethod07323(JsonParser.parseReader((JsonReader)jsonReader));
    }

    @Generated
    private JsonParserUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

