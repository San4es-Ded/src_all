package wtf.wyvern.client.modules.impl.render;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.Vector2f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.ui.interfaces.component.*;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.input.EventMouse;
import wtf.wyvern.core.events.impl.input.EventSetScreen;
import wtf.wyvern.core.events.impl.other.EventWindowResize;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.render.EventHudRender;
import wtf.wyvern.render.display.Render2DUtil;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.GuiUtil;
import wtf.wyvern.utility.math.MathUtil;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "Interface",
        category = Category.RENDER,
        description = "Интерфейс Клиента"
)
public final class Interface extends Module {
    public static final Interface INSTANCE = new Interface();
    private final wtf.wyvern.client.modules.api.setting.impl.ModeSetting hudMode = new wtf.wyvern.client.modules.api.setting.impl.ModeSetting("Режим", "Первый");
private final MultiBooleanSetting elementsSetting = MultiBooleanSetting.create("Элементы", List.of("Ватермарка", "Эффекты", "Модераторы", "Уведомления", "Информация", "Бинды", "Задержки", "Хелперы", "Таргет худ", "Список модулей", "Варден буст", "Хотбар", "Near"));
    private final SliderSetting hudSize = new SliderSetting(
            "Размер интерфейса", 100.0F, 50.0F, 175.0F, 1.0F,
            (oldValue, newValue) -> this.onHudScaleChanged(newValue));
    private final List<DraggableHudElement> elementsHud1 = new ArrayList<>();
    private final List<DraggableHudElement> elementsHud2 = new ArrayList<>();
    private DraggableHudElement draggingElement = null;
    private float dragOffsetX;
    private float dragOffsetY;
    long init = 0L;

    private Interface() {
        this.elementsHud1.add(new WatermarkComponent("Watermark", 0.0F, 0.0F, 960.0F, 495.5F, 10.0F, 10.0F, DraggableHudElement.Align.TOP_LEFT, false));
        this.elementsHud1.add(new PotionsComponent("Potions", 0.0F, 0.0F, 960.0F, 495.5F, 119.15234F, 73.0F, DraggableHudElement.Align.TOP_LEFT, false));
        this.elementsHud1.add(new StaffComponent("Staff", 0.0F, 0.0F, 960.0F, 495.5F, 10.0F, 73.0F, DraggableHudElement.Align.TOP_LEFT, false));
        NotifyComponent notifyComponent = new NotifyComponent("Notify", 0.0F, 0.0F, 960.0F, 495.5F, 0.0F, 50.0F, DraggableHudElement.Align.CENTER);
        this.elementsHud1.add(notifyComponent);
        Wyvern.getInstance().getNotifyManager().setNotifyComponent(notifyComponent);
        this.elementsHud2.add(notifyComponent);
        this.elementsHud1.add(new InformationComponent("Information", 0.0F, 0.0F, 960.0F, 495.5F, 10.0F, 41.5F, DraggableHudElement.Align.TOP_LEFT));
        this.elementsHud1.add(new KeybindsComponent("Keybinds", 349.0F, 0.0F, 960.0F, 495.5F, -122.0F, 73.0F, DraggableHudElement.Align.TOP_RIGHT, false));
        this.elementsHud1.add(new CoolDowns("CoolDowns", 0.0F, 0.0F, 960.0F, 495.5F, 10.0F, 145.0F, DraggableHudElement.Align.TOP_LEFT, false));
        this.elementsHud1.add(new Helpers("Helpers", 0.0F, 0.0F, 960.0F, 495.5F, 10.0F, 215.0F, DraggableHudElement.Align.TOP_LEFT));
        this.elementsHud1.add(new TargetHudComponent("TargetHUD", 166.5F, 128.5F, 960.0F, 495.5F, 0.0F, 31.75F, DraggableHudElement.Align.CENTER));
        this.elementsHud1.add(new ArrayListComponent("ArrayList", 0.0F, 0.0F, 960.0F, 495.5F, -10.0F, 10.0F, DraggableHudElement.Align.TOP_RIGHT));
        this.elementsHud1.add(new WardenBoostComponent("WardenBoost", 0.0F, 0.0F, 960.0F, 495.5F, -122.0F, 200.0F, DraggableHudElement.Align.TOP_RIGHT, false));
this.elementsHud1.add(new HootBarComponent("HotBar", 0.0F, 0.0F, 960.0F, 495.5F, 0.0F, 220.0F, DraggableHudElement.Align.CENTER));
        this.elementsHud1.add(new NearComponent("Near", 0.0F, 0.0F, 960.0F, 495.5F, -10.0F, 200.0F, DraggableHudElement.Align.TOP_RIGHT));

        this.elementsHud2.add(new WatermarkComponent("WatermarkV2", 0.0F, 0.0F, 960.0F, 495.5F, 5.0F, 5.0F, DraggableHudElement.Align.TOP_LEFT, true));
        this.elementsHud2.add(new KeybindsComponent("KeybindsV2", 0.0F, 0.0F, 960.0F, 495.5F, 5.0F, 30.0F, DraggableHudElement.Align.TOP_LEFT, true));
        this.elementsHud2.add(new CoolDowns("CoolDownsV2", 0.0F, 0.0F, 960.0F, 495.5F, 5.0F, 215.0F, DraggableHudElement.Align.TOP_LEFT, true));
        this.elementsHud2.add(new Helpers("HelpersV2", 0.0F, 0.0F, 960.0F, 495.5F, 5.0F, 270.0F, DraggableHudElement.Align.TOP_LEFT));
        this.elementsHud2.add(new PotionsComponent("PotionsV2", 0.0F, 0.0F, 960.0F, 495.5F, 5.0F, 100.0F, DraggableHudElement.Align.TOP_LEFT, true));
        this.elementsHud2.add(new TargetHudComponent("TargetHUDV2", 0.0F, 0.0F, 960.0F, 495.5F, 0.0F, 0.0F, DraggableHudElement.Align.CENTER, true));
        this.elementsHud2.add(new StaffComponent("StaffV2", 0.0F, 0.0F, 960.0F, 495.5F, -5.0F, 5.0F, DraggableHudElement.Align.TOP_RIGHT, true));
        this.elementsHud2.add(new ArrayListComponent("ArrayListV2", 0.0F, 0.0F, 960.0F, 495.5F, -5.0F, 5.0F, DraggableHudElement.Align.TOP_RIGHT));
        this.elementsHud2.add(new WardenBoostComponent("WardenBoostV2", 0.0F, 0.0F, 960.0F, 495.5F, -5.0F, 200.0F, DraggableHudElement.Align.TOP_RIGHT, true));
this.elementsHud2.add(new NearComponent("NearV2", 0.0F, 0.0F, 960.0F, 495.5F, -5.0F, 260.0F, DraggableHudElement.Align.TOP_RIGHT));
        this.elementsHud2.add(new InformationComponent("InformationV2", 0.0F, 0.0F, 960.0F, 495.5F, 5.0F, 170.0F, DraggableHudElement.Align.TOP_LEFT, true));
        this.elementsHud2.add(new HootBarComponent("HotBarV2", 0.0F, 0.0F, 960.0F, 495.5F, 0.0F, 220.0F, DraggableHudElement.Align.CENTER));
    }

    @FastNative
    private List<DraggableHudElement> getActiveElements() {
        return hudMode.is("Hud1") ? elementsHud1 : elementsHud2;
    }

    @FastNative
    public boolean isLiquidHudEnabled() {
        return false;
    }

    @FastNative
    public void onEnable() {
        this.init = System.currentTimeMillis();
        super.onEnable();
    }

    @FastNative
    public JsonObject save() {
        JsonObject object = super.save();
        JsonObject propertiesObject = new JsonObject();

        for (DraggableHudElement element : this.elementsHud1) {
            propertiesObject.add(element.getName(), element.save());
        }
        for (DraggableHudElement element : this.elementsHud2) {
            propertiesObject.add(element.getName(), element.save());
        }

        object.add("HudElements", propertiesObject);
        return object;
    }

    @FastNative
    public void load(JsonObject object) {
        super.load(object);
        if (object.has("HudElements") && object.get("HudElements").isJsonObject()) {
            JsonObject propertiesObject = object.getAsJsonObject("HudElements");

            for (DraggableHudElement element : this.elementsHud1) {
                String key = element.getName();
                if (propertiesObject.has(key) && propertiesObject.get(key).isJsonObject()) {
                    element.load(propertiesObject.getAsJsonObject(key));
                }
            }
            for (DraggableHudElement element : this.elementsHud2) {
                String key = element.getName();
                if (propertiesObject.has(key) && propertiesObject.get(key).isJsonObject()) {
                    element.load(propertiesObject.getAsJsonObject(key));
                }
            }
        }
    }

    @FastNative
    private void addElement(DraggableHudElement element) {

    }

    @EventTarget
    public void onRender(EventHudRender event) {
        if (!(mc.currentScreen instanceof ChatScreen) && this.draggingElement != null) {
            this.draggingElement.release();
            this.draggingElement = null;
        }

        CustomDrawContext ctx = event.getContext();
        float width = (float)mc.getWindow().getWidth() / this.getCustomScale();
        float height = (float)mc.getWindow().getHeight() / this.getCustomScale();
        if (!mc.options.hudHidden) {
            List<DraggableHudElement> elements = getActiveElements();
            Iterator<DraggableHudElement> var5 = elements.iterator();

            while(var5.hasNext()) {
                DraggableHudElement element = var5.next();
                if (this.shouldRender(element)) {
                    try {
                        element.render(ctx);
                    } catch (Exception ignored) {

                    }

                    if (this.draggingElement != element && System.currentTimeMillis() - this.init < 5000L) {
                        element.windowResized(width, height);
                    }
                }
            }
        }

        if (mc.currentScreen instanceof ChatScreen && this.draggingElement != null) {
            Vector2f mousePos = GuiUtil.getMouse((double)this.getCustomScale());
            double mouseX = (double)mousePos.getX();
            double mouseY = (double)mousePos.getY();
            this.draggingElement.set(ctx, (float)mouseX - this.dragOffsetX, (float)mouseY - this.dragOffsetY, this, width, height);
        }
    }

private static final List<String> COMPONENT_NAMES = List.of(
            "Watermark", "Potions", "Staff", "Notify", "Information",
            "Keybinds", "CoolDowns", "Helpers", "TargetHUD", "ArrayList",
            "WardenBoost", "HotBar", "Near"
    );
    private final java.util.Map<DraggableHudElement, Integer> settingIndexCache = new java.util.IdentityHashMap<>();

    @FastNative
    private boolean shouldRender(DraggableHudElement element) {
        Integer nameIndex = settingIndexCache.get(element);
        if (nameIndex == null) {
            nameIndex = COMPONENT_NAMES.indexOf(element.getName().replace("V2", ""));
            settingIndexCache.put(element, nameIndex);
        }
        if (nameIndex != -1 && nameIndex < elementsSetting.getBooleanSettings().size()) {
            return ((MultiBooleanSetting.Value)elementsSetting.getBooleanSettings().get(nameIndex)).isEnabled();
        }

        return true;
    }

    @FastNative
    @EventTarget
    public void onMouse(EventMouse event) {
        if (!(mc.currentScreen instanceof ChatScreen)) {
            if (this.draggingElement != null) {
                this.draggingElement.release();
                this.draggingElement = null;
            }
        } else {
            Vector2f mousePos = GuiUtil.getMouse((double)this.getCustomScale());
            double mouseX = (double)mousePos.getX();
            double mouseY = (double)mousePos.getY();
if (event.getAction() == 1 && event.getButton() == 1) {
                // ПКМ по элементу открывает его собственное меню размера.
                for (DraggableHudElement element : reversedActiveElements()) {
                    if (element instanceof NearComponent near && this.shouldRender(element)
                            && (near.isMouseOverMenu(mouseX, mouseY) || element.isMouseOver(mouseX, mouseY))) {
                        near.toggleMenu(mouseX, mouseY);
                        return;
                    }
                }
            } else if (event.getAction() == 1 && event.getButton() == 0) {
                // Клик по открытому меню не должен превращаться в перетаскивание.
                for (DraggableHudElement element : reversedActiveElements()) {
                    if (element instanceof NearComponent near && near.isMenuOpen()) {
                        if (near.onMouseClicked(mouseX, mouseY)) {
                            return;
                        }
                        near.closeMenu();
                    }
                }

                for (DraggableHudElement element : reversedActiveElements()) {
                    if (this.shouldRender(element) && element.isMouseOver(mouseX, mouseY)) {
                        this.draggingElement = element;
                        this.dragOffsetX = (float)mouseX - element.getX();
                        this.dragOffsetY = (float)mouseY - element.getY();
                        break;
                    }
                }
            } else if (event.getAction() == 0) {
                for (DraggableHudElement element : getActiveElements()) {
                    if (element instanceof NearComponent near) {
                        near.onMouseReleased();
                    }
                }

                if (this.draggingElement != null) {
                    this.draggingElement.release();
                    this.draggingElement = null;
                }
            }
        }
    }

    private List<DraggableHudElement> reversedActiveElements() {
        List<DraggableHudElement> reversed = new ArrayList<>(getActiveElements());
        Collections.reverse(reversed);
        return reversed;
    }

    @FastNative
    public float getCustomScale() {
        return Math.max(1.0F, hudSize.getCurrent() / 50.0F);
    }

    @FastNative
    private void onHudScaleChanged(float percent) {
        if (mc == null || mc.getWindow() == null) return;

        float scale = Math.max(1.0F, percent / 50.0F);
        float width = (float) mc.getWindow().getWidth() / scale;
        float height = (float) mc.getWindow().getHeight() / scale;

        for (DraggableHudElement element : elementsHud1) {
            element.windowResized(width, height);
        }
        for (DraggableHudElement element : elementsHud2) {
            element.windowResized(width, height);
        }
    }

    @FastNative
    public org.joml.Vector2f getNearest(float x, float y) {
        float minDeltaX = Float.MAX_VALUE;
        float minDeltaY = Float.MAX_VALUE;
        float thoroughness = 0.0F;
        org.joml.Vector2f nearest = new org.joml.Vector2f(-1.0F, -1.0F);
        Iterator<DraggableHudElement> var7 = getActiveElements().iterator();

        float minX;
        float minY;
        float deltaX;
        float deltaY;
        while(var7.hasNext()) {
            DraggableHudElement s = var7.next();
            if (!s.equals(this.draggingElement)) {
                minX = s.getX();
                minY = s.getY();
                deltaX = s.getX() + s.getWidth();
                deltaY = s.getY() + s.getHeight();
                float tempXC = s.getX() + s.getWidth() / 2.0F;
                float tempYC = s.getY() + s.getHeight() / 2.0F;
                float nearestX = this.getNearest(minX, deltaX, tempXC, x);
                float nearestY = this.getNearest(minY, deltaY, tempYC, y);
                float nearestDeltaX = MathUtil.goodSubtract(nearestX, x);
                float nearestDeltaY = MathUtil.goodSubtract(nearestY, y);
                if (nearestDeltaX < minDeltaX) {
                    minDeltaX = nearestDeltaX;
                    if (nearestDeltaX < thoroughness) {
                        nearest.x = nearestX;
                    }
                }

                if (nearestDeltaY < minDeltaY) {
                    minDeltaY = nearestDeltaY;
                    if (nearestDeltaY < thoroughness) {
                        nearest.y = nearestY;
                    }
                }
            }
        }

        if (nearest.x == -1.0F || nearest.y == -1.0F) {
            float tempXA = (float)mc.getWindow().getScaledWidth() / 2.0F;
            float tempYA = (float)mc.getWindow().getScaledHeight() / 2.0F;
            minX = this.getNearest(tempXA, tempXA, tempXA, x);
            minY = this.getNearest(tempYA, tempYA, tempYA, y);
            deltaX = MathUtil.goodSubtract(minX, x);
            deltaY = MathUtil.goodSubtract(minY, y);
            if (deltaX < minDeltaX && deltaX < thoroughness) {
                nearest.x = minX;
            }

            if (deltaY < minDeltaY && deltaY < thoroughness) {
                nearest.y = minY;
            }
        }

        return nearest;
    }

    @FastNative
    public float getNearest(float a, float b, float c, float target) {
        float nearest = a;
        if (MathUtil.goodSubtract(b, target) < MathUtil.goodSubtract(a, target)) {
            nearest = b;
        }

        if (MathUtil.goodSubtract(c, target) < MathUtil.goodSubtract(nearest, target)) {
            nearest = c;
        }

        return nearest;
    }

    @FastNative
    public boolean isEnableScoreBar() {
        return false;
    }

    @FastNative
    public boolean isEnableHotBar() {
        return this.isEnabled() && ((MultiBooleanSetting.Value) elementsSetting.getBooleanSettings().get(11)).isEnabled();
    }

    @FastNative
    public boolean isEnableTab() {
        return false;
    }

    @FastNative
    @EventTarget
    public void resize(EventWindowResize eventWindowResize) {
        float width = (float)mc.getWindow().getWidth() / this.getCustomScale();
        float height = (float)mc.getWindow().getHeight() / this.getCustomScale();
        Iterator<DraggableHudElement> var4 = getActiveElements().iterator();

        while(var4.hasNext()) {
            DraggableHudElement element = var4.next();
            element.windowResized(width, height);
        }
    }

    @EventTarget
    public void update(EventUpdate eventUpdate) {
        if (Render2DUtil.glowCache.size() > 400) {
            Render2DUtil.glowCache.values().removeIf((v) -> {
                if (v.tick()) {
                    v.destroy();
                    return true;
                } else {
                    return false;
                }
            });
        }

        Iterator<DraggableHudElement> var2 = getActiveElements().iterator();

        while(var2.hasNext()) {
            DraggableHudElement draggableHudElement = var2.next();
            draggableHudElement.tick();
        }
    }

    @FastNative
    @EventTarget
    public void screenEvent(EventSetScreen event) {
        if (event.getScreen() instanceof ChatScreen) {
            this.init = System.currentTimeMillis();
        }
    }

    @Generated
    public DraggableHudElement getDraggingElement() {
        return this.draggingElement;
    }

    @FastNative
    public int getGlowRadius() {
        return 10;
    }
}