package pyrock.classes;


import rockstar.client.internal.script.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import jep.python.PyCallable;
import net.funtimeevents.api.FunTimeEventsAPI;
import net.funtimeevents.model.BanResponse;
import net.funtimeevents.model.BansListResponse;
import net.funtimeevents.model.CaptchaResponse;
import net.funtimeevents.model.ChestResponse;
import net.funtimeevents.model.EventCoordinates;
import net.funtimeevents.model.EventResponse;
import net.funtimeevents.model.LootAreaResponse;
import net.funtimeevents.model.MineResponse;
import net.funtimeevents.model.ObservedPlayer;
import net.funtimeevents.model.PlayerDataResponse;
import net.funtimeevents.model.PlayerGearInfo;
import net.funtimeevents.model.PlayersListResponse;
import net.funtimeevents.model.SystemInfo;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

public class PyFunTime {
   public boolean ready() {
      try {
         return FunTimeEventsAPI.getSystemInfo() != null
            ? true
            : !FunTimeEventsAPI.getEvents().isEmpty()
               || !FunTimeEventsAPI.getMines().isEmpty()
               || !FunTimeEventsAPI.getCopperDungeons().isEmpty()
               || !FunTimeEventsAPI.getWardenCities().isEmpty();
      } catch (Throwable localValue2) {
         return false;
      }
   }

   public List<Map<String, Object>> events() {
      return map(safe(FunTimeEventsAPI::getEvents), PyFunTime::event);
   }

   public List<Map<String, Object>> mines() {
      return map(safe(FunTimeEventsAPI::getMines), PyFunTime::mine);
   }

   public List<Map<String, Object>> copperDungeons() {
      return map(safe(FunTimeEventsAPI::getCopperDungeons), PyFunTime::lootArea);
   }

   public List<Map<String, Object>> wardenCities() {
      return map(safe(FunTimeEventsAPI::getWardenCities), PyFunTime::lootArea);
   }

   @Nullable
   public Map<String, Object> systemInfo() {
      SystemInfo localValue1;
      try {
         localValue1 = FunTimeEventsAPI.getSystemInfo();
      } catch (Throwable localValue3) {
         return null;
      }

      if (localValue1 == null) {
         return null;
      } else {
         LinkedHashMap localValue2 = new LinkedHashMap();
         localValue2.put("events", localValue1.events());
         localValue2.put("mines", localValue1.mines());
         localValue2.put("copper_dungeons", localValue1.copperDungeons());
         localValue2.put("warden_cities", localValue1.wardenCities());
         localValue2.put("clients_connected", localValue1.clientsConnected());
         localValue2.put("tracked_anarchies", localValue1.trackedAnarchies());
         return localValue2;
      }
   }

   public void fetchEvents(@Nullable Map<String, String> localValue1, PyCallable localValue2) {
      deliver(FunTimeEventsAPI.fetchEvents(params(localValue1)), localValue2, localValue0 -> localValue0);
   }

   public void fetchMines(@Nullable Map<String, String> localValue1, PyCallable localValue2) {
      deliver(FunTimeEventsAPI.fetchMines(params(localValue1)), localValue2, localValue0 -> localValue0);
   }

   public void fetchCopperDungeon(PyCallable localValue1) {
      deliver(FunTimeEventsAPI.fetchCopperDungeon(), localValue1, localValue0 -> localValue0);
   }

   public void fetchWardenCity(PyCallable localValue1) {
      deliver(FunTimeEventsAPI.fetchWardenCity(), localValue1, localValue0 -> localValue0);
   }

   public void fetchPlayers(@Nullable Map<String, String> localValue1, PyCallable localValue2) {
      deliver(FunTimeEventsAPI.fetchPlayers(params(localValue1)), localValue2, localValue0 -> {
         ArrayList localValue1x = new ArrayList();
         if (localValue0 instanceof PlayersListResponse && localValue0.data() != null) {
            for (PlayerDataResponse localValue4 : localValue0.data()) {
               localValue1x.add(player(localValue4));
            }
         }

         return localValue1x;
      });
   }

   public void fetchBans(@Nullable Map<String, String> localValue1, PyCallable localValue2) {
      deliver(FunTimeEventsAPI.fetchBans(params(localValue1)), localValue2, localValue0 -> {
         ArrayList localValue1x = new ArrayList();
         if (localValue0 instanceof BansListResponse && localValue0.data() != null) {
            for (BanResponse localValue4 : localValue0.data()) {
               localValue1x.add(ban(localValue4));
            }
         }

         return localValue1x;
      });
   }

   public void solveCaptcha(String localValue1, PyCallable localValue2) {
      deliver(FunTimeEventsAPI.solveCaptcha(localValue1), localValue2, localValue0 -> {
         if (localValue0 instanceof CaptchaResponse) {
            LinkedHashMap localValue2x = new LinkedHashMap();
            localValue2x.put("solved", localValue0.solved());
            localValue2x.put("text", localValue0.text());
            localValue2x.put("percent", localValue0.overallPercent());
            return localValue2x;
         } else {
            return null;
         }
      });
   }

   private static Map<String, Object> event(EventResponse localValue0) {
      LinkedHashMap localValue1 = new LinkedHashMap();
      localValue1.put("server_id", localValue0.serverId());
      localValue1.put("server_type", localValue0.serverType());
      localValue1.put("name", localValue0.name());
      localValue1.put("status", localValue0.status());
      localValue1.put("time_left", localValue0.timeLeft());
      localValue1.put("level", localValue0.level());
      localValue1.put("message", localValue0.message());
      localValue1.put("updated_at", localValue0.updatedAt());
      EventCoordinates localValue2 = localValue0.coordinates();
      if (localValue2 != null) {
         LinkedHashMap localValue3 = new LinkedHashMap();
         localValue3.put("x", localValue2.x());
         localValue3.put("y", localValue2.y());
         localValue3.put("z", localValue2.z());
         localValue1.put("coordinates", localValue3);
      }

      if (localValue0.eventInfo() != null) {
         localValue1.put("mobs", localValue0.eventInfo().mobsCount());
      }

      return localValue1;
   }

   private static Map<String, Object> mine(MineResponse localValue0) {
      LinkedHashMap localValue1 = new LinkedHashMap();
      localValue1.put("server_id", localValue0.serverId());
      localValue1.put("server_type", localValue0.serverType());
      localValue1.put("rarity", localValue0.rarity());
      localValue1.put("time_left", localValue0.timeLeft());
      localValue1.put("updated_at", localValue0.updatedAt());
      if (localValue0.mineInfo() != null && localValue0.mineInfo().playersAround() != null) {
         ArrayList localValue2 = new ArrayList();

         for (ObservedPlayer localValue4 : localValue0.mineInfo().playersAround()) {
            LinkedHashMap localValue5 = new LinkedHashMap();
            localValue5.put("name", String.valueOf(localValue4));
            localValue2.add(localValue5);
         }

         localValue1.put("players_around", localValue2);
      }

      return localValue1;
   }

   private static Map<String, Object> lootArea(LootAreaResponse localValue0) {
      LinkedHashMap localValue1 = new LinkedHashMap();
      localValue1.put("server_id", localValue0.serverId());
      localValue1.put("server_type", localValue0.serverType());
      ArrayList<Map<String, Object>> localValue2 = new ArrayList<>();
      if (localValue0.chests() != null) {
         for (ChestResponse localValue4 : localValue0.chests()) {
            LinkedHashMap localValue5 = new LinkedHashMap();
            localValue5.put("x", localValue4.x());
            localValue5.put("y", localValue4.y());
            localValue5.put("z", localValue4.z());
            localValue5.put("time_left", localValue4.timeLeft());
            localValue5.put("created_at", localValue4.createdAt());
            localValue2.add(localValue5);
         }
      }

      localValue1.put("chests", localValue2);
      ArrayList localValue7 = new ArrayList();
      if (localValue0.players() != null) {
         for (PlayerGearInfo localValue9 : localValue0.players()) {
            LinkedHashMap localValue6 = new LinkedHashMap();
            localValue6.put("name", localValue9.playerName());
            localValue6.put("donate", localValue9.donate());
            localValue6.put("helmet", localValue9.helmet());
            localValue6.put("chestplate", localValue9.chestplate());
            localValue6.put("leggings", localValue9.leggings());
            localValue6.put("boots", localValue9.boots());
            localValue6.put("invisible", localValue9.isInvisible());
            localValue7.add(localValue6);
         }
      }

      localValue1.put("players", localValue7);
      return localValue1;
   }

   private static Map<String, Object> player(PlayerDataResponse localValue0) {
      LinkedHashMap localValue1 = new LinkedHashMap();
      localValue1.put("name", localValue0.playerName());
      localValue1.put("donate", localValue0.donate());
      localValue1.put("active", localValue0.active());
      localValue1.put("server_id", localValue0.serverId());
      localValue1.put("server_type", localValue0.serverType());
      return localValue1;
   }

   private static Map<String, Object> ban(BanResponse localValue0) {
      LinkedHashMap localValue1 = new LinkedHashMap();
      localValue1.put("server_id", localValue0.serverId());
      localValue1.put("server_type", localValue0.serverType());
      localValue1.put("name", localValue0.playerName());
      localValue1.put("reason", localValue0.reason());
      localValue1.put("end", localValue0.end());
      localValue1.put("banned_at", localValue0.bannedAt());
      return localValue1;
   }

   private static Map<String, String> params(@Nullable Map<String, String> localValue0) {
      return localValue0 == null ? new HashMap<>() : new HashMap<>(localValue0);
   }

   private static <T> List<T> safe(Supplier<List<T>> localValue0) {
      try {
         List localValue1 = (List)localValue0.get();
         return localValue1 == null ? List.of() : localValue1;
      } catch (Throwable localValue2) {
         return List.of();
      }
   }

   private static <T> List<Map<String, Object>> map(List<T> localValue0, Function<T, Map<String, Object>> localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (T localValue4 : localValue0) {
         if (localValue4 != null) {
            try {
               localValue2.add(localValue1.apply(localValue4));
            } catch (Throwable localValue6) {
            }
         }
      }

      return localValue2;
   }

   private static <T> void deliver(CompletableFuture<T> localValue0, PyCallable localValue1, Function<T, Object> localValue2) {
      if (localValue1 != null) {
         PythonScript localValue3 = PythonScript.internalMethod00581();
         localValue0.whenComplete((localValue3x, localValue4) -> {
            Object localValue5;
            try {
               localValue5 = localValue4 == null && localValue3x != null ? localValue2.apply(localValue3x) : null;
            } catch (Throwable localValue7) {
               localValue5 = null;
            }

            Object localValue6 = localValue5;
            MinecraftClientAccess.internalField0149.execute(() -> {
               if (localValue3 == null || localValue3.internalMethod08681()) {
                  if (EmbeddedPythonRuntime.internalMethod08724()) {
                     try (AutoCloseable localValue3xx = PythonScript.internalMethod02561(localValue3)) {
                        localValue1.call(new Object[]{localValue6});
                     } catch (Exception localValue8) {
                        RockstarClient.internalField0572.error("Python error in funtime callback:", localValue8);
                     }
                  }
               }
            });
         });
      }
   }
}
