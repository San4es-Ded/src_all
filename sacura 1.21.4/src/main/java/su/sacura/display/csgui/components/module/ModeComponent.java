 package su.sacura.display.csgui.components.module;
 
 import java.awt.Color;
 import net.minecraft.client.gui.DrawContext;
 import net.minecraft.util.math.RotationAxis;
 import su.sacura.display.csgui.helper.ThemeStorage;
 import su.sacura.features.modules.settings.impl.ModeSetting;
 import su.sacura.util.impl.math.helper.AnimationUtil;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.FontProvider;
 
 public class ModeComponent extends Component {
   public ModeSetting setting;
   
   private boolean expanded = false;
   
   private float animation = 0.0F;
   
   private float hoverOffset = 0.0F;
   
   private float hoverOpacity = 0.0F;
   
   public ModeComponent(ModeSetting setting, float width, float height) {
     super(width, height);
     this.setting = setting;
   }
   
   public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
     this.x = x;
     this.y = y;
     this.height = getHeight();
     float delta = 0.1F;
     this.animation = AnimationUtil.lerp(this.animation, this.expanded ? 1.0F : 0.0F, delta);
     String desc = this.setting.getDescription();
     boolean hasDesc = (desc != null && !desc.isEmpty());
     if (hasDesc) {
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), desc, x + 5.0F, y + 15.0F, 6.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
     } else {
       float collapsedHeight = 38.0F;
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + collapsedHeight / 2.0F - 3.5F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     } 
     context.getMatrices().push();
     float arrowX = x + 140.0F;
     float arrowY = y + 30.0F;
     float arrowSize = 6.0F;
     float centerX = arrowX + arrowSize / 2.0F;
     float centerY = arrowY + arrowSize / 2.0F;
     FontProvider.monoton.draw(context.getMatrices().peek().getPositionMatrix(), "G", x + 135.0F, y + 8.0F, 13.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     context.getMatrices().translate(centerX, centerY, 0.0F);
     context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F * this.animation));
     context.getMatrices().translate(-centerX, -centerY, 0.0F);
     FontProvider.arrows.draw(context.getMatrices().peek().getPositionMatrix(), "B", arrowX, arrowY, arrowSize, (new Color(255, 255, 255, globalAlpha)).getRGB());
     context.getMatrices().pop();
     Color borderC = ThemeStorage.borderColor;
     Color textC = ThemeStorage.textColor;
     String current = (String)this.setting.get();
     float valX = x + 5.0F;
     float valY = y + (hasDesc ? 28 : 32);
     int r = borderC.getRed();
     int g = borderC.getGreen();
     int b = borderC.getBlue();
     int a = (int)(200.0F * globalAlpha / 255.0F);
     int borderColor = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
     float maxDropHeight = (this.setting.strings.length * 12);
     float currentDropHeight = maxDropHeight * this.animation;
     float boxHeight = 16.0F + currentDropHeight;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX, valY - 2.0F, this.width - 10.0F, boxHeight, 3.0F, (new Color(r, g, b, (int)(30.0F * globalAlpha / 255.0F))).getRGB());
     RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), valX, valY - 2.0F, this.width - 10.0F, boxHeight, 3.0F, 0.1F, 0.5F, 1.0F, borderColor);
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), current, valX + 20.0F, valY + 1.5F, 7.0F, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
     FontProvider.monoton.draw(context.getMatrices().peek().getPositionMatrix(), "G", valX + 6.0F, valY + 1.5F, 9.0F, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
     if (this.animation > 0.01F) {
       float dropY = valY - 2.0F + 16.0F;
       if (currentDropHeight > 1.0F)
         RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX + 2.0F, dropY, this.width - 14.0F, 1.0F, 0.0F, (new Color(200, 200, 200, (int)(80.0F * this.animation * globalAlpha / 255.0F))).getRGB()); 
       int hoveredIndex = -1;
       float tempDropY = dropY;
       int i;
       for (i = 0; i < this.setting.strings.length && ((
         i + 1) * 12) <= currentDropHeight + 12.0F; i++) {
         if (mouseX >= valX && mouseX <= valX + this.width - 10.0F && mouseY >= tempDropY && mouseY < tempDropY + 12.0F) {
           hoveredIndex = i;
           break;
         } 
         tempDropY += 12.0F;
       } 
       if (hoveredIndex != -1) {
         if (this.hoverOpacity < 0.1F)
           this.hoverOffset = hoveredIndex * 12.0F; 
         this.hoverOffset = AnimationUtil.lerp(this.hoverOffset, hoveredIndex * 12.0F, delta);
         this.hoverOpacity = AnimationUtil.lerp(this.hoverOpacity, 10.0F, delta);
       } else {
         this.hoverOpacity = AnimationUtil.lerp(this.hoverOpacity, 0.0F, delta);
       } 
       if (this.hoverOpacity > 0.1F)
         RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX, dropY + this.hoverOffset, this.width - 10.0F, 12.0F, 0.0F, (new Color(200, 200, 200, (int)(this.hoverOpacity * this.animation * globalAlpha / 255.0F))).getRGB()); 
       for (i = 0; i < this.setting.strings.length && ((
         i + 1) * 12) <= currentDropHeight + 12.0F; i++) {
         String mode = this.setting.strings[i];
         int textAlpha = (int)(globalAlpha * this.animation);
         int textDimAlpha = (int)(150.0F * this.animation * globalAlpha / 255.0F);
         int textColor = mode.equals(current) ? (new Color(255, 255, 255, textAlpha)).getRGB() : (new Color(150, 150, 150, textDimAlpha)).getRGB();
         FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), mode, valX + 5.0F, dropY + 3.0F, 6.0F, textColor);
         if (i < this.setting.strings.length - 1) {
           int sepAlpha = (int)(80.0F * this.animation * globalAlpha / 255.0F);
           RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX + 2.0F, dropY + 12.0F, this.width - 14.0F, 1.0F, 0.0F, (new Color(200, 200, 200, sepAlpha)).getRGB());
         } 
         dropY += 12.0F;
       } 
     } 
   }
   
   public float getHeight() {
     String desc = this.setting.getDescription();
     float h = (desc != null && !desc.isEmpty()) ? 48.0F : 40.0F;
     h += (this.setting.strings.length * 12) * this.animation;
     return h;
   }
   
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
     String desc = this.setting.getDescription();
     boolean hasDesc = (desc != null && !desc.isEmpty());
     float valX = this.x + 5.0F;
     float valY = this.y + (hasDesc ? 28 : 32);
     if (mouseX >= valX && mouseX <= (valX + this.width - 10.0F) && mouseY >= (valY - 2.0F) && mouseY <= (valY - 2.0F + 16.0F)) {
       if (button == 1) {
         this.expanded = !this.expanded;
         return true;
       } 
       if (button == 0 && !this.expanded) {
         int index = this.setting.getIndex();
         index++;
         if (index >= this.setting.strings.length)
           index = 0; 
         this.setting.set(this.setting.strings[index]);
         return true;
       } 
     } 
     if (this.expanded) {
       float dropY = valY - 2.0F + 16.0F;
       for (String mode : this.setting.strings) {
         if (mouseX >= valX && mouseX <= (valX + this.width - 10.0F) && mouseY >= dropY && mouseY < (dropY + 12.0F) && 
           button == 0) {
           this.setting.set(mode);
           return true;
         } 
         dropY += 12.0F;
       } 
     } 
     return false;
   }
 }


