package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.PacketEvent;
import aethereal.util.NetworkUtil;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2535.class})
public class ClientConnectionMixin {
   @Inject(
      method = {"method_10743*", "method_10752*", "method_52906*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void send(class_2596<?> packet, CallbackInfo ci) {
      if (!NetworkUtil.b(packet)) {
         PacketEvent event = new PacketEvent(packet, PacketEvent.Type.SEND);
         EventManager.a((IEvent)event);
         if (event.a()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"channelRead0*", "method_10770*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void channelRead0(ChannelHandlerContext context, class_2596<?> packet, CallbackInfo ci) {
      PacketEvent event = new PacketEvent(packet, PacketEvent.Type.RECEIVE);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }
}
