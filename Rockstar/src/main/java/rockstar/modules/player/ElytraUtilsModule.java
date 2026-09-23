package rockstar.modules.player;












import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import java.util.function.Predicate;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.slot.SlotActionType;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.CustomItemUtils;
import rockstar.client.internal.script.ElytraUtils;
import rockstar.client.internal.script.ItemNbtUtils;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.util.EnchantmentUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.ArmorSlot;
import rockstar.client.module.Module;
import rockstar.client.notification.ItemNotification;

@ModuleInfo(name="Elytra Utils", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.elytra_utils")
public class ElytraUtilsModule
extends Module {
    private KeybindSetting internalField0648;
    private KeybindSetting internalField0647;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private boolean internalField0277;
    private InternalType0459 internalField0107;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        boolean bl;
        if (ElytraUtilsModule.internalField0149.player.isGliding()) {
            this.internalField0277 = true;
        }
        GuiMoveModule typedValue273 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
        ArmorSlot typedValue230 = InventoryUtils.internalMethod06826();
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod07766());
        InventorySlot typedValue222 = this.internalMethod04642(typedValue228);
        InventorySlot typedValue223 = typedValue228.internalMethod02510(Items.FIREWORK_ROCKET);
        boolean bl2 = bl = typedValue230.internalMethod00210() == Items.ELYTRA;
        if (this.internalField0107 != null) {
            if (this.internalField0107.internalField0022.internalMethod06662() >= 36 && this.internalField0107.internalField0022.internalMethod06662() <= 44) {
                InventoryUtils.internalMethod08821(this.internalField0107.internalField0023.internalMethod06662(), this.internalField0107.internalField0022.internalMethod06662() - 36);
                this.internalField0107 = null;
            } else if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
                if (this.internalField0107.internalField0227 == 0 && typedValue273.internalMethod06994().isEmpty()) {
                    InventoryUtils.internalMethod08821(this.internalField0107.internalField0022.internalMethod06662(), 8);
                    InventoryUtils.internalMethod08821(this.internalField0107.internalField0023.internalMethod06662(), 8);
                    InventoryUtils.internalMethod08821(this.internalField0107.internalField0022.internalMethod06662(), 8);
                    ++this.internalField0107.internalField0227;
                } else if (this.internalField0107.internalField0227 == 1 && typedValue273.internalMethod06994().isEmpty()) {
                    ++this.internalField0107.internalField0227;
                } else if (this.internalField0107.internalField0227 == 2 && typedValue273.internalMethod06994().isEmpty()) {
                    ++this.internalField0107.internalField0227;
                }
            } else if (this.internalField0107.internalField0227 == 0 && typedValue273.internalMethod06994().isEmpty()) {
                InventoryUtils.internalMethod08821(this.internalField0107.internalField0022.internalMethod06662(), 8);
                ++this.internalField0107.internalField0227;
            } else if (this.internalField0107.internalField0227 == 1 && typedValue273.internalMethod06994().isEmpty()) {
                InventoryUtils.internalMethod08821(this.internalField0107.internalField0023.internalMethod06662(), 8);
                ++this.internalField0107.internalField0227;
            } else if (this.internalField0107.internalField0227 == 2 && typedValue273.internalMethod06994().isEmpty()) {
                InventoryUtils.internalMethod08821(this.internalField0107.internalField0022.internalMethod06662(), 8);
                ++this.internalField0107.internalField0227;
            }
            if (this.internalField0107 != null && this.internalField0107.internalField0227 >= 3) {
                this.internalField0107 = null;
            }
        }
        if (bl) {
            if (this.internalField0650.internalMethod04496() && ElytraUtilsModule.internalField0149.player.isOnGround() && !ElytraUtilsModule.internalField0149.options.jumpKey.isPressed()) {
                ElytraUtilsModule.internalField0149.player.jump();
            }
            if (this.internalField0650.internalMethod04496() && !ElytraUtilsModule.internalField0149.player.isInFluid() && ElytraUtilsModule.internalField0149.player.isSprinting() && ElytraUtilsModule.internalField0149.player.input.hasForwardMovement() && ElytraUtilsModule.internalField0149.player.checkGliding()) {
                internalField0149.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)ElytraUtilsModule.internalField0149.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                if (this.internalField0651.internalMethod04496() && typedValue223 != null) {
                    ItemCooldownHolder.internalField0006.internalMethod07082(Items.FIREWORK_ROCKET);
                }
            }
        }
        if (this.internalField1261.internalMethod04496() && ElytraUtilsModule.internalField0149.player.isOnGround() && bl && this.internalField0277 && ElytraUtilsModule.internalField0149.player.getGlidingTicks() > 18) {
            if (typedValue222 != null) {
                this.internalMethod09321();
            } else {
                ElytraUtilsModule.internalField0149.interactionManager.clickSlot(0, 6, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)ElytraUtilsModule.internalField0149.player);
            }
            this.internalField0277 = false;
        }
    };
    private final EventListener<KeyPressEvent> internalField0158 = keyPressEvent -> {
        if (this.internalField0648.internalMethod02165(keyPressEvent.getKey()) && keyPressEvent.getAction() == 1 && ElytraUtilsModule.internalField0149.currentScreen == null) {
            this.internalMethod09321();
        }
        if (this.internalField0647.internalMethod02165(keyPressEvent.getKey()) && keyPressEvent.getAction() == 1 && ElytraUtilsModule.internalField0149.currentScreen == null && ElytraUtilsModule.internalField0149.player.isGliding()) {
            if (this.internalField0668.internalMethod06103(this.internalField0237)) {
                ItemCooldownHolder.internalField0006.internalMethod07082(Items.FIREWORK_ROCKET);
            } else {
                ElytraUtils.internalMethod07048(this.internalMethod05480(Items.FIREWORK_ROCKET, itemStack -> true));
            }
        }
    };
    private final EventListener<MouseEvent> internalField1028 = mouseEvent -> {
        if (this.internalField0648.internalMethod02165(mouseEvent.getButton()) && mouseEvent.getAction() == 1 && ElytraUtilsModule.internalField0149.currentScreen == null) {
            this.internalMethod09321();
        }
        if (this.internalField0647.internalMethod02165(mouseEvent.getButton()) && mouseEvent.getAction() == 1 && ElytraUtilsModule.internalField0149.currentScreen == null && ElytraUtilsModule.internalField0149.player.isGliding()) {
            if (this.internalField0668.internalMethod06103(this.internalField0237)) {
                ItemCooldownHolder.internalField0006.internalMethod07082(Items.FIREWORK_ROCKET);
            } else {
                ElytraUtils.internalMethod07048(this.internalMethod05480(Items.FIREWORK_ROCKET, itemStack -> true));
            }
        }
    };

    public ElytraUtilsModule() {
        this.internalMethod09320();
    }

    private void internalMethod09320() {
        this.internalField0648 = new KeybindSetting(this, "modules.settings.elytra_utils.swapKey");
        this.internalField0647 = new KeybindSetting(this, "modules.settings.elytra_utils.fireworkKey");
        this.internalField0668 = new ModeSetting((SettingOwner)this, "modules.settings.firework_swap_mode", () -> this.internalField0647.internalMethod07477() == -1);
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_utils.firework_swap_mode.default");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_utils.firework_swap_mode.packet");
        this.internalField0650 = new BooleanSetting(this, "modules.settings.elytra_utils.auto_takeoff");
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.elytra_utils.auto_use", () -> !this.internalField0650.internalMethod04496()).internalMethod06630();
        this.internalField1261 = new BooleanSetting(this, "modules.settings.elytra_utils.chest_on_ground");
    }

    private InventorySlot internalMethod04642(SlotCollection<InventorySlot> typedValue228) {
        InventorySlot typedValue222 = null;
        int n = Integer.MIN_VALUE;
        for (InventorySlot typedValue223 : typedValue228.internalMethod02638()) {
            int n2;
            ItemStack itemStack = typedValue223.internalMethod03427();
            if (LegacyItemTypes.getArmorSlot(itemStack) != net.minecraft.entity.EquipmentSlot.CHEST || (n2 = this.internalMethod05815(itemStack)) <= n) continue;
            n = n2;
            typedValue222 = typedValue223;
        }
        return typedValue222;
    }

    private int internalMethod05815(ItemStack itemStack) {
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 != null && "SunHelmet".equals(nestedValue2032.internalMethod01319())) {
            return Integer.MAX_VALUE;
        }
        int n = LegacyItemTypes.getDefense(itemStack);
        int n2 = LegacyItemTypes.getToughness(itemStack);
        int n3 = EnchantmentUtils.internalMethod03526(itemStack, (RegistryKey<Enchantment>)Enchantments.PROTECTION);
        return n * 5 + n3 * 3 + n2;
    }

    private void internalMethod09321() {
        boolean bl;
        ArmorSlot typedValue230 = InventoryUtils.internalMethod06826();
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
        InventorySlot typedValue222 = typedValue228.internalMethod03297(itemStack -> itemStack.getItem() == Items.ELYTRA && !itemStack.willBreakNextUse());
        InventorySlot typedValue223 = this.internalMethod04642(typedValue228);
        boolean bl2 = bl = typedValue230.internalMethod00210() == Items.ELYTRA;
        if (this.internalField0107 != null) {
            return;
        }
        if (!bl && typedValue222 != null) {
            this.internalField0107 = new InternalType0459(typedValue222, typedValue230);
            String string = ItemNbtUtils.internalMethod02181(typedValue222.internalMethod03427());
            RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.equipped", string), typedValue222.internalMethod03427()).internalMethod03390(string));
        } else if (typedValue223 != null) {
            this.internalField0107 = new InternalType0459(typedValue223, typedValue230);
            String string = ItemNbtUtils.internalMethod02181(typedValue223.internalMethod03427());
            RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.equipped", string), typedValue223.internalMethod03427()).internalMethod03390(string));
        }
    }

    public float internalMethod05480(Item item, Predicate<ItemStack> predicate) {
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod07766());
        Predicate<ItemStack> predicate2 = itemStack -> itemStack != null && !itemStack.isEmpty() && itemStack.getItem() == item && predicate.test(itemStack);
        InventorySlot typedValue222 = typedValue228.internalMethod03297(stack -> this.internalMethod03604(predicate2, item, stack));
        if (typedValue222 == null) {
            typedValue222 = typedValue228.internalMethod03297(predicate2);
        }
        if (typedValue222 == null) {
            return InventoryUtils.internalMethod06160().internalMethod08745() + 1;
        }
        int n = typedValue222.internalMethod06662();
        if (n >= 36 && n <= 44) {
            return n - 35;
        }
        return InventoryUtils.internalMethod06160().internalMethod08745() + 1;
    }

    private boolean internalMethod02470(ItemStack itemStack, Item item) {
        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }
        if (itemStack.getItem() != item) {
            return false;
        }
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        return nestedValue2032 != null && nestedValue2032.internalMethod08994();
    }

    @Override
    public void onDisable() {
        this.internalField0277 = false;
    }

    @Override
    public void onEnable() {
        this.internalField0277 = false;
    }

    private boolean internalMethod03604(Predicate predicate, Item item, ItemStack itemStack) {
        return predicate.test(itemStack) && this.internalMethod02470(itemStack, item);
    }

    static class InternalType0459 {
        int internalField0227;
        final InventorySlot internalField0022;
        final InventorySlot internalField0023;

        InternalType0459(InventorySlot typedValue222, InventorySlot typedValue223) {
            this.internalField0022 = typedValue222;
            this.internalField0023 = typedValue223;
        }
    }
}
