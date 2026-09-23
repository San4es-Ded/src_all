package pyrock.classes;






import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.auth.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jep.python.PyCallable;
import pyrock.classes.PyHudElement;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.config.AbstractClientConfig;
import rockstar.client.internal.auth.ClientDataConfig;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.ui.AbstractHudWidget;
import rockstar.client.RockstarClient;

public class PyHud {
    public PyHudElement add(String string, String string2, double d, double d2, double d3, double d4, boolean bl, PyCallable pyCallable, PyCallable pyCallable2) {
        String string3 = PyHud.normalizeName(string);
        PythonScript typedValue155 = PythonScript.internalMethod00581();
        PyHud.removeOwnedDuplicate(typedValue155, string3);
        PyHudElement pyHudElement = new PyHudElement(typedValue155, string3, PyHud.normalizeIcon(string2), Math.max(1.0f, (float)d), Math.max(1.0f, (float)d2), (float)d3, (float)d4);
        pyHudElement.setShowing(bl);
        if (pyCallable != null) {
            pyHudElement.renderer(pyCallable);
        }
        if (pyCallable2 != null) {
            pyHudElement.visibleWhen(pyCallable2);
        }
        RockstarClient.getInstance().internalMethod01271().internalMethod09520().add(pyHudElement);
        PythonScript.internalMethod06107(pyHudElement);
        PyHud.applySavedState(pyHudElement);
        return pyHudElement;
    }

    public PyHudElement find(String string) {
        String string2 = PyHud.normalizeName(string);
        for (AbstractHudWidget typedValue197 : RockstarClient.getInstance().internalMethod01271().internalMethod09520()) {
            PyHudElement pyHudElement;
            if (!(typedValue197 instanceof PyHudElement) || !(pyHudElement = (PyHudElement)typedValue197).getName().equalsIgnoreCase(string2)) continue;
            return pyHudElement;
        }
        return null;
    }

    public List<PyHudElement> all() {
        ArrayList<PyHudElement> arrayList = new ArrayList<PyHudElement>();
        for (AbstractHudWidget typedValue197 : RockstarClient.getInstance().internalMethod01271().internalMethod09520()) {
            if (!(typedValue197 instanceof PyHudElement)) continue;
            PyHudElement pyHudElement = (PyHudElement)typedValue197;
            arrayList.add(pyHudElement);
        }
        return arrayList;
    }

    public List<PyHudElement> mine() {
        PythonScript typedValue155 = PythonScript.internalMethod00581();
        ArrayList<PyHudElement> arrayList = new ArrayList<PyHudElement>();
        for (PyHudElement pyHudElement : this.all()) {
            if (!pyHudElement.ownedBy(typedValue155)) continue;
            arrayList.add(pyHudElement);
        }
        return arrayList;
    }

    public List<Map<String, Object>> elements() {
        List<AbstractHudWidget> list = RockstarClient.getInstance().internalMethod01271().internalMethod09520();
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(list.size());
        for (AbstractHudWidget typedValue197 : list) {
            arrayList.add(PyHud.describe(typedValue197));
        }
        return arrayList;
    }

    public Map<String, Object> element(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        for (AbstractHudWidget typedValue197 : RockstarClient.getInstance().internalMethod01271().internalMethod09520()) {
            if (!PyHud.matches(typedValue197, string.trim())) continue;
            return PyHud.describe(typedValue197);
        }
        return null;
    }

    private static boolean matches(AbstractHudWidget typedValue197, String string) {
        String string2 = typedValue197.getName();
        if (string2 == null) {
            return false;
        }
        if (string2.equalsIgnoreCase(string) || LanguageManager.internalMethod07214(string2).equalsIgnoreCase(string)) {
            return true;
        }
        int n = string2.lastIndexOf(46);
        return n >= 0 && n < string2.length() - 1 && string2.substring(n + 1).equalsIgnoreCase(string);
    }

    private static Map<String, Object> describe(AbstractHudWidget typedValue197) {
        float f = typedValue197.getAnimation().internalMethod02881();
        float f2 = typedValue197.getVisible().internalMethod02881();
        float f3 = typedValue197.getSelecting().internalMethod02881();
        float f4 = f * f2;
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        linkedHashMap.put("name", typedValue197.getName());
        linkedHashMap.put("title", LanguageManager.internalMethod07214(typedValue197.getName()));
        linkedHashMap.put("icon", typedValue197.getIcon());
        linkedHashMap.put("x", Float.valueOf(typedValue197.getX()));
        linkedHashMap.put("y", Float.valueOf(typedValue197.getY()));
        linkedHashMap.put("width", Float.valueOf(typedValue197.getWidth()));
        linkedHashMap.put("height", Float.valueOf(typedValue197.getHeight()));
        linkedHashMap.put("right", Float.valueOf(typedValue197.getX() + typedValue197.getWidth()));
        linkedHashMap.put("bottom", Float.valueOf(typedValue197.getY() + typedValue197.getHeight()));
        linkedHashMap.put("center_x", Float.valueOf(typedValue197.getX() + typedValue197.getWidth() / 2.0f));
        linkedHashMap.put("center_y", Float.valueOf(typedValue197.getY() + typedValue197.getHeight() / 2.0f));
        linkedHashMap.put("alpha", Float.valueOf(f4));
        linkedHashMap.put("appear", Float.valueOf(f));
        linkedHashMap.put("visible", Float.valueOf(f2));
        linkedHashMap.put("select", Float.valueOf(f3));
        linkedHashMap.put("drag", Float.valueOf(typedValue197.getDragAnim().internalMethod02881()));
        linkedHashMap.put("scale", Float.valueOf(0.5f + f4 * 0.5f - 0.05f * f3));
        linkedHashMap.put("showing", typedValue197.isShowing());
        linkedHashMap.put("dragging", typedValue197.isDragging());
        linkedHashMap.put("script", typedValue197 instanceof PyHudElement);
        return linkedHashMap;
    }

    public boolean remove(PyHudElement pyHudElement) {
        return pyHudElement != null && pyHudElement.remove();
    }

    public boolean remove(String string) {
        PyHudElement pyHudElement = this.find(string);
        return pyHudElement != null && pyHudElement.remove();
    }

    private static void removeOwnedDuplicate(PythonScript typedValue155, String string) {
        if (typedValue155 == null) {
            return;
        }
        RockstarClient.getInstance().internalMethod01271().internalMethod09520().removeIf(typedValue197 -> {
            PyHudElement pyHudElement;
            if (typedValue197 instanceof PyHudElement && (pyHudElement = (PyHudElement)typedValue197).ownedBy(typedValue155) && pyHudElement.getName().equalsIgnoreCase(string)) {
                pyHudElement.dispose();
                return true;
            }
            return false;
        });
    }

    private static void applySavedState(PyHudElement pyHudElement) {
        AbstractClientConfig typedValue138 = RockstarClient.getInstance().internalMethod03371().internalMethod01175("client");
        if (typedValue138 instanceof ClientDataConfig) {
            ClientDataConfig typedValue139 = (ClientDataConfig)typedValue138;
            typedValue139.internalMethod02871(pyHudElement);
        }
    }

    private static String normalizeName(String string) {
        if (string == null || string.isBlank()) {
            return "Script HUD";
        }
        return string.trim();
    }

    private static String normalizeIcon(String string) {
        if (string == null || string.isBlank()) {
            return "hud/player";
        }
        return string.trim();
    }
}

