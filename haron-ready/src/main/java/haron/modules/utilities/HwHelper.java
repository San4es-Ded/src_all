package haron.modules.utilities;

import haron.events.WorldRenderPostEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

@ModuleInfo(a="HW Helper", b="Shows helper timers for HolyWorld utility items.", c=ModuleCategory.UTILITIES)
public class HwHelper
extends HaronModule {
    private static final long MESSAGE_DURATION_MS = 4000L;
    private boolean stunTrapCooldownActive;
    private long stunTrapStartMs;
    private boolean explosiveTrapCooldownActive;
    private long explosiveTrapStartMs;
    private final BooleanSetting trackItems = new BooleanSetting("Отслеживать предметы", true);
    private final BooleanSetting showStunTrap = new BooleanSetting("Оглушающая ловушка", true);
    private final BooleanSetting showExplosiveTrap = new BooleanSetting("Взрывная ловушка", true);
    private final SettingGroup renderGroup = new SettingGroup("Рендер");
    private final BooleanSetting renderNearbyTrapBox = new BooleanSetting("Рендер ближних ловушек", true);
    private int lastStunTrapCount = 0;
    private int lastExplosiveTrapCount = 0;

    private int countItem(Item item) {
        int n = 0;
        for (int i = 0; i < HwHelper.c.player.getInventory().size(); ++i) {
            ItemStack itemStack = HwHelper.c.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            n += itemStack.getCount();
        }
        return n;
    }

    private void updateTrackedItem(Item item, boolean bl, boolean bl2) {
        int n;
        int n2 = this.countItem(item);
        boolean bl3 = HwHelper.c.player.getItemCooldownManager().isCoolingDown(item.getDefaultStack());
        int n3 = n = bl2 ? this.lastStunTrapCount : this.lastExplosiveTrapCount;
        if (bl && bl3 && n2 < n) {
            if (bl2) {
                this.stunTrapCooldownActive = true;
                this.stunTrapStartMs = System.currentTimeMillis();
            } else {
                this.explosiveTrapCooldownActive = true;
                this.explosiveTrapStartMs = System.currentTimeMillis();
            }
        }
        if (bl2) {
            this.lastStunTrapCount = n2;
        } else {
            this.lastExplosiveTrapCount = n2;
        }
    }

    private void showOverlayMessage() {
        String string;
        String string2;
        long l = System.currentTimeMillis();
        String string3 = this.getCooldownMessage("Stun Trap", l, this.stunTrapStartMs, this.stunTrapCooldownActive);
        if (string3 == null) {
            this.stunTrapCooldownActive = false;
        }
        if ((string2 = this.getCooldownMessage("Explosive Trap", l, this.explosiveTrapStartMs, this.explosiveTrapCooldownActive)) == null) {
            this.explosiveTrapCooldownActive = false;
        }
        if ((string = string3 == null || string2 == null ? (string3 != null ? string3 : string2) : HwHelper.$sf$0(string3, string2)) != null) {
            HwHelper.c.inGameHud.setOverlayMessage(Text.of((String)string), false);
        }
    }

    private String getCooldownMessage(String string, long l, long l2, boolean bl) {
        if (!bl) {
            return null;
        }
        long l3 = l - l2;
        if (l3 >= 4000L) {
            return null;
        }
        return String.format("%ModuleCard cooldown: %.1fs", string, (double)(4000L - l3) / 1000.0);
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (HwHelper.c.world == null || HwHelper.c.player == null || !((Boolean)this.trackItems.k()).booleanValue()) {
            return;
        }
        this.updateTrackedItem(Items.PRISMARINE_SHARD, (Boolean)this.showStunTrap.k(), true);
        this.updateTrackedItem(Items.POPPED_CHORUS_FRUIT, (Boolean)this.showExplosiveTrap.k(), false);
        this.showOverlayMessage();
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return string + " | " + string2;
    }
}

