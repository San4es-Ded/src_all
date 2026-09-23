package rockstar.client.internal.script;




import rockstar.client.event.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.internal.render.BlastShockwaveRenderer;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.render.JumpShockwaveRenderer;

public class ShockwaveEffectManager
implements MinecraftClientAccess {
    public static final ShockwaveEffectManager internalField0763 = new ShockwaveEffectManager();
    private final CopyOnWriteArrayList<InternalType0274> internalField0204 = new CopyOnWriteArrayList();
    private final BlastShockwaveRenderer internalField0313 = new BlastShockwaveRenderer();
    private final EventListener<WorldChangeEvent> internalField0157 = worldChangeEvent -> this.internalField0204.clear();
    private final EventListener<Render3DEvent> internalField0158 = render3DEvent -> {
        if (this.internalField0204.isEmpty()) {
            return;
        }
        List<JumpShockwaveRenderer.InternalType0218> list = this.internalMethod03041((Render3DEvent)render3DEvent);
        if (list.isEmpty()) {
            return;
        }
        Matrix4f matrix4f = new Matrix4f((Matrix4fc)render3DEvent.getProjectionMatrix()).mul((Matrix4fc)render3DEvent.getPositionMatrix()).invert();
        this.internalField0313.internalMethod03482(matrix4f, list);
    };

    private ShockwaveEffectManager() {
        this.internalField0313.internalMethod04833();
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public void internalMethod03369() {
    }

    public void internalMethod04323(Vec3d vec3d, float f, float f2) {
        this.internalMethod04641(vec3d, f, f2, null, 1.0f);
    }

    public void internalMethod06511(Vec3d vec3d, float f, float f2, ColorRGBA colorRGBA) {
        this.internalMethod04641(vec3d, f, f2, colorRGBA, 1.0f);
    }

    public void internalMethod04641(Vec3d vec3d, float f, float f2, ColorRGBA colorRGBA, float f3) {
        if (ShockwaveEffectManager.internalField0149.world == null) {
            return;
        }
        long l = (long)(f2 * 1000.0f);
        long l2 = (long)((float)l * 0.3f);
        long l3 = Math.max(1L, l - l2);
        this.internalField0204.add(new InternalType0274(vec3d.x, vec3d.y, vec3d.z, f, l2, l3, f3));
    }

    private List<JumpShockwaveRenderer.InternalType0218> internalMethod03041(Render3DEvent render3DEvent) {
        ArrayList<JumpShockwaveRenderer.InternalType0218> arrayList = new ArrayList<JumpShockwaveRenderer.InternalType0218>();
        long l = System.currentTimeMillis();
        Vec3d vec3d = render3DEvent.getCamera().getCameraPos();
        this.internalField0204.removeIf(nestedValue2035 -> l - nestedValue2035.internalField1059 > nestedValue2035.internalField0229 + nestedValue2035.internalField0230);
        for (InternalType0274 nestedValue2036 : this.internalField0204) {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            if (arrayList.size() >= 12) break;
            long l2 = l - nestedValue2036.internalField1059;
            long l3 = nestedValue2036.internalField0229 + nestedValue2036.internalField0230;
            if (l3 <= 0L || (f5 = nestedValue2036.internalField0205 * (f4 = (float)Math.pow(f3 = MathHelper.clamp((float)((float)l2 / (float)l3), (float)0.0f, (float)1.0f), 0.6f))) <= 0.001f || (f2 = (f = 1.0f - MathHelper.clamp((float)((f3 - 0.4f) / 0.6f), (float)0.0f, (float)1.0f)) * nestedValue2036.internalField0206) <= 0.001f) continue;
            float f6 = MathHelper.clamp((float)(f5 * 0.12f), (float)0.5f, (float)1.2f);
            arrayList.add(new JumpShockwaveRenderer.InternalType0218((float)(nestedValue2036.internalField0194 - vec3d.x), (float)(nestedValue2036.internalField0193 - vec3d.y), (float)(nestedValue2036.internalField1045 - vec3d.z), f5, f6, 1.0f, 1.0f, 1.0f, f, f2));
        }
        return arrayList;
    }

    static final class InternalType0274 {
        final double internalField0194;
        final double internalField0193;
        final double internalField1045;
        final float internalField0205;
        final long internalField0229;
        final long internalField0230;
        final float internalField0206;
        final long internalField1059;

        InternalType0274(double d, double d2, double d3, float f, long l, long l2, float f2) {
            this.internalField0194 = d;
            this.internalField0193 = d2;
            this.internalField1045 = d3;
            this.internalField0205 = f;
            this.internalField0229 = l;
            this.internalField0230 = l2;
            this.internalField0206 = f2;
            this.internalField1059 = System.currentTimeMillis();
        }
    }
}
