 package su.sacura.features.modules.impl.combat;
 
 import com.google.common.eventbus.Subscribe;
 import java.awt.Color;
 import net.minecraft.text.MutableText;
 import net.minecraft.text.Style;
 import net.minecraft.text.Text;
 import net.minecraft.text.TextColor;
 import net.minecraft.util.Formatting;
 import su.sacura.events.player.EventAttack;
 import su.sacura.events.tick.TickEvent;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleAnnotations;
 import su.sacura.features.modules.impl.Category;
 import su.sacura.util.impl.render.providers.ColorProvider;
 
 @ModuleAnnotations(name = "Shift Tap", category = Category.PLAYER)
 public class ShiftTapModule extends Module {
   long shiftTapEndTime = 0L;
   
   boolean isModuleControllingSneak = false;
   
   private void startShiftTap() {
     this.shiftTapEndTime = System.currentTimeMillis() + 25L;
     if (!this.isModuleControllingSneak) {
       mc.options.sneakKey.setPressed(true);
       this.isModuleControllingSneak = true;
     } 
   }
   
   private void stopShiftTap() {
     if (this.isModuleControllingSneak) {
       mc.options.sneakKey.setPressed(false);
       this.isModuleControllingSneak = false;
     } 
   }
   
   @Subscribe
   public void onAttack(EventAttack e) {
     if (mc.player == null)
       return; 
     startShiftTap();
   }
   
   @Subscribe
   public void onTick(TickEvent e) {
     if (mc.player == null || mc.player.isSneaking()) {
       stopShiftTap();
       return;
     } 
     if (mc.getCurrentServerEntry() != null) {
       String serverAddress = (mc.getCurrentServerEntry()).address.toLowerCase();
       if (serverAddress.contains("holyworld") || serverAddress.contains("spacetimes")) {
         if (this.enable) {
           toggle();
           message(String.valueOf(Formatting.RED) + "Эта функция запрещена на этом сервере!");
         } 
         return;
       } 
     } 
     long currentTime = System.currentTimeMillis();
     if (this.isModuleControllingSneak && currentTime > this.shiftTapEndTime)
       stopShiftTap(); 
   }
   
   public static void message(String string) {
     if (mc == null || mc.player == null || mc.world == null || mc.inGameHud == null)
       return; 
     int start = ColorProvider.getColorStyle(1.0F);
     int end = ColorProvider.getColorStyle(100.0F);
     mc.inGameHud.getChatHud().addMessage(applyGradient(string, start, end));
   }
   
   private static Text applyGradient(String string, int startColor, int endColor) {
     MutableText component = Text.empty();
     String name = "(sacura)";
     int length = "(sacura)".length();
     float inv = (length <= 1) ? 0.0F : (1.0F / (length - 1));
     for (int i = 0; i < length; i++) {
       int rgb = ColorProvider.blendColors(startColor, endColor, (length == 1) ? 0.5F : (i * inv)) & 0xFFFFFF;
       component.append((Text)Text.literal(String.valueOf("(sacura)".charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)).withBold(Boolean.valueOf(true))));
     } 
     int gray = Color.GRAY.getRGB() & 0xFFFFFF;
     component.append((Text)Text.literal(" >> ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gray)).withBold(Boolean.valueOf(true))));
     component.append((Text)Text.literal(string).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));
     return (Text)component;
   }
 }


