package haron.gui.core;

import haron.gui.core.ClickGuiOverlay;
import haron.gui.core.CategorySelectionModel;
import haron.render.ShapeRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class CategorySelectorOverlay
implements ClickGuiOverlay {
    private final CategorySelectionModel c;
    public static int a;
    public static boolean b;

    public CategorySelectorOverlay(CategorySelectionModel byxqxn2) {
        this.c = byxqxn2;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
    }
}

