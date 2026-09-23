package rockstar.client.internal.ui;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;

public final class ItemGuiAlphaCompat {
    private static final Map<ItemGuiElementRenderState, Float> ALPHAS = Collections.synchronizedMap(new WeakHashMap<>());

    private ItemGuiAlphaCompat() {
    }

    public static void register(ItemGuiElementRenderState state, float alpha) {
        ALPHAS.put(state, Math.clamp(alpha, 0.0F, 1.0F));
    }

    public static float get(ItemGuiElementRenderState state) {
        return ALPHAS.getOrDefault(state, 1.0F);
    }
}
