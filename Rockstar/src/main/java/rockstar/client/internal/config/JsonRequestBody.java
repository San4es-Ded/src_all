package rockstar.client.internal.config;




import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.MediaTypes;
import rockstar.client.network.StringRequestBody;

public class JsonRequestBody
extends StringRequestBody {
    public JsonRequestBody(JsonObjectNode typedValue030) {
        this(typedValue030.internalMethod02940());
    }

    public JsonRequestBody(JsonObject jsonObject) {
        super(MediaTypes.internalField1095, jsonObject.toString());
    }
}

