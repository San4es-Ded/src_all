package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.RegistryListSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyBlockSetting {
    private final RegistryListSetting setting;

    public PyBlockSetting(PyModule pyModule, String string) {
        this.setting = new RegistryListSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyBlockSetting(PyHudElement pyHudElement, String string) {
        this.setting = new RegistryListSetting(pyHudElement, string);
    }

    public PyBlockSetting(PyEspElement pyEspElement, String string) {
        this.setting = new RegistryListSetting(pyEspElement.getElement(), string);
    }

    public PyBlockSetting(RegistryListSetting typedValue163) {
        this.setting = typedValue163;
    }

    public PyBlockSetting select(String string) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod02288(PyBlockSetting.parse(string));
        return this;
    }

    public PyBlockSetting toggle(String string) {
        Block block = PyBlockSetting.block(string);
        if (block != null) {
            PythonScript.internalMethod06468(this.setting);
            this.setting.internalMethod05585(block);
        }
        return this;
    }

    public boolean isSelected(String string) {
        return this.setting.internalMethod05933(PyBlockSetting.parse(string));
    }

    public List<String> getSelected() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Identifier identifier : this.setting.internalMethod00086()) {
            arrayList.add(identifier.toString());
        }
        return arrayList;
    }

    public int count() {
        return this.setting.internalMethod02610();
    }

    public PyBlockSetting allow(String string) {
        Block block = PyBlockSetting.block(string);
        if (block != null) {
            this.setting.internalMethod02278(block);
        }
        return this;
    }

    public PyBlockSetting allowAll() {
        this.setting.internalMethod01531(new Block[0]);
        return this;
    }

    public PyBlockSetting allowItem(String string) {
        Item item;
        Identifier identifier = PyBlockSetting.parse(string);
        Item item2 = item = identifier == null ? null : (Item)Registries.ITEM.get(identifier);
        if (item != null) {
            this.setting.internalMethod02504(item);
        }
        return this;
    }

    public PyBlockSetting allowAllItems() {
        this.setting.internalMethod06411();
        return this;
    }

    private static Identifier parse(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = string.trim();
        return Identifier.tryParse((String)(string2.indexOf(58) < 0 ? "minecraft:" + string2 : string2));
    }

    private static Block block(String string) {
        Identifier identifier = PyBlockSetting.parse(string);
        return identifier == null ? null : (Block)Registries.BLOCK.get(identifier);
    }

    @Generated
    public RegistryListSetting getSetting() {
        return this.setting;
    }
}

