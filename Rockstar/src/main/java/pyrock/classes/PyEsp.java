package pyrock.classes;




import rockstar.client.rotation.*;
import rockstar.client.esp.*;
import rockstar.client.internal.rotation.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.classes.PyEspElement;
import rockstar.client.esp.EspFeature;
import rockstar.client.esp.EspManager;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.rotation.ScreenProjectionUtils;

public class PyEsp
implements MinecraftClientAccess {
    public PyEspElement element(String string, String string2) {
        return new PyEspElement(string, string2);
    }

    public boolean enabled() {
        return EspManager.internalMethod03145();
    }

    public List<String> elements() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (EspFeature typedValue091 : EspManager.internalMethod06726().internalMethod02968()) {
            arrayList.add(typedValue091.internalMethod01940());
        }
        return arrayList;
    }

    public float[] toScreen(double d, double d2, double d3) {
        float[] fArray;
        Vec2f vec2f = ScreenProjectionUtils.internalMethod00612(new Vec3d(d, d2, d3));
        if (vec2f == null) {
            fArray = null;
        } else {
            float[] fArray2 = new float[2];
            fArray2[0] = vec2f.x;
            fArray = fArray2;
            fArray2[1] = vec2f.y;
        }
        return fArray;
    }
}

