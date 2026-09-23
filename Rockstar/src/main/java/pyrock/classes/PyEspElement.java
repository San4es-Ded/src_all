package pyrock.classes;



import rockstar.client.esp.*;
import rockstar.client.internal.script.*;
import java.util.ArrayList;
import java.util.Locale;
import jep.python.PyCallable;
import lombok.Generated;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.RockstarClient;
import rockstar.client.esp.PlayerTargetType;
import rockstar.client.esp.EntityTargetType;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.ScriptEspFeature;
import rockstar.client.MinecraftClientAccess;

public class PyEspElement
implements MinecraftClientAccess {
    private final ScriptEspFeature element;
    private final PythonScript creator = PythonScript.internalMethod00581();

    public PyEspElement(String string, String string2) {
        this.element = new ScriptEspFeature(this.owner(), string, this.types(string2));
        EspManager.internalMethod06726().internalMethod01153(this.element);
    }

    public PyEspElement onRender(PyCallable pyCallable) {
        this.element.internalMethod02083(pyCallable == null ? null : (entity, render3DEvent) -> this.call(pyCallable, entity, render3DEvent));
        return this;
    }

    public PyEspElement onRenderAll(PyCallable pyCallable) {
        this.element.internalMethod02080(pyCallable == null ? null : (list, render3DEvent) -> this.call(pyCallable, list, render3DEvent));
        return this;
    }

    public PyEspElement onFilter(PyCallable pyCallable) {
        this.element.internalMethod05137(pyCallable == null ? null : entity -> {
            Boolean bl;
            Object object = this.call(pyCallable, entity, null);
            return !(object instanceof Boolean) || (bl = (Boolean)object) != false;
        });
        return this;
    }

    public PyEspElement enableFor(String string) {
        EntityTargetType typedValue093 = PyEspElement.type(string);
        if (typedValue093 != null) {
            this.element.internalMethod07221(typedValue093);
            return this;
        }
        PlayerTargetType typedValue092 = PyEspElement.subType(string);
        if (typedValue092 != null) {
            this.element.internalMethod07634(typedValue092);
            return this;
        }
        throw new IllegalArgumentException("\u043d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u0430\u044f \u0446\u0435\u043b\u044c: " + string);
    }

    public boolean isEnabled() {
        return EspManager.internalMethod03145() && this.element.internalMethod06968();
    }

    public String getName() {
        return this.element.internalMethod01940();
    }

    public void remove() {
        EspManager.internalMethod06726().internalMethod01836(this.element);
    }

    private Object call(PyCallable pyCallable, Object object, Object object2) {
        if (!EmbeddedPythonRuntime.internalMethod08724()) {
            return null;
        }
        PythonScript owner = this.creator != null ? this.creator : PythonScript.internalMethod00581();
        if (owner != null && !owner.internalMethod08681()) {
            return null;
        }
        try (AutoCloseable ignored = PythonScript.internalMethod02561(owner)) {
            return object2 == null ? pyCallable.call(new Object[]{object}) : pyCallable.call(new Object[]{object, object2});
        } catch (Exception exception) {
            this.element.internalMethod02083(null);
            this.element.internalMethod02080(null);
            this.element.internalMethod05137(null);
            RockstarClient.internalField0572.error("Python error in esp element '" + this.element.internalMethod01940() + "':", exception);
            return null;
        }
    }

    private EntityTargetType[] types(String string) {
        ArrayList<EntityTargetType> arrayList = new ArrayList<EntityTargetType>();
        if (string == null || string.isBlank()) {
            return new EntityTargetType[0];
        }
        for (String string2 : string.split(",")) {
            if (string2.isBlank()) continue;
            EntityTargetType typedValue093 = PyEspElement.type(string2);
            if (typedValue093 == null) {
                throw new IllegalArgumentException("\u043d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u0430\u044f \u0446\u0435\u043b\u044c: " + string2.trim());
            }
            arrayList.add(typedValue093);
        }
        return arrayList.toArray(new EntityTargetType[0]);
    }

    private static EntityTargetType type(String string) {
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        for (EntityTargetType typedValue093 : EntityTargetType.values()) {
            if (!typedValue093.internalMethod00613().equals(string2)) continue;
            return typedValue093;
        }
        return null;
    }

    private static PlayerTargetType subType(String string) {
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        for (PlayerTargetType typedValue092 : PlayerTargetType.values()) {
            if (!typedValue092.internalMethod03561().equals(string2)) continue;
            return typedValue092;
        }
        return null;
    }

    private Object owner() {
        PythonScript typedValue155 = PythonScript.internalMethod00581();
        return typedValue155 != null ? typedValue155 : this;
    }

    @Generated
    public ScriptEspFeature getElement() {
        return this.element;
    }

    @Generated
    public PythonScript getCreator() {
        return this.creator;
    }
}
