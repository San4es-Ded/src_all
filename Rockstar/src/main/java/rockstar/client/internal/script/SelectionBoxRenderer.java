package rockstar.client.internal.script;







import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.event.EventListener;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.game.RegionSelection;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.script.WorldDrawUtils;

public final class SelectionBoxRenderer {
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> this.internalMethod01020(render3DEvent.getMatrices(), render3DEvent.getCamera());

    public static SelectionBoxRenderer internalMethod05327(NewtonCoreManager typedValue289) {
        SelectionBoxRenderer typedValue311 = new SelectionBoxRenderer();
        typedValue289.internalMethod05035().internalMethod00647(typedValue311);
        return typedValue311;
    }

    private void internalMethod01020(MatrixStack matrixStack, Camera camera) {
        Box box = RegionSelection.internalMethod00889().internalMethod03550();
        if (box == null) {
            return;
        }
        ColorRGBA colorRGBA = ThemeColors.internalMethod02531();
        ColorRGBA colorRGBA2 = new ColorRGBA(colorRGBA.getRed(), colorRGBA.getGreen(), colorRGBA.getBlue()).withAlpha(40.0f);
        ColorRGBA colorRGBA3 = new ColorRGBA(colorRGBA.getRed(), colorRGBA.getGreen(), colorRGBA.getBlue()).withAlpha(220.0f);
        Vec3d vec3d = camera.getCameraPos();
        matrixStack.push();
        matrixStack.translate(-vec3d.x, -vec3d.y, -vec3d.z);
        WorldDrawUtils.internalMethod05989(matrixStack, box, colorRGBA2);
        WorldDrawUtils.internalMethod06479(matrixStack, box, colorRGBA3);
        matrixStack.pop();
    }
}

