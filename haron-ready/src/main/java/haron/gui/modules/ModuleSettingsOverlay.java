package haron.gui.modules;

import haron.gui.core.ClickGuiOverlay;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.core.ClickGuiTabType;
import haron.gui.modules.ModulesTab;
import haron.gui.modules.ModuleCard;
import haron.render.ShapeRenderer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleSettingsOverlay
implements ClickGuiOverlay {
    private static final float MODULE_LIST_TOP_OFFSET = 48.0f;
    private static final float MODULE_LIST_BOTTOM_PADDING = 5.0f;
    private final ClickGuiScreen screen;
    private int mouseX;
    private int mouseY;

    private ModuleCard findHoveredSettingsCard(List<ModuleCard> list, int n, int n2) {
        for (ModuleCard sudbet2 : list) {
            if (sudbet2.f() || sudbet2.g() || !sudbet2.f(n, n2)) continue;
            return sudbet2;
        }
        return null;
    }

    public ModuleSettingsOverlay(ClickGuiScreen xm1fpp2) {
        this.screen = xm1fpp2;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        ClickGuiTab ta3d0p2 = this.screen.a();
        if (ta3d0p2 instanceof ModulesTab) {
            ModulesTab so7bsc2 = (ModulesTab)ta3d0p2;
            if (ta3d0p2.a() != ClickGuiTabType.MODULES) {
                return;
            }
            List<ModuleCard> list = so7bsc2.h();
            if (list.isEmpty()) {
                return;
            }
            this.mouseX = n;
            this.mouseY = n2;
            float f3 = f2 + 48.0f;
            float f4 = ClickGuiScreen.e() - 48.0f - 5.0f;
            ModuleCard sudbet2 = this.findHoveredSettingsCard(list, n, n2);
            for (ModuleCard sudbet3 : list) {
                sudbet3.a(matrixStack, s7swsm2, f, f2, n, n2, f3, f4, sudbet2 == null || sudbet2 == sudbet3);
            }
        }
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        ClickGuiTab ta3d0p2 = this.screen.a();
        if (ta3d0p2 instanceof ModulesTab) {
            ModulesTab so7bsc2 = (ModulesTab)ta3d0p2;
            if (ta3d0p2.a() != ClickGuiTabType.MODULES) {
                return;
            }
            Iterator<ModuleCard> iterator = so7bsc2.h().iterator();
            while (iterator.hasNext()) {
                iterator.next().a(matrixStack, s7swsm2, n, n2);
            }
        }
    }
}

