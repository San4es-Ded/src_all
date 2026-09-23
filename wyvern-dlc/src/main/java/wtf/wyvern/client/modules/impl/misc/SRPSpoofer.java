package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "SRPSpoofer",
        category = Category.MISC,
        description = "Подменяет данные о том, что установлен серверный ресурс-пак"
)
@FastNative
public class SRPSpoofer extends Module {
    public static final SRPSpoofer INSTANCE = new SRPSpoofer();

    @EventTarget
    public void onPacketReceive(EventPacket e) {
        if (e.getPacket() instanceof ResourcePackSendS2CPacket send && e.isReceive()) {
            java.util.UUID id = send.id();
            mc.getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(id, ResourcePackStatusC2SPacket.Status.ACCEPTED));
            mc.getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(id, ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
            e.cancel();
        }
    }
}
