package pyrock.classes;


import rockstar.client.internal.core.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import rockstar.client.internal.core.MenuLayoutProvider;

public class PyMenu {
    public boolean opened() {
        return MenuLayoutProvider.internalMethod05990() != null;
    }

    public String type() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 == null ? null : typedValue208.internalMethod03999();
    }

    public float progress() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 == null ? 0.0f : typedValue208.internalMethod08297();
    }

    public float open() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 == null ? 0.0f : typedValue208.internalMethod04389();
    }

    public float close() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 == null ? 0.0f : typedValue208.internalMethod04391();
    }

    public boolean closing() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 != null && typedValue208.internalMethod04390();
    }

    public float alpha() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 == null ? 0.0f : typedValue208.internalMethod08283();
    }

    public float scale() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        return typedValue208 == null ? 0.0f : typedValue208.internalMethod08285();
    }

    public List<Map<String, Object>> panels() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        if (typedValue208 == null) {
            return List.of();
        }
        List<MenuLayoutProvider.InternalType0257> list = typedValue208.internalMethod05973();
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(list.size());
        for (MenuLayoutProvider.InternalType0257 nestedValue0100 : list) {
            arrayList.add(PyMenu.panel(nestedValue0100));
        }
        return arrayList;
    }

    public Map<String, Object> panel(String string) {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        if (typedValue208 == null || string == null) {
            return null;
        }
        for (MenuLayoutProvider.InternalType0257 nestedValue0100 : typedValue208.internalMethod05973()) {
            if (!nestedValue0100.internalMethod00905().equalsIgnoreCase(string.trim())) continue;
            return PyMenu.panel(nestedValue0100);
        }
        return null;
    }

    public float x() {
        return PyMenu.bounds()[0];
    }

    public float y() {
        return PyMenu.bounds()[1];
    }

    public float width() {
        return PyMenu.bounds()[2];
    }

    public float height() {
        return PyMenu.bounds()[3];
    }

    private static Map<String, Object> panel(MenuLayoutProvider.InternalType0257 nestedValue0100) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        linkedHashMap.put("name", nestedValue0100.internalMethod00905());
        linkedHashMap.put("x", Float.valueOf(nestedValue0100.internalMethod01894()));
        linkedHashMap.put("y", Float.valueOf(nestedValue0100.internalMethod01897()));
        linkedHashMap.put("width", Float.valueOf(nestedValue0100.internalMethod08998()));
        linkedHashMap.put("height", Float.valueOf(nestedValue0100.internalMethod08999()));
        linkedHashMap.put("right", Float.valueOf(nestedValue0100.internalMethod01894() + nestedValue0100.internalMethod08998()));
        linkedHashMap.put("bottom", Float.valueOf(nestedValue0100.internalMethod01897() + nestedValue0100.internalMethod08999()));
        linkedHashMap.put("center_x", Float.valueOf(nestedValue0100.internalMethod01894() + nestedValue0100.internalMethod08998() / 2.0f));
        linkedHashMap.put("center_y", Float.valueOf(nestedValue0100.internalMethod01897() + nestedValue0100.internalMethod08999() / 2.0f));
        return linkedHashMap;
    }

    private static float[] bounds() {
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        if (typedValue208 == null) {
            return new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        }
        float f = Float.MAX_VALUE;
        float f2 = Float.MAX_VALUE;
        float f3 = -3.4028235E38f;
        float f4 = -3.4028235E38f;
        for (MenuLayoutProvider.InternalType0257 nestedValue0100 : typedValue208.internalMethod05973()) {
            f = Math.min(f, nestedValue0100.internalMethod01894());
            f2 = Math.min(f2, nestedValue0100.internalMethod01897());
            f3 = Math.max(f3, nestedValue0100.internalMethod01894() + nestedValue0100.internalMethod08998());
            f4 = Math.max(f4, nestedValue0100.internalMethod01897() + nestedValue0100.internalMethod08999());
        }
        if (f > f3) {
            return new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        }
        return new float[]{f, f2, f3 - f, f4 - f2};
    }

    public Map<String, Object> all() {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        MenuLayoutProvider typedValue208 = MenuLayoutProvider.internalMethod05990();
        linkedHashMap.put("opened", typedValue208 != null);
        linkedHashMap.put("type", typedValue208 == null ? null : typedValue208.internalMethod03999());
        linkedHashMap.put("progress", Float.valueOf(this.progress()));
        linkedHashMap.put("open", Float.valueOf(this.open()));
        linkedHashMap.put("close", Float.valueOf(this.close()));
        linkedHashMap.put("closing", this.closing());
        linkedHashMap.put("alpha", Float.valueOf(this.alpha()));
        linkedHashMap.put("scale", Float.valueOf(this.scale()));
        float[] fArray = PyMenu.bounds();
        linkedHashMap.put("x", Float.valueOf(fArray[0]));
        linkedHashMap.put("y", Float.valueOf(fArray[1]));
        linkedHashMap.put("width", Float.valueOf(fArray[2]));
        linkedHashMap.put("height", Float.valueOf(fArray[3]));
        linkedHashMap.put("panels", this.panels());
        return linkedHashMap;
    }
}

