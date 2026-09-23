 package su.sacura.display.csgui.components.module;
 
 import java.awt.Color;
 import net.minecraft.client.gui.DrawContext;
 import su.sacura.display.csgui.helper.ThemeStorage;
 import su.sacura.features.modules.settings.impl.BindSetting;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.FontProvider;
 import su.sacura.util.impl.system.KeyUtil;
 
 public class BindComponent extends Component {
   public BindSetting setting;
   
   private boolean binding = false;
   
   public BindComponent(BindSetting setting, float width, float height) {
     super(width, height);
     this.setting = setting;
   }
   
   public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
     this.x = x;
     this.y = y;
     this.height = getHeight();
     String desc = this.setting.getDescription();
     boolean hasDesc = (desc != null && !desc.isEmpty());
     if (hasDesc) {
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), desc, x + 5.0F, y + 15.0F, 6.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
     } else {
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + this.height / 2.0F - 3.5F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     } 
     Color borderC = ThemeStorage.borderColor;
     Color textC = ThemeStorage.textColor;
     String bindText = this.binding ? "..." : ((((Integer)this.setting.get()).intValue() != -1) ? KeyUtil.getKey(((Integer)this.setting.get()).intValue()) : "n/a");
     float bindTextWidth = FontProvider.regular.getWidth(bindText, 7.0F);
     float boxWidth = bindTextWidth + 10.0F;
     float boxHeight = 12.0F;
     float boxX = x + this.width - boxWidth - 7.0F;
     float boxY = y + (this.height - boxHeight) / 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), boxX, boxY, boxWidth, boxHeight, 2.0F, (new Color(borderC.getRed(), borderC.getGreen(), borderC.getBlue(), globalAlpha)).getRGB());
     FontProvider.regular.drawCentered(context.getMatrices().peek().getPositionMatrix(), bindText, boxX + boxWidth / 2.0F - 0.5F, boxY + 1.5F, 7.0F, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
   }
   
   public float getHeight() {
     String desc = this.setting.getDescription();
     return (desc != null && !desc.isEmpty()) ? 28.0F : 18.0F;
   }
   
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
     String bindText = this.binding ? "..." : ((((Integer)this.setting.get()).intValue() != -1) ? KeyUtil.getKey(((Integer)this.setting.get()).intValue()) : "n/a");
     float bindTextWidth = FontProvider.regular.getWidth(bindText, 7.0F), boxWidth = bindTextWidth + 10.0F, boxHeight = 12.0F, boxX = this.x + this.width - boxWidth - 7.0F, boxY = this.y + (this.height - boxHeight) / 2.0F;
     if (mouseX >= boxX && mouseX <= (boxX + boxWidth) && mouseY >= boxY && mouseY <= (boxY + boxHeight) && button == 0) {
       this.binding = !this.binding;
       return true;
     } 
     if (this.binding) {
       this.setting.set(Integer.valueOf(button));
       this.binding = false;
       return true;
     } 
     return false;
   }
   
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
     if (this.binding) {
       if (keyCode == 256) {
         this.setting.set(Integer.valueOf(-1));
       } else if (keyCode == 261) {
         this.setting.set(Integer.valueOf(-1));
       } else {
         this.setting.set(Integer.valueOf(keyCode));
       } 
       this.binding = false;
       return true;
     } 
     return false;
   }
 }


