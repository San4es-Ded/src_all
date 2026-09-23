package rockstar.client.esp;



import rockstar.client.event.*;
import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.modules.visual.EspModule;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspFeature;
import rockstar.client.esp.ArrowEspFeature;
import rockstar.client.esp.BoxEspFeature;
import rockstar.client.esp.FillEspFeature;
import rockstar.client.esp.FlameEspFeature;
import rockstar.client.esp.FriendMarkerEspFeature;
import rockstar.client.esp.GlowEspFeature;
import rockstar.client.esp.JumpCircleEspFeature;
import rockstar.client.esp.NametagEspFeature;
import rockstar.client.esp.ScriptEspFeature;
import rockstar.client.esp.TaksaEspFeature;

public class EspManager {
    private static final EspManager internalField0029 = new EspManager();
    private final List<EspFeature> internalField0416 = new ArrayList<EspFeature>();
    private final Map<String, JsonObject> internalField0543 = new ConcurrentHashMap<String, JsonObject>();
    private boolean internalField0277;
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        if (keyPressEvent.getAction() == 1) {
            this.internalMethod07139(keyPressEvent.getKey());
        }
    };
    private final EventListener<MouseEvent> internalField0158 = mouseEvent -> {
        if (mouseEvent.getAction() == 1) {
            this.internalMethod07139(mouseEvent.getButton());
        }
    };

    private EspManager() {
    }

    public static boolean internalMethod03145() {
        EspModule typedValue320 = RockstarClient.getInstance().getModuleManager().getModule(EspModule.class);
        return typedValue320 != null && typedValue320.isEnabled();
    }

    public void internalMethod03144() {
        if (!this.internalField0277) {
            RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
            this.internalField0277 = true;
        }
        this.internalMethod08098(new GlowEspFeature());
        this.internalMethod08098(new FlameEspFeature());
        this.internalMethod08098(new FillEspFeature());
        this.internalMethod08098(new NametagEspFeature());
        this.internalMethod08098(new ArrowEspFeature());
        this.internalMethod08098(new FriendMarkerEspFeature());
        this.internalMethod08098(new TaksaEspFeature());
        this.internalMethod08098(new BoxEspFeature());
        this.internalMethod08098(new JumpCircleEspFeature());
    }

    private void internalMethod07139(int n) {
        if (n == -1) {
            return;
        }
        if (MinecraftClient.getInstance().currentScreen != null) {
            return;
        }
        if (!EspManager.internalMethod03145()) {
            return;
        }
        for (EspFeature typedValue091 : this.internalField0416) {
            typedValue091.internalMethod02761(n);
        }
    }

    private void internalMethod08098(EspFeature typedValue091) {
        this.internalField0416.add(typedValue091);
        typedValue091.internalMethod06967();
        this.internalMethod08236(typedValue091);
    }

    public void internalMethod01153(EspFeature typedValue091) {
        if (typedValue091 == null || this.internalField0416.contains(typedValue091)) {
            return;
        }
        this.internalMethod08098(typedValue091);
    }

    public void internalMethod01836(EspFeature typedValue091) {
        if (typedValue091 == null || !this.internalField0416.remove(typedValue091)) {
            return;
        }
        try {
            this.internalField0543.put(typedValue091.internalMethod01940(), typedValue091.internalMethod06190());
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.warn("Config: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u044d\u043b\u0435\u043c\u0435\u043d\u0442 ESP {}", (Object)typedValue091.internalMethod01940(), (Object)exception);
        }
        RockstarClient.getInstance().internalMethod03317().internalMethod07237(typedValue091);
    }

    public void internalMethod04326(Object object) {
        for (EspFeature typedValue091 : new ArrayList<EspFeature>(this.internalField0416)) {
            ScriptEspFeature typedValue096;
            if (!(typedValue091 instanceof ScriptEspFeature) || (typedValue096 = (ScriptEspFeature)typedValue091).internalMethod05817() != object) continue;
            this.internalMethod01836(typedValue091);
        }
    }

    private void internalMethod08236(EspFeature typedValue091) {
        JsonObject jsonObject = this.internalField0543.remove(typedValue091.internalMethod01940());
        if (jsonObject != null) {
            typedValue091.internalMethod03652(jsonObject);
        }
    }

    public void internalMethod02772(Object object) {
        for (EspFeature typedValue091 : new ArrayList<EspFeature>(this.internalField0416)) {
            ScriptEspFeature typedValue096;
            if (!(typedValue091 instanceof ScriptEspFeature) || (typedValue096 = (ScriptEspFeature)typedValue091).internalMethod05817() != object) continue;
            this.internalMethod08236(typedValue091);
        }
    }

    public <T extends EspFeature> T internalMethod05464(Class<T> clazz) {
        for (EspFeature typedValue091 : this.internalField0416) {
            if (!clazz.isInstance(typedValue091)) continue;
            return (T)typedValue091;
        }
        return null;
    }

    public EspFeature internalMethod04353(String string) {
        for (EspFeature typedValue091 : this.internalField0416) {
            if (!typedValue091.internalMethod01940().equals(string)) continue;
            return typedValue091;
        }
        return null;
    }

    public JsonArray internalMethod00487() {
        JsonArray jsonArray = new JsonArray();
        HashSet<String> hashSet = new HashSet<String>();
        for (EspFeature object : this.internalField0416) {
            hashSet.add(object.internalMethod01940());
            jsonArray.add((JsonElement)object.internalMethod06190());
        }
        for (Map.Entry entry : this.internalField0543.entrySet()) {
            if (hashSet.contains(entry.getKey())) continue;
            jsonArray.add((JsonElement)((JsonObject)entry.getValue()).deepCopy());
        }
        return jsonArray;
    }

    public void internalMethod02428(JsonArray jsonArray) {
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (!jsonObject.has("name")) continue;
            String string = jsonObject.get("name").getAsString();
            EspFeature typedValue091 = this.internalMethod04353(string);
            if (typedValue091 != null) {
                typedValue091.internalMethod03652(jsonObject);
                continue;
            }
            this.internalField0543.put(string, jsonObject.deepCopy());
        }
    }

    @Generated
    public static EspManager internalMethod06726() {
        return internalField0029;
    }

    @Generated
    public List<EspFeature> internalMethod02968() {
        return this.internalField0416;
    }
}
