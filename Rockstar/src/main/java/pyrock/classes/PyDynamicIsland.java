package pyrock.classes;




import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.auth.*;
import java.util.ArrayList;
import java.util.List;
import jep.python.PyCallable;
import pyrock.classes.PyIslandStatus;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.config.AbstractClientConfig;
import rockstar.client.internal.auth.ClientDataConfig;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.DynamicIslandStatus;
import rockstar.client.RockstarClient;

public class PyDynamicIsland {
    public PyIslandStatus add(String string, double d, double d2, double d3, boolean bl, PyCallable pyCallable, PyCallable pyCallable2, PyCallable pyCallable3, PyCallable pyCallable4) {
        DynamicIslandHud typedValue201 = PyDynamicIsland.island();
        if (typedValue201 == null) {
            return null;
        }
        String string2 = PyDynamicIsland.normalizeName(string);
        PythonScript typedValue155 = PythonScript.internalMethod00581();
        PyDynamicIsland.removeOwnedDuplicate(typedValue155, string2);
        PyIslandStatus pyIslandStatus = new PyIslandStatus(typedValue155, typedValue201.internalMethod03091(), string2, Math.max(1.0f, (float)d), Math.max(1.0f, (float)d2), Math.max(0.0f, (float)d3)).expandable(bl);
        if (pyCallable != null) {
            pyIslandStatus.render(pyCallable);
        }
        if (pyCallable2 != null) {
            pyIslandStatus.visibleWhen(pyCallable2);
        }
        if (pyCallable3 != null) {
            pyIslandStatus.measure(pyCallable3);
        }
        if (pyCallable4 != null) {
            pyIslandStatus.onClick(pyCallable4);
        }
        PythonScript.internalMethod00383(typedValue201.internalMethod03091(), pyIslandStatus);
        PyDynamicIsland.applySavedState(typedValue201);
        return typedValue201.internalMethod03346(pyIslandStatus);
    }

    public PyIslandStatus add(String string, double d, double d2, double d3) {
        return this.add(string, d, d2, d3, false, null, null, null, null);
    }

    public PyIslandStatus add(String string, double d, double d2) {
        return this.add(string, d, d2, 7.0);
    }

    public PyIslandStatus add(String string) {
        return this.add(string, 48.0, 15.0, 7.0);
    }

    public PyIslandStatus find(String string) {
        DynamicIslandHud typedValue201 = PyDynamicIsland.island();
        if (typedValue201 == null) {
            return null;
        }
        String string2 = PyDynamicIsland.normalizeName(string);
        for (DynamicIslandStatus typedValue202 : typedValue201.internalMethod05407()) {
            PyIslandStatus pyIslandStatus;
            if (!(typedValue202 instanceof PyIslandStatus) || !(pyIslandStatus = (PyIslandStatus)typedValue202).getName().equalsIgnoreCase(string2)) continue;
            return pyIslandStatus;
        }
        return null;
    }

    public List<PyIslandStatus> all() {
        DynamicIslandHud typedValue201 = PyDynamicIsland.island();
        ArrayList<PyIslandStatus> arrayList = new ArrayList<PyIslandStatus>();
        if (typedValue201 == null) {
            return arrayList;
        }
        for (DynamicIslandStatus typedValue202 : typedValue201.internalMethod05407()) {
            if (!(typedValue202 instanceof PyIslandStatus)) continue;
            PyIslandStatus pyIslandStatus = (PyIslandStatus)typedValue202;
            arrayList.add(pyIslandStatus);
        }
        return arrayList;
    }

    public List<PyIslandStatus> mine() {
        PythonScript typedValue155 = PythonScript.internalMethod00581();
        ArrayList<PyIslandStatus> arrayList = new ArrayList<PyIslandStatus>();
        for (PyIslandStatus pyIslandStatus : this.all()) {
            if (!pyIslandStatus.ownedBy(typedValue155)) continue;
            arrayList.add(pyIslandStatus);
        }
        return arrayList;
    }

    public boolean remove(PyIslandStatus pyIslandStatus) {
        return pyIslandStatus != null && pyIslandStatus.remove();
    }

    public boolean remove(String string) {
        PyIslandStatus pyIslandStatus = this.find(string);
        return pyIslandStatus != null && pyIslandStatus.remove();
    }

    private static DynamicIslandHud island() {
        return RockstarClient.getInstance().internalMethod01271() == null ? null : RockstarClient.getInstance().internalMethod01271().internalMethod01259();
    }

    private static void removeOwnedDuplicate(PythonScript typedValue155, String string) {
        if (typedValue155 == null) {
            return;
        }
        DynamicIslandHud typedValue201 = PyDynamicIsland.island();
        if (typedValue201 == null) {
            return;
        }
        for (DynamicIslandStatus typedValue202 : new ArrayList<DynamicIslandStatus>(typedValue201.internalMethod05407())) {
            PyIslandStatus pyIslandStatus;
            if (!(typedValue202 instanceof PyIslandStatus) || !(pyIslandStatus = (PyIslandStatus)typedValue202).ownedBy(typedValue155) || !pyIslandStatus.getName().equalsIgnoreCase(string)) continue;
            pyIslandStatus.dispose();
            typedValue201.internalMethod05741(pyIslandStatus);
        }
    }

    private static void applySavedState(DynamicIslandHud typedValue201) {
        AbstractClientConfig typedValue138 = RockstarClient.getInstance().internalMethod03371().internalMethod01175("client");
        if (typedValue138 instanceof ClientDataConfig) {
            ClientDataConfig typedValue139 = (ClientDataConfig)typedValue138;
            typedValue139.internalMethod02871(typedValue201);
        }
    }

    private static String normalizeName(String string) {
        if (string == null || string.isBlank()) {
            return "Script Status";
        }
        return string.trim();
    }
}

