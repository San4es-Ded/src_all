package haron.modules.utilities;

import haron.events.PacketEvent;
import haron.events.WorldChangedEvent;
import haron.events.ClientTickEvent;
import haron.events.MouseButtonEvent;
import haron.media.chat.w53bpe;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.TextTokenType;
import haron.settings.ValidatedTextSetting;
import haron.util.jeooat;
import java.util.List;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import ru.haron.mixin.accessor.PlayerListHudAccessor;

@ModuleInfo(a="RW Joiner", b="Автоматически подключается к выбранному грифу ReallyWorld", c=ModuleCategory.UTILITIES)
public class RwJoiner
extends HaronModule {
    private static final String f = "ГРИФЕРСКОЕ ВЫЖИВАНИЕ (1.16.5-1.20.4)";
    private static final int g = 5000;
    private List<ItemStack> n;
    private static final long q = 100L;
    private static final long r = 1000L;
    private static final long v = 3000L;
    public static int a;
    public static boolean b;
    private final ValidatedTextSetting e = new ValidatedTextSetting("Номер грифа", TextTokenType.NUMBER, "1", "Введите номер");
    private final jeooat h = new jeooat();
    private int i = -1;
    private String j = "";
    private boolean k = false;
    private boolean l = false;
    private boolean m = false;
    private int o = 0;
    private long p = 0L;
    private boolean s = false;
    private long t = 0L;
    private long u = 0L;

    @Override
    public void e() {
        super.e();
        if (RwJoiner.c.player == null || RwJoiner.c.world == null) {
            this.d();
            return;
        }
        if (!this.q()) {
            this.d();
            return;
        }
        this.l = false;
        this.k = false;
        this.o = 0;
        this.s = false;
        this.p = 0L;
        this.t = 0L;
        this.u = 0L;
        this.h.b();
    }

    private int b(String string) {
        if (this.n == null) {
            return -1;
        }
        for (int i = 0; i < this.n.size(); ++i) {
            String string2;
            ItemStack itemStack = this.n.get(i);
            if (itemStack.isEmpty() || (string2 = Formatting.strip((String)itemStack.getName().getString())) == null || !string2.contains(string)) continue;
            return i;
        }
        return -1;
    }

    private void n() {
        if (System.currentTimeMillis() - this.p >= 100L && (this.t <= 0L || System.currentTimeMillis() - this.t >= 1000L)) {
            if (this.u > 0L && System.currentTimeMillis() - this.u < 3000L) {
                return;
            }
            Optional<Integer> optional = this.a(Items.COMPASS);
            if (optional.isPresent()) {
                int n = optional.get();
                if (RwJoiner.c.player.getInventory().selectedSlot != n) {
                    RwJoiner.c.player.getInventory().selectedSlot = n;
                    return;
                }
                RwJoiner.c.interactionManager.interactItem((PlayerEntity)RwJoiner.c.player, Hand.MAIN_HAND);
                this.p = System.currentTimeMillis();
                this.t = System.currentTimeMillis();
                this.s = true;
            }
        }
    }

    @Override
    public void f() {
        super.f();
        this.p();
        this.l = false;
        this.o = 0;
        this.s = false;
        this.u = 0L;
    }

    private Optional<Integer> a(Item item) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = RwJoiner.c.player.getInventory().getStack(i);
            if (itemStack.isEmpty() || itemStack.getItem() != item) continue;
            return Optional.of(i);
        }
        return Optional.empty();
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (RwJoiner.c.player == null || RwJoiner.c.world == null || !this.q()) {
            return;
        }
        if (this.l) {
            if (this.h.a() >= 5000L) {
                this.l = false;
                this.p();
                this.t = 0L;
                this.n();
                return;
            }
            return;
        }
        if (this.k || this.s) {
            return;
        }
        if (this.t > 0L && System.currentTimeMillis() - this.t > 1000L) {
            this.t = 0L;
        }
        this.n();
    }

    private void a(String string) {
        w53bpe.a((Object)RwJoiner.$sf$1(this.g(), string));
    }

    @EventHandler
    public void a(PacketEvent g07m232) {
        String string;
        InventoryS2CPacket inventoryS2CPacket;
        if (g07m232.d() instanceof OpenScreenS2CPacket) {
            OpenScreenS2CPacket openScreenS2CPacket = (OpenScreenS2CPacket)g07m232.d();
            String string2 = Formatting.strip((String)openScreenS2CPacket.getName().getString());
            if (string2.contains("Выбор сервера") || string2.contains("Выбор мира грифа")) {
                this.i = openScreenS2CPacket.getSyncId();
                this.j = string2;
                this.k = true;
                this.s = false;
                g07m232.b();
                return;
            }
            return;
        }
        if (g07m232.d() instanceof InventoryS2CPacket && ((inventoryS2CPacket = (InventoryS2CPacket)g07m232.d()).getSyncId() == this.i || this.i != -1 && this.k)) {
            this.n = inventoryS2CPacket.getContents();
            this.o();
            g07m232.b();
            return;
        }
        if (g07m232.d() instanceof CloseScreenS2CPacket && ((CloseScreenS2CPacket)g07m232.d()).getSyncId() == this.i) {
            this.p();
            g07m232.b();
            return;
        }
        if (!(g07m232.d() instanceof GameMessageS2CPacket) || (string = Formatting.strip((String)((GameMessageS2CPacket)g07m232.d()).content().getString())) == null) {
            return;
        }
        if (string.contains("Не удалось подключиться к серверу") || string.contains("Подождите несколько секунд перед повторым подключением") || string.contains("сервер переполнен") || string.contains("Unable to connect")) {
            if (!string.contains("Unable to connect")) {
                g07m232.b();
            } else if (b) {
                // empty if block
            }
            this.l = true;
            this.h.b();
            this.p();
        }
    }

    @EventHandler
    public void a(WorldChangedEvent m7z9q12) {
        if (this.l() && !this.l && this.e.d()) {
            try {
                this.a(RwJoiner.$sf$2(Integer.parseInt((String)this.e.k())));
                this.d();
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    @EventHandler
    public void a(MouseButtonEvent tp78k22) {
    }

    private void o() {
        if (this.n == null || this.n.isEmpty() || System.currentTimeMillis() - this.p < 100L) {
            return;
        }
        if (this.j.contains("Выбор сервера") && !this.m) {
            int n = this.b("ГРИФЕРСКОЕ ВЫЖИВАНИЕ (1.16.5-1.20.4)");
            if (n == -1) {
                this.p();
                this.d();
                return;
            }
            RwJoiner.c.interactionManager.clickSlot(this.i, n, 0, SlotActionType.PICKUP, (PlayerEntity)RwJoiner.c.player);
            this.m = true;
            this.p = System.currentTimeMillis();
            return;
        }
        if (this.j.contains("Выбор мира грифа") && this.m && this.e.d()) {
            try {
                int n = this.b(RwJoiner.$sf$0(Integer.parseInt((String)this.e.k())));
                if (n == -1) {
                    this.p();
                    return;
                }
                RwJoiner.c.interactionManager.clickSlot(this.i, n, 0, SlotActionType.PICKUP, (PlayerEntity)RwJoiner.c.player);
                int n2 = this.o;
                this.o = (n2 | 1) + (n2 & 1);
                this.p = System.currentTimeMillis();
                this.u = System.currentTimeMillis();
                this.p();
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    private void p() {
        this.i = -1;
        this.j = "";
        this.k = false;
        this.n = null;
        this.m = false;
        this.s = false;
        this.t = 0L;
    }

    private boolean q() {
        if (RwJoiner.c.inGameHud == null || RwJoiner.c.inGameHud.getPlayerListHud() == null) {
            return false;
        }
        try {
            Text text = ((PlayerListHudAccessor)RwJoiner.c.inGameHud.getPlayerListHud()).getHeader();
            if (text == null) {
                return false;
            }
            String string = Formatting.strip((String)text.getString());
            return string != null && string.contains("Вы находитесь в: Lobby");
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static /* synthetic */ String $sf$0(int n) {
        int n2 = 852;
        return "ГРИФ #" + n;
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + ": " + string2;
    }

    private static /* synthetic */ String $sf$2(int n) {
        return "Успешно подключено к Гриф #" + n;
    }
}

