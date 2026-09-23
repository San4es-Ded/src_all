package haron.modules.utilities;

import haron.events.PacketDirection;
import haron.events.PacketEvent;
import haron.media.chat.w53bpe;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.BooleanSetting;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@ModuleInfo(a="Item Pickup Logger", b="Логирует поднятые предметы в чат", c=ModuleCategory.UTILITIES)
public class ItemPickupLogger
extends HaronModule {
    private final BooleanSetting e = new BooleanSetting("Только донат предметы", false);
    public static int a;
    public static boolean b;

    public BooleanSetting n() {
        return this.e;
    }

    private boolean a(ItemStack itemStack) {
        Text text = itemStack.getName();
        if (text.getString().contains("★")) {
            return true;
        }
        if (text instanceof MutableText) {
            MutableText mutableText = (MutableText)text;
            if (mutableText.getStyle().isObfuscated()) {
                return true;
            }
            Iterator iterator = mutableText.getSiblings().iterator();
            while (iterator.hasNext()) {
                if (!((Text)iterator.next()).getStyle().isObfuscated()) continue;
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void a(PacketEvent g07m232) {
        if (g07m232.e() != PacketDirection.RECIEVE || ItemPickupLogger.c.player == null || ItemPickupLogger.c.world == null || !(g07m232.d() instanceof ItemPickupAnimationS2CPacket)) {
            return;
        }
        ItemPickupAnimationS2CPacket itemPickupAnimationS2CPacket = (ItemPickupAnimationS2CPacket)g07m232.d();
        if (itemPickupAnimationS2CPacket.getCollectorEntityId() != ItemPickupLogger.c.player.getId()) {
            return;
        }
        Entity entity = ItemPickupLogger.c.world.getEntityById(itemPickupAnimationS2CPacket.getEntityId());
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)entity;
            ItemStack itemStack = itemEntity.getStack();
            if (itemStack.isEmpty()) {
                return;
            }
            boolean bl = this.a(itemStack);
            if (!((Boolean)this.e.k()).booleanValue() || bl) {
                MutableText mutableText;
                Text text = itemStack.getName();
                int n = itemStack.getCount();
                MutableText mutableText2 = mutableText = !bl ? Text.literal((String)"§7Поднят: ").append(text) : Text.literal((String)"§7Поднят донат предмет: ").append(text);
                if (n > 1) {
                    mutableText.append((Text)Text.literal((String)ItemPickupLogger.$sf$0(n)));
                }
                w53bpe.b((Text)mutableText);
            }
        }
    }

    private static /* synthetic */ String $sf$0(int n) {
        return " §7x" + n;
    }
}

