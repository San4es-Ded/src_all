package haron.modules.utilities;

import haron.events.TotemPopEvent;
import haron.events.AttackTargetEvent;
import haron.media.chat.w53bpe;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.util.jeooat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@ModuleInfo(a="Totem Tracker", b="Tracks totem pops from recently attacked entities.", c=ModuleCategory.UTILITIES)
public class TotemTracker
extends HaronModule {
    private static final long TARGET_MEMORY_MS = 5000L;
    private final Map<UUID, jeooat> attackedTargets = new HashMap<UUID, jeooat>();
    private int lastNotificationAge = -1;

    private void removeExpiredTargets() {
        this.attackedTargets.entrySet().removeIf(entry -> {
            return ((jeooat)entry.getValue()).hasElapsed(5000L);
        });
    }

    private void notifyTotemPop(LivingEntity livingEntity, ItemStack itemStack) {
        int n = TotemTracker.c.player.age;
        if (n == this.lastNotificationAge) {
            return;
        }
        this.lastNotificationAge = n;
        w53bpe.a((Object)TotemTracker.$sf$0(livingEntity.getName().getString(), EnchantmentHelper.getEnchantments((ItemStack)itemStack).isEmpty() ? "regular" : "enchanted"));
    }

    @Override
    public void f() {
        super.f();
        this.attackedTargets.clear();
        this.lastNotificationAge = -1;
    }

    @EventHandler
    public void a(TotemPopEvent bjffkp2) {
        if (TotemTracker.c.player == null) {
            return;
        }
        LivingEntity livingEntity = bjffkp2.a();
        if (livingEntity == TotemTracker.c.player) {
            return;
        }
        jeooat jeooat2 = this.attackedTargets.get(livingEntity.getUuid());
        if (jeooat2 != null && !jeooat2.hasElapsed(5000L)) {
            this.notifyTotemPop(livingEntity, bjffkp2.b());
        }
        this.removeExpiredTargets();
    }

    @EventHandler
    public void a(AttackTargetEvent dt813s2) {
        if (TotemTracker.c.player != null) {
            Entity entity = dt813s2.a();
            if (!(entity instanceof LivingEntity)) {
                return;
            }
            LivingEntity livingEntity = (LivingEntity)entity;
            if (livingEntity == TotemTracker.c.player) {
                return;
            }
            this.attackedTargets.put(livingEntity.getUuid(), new jeooat());
        }
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return "Totem KillTracker: " + string + " popped a " + string2 + " totem.";
    }
}

