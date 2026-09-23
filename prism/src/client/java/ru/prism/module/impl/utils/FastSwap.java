package ru.prism.module.impl.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import ru.prism.manager.event_impl.EventDisplay;
import ru.prism.manager.event_impl.EventKey;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BindSetting;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.math.Keyboard;
import ru.prism.utils.render.font.Font;
import ru.prism.utils.render.font.Fonts;

import java.util.List;
import java.util.function.Predicate;

@ModuleInfo(
        name = "Fast Swap",
        desc = "Быстрый доступ к хорусам, пёрлам и трапкам — предмет прыгает в руку по нажатию клавиши, без ковыряния в инвентаре.",
        category = Category.UTILITIES
)
public class FastSwap extends Module {

    public final BindSetting chorusKey = new BindSetting(this, "Кнопка хоруса", -1);
    public final BindSetting pearlKey = new BindSetting(this, "Кнопка эндерпёрла", -1);
    public final BindSetting healPotionKey = new BindSetting(this, "Кнопка зелья исцеления", -1);
    public final BindSetting trapKey = new BindSetting(this, "Кнопка трапки", -1);
    public final BindSetting plastKey = new BindSetting(this, "Кнопка пласта", -1);
    public final BindSetting disorientKey = new BindSetting(this, "Кнопка дезориентации", -1);
    public final BindSetting dustKey = new BindSetting(this, "Кнопка явной пыли", -1);
    public final BindSetting ballLightningKey = new BindSetting(this, "Кнопка шаровой молнии", -1);
    public final BindSetting slimeKey = new BindSetting(this, "Кнопка кома слизи", -1);
    public final BindSetting turtleGrabKey = new BindSetting(this, "Кнопка черепашьего захвата", -1);
    public final BindSetting spiderFateKey = new BindSetting(this, "Кнопка паучьей судьбы", -1);
    public final BindSetting stunKey = new BindSetting(this, "Кнопка стана", -1);
    public final BindSetting magneticOrbKey = new BindSetting(this, "Кнопка магнитного шара", -1);
    public final BindSetting explosiveTrapKey = new BindSetting(this, "Кнопка взрывной трапки", -1);
    public final BindSetting explosiveItemKey = new BindSetting(this, "Кнопка взрывной штучки", -1);
    public final BindSetting starStunKey = new BindSetting(this, "Кнопка стана звезды", -1);
    public final BindSetting snowballKey = new BindSetting(this, "Кнопка кома снега", -1);

    public final BooleanSetting showBinds = new BooleanSetting(this, "Показывать бинды", true);
    public final SliderSetting fontScale = new SliderSetting(this, "Размер шрифта", 1.0F, 0.5F, 2.0F, 0.1F);

    private final List<SwapRule> rules = List.of(
            new SwapRule(chorusKey, stack -> stack.isOf(Items.CHORUS_FRUIT)),
            new SwapRule(pearlKey, stack -> stack.isOf(Items.ENDER_PEARL)),
            new SwapRule(healPotionKey, this::isHealPotion),
            new SwapRule(trapKey, stack -> stack.isOf(Items.NETHERITE_SCRAP)),
            new SwapRule(plastKey, stack -> stack.isOf(Items.DRIED_KELP)),
            new SwapRule(disorientKey, stack -> stack.isOf(Items.ENDER_EYE)),
            new SwapRule(dustKey, stack -> stack.isOf(Items.SUGAR)),
            new SwapRule(ballLightningKey, stack -> stack.isOf(Items.NETHER_STAR)),
            new SwapRule(slimeKey, stack -> stack.isOf(Items.SLIME_BALL)),
            new SwapRule(turtleGrabKey, stack -> stack.isOf(Items.TURTLE_SCUTE)),
            new SwapRule(spiderFateKey, stack -> stack.isOf(Items.COBWEB)),
            new SwapRule(stunKey, stack -> stack.isOf(Items.ENDER_EYE)),
            new SwapRule(magneticOrbKey, stack -> stack.isOf(Items.FIREWORK_STAR)),
            new SwapRule(explosiveTrapKey, stack -> stack.isOf(Items.PRISMARINE_SHARD)),
            new SwapRule(explosiveItemKey, stack -> stack.isOf(Items.FIRE_CHARGE)),
            new SwapRule(starStunKey, stack -> stack.isOf(Items.NETHER_STAR)),
            new SwapRule(snowballKey, stack -> stack.isOf(Items.SNOWBALL))
    );

    @EventHandler
    public void onKey(EventKey event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        int key = event.getKey();
        if (key <= 0) return;

        for (SwapRule rule : rules) {
            if (rule.key.get() != key) continue;

            int slot = findInHotbar(rule.predicate);
            if (slot != -1) {
                mc.player.getInventory().setSelectedSlot(slot);
            }
            return;
        }
    }

    @EventHandler
    public void onDisplay(EventDisplay event) {
        if (mc.player == null || mc.world == null || !showBinds.getValue()) return;

        Font font = Fonts.sf_bold;
        float baseSize = 6.5F;
        float scale = fontScale.getValue();
        float centerX = mc.getWindow().getScaledWidth() / 2F;
        float baseY = mc.getWindow().getScaledHeight() - 19F;

        PlayerInventory inventory = mc.player.getInventory();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) continue;

            int key = overlayKey(stack);
            if (key <= 0) continue;

            String name = Keyboard.keyName(key);
            float fit = Math.min(1.0F, 14.0F / Math.max(1.0F, font.getWidth(name, baseSize)));
            float size = baseSize * fit * scale;
            float x = centerX - 91F + i * 20F + 2F;

            font.draw(name, x + 0.6F, baseY + 0.6F, size, ColorUtil.getColor(0, 0.65F));
            font.draw(name, x, baseY, size, ColorUtil.getColor(255));
        }
    }

    private int overlayKey(ItemStack stack) {
        for (SwapRule rule : rules) {
            if (rule.key.get() > 0 && rule.predicate.test(stack)) {
                return rule.key.get();
            }
        }
        return -1;
    }

    private int findInHotbar(Predicate<ItemStack> predicate) {
        if (mc.player == null) return -1;

        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (predicate.test(inventory.getStack(i))) {
                return i;
            }
        }
        return -1;
    }

    private boolean isHealPotion(ItemStack stack) {
        if (!stack.isOf(Items.POTION)) return false;

        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (contents == null) return false;

        for (StatusEffectInstance effect : contents.getEffects()) {
            if (effect.getEffectType().value() == StatusEffects.INSTANT_HEALTH.value()) {
                return true;
            }
        }
        return false;
    }

    private static final class SwapRule {
        private final BindSetting key;
        private final Predicate<ItemStack> predicate;

        private SwapRule(BindSetting key, Predicate<ItemStack> predicate) {
            this.key = key;
            this.predicate = predicate;
        }
    }
}
