 package su.sacura.events.render;
 
 import net.minecraft.client.util.math.MatrixStack;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.Hand;
 
 public class EventHeldItemRenderer {
   private final Hand hand;
   
   private final ItemStack item;
   
   private final float ep;
   
   private final MatrixStack stack;
   
   public Hand getHand() {
     return this.hand;
   }
   
   public ItemStack getItem() {
     return this.item;
   }
   
   public float getEp() {
     return this.ep;
   }
   
   public MatrixStack getStack() {
     return this.stack;
   }
   
   public EventHeldItemRenderer(Hand hand, ItemStack item, float equipProgress, MatrixStack stack) {
     this.hand = hand;
     this.item = item;
     this.ep = equipProgress;
     this.stack = stack;
   }
 }


