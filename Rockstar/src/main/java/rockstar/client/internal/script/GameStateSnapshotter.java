package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.inventory.AssistSwapItem;
import rockstar.modules.other.AssistModule;
import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import moscow.rockstar.mixin.accessors.ChatHudAccessor;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

public final class GameStateSnapshotter {
   private GameStateSnapshotter() {
   }

   public static JsonObject internalMethod02895() {
      JsonObject localValue0 = new JsonObject();
      if (MinecraftClientAccess.internalField0149.player != null && MinecraftClientAccess.internalField0149.world != null) {
         localValue0.addProperty("inGame", true);
         localValue0.addProperty("name", MinecraftClientAccess.internalField0149.player.getName().getString());
         localValue0.addProperty("uuid", MinecraftClientAccess.internalField0149.player.getUuidAsString());
         Vec3d localValue1 = MinecraftClientAccess.internalField0149.player.getEntityPos();
         localValue0.add("pos", internalMethod02218(localValue1));
         JsonObject localValue2 = new JsonObject();
         localValue2.addProperty("x", MinecraftClientAccess.internalField0149.player.getBlockX());
         localValue2.addProperty("y", MinecraftClientAccess.internalField0149.player.getBlockY());
         localValue2.addProperty("z", MinecraftClientAccess.internalField0149.player.getBlockZ());
         localValue0.add("blockPos", localValue2);
         localValue0.addProperty("yaw", internalMethod00964(MinecraftClientAccess.internalField0149.player.getYaw(), 2));
         localValue0.addProperty("pitch", internalMethod00964(MinecraftClientAccess.internalField0149.player.getPitch(), 2));
         localValue0.addProperty("facing", MinecraftClientAccess.internalField0149.player.getHorizontalFacing().asString());
         localValue0.add("velocity", internalMethod02218(MinecraftClientAccess.internalField0149.player.getVelocity()));
         localValue0.addProperty("health", internalMethod00964(MinecraftClientAccess.internalField0149.player.getHealth(), 2));
         localValue0.addProperty("maxHealth", internalMethod00964(MinecraftClientAccess.internalField0149.player.getMaxHealth(), 2));
         localValue0.addProperty("absorption", internalMethod00964(MinecraftClientAccess.internalField0149.player.getAbsorptionAmount(), 2));
         localValue0.addProperty("armor", MinecraftClientAccess.internalField0149.player.getArmor());
         localValue0.addProperty("food", MinecraftClientAccess.internalField0149.player.getHungerManager().getFoodLevel());
         localValue0.addProperty("saturation", internalMethod00964(MinecraftClientAccess.internalField0149.player.getHungerManager().getSaturationLevel(), 2));
         localValue0.addProperty("air", MinecraftClientAccess.internalField0149.player.getAir());
         localValue0.addProperty("xpLevel", MinecraftClientAccess.internalField0149.player.experienceLevel);
         localValue0.addProperty(
            "gameMode",
            MinecraftClientAccess.internalField0149.interactionManager == null
               ? "unknown"
               : MinecraftClientAccess.internalField0149.interactionManager.getCurrentGameMode().asString()
         );
         localValue0.addProperty("onGround", MinecraftClientAccess.internalField0149.player.isOnGround());
         localValue0.addProperty("sneaking", MinecraftClientAccess.internalField0149.player.isSneaking());
         localValue0.addProperty("sprinting", MinecraftClientAccess.internalField0149.player.isSprinting());
         localValue0.addProperty("inWater", MinecraftClientAccess.internalField0149.player.isTouchingWater());
         localValue0.addProperty("flying", MinecraftClientAccess.internalField0149.player.getAbilities().flying);
         localValue0.addProperty("alive", MinecraftClientAccess.internalField0149.player.isAlive());
         localValue0.add("hands", internalMethod09921());
         localValue0.add("armorItems", internalMethod08217());
         localValue0.add("effects", internalMethod05449(MinecraftClientAccess.internalField0149.player.getStatusEffects()));
         LivingEntity localValue3 = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
         if (localValue3 != null) {
            JsonObject localValue4 = new JsonObject();
            localValue4.addProperty("name", localValue3.getName().getString());
            localValue4.addProperty("health", internalMethod00964(localValue3.getHealth(), 2));
            localValue4.addProperty("distance", internalMethod00964(MinecraftClientAccess.internalField0149.player.distanceTo(localValue3), 2));
            localValue0.add("combatTarget", localValue4);
         }

         return localValue0;
      } else {
         localValue0.addProperty("inGame", false);
         localValue0.addProperty(
            "account", MinecraftClientAccess.internalField0149.getSession() == null ? "?" : MinecraftClientAccess.internalField0149.getSession().getUsername()
         );
         return localValue0;
      }
   }

   public static JsonObject internalMethod03738() {
      JsonObject localValue0 = new JsonObject();
      if (MinecraftClientAccess.internalField0149.world == null) {
         localValue0.addProperty("loaded", false);
         return localValue0;
      } else {
         localValue0.addProperty("loaded", true);
         localValue0.addProperty("dimension", MinecraftClientAccess.internalField0149.world.getRegistryKey().getValue().toString());
         localValue0.addProperty("time", MinecraftClientAccess.internalField0149.world.getTimeOfDay() % 24000L);
         localValue0.addProperty("day", MinecraftClientAccess.internalField0149.world.getTimeOfDay() / 24000L);
         localValue0.addProperty("raining", MinecraftClientAccess.internalField0149.world.isRaining());
         localValue0.addProperty("thundering", MinecraftClientAccess.internalField0149.world.isThundering());
         localValue0.addProperty("difficulty", MinecraftClientAccess.internalField0149.world.getDifficulty().getName());
         localValue0.addProperty("playersAround", MinecraftClientAccess.internalField0149.world.getPlayers().size());
         if (MinecraftClientAccess.internalField0149.player != null) {
            localValue0.addProperty(
               "biome", MinecraftClientAccess.internalField0149.world.getBiome(MinecraftClientAccess.internalField0149.player.getBlockPos()).getIdAsString()
            );
            localValue0.addProperty("light", MinecraftClientAccess.internalField0149.world.getLightLevel(MinecraftClientAccess.internalField0149.player.getBlockPos()));
         }

         return localValue0;
      }
   }

   public static JsonObject internalMethod08143() {
      JsonObject localValue0 = new JsonObject();
      localValue0.addProperty("connected", MinecraftClientAccess.internalField0149.getNetworkHandler() != null);
      localValue0.addProperty("singleplayer", MinecraftClientAccess.internalField0149.isInSingleplayer());
      ServerInfo localValue1 = MinecraftClientAccess.internalField0149.getCurrentServerEntry();
      if (localValue1 != null) {
         localValue0.addProperty("address", localValue1.address);
         localValue0.addProperty("name", localValue1.name);
         localValue0.addProperty("ping", localValue1.ping);
         if (localValue1.version != null) {
            localValue0.addProperty("version", localValue1.version.getString());
         }
      }

      if (MinecraftClientAccess.internalField0149.getNetworkHandler() != null) {
         Collection localValue2 = MinecraftClientAccess.internalField0149.getNetworkHandler().getPlayerList();
         localValue0.addProperty("online", localValue2.size());
         PlayerListEntry localValue3 = MinecraftClientAccess.internalField0149.player == null
            ? null
            : MinecraftClientAccess.internalField0149.getNetworkHandler().getPlayerListEntry(MinecraftClientAccess.internalField0149.player.getUuid());
         if (localValue3 != null) {
            localValue0.addProperty("ownPing", localValue3.getLatency());
         }
      }

      localValue0.addProperty("tps", internalMethod00964(RockstarClient.getInstance().internalMethod06191().internalMethod00956(), 2));
      return localValue0;
   }

   public static JsonObject internalMethod09093() {
      JsonObject localValue0 = new JsonObject();
      localValue0.addProperty("client", "Rockstar 2.1");
      localValue0.addProperty("minecraft", SharedConstants.getGameVersion().name());
      localValue0.addProperty(
         "account", MinecraftClientAccess.internalField0149.getSession() == null ? "?" : MinecraftClientAccess.internalField0149.getSession().getUsername()
      );
      localValue0.addProperty("fps", MinecraftClientAccess.internalField0149.getCurrentFps());
      localValue0.addProperty("language", LanguageManager.internalMethod00625().name());
      localValue0.addProperty("theme", RockstarClient.getInstance().internalMethod04467().internalMethod05065().name());
      localValue0.addProperty("config", RockstarClient.getInstance().internalMethod02152().internalMethod02481());
      localValue0.addProperty("commandPrefix", RockstarClient.getInstance().internalMethod05348().internalMethod03606());
      localValue0.addProperty("swingPreset", RockstarClient.getInstance().internalMethod00061().internalMethod02484());
      List localValue1 = RockstarClient.getInstance().getModuleManager().getModules();
      JsonArray localValue2 = new JsonArray();

      for (ModuleEntry localValue4 : (Iterable<ModuleEntry>)(Iterable<?>)localValue1) {
         if (localValue4.isEnabled()) {
            localValue2.add(localValue4.getName());
         }
      }

      localValue0.addProperty("modulesTotal", localValue1.size());
      localValue0.add("modulesEnabled", localValue2);
      JsonArray localValue6 = new JsonArray();

      for (PythonScript localValue5 : RockstarClient.getInstance().internalMethod04979().internalMethod02641()) {
         if (localValue5.internalMethod08681()) {
            localValue6.add(localValue5.internalMethod01198());
         }
      }

      localValue0.add("scriptsLoaded", localValue6);
      return localValue0;
   }

   public static JsonObject internalMethod08175() {
      JsonObject localValue0 = new JsonObject();
      localValue0.addProperty("framebufferWidth", MinecraftClientAccess.internalField0149.getWindow().getFramebufferWidth());
      localValue0.addProperty("framebufferHeight", MinecraftClientAccess.internalField0149.getWindow().getFramebufferHeight());
      localValue0.addProperty("guiWidth", MinecraftClientAccess.internalField0149.getWindow().getScaledWidth());
      localValue0.addProperty("guiHeight", MinecraftClientAccess.internalField0149.getWindow().getScaledHeight());
      localValue0.addProperty("guiScale", MinecraftClientAccess.internalField0149.getWindow().getScaleFactor());
      localValue0.addProperty("cursorLocked", MinecraftClientAccess.internalField0149.mouse.isCursorLocked());
      localValue0.addProperty("hudHidden", MinecraftClientAccess.internalField0149.options != null && MinecraftClientAccess.internalField0149.options.hudHidden);
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("x", internalMethod00964((float)MinecraftClientAccess.internalField0149.mouse.getX(), 1));
      localValue1.addProperty("y", internalMethod00964((float)MinecraftClientAccess.internalField0149.mouse.getY(), 1));
      localValue0.add("cursor", localValue1);
      if (MinecraftClientAccess.internalField0149.currentScreen == null) {
         localValue0.add("screen", JsonNull.INSTANCE);
         localValue0.add("screenType", JsonNull.INSTANCE);
      } else {
         localValue0.addProperty(
            "screen",
            MinecraftClientAccess.internalField0149.currentScreen.getTitle() == null
               ? ""
               : MinecraftClientAccess.internalField0149.currentScreen.getTitle().getString()
         );
         localValue0.addProperty("screenType", MinecraftClientAccess.internalField0149.currentScreen.getClass().getSimpleName());
      }

      return localValue0;
   }

   public static JsonArray internalMethod03555(double localValue0, int localValue2, String localValue3) {
      JsonArray localValue4 = new JsonArray();
      if (MinecraftClientAccess.internalField0149.world != null && MinecraftClientAccess.internalField0149.player != null) {
         ArrayList localValue5 = new ArrayList();

         for (Entity localValue7 : MinecraftClientAccess.internalField0149.world.getEntities()) {
            if (localValue7 != MinecraftClientAccess.internalField0149.player
               && !localValue7.isRemoved()
               && !(localValue7.distanceTo(MinecraftClientAccess.internalField0149.player) > localValue0)) {
               boolean localValue8 = switch (localValue3) {
                  case "players" -> localValue7 instanceof PlayerEntity;
                  case "living" -> localValue7 instanceof LivingEntity;
                  default -> true;
               };
               if (localValue8) {
                  localValue5.add(localValue7);
               }
            }
         }

         localValue5.sort(
            (localValue0x, localValue1) -> Float.compare(
               ((net.minecraft.entity.Entity)localValue0x).distanceTo(MinecraftClientAccess.internalField0149.player), ((net.minecraft.entity.Entity)localValue1).distanceTo(MinecraftClientAccess.internalField0149.player)
            )
         );

         for (Entity localValue12 : (Iterable<Entity>)(Iterable<?>)localValue5.subList(0, Math.min(localValue2, localValue5.size()))) {
            JsonObject localValue13 = new JsonObject();
            localValue13.addProperty("name", localValue12.getName().getString());
            localValue13.addProperty("type", Registries.ENTITY_TYPE.getId(localValue12.getType()).toString());
            localValue13.addProperty("distance", internalMethod00964(localValue12.distanceTo(MinecraftClientAccess.internalField0149.player), 2));
            localValue13.add("pos", internalMethod02218(localValue12.getEntityPos()));
            if (localValue12 instanceof LivingEntity localValue9) {
               localValue13.addProperty("health", internalMethod00964(localValue9.getHealth(), 1));
               localValue13.addProperty("maxHealth", internalMethod00964(localValue9.getMaxHealth(), 1));
            }

            if (localValue12 instanceof PlayerEntity localValue14) {
               localValue13.addProperty("player", true);
               localValue13.addProperty("friend", RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue14.getName().getString()));
               localValue13.add("hand", internalMethod02263(localValue14.getMainHandStack()));
            }

            localValue4.add(localValue13);
         }

         return localValue4;
      } else {
         return localValue4;
      }
   }

   public static JsonArray internalMethod03981(int localValue0) {
      JsonArray localValue1 = new JsonArray();
      if (MinecraftClientAccess.internalField0149.inGameHud == null) {
         return localValue1;
      } else {
         List localValue2 = ((ChatHudAccessor)(Object)MinecraftClientAccess.internalField0149.inGameHud.getChatHud()).getMessages();
         int localValue3 = Math.max(0, localValue2.size() - localValue0);

         for (int localValue4 = localValue2.size() - 1; localValue4 >= localValue3; localValue4--) {
            localValue1.add(((ChatHudLine)localValue2.get(localValue4)).content().getString());
         }

         return localValue1;
      }
   }

   public static JsonArray internalMethod02493(String localValue0, boolean localValue1, String localValue2, boolean localValue3) {
      JsonArray localValue4 = new JsonArray();

      for (ModuleEntry localValue6 : RockstarClient.getInstance().getModuleManager().getModules()) {
         if ((localValue3 || !localValue6.internalMethod08983() && localValue6.isAvailable())
            && (!localValue1 || localValue6.isEnabled())
            && (localValue0 == null || localValue0.isBlank() || localValue6.getCategory() == ModuleCategory.internalMethod05008(localValue0))
            && (localValue2 == null || localValue2.isBlank() || localValue6.getName().toLowerCase(Locale.ROOT).contains(localValue2.toLowerCase(Locale.ROOT)))) {
            localValue4.add(internalMethod04760(localValue6, false));
         }
      }

      return localValue4;
   }

   public static JsonObject internalMethod04760(ModuleEntry localValue0, boolean localValue1) {
      JsonObject localValue2 = new JsonObject();
      localValue2.addProperty("name", localValue0.getName());
      localValue2.addProperty("category", localValue0.getCategory().name());
      localValue2.addProperty("enabled", localValue0.isEnabled());
      localValue2.addProperty("hidden", localValue0.internalMethod08983());
      localValue2.addProperty("key", localValue0.getKeybind());
      localValue2.addProperty("keyName", localValue0.getKeybind() <= 0 ? null : TextUtils.internalMethod04982(localValue0.getKeybind()));
      localValue2.addProperty("description", localValue0.internalMethod05655());
      if (localValue1) {
         JsonArray localValue3 = new JsonArray();

         for (Setting localValue5 : localValue0.getSettings()) {
            localValue3.add(internalMethod01496(localValue5));
         }

         localValue2.add("settings", localValue3);
      } else {
         localValue2.addProperty("settings", localValue0.getSettings().size());
      }

      return localValue2;
   }

   public static JsonObject internalMethod01496(Setting localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("name", localValue0.getName());
      localValue1.addProperty("label", LanguageManager.internalMethod07214(localValue0.getName()));
      localValue1.addProperty("type", internalMethod03570(localValue0));
      localValue1.addProperty("visible", localValue0.isVisible());
      if (localValue0 instanceof BooleanSetting localValue2) {
         localValue1.addProperty("value", localValue2.internalMethod04496());
      } else if (localValue0 instanceof SliderSetting localValue3) {
         localValue1.addProperty("value", internalMethod00964(localValue3.internalMethod08576(), 3));
         localValue1.addProperty("min", localValue3.internalMethod05288());
         localValue1.addProperty("max", localValue3.internalMethod05291());
         localValue1.addProperty("step", localValue3.internalMethod08575());
      } else if (localValue0 instanceof RangeSetting localValue4) {
         JsonObject localValue10 = new JsonObject();
         localValue10.addProperty("first", internalMethod00964(localValue4.internalMethod06919(), 3));
         localValue10.addProperty("second", internalMethod00964(localValue4.internalMethod07967(), 3));
         localValue1.add("value", localValue10);
         localValue1.addProperty("min", localValue4.internalMethod07968());
         localValue1.addProperty("max", localValue4.internalMethod07979());
         localValue1.addProperty("step", localValue4.internalMethod07980());
      } else if (localValue0 instanceof ModeSetting localValue5) {
         localValue1.addProperty("value", localValue5.internalMethod07418() == null ? null : localValue5.internalMethod07418().getName());
         localValue1.addProperty("valueLabel", localValue5.internalMethod07418() == null ? null : LanguageManager.internalMethod07214(localValue5.internalMethod07418().getName()));
         localValue1.add("options", internalMethod01661(localValue5));
         localValue1.add("optionLabels", internalMethod07143(internalMethod01661(localValue5)));
      } else if (localValue0 instanceof MultiSelectSetting localValue6) {
         JsonArray localValue13 = new JsonArray();

         for (MultiSelectSetting.InternalType0091 localValue12 : localValue6.internalMethod01792()) {
            if (localValue12.isSelected()) {
               localValue13.add(localValue12.getName());
            }
         }

         localValue1.add("value", localValue13);
         localValue1.add("options", internalMethod07243(localValue6));
         localValue1.add("optionLabels", internalMethod07143(internalMethod07243(localValue6)));
      } else if (localValue0 instanceof ColorSetting localValue7) {
         localValue1.addProperty("value", localValue7.internalMethod05620().toHex());
      } else if (localValue0 instanceof KeybindSetting localValue8) {
         localValue1.addProperty("value", localValue8.internalMethod07477());
         localValue1.addProperty("keyName", localValue8.internalMethod07477() <= 0 ? null : TextUtils.internalMethod04982(localValue8.internalMethod07477()));
      } else if (localValue0 instanceof TextSetting localValue9) {
         localValue1.addProperty("value", localValue9.internalMethod08926());
      } else if (localValue0 instanceof ButtonSetting) {
         localValue1.addProperty(
            "value", "\u043a\u043d\u043e\u043f\u043a\u0430: setting_set \u0441\u043e \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435\u043c \"click\""
         );
      } else {
         localValue1.add("raw", localValue0.toJson());
      }

      return localValue1;
   }

   public static String internalMethod03570(Setting localValue0) {
      if (localValue0 instanceof BooleanSetting) {
         return "boolean";
      } else if (localValue0 instanceof SliderSetting) {
         return "slider";
      } else if (localValue0 instanceof RangeSetting) {
         return "range";
      } else if (localValue0 instanceof ModeSetting) {
         return "mode";
      } else if (localValue0 instanceof MultiSelectSetting) {
         return "select";
      } else if (localValue0 instanceof ColorSetting) {
         return "color";
      } else if (localValue0 instanceof KeybindSetting) {
         return "bind";
      } else if (localValue0 instanceof TextSetting) {
         return "text";
      } else {
         return localValue0 instanceof ButtonSetting ? "button" : "raw";
      }
   }

   public static JsonObject internalMethod09119() {
      JsonObject localValue0 = new JsonObject();
      JsonArray localValue1 = new JsonArray();

      for (ModuleEntry localValue3 : RockstarClient.getInstance().getModuleManager().getModules()) {
         if (localValue3.getKeybind() > 0) {
            JsonObject localValue4 = new JsonObject();
            localValue4.addProperty("module", localValue3.getName());
            localValue4.addProperty("key", localValue3.getKeybind());
            localValue4.addProperty("keyName", TextUtils.internalMethod04982(localValue3.getKeybind()));
            localValue4.addProperty("enabled", localValue3.isEnabled());
            localValue1.add(localValue4);
         }
      }

      localValue0.add("modules", localValue1);
      JsonArray localValue9 = new JsonArray();

      for (ModuleEntry localValue11 : RockstarClient.getInstance().getModuleManager().getModules()) {
         for (Setting localValue6 : localValue11.getSettings()) {
            if (localValue6 instanceof KeybindSetting localValue7 && localValue7.internalMethod07477() > 0) {
               JsonObject localValue8 = new JsonObject();
               localValue8.addProperty("module", localValue11.getName());
               localValue8.addProperty("setting", localValue6.getName());
               localValue8.addProperty("key", localValue7.internalMethod07477());
               localValue8.addProperty("keyName", TextUtils.internalMethod04982(localValue7.internalMethod07477()));
               localValue9.add(localValue8);
            }
         }
      }

      localValue0.add("settings", localValue9);
      JsonArray localValue10 = new JsonArray();
      AssistModule localValue11 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
      if (localValue11 != null) {
         for (AssistSwapItem localValue12 : localValue11.internalMethod08262()) {
            JsonObject localValue13 = new JsonObject();
            localValue13.addProperty("name", localValue12.internalMethod06026());
            localValue13.addProperty("item", Registries.ITEM.getId(localValue12.internalMethod06489().getItem()).toString());
            localValue13.addProperty("key", localValue12.internalMethod03234());
            localValue13.addProperty(
               "keyName",
               localValue12.internalMethod03234() <= 0 ? null : TextUtils.internalMethod04982(localValue12.internalMethod03234())
            );
            localValue13.addProperty("available", localValue12.internalMethod03236());
            localValue10.add(localValue13);
         }
      }

      localValue0.add("items", localValue10);
      localValue0.add("macros", internalMethod02087());
      return localValue0;
   }

   public static JsonArray internalMethod02087() {
      JsonArray localValue0 = new JsonArray();

      for (ChatMacroEntry localValue2 : RockstarClient.getInstance().internalMethod05155().internalMethod06721()) {
         JsonObject localValue3 = new JsonObject();
         localValue3.addProperty("command", localValue2.internalMethod07169());
         localValue3.addProperty("key", localValue2.internalMethod03890());
         localValue3.addProperty("keyName", TextUtils.internalMethod04982(localValue2.internalMethod03890()));
         localValue0.add(localValue3);
      }

      return localValue0;
   }

   public static JsonObject internalMethod09906() {
      JsonObject localValue0 = new JsonObject();
      localValue0.addProperty("active", RockstarClient.getInstance().internalMethod02152().internalMethod02481());
      localValue0.addProperty("autoSave", RockstarClient.getInstance().internalMethod02152().internalMethod03463());
      JsonArray localValue1 = new JsonArray();
      RockstarClient.getInstance().internalMethod02152().internalMethod07433().forEach(localValue1::add);
      localValue0.add("configs", localValue1);
      return localValue0;
   }

   public static JsonArray internalMethod02707() {
      JsonArray localValue0 = new JsonArray();

      for (PythonScript localValue2 : RockstarClient.getInstance().internalMethod04979().internalMethod02641()) {
         JsonObject localValue3 = new JsonObject();
         localValue3.addProperty("name", localValue2.internalMethod01198());
         localValue3.addProperty("loaded", localValue2.internalMethod08681());
         localValue3.addProperty("protected", localValue2.internalMethod05902() == null);
         if (localValue2.internalMethod07951() != null) {
            localValue3.addProperty("error", localValue2.internalMethod07951());
         }

         if (localValue2.internalMethod05902() != null) {
            localValue3.addProperty("path", localValue2.internalMethod05902().getAbsolutePath());
         }

         JsonArray localValue4 = new JsonArray();
         localValue2.internalMethod01625().forEach(localValue1 -> localValue4.add(localValue1.getName()));
         localValue3.add("modules", localValue4);
         localValue0.add(localValue3);
      }

      return localValue0;
   }

   public static JsonObject internalMethod09577() {
      SwingAnimationManager localValue0 = RockstarClient.getInstance().internalMethod00061();
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("current", localValue0.internalMethod02484());
      JsonArray localValue2 = new JsonArray();

      for (SwingPreset localValue4 : localValue0.internalMethod05754()) {
         JsonObject localValue5 = new JsonObject();
         localValue5.addProperty("name", localValue4.internalMethod02665());
         localValue5.addProperty("label", LanguageManager.internalMethod07214(localValue4.internalMethod02665()));
         localValue5.addProperty("source", "builtin");
         localValue2.add(localValue5);
      }

      for (SwingPresetConfig localValue7 : RockstarClient.getInstance().internalMethod01001().internalMethod01720()) {
         JsonObject localValue8 = new JsonObject();
         localValue8.addProperty("name", localValue7.internalMethod02141());
         localValue8.addProperty("label", localValue7.internalMethod02141());
         localValue8.addProperty("source", "file");
         localValue2.add(localValue8);
      }

      localValue1.add("presets", localValue2);
      localValue1.add("shared", internalMethod06660(localValue0.internalMethod04275().getSettings()));
      localValue1.add("start", internalMethod06660(localValue0.internalMethod04274().getSettings()));
      localValue1.add("end", internalMethod06660(localValue0.internalMethod05639().getSettings()));
      return localValue1;
   }

   public static JsonArray internalMethod06660(List<Setting> localValue0) {
      JsonArray localValue1 = new JsonArray();

      for (Setting localValue3 : localValue0) {
         localValue1.add(internalMethod01496(localValue3));
      }

      return localValue1;
   }

   private static JsonArray internalMethod07143(JsonArray localValue0) {
      JsonArray localValue1 = new JsonArray();
      localValue0.forEach(localValue1x -> localValue1.add(LanguageManager.internalMethod07214(localValue1x.getAsString())));
      return localValue1;
   }

   private static JsonArray internalMethod01661(ModeSetting localValue0) {
      JsonArray localValue1 = new JsonArray();

      for (ModeSetting.InternalType0088 localValue3 : localValue0.internalMethod06723()) {
         localValue1.add(localValue3.getName());
      }

      return localValue1;
   }

   private static JsonArray internalMethod07243(MultiSelectSetting localValue0) {
      JsonArray localValue1 = new JsonArray();

      for (MultiSelectSetting.InternalType0091 localValue3 : localValue0.internalMethod01792()) {
         localValue1.add(localValue3.getName());
      }

      return localValue1;
   }

   private static JsonObject internalMethod09921() {
      JsonObject localValue0 = new JsonObject();
      localValue0.add("main", internalMethod02263(MinecraftClientAccess.internalField0149.player.getMainHandStack()));
      localValue0.add("off", internalMethod02263(MinecraftClientAccess.internalField0149.player.getOffHandStack()));
      localValue0.addProperty("selectedSlot", MinecraftClientAccess.internalField0149.player.getInventory().getSelectedSlot());
      return localValue0;
   }

   private static JsonArray internalMethod08217() {
      JsonArray localValue0 = new JsonArray();

      for (ItemStack localValue2 : rockstar.client.util.LegacyItemTypes.armorItems(MinecraftClientAccess.internalField0149.player)) {
         localValue0.add(internalMethod02263(localValue2));
      }

      return localValue0;
   }

   public static JsonArray internalMethod08084() {
      JsonArray localValue0 = new JsonArray();
      if (MinecraftClientAccess.internalField0149.player == null) {
         return localValue0;
      } else {
         DefaultedList localValue1 = MinecraftClientAccess.internalField0149.player.getInventory().getMainStacks();

         for (int localValue2 = 0; localValue2 < localValue1.size(); localValue2++) {
            ItemStack localValue3 = (ItemStack)localValue1.get(localValue2);
            if (!localValue3.isEmpty()) {
               JsonObject localValue4 = internalMethod02263(localValue3);
               localValue4.addProperty("slot", localValue2);
               localValue4.addProperty("hotbar", localValue2 < 9);
               localValue0.add(localValue4);
            }
         }

         return localValue0;
      }
   }

   private static JsonObject internalMethod02263(ItemStack localValue0) {
      JsonObject localValue1 = new JsonObject();
      if (localValue0 != null && !localValue0.isEmpty()) {
         localValue1.addProperty("id", Registries.ITEM.getId(localValue0.getItem()).toString());
         localValue1.addProperty("name", localValue0.getName().getString());
         localValue1.addProperty("count", localValue0.getCount());
         if (localValue0.isDamageable()) {
            localValue1.addProperty("durability", localValue0.getMaxDamage() - localValue0.getDamage());
            localValue1.addProperty("maxDurability", localValue0.getMaxDamage());
         }

         return localValue1;
      } else {
         localValue1.addProperty("id", "minecraft:air");
         localValue1.addProperty("count", 0);
         return localValue1;
      }
   }

   private static JsonArray internalMethod05449(Collection<StatusEffectInstance> localValue0) {
      JsonArray localValue1 = new JsonArray();

      for (StatusEffectInstance localValue3 : localValue0) {
         JsonObject localValue4 = new JsonObject();
         localValue4.addProperty("name", ((StatusEffect)localValue3.getEffectType().value()).getName().getString());
         localValue4.addProperty("id", Registries.STATUS_EFFECT.getId((StatusEffect)localValue3.getEffectType().value()) + "");
         localValue4.addProperty("amplifier", localValue3.getAmplifier() + 1);
         localValue4.addProperty("ticks", localValue3.isInfinite() ? -1 : localValue3.getDuration());
         localValue1.add(localValue4);
      }

      return localValue1;
   }

   private static JsonObject internalMethod02218(Vec3d localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("x", internalMethod00964((float)localValue0.x, 3));
      localValue1.addProperty("y", internalMethod00964((float)localValue0.y, 3));
      localValue1.addProperty("z", internalMethod00964((float)localValue0.z, 3));
      return localValue1;
   }

   private static float internalMethod00964(float localValue0, int localValue1) {
      float localValue2 = (float)Math.pow(10.0, localValue1);
      return Math.round(localValue0 * localValue2) / localValue2;
   }

   public static JsonElement internalMethod05987(String localValue0, JsonElement localValue1) {
      JsonObject localValue2 = new JsonObject();
      localValue2.add(localValue0, localValue1);
      return localValue2;
   }
}
