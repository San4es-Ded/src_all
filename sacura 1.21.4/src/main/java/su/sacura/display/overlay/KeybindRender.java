 package su.sacura.display.overlay;
 
 import java.awt.Color;
 import net.minecraft.client.gui.DrawContext;
 import org.joml.Matrix4f;
 import org.lwjgl.glfw.GLFW;
 import su.sacura.Sacura;
 import su.sacura.display.csgui.helper.ThemeStorage;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.api.core.ModuleManager;
 import su.sacura.util.impl.math.helper.MathUtil;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.ColorProvider;
 import su.sacura.util.impl.render.providers.FontProvider;
 import su.sacura.util.impl.system.KeyUtil;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class KeybindRender implements MinecraftWrapper {
   private static float x = 100.0F;
   
   private static float y = 100.0F;
   
   private static float width;
   
   private static float height;
   
   private static float widthOut;
   
   private static float heightOut;
   
   private static float alpha;
   
   private static boolean dragging = false;
   
   private static float dragOffsetX;
   
   private static float dragOffsetY;
   
   private static boolean wasMouseDown = false;
   
   public static void render(DrawContext drawContext, int scWidth, int scHeight) {
                System.out.println("[KeybindRender] render called");
     setPosition();
     double mouseX = mc.mouse.getX() * mc.getWindow().getScaledWidth() / mc.getWindow().getWidth();
     double mouseY = mc.mouse.getY() * mc.getWindow().getScaledHeight() / mc.getWindow().getHeight();
     boolean isMouseDown = (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1);
     if (mc.currentScreen instanceof net.minecraft.client.gui.screen.ChatScreen)
       if (dragging) {
         x = (float)(mouseX - dragOffsetX);
         y = (float)(mouseY - dragOffsetY);
         if (!isMouseDown)
           dragging = false; 
       } else if (isMouseDown && !wasMouseDown) {
         String str = "HotKeys";
         float f1 = FontProvider.regular.getHeight(8.0F) + 8.0F;
         float f2 = FontProvider.regular.getWidth(str, 8.0F) + FontProvider.icons.getWidth("C", 10.0F) + 15.0F;
         if (mouseX >= x && mouseX <= (x + Math.max(width, f2)) && mouseY >= y && mouseY <= (y + height)) {
           dragging = true;
           dragOffsetX = (float)(mouseX - x);
           dragOffsetY = (float)(mouseY - y);
         } 
       }  
     wasMouseDown = isMouseDown;
     Matrix4f matrix = drawContext.getMatrices().peek().getPositionMatrix();
     String title = "HotKeys";
     float titleHeight = FontProvider.regular.getHeight(8.0F) + 8.0F;
     float titleRenderWidth = FontProvider.regular.getWidth(title, 8.0F) + FontProvider.icons.getWidth("C", 10.0F) + 15.0F;
     ModuleManager moduleManager = Sacura.getInstance().getModuleManager();
     boolean anyModuleEnabled = false;
     for (Module f : moduleManager.module) {
       if (f.bind.getKey() != -1 && f.enable) {
         anyModuleEnabled = true;
         break;
       } 
     } 
     Color accent = ThemeStorage.accentColor;
     Color bg = ThemeStorage.backgroundColor;
     Color text = ThemeStorage.textColor;
     int bgRGB = (new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 200)).getRGB();
     int textRGB = text.getRGB();
     if (anyModuleEnabled || mc.currentScreen instanceof net.minecraft.client.gui.screen.ChatScreen) {
       RenderUtils.blur(matrix, x, y, titleRenderWidth, titleHeight, 5.0F, 10.0F, textRGB);
       RenderUtils.rect(matrix, x, y, titleRenderWidth, titleHeight, 5.0F, bgRGB);
       FontProvider.icons.draw(matrix, "C", x + 5.0F, y + titleHeight / 2.0F - FontProvider.icons.getHeight(10.0F) / 2.0F + 2.5F, 10.0F, ColorProvider.wave(accent.getRGB(), (new Color(10, 5, 20)).getRGB(), 1.0D));
       FontProvider.regular.drawWave(matrix, title, x + 4.0F + FontProvider.icons.getWidth("C", 10.0F) + 5.0F, y + titleHeight / 2.0F - FontProvider.regular.getHeight(8.0F) / 2.0F, 8.0F, textRGB, (new Color(100, 100, 100)).getRGB());
     } 
     if (!anyModuleEnabled) {
       width = titleRenderWidth;
       height = titleHeight;
       return;
     } 
     float modulesStartY = y + titleHeight + 2.0F;
     float modulesHeight = 0.0F;
     float spacing = 0.5F;
     float maxWidth = 80.0F;
     for (Module f : moduleManager.module) {
       f.animAlpha = MathUtil.lerp(f.animAlpha, f.enable ? 1.0F : 0.0F, 10.0F);
       if (f.bind.getKey() == -1 || f.animAlpha <= 0.05F)
         continue; 
       String textKey = KeyUtil.getKey(f.bind.getKey());
       float iconWidth = FontProvider.regular.getWidth(f.category.iconChar, 11.0F);
       float nameWidth = FontProvider.regular.getWidth(f.name, 7.0F);
       float keyWidth = FontProvider.regular.getWidth(textKey, 7.0F);
       float localWidth = 3.0F + iconWidth + 5.0F + nameWidth + 15.0F + keyWidth + 6.0F;
       if (localWidth > maxWidth)
         maxWidth = localWidth; 
       modulesHeight += 14.0F + spacing;
     } 
     float yy = 0.0F;
     for (Module f : moduleManager.module) {
       if (f.bind.getKey() == -1 || f.animAlpha <= 0.05F)
         continue; 
       String textName = f.name;
       String textKey = KeyUtil.getKey(f.bind.getKey());
       String icon = f.category.iconChar;
       float iconWidth = FontProvider.regular.getWidth(icon, 11.0F);
       float nameWidth = FontProvider.regular.getWidth(f.name, 7.0F);
       float keyWidth = FontProvider.regular.getWidth(textKey, 7.0F);
       float localWidth = 3.0F + iconWidth + 5.0F + nameWidth + 15.0F + keyWidth + 6.0F;
       int textAlpha = (int)(255.0F * f.animAlpha * alpha);
       int textAlphaI = (int)(50.0F * f.animAlpha * alpha);
       int bgAlpha = (int)(200.0F * f.animAlpha * alpha);
       int bgRGB1 = (new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 200)).getRGB();
       float entryY = modulesStartY + yy;
       float entryHeight = 15.0F;
       RenderUtils.blur(matrix, x, entryY, localWidth, entryHeight, 5.0F, 10.0F, (new Color(255, 255, 255, 255)).getRGB());
       RenderUtils.rect(matrix, x, entryY, localWidth, entryHeight, 5.0F, bgRGB1);
       float separatorX = x + localWidth - keyWidth - 12.0F;
       RenderUtils.rect(matrix, separatorX, entryY + 4.0F, 1.0F, 8.0F, 0.0F, ColorProvider.rgba(255, 255, 255, textAlphaI));
       float iconY = entryY + entryHeight / 2.0F - FontProvider.regular.getHeight(11.0F) / 2.0F;
       float nameY = entryY + entryHeight / 2.0F - FontProvider.regular.getHeight(7.0F) / 2.0F;
       float keyY = entryY + entryHeight / 2.0F - FontProvider.regular.getHeight(7.0F) / 2.0F;
       FontProvider.regular.draw(matrix, icon, x + 3.0F, iconY - 1.0F, 11.0F, ColorProvider.rgba(255, 255, 255, textAlpha));
       FontProvider.regular.draw(matrix, textName, x + 3.0F + iconWidth + 5.0F, nameY, 7.0F, ColorProvider.rgba(255, 255, 255, textAlpha));
       FontProvider.regular.draw(matrix, textKey, x + localWidth - 6.0F - keyWidth, keyY, 7.0F, ColorProvider.rgba(255, 255, 255, textAlpha));
       yy += entryHeight + spacing;
     } 
     width = Math.max(titleRenderWidth, maxWidth);
     height = titleHeight + 5.0F + modulesHeight;
   }
   
   private static void setPosition() {
     widthOut = MathUtil.lerp(widthOut, width, 0.1F);
     heightOut = MathUtil.lerp(heightOut, height, 0.1F);
     alpha = MathUtil.lerp(alpha, (height > 16.0F) ? 1.0F : 0.0F, 0.1F);
   }
   
   public static void onMouseClick(double mouseX, double mouseY, int button) {
     String title = "HotKeys";
     float titleHeight = FontProvider.regular.getHeight(8.0F) + 6.0F;
     float titleRenderWidth = FontProvider.regular.getWidth(title, 8.0F) + FontProvider.icons.getWidth("C", 10.0F) + 15.0F;
     if (button == 0 && mouseX >= x && mouseX <= (x + titleRenderWidth) && mouseY >= y && mouseY <= (y + titleHeight)) {
       dragging = true;
       dragOffsetX = (float)(mouseX - x);
       dragOffsetY = (float)(mouseY - y);
     } 
   }
 }


