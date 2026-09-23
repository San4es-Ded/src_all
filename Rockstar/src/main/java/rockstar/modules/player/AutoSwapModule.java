package rockstar.modules.player;










import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.CustomItemUtils;
import rockstar.client.internal.inventory.CustomItemRankingUtils;
import rockstar.client.util.GameUtils;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;
import rockstar.modules.combat.AutoTotemModule;
import rockstar.client.notification.ItemNotification;

@ModuleInfo(name="Auto Swap", category=ModuleCategory.PLAYER)
public class AutoSwapModule
extends Module {
    private KeybindSetting internalField0648;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting internalField0669;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting internalField1272;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting.InternalType0088 internalField1067;
    private KeybindSetting internalField0647;
    private KeybindSetting internalField1260;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting internalField0674;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;
    private MultiSelectSetting.InternalType0091 internalField1488;
    private MultiSelectSetting.InternalType0091 internalField1490;
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;
    private int internalField0227 = -1;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final Stopwatch internalField0518 = new Stopwatch();
    private float internalField0206 = -1.0f;
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        KeybindSetting typedValue161;
        if (keyPressEvent.getAction() != 1 || AutoSwapModule.internalField0149.currentScreen != null) {
            return;
        }
        KeybindSetting typedValue162 = typedValue161 = this.internalField1272.internalMethod06103(this.internalField1066) ? this.internalField0647 : this.internalField1260;
        if (typedValue161.internalMethod02165(keyPressEvent.getKey())) {
            this.internalMethod09448();
        }
        if (this.internalField0648.internalMethod02165(keyPressEvent.getKey()) && !this.internalField0276 && this.internalMethod09460()) {
            this.internalMethod09451();
        }
    };
    private final EventListener<MouseEvent> internalField0158 = mouseEvent -> {
        if (AutoSwapModule.internalField0149.currentScreen != null) {
            return;
        }
        if (this.internalField0647.internalMethod02165(mouseEvent.getButton()) || this.internalField1260.internalMethod02165(mouseEvent.getButton())) {
            boolean bl = this.internalField0277 = !this.internalField0277;
        }
        if (this.internalField0648.internalMethod02165(mouseEvent.getButton()) && !this.internalField0276 && this.internalMethod09460()) {
            this.internalMethod09451();
        }
    };

    public AutoSwapModule() {
        this.internalMethod09274();
    }

    private void internalMethod09274() {
        this.internalField0648 = new KeybindSetting(this, "modules.settings.auto_swap.button");
        this.internalField0668 = new ModeSetting(this, "modules.settings.auto_swap.item");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_swap.item.talisman").select();
        this.internalField0669 = new ModeSetting(this, "modules.settings.auto_swap.swap_to");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_swap.swap_to.talisman").select();
        this.internalField1272 = new ModeSetting(this, "\u0421\u0435\u0440\u0432\u0435\u0440");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField1272, "HW").select();
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField1272, "VonTam");
        this.internalField0647 = new KeybindSetting(this, "modules.settings.auto_swap.auto_cerber", () -> !this.internalField1272.internalMethod06103(this.internalField1066));
        this.internalField1260 = new KeybindSetting(this, "\u0410\u0432\u0442\u043e \u044f\u0440\u043e\u0441\u0442\u044c/\u043a\u0430\u0440\u0430\u0442\u0435\u043b\u044c", () -> !this.internalField1272.internalMethod06103(this.internalField1067));
        this.internalField0675 = new MultiSelectSetting((SettingOwner)this, "\u0411\u0440\u0430\u0442\u044c \u0426\u0435\u0440\u0431\u0435\u0440", () -> !this.internalField1272.internalMethod06103(this.internalField1066));
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u0412\u0441\u0435\u0433\u0434\u0430");
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u041a\u043e\u0433\u0434\u0430 \u0431\u0435\u0437 \u043c\u0435\u0447\u0430", this.internalField0245::isSelected).select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u041a\u043e\u0433\u0434\u0430 \u043b\u0438\u0432\u0430\u0435\u0442", this.internalField0245::isSelected).select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u041a\u043e\u0433\u0434\u0430 \u0432 \u044d\u043b\u0438\u0442\u0440\u0435", this.internalField0245::isSelected).select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u0415\u0441\u043b\u0438 \u0442\u0435\u0431\u044f \u043d\u0435 \u0431\u044c\u044e\u0442");
        this.internalField0674 = new MultiSelectSetting((SettingOwner)this, "\u0411\u0440\u0430\u0442\u044c \u044f\u0440\u043e\u0441\u0442\u044c/\u043a\u0430\u0440\u0430\u0442\u0435\u043b\u044f", () -> !this.internalField1272.internalMethod06103(this.internalField1067));
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0674, "\u0412\u0441\u0435\u0433\u0434\u0430");
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0674, "\u041a\u043e\u0433\u0434\u0430 \u0431\u0435\u0437 \u043c\u0435\u0447\u0430", this.internalField1072::isSelected).select();
        this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0674, "\u041a\u043e\u0433\u0434\u0430 \u043b\u0438\u0432\u0430\u0435\u0442", this.internalField1072::isSelected).select();
        this.internalField1490 = new MultiSelectSetting.InternalType0091(this.internalField0674, "\u041a\u043e\u0433\u0434\u0430 \u0432 \u044d\u043b\u0438\u0442\u0440\u0435", this.internalField1072::isSelected).select();
        new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_swap.swap_to.orb");
        new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_swap.item.orb");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1099 = false;
        this.internalField0227 = -1;
        this.internalMethod09276();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1099 = false;
        this.internalField0227 = -1;
        this.internalMethod09276();
    }

    private void internalMethod09276() {
        this.internalField0518.internalMethod02364(0L);
        this.internalField0206 = -1.0f;
    }

    @Override
    public void internalMethod08229() {
        LivingEntity livingEntity;
        LivingEntity livingEntity2;
        Entity entity = RockstarClient.getInstance().internalMethod04463().internalMethod04526();
        LivingEntity livingEntity3 = livingEntity2 = entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
        if (AutoSwapModule.internalField0149.player == null) {
            return;
        }
        this.internalMethod09461();
        boolean bl = this.internalMethod07427(InventoryUtils.internalMethod06162().internalMethod03427());
        if (!bl) {
            this.internalField0276 = false;
        }
        if (this.internalField0276 && this.internalField0227 != -1 && !this.internalMethod00609(livingEntity2)) {
            InventoryUtils.internalMethod08821(this.internalField0227, 40);
            this.internalField1099 = true;
            this.internalField0227 = -1;
            this.internalField0276 = false;
            return;
        }
        if (!this.internalField0277 || livingEntity2 == null) {
            return;
        }
        if (this.internalMethod00609(livingEntity2) && !this.internalField0276 && this.internalMethod09460()) {
            this.internalMethod09459();
            this.internalField0276 = true;
        }
    }

    public boolean internalMethod09275() {
        if (!this.isEnabled() || AutoSwapModule.internalField0149.player == null) {
            return false;
        }
        return this.internalField0277 && (this.internalField0276 || this.internalMethod07427(InventoryUtils.internalMethod06162().internalMethod03427()));
    }

    private void internalMethod09448() {
        boolean bl = this.internalField0277 = !this.internalField0277;
        if (!this.internalField0277 && this.internalField0276 && this.internalField0227 != -1) {
            InventoryUtils.internalMethod08821(this.internalField0227, 40);
            this.internalField0227 = -1;
            this.internalField0276 = false;
        }
    }

    private void internalMethod09451() {
        boolean bl;
        if (AutoSwapModule.internalField0149.currentScreen != null || ServerUtils.internalMethod01786(KnownServer.internalField0579) && !this.internalField0519.internalMethod02365(150L)) {
            return;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()).internalMethod07591(InventorySlots.internalMethod07766());
        boolean bl2 = bl = !this.internalField0237.isSelected() && !this.internalField0238.isSelected();
        if (this.internalField1066.isSelected() && bl) {
            this.internalMethod06647(typedValue228);
            return;
        }
        List<@Nullable InventorySlot> list = typedValue228.internalMethod07613(this.internalField0237.isSelected() ? Items.TOTEM_OF_UNDYING : Items.PLAYER_HEAD);
        List<@Nullable InventorySlot> list2 = typedValue228.internalMethod07613(this.internalField0238.isSelected() ? Items.TOTEM_OF_UNDYING : Items.PLAYER_HEAD);
        InventorySlot typedValue224 = list.stream().min(Comparator.comparingInt(typedValue222 -> CustomItemRankingUtils.internalMethod05103(typedValue222.internalMethod03427()) - (typedValue222.internalMethod06662() == 45 ? 199 : this.internalMethod01943((InventorySlot)typedValue222)))).orElse(null);
        InventorySlot typedValue225 = list2.stream().filter(typedValue223 -> typedValue224 != typedValue223).min(Comparator.comparingInt(typedValue222 -> CustomItemRankingUtils.internalMethod05103(typedValue222.internalMethod03427()) - (typedValue222.internalMethod06662() == 45 ? 199 : this.internalMethod01943((InventorySlot)typedValue222)))).orElse(null);
        if (typedValue224 == null || typedValue225 == null) {
            return;
        }
        InventoryUtils.internalMethod08821((AutoSwapModule.internalField0149.player.getOffHandStack().getItem() == typedValue224.internalMethod00210() ? typedValue225 : typedValue224).internalMethod06662(), 40);
        this.internalField0519.internalMethod00701();
        ItemStack itemStack = AutoSwapModule.internalField0149.player.getOffHandStack();
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 != null && itemStack.getItem() != Items.AIR) {
            String string = nestedValue2032.internalMethod00671(itemStack);
            RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.moved_to_offhand", string), itemStack).internalMethod03390(string).internalMethod05942(nestedValue2032.internalMethod01667(itemStack)));
        }
    }

    private void internalMethod06647(SlotCollection<InventorySlot> typedValue228) {
        ItemStack itemStack2;
        ItemStack itemStack3;
        List<ItemStack> list = typedValue228.internalMethod07613(Items.PLAYER_HEAD).stream().filter(typedValue222 -> typedValue222 != null && typedValue222.internalMethod03427() != null).map(InventorySlot::internalMethod03427).filter(itemStack -> {
            CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
            return nestedValue2032 != null && nestedValue2032.internalMethod08988();
        }).toList();
        ItemStack itemStack4 = AutoSwapModule.internalField0149.player.getOffHandStack();
        ItemStack itemStack5 = CustomItemRankingUtils.internalMethod08722(list);
        ItemStack itemStack6 = CustomItemRankingUtils.internalMethod09471(list);
        if (itemStack5 != null && itemStack6 != null) {
            itemStack3 = ItemStack.areEqual((ItemStack)itemStack4, (ItemStack)itemStack5) ? itemStack6 : itemStack5;
        } else {
            itemStack2 = CustomItemRankingUtils.internalMethod09915(list);
            if (itemStack6 != null && itemStack2 != null) {
                itemStack3 = ItemStack.areEqual((ItemStack)itemStack4, (ItemStack)itemStack6) ? itemStack2 : itemStack6;
            } else {
                return;
            }
        }
        if (ItemStack.areEqual((ItemStack)itemStack3, (ItemStack)itemStack4)) {
            return;
        }
        ItemStack targetHead = itemStack3;
        InventorySlot typedValue223 = typedValue228.internalMethod07613(Items.PLAYER_HEAD).stream().filter(typedValue222 -> ItemStack.areEqual(typedValue222.internalMethod03427(), targetHead)).findFirst().orElse(null);
        if (typedValue223 == null || typedValue223.internalMethod06662() == 45) {
            return;
        }
        InventoryUtils.internalMethod08821(typedValue223.internalMethod06662(), 40);
        this.internalField0519.internalMethod00701();
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(targetHead);
        if (nestedValue2032 != null) {
            String string = nestedValue2032.internalMethod00671(targetHead);
            RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.moved_to_offhand", string), targetHead).internalMethod03390(string).internalMethod05942(nestedValue2032.internalMethod01667(targetHead)));
        }
    }

    private void internalMethod09459() {
        if (AutoSwapModule.internalField0149.currentScreen != null) {
            return;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()).internalMethod07591(InventorySlots.internalMethod07766());
        List<@Nullable InventorySlot> list = typedValue228.internalMethod00168(this::internalMethod07427);
        InventorySlot typedValue223 = list.stream().filter(typedValue222 -> typedValue222.internalMethod06662() != 45).min(Comparator.comparingInt(this::internalMethod06889).thenComparing(typedValue222 -> CustomItemRankingUtils.internalMethod05103(typedValue222.internalMethod03427()))).orElse(null);
        if (typedValue223 == null) {
            return;
        }
        this.internalField0227 = typedValue223.internalMethod06662();
        ItemStack itemStack = typedValue223.internalMethod03427();
        InventoryUtils.internalMethod08821(typedValue223.internalMethod06662(), 40);
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 != null && itemStack.getItem() != Items.AIR) {
            String string = nestedValue2032.internalMethod00671(itemStack);
            RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.moved_to_offhand", string), itemStack).internalMethod03390(string).internalMethod05942(nestedValue2032.internalMethod01667(itemStack)));
        }
    }

    private boolean internalMethod09460() {
        return !RockstarClient.getInstance().getModuleManager().getModule(AutoTotemModule.class).internalMethod09477();
    }

    private void internalMethod09461() {
        float f = AutoSwapModule.internalField0149.player.getHealth() + AutoSwapModule.internalField0149.player.getAbsorptionAmount();
        if (this.internalField0206 >= 0.0f && AutoSwapModule.internalField0149.player.hurtTime > 0 && this.internalField0206 - f >= 2.0f) {
            this.internalField0518.internalMethod00701();
        }
        this.internalField0206 = f;
    }

    private boolean internalMethod00609(LivingEntity livingEntity) {
        PlayerEntity playerEntity;
        if (!this.internalField0277) {
            return false;
        }
        if (this.internalMethod09462() && !this.internalField0518.internalMethod02365(1000L)) {
            return false;
        }
        if (this.internalMethod09948()) {
            return true;
        }
        if (AutoSwapModule.internalField0149.player.hurtTime > 0 || livingEntity == null) {
            return false;
        }
        if (this.internalMethod09949() && !LegacyItemTypes.isSword(livingEntity.getMainHandStack())) {
            return true;
        }
        if (this.internalMethod09950() && GameUtils.internalMethod07128(livingEntity) && !AutoSwapModule.internalField0149.player.isTouchingWater()) {
            return true;
        }
        return this.internalMethod09951() && livingEntity instanceof PlayerEntity && rockstar.client.util.LegacyItemTypes.armorItem((playerEntity = (PlayerEntity)livingEntity).getInventory(), 2).getItem() == Items.ELYTRA;
    }

    private boolean internalMethod09462() {
        return this.internalField1272.internalMethod06103(this.internalField1066) && this.internalField1073.isSelected();
    }

    private boolean internalMethod09948() {
        return this.internalField1272.internalMethod06103(this.internalField1066) ? this.internalField0245.isSelected() : this.internalField1072.isSelected();
    }

    private boolean internalMethod09949() {
        return this.internalField1272.internalMethod06103(this.internalField1066) ? this.internalField0244.isSelected() : this.internalField1491.isSelected();
    }

    private boolean internalMethod09950() {
        return this.internalField1272.internalMethod06103(this.internalField1066) ? this.internalField1075.isSelected() : this.internalField1488.isSelected();
    }

    private boolean internalMethod09951() {
        return this.internalField1272.internalMethod06103(this.internalField1066) ? this.internalField1074.isSelected() : this.internalField1490.isSelected();
    }

    private int internalMethod01943(InventorySlot typedValue222) {
        return this.internalField0277 && this.internalMethod07427(typedValue222.internalMethod03427()) ? -99 : 0;
    }

    private boolean internalMethod07427(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 == null) {
            return false;
        }
        String string = nestedValue2032.internalMethod01319();
        if (string == null) {
            return false;
        }
        if (this.internalField1272.internalMethod06103(this.internalField1067)) {
            if (!nestedValue2032.internalMethod08992()) {
                return false;
            }
            return string.equals("\u042f\u0440\u043e\u0441\u0442\u0438") || string.equals("\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f");
        }
        if (!nestedValue2032.internalMethod08988()) {
            return false;
        }
        return string.equals("Cerber");
    }

    private int internalMethod06889(InventorySlot typedValue222) {
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(typedValue222.internalMethod03427());
        if (nestedValue2032 == null) {
            return 2;
        }
        String string = nestedValue2032.internalMethod01319();
        if (string == null) {
            return 2;
        }
        if (this.internalField1272.internalMethod06103(this.internalField1067)) {
            if (!nestedValue2032.internalMethod08992()) {
                return 2;
            }
            if (string.equals("\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f")) {
                return 0;
            }
            if (string.equals("\u042f\u0440\u043e\u0441\u0442\u0438")) {
                return 1;
            }
            return 2;
        }
        if (!nestedValue2032.internalMethod08988()) {
            return 2;
        }
        return string.equals("Cerber") ? 0 : 1;
    }

    @Generated
    public boolean internalMethod09277() {
        return this.internalField0277;
    }

    @Generated
    public boolean internalMethod09449() {
        return this.internalField0276;
    }

    @Generated
    public boolean internalMethod09452() {
        return this.internalField1099;
    }
}
