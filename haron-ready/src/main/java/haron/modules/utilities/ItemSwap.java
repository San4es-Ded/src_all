package haron.modules.utilities;

import haron.events.KeyInputEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.KeybindSetting;
import haron.settings.ModeSetting;
import java.util.Optional;
import java.util.function.Predicate;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleInfo(a="Item Swap", b="Автоматически меняет предмет в левой руке при нажатии клавиши", c=ModuleCategory.UTILITIES)
public class ItemSwap
extends HaronModule {
    private final ModeSetting e = new ModeSetting("Менять с", new String[]{"Сфера", "Тотем"}, "Сфера");
    private final ModeSetting f = new ModeSetting("Менять на", new String[]{"Сфера", "Тотем"}, "Тотем");
    private final KeybindSetting g = new KeybindSetting("Кнопка свапа", -1);
    private boolean h = false;
    private boolean i = false;
    private int j = 0;
    private int k = 0;
    private boolean l = false;
    private int m = 0;
    private int n = 0;
    private String o = "";
    private int p = -1;
    private int q = -1;
    public static int a;
    public static boolean b;

    @Override
    public void e() {
        super.e();
        this.h = false;
        this.i = false;
        this.j = 0;
        this.k = 0;
        this.l = false;
        this.m = 0;
        this.n = 0;
    }

    private Optional<Integer> b(Predicate<ItemStack> predicate) {
        for (int i = 9; i < 36; ++i) {
            if (!predicate.test(ItemSwap.c.player.getInventory().getStack(i))) continue;
            return Optional.of(i);
        }
        return Optional.empty();
    }

    private void b(int n) {
        this.p = ItemSwap.c.player.getInventory().selectedSlot;
        this.q = n;
        this.i = true;
        this.k = 0;
        this.n = 0;
    }

    private void c(int n) {
        ItemSwap.c.interactionManager.clickSlot(ItemSwap.c.player.currentScreenHandler.syncId, n, 40, SlotActionType.SWAP, (PlayerEntity)ItemSwap.c.player);
    }

    private void n() {
        if (this.n > 0) {
            --this.n;
        }
        switch (this.k) {
            case 0: {
                ItemSwap.c.player.getInventory().selectedSlot = this.q;
                this.n = 2;
                this.k = 1;
                break;
            }
            case 1: {
                ItemSwap.c.player.networkHandler.sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                this.n = 2;
                this.k = 2;
                break;
            }
            case 2: {
                ItemSwap.c.player.getInventory().selectedSlot = this.p;
                ItemSwap.c.player.sendMessage((Text)Text.literal((String)"§fСвапнул на ").append(ItemSwap.c.player.getOffHandStack().getName()), true);
                this.i = false;
                this.k = 0;
            }
        }
    }

    @Override
    public void f() {
        super.f();
        if (ItemSwap.c.currentScreen instanceof InventoryScreen) {
            ItemSwap.c.player.closeHandledScreen();
        }
        this.i = false;
        this.h = false;
    }

    @EventHandler
    public void a(KeyInputEvent dsgqgn2) {
        if (ItemSwap.c.player != null && ItemSwap.c.world != null && ItemSwap.c.currentScreen == null && dsgqgn2.a() == this.g.a() && dsgqgn2.c() == 1) {
            this.o = this.q();
            Optional<Integer> optional = this.a((ItemStack itemStack) -> {
                return this.a((ItemStack)itemStack, this.o);
            });
            if (optional.isPresent()) {
                this.b(optional.get());
                return;
            }
            if (!this.a(this.o)) {
                ItemSwap.c.player.sendMessage((Text)Text.literal((String)"§cНе найден предмет для свапа!"), true);
                return;
            }
            this.h = true;
            this.j = 0;
            this.l = false;
            this.m = 0;
        }
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (ItemSwap.c.player != null) {
            if (this.i) {
                this.n();
            }
            if (this.h) {
                this.o();
            }
        }
    }

    private boolean a(String string) {
        return "Тотем".equals(string) || "Сфера".equals(string);
    }

    private Optional<Integer> a(Predicate<ItemStack> predicate) {
        for (int i = 0; i < 9; ++i) {
            if (!predicate.test(ItemSwap.c.player.getInventory().getStack(i))) continue;
            return Optional.of(i);
        }
        return Optional.empty();
    }

    private boolean a(ItemStack itemStack, String string) {
        if (itemStack.isEmpty()) {
            return false;
        }
        if ("Сфера".equals(string)) {
            return itemStack.getItem() == Items.PLAYER_HEAD;
        }
        if ("Тотем".equals(string)) {
            return itemStack.getItem() == Items.TOTEM_OF_UNDYING;
        }
        return false;
    }

    private void o() {
        if (this.m > 0) {
            --this.m;
            return;
        }
        switch (this.j) {
            case 0: {
                if (ItemSwap.c.player.getAbilities().creativeMode) {
                    this.j = 1;
                    break;
                }
                if (!this.b(itemStack -> {
                    return this.a((ItemStack)itemStack, this.o);
                }).isPresent()) {
                    ItemSwap.c.player.sendMessage((Text)Text.literal((String)"§cНе найден предмет для свапа!"), true);
                    this.h = false;
                    break;
                }
                if (!(ItemSwap.c.currentScreen instanceof InventoryScreen)) {
                    c.setScreen((Screen)new InventoryScreen((PlayerEntity)ItemSwap.c.player));
                }
                this.m = 2;
                this.j = 1;
                break;
            }
            case 1: {
                if (!(ItemSwap.c.currentScreen instanceof InventoryScreen) || this.l) break;
                this.p();
                this.l = true;
                this.m = 2;
                this.j = 2;
                break;
            }
            case 2: {
                if (ItemSwap.c.currentScreen instanceof InventoryScreen) {
                    c.setScreen((Screen)null);
                }
                this.h = false;
                this.j = 0;
            }
        }
    }

    private void p() {
        Optional<Integer> optional = this.b(itemStack -> {
            return this.a((ItemStack)itemStack, this.o);
        });
        if (optional.isPresent()) {
            this.c(optional.get());
            ItemSwap.c.player.sendMessage((Text)Text.literal((String)"§fСвапнул на ").append(ItemSwap.c.player.getOffHandStack().getName()), true);
        }
    }

    private String q() {
        String string;
        ItemStack itemStack = ItemSwap.c.player.getOffHandStack();
        return !this.a(itemStack, string = this.e.d()) ? string : this.f.d();
    }
}

