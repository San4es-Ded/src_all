package rockstar.client.internal.inventory;






import rockstar.client.rotation.*;
import rockstar.client.bot.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BotPlayerController {
   private final BotTargetManager internalField0716;
   private final WorldSnapshotTracker internalField0414;
   private Vec3d internalField0283 = Vec3d.ZERO;
   private float internalField0205;
   private float internalField0206;
   private boolean internalField0277;
   private boolean internalField0276;
   private boolean internalField1099;
   private boolean internalField1100 = true;
   private boolean internalField1102;
   private boolean internalField1101;
   private float internalField1048 = 20.0F;
   private int internalField0227 = 20;
   private int internalField0228;
   private ItemStack internalField0878 = ItemStack.EMPTY;
   private ItemStack internalField0879 = ItemStack.EMPTY;
   private boolean internalField1516;
   private BotFakePlayer internalField0412;
   private Entity internalField0410;
   private ItemStack[] internalField0299;
   private int internalField1053;
   private long internalField0229;
   private long internalField0230;
   private long internalField1059;
   private double internalField0194;
   private double internalField0193;
   private double internalField1045;
   private float internalField1047;
   private float internalField1049;
   private boolean internalField1517 = true;
   private int internalField1055;
   private final List<BotPlayerController.InternalType0204> internalField0416 = new ArrayList<>();

   public BotPlayerController(BotTargetManager localValue1, WorldSnapshotTracker localValue2) {
      this.internalField0716 = localValue1;
      this.internalField0414 = localValue2;
   }

   public void internalMethod02457() {
      this.internalMethod07743();
      this.internalMethod02465();
      this.internalMethod09810();
   }

   public void internalMethod02465() {
      BotPlayerState localValue1 = this.internalField0716.internalMethod06688();
      this.internalField0283 = localValue1.internalMethod06297();
      this.internalField0205 = localValue1.internalMethod02808();
      this.internalField0206 = localValue1.internalMethod02817();
      this.internalField0277 = localValue1.internalMethod08608();
      this.internalField0276 = localValue1.internalMethod08612();
      this.internalField1100 = localValue1.internalMethod09849();
      this.internalField1102 = localValue1.internalMethod09866();
      this.internalField1101 = localValue1.internalMethod09199();
      this.internalField1048 = localValue1.internalMethod08591();
      this.internalField0227 = localValue1.internalMethod02809();
      this.internalField0228 = localValue1.internalMethod02818();
      this.internalField0878 = localValue1.internalMethod07411();
      this.internalField0879 = localValue1.internalMethod05298();
      if (this.internalField1516) {
         this.internalMethod09627(MinecraftClient.getInstance());
      }

      this.internalField0414.internalMethod00561(localValue1);
      this.internalMethod09812();
   }

   public boolean internalMethod02294(MinecraftClient localValue1) {
      if (localValue1 != null && localValue1.world != null && localValue1.player != null) {
         this.internalField0410 = localValue1.getCameraEntity();
         this.internalMethod08743(localValue1);
         this.internalField1516 = true;
         this.internalMethod09145(localValue1);
         this.internalMethod09627(localValue1);
         this.internalMethod09812();
         localValue1.setCameraEntity(this.internalField0412);
         return true;
      } else {
         return false;
      }
   }

   public void internalMethod02293(MinecraftClient localValue1) {
      this.internalField1516 = false;
      this.internalMethod08604(localValue1);
      if (localValue1 != null && this.internalField0412 != null && localValue1.getCameraEntity() == this.internalField0412) {
         localValue1.setCameraEntity((Entity)(localValue1.player != null ? localValue1.player : this.internalField0410));
      }

      if (this.internalField0412 != null) {
         this.internalField0412.internalMethod07331();
         this.internalField0412 = null;
      }

      this.internalField0410 = null;
   }

   public void internalMethod01738(MinecraftClient localValue1) {
      if (this.internalField1516 && localValue1 != null && localValue1.player != null && localValue1.options != null) {
         BotPlayerState localValue2 = this.internalField0716.internalMethod06688();
         localValue2.internalMethod06850(localValue1.player.getYaw(), localValue1.player.getPitch());
         double localValue3 = this.internalMethod00297(localValue1.options.forwardKey.isPressed()) - this.internalMethod00297(localValue1.options.backKey.isPressed());
         double localValue5 = this.internalMethod00297(localValue1.options.rightKey.isPressed()) - this.internalMethod00297(localValue1.options.leftKey.isPressed());
         boolean localValue7 = localValue1.options.sprintKey.isPressed() && localValue3 > 0.0 && !localValue1.options.sneakKey.isPressed();
         boolean localValue8 = localValue1.options.sneakKey.isPressed();
         this.internalField0716.internalMethod00339(localValue7);
         this.internalField0716.internalMethod00260(localValue8);
         this.internalMethod03263(localValue1, localValue2);
         this.internalMethod01668(localValue2, localValue3, localValue5, localValue7, localValue8, localValue1.options.jumpKey.isPressed());
         this.internalMethod07661(localValue1);
      }
   }

   private void internalMethod01668(BotPlayerState localValue1, double localValue2, double localValue4, boolean localValue6, boolean localValue7, boolean localValue8) {
      double localValue9 = Math.sqrt(localValue2 * localValue2 + localValue4 * localValue4);
      if (localValue9 > 1.0) {
         localValue2 /= localValue9;
         localValue4 /= localValue9;
      }

      double localValue11 = this.internalField0716.internalMethod06687().internalMethod07713();
      if (localValue6) {
         localValue11 *= this.internalField0716.internalMethod06687().internalMethod09862();
      } else if (localValue7) {
         localValue11 *= this.internalField0716.internalMethod06687().internalMethod09190();
      }

      double localValue13 = Math.toRadians(localValue1.internalMethod02808());
      double localValue15 = Math.sin(localValue13);
      double localValue17 = Math.cos(localValue13);
      Vec3d localValue19 = localValue1.internalMethod06297();
      double localValue20 = (-localValue15 * localValue2 + localValue17 * localValue4) * localValue11;
      double localValue22 = (localValue17 * localValue2 + localValue15 * localValue4) * localValue11;
      this.internalField1099 = localValue8;
      if (localValue1.internalMethod09866()) {
         double localValue30 = 0.0;
         double localValue26 = Math.max(this.internalField0716.internalMethod06687().internalMethod09193(), (double)localValue1.internalMethod08610());
         if (localValue8) {
            localValue30 += localValue26;
         }

         if (localValue7) {
            localValue30 -= localValue26;
         }

         double localValue28 = this.internalField0716.internalMethod06687().internalMethod09200();
         if (Math.abs(localValue20) > localValue28 || Math.abs(localValue22) > localValue28 || Math.abs(localValue30) > localValue28) {
            localValue1.internalMethod07202(localValue19.x + localValue20, localValue19.y + localValue30, localValue19.z + localValue22);
            localValue1.internalMethod09022(false);
         }
      } else {
         if (localValue8) {
            this.internalField0716.internalMethod07696();
         }

         double localValue24 = this.internalField0716.internalMethod06687().internalMethod09200();
         if (Math.abs(localValue20) > localValue24 || Math.abs(localValue22) > localValue24) {
            localValue1.internalMethod07202(localValue19.x + localValue20, localValue19.y, localValue19.z + localValue22);
         }
      }
   }

   private void internalMethod07661(MinecraftClient localValue1) {
      long localValue2 = System.currentTimeMillis();
      long localValue4 = this.internalField0716.internalMethod06687().internalMethod04162();
      boolean localValue6 = localValue1.options.attackKey.isPressed();
      boolean localValue7 = localValue1.options.useKey.isPressed();
      if (localValue6) {
         boolean localValue8 = this.internalField0716.internalMethod07693();
         if (localValue8 || localValue2 - this.internalField0229 >= localValue4) {
            this.internalField0716.internalMethod04573();
            this.internalField0229 = localValue2;
         }
      } else {
         this.internalField0716.internalMethod09726();
      }

      if (localValue7 && localValue2 - this.internalField0230 >= localValue4) {
         this.internalField0716.internalMethod04580();
         this.internalField0230 = localValue2;
      }

      if (localValue1.options.dropKey.isPressed() && localValue2 - this.internalField1059 >= localValue4) {
         this.internalField0716.internalMethod08814(false);
         this.internalField1059 = localValue2;
      }
   }

   public void internalMethod06424(Vec3d localValue1) {
      if (localValue1 != null) {
         this.internalField0716.internalMethod05389(localValue1.x, localValue1.y, localValue1.z);
      }
   }

   public void internalMethod01966(Vec3d localValue1, double localValue2) {
      this.internalField0716.internalMethod01196(localValue1, localValue2);
   }

   public void internalMethod00783(double localValue1, double localValue3, double localValue5) {
      this.internalField0716.internalMethod02198(localValue1, localValue3, localValue5);
   }

   public void internalMethod07707() {
      this.internalField0716.internalMethod07696();
   }

   public void internalMethod00298(boolean localValue1) {
      this.internalField0716.internalMethod00260(localValue1);
   }

   public void internalMethod00365(boolean localValue1) {
      this.internalField0716.internalMethod00339(localValue1);
   }

   public void internalMethod06482(Hand localValue1) {
      this.internalField0716.internalMethod04050().internalMethod07163(new HandSwingC2SPacket(localValue1 == null ? Hand.MAIN_HAND : localValue1));
   }

   public void internalMethod08980(boolean localValue1) {
      this.internalField0716.internalMethod08814(localValue1);
   }

   public void internalMethod07711() {
      this.internalField0716.internalMethod04050().internalMethod07163(new ClientStatusC2SPacket(Mode.PERFORM_RESPAWN));
   }

   public void internalMethod07741() {
      if (this.internalField0716.internalMethod07697() && this.internalField0716.internalMethod06688().internalMethod02812()) {
         int localValue1 = this.internalField0716.internalMethod06688().internalMethod08597();
         this.internalField0716.internalMethod04050().internalMethod07163(new CloseHandledScreenC2SPacket(localValue1));
         this.internalField0716.internalMethod06688().internalMethod04821(localValue1);
      }
   }

   public boolean internalMethod00296(int localValue1) {
      return this.internalField0716.internalMethod00257(localValue1);
   }

   public void internalMethod04329(int localValue1, int localValue2, SlotActionType localValue3, long localValue4) {
      long localValue6 = System.currentTimeMillis() + Math.max(0L, localValue4);
      this.internalField0416.add(new BotPlayerController.InternalType0204(localValue1, localValue2, localValue3 == null ? SlotActionType.PICKUP : localValue3, localValue6));
   }

   public boolean internalMethod04336(Item localValue1) {
      return this.internalField0716.internalMethod05748(localValue1);
   }

   public ItemStack internalMethod01356(Hand localValue1) {
      return localValue1 == Hand.OFF_HAND ? this.internalField0879 : this.internalField0878;
   }

   public Vec3d internalMethod05321() {
      return this.internalField0716.internalMethod01993();
   }

   public Vec3d internalMethod02340() {
      return this.internalField0716.internalMethod07160();
   }

   public boolean internalMethod02458() {
      return this.internalField1048 > 0.0F;
   }

   public boolean internalMethod02466() {
      return !this.internalMethod02458();
   }

   private double internalMethod00297(boolean localValue1) {
      return localValue1 ? 1.0 : 0.0;
   }

   private void internalMethod07743() {
      if (!this.internalField0416.isEmpty()) {
         long localValue1 = System.currentTimeMillis();
         Iterator localValue3 = this.internalField0416.iterator();

         while (localValue3.hasNext()) {
            BotPlayerController.InternalType0204 localValue4 = (BotPlayerController.InternalType0204)localValue3.next();
            if (localValue1 >= localValue4.internalMethod06134()) {
               this.internalField0716.internalMethod06223(localValue4.internalMethod06133(), localValue4.internalMethod06137(), localValue4.internalMethod04829());
               localValue3.remove();
            }
         }
      }
   }

   private void internalMethod09810() {
      if (this.internalField0716.internalMethod07697()) {
         BotPlayerState localValue1 = this.internalField0716.internalMethod06688();
         this.internalField1055++;
         double localValue2 = localValue1.internalMethod02807() - this.internalField0194;
         double localValue4 = localValue1.internalMethod02816() - this.internalField0193;
         double localValue6 = localValue1.internalMethod08590() - this.internalField1045;
         float localValue8 = MathHelper.wrapDegrees(localValue1.internalMethod02808() - this.internalField1047);
         float localValue9 = localValue1.internalMethod02817() - this.internalField1049;
         boolean localValue10 = this.internalField1055 >= Math.max(1, this.internalField0716.internalMethod06687().internalMethod09860());
         boolean localValue11 = localValue2 * localValue2 + localValue4 * localValue4 + localValue6 * localValue6 > this.internalField0716.internalMethod06687().internalMethod10124() || localValue10;
         boolean localValue12 = Math.abs(localValue8) > this.internalField0716.internalMethod06687().internalMethod10125()
            || Math.abs(localValue9) > this.internalField0716.internalMethod06687().internalMethod10125();
         boolean localValue13 = localValue1.internalMethod09849() != this.internalField1517;
         if (localValue11 && localValue12) {
            this.internalField0716
               .internalMethod04050()
               .internalMethod07163(
                  new Full(
                     localValue1.internalMethod02807(),
                     localValue1.internalMethod02816(),
                     localValue1.internalMethod08590(),
                     localValue1.internalMethod02808(),
                     localValue1.internalMethod02817(),
                     localValue1.internalMethod09849(),
                     false
                  )
               );
         } else if (localValue11) {
            this.internalField0716
               .internalMethod04050()
               .internalMethod07163(
                  new PositionAndOnGround(localValue1.internalMethod02807(), localValue1.internalMethod02816(), localValue1.internalMethod08590(), localValue1.internalMethod09849(), false)
               );
         } else if (localValue12) {
            this.internalField0716
               .internalMethod04050()
               .internalMethod07163(new LookAndOnGround(localValue1.internalMethod02808(), localValue1.internalMethod02817(), localValue1.internalMethod09849(), false));
         } else {
            if (!localValue13) {
               return;
            }

            this.internalField0716.internalMethod04050().internalMethod07163(new OnGroundOnly(localValue1.internalMethod09849(), false));
         }

         if (localValue11) {
            this.internalField0194 = localValue1.internalMethod02807();
            this.internalField0193 = localValue1.internalMethod02816();
            this.internalField1045 = localValue1.internalMethod08590();
            this.internalField1055 = 0;
         }

         if (localValue12) {
            this.internalField1047 = localValue1.internalMethod02808();
            this.internalField1049 = localValue1.internalMethod02817();
         }

         this.internalField1517 = localValue1.internalMethod09849();
         localValue1.internalMethod02811();
      }
   }

   private void internalMethod09145(MinecraftClient localValue1) {
      if (this.internalField0412 == null || this.internalField0412.getEntityWorld() != localValue1.world || this.internalField0412.isRemoved()) {
         if (this.internalField0412 != null) {
            this.internalField0412.internalMethod07331();
         }

         UUID localValue2 = UUID.nameUUIDFromBytes(("RockstarBotCamera:" + this.internalField0716.internalMethod03426()).getBytes(StandardCharsets.UTF_8));
         this.internalField0412 = new BotFakePlayer(localValue1.world, new GameProfile(localValue2, this.internalField0716.internalMethod03426()));
         this.internalField0412.internalMethod07330();
      }
   }

   private void internalMethod08743(MinecraftClient localValue1) {
      if (localValue1 != null && localValue1.player != null && this.internalField0299 == null) {
         PlayerInventory localValue2 = localValue1.player.getInventory();
         this.internalField0299 = new ItemStack[localValue2.size()];

         for (int localValue3 = 0; localValue3 < this.internalField0299.length; localValue3++) {
            this.internalField0299[localValue3] = this.internalMethod06200(localValue2.getStack(localValue3));
         }

         this.internalField1053 = localValue2.getSelectedSlot();
      }
   }

   private void internalMethod08604(MinecraftClient localValue1) {
      if (localValue1 != null && localValue1.player != null && this.internalField0299 != null) {
         PlayerInventory localValue2 = localValue1.player.getInventory();

         for (int localValue3 = 0; localValue3 < this.internalField0299.length && localValue3 < localValue2.size(); localValue3++) {
            localValue2.setStack(localValue3, this.internalMethod06200(this.internalField0299[localValue3]));
         }

         localValue2.setSelectedSlot(MathHelper.clamp(this.internalField1053, 0, 8));
         localValue2.markDirty();
         this.internalField0299 = null;
      } else {
         this.internalField0299 = null;
      }
   }

   private void internalMethod09627(MinecraftClient localValue1) {
      if (localValue1 != null && localValue1.player != null) {
         BotPlayerState localValue2 = this.internalField0716.internalMethod06688();
         PlayerInventory localValue3 = localValue1.player.getInventory();
         ItemStack[] localValue4 = localValue2.internalMethod00106();

         for (int localValue5 = 0; localValue5 < localValue4.length && localValue5 < 36 && localValue5 < localValue3.size(); localValue5++) {
            localValue3.setStack(localValue5, this.internalMethod06200(localValue4[localValue5]));
         }

         ItemStack[] localValue8 = localValue2.internalMethod05401();

         for (int localValue6 = 0; localValue6 < localValue8.length; localValue6++) {
            int localValue7 = 36 + localValue6;
            if (localValue7 < localValue3.size()) {
               localValue3.setStack(localValue7, this.internalMethod06200(localValue8[localValue6]));
            }
         }

         if (40 < localValue3.size()) {
            localValue3.setStack(40, this.internalMethod06200(localValue2.internalMethod05298()));
         }

         localValue3.setSelectedSlot(MathHelper.clamp(localValue2.internalMethod02818(), 0, 8));
         localValue3.markDirty();
      }
   }

   private void internalMethod03263(MinecraftClient localValue1, BotPlayerState localValue2) {
      if (localValue1 != null && localValue1.player != null && localValue2 != null) {
         int localValue3 = MathHelper.clamp(localValue1.player.getInventory().getSelectedSlot(), 0, 8);
         if (localValue3 != localValue2.internalMethod02818()) {
            this.internalField0716.internalMethod00338(localValue3);
         }
      }
   }

   private ItemStack internalMethod06200(ItemStack localValue1) {
      return localValue1 == null ? ItemStack.EMPTY : localValue1.copy();
   }

   private void internalMethod09812() {
      if (this.internalField1516 && this.internalField0412 != null && !this.internalField0412.isRemoved()) {
         this.internalField0412.internalMethod05011(this.internalField0716);
      }
   }

   @Generated
   public BotTargetManager internalMethod04563() {
      return this.internalField0716;
   }

   @Generated
   public WorldSnapshotTracker internalMethod06991() {
      return this.internalField0414;
   }

   @Generated
   public Vec3d internalMethod07962() {
      return this.internalField0283;
   }

   @Generated
   public float internalMethod02454() {
      return this.internalField0205;
   }

   @Generated
   public float internalMethod02462() {
      return this.internalField0206;
   }

   @Generated
   public boolean internalMethod07708() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod07712() {
      return this.internalField0276;
   }

   @Generated
   public boolean internalMethod07742() {
      return this.internalField1099;
   }

   @Generated
   public boolean internalMethod07744() {
      return this.internalField1100;
   }

   @Generated
   public boolean internalMethod09811() {
      return this.internalField1102;
   }

   @Generated
   public boolean internalMethod09813() {
      return this.internalField1101;
   }

   @Generated
   public float internalMethod07704() {
      return this.internalField1048;
   }

   @Generated
   public int internalMethod02455() {
      return this.internalField0227;
   }

   @Generated
   public int internalMethod02463() {
      return this.internalField0228;
   }

   @Generated
   public ItemStack internalMethod02104() {
      return this.internalField0878;
   }

   @Generated
   public ItemStack internalMethod05240() {
      return this.internalField0879;
   }

   @Generated
   public boolean internalMethod09820() {
      return this.internalField1516;
   }

   @Generated
   public BotFakePlayer internalMethod06990() {
      return this.internalField0412;
   }

   @Generated
   public Entity internalMethod05592() {
      return this.internalField0410;
   }

   @Generated
   public ItemStack[] internalMethod05473() {
      return this.internalField0299;
   }

   @Generated
   public int internalMethod07705() {
      return this.internalField1053;
   }

   @Generated
   public long internalMethod02456() {
      return this.internalField0229;
   }

   @Generated
   public long internalMethod02464() {
      return this.internalField0230;
   }

   @Generated
   public long internalMethod07706() {
      return this.internalField1059;
   }

   @Generated
   public double internalMethod02453() {
      return this.internalField0194;
   }

   @Generated
   public double internalMethod02461() {
      return this.internalField0193;
   }

   @Generated
   public double internalMethod07703() {
      return this.internalField1045;
   }

   @Generated
   public float internalMethod07709() {
      return this.internalField1047;
   }

   @Generated
   public float internalMethod07740() {
      return this.internalField1049;
   }

   @Generated
   public boolean internalMethod09821() {
      return this.internalField1517;
   }

   @Generated
   public int internalMethod07710() {
      return this.internalField1055;
   }

   @Generated
   public List<BotPlayerController.InternalType0204> internalMethod01709() {
      return this.internalField0416;
   }

   static final class InternalType0204 {
      private final int internalField0227;
      private final int internalField0228;
      private final SlotActionType internalField0899;
      private final long internalField0229;

      InternalType0204(int localValue1, int localValue2, SlotActionType localValue3, long localValue4) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField0899 = localValue3;
         this.internalField0229 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0204[slot=" + this.internalField0227 + ", button=" + this.internalField0228 + ", actionType=" + this.internalField0899 + ", runAtMs=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0899);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BotPlayerController.InternalType0204 other = (BotPlayerController.InternalType0204) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField0899, other.internalField0899)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public int internalMethod06133() {
         return this.internalField0227;
      }

      public int internalMethod06137() {
         return this.internalField0228;
      }

      public SlotActionType internalMethod04829() {
         return this.internalField0899;
      }

      public long internalMethod06134() {
         return this.internalField0229;
      }
   }
}
