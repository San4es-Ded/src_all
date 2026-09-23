package aethereal.module.misc;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.module.misc.autoend.AutoEndNavigator;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_10192;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1304;
import net.minecraft.class_1542;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1743;
import net.minecraft.class_1753;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1821;
import net.minecraft.class_1826;
import net.minecraft.class_1844;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2551;
import net.minecraft.class_2625;
import net.minecraft.class_2680;
import net.minecraft.class_2745;
import net.minecraft.class_2818;
import net.minecraft.class_345;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4174;
import net.minecraft.class_418;
import net.minecraft.class_490;
import net.minecraft.class_6880;
import net.minecraft.class_7439;
import net.minecraft.class_7715;
import net.minecraft.class_7923;
import net.minecraft.class_8242;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import platform.inject.accessors.BossBarHudAccessor;

@ModuleRegister(
   a = "Auto End",
   b = "Фармит Энд: бьёт горшки, чистит подозрительный песок, сдаёт лут домой и берёт снабжение",
   c = Category.Misc
)
public class AutoEnd extends Module implements Interface {
   private static final double REACH_SQ = 18.0625;
   private static final double MAX_TARGET_DISTANCE_SQ = 22500.0;
   private static final int SCAN_CHUNKS_PER_TICK = 24;
   private static final int SKIP_TARGET_TICKS = 1200;
   private static final Pattern PVP_SECONDS = Pattern.compile("(\\d+(?:[.,]\\d+)?)\\s*(?:сек|sec|s)", 66);
   private final SliderSetting b = new SliderSetting("Горшков до дома", 5.0F, 1.0F, 64.0F, 1.0F);
   private final BooleanSetting c = new BooleanSetting("Авто-снабжение", true);
   private final StringSetting d2 = new StringSetting("Табличка снабжения", "снабжение").a(() -> this.c.c());
   private final BooleanSetting e = new BooleanSetting("Брать еду", true).a(() -> this.c.c());
   private final SliderSetting f2 = new SliderSetting("Количество еды", 16.0F, 1.0F, 64.0F, 1.0F).a(() -> this.c.c() && this.e.c());
   private final BooleanSetting g2 = new BooleanSetting("Брать невидимость", true).a(() -> this.c.c());
   private final SliderSetting h2 = new SliderSetting("Зелий невидимости", 3.0F, 1.0F, 16.0F, 1.0F).a(() -> this.c.c() && this.g2.c());
   private final BooleanSetting i2 = new BooleanSetting("Брать скорость", true).a(() -> this.c.c());
   private final SliderSetting j2 = new SliderSetting("Зелий скорости", 3.0F, 1.0F, 16.0F, 1.0F).a(() -> this.c.c() && this.i2.c());
   private final BooleanSetting k2 = new BooleanSetting("Показывать путь", true);
   private final AutoEndNavigator navigator = new AutoEndNavigator();
   private class_2338 targetBlock;
   private boolean targetBrushable;
   private boolean breakingStarted;
   private boolean brushingStarted;
   private boolean pathRequested;
   private int scanDelay;
   private int repathDelay;
   private int aimTicks;
   private int brushUseCooldown;
   private int previousSelectedSlot = -1;
   private int brushInventorySlot = -1;
   private int brushHotbarSlot = -1;
   private boolean brushMovedToHotbar;
   private final Map<class_2338, Integer> skippedTargets = new HashMap<>();
   private class_2338 patrolGoal;
   private class_2338 lootOrigin;
   private class_2338 lootGoal;
   private int lootTicks;
   private int lootEmptyTicks;
   private boolean lootFromPot;
   private final class_1799[] lootHotbarSnapshot = new class_1799[9];
   private final Set<Integer> pickedHotbarSlots = new HashSet<>();
   private boolean hotbarCleanupPending;
   private boolean automationInventoryOpened;
   private int hotbarCleanupDelay;
   private int discardCooldown;
   private int discardSettleTicks;
   private boolean discardStopping;
   private class_2338 aimBlock;
   private boolean aimInitialized;
   private int aimProfileTicks;
   private float aimYawVelocity;
   private float aimPitchVelocity;
   private float aimYawSpeed = 12.0F;
   private float aimPitchSpeed = 9.0F;
   private float aimYawSpeedTarget = 12.0F;
   private float aimPitchSpeedTarget = 9.0F;
   private double aimOffsetX;
   private double aimOffsetY;
   private double aimOffsetZ;
   private double aimOffsetXTarget;
   private double aimOffsetYTarget;
   private double aimOffsetZTarget;
   private int brokenPots;
   private boolean returnHomePending;
   private boolean deathRecoveryPending;
   private boolean deathRecoveryActive;
   private int initialPortalCheckTicks;
   private AutoEnd.Cycle cycle = AutoEnd.Cycle.FARMING;
   private int cycleTicks;
   private int actionDelay;
   private class_2338 homeChest;
   private class_2338 supplyChest;
   private class_2338 portalTarget;
   private class_2338 portalWalkGoal;
   private int depositSlot;
   private boolean automationContainerOpened;
   private int automationContainerOpenTicks;
   private final Set<class_2338> exhaustedHomeChests = new HashSet<>();
   private final Set<class_2338> exhaustedSupplyChests = new HashSet<>();
   private final EnumSet<AutoEnd.Supply> unavailableSupplies = EnumSet.noneOf(AutoEnd.Supply.class);
   private class_243 homeCommandPos;
   private class_1937 homeCommandWorld;
   private volatile boolean homeTeleportConfirmed;
   private class_243 darenaCommandPos;
   private class_1937 darenaCommandWorld;
   private int darenaClickCooldown;
   private int pvpClearTicks;
   private class_243 portalEntryPos;
   private class_1937 portalEntryWorld;
   private int screenResumeDelay;
   private AutoEnd.Supply activeConsumable;
   private int consumablePreviousSlot = -1;
   private int consumableHotbarSlot = -1;
   private int consumableUseDelay;
   private int consumableTicks;
   private int consumableInteractCooldown;
   private int consumableReleaseTicks;
   private class_2338 consumableAvoidChest;
   private int scanCenterChunkX = Integer.MIN_VALUE;
   private int scanCenterChunkZ = Integer.MIN_VALUE;
   private int scanRadius = -1;
   private int scanCursor;
   private double scanNearestDistance = 22500.0;
   private AutoEnd.TargetSelection scanNearest;
   private boolean scanCanBrush;

   public AutoEnd() {
      this.a(new Setting[]{this.b, this.c, this.d2, this.e, this.f2, this.g2, this.h2, this.i2, this.j2, this.k2});
   }

   @Override
   public void b() {
      this.screenResumeDelay = 0;
      this.clearHotbarState();
      this.clearTarget(false);
      this.clearLoot();
      this.resetCycle();
      this.initialPortalCheckTicks = 100;
      this.skippedTargets.clear();
      this.scanDelay = 0;
      this.resetScan();
      this.navigator.stop();
      super.b();
   }

   @Override
   public void c() {
      this.screenResumeDelay = 0;
      this.stopConsumable();
      this.closeAutomationInventory();
      this.clearHotbarState();
      this.closeAutomationContainer();
      this.releasePortalKeys();
      this.stopNavigation();
      this.clearTarget(false);
      this.clearLoot();
      this.skippedTargets.clear();
      this.resetScan();
      if (aM_.field_1690 != null) {
         aM_.field_1690.field_1904.method_23481(false);
      }

      super.c();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         boolean dead = aM_.field_1755 instanceof class_418
            || aM_.field_1724.method_29504()
            || aM_.field_1724.method_6032() <= 0.0F
            || aM_.field_1724.field_6213 > 0;
         if (dead) {
            this.onDeath();
         } else if (aM_.field_1761 != null) {
            if (this.deathRecoveryPending) {
               this.recoverFromDeath();
            } else if (!this.emergencyVoidEscape()) {
               if (!this.closeTimedOutContainer()) {
                  if (this.cycle == AutoEnd.Cycle.FARMING && this.initialPortalCheckTicks > 0 && aM_.field_1687.method_27983().equals(class_1937.field_25179)) {
                     this.initialPortalCheckTicks--;
                     if (this.initialPortalCheckTicks % 10 == 0 && this.findPortal() != null) {
                        this.initialPortalCheckTicks = 0;
                        this.stopNavigation();
                        this.releasePortalKeys();
                        if (this.needsSupplies()) {
                           this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
                           this.pathRequested = false;
                           this.repathDelay = 0;
                        } else {
                           this.portalTarget = null;
                           this.portalWalkGoal = null;
                           this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
                        }
                     }
                  } else {
                     this.initialPortalCheckTicks = 0;
                  }

                  if (this.scanDelay > 0) {
                     this.scanDelay--;
                  }

                  if (this.repathDelay > 0) {
                     this.repathDelay--;
                  }

                  if (this.brushUseCooldown > 0) {
                     this.brushUseCooldown--;
                  }

                  if (this.actionDelay > 0) {
                     this.actionDelay--;
                  }

                  if (this.darenaClickCooldown > 0) {
                     this.darenaClickCooldown--;
                  }

                  this.tickSkippedTargets();
                  this.trackPickedHotbar();
                  if (!this.discardJunkImmediately() && !this.cleanupHotbar()) {
                     this.keepEmptyHand();
                     if (aM_.field_1755 != null) {
                        if (this.activeConsumable != null) {
                           this.stopConsumable();
                        }

                        this.pauseMovement();
                        if (!this.automationContainerOpened && !this.isAutomationContainerState()) {
                           this.screenResumeDelay = 3;
                           return;
                        }
                     } else if (this.screenResumeDelay > 0) {
                        this.screenResumeDelay--;
                        this.pauseMovement();
                        return;
                     }

                     boolean allowPotions = this.cycle == AutoEnd.Cycle.FARMING || this.cycle == AutoEnd.Cycle.FIND_PORTAL;
                     if (this.canUseConsumables() && this.tickConsumables(allowPotions)) {
                        this.pauseMovement();
                     } else {
                        if (aM_.field_1755 == null && this.cycle != AutoEnd.Cycle.ENTER_PORTAL) {
                           this.navigator.tick();
                        }

                        this.checkNavigatorFailure();
                        if (this.cycle != AutoEnd.Cycle.FARMING) {
                           this.tickCycle();
                        } else {
                           this.tickFarming();
                        }
                     }
                  } else {
                     this.pauseMovement();
                  }
               }
            }
         }
      }
   }

   private void tickFarming() {
      if (this.lootOrigin != null) {
         this.tickLoot();
      } else if (this.targetBlock != null && this.targetBrushable && this.brushingStarted && !this.isBrushable(this.targetBlock)) {
         class_2338 source = this.targetBlock.method_10062();
         this.stopBrushing();
         this.startLoot(source, false);
      } else if (this.targetBlock != null && !this.targetBrushable && !this.isPot(this.targetBlock)) {
         this.startLoot(this.targetBlock.method_10062(), true);
      } else {
         if (!this.targetStillThere()) {
            if (this.targetBlock != null) {
               this.clearTarget(true);
            }

            if (this.scanDelay > 0) {
               this.patrol();
               return;
            }

            AutoEnd.ScanResult result = this.scanTargets();
            this.scanDelay = 1;
            if (!result.complete() || result.selection() == null) {
               this.patrol();
               return;
            }

            this.clearPatrol();
            this.resetScan();
            this.targetBlock = result.selection().pos();
            this.targetBrushable = result.selection().brushable();
            boolean needsRoute = this.targetBrushable
               ? !this.standingOnBrushable(this.targetBlock)
               : !this.inReach(this.targetBlock) || !this.visible(this.targetBlock);
            if (needsRoute) {
               this.requestTargetRoute(true);
               return;
            }
         }

         if (this.targetBrushable && !this.standingOnBrushable(this.targetBlock)) {
            if (this.brushingStarted) {
               this.stopBrushing();
            }

            this.breakingStarted = false;
            this.aimTicks = 0;
            this.requestTargetRoute(false);
         } else if (!this.inReach(this.targetBlock)) {
            if (this.brushingStarted) {
               this.stopBrushing();
            }

            this.breakingStarted = false;
            this.aimTicks = 0;
            this.requestTargetRoute(false);
         } else {
            boolean navWorking = this.navigator.isWorking();
            if (this.targetBrushable) {
               if (this.pathRequested || navWorking) {
                  this.stopNavigation();
               }
            } else if (this.pathRequested) {
               if (navWorking) {
                  return;
               }

               if (!this.canSeeFrom(aM_.field_1724.method_24515(), this.targetBlock)) {
                  this.skippedTargets.put(this.targetBlock.method_10062(), 1200);
                  this.stopNavigation();
                  this.clearTarget(false);
                  this.scanDelay = 1;
                  return;
               }

               this.stopNavigation();
            }

            if (!this.visible(this.targetBlock)) {
               this.breakingStarted = false;
               this.aimTicks = 0;
               if (this.brushingStarted) {
                  this.stopBrushing();
               }

               if (!this.pathRequested && !this.navigator.isWorking()) {
                  this.requestTargetRoute(true);
               }
            } else {
               boolean aimed = this.preciseAim(this.targetBlock);
               this.aimTicks++;
               if (aimed || this.aimTicks >= 12) {
                  if (this.aimTicks >= 2) {
                     if (this.targetBrushable) {
                        this.brush();
                     } else if (!this.crosshairOn(this.targetBlock)) {
                        this.breakingStarted = false;
                        this.aimTicks = 0;
                     } else {
                        this.breakingStarted = true;
                        this.breakPot();
                     }
                  }
               }
            }
         }
      }
   }

   private void breakPot() {
      if (aM_.field_1765 instanceof class_3965 hit && hit.method_17777().equals(this.targetBlock)) {
         if (!aM_.field_1724.method_6115()) {
            aM_.field_1761.method_2902(hit.method_17777(), hit.method_17780());
            aM_.field_1724.method_6104(class_1268.field_5808);
         }
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.activeConsumable != null || this.discardStopping || this.screenResumeDelay > 0 || aM_.field_1755 != null) {
         event.a(0.0F);
         event.b(0.0F);
         event.b(false);
         if (aM_.field_1724 != null) {
            aM_.field_1724.method_5728(false);
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_7439 packet) {
         String text = packet.comp_763().getString();
         if (text != null && !text.isBlank()) {
            if (this.isHomeMessage(text.toLowerCase(Locale.ROOT))) {
               this.homeTeleportConfirmed = true;
            }
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && this.k2.c() && aM_.field_1724 != null) {
         List<class_2338> path = this.navigator.getRemainingPath();
         if (!path.isEmpty()) {
            int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
            class_243 previous = aM_.field_1724.method_19538().method_1031(0.0, 0.12, 0.0);

            for (int index = 0; index < path.size(); index++) {
               class_2338 node = path.get(index);
               class_243 point = new class_243(node.method_10263() + 0.5, node.method_10264() + 0.12, node.method_10260() + 0.5);
               class_243 middle = previous.method_1019(point).method_1021(0.5);
               event.e().a(event.h(), previous, point, middle, ColorUtil.a(accent, 200), 2.0F);
               previous = point;
            }

            class_2338 goal = path.getLast();
            class_238 goalBox = new class_238(
               goal.method_10263() + 0.16,
               goal.method_10264() + 0.03,
               goal.method_10260() + 0.16,
               goal.method_10263() + 0.84,
               goal.method_10264() + 0.72,
               goal.method_10260() + 0.84
            );
            event.e().a(event.h(), goalBox, ColorUtil.a(255, 210, 60, 220), 2.0F);
         }
      }
   }

   private void pauseMovement() {
      this.navigator.pauseMovement();
      if (aM_.field_1724 != null) {
         aM_.field_1724.method_5728(false);
      }
   }

   private void onDeath() {
      if (!this.deathRecoveryPending) {
         this.deathRecoveryPending = true;
         this.deathRecoveryActive = false;
         this.returnHomePending = false;
         this.stopNavigation();
         this.patrolGoal = null;
         this.clearTarget(false);
         this.clearLoot();
         this.releasePortalKeys();
         this.closeAutomationContainer();
      }
   }

   private void recoverFromDeath() {
      this.stopConsumable();
      this.stopNavigation();
      this.patrolGoal = null;
      this.clearTarget(false);
      this.clearLoot();
      this.releasePortalKeys();
      this.closeAutomationContainer();
      this.clearReturnState();
      this.brokenPots = 0;
      this.returnHomePending = false;
      this.deathRecoveryPending = false;
      this.deathRecoveryActive = true;
      this.sendHome();
      this.setCycle(AutoEnd.Cycle.HOME_WAIT);
   }

   private boolean emergencyVoidEscape() {
      if (this.cycle != AutoEnd.Cycle.HOME_WAIT && !aM_.field_1687.method_27983().equals(class_1937.field_25179)) {
         int y = class_3532.method_15357(aM_.field_1724.method_23318());
         if (y >= 2 && y <= 6) {
            this.stopConsumable();
            this.stopNavigation();
            this.patrolGoal = null;
            this.clearTarget(false);
            this.clearLoot();
            this.clearHotbarState();
            this.releasePortalKeys();
            this.closeAutomationInventory();
            this.closeAutomationContainer();
            this.clearReturnState();
            this.deathRecoveryPending = false;
            this.deathRecoveryActive = false;
            this.returnHomePending = false;
            this.sendHome();
            this.setCycle(AutoEnd.Cycle.HOME_WAIT);
            this.pauseMovement();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void tickCycle() {
      this.cycleTicks++;
      switch (this.cycle) {
         case PVP_WAIT:
            this.tickPvpWait();
            break;
         case DARENA_WAIT:
            this.tickDarenaWait();
            break;
         case HOME_WAIT:
            this.tickHomeWait();
            break;
         case FIND_CHEST:
            this.tickFindChest();
            break;
         case WALK_CHEST:
            this.tickWalkChest();
            break;
         case OPEN_CHEST:
            this.tickOpenChest();
            break;
         case DEPOSIT:
            this.tickDeposit();
            break;
         case FIND_SUPPLY:
            this.tickFindSupply();
            break;
         case WALK_SUPPLY:
            this.tickWalkSupply();
            break;
         case OPEN_SUPPLY:
            this.tickOpenSupply();
            break;
         case TAKE_SUPPLY:
            this.tickTakeSupply();
            break;
         case FIND_PORTAL:
            this.tickFindPortal();
            break;
         case WALK_PORTAL:
            this.tickWalkPortal();
            break;
         case ENTER_PORTAL:
            this.tickEnterPortal();
      }
   }

   private void setCycle(AutoEnd.Cycle next) {
      this.cycle = next;
      this.cycleTicks = 0;
      this.scanDelay = 0;
   }

   private void beginReturnHome() {
      this.stopConsumable();
      this.stopNavigation();
      this.patrolGoal = null;
      this.clearTarget(false);
      this.releasePortalKeys();
      this.closeAutomationContainer();
      this.clearReturnState();
      this.homeCommandPos = null;
      this.homeCommandWorld = null;
      this.homeTeleportConfirmed = false;
      this.actionDelay = 0;
      this.setCycle(AutoEnd.Cycle.PVP_WAIT);
   }

   private void tickPvpWait() {
      if (this.pvpActive()) {
         this.pvpClearTicks = 0;
         this.patrol();
      } else if (++this.pvpClearTicks < 3) {
         this.patrol();
      } else {
         this.clearPatrol();
         this.darenaCommandPos = this.playerPos();
         this.darenaCommandWorld = aM_.field_1687;
         aM_.field_1724.field_3944.method_45730("darena");
         this.setCycle(AutoEnd.Cycle.DARENA_WAIT);
      }
   }

   private void tickDarenaWait() {
      this.stopNavigation();
      if (aM_.field_1724.field_7512 instanceof class_1707 handler) {
         if (this.darenaClickCooldown <= 0 && this.clickDarenaMenu(handler)) {
            this.darenaClickCooldown = 20;
         }
      } else {
         boolean moved = this.darenaCommandWorld != null && aM_.field_1687 != this.darenaCommandWorld;
         if (!moved && this.darenaCommandPos != null) {
            moved = this.playerPos().method_1025(this.darenaCommandPos) > 64.0;
         }

         if (moved) {
            this.sendHome();
            this.cycle = AutoEnd.Cycle.HOME_WAIT;
            this.cycleTicks = 0;
         } else {
            if (this.cycleTicks > 0 && this.cycleTicks % 60 == 0) {
               this.darenaCommandPos = this.playerPos();
               this.darenaCommandWorld = aM_.field_1687;
               this.darenaClickCooldown = 0;
               aM_.field_1724.field_3944.method_45730("darena");
            }
         }
      }
   }

   private boolean clickDarenaMenu(class_1707 handler) {
      int size = handler.method_17388() * 9;

      for (int index = 0; index < size; index++) {
         class_1735 slot = handler.method_7611(index);
         if (!slot.method_7677().method_7960() && slot.method_7677().method_31574(class_1802.field_8323)) {
            aM_.field_1761.method_2906(handler.field_7763, slot.field_7874, 0, class_1713.field_7790, aM_.field_1724);
            return true;
         }
      }

      return false;
   }

   private boolean pvpActive() {
      if (aM_.field_1705 != null && aM_.field_1705.method_1740() != null) {
         Map<UUID, class_345> bars = ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars();

         for (class_345 bar : bars.values()) {
            String name = bar.method_5414().getString().toLowerCase(Locale.ROOT);
            if (name.contains("pvp") || name.contains("пвп") || name.contains("combat")) {
               Matcher matcher = PVP_SECONDS.matcher(name);
               if (matcher.find()) {
                  try {
                     return Math.ceil(Double.parseDouble(matcher.group(1).replace(',', '.'))) > 0.0;
                  } catch (NumberFormatException var7) {
                     return true;
                  }
               } else {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void tickHomeWait() {
      this.stopNavigation();
      boolean confirmed = this.homeTeleportConfirmed;
      boolean arrived = confirmed || this.homeCommandWorld != null && aM_.field_1687 != this.homeCommandWorld;
      if (!arrived && this.homeCommandPos != null) {
         arrived = this.playerPos().method_1025(this.homeCommandPos) > 64.0;
      }

      if (arrived) {
         AutoEnd.Cycle next = this.deathRecoveryActive
            ? (this.needsSupplies() ? AutoEnd.Cycle.FIND_SUPPLY : AutoEnd.Cycle.FIND_PORTAL)
            : AutoEnd.Cycle.FIND_CHEST;
         this.homeTeleportConfirmed = false;
         this.setCycle(next);
      } else {
         if (this.cycleTicks > 0 && this.cycleTicks % 120 == 0) {
            this.sendHome();
         }
      }
   }

   private void sendHome() {
      this.homeTeleportConfirmed = false;
      this.homeCommandPos = this.playerPos();
      this.homeCommandWorld = aM_.field_1687;
      aM_.field_1724.field_3944.method_45730("home");
   }

   private boolean isHomeMessage(String message) {
      return !message.contains("home")
         ? false
         : message.contains("телепортирование к home")
            || message.contains("телепортирован к home")
            || message.contains("teleporting to home")
            || message.contains("teleported to home");
   }

   private void tickFindChest() {
      if (this.scanDelay <= 0) {
         this.scanDelay = 10;
         this.homeChest = this.findHomeChest();
         if (this.homeChest != null) {
            this.cycle = AutoEnd.Cycle.WALK_CHEST;
            this.cycleTicks = 0;
            this.pathRequested = false;
            this.repathDelay = 0;
         } else if (this.cycleTicks > 120) {
            this.exhaustedHomeChests.clear();
            this.cycleTicks = 0;
         }
      }
   }

   private void tickWalkChest() {
      if (!this.isChest(this.homeChest)) {
         this.stopNavigation();
         this.homeChest = null;
         this.setCycle(AutoEnd.Cycle.FIND_CHEST);
      } else if (this.crosshairHit(this.homeChest) == null && this.raycastHit(this.homeChest) == null) {
         if (!this.pathRequested || !this.navigator.isWorking() && this.repathDelay <= 0) {
            this.pathRequested = this.navigator.navigate(this.homeChest, 1, false);
            this.repathDelay = 8;
         }
      } else {
         this.stopNavigation();
         this.cycle = AutoEnd.Cycle.OPEN_CHEST;
         this.cycleTicks = 0;
         this.actionDelay = 0;
         this.resetAim();
      }
   }

   private void tickOpenChest() {
      if (aM_.field_1724.field_7512 instanceof class_1707 handler) {
         this.automationContainerOpened = true;
         this.depositSlot = handler.method_7629().method_5439();
         this.cycle = AutoEnd.Cycle.DEPOSIT;
         this.cycleTicks = 0;
         this.actionDelay = 3;
      } else if (!this.isChest(this.homeChest)) {
         this.homeChest = null;
         this.setCycle(AutoEnd.Cycle.FIND_CHEST);
      } else if (this.actionDelay <= 0) {
         boolean aimed = this.preciseAim(this.homeChest);
         class_3965 hit = this.crosshairHit(this.homeChest);
         if (hit == null && !aimed) {
            if (this.cycleTicks > 100) {
               this.exhaustHomeChest();
            }
         } else {
            if (hit == null) {
               hit = this.raycastHit(this.homeChest);
            }

            if (hit == null) {
               this.cycle = AutoEnd.Cycle.WALK_CHEST;
               this.cycleTicks = 0;
               this.pathRequested = false;
               this.repathDelay = 0;
               this.actionDelay = 0;
               this.resetAim();
            } else {
               aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, hit);
               aM_.field_1724.method_6104(class_1268.field_5808);
               this.actionDelay = 12;
               if (this.cycleTicks > 100) {
                  this.exhaustHomeChest();
               }
            }
         }
      }
   }

   private void exhaustHomeChest() {
      if (this.homeChest != null) {
         this.exhaustedHomeChests.add(this.homeChest.method_10062());
      }

      this.homeChest = null;
      this.setCycle(AutoEnd.Cycle.FIND_CHEST);
      this.actionDelay = 0;
      this.resetAim();
   }

   private void tickDeposit() {
      if (aM_.field_1724.field_7512 instanceof class_1707 handler) {
         if (this.actionDelay <= 0) {
            int chestSlots = handler.method_7629().method_5439();
            int junk = this.findJunkSlot(handler, chestSlots);
            if (junk >= 0) {
               aM_.field_1761.method_2906(handler.field_7763, ((class_1735)handler.field_7761.get(junk)).field_7874, 1, class_1713.field_7795, aM_.field_1724);
               this.actionDelay = 3;
            } else {
               int deposit = this.findDepositSlot(handler, Math.max(this.depositSlot, chestSlots));
               if (deposit >= 0) {
                  aM_.field_1761
                     .method_2906(handler.field_7763, ((class_1735)handler.field_7761.get(deposit)).field_7874, 0, class_1713.field_7794, aM_.field_1724);
                  this.depositSlot = deposit + 1;
                  this.actionDelay = 3;
               } else {
                  boolean chestFull = this.findDepositSlot(handler, chestSlots) >= 0;
                  this.closeAutomationContainer();
                  if (chestFull) {
                     this.exhaustedHomeChests.add(this.homeChest.method_10062());
                     this.homeChest = null;
                     this.setCycle(AutoEnd.Cycle.FIND_CHEST);
                  } else {
                     this.homeChest = null;
                     this.setCycle(this.needsSupplies() ? AutoEnd.Cycle.FIND_SUPPLY : AutoEnd.Cycle.FIND_PORTAL);
                     this.resetAim();
                  }
               }
            }
         }
      } else {
         this.automationContainerOpened = false;
         this.homeChest = null;
         this.setCycle(AutoEnd.Cycle.FIND_CHEST);
      }
   }

   private int findDepositSlot(class_1707 handler, int start) {
      for (int index = Math.max(0, start); index < handler.field_7761.size(); index++) {
         class_1799 stack = ((class_1735)handler.field_7761.get(index)).method_7677();
         if (!stack.method_7960() && !this.isKeptSupply(stack)) {
            return index;
         }
      }

      return -1;
   }

   private int findJunkSlot(class_1707 handler, int chestSlots) {
      for (int index = Math.max(0, chestSlots); index < handler.field_7761.size(); index++) {
         if (this.isJunk(((class_1735)handler.field_7761.get(index)).method_7677())) {
            return index;
         }
      }

      return -1;
   }

   private class_2338 findHomeChest() {
      class_2338 origin = aM_.field_1724.method_24515();
      class_2338 best = null;
      double bestDistance = Double.MAX_VALUE;

      for (class_2338 pos : class_2338.method_25996(origin, 2, 4, 2)) {
         int dx = pos.method_10263() - origin.method_10263();
         int dz = pos.method_10260() - origin.method_10260();
         if (dx * dx + dz * dz <= 4 && this.isChest(pos) && !this.exhaustedHomeChests.contains(pos)) {
            class_2338 candidate = pos.method_10062();
            double distance = aM_.field_1724.method_33571().method_1025(candidate.method_46558());
            if (!this.isSupplyChest(candidate) && !(distance >= bestDistance)) {
               best = candidate;
               bestDistance = distance;
            }
         }
      }

      return best;
   }

   private void tickFindSupply() {
      if (!this.needsSupplies()) {
         this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
      } else if (this.scanDelay <= 0) {
         this.scanDelay = 10;
         this.supplyChest = this.findSupplyChest();
         if (this.supplyChest != null) {
            this.unavailableSupplies.clear();
            this.cycle = AutoEnd.Cycle.WALK_SUPPLY;
            this.cycleTicks = 0;
            this.pathRequested = false;
            this.repathDelay = 0;
         } else if (this.cycleTicks > 120) {
            this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
         }
      }
   }

   private void tickWalkSupply() {
      if (this.isChest(this.supplyChest) && this.isSupplyChest(this.supplyChest)) {
         if (aM_.field_1724.method_33571().method_1025(this.supplyChest.method_46558()) <= 18.0625) {
            this.stopNavigation();
            this.cycle = AutoEnd.Cycle.OPEN_SUPPLY;
            this.cycleTicks = 0;
            this.actionDelay = 0;
            this.resetAim();
         } else {
            if (!this.pathRequested || !this.navigator.isWorking() && this.repathDelay <= 0) {
               this.pathRequested = this.navigator.navigate(this.supplyChest, 1, false);
               this.repathDelay = 8;
            }
         }
      } else {
         this.stopNavigation();
         this.supplyChest = null;
         this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
      }
   }

   private void tickOpenSupply() {
      if (aM_.field_1724.field_7512 instanceof class_1707) {
         this.automationContainerOpened = true;
         this.cycle = AutoEnd.Cycle.TAKE_SUPPLY;
         this.cycleTicks = 0;
         this.actionDelay = 3;
      } else if (!this.isChest(this.supplyChest) || !this.isSupplyChest(this.supplyChest)) {
         this.supplyChest = null;
         this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
      } else if (this.actionDelay <= 0) {
         boolean aimed = this.preciseAim(this.supplyChest);
         class_3965 hit = this.crosshairHit(this.supplyChest);
         if (hit == null && !aimed) {
            if (this.cycleTicks > 100) {
               this.exhaustSupplyChest();
            }
         } else {
            if (hit == null) {
               hit = this.raycastHit(this.supplyChest);
            }

            if (hit == null) {
               if (this.cycleTicks > 100) {
                  this.exhaustSupplyChest();
               }
            } else {
               aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, hit);
               aM_.field_1724.method_6104(class_1268.field_5808);
               this.actionDelay = 12;
               if (this.cycleTicks > 100) {
                  this.exhaustSupplyChest();
               }
            }
         }
      }
   }

   private void exhaustSupplyChest() {
      if (this.supplyChest != null) {
         this.exhaustedSupplyChests.add(this.supplyChest.method_10062());
      }

      this.supplyChest = null;
      this.unavailableSupplies.clear();
      this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
      this.actionDelay = 0;
      this.resetAim();
   }

   private void tickTakeSupply() {
      if (aM_.field_1724.field_7512 instanceof class_1707 handler) {
         if (this.actionDelay <= 0) {
            AutoEnd.Supply kind = this.nextSupply();
            if (kind == null) {
               boolean stillNeeds = this.needsSupplies();
               this.closeAutomationContainer();
               if (stillNeeds) {
                  this.exhaustedSupplyChests.add(this.supplyChest.method_10062());
                  this.supplyChest = null;
                  this.unavailableSupplies.clear();
                  this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
               } else {
                  this.supplyChest = null;
                  this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
               }
            } else {
               int chestSlots = handler.method_17388() * 9;
               int source = this.findSupplyInPlayerInventory(handler, chestSlots, kind);
               if (source < 0) {
                  source = this.findSupplyInChest(handler, chestSlots, kind);
               }

               if (source < 0) {
                  this.unavailableSupplies.add(kind);
                  this.actionDelay = 1;
               } else {
                  int wanted = Math.max(0, this.wantedAmount(kind) - this.carriedAmount(kind));
                  if (!this.transferToHotbar(handler, source, chestSlots, wanted)) {
                     this.closeAutomationContainer();
                     this.exhaustedSupplyChests.add(this.supplyChest.method_10062());
                     this.supplyChest = null;
                     this.unavailableSupplies.clear();
                     this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
                  } else {
                     this.actionDelay = 3;
                  }
               }
            }
         }
      } else {
         this.automationContainerOpened = false;
         this.supplyChest = null;
         this.unavailableSupplies.clear();
         this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
      }
   }

   private int findSupplyInChest(class_1707 handler, int chestSlots, AutoEnd.Supply kind) {
      for (int index = 0; index < chestSlots; index++) {
         if (this.matchesSupply(handler.method_7611(index).method_7677(), kind)) {
            return index;
         }
      }

      return -1;
   }

   private int findSupplyInPlayerInventory(class_1707 handler, int chestSlots, AutoEnd.Supply kind) {
      int end = Math.max(chestSlots, handler.field_7761.size() - 9);

      for (int index = chestSlots; index < end; index++) {
         if (this.matchesSupply(handler.method_7611(index).method_7677(), kind)) {
            return index;
         }
      }

      return -1;
   }

   private boolean transferToHotbar(class_1707 handler, int source, int chestSlots, int wanted) {
      class_1799 stack = handler.method_7611(source).method_7677();
      int amount = Math.min(wanted, stack.method_7947());
      if (amount <= 0) {
         return true;
      } else {
         int hotbarStart = Math.max(chestSlots, handler.field_7761.size() - 9);
         int target = -1;

         for (int index = hotbarStart; index < handler.field_7761.size() - 1; index++) {
            class_1799 existing = handler.method_7611(index).method_7677();
            if (!existing.method_7960() && class_1799.method_31577(existing, stack) && existing.method_7914() - existing.method_7947() >= amount) {
               target = index;
               break;
            }
         }

         if (target < 0) {
            for (int indexx = hotbarStart; indexx < handler.field_7761.size() - 1; indexx++) {
               if (!handler.method_7611(indexx).method_7681()) {
                  target = indexx;
                  break;
               }
            }
         }

         if (target < 0) {
            return false;
         } else {
            aM_.field_1761.method_2906(handler.field_7763, handler.method_7611(source).field_7874, 0, class_1713.field_7790, aM_.field_1724);

            for (int step = 0; step < amount; step++) {
               aM_.field_1761.method_2906(handler.field_7763, handler.method_7611(target).field_7874, 1, class_1713.field_7790, aM_.field_1724);
            }

            aM_.field_1761.method_2906(handler.field_7763, handler.method_7611(source).field_7874, 0, class_1713.field_7790, aM_.field_1724);
            return true;
         }
      }
   }

   private class_2338 findSupplyChest() {
      class_2338 origin = aM_.field_1724.method_24515();
      int radius = Math.min(3, (Integer)aM_.field_1690.method_42503().method_41753());
      return this.chestsInChunks(origin.method_10263() >> 4, origin.method_10260() >> 4, radius)
         .stream()
         .filter(pos -> !this.exhaustedSupplyChests.contains(pos))
         .filter(this::isSupplyChest)
         .filter(pos -> origin.method_10262(pos) <= 1600.0)
         .min(Comparator.comparingDouble(pos -> origin.method_10262(pos)))
         .orElse(null);
   }

   private List<class_2338> chestsInChunks(int centerX, int centerZ, int radius) {
      List<class_2338> result = new ArrayList<>();

      for (int chunkX = centerX - radius; chunkX <= centerX + radius; chunkX++) {
         for (int chunkZ = centerZ - radius; chunkZ <= centerZ + radius; chunkZ++) {
            class_2818 chunk = aM_.field_1687.method_2935().method_21730(chunkX, chunkZ);
            if (chunk != null) {
               for (class_2338 pos : chunk.method_12214().keySet()) {
                  if (this.isChest(pos)) {
                     result.add(pos.method_10062());
                  }
               }
            }
         }
      }

      return result;
   }

   private boolean isSupplyChest(class_2338 chest) {
      if (!this.isChest(chest)) {
         return false;
      } else {
         for (class_2338 pos : class_2338.method_10097(chest.method_10069(-1, -1, -1), chest.method_10069(1, 2, 1))) {
            if (aM_.field_1687.method_8321(pos) instanceof class_2625 sign && this.signMatches(sign) && this.sameChest(chest, this.chestForSign(pos))) {
               return true;
            }
         }

         return false;
      }
   }

   private class_2338 chestForSign(class_2338 signPos) {
      class_2680 state = aM_.field_1687.method_8320(signPos);
      if (state.method_26204() instanceof class_2551) {
         class_2338 behind = signPos.method_10093(((class_2350)state.method_11654(class_2551.field_11726)).method_10153());
         if (this.isChest(behind)) {
            return behind.method_10062();
         }
      } else if (state.method_26204() instanceof class_7715) {
         class_2338 behind = signPos.method_10093(((class_2350)state.method_11654(class_7715.field_40319)).method_10153());
         if (this.isChest(behind)) {
            return behind.method_10062();
         }
      }

      class_2338 nearest = null;
      double nearestDistance = Double.MAX_VALUE;

      for (class_2338 pos : class_2338.method_25996(signPos, 2, 2, 2)) {
         if (this.isChest(pos)) {
            double distance = signPos.method_10262(pos);
            if (distance < nearestDistance) {
               nearest = pos.method_10062();
               nearestDistance = distance;
            }
         }
      }

      return nearest;
   }

   private boolean sameChest(class_2338 candidate, class_2338 assigned) {
      if (assigned == null) {
         return false;
      } else if (candidate.equals(assigned)) {
         return true;
      } else {
         class_2680 state = aM_.field_1687.method_8320(assigned);
         return state.method_26204() instanceof class_2281 && state.method_11654(class_2281.field_10770) != class_2745.field_12569
            ? assigned.method_10093(class_2281.method_9758(state)).equals(candidate)
            : false;
      }
   }

   private boolean signMatches(class_2625 sign) {
      String text = normalize(this.signText(sign.method_49853()) + " " + this.signText(sign.method_49854()));

      for (String keyword : this.signKeywords()) {
         if (!keyword.isBlank() && text.contains(keyword)) {
            return true;
         }
      }

      return false;
   }

   private String signText(class_8242 text) {
      StringBuilder builder = new StringBuilder();

      for (int line = 0; line < 4; line++) {
         builder.append(' ').append(text.method_49859(line, false).getString());
      }

      return builder.toString();
   }

   private List<String> signKeywords() {
      List<String> keywords = new ArrayList<>();
      String value = this.d2.c();
      if (value != null) {
         for (String part : value.split("[,;\\s]+")) {
            String normalized = normalize(part).trim();
            if (!normalized.isBlank()) {
               keywords.add(normalized);
            }
         }
      }

      if (keywords.isEmpty()) {
         keywords.add(normalize("снабжение"));
      }

      return keywords;
   }

   private static String normalize(String text) {
      return text == null ? "" : text.replaceAll("§.", "").toLowerCase(Locale.ROOT).trim();
   }

   private boolean needsSupplies() {
      if (this.c.c() && aM_.field_1724 != null) {
         for (AutoEnd.Supply kind : AutoEnd.Supply.values()) {
            if (this.supplyEnabled(kind) && this.carriedAmount(kind) < this.wantedAmount(kind)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private AutoEnd.Supply nextSupply() {
      for (AutoEnd.Supply kind : AutoEnd.Supply.values()) {
         if (this.supplyEnabled(kind) && !this.unavailableSupplies.contains(kind) && this.carriedAmount(kind) < this.wantedAmount(kind)) {
            return kind;
         }
      }

      return null;
   }

   private boolean supplyEnabled(AutoEnd.Supply kind) {
      return switch (kind) {
         case FOOD -> this.e.c();
         case INVISIBILITY -> this.g2.c();
         case SPEED -> this.i2.c();
      };
   }

   private int wantedAmount(AutoEnd.Supply kind) {
      float value = switch (kind) {
         case FOOD -> this.f2.c();
         case INVISIBILITY -> this.h2.c();
         case SPEED -> this.j2.c();
      };
      return Math.max(1, Math.round(value));
   }

   private int carriedAmount(AutoEnd.Supply kind) {
      int total = 0;

      for (int slot = 0; slot < 9; slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         if (this.matchesSupply(stack, kind)) {
            total += stack.method_7947();
         }
      }

      return total;
   }

   private boolean isKeptSupply(class_1799 stack) {
      if (stack != null && !stack.method_7960()) {
         if (stack.method_31574(class_1802.field_42716)) {
            return true;
         } else {
            for (AutoEnd.Supply kind : AutoEnd.Supply.values()) {
               if (this.supplyEnabled(kind) && this.matchesSupply(stack, kind)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private boolean matchesSupply(class_1799 stack, AutoEnd.Supply kind) {
      if (stack != null && !stack.method_7960()) {
         return switch (kind) {
            case FOOD -> {
               class_4174 food = (class_4174)stack.method_57824(class_9334.field_50075);
               yield food != null && !food.comp_2493() && !normalize(stack.method_7964().getString()).contains("пласт");
            }
            case INVISIBILITY -> this.isPotionOf(stack, class_1294.field_5905, "невид", "инвиз", "invis");
            case SPEED -> this.isPotionOf(stack, class_1294.field_5904, "скорост", "speed");
         };
      } else {
         return false;
      }
   }

   private boolean isPotionOf(class_1799 stack, class_6880<class_1291> effect, String... nameFallbacks) {
      if (!stack.method_31574(class_1802.field_8574)) {
         return false;
      } else {
         class_1844 contents = (class_1844)stack.method_57824(class_9334.field_49651);
         if (contents != null) {
            for (class_1293 instance : contents.method_57397()) {
               if (instance.method_5579().equals(effect)) {
                  return true;
               }
            }
         }

         String name = normalize(stack.method_7964().getString());

         for (String fallback : nameFallbacks) {
            if (name.contains(fallback)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean canUseConsumables() {
      return switch (this.cycle) {
         case DARENA_WAIT, OPEN_CHEST, DEPOSIT, OPEN_SUPPLY, TAKE_SUPPLY, ENTER_PORTAL -> false;
         default -> true;
      };
   }

   private boolean tickConsumables(boolean allowPotions) {
      if (this.consumableReleaseTicks > 0) {
         aM_.field_1690.field_1904.method_23481(false);
         this.consumableReleaseTicks--;
         return true;
      } else if (this.activeConsumable != null) {
         return this.tickActiveConsumable();
      } else if (this.c.c() && aM_.field_1755 == null) {
         AutoEnd.Supply kind = null;
         if (this.e.c() && aM_.field_1724.method_7344().method_7586() < 20) {
            kind = AutoEnd.Supply.FOOD;
         } else if (allowPotions && this.g2.c() && this.needsEffect(class_1294.field_5905)) {
            kind = AutoEnd.Supply.INVISIBILITY;
         } else if (allowPotions && this.i2.c() && this.needsEffect(class_1294.field_5904)) {
            kind = AutoEnd.Supply.SPEED;
         }

         return kind != null && this.startConsumable(kind);
      } else {
         return false;
      }
   }

   private boolean needsEffect(class_6880<class_1291> effect) {
      class_1293 instance = aM_.field_1724.method_6112(effect);
      return instance == null || !instance.method_48559() && instance.method_5584() <= 200;
   }

   private boolean startConsumable(AutoEnd.Supply kind) {
      int slot = -1;

      for (int index = 0; index < 9; index++) {
         if (this.matchesSupply(aM_.field_1724.method_31548().method_5438(index), kind)) {
            slot = index;
            break;
         }
      }

      if (slot < 0) {
         return false;
      } else {
         this.consumablePreviousSlot = aM_.field_1724.method_31548().field_7545;
         this.consumableHotbarSlot = slot;
         this.selectSlot(slot);
         this.activeConsumable = kind;
         this.consumableUseDelay = 2;
         this.consumableTicks = 0;
         this.consumableInteractCooldown = 0;
         this.consumableAvoidChest = this.findAvoidContainer();
         return true;
      }
   }

   private boolean tickActiveConsumable() {
      if (aM_.field_1755 == null && this.consumableStillNeeded(this.activeConsumable)) {
         if (this.consumableHotbarSlot >= 0
            && this.matchesSupply(aM_.field_1724.method_31548().method_5438(this.consumableHotbarSlot), this.activeConsumable)
            && ++this.consumableTicks <= 140) {
            this.selectSlot(this.consumableHotbarSlot);
            if (!this.lookAwayFromContainer() && !aM_.field_1724.method_6115()) {
               aM_.field_1690.field_1904.method_23481(false);
               return true;
            } else if (this.consumableUseDelay-- > 0) {
               return true;
            } else {
               aM_.field_1690.field_1904.method_23481(true);
               if (!aM_.field_1724.method_6115()) {
                  if (this.consumableInteractCooldown <= 0) {
                     aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
                     this.consumableInteractCooldown = 10;
                  } else {
                     this.consumableInteractCooldown--;
                  }
               } else {
                  this.consumableInteractCooldown = 10;
               }

               return true;
            }
         } else {
            this.stopConsumable();
            return this.consumableReleaseTicks > 0;
         }
      } else {
         this.stopConsumable();
         return this.consumableReleaseTicks > 0;
      }
   }

   private boolean consumableStillNeeded(AutoEnd.Supply kind) {
      if (kind == null) {
         return false;
      } else {
         return switch (kind) {
            case FOOD -> aM_.field_1724.method_7344().method_7586() < 20;
            case INVISIBILITY -> this.needsEffect(class_1294.field_5905);
            case SPEED -> this.needsEffect(class_1294.field_5904);
         };
      }
   }

   private class_2338 findAvoidContainer() {
      if (aM_.field_1765 instanceof class_3965 hit && hit.method_17783() == class_240.field_1332 && this.isContainer(hit.method_17777())) {
         return hit.method_17777().method_10062();
      } else {
         for (class_2338 pos : class_2338.method_25996(aM_.field_1724.method_24515(), 6, 4, 6)) {
            if (this.isContainer(pos)) {
               return pos.method_10062();
            }
         }

         return null;
      }
   }

   private boolean lookAwayFromContainer() {
      if (this.consumableAvoidChest == null) {
         return true;
      } else if (!this.isContainer(this.consumableAvoidChest)) {
         this.consumableAvoidChest = this.findAvoidContainer();
         return this.consumableAvoidChest == null;
      } else {
         class_243 center = this.consumableAvoidChest.method_46558();
         float toward = (float)Math.toDegrees(Math.atan2(center.field_1350 - aM_.field_1724.method_23321(), center.field_1352 - aM_.field_1724.method_23317()))
            - 90.0F;
         float away = class_3532.method_15393(toward + 180.0F);
         float yawError = class_3532.method_15393(away - aM_.field_1724.method_36454());
         float pitchError = -28.0F - aM_.field_1724.method_36455();
         if (aM_.field_1724.method_24828()) {
            aM_.field_1724.method_36456(aM_.field_1724.method_36454() + class_3532.method_15363(yawError, -14.0F, 14.0F));
            aM_.field_1724
               .method_36457(class_3532.method_15363(aM_.field_1724.method_36455() + class_3532.method_15363(pitchError, -7.0F, 7.0F), -90.0F, 90.0F));
         }

         boolean onContainer = aM_.field_1765 instanceof class_3965 hit && hit.method_17783() == class_240.field_1332 && this.isContainer(hit.method_17777());
         return !onContainer && Math.abs(yawError) <= 12.0F && Math.abs(pitchError) <= 8.0F;
      }
   }

   private boolean isContainer(class_2338 pos) {
      return this.isChest(pos) || pos != null && aM_.field_1687 != null && aM_.field_1687.method_8320(pos).method_27852(class_2246.field_16328);
   }

   private void stopConsumable() {
      boolean wasUsing = this.activeConsumable != null || aM_.field_1724 != null && aM_.field_1724.method_6115();
      if (aM_.field_1690 != null) {
         wasUsing |= aM_.field_1690.field_1904.method_1434();
         aM_.field_1690.field_1904.method_23481(false);
      }

      if (aM_.field_1724 != null && this.consumablePreviousSlot >= 0) {
         this.selectSlot(8);
      }

      this.activeConsumable = null;
      this.consumablePreviousSlot = -1;
      this.consumableHotbarSlot = -1;
      this.consumableUseDelay = 0;
      this.consumableTicks = 0;
      this.consumableInteractCooldown = 0;
      this.consumableAvoidChest = null;
      this.consumableReleaseTicks = wasUsing ? 2 : 0;
   }

   private void selectSlot(int slot) {
      if (slot >= 0 && slot <= 8 && aM_.field_1724.method_31548().field_7545 != slot) {
         aM_.field_1724.method_31548().method_61496(slot);
      }
   }

   private void tickFindPortal() {
      if (this.scanDelay <= 0) {
         this.scanDelay = 20;
         this.portalTarget = this.findPortal();
         if (this.portalTarget != null) {
            this.beginWalkPortal(this.portalTarget);
         }
      }
   }

   private void beginWalkPortal(class_2338 portal) {
      this.stopNavigation();
      this.releasePortalKeys();
      this.portalTarget = this.portalCenter(portal);
      this.portalWalkGoal = this.portalWalkGoal(this.portalTarget);
      this.portalEntryWorld = aM_.field_1687;
      this.portalEntryPos = this.playerPos();
      this.setCycle(AutoEnd.Cycle.WALK_PORTAL);
      this.pathRequested = false;
      this.repathDelay = 0;
      this.initialPortalCheckTicks = 0;
   }

   private class_2338 findPortal() {
      for (class_2338 pos : class_2338.method_25996(aM_.field_1724.method_24515(), 48, 20, 48)) {
         if (this.isPortal(pos)) {
            return this.portalCenter(pos);
         }
      }

      return null;
   }

   private class_2338 portalCenter(class_2338 seed) {
      if (!this.isPortal(seed)) {
         return seed == null ? null : seed.method_10062();
      } else {
         int minX = seed.method_10263();
         int maxX = seed.method_10263();
         int minZ = seed.method_10260();
         int maxZ = seed.method_10260();

         for (int x = seed.method_10263() - 4; x <= seed.method_10263() + 4; x++) {
            for (int z = seed.method_10260() - 4; z <= seed.method_10260() + 4; z++) {
               if (this.isPortal(new class_2338(x, seed.method_10264(), z))) {
                  minX = Math.min(minX, x);
                  maxX = Math.max(maxX, x);
                  minZ = Math.min(minZ, z);
                  maxZ = Math.max(maxZ, z);
               }
            }
         }

         return new class_2338((minX + maxX) / 2, seed.method_10264(), (minZ + maxZ) / 2);
      }
   }

   private boolean isPortal(class_2338 pos) {
      return pos != null && aM_.field_1687 != null && aM_.field_1687.method_8320(pos).method_27852(class_2246.field_10027);
   }

   private void tickWalkPortal() {
      if (this.leftThroughPortal()) {
         this.arrivedInEnd();
      } else if (!this.isPortal(this.portalTarget)) {
         this.stopNavigation();
         this.portalTarget = null;
         this.portalWalkGoal = null;
         this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
      } else {
         double dx = aM_.field_1724.method_23317() - (this.portalTarget.method_10263() + 0.5);
         double dz = aM_.field_1724.method_23321() - (this.portalTarget.method_10260() + 0.5);
         double dy = aM_.field_1724.method_23318() - this.portalTarget.method_10264();
         double flatSq = dx * dx + dz * dz;
         if (flatSq <= 7.0 && Math.abs(dy) <= 3.5) {
            this.switchToEnterPortal();
         } else if (this.navigator.hasFailed() && flatSq <= 64.0 && Math.abs(dy) <= 4.5) {
            this.switchToEnterPortal();
         } else {
            if (this.portalWalkGoal == null || !this.navigator.isWalkableNode(this.portalWalkGoal)) {
               this.portalWalkGoal = this.portalWalkGoal(this.portalTarget);
            }

            if (this.portalWalkGoal == null) {
               this.stopNavigation();
               this.portalTarget = null;
               this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
            } else {
               if (!this.pathRequested || !this.navigator.isWorking() && this.repathDelay <= 0) {
                  this.pathRequested = this.navigator.navigate(this.portalWalkGoal, 0, false);
                  this.repathDelay = 8;
               }
            }
         }
      }
   }

   private void switchToEnterPortal() {
      this.stopNavigation();
      this.cycle = AutoEnd.Cycle.ENTER_PORTAL;
      this.cycleTicks = 0;
   }

   private class_2338 portalWalkGoal(class_2338 portal) {
      if (portal != null && aM_.field_1724 != null) {
         double dx = aM_.field_1724.method_23317() - (portal.method_10263() + 0.5);
         double dz = aM_.field_1724.method_23321() - (portal.method_10260() + 0.5);
         int dirX;
         int dirZ;
         if (Math.abs(dx) >= Math.abs(dz)) {
            dirX = dx >= 0.0 ? 1 : -1;
            dirZ = 0;
         } else {
            dirX = 0;
            dirZ = dz >= 0.0 ? 1 : -1;
         }

         int perpX = -dirZ;
         int perpZ = dirX;

         for (int distance = 2; distance <= 4; distance++) {
            for (int spread = 0; spread <= 1; spread++) {
               int[] sides = spread == 0 ? new int[]{0} : new int[]{-spread, spread};

               for (int side : sides) {
                  int x = portal.method_10263() + dirX * distance + perpX * side;
                  int z = portal.method_10260() + dirZ * distance + perpZ * side;

                  for (int dy = 0; dy <= 4; dy++) {
                     class_2338 above = new class_2338(x, portal.method_10264() + dy, z);
                     if (this.navigator.isWalkableNode(above)) {
                        return above.method_10062();
                     }

                     if (dy > 0) {
                        class_2338 below = new class_2338(x, portal.method_10264() - dy, z);
                        if (this.navigator.isWalkableNode(below)) {
                           return below.method_10062();
                        }
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean leftThroughPortal() {
      boolean worldChanged = this.portalEntryWorld != null && aM_.field_1687 != this.portalEntryWorld;
      boolean teleported = this.portalEntryPos != null && this.playerPos().method_1025(this.portalEntryPos) > 256.0;
      return this.cycleTicks > 8 && (worldChanged || teleported);
   }

   private void tickEnterPortal() {
      if (this.leftThroughPortal()) {
         this.arrivedInEnd();
      } else if (!this.isPortal(this.portalTarget)) {
         this.releasePortalKeys();
         this.portalTarget = null;
         this.portalWalkGoal = null;
         this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
      } else {
         double dx = this.portalTarget.method_10263() + 0.5 - aM_.field_1724.method_23317();
         double dz = this.portalTarget.method_10260() + 0.5 - aM_.field_1724.method_23321();
         double distanceSq = dx * dx + dz * dz;
         float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
         float error = class_3532.method_15393(yaw - aM_.field_1724.method_36454());
         aM_.field_1724.method_36456(aM_.field_1724.method_36454() + class_3532.method_15363(error, -14.0F, 14.0F));
         boolean facing = Math.abs(error) <= 38.0F;
         aM_.field_1690.field_1894.method_23481(facing && distanceSq > 0.04);
         aM_.field_1690.field_1867.method_23481(false);
         aM_.field_1690.field_1903.method_23481(facing);
         if (this.cycleTicks > 220) {
            this.releasePortalKeys();
            this.stopNavigation();
            this.portalTarget = null;
            this.portalWalkGoal = null;
            this.setCycle(AutoEnd.Cycle.FIND_PORTAL);
         }
      }
   }

   private void arrivedInEnd() {
      this.stopConsumable();
      this.releasePortalKeys();
      this.stopNavigation();
      this.brokenPots = 0;
      this.returnHomePending = false;
      this.clearReturnState();
      this.homeCommandPos = null;
      this.homeCommandWorld = null;
      this.homeTeleportConfirmed = false;
      this.deathRecoveryPending = false;
      this.deathRecoveryActive = false;
      this.setCycle(AutoEnd.Cycle.FARMING);
   }

   private void releasePortalKeys() {
      if (aM_.field_1690 != null) {
         aM_.field_1690.field_1894.method_23481(false);
         aM_.field_1690.field_1867.method_23481(false);
         aM_.field_1690.field_1903.method_23481(false);
      }
   }

   private void clearReturnState() {
      this.exhaustedHomeChests.clear();
      this.exhaustedSupplyChests.clear();
      this.unavailableSupplies.clear();
      this.homeChest = null;
      this.supplyChest = null;
      this.portalTarget = null;
      this.portalWalkGoal = null;
      this.patrolGoal = null;
      this.darenaCommandPos = null;
      this.darenaCommandWorld = null;
      this.darenaClickCooldown = 0;
      this.pvpClearTicks = 0;
      this.portalEntryPos = null;
      this.portalEntryWorld = null;
      this.actionDelay = 0;
      this.cycleTicks = 0;
   }

   private void resetCycle() {
      this.stopConsumable();
      this.cycle = AutoEnd.Cycle.FARMING;
      this.brokenPots = 0;
      this.returnHomePending = false;
      this.deathRecoveryPending = false;
      this.deathRecoveryActive = false;
      this.initialPortalCheckTicks = 0;
      this.depositSlot = 0;
      this.automationContainerOpened = false;
      this.automationContainerOpenTicks = 0;
      this.clearReturnState();
      this.homeCommandPos = null;
      this.homeCommandWorld = null;
      this.homeTeleportConfirmed = false;
   }

   private boolean isAutomationContainerState() {
      return aM_.field_1724 != null && aM_.field_1724.field_7512 instanceof class_1707
         ? this.cycle == AutoEnd.Cycle.DARENA_WAIT
            || this.cycle == AutoEnd.Cycle.OPEN_CHEST
            || this.cycle == AutoEnd.Cycle.DEPOSIT
            || this.cycle == AutoEnd.Cycle.OPEN_SUPPLY
            || this.cycle == AutoEnd.Cycle.TAKE_SUPPLY
         : false;
   }

   private boolean closeTimedOutContainer() {
      boolean container = aM_.field_1724.field_7512 instanceof class_1707;
      boolean ours = this.automationContainerOpened || this.isAutomationContainerState();
      if (container && ours) {
         if (++this.automationContainerOpenTicks < 200) {
            return false;
         } else {
            AutoEnd.Cycle stuck = this.cycle;
            this.pauseMovement();
            aM_.field_1724.method_7346();
            this.automationContainerOpened = false;
            this.automationContainerOpenTicks = 0;
            this.actionDelay = 0;
            this.screenResumeDelay = 3;
            switch (stuck) {
               case DARENA_WAIT:
                  this.darenaClickCooldown = 0;
                  this.cycleTicks = 59;
               case HOME_WAIT:
               case FIND_CHEST:
               case WALK_CHEST:
               case FIND_SUPPLY:
               case WALK_SUPPLY:
               default:
                  break;
               case OPEN_CHEST:
               case DEPOSIT:
                  this.homeChest = null;
                  this.setCycle(AutoEnd.Cycle.FIND_CHEST);
                  this.scanDelay = 20;
                  this.depositSlot = 0;
                  break;
               case OPEN_SUPPLY:
               case TAKE_SUPPLY:
                  this.supplyChest = null;
                  this.setCycle(AutoEnd.Cycle.FIND_SUPPLY);
                  this.scanDelay = 20;
            }

            return true;
         }
      } else {
         this.automationContainerOpenTicks = 0;
         return false;
      }
   }

   private void closeAutomationContainer() {
      if (this.automationContainerOpened && aM_.field_1724 != null && aM_.field_1724.field_7512 instanceof class_1707) {
         aM_.field_1724.method_7346();
         this.automationContainerOpened = false;
         this.automationContainerOpenTicks = 0;
      } else {
         this.automationContainerOpened = false;
         this.automationContainerOpenTicks = 0;
      }
   }

   private boolean isChest(class_2338 pos) {
      if (pos != null && aM_.field_1687 != null) {
         class_2680 state = aM_.field_1687.method_8320(pos);
         return state.method_27852(class_2246.field_10034) || state.method_27852(class_2246.field_10380);
      } else {
         return false;
      }
   }

   private class_3965 crosshairHit(class_2338 pos) {
      if (pos == null || !(aM_.field_1765 instanceof class_3965 hit && hit.method_17783() == class_240.field_1332)) {
         return null;
      } else {
         return this.hitMatchesChest(pos, hit.method_17777()) && !(aM_.field_1724.method_33571().method_1025(hit.method_17784()) > 18.0625) ? hit : null;
      }
   }

   private class_3965 raycastHit(class_2338 pos) {
      if (pos == null) {
         return null;
      } else {
         class_3965 direct = this.crosshairHit(pos);
         if (direct != null) {
            return direct;
         } else {
            class_243 eye = aM_.field_1724.method_33571();
            class_243 center = pos.method_46558();
            List<class_243> points = new ArrayList<>();
            points.add(center);

            for (class_2350 direction : class_2350.values()) {
               points.add(center.method_1031(direction.method_10148() * 0.49, direction.method_10164() * 0.49, direction.method_10165() * 0.49));
            }

            points.sort(Comparator.comparingDouble(eye::method_1025));

            for (class_243 point : points) {
               class_3965 hit = aM_.field_1687.method_17742(new class_3959(eye, point, class_3960.field_17558, class_242.field_1348, aM_.field_1724));
               if (hit.method_17783() == class_240.field_1332
                  && this.hitMatchesChest(pos, hit.method_17777())
                  && eye.method_1025(hit.method_17784()) <= 18.0625) {
                  return hit;
               }
            }

            return null;
         }
      }
   }

   private boolean hitMatchesChest(class_2338 intended, class_2338 hit) {
      if (intended.equals(hit)) {
         return true;
      } else {
         return this.isChest(intended) && this.isChest(hit) && intended.method_10264() == hit.method_10264()
            ? Math.abs(intended.method_10263() - hit.method_10263()) + Math.abs(intended.method_10260() - hit.method_10260()) == 1
            : false;
      }
   }

   private boolean isPot(class_2338 pos) {
      return pos != null && aM_.field_1687 != null && aM_.field_1687.method_8320(pos).method_27852(class_2246.field_42752);
   }

   private boolean isBrushable(class_2338 pos) {
      if (pos != null && aM_.field_1687 != null) {
         class_2680 state = aM_.field_1687.method_8320(pos);
         return state.method_27852(class_2246.field_42728) || state.method_27852(class_2246.field_43227);
      } else {
         return false;
      }
   }

   private boolean targetStillThere() {
      return this.targetBrushable ? this.isBrushable(this.targetBlock) : this.isPot(this.targetBlock);
   }

   private boolean inReach(class_2338 pos) {
      return pos != null && aM_.field_1724.method_33571().method_1025(pos.method_46558()) <= 18.0625;
   }

   private boolean visible(class_2338 pos) {
      if (pos == null) {
         return false;
      } else {
         class_3965 hit = aM_.field_1687
            .method_17742(new class_3959(aM_.field_1724.method_33571(), pos.method_46558(), class_3960.field_17558, class_242.field_1348, aM_.field_1724));
         return hit.method_17783() == class_240.field_1332 && hit.method_17777().equals(pos);
      }
   }

   private boolean canSeeFrom(class_2338 stand, class_2338 target) {
      class_243 eye = new class_243(stand.method_10263() + 0.5, stand.method_10264() + 1.62, stand.method_10260() + 0.5);
      class_3965 hit = aM_.field_1687.method_17742(new class_3959(eye, target.method_46558(), class_3960.field_17558, class_242.field_1348, aM_.field_1724));
      return hit.method_17783() == class_240.field_1332 && hit.method_17777().equals(target);
   }

   private boolean crosshairOn(class_2338 pos) {
      return aM_.field_1765 instanceof class_3965 hit && hit.method_17777().equals(pos);
   }

   private boolean standingOnBrushable(class_2338 pos) {
      if (pos == null) {
         return false;
      } else {
         class_2338 feet = aM_.field_1724.method_24515();
         if (feet.method_10263() == pos.method_10263() && feet.method_10260() == pos.method_10260()) {
            double y = aM_.field_1724.method_23318();
            return y >= pos.method_10264() + 0.7 && y <= pos.method_10264() + 1.35;
         } else {
            return false;
         }
      }
   }

   private AutoEnd.ScanResult scanTargets() {
      class_2338 origin = aM_.field_1724.method_24515();
      int centerX = origin.method_10263() >> 4;
      int centerZ = origin.method_10260() >> 4;
      int radius = Math.min(8, (Integer)aM_.field_1690.method_42503().method_41753());
      boolean canBrush = this.hasBrush();
      if (this.scanCenterChunkX != centerX || this.scanCenterChunkZ != centerZ || this.scanRadius != radius || this.scanCanBrush != canBrush) {
         this.scanCenterChunkX = centerX;
         this.scanCenterChunkZ = centerZ;
         this.scanRadius = radius;
         this.scanCanBrush = canBrush;
         this.scanCursor = 0;
         this.scanNearestDistance = 22500.0;
         this.scanNearest = null;
      }

      int side = radius * 2 + 1;
      int total = side * side;

      for (int processed = 0; this.scanCursor < total && processed < 24; processed++) {
         int row = this.scanCursor / side;
         int column = this.scanCursor % side;
         this.scanCursor++;
         class_2818 chunk = aM_.field_1687.method_2935().method_21730(centerX - radius + row, centerZ - radius + column);
         if (chunk != null) {
            for (class_2338 pos : chunk.method_12214().keySet()) {
               if (!this.skippedTargets.containsKey(pos)) {
                  boolean pot = this.isPot(pos);
                  boolean brushable = canBrush && this.isBrushable(pos);
                  if (pot || brushable) {
                     double distance = origin.method_10262(pos);
                     if (distance < this.scanNearestDistance) {
                        this.scanNearestDistance = distance;
                        this.scanNearest = new AutoEnd.TargetSelection(pos.method_10062(), brushable);
                     }
                  }
               }
            }
         }
      }

      if (this.scanCursor < total) {
         return new AutoEnd.ScanResult(false, null);
      } else {
         AutoEnd.TargetSelection selection = this.scanNearest;
         this.resetScan();
         return new AutoEnd.ScanResult(true, selection);
      }
   }

   private void resetScan() {
      this.scanCenterChunkX = Integer.MIN_VALUE;
      this.scanCenterChunkZ = Integer.MIN_VALUE;
      this.scanRadius = -1;
      this.scanCursor = 0;
      this.scanNearestDistance = 22500.0;
      this.scanNearest = null;
      this.scanCanBrush = false;
   }

   private void tickSkippedTargets() {
      this.skippedTargets.replaceAll((pos, ticks) -> ticks - 1);
      this.skippedTargets.entrySet().removeIf(entry -> entry.getValue() <= 0);
   }

   private void patrol() {
      if (aM_.field_1724 != null) {
         if (this.patrolGoal != null) {
            double dx = aM_.field_1724.method_23317() - (this.patrolGoal.method_10263() + 0.5);
            double dz = aM_.field_1724.method_23321() - (this.patrolGoal.method_10260() + 0.5);
            if (dx * dx + dz * dz <= 2.25) {
               this.stopNavigation();
               this.patrolGoal = null;
            }
         }

         if (this.patrolGoal == null && this.repathDelay <= 0) {
            this.patrolGoal = this.randomPatrolGoal(aM_.field_1724.method_24515(), ThreadLocalRandom.current());
            if (this.patrolGoal == null) {
               this.repathDelay = 8;
            } else {
               this.pathRequested = this.navigator.navigate(this.patrolGoal, 0, true);
               this.repathDelay = 8;
            }
         }
      }
   }

   private class_2338 randomPatrolGoal(class_2338 origin, ThreadLocalRandom random) {
      for (int attempt = 0; attempt < 24; attempt++) {
         double angle = random.nextDouble(Math.PI * 2);
         int distance = random.nextInt(10, 25);
         int x = origin.method_10263() + (int)Math.round(Math.cos(angle) * distance);
         int z = origin.method_10260() + (int)Math.round(Math.sin(angle) * distance);

         for (int dy = 0; dy <= 6; dy++) {
            class_2338 up = new class_2338(x, origin.method_10264() + dy, z);
            if (this.navigator.isWalkableNode(up)) {
               return up;
            }

            if (dy > 0) {
               class_2338 down = new class_2338(x, origin.method_10264() - dy, z);
               if (this.navigator.isWalkableNode(down)) {
                  return down;
               }
            }
         }
      }

      return null;
   }

   private void clearPatrol() {
      if (this.patrolGoal != null || this.pathRequested || this.navigator.isWorking()) {
         this.stopNavigation();
      }

      this.patrolGoal = null;
      this.pathRequested = false;
      this.repathDelay = 0;
   }

   private void requestTargetRoute(boolean force) {
      if (this.targetBlock != null && (force || !this.pathRequested)) {
         class_2338 goal = this.targetBrushable ? this.targetBlock.method_10084() : this.targetBlock;
         List<class_2338> stands = this.targetBrushable ? List.of(goal) : this.potStandPositions(this.targetBlock);
         this.pathRequested = stands.isEmpty() ? this.navigator.navigate(goal, 0, force) : this.navigator.navigateToAny(stands, force);
         this.repathDelay = 8;
      }
   }

   private List<class_2338> potStandPositions(class_2338 pot) {
      class_2338 feet = aM_.field_1724.method_24515();
      boolean blockedHere = !this.visible(pot);

      for (int dy : new int[]{0, -1, 1, -2, 2, -3, 3}) {
         List<class_2338> stands = new ArrayList<>();

         for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
               if (dx != 0 || dz != 0) {
                  class_2338 stand = new class_2338(pot.method_10263() + dx, pot.method_10264() + dy, pot.method_10260() + dz);
                  if (!blockedHere || !stand.equals(feet)) {
                     class_243 eye = new class_243(stand.method_10263() + 0.5, stand.method_10264() + 1.62, stand.method_10260() + 0.5);
                     if (!(eye.method_1025(pot.method_46558()) > 18.0625) && this.navigator.isWalkableNode(stand) && this.canSeeFrom(stand, pot)) {
                        stands.add(stand.method_10062());
                     }
                  }
               }
            }
         }

         if (!stands.isEmpty()) {
            stands.sort(Comparator.comparingDouble(feet::method_10262));
            return List.copyOf(stands);
         }
      }

      return List.of();
   }

   private void clearTarget(boolean stopPath) {
      if (this.brushingStarted || this.previousSelectedSlot != -1 || this.brushMovedToHotbar) {
         this.stopBrushing();
      }

      if (stopPath) {
         this.stopNavigation();
      }

      this.targetBlock = null;
      this.targetBrushable = false;
      this.breakingStarted = false;
      this.pathRequested = false;
      this.repathDelay = 0;
      this.aimTicks = 0;
      this.resetScan();
      this.resetAim();
   }

   private void stopNavigation() {
      this.navigator.stop();
      this.pathRequested = false;
      this.repathDelay = 0;
   }

   private void checkNavigatorFailure() {
      switch (this.cycle) {
         case DARENA_WAIT:
         case HOME_WAIT:
         case OPEN_CHEST:
         case DEPOSIT:
         case OPEN_SUPPLY:
         case TAKE_SUPPLY:
         case ENTER_PORTAL:
            return;
         case FIND_CHEST:
         case WALK_CHEST:
         case FIND_SUPPLY:
         case WALK_SUPPLY:
         case FIND_PORTAL:
         case WALK_PORTAL:
         default:
            if (this.navigator.hasFailed()) {
               if (this.cycle == AutoEnd.Cycle.WALK_PORTAL && this.isPortal(this.portalTarget)) {
                  this.switchToEnterPortal();
               } else if (this.cycle == AutoEnd.Cycle.FARMING) {
                  if (this.lootOrigin != null) {
                     this.finishLoot();
                  } else {
                     this.skipFailedTarget();
                  }
               } else {
                  this.stopNavigation();
                  if (this.cycle == AutoEnd.Cycle.WALK_CHEST && this.homeChest != null) {
                     this.exhaustedHomeChests.add(this.homeChest.method_10062());
                     this.homeChest = null;
                     this.cycle = AutoEnd.Cycle.FIND_CHEST;
                  } else if (this.cycle == AutoEnd.Cycle.WALK_SUPPLY && this.supplyChest != null) {
                     this.exhaustedSupplyChests.add(this.supplyChest.method_10062());
                     this.supplyChest = null;
                     this.unavailableSupplies.clear();
                     this.cycle = AutoEnd.Cycle.FIND_SUPPLY;
                  } else if (this.cycle == AutoEnd.Cycle.WALK_PORTAL) {
                     this.portalTarget = null;
                     this.portalWalkGoal = null;
                     this.cycle = AutoEnd.Cycle.FIND_PORTAL;
                  } else if (this.cycle == AutoEnd.Cycle.PVP_WAIT) {
                     this.patrolGoal = null;
                  }

                  this.cycleTicks = 0;
                  this.scanDelay = 0;
               }
            }
      }
   }

   private void skipFailedTarget() {
      if (this.targetBlock != null) {
         this.skippedTargets.put(this.targetBlock.method_10062(), 1200);
         this.stopNavigation();
         this.clearTarget(false);
         this.scanDelay = 1;
      } else {
         this.stopNavigation();
         this.patrolGoal = null;
         this.scanDelay = 0;
         this.repathDelay = 8;
      }
   }

   private boolean hasBrush() {
      return this.findBrushSlot(0, 35) != -1;
   }

   private int findBrushSlot(int start, int end) {
      for (int slot = start; slot <= end; slot++) {
         if (aM_.field_1724.method_31548().method_5438(slot).method_31574(class_1802.field_42716)) {
            return slot;
         }
      }

      return -1;
   }

   private boolean equipBrush() {
      if (aM_.field_1724.method_6047().method_31574(class_1802.field_42716)) {
         return true;
      } else {
         int hotbar = this.findBrushSlot(0, 8);
         if (hotbar != -1) {
            if (this.previousSelectedSlot == -1) {
               this.previousSelectedSlot = aM_.field_1724.method_31548().field_7545;
            }

            this.brushHotbarSlot = hotbar;
            this.selectSlot(hotbar);
            this.brushUseCooldown = 1;
            return false;
         } else {
            int inventory = this.findBrushSlot(9, 35);
            if (inventory != -1 && aM_.field_1724.field_7512 == aM_.field_1724.field_7498) {
               if (this.previousSelectedSlot == -1) {
                  this.previousSelectedSlot = aM_.field_1724.method_31548().field_7545;
               }

               this.brushHotbarSlot = this.previousSelectedSlot;
               this.brushInventorySlot = inventory;
               aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, inventory, this.brushHotbarSlot, class_1713.field_7791, aM_.field_1724);
               this.brushMovedToHotbar = true;
               this.brushUseCooldown = 1;
               return false;
            } else {
               return false;
            }
         }
      }
   }

   private void brush() {
      if (this.equipBrush()) {
         if (aM_.field_1765 instanceof class_3965 hit && hit.method_17777().equals(this.targetBlock)) {
            this.brushingStarted = true;
            aM_.field_1690.field_1904.method_23481(true);
            if ((!aM_.field_1724.method_6115() || !aM_.field_1724.method_6030().method_31574(class_1802.field_42716)) && this.brushUseCooldown <= 0) {
               aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, hit);
               this.brushUseCooldown = 4;
            }

            if (!this.isBrushable(this.targetBlock)) {
               class_2338 source = this.targetBlock.method_10062();
               this.stopBrushing();
               this.startLoot(source, false);
            }
         } else {
            this.brushingStarted = false;
         }
      }
   }

   private void stopBrushing() {
      if (aM_.field_1690 != null) {
         aM_.field_1690.field_1904.method_23481(false);
      }

      if (aM_.field_1724 != null && aM_.field_1761 != null && aM_.field_1724.method_6115()) {
         aM_.field_1761.method_2897(aM_.field_1724);
      }

      if (aM_.field_1724 != null
         && aM_.field_1761 != null
         && this.brushMovedToHotbar
         && this.brushInventorySlot >= 9
         && this.brushHotbarSlot >= 0
         && aM_.field_1724.field_7512 == aM_.field_1724.field_7498) {
         aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, this.brushInventorySlot, this.brushHotbarSlot, class_1713.field_7791, aM_.field_1724);
      }

      if (aM_.field_1724 != null && this.previousSelectedSlot >= 0) {
         this.selectSlot(8);
      }

      this.brushingStarted = false;
      this.brushUseCooldown = 0;
      this.previousSelectedSlot = -1;
      this.brushInventorySlot = -1;
      this.brushHotbarSlot = -1;
      this.brushMovedToHotbar = false;
   }

   private void startLoot(class_2338 source, boolean fromPot) {
      this.stopNavigation();
      this.snapshotHotbar();
      this.lootOrigin = source.method_10062();
      this.lootFromPot = fromPot;
      if (fromPot) {
         this.brokenPots++;
         this.returnHomePending = this.brokenPots >= Math.max(1, Math.round(this.b.c()));
      }

      this.lootGoal = null;
      this.lootTicks = 0;
      this.lootEmptyTicks = 0;
      this.clearTarget(false);
   }

   private void tickLoot() {
      this.lootTicks++;
      if (!this.isPot(this.lootOrigin) && (!this.hasBrush() || !this.isBrushable(this.lootOrigin))) {
         class_238 area = new class_238(this.lootOrigin).method_1009(12.0, 3.0, 12.0);
         List<class_1542> drops = aM_.field_1687.method_8390(class_1542.class, area, item -> item.method_5805() && !item.method_6983().method_7960());
         class_1542 nearest = drops.stream()
            .filter(drop -> !this.isJunk(drop.method_6983()))
            .min(Comparator.comparingDouble(drop -> aM_.field_1724.method_5858(drop)))
            .orElse(null);
         if (nearest != null) {
            this.lootEmptyTicks = 0;
         } else if (this.lootTicks >= 12) {
            this.lootEmptyTicks++;
         }

         if (nearest == null) {
            if (this.pathRequested || this.lootGoal != null || this.navigator.isWorking()) {
               this.stopNavigation();
               this.lootGoal = null;
            }

            if (this.lootEmptyTicks >= 8 || this.lootTicks >= 120) {
               this.finishLoot();
            }
         } else if (!this.standingOn(nearest)) {
            this.walkToLoot(nearest.method_24515());
         } else {
            if (this.pathRequested || this.navigator.isWorking()) {
               this.stopNavigation();
            }
         }
      } else {
         class_2338 source = this.lootOrigin.method_10062();
         boolean brushable = this.isBrushable(source);
         this.stopNavigation();
         this.clearLoot();
         this.clearTarget(false);
         this.targetBlock = source;
         this.targetBrushable = brushable;
         this.scanDelay = 0;
      }
   }

   private boolean standingOn(class_1542 item) {
      double dx = item.method_23317() - aM_.field_1724.method_23317();
      double dz = item.method_23321() - aM_.field_1724.method_23321();
      return dx * dx + dz * dz <= 0.2 && Math.abs(item.method_23318() - aM_.field_1724.method_23318()) <= 1.5;
   }

   private void walkToLoot(class_2338 goal) {
      boolean changed = !goal.equals(this.lootGoal);
      if (changed || !this.pathRequested || !this.navigator.isWorking() && this.repathDelay <= 0) {
         this.lootGoal = goal.method_10062();
         this.pathRequested = this.navigator.navigate(this.lootGoal, 0, changed);
         this.repathDelay = 3;
      }
   }

   private void finishLoot() {
      this.stopNavigation();
      this.clearLoot();
      if (this.returnHomePending) {
         this.beginReturnHome();
      } else {
         this.scanDelay = 0;
      }
   }

   private void clearLoot() {
      this.lootOrigin = null;
      this.lootGoal = null;
      this.lootTicks = 0;
      this.lootEmptyTicks = 0;
      this.lootFromPot = false;
      this.clearSnapshot();
   }

   private void snapshotHotbar() {
      if (aM_.field_1724 == null) {
         this.clearSnapshot();
      } else {
         for (int slot = 0; slot < this.lootHotbarSnapshot.length; slot++) {
            this.lootHotbarSnapshot[slot] = aM_.field_1724.method_31548().method_5438(slot).method_7972();
         }
      }
   }

   private void clearSnapshot() {
      for (int slot = 0; slot < this.lootHotbarSnapshot.length; slot++) {
         this.lootHotbarSnapshot[slot] = null;
      }
   }

   private void trackPickedHotbar() {
      if (this.lootOrigin != null && aM_.field_1724 != null) {
         for (int slot = 0; slot < this.lootHotbarSnapshot.length; slot++) {
            class_1799 current = aM_.field_1724.method_31548().method_5438(slot);
            class_1799 before = this.lootHotbarSnapshot[slot];
            if (!current.method_7960() && !this.isKeptSupply(current) && !this.isArmor(current)) {
               boolean appeared = before == null || before.method_7960();
               boolean replaced = !appeared && !class_1799.method_31577(before, current);
               boolean grew = !appeared && !replaced && current.method_7947() > before.method_7947();
               if (appeared || replaced || grew) {
                  this.pickedHotbarSlots.add(slot);
                  this.hotbarCleanupPending = true;
               }
            }
         }
      }
   }

   private boolean discardJunkImmediately() {
      if (aM_.field_1724 != null
         && aM_.field_1761 != null
         && !this.automationContainerOpened
         && this.activeConsumable == null
         && this.previousSelectedSlot < 0
         && !this.brushingStarted
         && (aM_.field_1755 == null || this.automationInventoryOpened)
         && aM_.field_1724.field_7512 == aM_.field_1724.field_7498) {
         int junk = -1;

         for (int slot = 0; slot < 36; slot++) {
            if (this.isJunk(aM_.field_1724.method_31548().method_5438(slot))) {
               junk = slot;
               break;
            }
         }

         if (junk < 0 && this.discardCooldown <= 0) {
            this.discardStopping = false;
            this.discardSettleTicks = 0;
            return false;
         } else {
            this.discardStopping = true;
            class_243 velocity = aM_.field_1724.method_18798();
            aM_.field_1724.method_18800(0.0, velocity.field_1351, 0.0);
            if (this.discardCooldown > 0) {
               this.discardCooldown--;
               return true;
            } else if (this.discardSettleTicks < 3) {
               this.discardSettleTicks++;
               return true;
            } else {
               if (junk >= 0) {
                  int screenSlot = junk < 9 ? 36 + junk : junk;
                  aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, screenSlot, 1, class_1713.field_7795, aM_.field_1724);
                  if (junk < 9) {
                     this.pickedHotbarSlots.remove(junk);
                  }

                  this.discardSettleTicks = 0;
                  this.discardCooldown = 1;
               }

               return true;
            }
         }
      } else {
         this.discardStopping = false;
         this.discardSettleTicks = 0;
         return false;
      }
   }

   private boolean cleanupHotbar() {
      if (aM_.field_1724 != null && aM_.field_1761 != null) {
         class_1799 ninth = aM_.field_1724.method_31548().method_5438(8);
         boolean ninthOccupied = !ninth.method_7960() && !this.isArmor(ninth);
         if (!this.hotbarCleanupPending && this.pickedHotbarSlots.isEmpty() && !ninthOccupied && !this.automationInventoryOpened) {
            return false;
         } else if (this.activeConsumable != null || this.previousSelectedSlot >= 0 || this.brushingStarted || this.automationContainerOpened) {
            return false;
         } else if (!this.automationInventoryOpened) {
            if (aM_.field_1755 != null) {
               return false;
            } else {
               aM_.method_1507(new class_490(aM_.field_1724));
               this.automationInventoryOpened = true;
               this.hotbarCleanupDelay = 3;
               return true;
            }
         } else if (aM_.field_1755 instanceof class_490 && aM_.field_1724.field_7512 == aM_.field_1724.field_7498) {
            if (this.hotbarCleanupDelay-- > 0) {
               return true;
            } else {
               for (Integer slot : new ArrayList<>(this.pickedHotbarSlots)) {
                  class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
                  if (!stack.method_7960() && !this.isKeptSupply(stack) && !this.isArmor(stack)) {
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, 36 + slot, 0, class_1713.field_7794, aM_.field_1724);
                     this.pickedHotbarSlots.remove(slot);
                     this.hotbarCleanupDelay = 3;
                     return true;
                  }

                  this.pickedHotbarSlots.remove(slot);
               }

               ninth = aM_.field_1724.method_31548().method_5438(8);
               if (!ninth.method_7960() && !this.isArmor(ninth)) {
                  int empty = this.emptyHotbarSlot();
                  if (empty >= 0) {
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, 44, empty, class_1713.field_7791, aM_.field_1724);
                  } else {
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, 44, 0, class_1713.field_7794, aM_.field_1724);
                  }

                  this.hotbarCleanupDelay = 3;
                  return true;
               } else {
                  this.hotbarCleanupPending = false;
                  this.closeAutomationInventory();
                  if (aM_.field_1724.method_31548().method_5438(8).method_7960()) {
                     this.selectSlot(8);
                  }

                  this.screenResumeDelay = 2;
                  return true;
               }
            }
         } else {
            this.automationInventoryOpened = false;
            return false;
         }
      } else {
         return false;
      }
   }

   private int emptyHotbarSlot() {
      for (int slot = 0; slot < 8; slot++) {
         if (aM_.field_1724.method_31548().method_5438(slot).method_7960()) {
            return slot;
         }
      }

      return -1;
   }

   private void keepEmptyHand() {
      if (aM_.field_1724 != null
         && aM_.field_1755 == null
         && this.activeConsumable == null
         && this.previousSelectedSlot < 0
         && !this.brushingStarted
         && !this.automationInventoryOpened) {
         class_1799 ninth = aM_.field_1724.method_31548().method_5438(8);
         if (!ninth.method_7960()) {
            if (!this.isArmor(ninth)) {
               this.hotbarCleanupPending = true;
            }
         } else {
            this.selectSlot(8);
         }
      }
   }

   private void closeAutomationInventory() {
      if (this.automationInventoryOpened && aM_.field_1724 != null && aM_.field_1755 instanceof class_490) {
         aM_.field_1724.method_7346();
      }

      this.automationInventoryOpened = false;
      this.hotbarCleanupDelay = 0;
   }

   private void clearHotbarState() {
      this.pickedHotbarSlots.clear();
      this.hotbarCleanupPending = false;
      this.automationInventoryOpened = false;
      this.hotbarCleanupDelay = 0;
      this.discardCooldown = 0;
      this.discardSettleTicks = 0;
      this.discardStopping = false;
      this.clearSnapshot();
   }

   private boolean isArmor(class_1799 stack) {
      if (stack != null && !stack.method_7960()) {
         class_10192 equippable = (class_10192)stack.method_57824(class_9334.field_54196);
         if (equippable == null) {
            return false;
         } else {
            class_1304 slot = equippable.comp_3174();
            return slot == class_1304.field_6169 || slot == class_1304.field_6174 || slot == class_1304.field_6172 || slot == class_1304.field_6166;
         }
      } else {
         return false;
      }
   }

   private boolean isJunk(class_1799 stack) {
      if (stack != null && !stack.method_7960()) {
         String name = stack.method_7964().getString().toLowerCase(Locale.ROOT);
         if (stack.method_31574(class_1802.field_42716) || name.contains("модификатор")) {
            return false;
         } else if (stack.method_31574(class_1802.field_8107)
            || stack.method_31574(class_1802.field_8236)
            || stack.method_31574(class_1802.field_8087)
            || stack.method_7909() instanceof class_1821
            || stack.method_7909() instanceof class_1794
            || stack.method_7909() instanceof class_1753
            || stack.method_7909() instanceof class_1743) {
            return true;
         } else if (stack.method_7909() instanceof class_1826
            && !stack.method_31574(class_1802.field_8086)
            && !stack.method_31574(class_1802.field_8136)
            && !stack.method_31574(class_1802.field_8503)) {
            return true;
         } else {
            String path = class_7923.field_41178.method_10221(stack.method_7909()).method_12832();
            return !path.endsWith("_armor_trim_smithing_template")
                  && !path.endsWith("_horse_armor")
                  && !path.equals("book")
                  && !path.endsWith("_book")
                  && (!name.contains("сингуляр") || !name.contains("скин"))
                  && !name.contains("хлопушк")
               ? stack.method_31574(class_1802.field_20391)
                  || stack.method_31574(class_1802.field_8153)
                  || stack.method_31574(class_1802.field_8539)
                  || stack.method_31574(class_1802.field_8745)
                  || stack.method_31574(class_1802.field_8606)
                  || stack.method_31574(class_1802.field_8276)
                  || stack.method_31574(class_1802.field_8711)
                  || stack.method_31574(class_1802.field_8536)
                  || stack.method_31574(class_1802.field_8233)
                  || stack.method_31574(class_1802.field_8719)
                  || stack.method_31574(class_1802.field_8140)
                  || stack.method_31574(class_1802.field_8448)
                  || stack.method_31574(class_1802.field_8864)
                  || stack.method_31574(class_1802.field_8469)
                  || stack.method_31574(class_1802.field_8777)
                  || stack.method_31574(class_1802.field_8620)
                  || stack.method_31574(class_1802.field_8137)
                  || stack.method_31574(class_1802.field_8687)
                  || stack.method_31574(class_1802.field_8894)
                  || stack.method_31574(class_1802.field_8070)
                  || stack.method_31574(class_1802.field_8301)
                  || stack.method_31574(class_1802.field_8657)
                  || stack.method_31574(class_1802.field_17515)
                  || stack.method_31574(class_1802.field_20417)
                  || stack.method_31574(class_1802.field_8323)
               : true;
         }
      } else {
         return false;
      }
   }

   private boolean preciseAim(class_2338 pos) {
      if (pos == null) {
         this.resetAim();
         return false;
      } else if (!aM_.field_1724.method_24828()) {
         this.aimYawVelocity = 0.0F;
         this.aimPitchVelocity = 0.0F;
         return false;
      } else {
         if (!pos.equals(this.aimBlock)) {
            this.resetAim();
            this.aimBlock = pos.method_10062();
         }

         ThreadLocalRandom random = ThreadLocalRandom.current();
         if (!this.aimInitialized) {
            this.aimInitialized = true;
            this.aimProfileTicks = random.nextInt(18, 33);
            this.aimYawSpeed = random.nextFloat(10.5F, 13.5F);
            this.aimPitchSpeed = random.nextFloat(7.5F, 10.5F);
            this.aimYawSpeedTarget = this.aimYawSpeed;
            this.aimPitchSpeedTarget = this.aimPitchSpeed;
            this.randomizeAimOffset(random);
         } else if (--this.aimProfileTicks <= 0) {
            this.aimYawSpeedTarget = random.nextFloat(10.0F, 15.0F);
            this.aimPitchSpeedTarget = random.nextFloat(7.0F, 11.5F);
            this.randomizeAimOffset(random);
            this.aimProfileTicks = random.nextInt(18, 33);
         }

         this.aimYawSpeed = this.aimYawSpeed + class_3532.method_15363(this.aimYawSpeedTarget - this.aimYawSpeed, -0.25F, 0.25F);
         this.aimPitchSpeed = this.aimPitchSpeed + class_3532.method_15363(this.aimPitchSpeedTarget - this.aimPitchSpeed, -0.2F, 0.2F);
         this.aimOffsetX = this.aimOffsetX + class_3532.method_15350(this.aimOffsetXTarget - this.aimOffsetX, -0.012, 0.012);
         this.aimOffsetY = this.aimOffsetY + class_3532.method_15350(this.aimOffsetYTarget - this.aimOffsetY, -0.01, 0.01);
         this.aimOffsetZ = this.aimOffsetZ + class_3532.method_15350(this.aimOffsetZTarget - this.aimOffsetZ, -0.012, 0.012);
         class_243 point = pos.method_46558().method_1031(this.aimOffsetX, this.aimOffsetY, this.aimOffsetZ);
         class_243 eye = aM_.field_1724.method_33571();
         double dx = point.field_1352 - eye.field_1352;
         double dy = point.field_1351 - eye.field_1351;
         double dz = point.field_1350 - eye.field_1350;
         double flat = Math.sqrt(dx * dx + dz * dz);
         float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
         float pitch = (float)(-Math.toDegrees(Math.atan2(dy, flat)));
         float yawError = class_3532.method_15393(yaw - aM_.field_1724.method_36454());
         float pitchError = pitch - aM_.field_1724.method_36455();
         float wantedYaw = approach(yawError, this.aimYawSpeed, 34.0F);
         float wantedPitch = approach(pitchError, this.aimPitchSpeed, 24.0F);
         this.aimYawVelocity = this.aimYawVelocity + class_3532.method_15363(wantedYaw - this.aimYawVelocity, -1.75F, 1.75F);
         this.aimPitchVelocity = this.aimPitchVelocity + class_3532.method_15363(wantedPitch - this.aimPitchVelocity, -1.25F, 1.25F);
         float yawStep = Math.abs(this.aimYawVelocity) > Math.abs(yawError) ? yawError : this.aimYawVelocity;
         float pitchStep = Math.abs(this.aimPitchVelocity) > Math.abs(pitchError) ? pitchError : this.aimPitchVelocity;
         aM_.field_1724.method_36456(aM_.field_1724.method_36454() + yawStep);
         aM_.field_1724.method_36457(class_3532.method_15363(aM_.field_1724.method_36455() + pitchStep, -90.0F, 90.0F));
         if (yawStep == yawError) {
            this.aimYawVelocity *= 0.35F;
         }

         if (pitchStep == pitchError) {
            this.aimPitchVelocity *= 0.35F;
         }

         return Math.abs(yawError) <= 2.25F
            && Math.abs(pitchError) <= 1.75F
            && Math.abs(this.aimYawVelocity) <= 2.0F
            && Math.abs(this.aimPitchVelocity) <= 1.6F;
      }
   }

   private static float approach(float error, float maxSpeed, float slowAngle) {
      float absolute = Math.abs(error);
      if (absolute < 0.001F) {
         return 0.0F;
      } else {
         float t = class_3532.method_15363(absolute / slowAngle, 0.0F, 1.0F);
         float eased = t * t * (3.0F - 2.0F * t);
         float speed = Math.max(absolute * 0.3F, maxSpeed * eased);
         return Math.copySign(Math.min(absolute, Math.min(maxSpeed, speed)), error);
      }
   }

   private void randomizeAimOffset(ThreadLocalRandom random) {
      this.aimOffsetXTarget = random.nextDouble(-0.16, 0.16);
      this.aimOffsetYTarget = random.nextDouble(-0.13, 0.15);
      this.aimOffsetZTarget = random.nextDouble(-0.16, 0.16);
   }

   private void resetAim() {
      this.aimBlock = null;
      this.aimInitialized = false;
      this.aimProfileTicks = 0;
      this.aimYawVelocity = 0.0F;
      this.aimPitchVelocity = 0.0F;
      this.aimYawSpeed = 12.0F;
      this.aimPitchSpeed = 9.0F;
      this.aimYawSpeedTarget = 12.0F;
      this.aimPitchSpeedTarget = 9.0F;
      this.aimOffsetX = 0.0;
      this.aimOffsetY = 0.0;
      this.aimOffsetZ = 0.0;
      this.aimOffsetXTarget = 0.0;
      this.aimOffsetYTarget = 0.0;
      this.aimOffsetZTarget = 0.0;
   }

   private class_243 playerPos() {
      return new class_243(aM_.field_1724.method_23317(), aM_.field_1724.method_23318(), aM_.field_1724.method_23321());
   }

   private static enum Cycle {
      FARMING,
      PVP_WAIT,
      DARENA_WAIT,
      HOME_WAIT,
      FIND_CHEST,
      WALK_CHEST,
      OPEN_CHEST,
      DEPOSIT,
      FIND_SUPPLY,
      WALK_SUPPLY,
      OPEN_SUPPLY,
      TAKE_SUPPLY,
      FIND_PORTAL,
      WALK_PORTAL,
      ENTER_PORTAL;
   }

   private record ScanResult(boolean complete, AutoEnd.TargetSelection selection) {
   }

   private static enum Supply {
      FOOD,
      INVISIBILITY,
      SPEED;
   }

   private record TargetSelection(class_2338 pos, boolean brushable) {
   }
}
