package rockstar.client.data;


import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import rockstar.client.data.JsonNode;

public class JsonPrimitiveNode
extends JsonNode {
    private final JsonPrimitive internalField0224;

    public JsonPrimitiveNode(String string) {
        this(new JsonPrimitive(string));
    }

    public JsonPrimitiveNode(boolean bl) {
        this(new JsonPrimitive(Boolean.valueOf(bl)));
    }

    public JsonPrimitiveNode(Number number) {
        this(new JsonPrimitive(number));
    }

    public JsonPrimitiveNode(JsonPrimitive jsonPrimitive) {
        super((JsonElement)jsonPrimitive);
        this.internalField0224 = jsonPrimitive;
    }

    public JsonPrimitive internalMethod07445() {
        return this.internalField0224;
    }

    public boolean internalMethod04175() {
        return this.internalField0224.isBoolean();
    }

    public boolean internalMethod09317() {
        return this.internalField0224.isNumber();
    }

    public boolean internalMethod09318() {
        return this.internalField0224.isString();
    }
}

