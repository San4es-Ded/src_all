package haron.modules.utilities;

import haron.core.BooleanCoercion;
import haron.events.KeyInputEvent;
import haron.events.ClientTickEvent;
import haron.inventory.rk1ts2;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.KeybindSetting;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

@ModuleInfo(a="Elytra Swap", b="Автоматически меняет нагрудник на элитры при падении", c=ModuleCategory.UTILITIES)
public class ElytraSwap
extends HaronModule {
    private final KeybindSetting e = new KeybindSetting("Кнопка свапа", -1);
    private boolean f = false;
    private int g = 0;
    private boolean h = false;
    private int i = 0;
    public static int a;
    public static boolean b;

    @Override
    public void e() {
        super.e();
        this.f = false;
        this.g = 0;
        this.h = false;
        this.i = 0;
    }

    private void b(int n) {
        ElytraSwap.c.interactionManager.clickSlot(ElytraSwap.c.player.currentScreenHandler.syncId, n, 40, SlotActionType.SWAP, (PlayerEntity)ElytraSwap.c.player);
        ElytraSwap.c.interactionManager.clickSlot(ElytraSwap.c.player.currentScreenHandler.syncId, 6, 40, SlotActionType.SWAP, (PlayerEntity)ElytraSwap.c.player);
        ElytraSwap.c.interactionManager.clickSlot(ElytraSwap.c.player.currentScreenHandler.syncId, n, 40, SlotActionType.SWAP, (PlayerEntity)ElytraSwap.c.player);
    }

    private void n() {
        if (this.q()) {
            Optional<Integer> optional = this.p();
            if (optional.isPresent()) {
                this.b(optional.get());
                ElytraSwap.c.player.sendMessage(Text.of((String)ElytraSwap.$sf$2(this.g())), true);
                return;
            }
            return;
        }
        Optional<Integer> optional = this.o();
        if (optional.isPresent()) {
            this.b(optional.get());
            ElytraSwap.c.player.sendMessage(Text.of((String)ElytraSwap.$sf$3(this.g())), true);
        }
    }

    @Override
    public void f() {
        super.f();
    }

    @EventHandler
    public void a(KeyInputEvent dsgqgn2) {
        if (ElytraSwap.c.player == null || ElytraSwap.c.world == null || ElytraSwap.c.currentScreen != null || dsgqgn2.a() != ((Integer)this.e.k()).intValue() || dsgqgn2.c() != 1 || this.f) {
            return;
        }
        this.f = true;
        this.g = 0;
        this.h = false;
        this.i = 0;
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (ElytraSwap.c.player == null || !this.f) {
            return;
        }
        if (this.i > 0) {
            int n = this.i;
            this.i = (n ^ 1) - 2 * (~n & 1);
        }
        switch (this.g) {
            case 0: {
                if (!ElytraSwap.c.player.getAbilities().creativeMode) {
                    if (!this.q()) {
                        if (!this.o().isPresent()) {
                            ElytraSwap.c.player.sendMessage(Text.of((String)ElytraSwap.$sf$0(this.g())), true);
                            this.f = false;
                        }
                    } else if (!this.p().isPresent()) {
                        ElytraSwap.c.player.sendMessage(Text.of((String)ElytraSwap.$sf$1(this.g())), true);
                        this.f = false;
                    }
                    if (!(ElytraSwap.c.currentScreen instanceof InventoryScreen)) {
                        c.setScreen((Screen)new InventoryScreen((PlayerEntity)ElytraSwap.c.player));
                    }
                    this.i = 2;
                    this.g = 1;
                    break;
                }
                this.g = 1;
                break;
            }
            case 1: {
                if (!(ElytraSwap.c.currentScreen instanceof InventoryScreen) || this.h) break;
                this.n();
                this.h = true;
                this.i = 2;
                this.g = 2;
                break;
            }
            case 2: {
                if (ElytraSwap.c.currentScreen instanceof InventoryScreen) {
                    ElytraSwap.c.player.closeHandledScreen();
                }
                this.f = false;
                this.g = 0;
            }
        }
    }

    private boolean a(ItemStack itemStack) {
        if (itemStack.getItem() == Items.ELYTRA) {
            return false;
        }
        EquippableComponent equippableComponent = (EquippableComponent)itemStack.get(DataComponentTypes.EQUIPPABLE);
        return BooleanCoercion.from(equippableComponent == null || equippableComponent.slot() != EquipmentSlot.CHEST ? 0 : 1);
    }

    private Optional<Integer> o() {
        return rk1ts2.a(itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.ELYTRA ? 0 : 1);
        }, true, false);
    }

    private Optional<Integer> p() {
        return rk1ts2.a(this::a, true, false);
    }

    private boolean q() {
        return BooleanCoercion.from(ElytraSwap.c.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA ? 0 : 1);
    }

    private static /* synthetic */ String $sf$0(String string) {
        return string + ": Свап отменен: нет элитр.";
    }

    private static /* synthetic */ String $sf$3(String string) {
        return string + ": Надел элитры.";
    }

    private static /* synthetic */ String $sf$1(String string) {
        return string + ": Свап отменен: нет нагрудника.";
    }

    private static /* synthetic */ String $sf$2(String string) {
        return string + ": Снял элитры.";
    }
}

