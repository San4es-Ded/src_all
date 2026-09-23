package wtf.wyvern.client.ui.interfaces.component;

import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Font;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import net.minecraft.client.gui.screen.ChatScreen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ArrayListComponent extends DraggableHudElement {
    private final Animation widthAnimation;
    private final Animation alpha;
    private final List<Module> activeModules = new ArrayList<>();
    private final Map<Module, NameWidth> nameWidths = new IdentityHashMap<>();
    private final Comparator<Module> moduleWidthComparator =
            Comparator.comparingDouble(module -> -getTextWidth(module));

    public ArrayListComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.alpha = new Animation(200L, Easing.CUBIC_OUT);
    }

    @Override
    public void render(CustomDrawContext ctx) {
        activeModules.clear();
        for (Module module : Wyvern.getInstance().getModuleManager().getModules()) {
            if (module.getAnimation().getValue() > 0.001F) {
                activeModules.add(module);
            }
        }
        activeModules.sort(moduleWidthComparator);

        boolean isFound = !activeModules.isEmpty();
        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (mc.currentScreen instanceof ChatScreen) {
            this.alpha.update(1.0F);
        }

        if (this.alpha.getValue() < 0.01F) return;

        float posX = this.getX();
        float posY = this.getY();
        float currentY = posY;
        float maxWidth = 0;

        for (Module module : activeModules) {
            float textWidth = getTextWidth(module);
            maxWidth = Math.max(maxWidth, textWidth + 8.0F);
        }

        this.width = maxWidth;

        float blurY = posY;
        for (int i = 0; i < activeModules.size(); i++) {
            Module module = activeModules.get(i);
            float anim = module.getAnimation().getValue();
            if (anim > 0.01F) {
                float textWidth = getTextWidth(module);
                float width = textWidth + 8.0F;
                float height = 12.0F * anim;
                float x = posX + (this.width - width);
                float currentAlpha = this.alpha.getValue() * anim;

                float r = 2.0F;
                BorderRadius radius = new BorderRadius(r, (i == 0) ? r : 0.0F, (i == activeModules.size() - 1) ? r : 0.0F, r);

                DrawUtil.drawBlur(ctx.getMatrices(), x, blurY, width, height, 15.0F, radius, ColorRGBA.WHITE.withAlpha(currentAlpha * 255.0F));
                blurY += height;
            }
        }

        float rectY = posY;
        for (int i = 0; i < activeModules.size(); i++) {
            Module module = activeModules.get(i);
            float anim = module.getAnimation().getValue();
            if (anim > 0.01F) {
                float textWidth = getTextWidth(module);
                float width = textWidth + 8.0F;
                float height = 12.0F * anim;
                float x = posX + (this.width - width);
                float currentAlpha = this.alpha.getValue() * anim;

                float r = 2.0F;
                BorderRadius radius = new BorderRadius(r, (i == 0) ? r : 0.0F, (i == activeModules.size() - 1) ? r : 0.0F, r);

                DrawUtil.drawRoundedRect(ctx.getMatrices(), x, rectY, width, height, radius, new ColorRGBA(0, 0, 0, currentAlpha * 125.0F));
                rectY += height;
            }
        }

        float textY = posY;
        for (int i = 0; i < activeModules.size(); i++) {
            Module module = activeModules.get(i);
            float anim = module.getAnimation().getValue();
            if (anim > 0.01F) {
                float textWidth = getTextWidth(module);
                float width = textWidth + 8.0F;
                float height = 12.0F * anim;
                float x = posX + (this.width - width);
                float currentAlpha = this.alpha.getValue() * anim;

                ColorRGBA textC = ColorRGBA.WHITE.withAlpha((int) (255 * currentAlpha));
                Font font = Fonts.REGULAR.getFont(8.0F);
                ctx.drawText(font, module.getName(), x + 4.0F, textY + (height - font.height()) / 2.0F + 1.0F, textC);

                textY += height;
            }
        }

        this.height = textY - posY;
    }

    private float getTextWidth(Module module) {
        String name = module.getName();
        NameWidth cached = nameWidths.get(module);
        if (cached == null || !cached.name.equals(name)) {
            cached = new NameWidth(name, Fonts.REGULAR.getWidth(name, 8.0F));
            nameWidths.put(module, cached);
        }
        return cached.width;
    }

    private record NameWidth(String name, float width) {
    }
}
