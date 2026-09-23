package aethereal.config;

import aethereal.api.Compile;
import aethereal.core.EventTarget;
import aethereal.core.NativeMethodLookup;
import aethereal.event.BackendEvent;
import aethereal.network.PacketSecurity;
import aethereal.util.ChatUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ResourcePacksProcessor extends BaseProcessor {
   @Compile
   @Override
   public void setup() {
   }

   @Override
   public void unSetup() {
   }

   @EventTarget
   public void a(BackendEvent event) {
      if (event.b() && "resource-packs".equals(event.d().b())) {
         PacketSecurity security = event.d().a();
         String payload = event.d().c();
         String pack = security.a(payload, "pack");
         String archive = security.a(payload, "archive");
         if (pack != null && archive != null) {
            File directory = new File(aM_.field_1697, "resourcepacks");
            if (!directory.exists()) {
               directory.mkdirs();
            }

            try {
               Files.write(new File(directory, pack + ".zip").toPath(), Base64.getDecoder().decode(archive));
            } catch (IOException var8) {
               ChatUtil.a("&c✖ &7Не удалось сохранить ресурс-пак &a" + pack);
               return;
            }

            ChatUtil.a("&a✔ &7Ресурс-пак &a" + pack + " &7успешно добавлен в список доступных ресурс-паков.");
         }
      }
   }

   static {
      NativeMethodLookup.lookup(ResourcePacksProcessor.class, 35);
   }
}
