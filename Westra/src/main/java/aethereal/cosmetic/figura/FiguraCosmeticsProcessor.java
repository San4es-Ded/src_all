package aethereal.cosmetic.figura;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.NativeMethodLookup;
import aethereal.util.ChatUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import lombok.Generated;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;

public class FiguraCosmeticsProcessor extends BaseProcessor {
   private final CosmeticsRepository repository = new CosmeticsRepository();
   private boolean loaded;

   @Compile
   @Override
   public void setup() {
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> CosmeticsState.tickRestore());
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> CosmeticsState.onWorldLeave());
   }

   @Generated
   public CosmeticsRepository getRepository() {
      return this.repository;
   }

   public List<CosmeticEntry> getCatalog() {
      this.load();
      return this.repository.entries();
   }

   public boolean equip(CosmeticEntry entry) {
      this.load();
      if (!FiguraBridge.available()) {
         ChatUtil.a("Косметика требует мод &cFigura&7 в папке mods.");
         return false;
      } else {
         boolean ok = CosmeticsState.apply(entry);
         if (ok) {
            this.save();
         } else {
            ChatUtil.a("Не удалось надеть &c" + (entry == null ? "?" : entry.name()) + "&7.");
         }

         return ok;
      }
   }

   public void unequip(CosmeticCategory category) {
      this.load();
      CosmeticsState.clear(category);
      this.save();
   }

   public String selected(CosmeticCategory category) {
      return CosmeticsState.selected(category);
   }

   private void load() {
      if (!this.loaded) {
         this.loaded = true;

         try {
            File file = this.file();
            if (!file.exists()) {
               return;
            }

            JsonObject root = (JsonObject)JsonParser.parseString(Files.readString(file.toPath()));
            if (root == null) {
               return;
            }

            if (root.has("loadout") && root.get("loadout").isJsonPrimitive()) {
               CosmeticsState.load(root.get("loadout").getAsString());
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }

   private void save() {
      try {
         File file = this.file();
         File dir = file.getParentFile();
         if (dir != null && !dir.exists()) {
            dir.mkdirs();
         }

         JsonObject root = new JsonObject();
         root.addProperty("loadout", CosmeticsState.selected());
         Files.writeString(file.toPath(), root.toString());
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   private File file() {
      return new File(new File(new File(aM_.field_1697, "configs"), "general"), "cosmetics.json");
   }

   @Override
   public void unSetup() {
      this.save();
   }

   static {
      NativeMethodLookup.lookup(FiguraCosmeticsProcessor.class, 24);
   }
}
