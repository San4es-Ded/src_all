package ru.prism.module.impl.render;

import net.minecraft.text.OrderedText;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.other.Instance;

import java.util.IdentityHashMap;
import java.util.Map;

@ModuleInfo(
        name = "Animations",
        desc = "Плавные анимации интерфейса: табличка, чат, хотбар и перспектива.",
        category = Category.VISUALS
)
public class Animations extends Module {

    public static Animations get() {
        return Instance.get(Animations.class);
    }

    public BooleanSetting tabList = new BooleanSetting(this, "Табличка", true);
    public SliderSetting tabDuration = new SliderSetting(this, "Длительность таблички", 200, 50, 500, 10).setVisible(tabList::getValue);
    public BooleanSetting chat = new BooleanSetting(this, "Чат", true);
    public SliderSetting chatDuration = new SliderSetting(this, "Длительность чата", 200, 50, 500, 10).setVisible(chat::getValue);
    public BooleanSetting hotbar = new BooleanSetting(this, "Хотбар", true);
    public SliderSetting hotbarDuration = new SliderSetting(this, "Длительность хотбара", 100, 50, 500, 10).setVisible(hotbar::getValue);
    public BooleanSetting perspective = new BooleanSetting(this, "Перспектива", true);
    public SliderSetting perspectiveDuration = new SliderSetting(this, "Длительность перспективы", 300, 50, 1000, 10).setVisible(perspective::getValue);

    private final Animation tabAnimation = new Animation();
    private final Animation chatInputAnimation = new Animation();
    private final Animation hotbarAnimation = new Animation();
    private final Animation perspectiveAnimation = new Animation();
    private final Map<OrderedText, Animation> chatMessages = new IdentityHashMap<>();

    private boolean tabShown;
    private int hotbarSlot = -1;
    private boolean thirdPerson;

    {
        perspectiveAnimation.set(4.0);
    }

    @Override
    protected void onDisable() {
        chatMessages.clear();
        tabShown = false;
        hotbarSlot = -1;
        thirdPerson = false;
    }

    public void updateTab(boolean shown) {
        float duration = tabDuration.getValue() / 1000.0F;
        if (shown && !tabShown) {
            tabAnimation.run(1.0, duration, Easings.QUAD_OUT);
        } else if (!shown && tabShown) {
            tabAnimation.run(0.0, duration, Easings.QUAD_OUT);
        }
        tabShown = shown;
        tabAnimation.update();
    }

    public float tabScale() {
        return tabAnimation.get();
    }

    public boolean tabRenderable() {
        return tabShown || tabAnimation.isAlive();
    }

    public void startChatInput() {
        chatInputAnimation.set(14.0);
        chatInputAnimation.run(0.0, chatDuration.getValue() / 1000.0F, Easings.QUAD_OUT);
    }

    public void updateChatInput() {
        chatInputAnimation.update();
    }

    public float chatInputOffset() {
        return chatInputAnimation.get();
    }

    public void resetChatInput() {
        chatInputAnimation.set(0.0);
    }

    public void addChatMessage(OrderedText content, int width) {
        Animation animation = new Animation();
        animation.set(-width);
        animation.run(0.0, chatDuration.getValue() / 1000.0F, Easings.QUAD_OUT);
        chatMessages.put(content, animation);
    }

    public int chatMessageOffset(OrderedText content) {
        Animation animation = chatMessages.get(content);
        return animation == null ? 0 : Math.round(animation.get());
    }

    public void updateChatMessages() {
        chatMessages.values().forEach(Animation::update);
        chatMessages.values().removeIf(Animation::isFinished);
    }

    public void clearChatMessages() {
        chatMessages.clear();
    }

    public void updateHotbar(int slot, int center) {
        float target = center - 91 - 1 + slot * 20;
        if (hotbarSlot == -1) {
            hotbarAnimation.set(target);
            hotbarSlot = slot;
        } else if (slot == hotbarSlot) {
            if (!hotbarAnimation.isAlive()) {
                hotbarAnimation.set(target);
            }
        } else {
            hotbarAnimation.run(target, hotbarDuration.getValue() / 1000.0F, Easings.QUAD_OUT);
            hotbarSlot = slot;
        }
        hotbarAnimation.update();
    }

    public float hotbarX() {
        return hotbarAnimation.get();
    }

    public void updatePerspective(boolean value) {
        float duration = perspectiveDuration.getValue() * 1.5F / 1000.0F;
        if (value && !thirdPerson) {
            perspectiveAnimation.set(1.0);
            perspectiveAnimation.run(4.0, duration, Easings.BACK_OUT);
        } else if (!value && thirdPerson) {
            perspectiveAnimation.run(1.0, duration, Easings.BACK_OUT);
        }
        thirdPerson = value;
        perspectiveAnimation.update();
    }

    public float perspectiveDistance() {
        return perspectiveAnimation.get();
    }
}
