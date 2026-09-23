package pulse.events;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;

public class WorldRenderEvent extends PulseEvent {
   private final MatrixStack matrices;
   private final Immediate bufferSource;
   private final float tickDelta;

   public WorldRenderEvent(MatrixStack matrices, float tickDelta) {
      this(matrices, null, tickDelta);
   }

   public WorldRenderEvent(MatrixStack matrices, Immediate bufferSource, float tickDelta) {
      this.matrices = matrices;
      this.bufferSource = bufferSource;
      this.tickDelta = tickDelta;
   }

   public MatrixStack matrices() {
      return this.matrices;
   }

   public Immediate bufferSource() {
      return this.bufferSource;
   }

   public float tickDelta() {
      return this.tickDelta;
   }

   public MatrixStack a() {
      return this.matrices;
   }

   public float b() {
      return this.tickDelta;
   }
}
