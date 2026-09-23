 package su.sacura.display.csgui.components.module;
 
 import java.awt.Color;
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.client.gui.DrawContext;
 import net.minecraft.client.texture.AbstractTexture;
 import net.minecraft.util.Identifier;
 import su.sacura.display.csgui.helper.ThemeStorage;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.util.impl.math.helper.AnimationUtil;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.FontProvider;
 
 public class BooleanComponent extends Component {
   public BooleanSetting setting;
   
   private float progress = 0.0F;
   
   private long lastTime = 0L;
   
   public BooleanComponent(BooleanSetting setting, float width, float height) {
     super(width, height);
     this.setting = setting;
   }
   
   public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
     this.x = x;
     this.y = y;
     this.height = getHeight();
     long currentTime = System.nanoTime();
     if (this.lastTime == 0L)
       this.lastTime = currentTime; 
     float delta = (float)(currentTime - this.lastTime) / 1.0E9F;
     this.lastTime = currentTime;
     if (delta > 0.1F)
       delta = 0.1F; 
     boolean enabled = ((Boolean)this.setting.get()).booleanValue();
     this.progress = AnimationUtil.lerp(this.progress, enabled ? 1.0F : 0.0F, Math.min(1.0F, delta * 10.0F));
     String desc = this.setting.getDescription();
     boolean hasDesc = (desc != null && !desc.isEmpty());
     if (hasDesc) {
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), desc, x + 5.0F, y + 15.0F, 6.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
     } else {
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + this.height / 2.0F - 3.5F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     } 
     Color accent = ThemeStorage.accentColor;
     Color borderC = ThemeStorage.borderColor;
     Color textC = ThemeStorage.textColor;
     float boxSize = 10.0F;
     float boxX = x + this.width - boxSize - 7.0F;
     float boxY = y + (this.height - boxSize) / 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), boxX, boxY, boxSize, boxSize, 2.0F, (new Color(borderC.getRed(), borderC.getGreen(), borderC.getBlue(), globalAlpha)).getRGB());
     if (this.progress > 0.3F) {
       int r = accent.getRed();
       int g = accent.getGreen();
       int b = accent.getBlue();
       int a = (int)(globalAlpha * this.progress);
       int color = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
       RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), boxX, boxY, boxSize, boxSize, 2.0F, color);
       AbstractTexture abstractTexture = MinecraftClient.getInstance().getTextureManager().getTexture(Identifier.ofVanilla("sacura/images/checkmark.png"));
       int ico = abstractTexture.getGlId();
       RenderUtils.texture(context.getMatrices().peek().getPositionMatrix(), boxX + 2.1F, boxY + 2.0F, 6.0F, 6.0F, ico, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
     } 
   }
   
   public float getHeight() {
     String desc = this.setting.getDescription();
     return (desc != null && !desc.isEmpty()) ? 28.0F : 18.0F;
   }
   
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
     float boxSize = 10.0F, boxX = this.x + this.width - boxSize - 7.0F, boxY = this.y + (this.height - boxSize) / 2.0F;
     if (mouseX >= boxX && mouseX <= (boxX + boxSize) && mouseY >= boxY && mouseY <= (boxY + boxSize) && button == 0) {
       this.setting.set(Boolean.valueOf(!((Boolean)this.setting.get()).booleanValue()));
       return true;
     } 
     return false;
   }
 }


