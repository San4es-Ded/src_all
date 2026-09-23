package pulse.modules.utilities;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import pulse.events.ClientTickEvent;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.settings.BooleanSetting;
import pulse.settings.SliderSetting;

@ModuleInfo(a = "Optimization", b = "РљРѕРјРїР»РµРєСЃРЅР°СЏ РѕРїС‚РёРјРёР·Р°С†РёСЏ РєР»РёРµРЅС‚Р° (GPU Tape, FerriteCore, ThreadTweak, FPS Boost)", c = ModuleCategory.UTILITIES)
public final class Optimization extends ClientModule {
   public static Optimization INSTANCE;
   public final BooleanSetting minecraftSettings = new BooleanSetting("РќР°СЃС‚СЂРѕР№РєРё Minecraft", true);
   public final SliderSetting chunkDistance = new SliderSetting("РџСЂРѕРіСЂСѓР·РєР° С‡Р°РЅРєРѕРІ", 8.0F, 2.0F, 32.0F, 1.0F);
   public final BooleanSetting fastGraphics = new BooleanSetting("Р‘С‹СЃС‚СЂР°СЏ РіСЂР°С„РёРєР°", true);
   public final BooleanSetting dynamicFps = new BooleanSetting("Р”РёРЅР°РјРёС‡РµСЃРєРёР№ FPS", true);
   public final SliderSetting bgFps = new SliderSetting("FPS РІ С„РѕРЅРµ", 10.0F, 5.0F, 60.0F, 5.0F);
   public final BooleanSetting hideEntities = new BooleanSetting("РЎРєСЂС‹С‚РёРµ СЃСѓС‰РЅРѕСЃС‚РµР№", true);
   public final BooleanSetting hidePlayers = new BooleanSetting("РЎРєСЂС‹РІР°С‚СЊ РёРіСЂРѕРєРѕРІ", false);
   public final BooleanSetting hideBlockEntities = new BooleanSetting("РЎРєСЂС‹С‚РёРµ Р±Р»РѕРє-СЌРЅС‚РёС‚Рё", true);
   public final SliderSetting blockEntityDistance = new SliderSetting("Р”РёСЃС‚Р°РЅС†РёСЏ Р±Р»РѕРє-СЌРЅС‚РёС‚Рё", 64.0F, 8.0F, 128.0F, 8.0F);
   public final BooleanSetting limitParticles = new BooleanSetting("Р›РёРјРёС‚ С‡Р°СЃС‚РёС†", true);
   public final SliderSetting particleDistance = new SliderSetting("Р”РёСЃС‚Р°РЅС†РёСЏ С‡Р°СЃС‚РёС†", 32.0F, 8.0F, 128.0F, 4.0F);
   public final SliderSetting particleCap = new SliderSetting("РџРѕС‚РѕР»РѕРє С‡Р°СЃС‚РёС†", 1500.0F, 100.0F, 5000.0F, 100.0F);
   public final BooleanSetting noWallParticles = new BooleanSetting("РќРµ СЃРїР°РІРЅРёС‚СЊ Р·Р° СЃС‚РµРЅР°РјРё", true);
   public final BooleanSetting limitItems = new BooleanSetting("Р›РёРјРёС‚ РїСЂРµРґРјРµС‚РѕРІ", true);
   public final SliderSetting maxItemsPerBlock = new SliderSetting("Р РµРЅРґРµСЂ РЅР° Р±Р»РѕРє", 4.0F, 1.0F, 16.0F, 1.0F);
   public final BooleanSetting noTickExcess = new BooleanSetting("РќРµ С‚РёРєР°С‚СЊ Р»РёС€РЅРёРµ", true);
   public final BooleanSetting frameDistance = new BooleanSetting("Р”РёСЃС‚Р°РЅС†РёСЏ СЂР°РјРѕРє", true);
   public final SliderSetting itemFrameDistance = new SliderSetting("Р Р°РјРєРё СЃ РїСЂРµРґРјРµС‚РѕРј", 64.0F, 8.0F, 128.0F, 8.0F);
   public final SliderSetting mapFrameDistance = new SliderSetting("Р Р°РјРєРё СЃ РєР°СЂС‚РѕР№", 32.0F, 8.0F, 128.0F, 8.0F);
   public final BooleanSetting nearSignText = new BooleanSetting("РўРµРєСЃС‚ С‚Р°Р±Р»РёС‡РµРє РІР±Р»РёР·Рё", true);
   public final SliderSetting signTextDistance = new SliderSetting("Р”РёСЃС‚Р°РЅС†РёСЏ С‚РµРєСЃС‚Р°", 16.0F, 4.0F, 64.0F, 4.0F);
   public final BooleanSetting simpleLeaves = new BooleanSetting("РЈРїСЂРѕС‰С‘РЅРЅР°СЏ Р»РёСЃС‚РІР°", true);
   public final BooleanSetting cacheSkyColor = new BooleanSetting("РљСЌС€ С†РІРµС‚Р° РЅРµР±Р°", true);
   public final BooleanSetting lessLightUpdates = new BooleanSetting("Р РµР¶Рµ РѕР±РЅРѕРІР»СЏС‚СЊ СЃРІРµС‚", true);
   public final BooleanSetting skipEmptyToasts = new BooleanSetting("РџСЂРѕРїСѓСЃРє РїСѓСЃС‚С‹С… С‚РѕСЃС‚РѕРІ", true);
   public final BooleanSetting textBatching = new BooleanSetting("Р‘Р°С‚С‡РёРЅРі С‚РµРєСЃС‚Р°", true);
   public final BooleanSetting largeFontAtlas = new BooleanSetting("РљСЂСѓРїРЅС‹Р№ Р°С‚Р»Р°СЃ С€СЂРёС„С‚Р°", true);
   public final BooleanSetting smoothChunkLoading = new BooleanSetting("РЎРіР»Р°Р¶РёРІР°РЅРёРµ РїСЂРѕРіСЂСѓР·РєРё", true);
   public final SliderSetting chunksPerFrame = new SliderSetting("Р§Р°РЅРєРѕРІ Р·Р° РєР°РґСЂ", 128.0F, 16.0F, 512.0F, 16.0F);
   public final BooleanSetting fixMemoryLeaks = new BooleanSetting("Р¤РёРєСЃС‹ СѓС‚РµС‡РµРє РїР°РјСЏС‚Рё", true);
   private int tickCounter = 0;
   private int currentParticleCount = 0;
   private int originalViewDistance = -1;
   private boolean originalAo = true;
   private int originalMaxFps = -1;
   private boolean wasUnfocused = false;
   private Boolean originalRawMouse = null;
   private Boolean originalVsync = null;
   private Thread optimizedThread = null;
   private int originalThreadPriority = 5;
   private final Map<Thread, Integer> originalWorkerPriorities = new ConcurrentHashMap<>();

   public Optimization() {
      INSTANCE = this;
      this.collectSettings();
   }

   @Override
   public void onEnable() {
      super.onEnable();
      INSTANCE = this;
      if (c.options != null) {
         try {
            if (this.originalViewDistance == -1) {
               this.originalViewDistance = (Integer)c.options.getViewDistance().getValue();
            }

            this.originalAo = (Boolean)c.options.getAo().getValue();
            if (this.originalMaxFps == -1) {
               this.originalMaxFps = (Integer)c.options.getMaxFps().getValue();
            }
         } catch (Throwable var3) {
         }
      }

      this.applyThreadTweak();
      this.tuneWorkerThreads();
      this.applyLowLatencySettings();
      this.applyMinecraftSettings();
      if (c.worldRenderer != null) {
         try {
            c.worldRenderer.reload();
         } catch (Throwable var2) {
         }
      }
   }

   @Override
   public void onDisable() {
      INSTANCE = null;
      super.onDisable();
      if (this.optimizedThread != null) {
         try {
            this.optimizedThread.setPriority(this.originalThreadPriority);
         } catch (Throwable var4) {
         }

         this.optimizedThread = null;
      }

      this.restoreWorkerThreads();
      if (c.options != null) {
         try {
            if (this.originalViewDistance > 0) {
               c.options.getViewDistance().setValue(this.originalViewDistance);
            }

            c.options.getAo().setValue(this.originalAo);
            if (this.originalMaxFps > 0) {
               c.options.getMaxFps().setValue(this.originalMaxFps);
            }

            if (this.originalRawMouse != null) {
               c.options.getRawMouseInput().setValue(this.originalRawMouse);
            }

            if (this.originalVsync != null) {
               c.options.getEnableVsync().setValue(this.originalVsync);
            }
         } catch (Throwable var3) {
         }
      }

      this.originalViewDistance = -1;
      this.originalMaxFps = -1;
      this.originalRawMouse = null;
      this.originalVsync = null;
      this.wasUnfocused = false;
      if (c.worldRenderer != null) {
         try {
            c.worldRenderer.reload();
         } catch (Throwable var2) {
         }
      }
   }

   @EventHandler
   public void onTick(ClientTickEvent event) {
      if (c.player != null && c.world != null) {
         this.tickCounter++;
         this.currentParticleCount = 0;
         if (this.tickCounter % 20 == 0) {
            this.applyMinecraftSettings();
         }

         if (this.tickCounter % 100 == 0) {
            this.tuneWorkerThreads();
         }

         if (this.dynamicFps.get() && c.options != null) {
            try {
               if (!c.isWindowFocused()) {
                  if (!this.wasUnfocused) {
                     if (this.originalMaxFps == -1) {
                        this.originalMaxFps = (Integer)c.options.getMaxFps().getValue();
                     }

                     c.options.getMaxFps().setValue(this.bgFps.roundedInt());
                     this.wasUnfocused = true;
                  }
               } else if (this.wasUnfocused) {
                  if (this.originalMaxFps > 0) {
                     c.options.getMaxFps().setValue(this.originalMaxFps);
                  }

                  this.wasUnfocused = false;
               }
            } catch (Throwable var3) {
            }
         }
      }
   }

   private void applyThreadTweak() {
      try {
         this.optimizedThread = Thread.currentThread();
         this.originalThreadPriority = this.optimizedThread.getPriority();
         this.optimizedThread.setPriority(Math.min(10, 7));
      } catch (Throwable var2) {
      }
   }

   private void tuneWorkerThreads() {
      try {
         for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread != null && thread.isAlive()) {
               String name = thread.getName();
               boolean renderCritical = name.equals("Render thread")
                  || name.equals("Client thread")
                  || name.contains("Chunk Render")
                  || name.contains("Main-Worker")
                  || name.contains("Worker-Main")
                  || name.contains("ForkJoinPool.commonPool-worker");
               if (renderCritical) {
                  this.originalWorkerPriorities.putIfAbsent(thread, thread.getPriority());
                  int target = !name.equals("Render thread") && !name.equals("Client thread") ? Math.min(10, 7) : 10;
                  if (thread.getPriority() != target) {
                     thread.setPriority(target);
                  }
               }
            }
         }
      } catch (Throwable var6) {
      }
   }

   private void restoreWorkerThreads() {
      for (Map.Entry<Thread, Integer> entry : this.originalWorkerPriorities.entrySet()) {
         try {
            Thread thread = entry.getKey();
            if (thread != null && thread.isAlive()) {
               thread.setPriority(entry.getValue());
            }
         } catch (Throwable var4) {
         }
      }

      this.originalWorkerPriorities.clear();
   }

   private void applyLowLatencySettings() {
      if (c.options != null) {
         try {
            if (this.originalRawMouse == null) {
               this.originalRawMouse = (Boolean)c.options.getRawMouseInput().getValue();
            }

            if (this.originalVsync == null) {
               this.originalVsync = (Boolean)c.options.getEnableVsync().getValue();
            }

            c.options.getRawMouseInput().setValue(true);
            c.options.getEnableVsync().setValue(false);
            c.options.getMaxFps().setValue(260);
         } catch (Throwable var2) {
         }
      }
   }

   private void applyMinecraftSettings() {
      if (this.minecraftSettings.get() && c.options != null) {
         try {
            int targetDistance = this.chunkDistance.roundedInt();
            if ((Integer)c.options.getViewDistance().getValue() != targetDistance) {
               c.options.getViewDistance().setValue(targetDistance);
            }

            if (this.fastGraphics.get() && (Boolean)c.options.getAo().getValue()) {
               c.options.getAo().setValue(false);
            }
         } catch (Throwable var2) {
         }
      }
   }

   private void cleanupMemoryLeaks() {
   }

   public static boolean shouldRenderEntity(Entity entity) {
      return true;
   }

   public static boolean shouldRenderBlockEntity(BlockEntity blockEntity) {
      return true;
   }

   public static boolean shouldSpawnParticle(double x, double y, double z) {
      Optimization opt = INSTANCE;
      if (opt != null && opt.k() && opt.limitParticles.get()) {
         MinecraftClient client = MinecraftClient.getInstance();
         if (client.player == null) {
            return true;
         }

         double dx = client.player.getX() - x;
         double dy = client.player.getY() - y;
         double dz = client.player.getZ() - z;
         double max = opt.particleDistance.get();
         if (dx * dx + dy * dy + dz * dz > max * max) {
            return false;
         }

         if (opt.currentParticleCount >= opt.particleCap.roundedInt()) {
            return false;
         }

         opt.currentParticleCount++;
         return true;
      } else {
         return true;
      }
   }

   public static int getTargetFps(int defaultFps) {
      if (INSTANCE != null && INSTANCE.l() && INSTANCE.dynamicFps.get()) {
         MinecraftClient client = MinecraftClient.getInstance();
         return !client.isWindowFocused() ? INSTANCE.bgFps.roundedInt() : defaultFps;
      } else {
         return defaultFps;
      }
   }
}
