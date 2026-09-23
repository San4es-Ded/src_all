package rockstar.client.bot;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.BitSet;
import java.util.OptionalDouble;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.LastSeenMessageList.Acknowledgment;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EmptyBlockView;

public class BotTargetManager {
   private final String internalField0248;
   private final OfflineBotConnection internalField0713;
   private final BotPlayerState internalField0172;
   private final WorldSnapshotTracker internalField0414;
   private final BotPlayerController internalField0413;
   private BotConfig internalField0171;
   private BotPlayPacketHandler internalField0170;
   private BotBehavior internalField0182 = new VelocityDecayBehavior();
   private boolean internalField0277;
   private int internalField0227;
   private boolean internalField0276;
   private String internalField0247;
   private int internalField0228 = 25565;
   private int internalField1053 = 0;
   private double internalField0194;
   private double internalField0193;
   private double internalField1045;
   private double internalField1043;
   private double internalField1042 = Double.NaN;
   private long internalField0229;
   private BlockPos internalField0352;
   private Direction internalField0150 = Direction.UP;
   private double internalField1044;
   private long internalField0230;
   private long internalField1059;
   private int internalField1055;

   public BotTargetManager(String localValue1, BotConfig localValue2) {
      this.internalField0248 = localValue1;
      this.internalField0171 = localValue2;
      this.internalField0172 = new BotPlayerState();
      this.internalField0414 = new WorldSnapshotTracker(localValue1);
      this.internalField0413 = new BotPlayerController(this, this.internalField0414);
      this.internalField0713 = new OfflineBotConnection(this);
      this.internalField0227 = localValue2.internalMethod09194();
   }

   public void internalMethod02830(String localValue1, int localValue2) {
      this.internalField0247 = localValue1;
      this.internalField0228 = localValue2;
      this.internalField0276 = false;
      this.internalField0414.internalMethod00864(localValue1, localValue2);
      this.internalField0713.internalMethod01000(localValue1, localValue2);
   }

   public void internalMethod04572() {
      this.internalField0276 = true;
      this.internalMethod05456(new VelocityDecayBehavior());
      this.internalMethod09726();
      this.internalField0414.internalMethod04199();
      this.internalField0713.internalMethod06538();
   }

   public void internalMethod04579() {
      this.internalField0713.internalMethod06982();
      if (this.internalField0713.internalMethod06539()) {
         this.internalField1053++;
         if (this.internalField0413.internalMethod09820()) {
            this.internalField0413.internalMethod01738(MinecraftClient.getInstance());
         } else if (this.internalField0182 != null) {
            this.internalField0182.internalMethod07229(this);
         }

         this.internalMethod09926();
         this.internalField0414.internalMethod04644(this.internalField0172);
         this.internalField0413.internalMethod02457();
      }
   }

   public void internalMethod07692() {
      this.internalMethod07696();
   }

   public void internalMethod07696() {
      if (this.internalField0713.internalMethod06539()) {
         if (this.internalField0172.internalMethod09866()) {
            Vec3d localValue1 = this.internalField0172.internalMethod06297();
            this.internalField0172
               .internalMethod07202(
                  localValue1.x, localValue1.y + Math.max(this.internalField0171.internalMethod09193(), (double)this.internalField0172.internalMethod08610()), localValue1.z
               );
            this.internalField0172.internalMethod09022(false);
         } else if (this.internalField0172.internalMethod09849()) {
            this.internalMethod09928();
            this.internalField1043 = this.internalField0171.internalMethod09193();
            this.internalField0172.internalMethod09022(false);
         }
      }
   }

   public void internalMethod07730() {
      this.internalMethod00260(!this.internalField0172.internalMethod08608());
   }

   public void internalMethod00260(boolean localValue1) {
      if (this.internalField0713.internalMethod06539()) {
         if (this.internalField0172.internalMethod08608() != localValue1) {
            this.internalField0172.internalMethod04823(localValue1);
            this.internalMethod07655();
         }
      }
   }

   public void internalMethod00339(boolean localValue1) {
      if (this.internalField0713.internalMethod06539()) {
         if (this.internalField0172.internalMethod08612() != localValue1) {
            this.internalField0172.internalMethod04882(localValue1);
            this.internalMethod07656(localValue1 ? Mode.START_SPRINTING : Mode.STOP_SPRINTING);
         }
      }
   }

   public void internalMethod07733() {
      if (this.internalField0713.internalMethod06539()) {
         this.internalField0713.internalMethod07163(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }
   }

   public void internalMethod05689(Entity localValue1) {
      if (this.internalField0713.internalMethod06539() && localValue1 != null) {
         this.internalField0713.internalMethod07163(PlayerInteractEntityC2SPacket.attack(localValue1, this.internalField0172.internalMethod08608()));
         this.internalField0713.internalMethod07163(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }
   }

   public void internalMethod00256(int localValue1) {
      if (this.internalField0713.internalMethod06539() && localValue1 >= 0) {
         Entity localValue2 = this.internalMethod01876(localValue1);
         if (localValue2 == null) {
            this.internalMethod07733();
         } else {
            this.internalMethod05689(localValue2);
         }
      }
   }

   public void internalMethod09725() {
      if (this.internalField0713.internalMethod06539()) {
         this.internalField0713
            .internalMethod07163(
               new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, this.internalField0172.internalMethod02808(), this.internalField0172.internalMethod02817())
            );
      }
   }

   public void internalMethod04236(BlockHitResult localValue1) {
      if (this.internalField0713.internalMethod06539() && localValue1 != null) {
         this.internalField0713.internalMethod07163(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, localValue1, this.internalMethod07729()));
         this.internalField0713.internalMethod07163(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }
   }

   public void internalMethod05233(BlockHitResult localValue1) {
      if (this.internalField0713.internalMethod06539() && localValue1 != null) {
         BlockPos localValue2 = localValue1.getBlockPos();
         Direction localValue3 = localValue1.getSide();
         this.internalMethod06497(localValue2, localValue3);
         this.internalMethod05595(localValue2, localValue3);
      }
   }

   public void internalMethod06497(BlockPos localValue1, Direction localValue2) {
      if (this.internalField0713.internalMethod06539() && localValue1 != null) {
         this.internalField0713
            .internalMethod07163(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, localValue1, localValue2 == null ? Direction.UP : localValue2, this.internalMethod07729()));
         this.internalField0713.internalMethod07163(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }
   }

   public void internalMethod05595(BlockPos localValue1, Direction localValue2) {
      if (this.internalField0713.internalMethod06539() && localValue1 != null) {
         this.internalField0713
            .internalMethod07163(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, localValue1, localValue2 == null ? Direction.UP : localValue2, this.internalMethod07729()));
      }
   }

   public boolean internalMethod04573() {
      if (!this.internalField0713.internalMethod06539()) {
         return false;
      } else {
         BotTargetManager.InternalType0481 localValue1 = this.internalMethod01916(this.internalField0171.internalMethod09202());
         BotTargetManager.InternalType0482 localValue2 = this.internalMethod01917(localValue1 == null ? this.internalField0171.internalMethod09202() : localValue1.internalMethod05299());
         if (localValue2 != null) {
            this.internalMethod09726();
            this.internalMethod00256(localValue2.internalMethod02460().internalMethod05993());
            return true;
         } else if (localValue1 != null) {
            this.internalMethod07957(localValue1.internalMethod04871());
            return true;
         } else {
            this.internalMethod09726();
            this.internalMethod07733();
            return false;
         }
      }
   }

   public boolean internalMethod04580() {
      if (!this.internalField0713.internalMethod06539()) {
         return false;
      } else {
         BotTargetManager.InternalType0481 localValue1 = this.internalMethod01916(this.internalField0171.internalMethod09202());
         if (localValue1 != null) {
            this.internalMethod04236(localValue1.internalMethod04871());
            return true;
         } else {
            this.internalMethod09725();
            return false;
         }
      }
   }

   public void internalMethod08814(boolean localValue1) {
      if (this.internalField0713.internalMethod06539()) {
         this.internalField0713.internalMethod07163(new PlayerActionC2SPacket(localValue1 ? Action.DROP_ALL_ITEMS : Action.DROP_ITEM, BlockPos.ORIGIN, Direction.DOWN));
      }
   }

   public void internalMethod00338(int localValue1) {
      if (this.internalField0713.internalMethod06539()) {
         if (localValue1 >= 0 && localValue1 <= 8) {
            this.internalField0172.internalMethod09021(localValue1);
            this.internalField0713.internalMethod07163(new UpdateSelectedSlotC2SPacket(localValue1));
         }
      }
   }

   public boolean internalMethod05748(Item localValue1) {
      if (this.internalField0713.internalMethod06539() && localValue1 != null) {
         for (int localValue2 = 0; localValue2 < 9; localValue2++) {
            ItemStack localValue3 = this.internalField0172.internalMethod00106()[localValue2];
            if (localValue3 != null && !localValue3.isEmpty() && localValue3.getItem() == localValue1) {
               this.internalMethod00338(localValue2);
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean internalMethod00257(int localValue1) {
      return this.internalMethod06223(localValue1, 0, SlotActionType.PICKUP);
   }

   public boolean internalMethod06223(int localValue1, int localValue2, SlotActionType localValue3) {
      if (this.internalField0713.internalMethod06539() && this.internalField0172.internalMethod02812() && localValue1 >= 0) {
         if (MinecraftClient.getInstance().getNetworkHandler() == null) {
            return false;
         }

         SlotActionType localValue4 = localValue3 == null ? SlotActionType.PICKUP : localValue3;
         Int2ObjectOpenHashMap<ItemStackHash> localValue5 = new Int2ObjectOpenHashMap<>();
         var localValue10 = MinecraftClient.getInstance().getNetworkHandler().getComponentHasher();
         ItemStack localValue6 = this.internalField0172.internalMethod08661();
         if (localValue4 == SlotActionType.PICKUP && localValue2 == 0) {
            ItemStack localValue7 = this.internalField0172.internalMethod01305(localValue1).copy();
            ItemStack localValue8 = localValue6 == null ? ItemStack.EMPTY : localValue6.copy();
            ItemStack localValue9 = localValue7 == null ? ItemStack.EMPTY : localValue7.copy();
            this.internalField0172.internalMethod03129(this.internalField0172.internalMethod08597(), this.internalField0172.internalMethod08607(), localValue1, localValue8);
            this.internalField0172.internalMethod04965(localValue9);
            localValue5.put(localValue1, ItemStackHash.fromItemStack(localValue8, localValue10));
            localValue6 = localValue9;
         }

         this.internalField0713
            .internalMethod07163(
               new ClickSlotC2SPacket(
                  this.internalField0172.internalMethod08597(),
                  this.internalField0172.internalMethod08607(),
                  (short)localValue1,
                  (byte)localValue2,
                  localValue4,
                  localValue5,
                  ItemStackHash.fromItemStack(localValue6 == null ? ItemStack.EMPTY : localValue6, localValue10)
               )
            );
         return true;
      } else {
         return false;
      }
   }

   public boolean internalMethod07693() {
      BotTargetManager.InternalType0481 localValue1 = this.internalMethod01916(this.internalField0171.internalMethod09202());
      return localValue1 != null && this.internalMethod07417(localValue1.internalMethod04871().getBlockPos());
   }

   public void internalMethod09726() {
      if (this.internalField0352 != null && this.internalField0713.internalMethod06539()) {
         this.internalField0713
            .internalMethod07163(
               new PlayerActionC2SPacket(
                  Action.ABORT_DESTROY_BLOCK,
                  this.internalField0352,
                  this.internalField0150 == null ? Direction.UP : this.internalField0150,
                  this.internalMethod07729()
               )
            );
         this.internalMethod09925();
      } else {
         this.internalMethod09925();
      }
   }

   private void internalMethod07957(BlockHitResult localValue1) {
      if (localValue1 != null && this.internalField0713.internalMethod06539()) {
         BlockPos localValue2 = localValue1.getBlockPos();
         Direction localValue3 = localValue1.getSide() == null ? this.internalMethod00513(localValue2) : localValue1.getSide();
         if (!this.internalMethod07417(localValue2)) {
            this.internalMethod09726();
         } else if (this.internalField0172.internalMethod09199()) {
            this.internalMethod09726();
            this.internalMethod05233(localValue1);
         } else {
            long localValue4 = System.currentTimeMillis();
            if (!localValue2.equals(this.internalField0352)) {
               this.internalMethod09726();
               this.internalField0352 = localValue2.toImmutable();
               this.internalField0150 = localValue3;
               this.internalField1044 = 0.0;
               this.internalField0230 = localValue4;
               this.internalField1059 = 0L;
               this.internalMethod06497(this.internalField0352, this.internalField0150);
            } else {
               this.internalField0150 = localValue3;
               BlockState localValue6 = this.internalField0414.internalMethod00820(localValue2);
               double localValue7 = Math.max(1.0, (localValue4 - this.internalField0230) / 50.0);
               this.internalField0230 = localValue4;
               double localValue9 = Math.max(
                  this.internalField0171.internalMethod10142(),
                  localValue6.getHardness(EmptyBlockView.INSTANCE, localValue2) * this.internalField0171.internalMethod10141()
               );
               this.internalField1044 += localValue7 / localValue9;
               if (localValue4 - this.internalField1059 >= this.internalField0171.internalMethod09864()) {
                  this.internalField0713.internalMethod07163(new HandSwingC2SPacket(Hand.MAIN_HAND));
                  this.internalField1059 = localValue4;
               }

               if (this.internalField1044 >= 1.0) {
                  this.internalMethod05595(localValue2, this.internalField0150);
                  this.internalMethod09925();
               }
            }
         }
      }
   }

   private boolean internalMethod07417(BlockPos localValue1) {
      if (localValue1 == null) {
         return false;
      } else {
         BlockState localValue2 = this.internalField0414.internalMethod00820(localValue1);
         return !localValue2.isAir() && localValue2.getHardness(EmptyBlockView.INSTANCE, localValue1) >= 0.0F;
      }
   }

   private void internalMethod09925() {
      this.internalField0352 = null;
      this.internalField0150 = Direction.UP;
      this.internalField1044 = 0.0;
      this.internalField0230 = 0L;
      this.internalField1059 = 0L;
   }

   public void internalMethod04382(String localValue1) {
      if (this.internalField0713.internalMethod06539()) {
         if (localValue1.startsWith("/")) {
            this.internalMethod02827(localValue1.substring(1));
         } else {
            this.internalField0713
               .internalMethod07163(
                  new ChatMessageC2SPacket(localValue1, Instant.now(), ThreadLocalRandom.current().nextLong(), null, new Acknowledgment(0, new BitSet(), (byte)0))
               );
         }
      }
   }

   public void internalMethod02827(String localValue1) {
      if (this.internalField0713.internalMethod06539()) {
         this.internalField0713.internalMethod07163(new CommandExecutionC2SPacket(localValue1));
      }
   }

   public void internalMethod05456(BotBehavior localValue1) {
      this.internalField0182 = (BotBehavior)(localValue1 == null ? new VelocityDecayBehavior() : localValue1);
   }

   public void internalMethod09732() {
      this.internalMethod05456(new VelocityDecayBehavior());
      this.internalMethod09726();
      this.internalField0194 = 0.0;
      this.internalField0193 = 0.0;
      this.internalField1045 = 0.0;
   }

   public void internalMethod05389(double localValue1, double localValue3, double localValue5) {
      Vec3d localValue7 = this.internalField0172.internalMethod06297();
      double localValue8 = localValue1 - localValue7.x;
      double localValue10 = localValue3 - (localValue7.y + this.internalField0171.internalMethod10123());
      double localValue12 = localValue5 - localValue7.z;
      double localValue14 = Math.sqrt(localValue8 * localValue8 + localValue12 * localValue12);
      float localValue16 = (float)Math.toDegrees(Math.atan2(-localValue8, localValue12));
      float localValue17 = (float)Math.toDegrees(-Math.atan2(localValue10, localValue14));
      this.internalField0172
         .internalMethod06850(
            this.internalMethod05392(this.internalField0172.internalMethod02808(), localValue16, (float)this.internalField0171.internalMethod07749()),
            this.internalMethod05392(this.internalField0172.internalMethod02817(), localValue17, (float)this.internalField0171.internalMethod09840())
         );
   }

   public void internalMethod02198(double localValue1, double localValue3, double localValue5) {
      this.internalField0172.internalMethod07202(localValue1, localValue3, localValue5);
      this.internalField1043 = 0.0;
      this.internalField1042 = localValue3;
      this.internalField0172.internalMethod09022(true);
   }

   public void internalMethod01196(Vec3d localValue1, double localValue2) {
      if (localValue1 != null) {
         Vec3d localValue4 = this.internalField0172.internalMethod06297();
         double localValue5 = localValue1.x - localValue4.x;
         double localValue7 = localValue1.z - localValue4.z;
         double localValue9 = Math.sqrt(localValue5 * localValue5 + localValue7 * localValue7);
         this.internalMethod05389(localValue1.x, localValue1.y + this.internalField0171.internalMethod10123(), localValue1.z);
         if (!(localValue9 <= localValue2) && !(localValue9 < 1.0E-4)) {
            double localValue11 = this.internalField0171.internalMethod07713() * this.internalField0171.internalMethod04160();
            double localValue13 = Math.min(localValue11, Math.max(0.0, localValue9 - localValue2));
            this.internalField0194 = this.internalMethod05388(this.internalField0194, localValue13, this.internalField0171.internalMethod07717());
            this.internalField0193 = localValue5 / localValue9 * this.internalField0194;
            this.internalField1045 = localValue7 / localValue9 * this.internalField0194;
            double localValue15 = localValue1.y - localValue4.y;
            if (localValue15 > this.internalField0171.internalMethod10122() && this.internalField0172.internalMethod09849()) {
               this.internalMethod07696();
            }

            this.internalField0172.internalMethod07202(localValue4.x + this.internalField0193, localValue4.y, localValue4.z + this.internalField1045);
         } else {
            this.internalMethod09733();
         }
      }
   }

   public void internalMethod09733() {
      if (Math.abs(this.internalField0193) < 1.0E-5 && Math.abs(this.internalField1045) < 1.0E-5) {
         this.internalField0194 = 0.0;
         this.internalField0193 = 0.0;
         this.internalField1045 = 0.0;
      } else {
         this.internalField0193 = this.internalField0193 * this.internalField0171.internalMethod07745();
         this.internalField1045 = this.internalField1045 * this.internalField0171.internalMethod07745();
         this.internalField0194 = this.internalField0194 * this.internalField0171.internalMethod07745();
         Vec3d localValue1 = this.internalField0172.internalMethod06297();
         this.internalField0172.internalMethod07202(localValue1.x + this.internalField0193, localValue1.y, localValue1.z + this.internalField1045);
      }
   }

   public void internalMethod00255(double localValue1) {
      double localValue3 = Math.toRadians(this.internalField0172.internalMethod02808());
      Vec3d localValue5 = this.internalField0172.internalMethod06297();
      this.internalField0172.internalMethod07202(localValue5.x - Math.sin(localValue3) * localValue1, localValue5.y, localValue5.z + Math.cos(localValue3) * localValue1);
   }

   public double internalMethod02497(Vec3d localValue1) {
      return this.internalField0172.internalMethod06297().distanceTo(localValue1);
   }

   public double internalMethod01366(Vec3d localValue1) {
      Vec3d localValue2 = this.internalField0172.internalMethod06297();
      double localValue3 = localValue1.x - localValue2.x;
      double localValue5 = localValue1.z - localValue2.z;
      return Math.sqrt(localValue3 * localValue3 + localValue5 * localValue5);
   }

   public boolean internalMethod00259(long localValue1) {
      return localValue1 - this.internalField0229 >= this.internalField0171.internalMethod09195();
   }

   public void internalMethod00258(long localValue1) {
      this.internalField0229 = localValue1;
   }

   public void internalMethod08828(boolean localValue1) {
      this.internalField0172.internalMethod09022(localValue1);
      if (localValue1) {
         this.internalField1043 = 0.0;
         this.internalField1042 = this.internalField0172.internalMethod02816();
      }
   }

   private void internalMethod09926() {
      if (this.internalField0172.internalMethod09866()) {
         this.internalField1043 = 0.0;
         this.internalField0172.internalMethod09022(false);
      } else if (this.internalField0172.internalMethod09849() && Math.abs(this.internalField1043) <= this.internalField0171.internalMethod09200()) {
         this.internalMethod09928();
      } else {
         Vec3d localValue1 = this.internalField0172.internalMethod06297();
         double localValue2 = localValue1.y + this.internalField1043;
         OptionalDouble localValue4 = this.internalMethod00184(localValue1.x, Math.max(localValue1.y, localValue2), localValue1.z);
         boolean localValue5 = localValue4.isPresent();
         double localValue6 = localValue4.orElse(this.internalField1042);
         if (this.internalField1043 <= 0.0
            && !Double.isNaN(localValue6)
            && localValue2 <= localValue6 + this.internalField0171.internalMethod10061()
            && localValue1.y >= localValue6 - this.internalField0171.internalMethod10061()) {
            this.internalField0172.internalMethod07202(localValue1.x, localValue6, localValue1.z);
            this.internalField0172.internalMethod09022(true);
            this.internalField1043 = 0.0;
            this.internalField1042 = localValue6;
         } else {
            this.internalField0172.internalMethod07202(localValue1.x, localValue2, localValue1.z);
            this.internalField0172.internalMethod09022(false);
            this.internalField1043 = Math.max(
               (this.internalField1043 - this.internalField0171.internalMethod09975()) * this.internalField0171.internalMethod10059(),
               -this.internalField0171.internalMethod10060()
            );
            if (localValue5 && Math.abs(localValue2 - localValue4.getAsDouble()) <= this.internalField0171.internalMethod10061()) {
               this.internalField1042 = localValue4.getAsDouble();
            }
         }
      }
   }

   private void internalMethod09928() {
      Vec3d localValue1 = this.internalField0172.internalMethod06297();
      OptionalDouble localValue2 = this.internalMethod00184(localValue1.x, localValue1.y, localValue1.z);
      if (localValue2.isEmpty()) {
         if (Double.isNaN(this.internalField1042)) {
            this.internalField1042 = localValue1.y;
         }
      } else {
         this.internalField1042 = localValue2.getAsDouble();
         if (Math.abs(localValue1.y - this.internalField1042) <= this.internalField0171.internalMethod10061()) {
            this.internalField0172.internalMethod07202(localValue1.x, this.internalField1042, localValue1.z);
         }
      }
   }

   private OptionalDouble internalMethod00184(double localValue1, double localValue3, double localValue5) {
      int localValue7 = MathHelper.floor(localValue3);
      int localValue8 = MathHelper.floor(localValue3 - this.internalField0171.internalMethod10062());

      for (int localValue9 = localValue7; localValue9 >= localValue8; localValue9--) {
         BlockPos localValue10 = BlockPos.ofFloored(localValue1, localValue9, localValue5);
         if (this.internalField0414.internalMethod08212(localValue10)) {
            return OptionalDouble.of(localValue9 + 1.0);
         }
      }

      return OptionalDouble.empty();
   }

   public BotTargetManager.InternalType0481 internalMethod01916(double localValue1) {
      double localValue3 = Math.max(0.0, localValue1);
      double localValue5 = Math.max(this.internalField0171.internalMethod09934(), this.internalField0171.internalMethod09200());
      Vec3d localValue7 = this.internalMethod01993();
      Vec3d localValue8 = this.internalMethod07160();
      BlockPos localValue9 = BlockPos.ofFloored(localValue7);

      for (double localValue10 = localValue5; localValue10 <= localValue3; localValue10 += localValue5) {
         Vec3d localValue12 = localValue7.add(localValue8.multiply(localValue10));
         BlockPos localValue13 = BlockPos.ofFloored(localValue12);
         if (!localValue13.equals(localValue9)) {
            BlockState localValue14 = this.internalField0414.internalMethod00820(localValue13);
            if (!localValue14.isAir()) {
               Direction localValue15 = this.internalMethod07035(localValue9, localValue13, localValue8);
               return new BotTargetManager.InternalType0481(new BlockHitResult(localValue12, localValue15, localValue13, false), localValue10);
            }

            localValue9 = localValue13;
         }
      }

      return null;
   }

   public BotTargetManager.InternalType0482 internalMethod01917(double localValue1) {
      Vec3d localValue3 = this.internalMethod01993();
      Vec3d localValue4 = this.internalMethod07160();
      double localValue5 = this.internalField0171.internalMethod09935();
      long localValue7 = this.internalField0414.internalMethod08714();
      BotTargetManager.InternalType0482 localValue9 = null;

      for (WorldSnapshotTracker.InternalType0205 localValue11 : this.internalField0414.internalMethod03121()) {
         if (localValue11.internalMethod05993() != this.internalField0414.internalMethod08709()
            && localValue11.internalMethod05993() != this.internalField0172.internalMethod08592()
            && localValue7 - localValue11.internalMethod05994() <= this.internalField0171.internalMethod07715()) {
            Vec3d localValue12 = localValue11.internalMethod02495().add(0.0, this.internalField0171.internalMethod09937(), 0.0);
            Vec3d localValue13 = localValue12.subtract(localValue3);
            double localValue14 = localValue13.dotProduct(localValue4);
            if (!(localValue14 < 0.0) && !(localValue14 > localValue1)) {
               Vec3d localValue16 = localValue3.add(localValue4.multiply(localValue14));
               if (!(localValue12.distanceTo(localValue16) > localValue5) && (localValue9 == null || localValue14 < localValue9.internalMethod06938())) {
                  localValue9 = new BotTargetManager.InternalType0482(localValue11, localValue14);
               }
            }
         }
      }

      return localValue9;
   }

   public Vec3d internalMethod01993() {
      return this.internalField0172.internalMethod06297().add(0.0, this.internalField0171.internalMethod10123(), 0.0);
   }

   public Vec3d internalMethod07160() {
      return Vec3d.fromPolar(this.internalField0172.internalMethod02817(), this.internalField0172.internalMethod02808()).normalize();
   }

   public boolean internalMethod01197(Vec3d localValue1, double localValue2) {
      if (localValue1 == null) {
         return false;
      } else {
         Vec3d localValue4 = this.internalMethod01993();
         double localValue5 = localValue1.x - localValue4.x;
         double localValue7 = localValue1.y - localValue4.y;
         double localValue9 = localValue1.z - localValue4.z;
         double localValue11 = Math.sqrt(localValue5 * localValue5 + localValue9 * localValue9);
         float localValue13 = (float)Math.toDegrees(Math.atan2(-localValue5, localValue9));
         float localValue14 = (float)Math.toDegrees(-Math.atan2(localValue7, localValue11));
         double localValue15 = Math.abs(MathHelper.wrapDegrees(localValue13 - this.internalField0172.internalMethod02808()));
         double localValue17 = Math.abs(localValue14 - this.internalField0172.internalMethod02817());
         return localValue15 <= localValue2 && localValue17 <= localValue2;
      }
   }

   public Direction internalMethod00513(BlockPos localValue1) {
      if (localValue1 == null) {
         return Direction.UP;
      } else {
         Vec3d localValue2 = this.internalMethod01993();
         Vec3d localValue3 = localValue1.toCenterPos();
         return Direction.getFacing(localValue2.x - localValue3.x, localValue2.y - localValue3.y, localValue2.z - localValue3.z);
      }
   }

   private Direction internalMethod07035(BlockPos localValue1, BlockPos localValue2, Vec3d localValue3) {
      int localValue4 = localValue2.getX() - localValue1.getX();
      int localValue5 = localValue2.getY() - localValue1.getY();
      int localValue6 = localValue2.getZ() - localValue1.getZ();
      if (localValue4 > 0) {
         return Direction.WEST;
      } else if (localValue4 < 0) {
         return Direction.EAST;
      } else if (localValue5 > 0) {
         return Direction.DOWN;
      } else if (localValue5 < 0) {
         return Direction.UP;
      } else if (localValue6 > 0) {
         return Direction.NORTH;
      } else {
         return localValue6 < 0 ? Direction.SOUTH : Direction.getFacing(-localValue3.x, -localValue3.y, -localValue3.z);
      }
   }

   private Entity internalMethod01876(int localValue1) {
      MinecraftClient localValue2 = MinecraftClient.getInstance();
      if (localValue2.world == null) {
         return null;
      } else {
         Entity localValue3 = localValue2.world.getEntityById(localValue1);
         if (localValue3 != null) {
            return localValue3;
         } else {
            UUID localValue4 = UUID.nameUUIDFromBytes(("RockstarBotTarget:" + this.internalField0248 + ":" + localValue1).getBytes(StandardCharsets.UTF_8));
            OtherClientPlayerEntity localValue5 = new OtherClientPlayerEntity(localValue2.world, new GameProfile(localValue4, "BotTarget"));
            localValue5.setId(localValue1);
            return localValue5;
         }
      }
   }

   private void internalMethod07656(Mode localValue1) {
      if (localValue1 != null && this.internalField0172.internalMethod08592() >= 0) {
         Entity localValue2 = this.internalMethod01876(this.internalField0172.internalMethod08592());
         if (localValue2 != null) {
            this.internalField0713.internalMethod07163(new ClientCommandC2SPacket(localValue2, localValue1));
         }
      }
   }

   private void internalMethod07655() {
      this.internalField0713.internalMethod07163(
         new net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket(
            new net.minecraft.util.PlayerInput(false, false, false, false, false, this.internalField0172.internalMethod08608(), this.internalField0172.internalMethod08612())
         )
      );
   }

   private int internalMethod07729() {
      return this.internalField1055++;
   }

   private double internalMethod05388(double localValue1, double localValue3, double localValue5) {
      return localValue1 < localValue3 ? Math.min(localValue1 + localValue5, localValue3) : Math.max(localValue1 - localValue5, localValue3);
   }

   private float internalMethod05392(float localValue1, float localValue2, float localValue3) {
      float localValue4 = MathHelper.wrapDegrees(localValue2 - localValue1);
      float localValue5 = MathHelper.clamp(localValue4, -localValue3, localValue3);
      return MathHelper.wrapDegrees(localValue1 + localValue5);
   }

   public boolean internalMethod07697() {
      return this.internalField0713.internalMethod06539();
   }

   public BotState internalMethod04051() {
      return this.internalField0713.internalMethod07176();
   }

   @Generated
   public String internalMethod03426() {
      return this.internalField0248;
   }

   @Generated
   public OfflineBotConnection internalMethod04050() {
      return this.internalField0713;
   }

   @Generated
   public BotPlayerState internalMethod06688() {
      return this.internalField0172;
   }

   @Generated
   public WorldSnapshotTracker internalMethod00273() {
      return this.internalField0414;
   }

   @Generated
   public BotPlayerController internalMethod00272() {
      return this.internalField0413;
   }

   @Generated
   public BotConfig internalMethod06687() {
      return this.internalField0171;
   }

   @Generated
   public BotPlayPacketHandler internalMethod06686() {
      return this.internalField0170;
   }

   @Generated
   public BotBehavior internalMethod06738() {
      return this.internalField0182;
   }

   @Generated
   public boolean internalMethod07731() {
      return this.internalField0277;
   }

   @Generated
   public int internalMethod04570() {
      return this.internalField0227;
   }

   @Generated
   public boolean internalMethod07734() {
      return this.internalField0276;
   }

   @Generated
   public String internalMethod06925() {
      return this.internalField0247;
   }

   @Generated
   public int internalMethod04577() {
      return this.internalField0228;
   }

   @Generated
   public int internalMethod07690() {
      return this.internalField1053;
   }

   @Generated
   public double internalMethod04569() {
      return this.internalField0194;
   }

   @Generated
   public double internalMethod04576() {
      return this.internalField0193;
   }

   @Generated
   public double internalMethod07689() {
      return this.internalField1045;
   }

   @Generated
   public double internalMethod07694() {
      return this.internalField1043;
   }

   @Generated
   public double internalMethod07728() {
      return this.internalField1042;
   }

   @Generated
   public long internalMethod04571() {
      return this.internalField0229;
   }

   @Generated
   public BlockPos internalMethod02736() {
      return this.internalField0352;
   }

   @Generated
   public Direction internalMethod00439() {
      return this.internalField0150;
   }

   @Generated
   public double internalMethod07732() {
      return this.internalField1044;
   }

   @Generated
   public long internalMethod04578() {
      return this.internalField0230;
   }

   @Generated
   public long internalMethod07691() {
      return this.internalField1059;
   }

   @Generated
   public int internalMethod07695() {
      return this.internalField1055;
   }

   @Generated
   public void internalMethod00501(BotConfig localValue1) {
      this.internalField0171 = localValue1;
   }

   @Generated
   public void internalMethod00500(BotPlayPacketHandler localValue1) {
      this.internalField0170 = localValue1;
   }

   @Generated
   public void internalMethod09131(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   @Generated
   public void internalMethod08813(int localValue1) {
      this.internalField0227 = localValue1;
   }

   public static final class InternalType0481 {
      private final BlockHitResult internalField0246;
      private final double internalField0194;

      public InternalType0481(BlockHitResult localValue1, double localValue2) {
         this.internalField0246 = localValue1;
         this.internalField0194 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0481[hitResult=" + this.internalField0246 + ", distance=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0246);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BotTargetManager.InternalType0481 other = (BotTargetManager.InternalType0481) localValue1;
         return java.util.Objects.equals(this.internalField0246, other.internalField0246)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public BlockHitResult internalMethod04871() {
         return this.internalField0246;
      }

      public double internalMethod05299() {
         return this.internalField0194;
      }
   }

   public static final class InternalType0482 {
      private final WorldSnapshotTracker.InternalType0205 internalField0327;
      private final double internalField0194;

      public InternalType0482(WorldSnapshotTracker.InternalType0205 localValue1, double localValue2) {
         this.internalField0327 = localValue1;
         this.internalField0194 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0482[snapshot=" + this.internalField0327 + ", distance=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0327);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BotTargetManager.InternalType0482 other = (BotTargetManager.InternalType0482) localValue1;
         return java.util.Objects.equals(this.internalField0327, other.internalField0327)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public WorldSnapshotTracker.InternalType0205 internalMethod02460() {
         return this.internalField0327;
      }

      public double internalMethod06938() {
         return this.internalField0194;
      }
   }
}
