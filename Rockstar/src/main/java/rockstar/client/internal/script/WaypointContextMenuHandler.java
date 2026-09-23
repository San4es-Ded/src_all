package rockstar.client.internal.script;








import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.window.ChatClickEvent;
import pyrock.events.window.ChatReleaseEvent;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.internal.script.AutopilotCommandHandler;
import rockstar.client.ui.MouseButton;
import rockstar.client.event.EventListener;
import rockstar.client.internal.script.WaypointLabelRenderer;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.ui.UiUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.rotation.ScreenProjectionUtils;
import rockstar.client.internal.core.NewtonApiHolder;

public class WaypointContextMenuHandler
implements MinecraftClientAccess {
    private HudContextMenu internalField0574;
    private final EventListener<ChatRenderEvent> internalField0157 = chatRenderEvent -> {
        if (this.internalField0574 == null) {
            return;
        }
        UiRenderContext iII = UiRenderContext.internalMethod02316(chatRenderEvent.getContext(), WaypointContextMenuHandler.internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().x(), WaypointContextMenuHandler.internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().y(), MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false));
        this.internalField0574.internalMethod03398(iII);
    };
    private final EventListener<ChatClickEvent> internalField0158 = chatClickEvent -> {
        if (this.internalField0574 != null) {
            this.internalField0574.internalMethod01643(chatClickEvent.getX(), chatClickEvent.getY(), MouseButton.internalMethod01669(chatClickEvent.getButton()));
            if (this.internalField0574.internalMethod04933(chatClickEvent.getX(), chatClickEvent.getY())) {
                return;
            }
            this.internalField0574.internalMethod05781(false);
        }
        if (chatClickEvent.getButton() != 1) {
            return;
        }
        Map.Entry<String, Vec3d> entry = this.internalMethod00214(chatClickEvent.getX(), chatClickEvent.getY());
        if (entry != null) {
            this.internalMethod06251(chatClickEvent.getX(), chatClickEvent.getY(), entry.getKey(), entry.getValue());
        }
    };
    private final EventListener<ChatReleaseEvent> internalField1028 = chatReleaseEvent -> {
        if (this.internalField0574 != null) {
            this.internalField0574.internalMethod02863(chatReleaseEvent.getX(), chatReleaseEvent.getY(), MouseButton.internalMethod01669(chatReleaseEvent.getButton()));
        }
    };

    public WaypointContextMenuHandler() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    private void internalMethod06251(float f, float f2, String string, Vec3d vec3d) {
        boolean bl;
        this.internalField0574 = new HudContextMenu(f, f2, 110.0f, 6.0f).internalMethod04738(string).internalMethod03873().internalMethod03323(LanguageManager.internalMethod07214("way.autopilot"), "plane", typedValue190 -> {
            AutopilotCommandHandler typedValue131 = AutopilotCommandHandler.internalMethod05304();
            if (typedValue131 != null) {
                typedValue131.internalMethod00817(new Vec3d(vec3d.x + 0.5, vec3d.y, vec3d.z + 0.5));
            }
            typedValue190.internalMethod05781(false);
        });
        boolean bl2 = bl = NewtonApiHolder.internalMethod05192() && NewtonApiHolder.internalMethod01856().internalMethod00137();
        if (bl) {
            this.internalField0574.internalMethod03323(LanguageManager.internalMethod07214("way.stop"), "xmark", typedValue190 -> {
                NewtonApiHolder.internalMethod01856().internalMethod00136();
                typedValue190.internalMethod05781(false);
            });
        } else {
            this.internalField0574.internalMethod03323(LanguageManager.internalMethod07214("way.goto"), "path", typedValue190 -> {
                if (NewtonApiHolder.internalMethod05192()) {
                    NewtonApiHolder.internalMethod01856().internalMethod02230(new BlockPos((int)Math.floor(vec3d.x), (int)Math.floor(vec3d.y), (int)Math.floor(vec3d.z)));
                }
                typedValue190.internalMethod05781(false);
            });
        }
        this.internalField0574.internalMethod03323(LanguageManager.internalMethod07214("remove"), "trash", typedValue190 -> {
            RockstarClient.getInstance().internalMethod06121().internalMethod06386(string);
            typedValue190.internalMethod05781(false);
        });
    }

    public boolean internalMethod04179(double d, double d2) {
        return this.internalMethod00214(d, d2) != null;
    }

    private Map.Entry<String, Vec3d> internalMethod00214(double d, double d2) {
        if (WaypointContextMenuHandler.internalField0149.player == null || WaypointContextMenuHandler.internalField0149.world == null) {
            return null;
        }
        for (Map.Entry<String, Vec3d> entry : RockstarClient.getInstance().internalMethod06121().internalMethod00276()) {
            float f;
            float f2;
            float f3;
            Vec3d vec3d = entry.getValue();
            Vec2f vec2f = ScreenProjectionUtils.internalMethod00612(vec3d.add(0.0, 0.5, 0.0));
            if (vec2f == null) continue;
            float f4 = (float)WaypointContextMenuHandler.internalField0149.player.getEntityPos().distanceTo(vec3d.add(0.5, 0.5, 0.5));
            float f5 = MathHelper.clamp((float)(1.1f - f4 / 100.0f), (float)0.6f, (float)1.1f);
            WaypointLabelRenderer.InternalType0471 nestedValue2060 = WaypointLabelRenderer.internalMethod06029(entry.getKey(), f4);
            float f6 = vec2f.x + nestedValue2060.internalMethod06498() * f5;
            if (!UiUtils.internalMethod05785(f6, f3 = vec2f.y + nestedValue2060.internalMethod06546() * f5, f2 = nestedValue2060.internalMethod08636() * f5, f = nestedValue2060.internalMethod08637() * f5, d, d2)) continue;
            return entry;
        }
        return null;
    }
}

