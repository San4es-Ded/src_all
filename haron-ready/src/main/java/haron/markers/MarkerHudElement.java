package haron.markers;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.client.MinecraftClientAccess;
import haron.events.HudRenderPreEvent;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudServices;
import haron.hud.core.HudService;
import haron.markers.AutoEventMarkerTracker;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ScaledGuiProjection;
import haron.render.icons.IconTexture;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.render.world.trp5t7;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.haron.Haron;

@HudServiceInfo
public class MarkerHudElement
extends HudService
implements MinecraftClientAccess {
    private static final float e = 22.0f;
    private static final float f = 6.0f;
    private static final float g = 3.0f;
    private static final float h = 16.0f;
    private static final float i = 5.0f;
    private static final float j = 10.0f;
    private static final float k = 2.5f;
    private static final float l = -7.0f;
    private static final float m = 8.0f;
    private static final float n = 0.0f;
    private static final float o = 64.0f;
    private static final float p = 5.0f;
    public static int a;
    public static boolean b;
    private void handler$zpn000$fixDistanceFormat(double d, CallbackInfoReturnable callbackInfoReturnable) {
        if (d < 1.0) {
            callbackInfoReturnable.setReturnValue("<1 м");
        } else if (d < 1000.0) {
            callbackInfoReturnable.setReturnValue(((int)d + " м"));
        } else {
            callbackInfoReturnable.setReturnValue(String.format("%.1f км", d / 1000.0));
        }
    }

    private String a(double d) {
        CallbackInfoReturnable callbackInfoReturnable = new CallbackInfoReturnable("a", true);
        this.handler$zpn000$fixDistanceFormat(d, callbackInfoReturnable);
        if (callbackInfoReturnable.isCancelled()) {
            return (String)callbackInfoReturnable.getReturnValue();
        }
        if (d < 1.0) {
            return "crypt";
        }
        return d >= 2.0 ? (d >= 5.0 ? String.format("crypt", d) : String.format("crypt", d)) : "crypt";
    }

    private double a(Waypoint yo0tnu2) {
        return MarkerHudElement.c.player.getPos().distanceTo(new Vec3d((double)yo0tnu2.b() + 0.5, (double)yo0tnu2.c() + 1.5, (double)yo0tnu2.d() + 0.5));
    }

    private static float a(float f) {
        return (float)Math.round(f * 2.0f) / 2.0f;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        IconTexture hclqea2 = HaronIcons.getInfo("crypt");
        if (hclqea2 != null) {
            Identifier identifier = hclqea2.a();
            RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
            float f3 = (float)hclqea2.b() / 2.0f;
            float f4 = (float)hclqea2.c() / 2.0f;
            float f5 = f2 + f3 / 2.0f;
            matrixStack.push();
            matrixStack.translate(f, f5, 0.0f);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0f));
            matrixStack.translate(-f, -f5, 0.0f);
            s7swsm2.a(identifier, f - f3 / 2.0f - 0.5f, f5 - f4 / 2.0f, f3, f4, pryrvd.aa, matrixStack);
            matrixStack.pop();
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, Waypoint yo0tnu2) {
        long l;
        AutoEventMarkerTracker g2mprb2;
        if (!(!yo0tnu2.j() || (g2mprb2 = HudServices.MAP_MARKER_MODULE).k() && this.a(yo0tnu2, g2mprb2))) {
            return;
        }
        Vec3d markerPosition = new Vec3d((double)yo0tnu2.b() + 0.5, (double)yo0tnu2.c() + 1.5, (double)yo0tnu2.d() + 0.5);
        Vec3d vec3d = trp5t7.a(markerPosition);
        if (vec3d == null || !trp5t7.b(vec3d)) {
            return;
        }
        float f = MarkerHudElement.a((float)vec3d.x);
        float f2 = MarkerHudElement.a((float)vec3d.y);
        String string = this.a(MarkerHudElement.c.player.getPos().distanceTo(markerPosition));
        FontRenderer v6hnga2 = ClientFonts.b[12];
        FontRenderer v6hnga3 = ClientFonts.b[10];
        String string2 = yo0tnu2.a();
        if (yo0tnu2.j() && yo0tnu2.r() && (l = yo0tnu2.p()) > 0L) {
            int n = (int)(l / 1000L);
            int n2 = n / 60;
            int n3 = n % 60;
            string2 = n2 <= 0 ? MarkerHudElement.$sf$0(string2, n3) : MarkerHudElement.$sf$1(string2, n2, n3);
        }
        float f3 = Math.max(72.0f, 21.5f + Math.max(v6hnga2.a(string2), v6hnga3.a(string)) + 10.0f);
        float f4 = MarkerHudElement.a(f - f3 / 2.0f);
        float f5 = MarkerHudElement.a(f2 + 25.0f);
        this.a(matrixStack, s7swsm2, f4, f5, f3, yo0tnu2);
        float f6 = MarkerHudElement.a(f4 + 3.0f);
        this.a(matrixStack, s7swsm2, f6, MarkerHudElement.a(f5 + 3.0f), yo0tnu2);
        float f7 = MarkerHudElement.a(f6 + 16.0f + 2.5f);
        float f8 = v6hnga2.b(string2);
        float f9 = v6hnga3.b(string);
        float f10 = MarkerHudElement.a(f5 + 2.0f);
        float f11 = MarkerHudElement.a(f10 + f8 - 7.0f);
        float f12 = f4 + f3 - 6.0f;
        float f13 = (f7 + f12) / 2.0f;
        float f14 = MarkerHudElement.a(f13 - v6hnga2.a(string2) / 2.0f);
        float f15 = MarkerHudElement.a(f13 - v6hnga3.a(string) / 2.0f);
        v6hnga2.a(string2, f14, (double)f10, pryrvd.a, matrixStack);
        v6hnga3.a(string, f15, (double)f11, pryrvd.a, matrixStack);
        this.a(matrixStack, s7swsm2, f, f5 + 22.0f);
    }

    private boolean a(Waypoint yo0tnu2, AutoEventMarkerTracker g2mprb2) {
        String string;
        int n = g2mprb2.h();
        String string2 = g2mprb2.i();
        String string3 = string = c.getCurrentServerEntry() != null ? MarkerHudElement.c.getCurrentServerEntry().address.toLowerCase() : "";
        if (string.contains("crypt")) {
            if (yo0tnu2.l() != n) {
                return false;
            }
            if (yo0tnu2.m() != null && string2 != null && !yo0tnu2.m().equals(string2)) {
                return false;
            }
        } else if (!string.contains("crypt") && yo0tnu2.l() != n) {
            return false;
        }
        return true;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, Waypoint yo0tnu2) {
        Color color = pryrvd.a(pryrvd.m, 200);
        Color color2 = pryrvd.a(pryrvd.n, 200);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, 23.0f, 6.0f, color, color, color2, color2, matrixStack);
        Color color3 = pryrvd.a(pryrvd.e, 230);
        Color color4 = pryrvd.a(pryrvd.f, 230);
        s7swsm2.a(f, f2, f3, 22.0f, 6.0f, color3, color3, color4, color4, matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, Waypoint yo0tnu2) {
        Color color = yo0tnu2.e();
        Color color2 = pryrvd.c(color, 150);
        Color color3 = pryrvd.b(color, 50);
        Color color4 = pryrvd.a(color3, 50);
        Color color5 = pryrvd.a(color3, 10);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 17.0f, 17.0f, 5.0f, color4, color4, color5, color5, matrixStack);
        s7swsm2.a(f, f2, 16.0f, 16.0f, 5.0f, color, color, color2, color2, matrixStack);
        ClientFonts.e[18].a(yo0tnu2.f().b(), f + 3.0f + 0.5f, (double)(f2 + 3.0f + 0.5f), pryrvd.aa, matrixStack);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler(priority=-100)
    public void a(HudRenderPreEvent tlfhp92) {
        if (MarkerHudElement.c.player == null || MarkerHudElement.c.world == null || MarkerRegistry.d()) {
            return;
        }
        ScaledGuiProjection.a(2.0);
        try {
            ShapeRenderer s7swsm2 = Haron.getInstance().getRender();
            MatrixStack matrixStack = tlfhp92.a();
            ArrayList<Waypoint> arrayList = new ArrayList<Waypoint>(MarkerRegistry.a());
            arrayList.sort(Comparator.comparingDouble(yo0tnu2 -> {
                return this.a((Waypoint)yo0tnu2);
            }).reversed());
            Iterator<Waypoint> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                this.a(matrixStack, s7swsm2, iterator.next());
            }
        }
        finally {
            ScaledGuiProjection.a();
        }
    }

    private static /* synthetic */ String $sf$0(String string, int n) {
        return string + "crypt" + n + "crypt";
    }

    private static /* synthetic */ String $sf$1(String string, int n, int n2) {
        return string + "crypt" + n + "crypt" + n2 + "crypt";
    }
}
