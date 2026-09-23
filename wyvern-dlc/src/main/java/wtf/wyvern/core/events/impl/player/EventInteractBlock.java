package wtf.wyvern.core.events.impl.player;

import lombok.Generated;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import wtf.wyvern.core.events.callables.EventCancellable;

public final class EventInteractBlock extends EventCancellable {
   private final BlockPos pos;
   private final Hand hand;

   @Generated
   public BlockPos getPos() {
      return this.pos;
   }

   @Generated
   public Hand getHand() {
      return this.hand;
   }

   @Generated
   public EventInteractBlock(BlockPos pos, Hand hand) {
      this.pos = pos;
      this.hand = hand;
   }
}
