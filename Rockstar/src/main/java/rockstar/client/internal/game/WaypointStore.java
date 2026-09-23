package rockstar.client.internal.game;




import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.util.ClientMessages;

public class WaypointStore {
    private final Map<String, Vec3d> internalField0543 = new HashMap<String, Vec3d>();

    public final void internalMethod05412(String string, int n, int n2, int n3) {
        Vec3d vec3d = new Vec3d((double)n, (double)n2, (double)n3);
        this.internalField0543.put(string, vec3d);
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("modules.waypoints.added", string, n, n2, n3)));
    }

    public final void internalMethod06386(String string) {
        if (this.internalField0543.remove(string) != null) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("modules.waypoints.deleted", string)));
        } else {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("modules.waypoints.not_found", string)));
        }
    }

    public final void internalMethod02210() {
        this.internalField0543.clear();
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("modules.waypoints.cleared")));
    }

    public final boolean internalMethod06387(String string) {
        return this.internalField0543.containsKey(string);
    }

    public final Set<Map.Entry<String, Vec3d>> internalMethod00276() {
        return this.internalField0543.entrySet();
    }

    public final void internalMethod05883(Map<String, Vec3d> map) {
        this.internalField0543.clear();
        this.internalField0543.putAll(map);
    }
}

