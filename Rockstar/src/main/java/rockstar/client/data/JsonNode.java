package rockstar.client.data;


import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import rockstar.client.data.JsonArrayNode;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.data.JsonPrimitiveNode;

public class JsonNode {
    private final JsonElement internalField0469;

    public static JsonNode internalMethod07323(JsonElement jsonElement) {
        if (jsonElement instanceof JsonObject) {
            return new JsonObjectNode(jsonElement.getAsJsonObject());
        }
        if (jsonElement instanceof JsonArray) {
            return new JsonArrayNode(jsonElement.getAsJsonArray());
        }
        if (jsonElement instanceof JsonPrimitive) {
            return new JsonPrimitiveNode(jsonElement.getAsJsonPrimitive());
        }
        return new JsonNode(jsonElement);
    }

    protected JsonNode(@Nonnull JsonElement jsonElement) {
        this.internalField0469 = jsonElement;
    }

    @Nonnull
    public JsonElement internalMethod03947() {
        return this.internalField0469;
    }

    public JsonNode internalMethod04511() {
        return JsonNode.internalMethod07323(this.internalField0469.deepCopy());
    }

    public boolean internalMethod07307() {
        return this.internalField0469.isJsonObject();
    }

    public boolean internalMethod08644() {
        return this.internalField0469.isJsonArray();
    }

    public boolean internalMethod08645() {
        return this.internalField0469.isJsonPrimitive();
    }

    public boolean internalMethod08654() {
        return this.internalField0469.isJsonNull();
    }

    public JsonObjectNode internalMethod04512() {
        if (this instanceof JsonObjectNode) {
            return (JsonObjectNode)this;
        }
        return new JsonObjectNode(this.internalField0469.getAsJsonObject());
    }

    public JsonArrayNode internalMethod05819() {
        if (this instanceof JsonArrayNode) {
            return (JsonArrayNode)this;
        }
        return new JsonArrayNode(this.internalField0469.getAsJsonArray());
    }

    public JsonPrimitiveNode internalMethod04513() {
        if (this instanceof JsonPrimitiveNode) {
            return (JsonPrimitiveNode)this;
        }
        return new JsonPrimitiveNode(this.internalField0469.getAsJsonPrimitive());
    }

    public boolean internalMethod08655() {
        return this.internalField0469.getAsBoolean();
    }

    public byte internalMethod07300() {
        return this.internalField0469.getAsByte();
    }

    public short internalMethod07304() {
        return this.internalField0469.getAsShort();
    }

    public int internalMethod07306() {
        return this.internalField0469.getAsInt();
    }

    public long internalMethod07303() {
        return this.internalField0469.getAsLong();
    }

    public float internalMethod07302() {
        return this.internalField0469.getAsFloat();
    }

    public double internalMethod07301() {
        return this.internalField0469.getAsDouble();
    }

    public BigInteger internalMethod01026() {
        return this.internalField0469.getAsBigInteger();
    }

    public BigDecimal internalMethod01014() {
        return this.internalField0469.getAsBigDecimal();
    }

    public Number internalMethod00748() {
        return this.internalField0469.getAsNumber();
    }

    public String internalMethod00968() {
        return this.internalField0469.getAsString();
    }

    public String toString() {
        return this.internalField0469.toString();
    }

    public boolean equals(Object object) {
        if (object instanceof JsonNode) {
            return this.internalField0469.equals(((JsonNode)object).internalField0469);
        }
        if (object instanceof JsonElement) {
            return this.internalField0469.equals(object);
        }
        return false;
    }

    public int hashCode() {
        return this.internalField0469.hashCode();
    }
}

