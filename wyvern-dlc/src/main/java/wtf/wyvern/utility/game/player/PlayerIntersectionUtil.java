package wtf.wyvern.utility.game.player;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.utility.game.other.NetworkUtils;
import wtf.wyvern.utility.interfaces.IClient;
import wtf.wyvern.render.display.base.color.ColorUtil;
import wtf.astroguard.J2C.FastNative;

public final class PlayerIntersectionUtil implements IClient {
   @FastNative
   public static void clickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player) {
      ScreenHandler screenHandler = player.currentScreenHandler;
      if (syncId != screenHandler.syncId) {

      } else {
         DefaultedList<Slot> defaultedList = screenHandler.slots;
         int i = defaultedList.size();
         List<ItemStack> list = Lists.newArrayListWithCapacity(i);
         Iterator var10 = defaultedList.iterator();

         while(var10.hasNext()) {
            Slot slot = (Slot)var10.next();
            list.add(slot.getStack().copy());
         }

         screenHandler.onSlotClick(slotId, button, actionType, player);
         Int2ObjectMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap();

         for(int j = 0; j < i; ++j) {
            ItemStack itemStack = (ItemStack)list.get(j);
            ItemStack itemStack2 = ((Slot)defaultedList.get(j)).getStack();
            if (!ItemStack.areEqual(itemStack, itemStack2)) {
               int2ObjectMap.put(j, itemStack2.copy());
            }
         }

         NetworkUtils.sendSilentPacket(new ClickSlotC2SPacket(syncId, screenHandler.getRevision(), slotId, button, actionType, screenHandler.getCursorStack().copy(), int2ObjectMap));
      }

   }

   @FastNative
   public static void sendSequencedPacket(SequencedPacketCreator packetCreator) {
      mc.interactionManager.sendSequencedPacket(mc.world, packetCreator);
   }

   @FastNative
   public static void startFallFlying() {
      mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_FALL_FLYING));
      mc.player.startGliding();
   }

   @FastNative
   public static void sendPacketWithOutEvent(Packet<?> packet) {
      mc.getNetworkHandler().getConnection().send(packet, (PacketCallbacks)null);
   }

   @FastNative
   public static List<BlockPos> getCube(BlockPos center, float radius) {
      return getCube(center, radius, radius, true);
   }

   @FastNative
   public static List<BlockPos> getCube(BlockPos center, float radiusXZ, float radiusY) {
      return getCube(center, radiusXZ, radiusY, true);
   }

   @FastNative
   public static List<BlockPos> getCube(BlockPos center, float radiusXZ, float radiusY, boolean down) {
      List<BlockPos> positions = new ArrayList();
      int centerX = center.getX();
      int centerY = center.getY();
      int centerZ = center.getZ();
      int posY = down ? centerY - (int)radiusY : centerY;

      for(int x = centerX - (int)radiusXZ; (float)x <= (float)centerX + radiusXZ; ++x) {
         for(int z = centerZ - (int)radiusXZ; (float)z <= (float)centerZ + radiusXZ; ++z) {
            for(int y = posY; (float)y <= (float)centerY + radiusY; ++y) {
               positions.add(new BlockPos(x, y, z));
            }
         }
      }

      return positions;
   }

   @FastNative
   public static List<BlockPos> getCube(BlockPos start, BlockPos end) {
      List<BlockPos> positions = new ArrayList();

      for(int x = start.getX(); x <= end.getX(); ++x) {
         for(int z = start.getZ(); z <= end.getZ(); ++z) {
            for(int y = start.getY(); y <= end.getY(); ++y) {
               positions.add(new BlockPos(x, y, z));
            }
         }
      }

      return positions;
   }

   @FastNative
   public static Type getKeyType(int key) {
      return key < 8 ? Type.MOUSE : Type.KEYSYM;
   }

   @FastNative
   public static Stream<Entity> streamEntities() {
      return StreamSupport.stream(mc.world.getEntities().spliterator(), false);
   }

   @FastNative
   public static boolean canChangeIntoPose(EntityPose pose) {
      return mc.player.getWorld().isSpaceEmpty(mc.player, mc.player.getDimensions(pose).getBoxAt(mc.player.getPos()).contract(1.0E-7D));
   }

   @FastNative
   public static boolean isPotionActive(RegistryEntry<StatusEffect> statusEffect) {
      return mc.player.getActiveStatusEffects().containsKey(statusEffect);
   }

   @FastNative
   public static boolean isPlayerInBlock(Block block) {
      return isBoxInBlock(mc.player.getBoundingBox().expand(-0.001D), block);
   }

   public static boolean isBoxInBlock(Box box, Block block) {
      return isBox(box, (pos) -> {
         return mc.world.getBlockState(pos).getBlock().equals(block);
      });
   }

   public static boolean isBoxInBlocks(Box box, List<Block> blocks) {
      return isBox(box, (pos) -> {
         return blocks.contains(mc.world.getBlockState(pos).getBlock());
      });
   }

   @FastNative
   public static boolean isBox(Box box, Predicate<BlockPos> pos) {
      return BlockPos.stream(box).anyMatch(pos);
   }

   @FastNative
   public static boolean isKey(Key key) {
      return isKey(key.getCategory(), key.getCode());
   }

   @FastNative
   public static boolean isKey(BindSetting setting) {
      int key = setting.getKeyCode();
      return mc.currentScreen == null && setting.isVisible() && isKey(getKeyType(key), key);
   }

   @FastNative
   public static boolean isKey(Type type, int keyCode) {
      if (keyCode != -1) {
         switch(type) {
         case KEYSYM:
            return GLFW.glfwGetKey(mc.getWindow().getHandle(), keyCode) == 1;
         case MOUSE:
            return GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), keyCode) == 1;
         }
      }

      return false;
   }

   @FastNative
   public static boolean isAir(BlockPos blockPos) {
      return isAir(mc.world.getBlockState(blockPos));
   }

   @FastNative
   public static boolean isAir(BlockState state) {
      return state.isAir() || state.getBlock().equals(Blocks.CAVE_AIR) || state.getBlock().equals(Blocks.VOID_AIR);
   }

   @FastNative
   public static boolean isChat(Screen screen) {
      return screen instanceof ChatScreen;
   }

   @FastNative
   public static boolean nullCheck() {
      return mc.player == null || mc.world == null;
   }

   @FastNative
   public static void useItem(Hand hand) {
      mc.interactionManager.interactItem(mc.player, hand);
   }

   /**
    * The scoreboard text-format + regex-strip + parse pipeline used to run per player
    * per frame (ESP tags, TargetHUD). The underlying score is an int that changes only
    * on server updates, so the parsed value is memoized per player keyed by the raw
    * score and the objective identity - identical inputs always produce identical
    * output, so behavior is unchanged.
    */
   private static final java.util.Map<java.util.UUID, ScoreHealthCache> SCORE_HEALTH_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

   private record ScoreHealthCache(ScoreboardObjective objective, int rawScore, float parsed, boolean valid) {
   }

   @FastNative
   public static float getHealth(LivingEntity entity) {
      float hp = entity.getHealth() + entity.getAbsorptionAmount();
      if (entity instanceof PlayerEntity) {
         PlayerEntity player = (PlayerEntity)entity;
         ScoreboardObjective scoreBoard = player.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
         if (scoreBoard != null) {
            var scoreAccess = player.getScoreboard().getScore(player, scoreBoard);
            // getScore may return null; the vanilla formatter accepts null (formats 0),
            // so mirror that with a sentinel raw value instead of dereferencing.
            int rawScore = scoreAccess != null ? scoreAccess.getScore() : Integer.MIN_VALUE;
            ScoreHealthCache cached = SCORE_HEALTH_CACHE.get(player.getUuid());
            if (cached == null || cached.objective() != scoreBoard || cached.rawScore() != rawScore) {
               float parsed = hp;
               boolean valid = false;
               MutableText text2 = ReadableScoreboardScore.getFormattedScore(scoreAccess, scoreBoard.getNumberFormatOr(StyledNumberFormat.EMPTY));
               try {
                  parsed = Float.parseFloat(ColorUtil.removeFormatting(text2.getString()));
                  valid = true;
               } catch (NumberFormatException var6) {
               }

               if (SCORE_HEALTH_CACHE.size() > 512) {
                  SCORE_HEALTH_CACHE.clear();
               }
               cached = new ScoreHealthCache(scoreBoard, rawScore, parsed, valid);
               SCORE_HEALTH_CACHE.put(player.getUuid(), cached);
            }
            if (cached.valid()) {
               hp = cached.parsed();
            }
         }
      }

      return MathHelper.clamp(hp, 0.0F, entity.getMaxHealth());
   }

   @FastNative
   public static String getHealthString(LivingEntity entity) {
      return getHealthString(getHealth(entity));
   }

   @FastNative
   public static String getHealthString(float hp) {
      return String.format("%.1f", hp).replace(",", ".").replace(".0", "");
   }

   @Generated
   private PlayerIntersectionUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
