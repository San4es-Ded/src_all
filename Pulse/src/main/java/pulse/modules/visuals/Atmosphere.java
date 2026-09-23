package pulse.modules.visuals;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import pulse.events.PacketEvent;
import pulse.events.PacketEvent.MessageDirection;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.settings.ModeSetting;

@ModuleInfo(a = "Atmosphere", b = "РќР°СЃС‚СЂРѕР№РєР° РІСЂРµРјРµРЅРё СЃСѓС‚РѕРє Рё РїРѕРіРѕРґС‹", c = ModuleCategory.VISUALS)
public class Atmosphere extends ClientModule {
   public final ModeSetting timeOfDay = new ModeSetting(
      "Р’СЂРµРјСЏ СЃСѓС‚РѕРє", new String[]{"РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ", "Р”РµРЅСЊ", "Р—Р°РєР°С‚", "РќРѕС‡СЊ", "РџРѕР»РЅРѕС‡СЊ", "Р Р°СЃСЃРІРµС‚"}, "РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ"
   );
   public final ModeSetting weather = new ModeSetting("РџРѕРіРѕРґР°", new String[]{"РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ", "РЇСЃРЅРѕ", "Р”РѕР¶РґСЊ", "РЎРЅРµРі", "Р“СЂРѕР·Р°"}, "РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ");

   public boolean isTimeCustom() {
      return this.k() && !this.timeOfDay.selectedValue().equals("РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ");
   }

   public long getCustomTime() {
      if (!this.k()) {
         return -1L;
      }

      String mode = this.timeOfDay.selectedValue();
      switch (mode) {
         case "Р”РµРЅСЊ":
            return 6000L;
         case "Р—Р°РєР°С‚":
            return 12500L;
         case "РќРѕС‡СЊ":
            return 14000L;
         case "РџРѕР»РЅРѕС‡СЊ":
            return 18000L;
         case "Р Р°СЃСЃРІРµС‚":
            return 23000L;
         default:
            return -1L;
      }
   }

   public boolean isWeatherCustom() {
      return this.k() && !this.weather.selectedValue().equals("РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ");
   }

   public String getWeatherMode() {
      return this.k() ? this.weather.selectedValue() : "РљР°Рє РЅР° СЃРµСЂРІРµСЂРµ";
   }

   @EventHandler
   public void onPacket(PacketEvent event) {
      if (this.k() && this.isTimeCustom()) {
         if (event.e() == PacketEvent.MessageDirection.RECIEVE && event.d() instanceof WorldTimeUpdateS2CPacket) {
            event.b();
         }
      }
   }
}
