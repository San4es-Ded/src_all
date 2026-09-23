 package su.sacura.display.csgui.components.module;
 
 import java.awt.Color;
 import java.util.List;
 import net.minecraft.client.gui.DrawContext;
 import net.minecraft.util.math.RotationAxis;
 import su.sacura.features.modules.settings.api.Setting;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ModeListSetting;
 import su.sacura.util.impl.math.helper.AnimationUtil;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.FontProvider;
 
 public class ModeListComponent extends Component {
   public ModeListSetting setting;
   
   private boolean expanded = false;
   
   private float animation = 0.0F;
   
   private float hoverOffset = 0.0F;
   
   private float hoverOpacity = 0.0F;
   
   public ModeListComponent(ModeListSetting setting, float width, float height) {
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
       FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 6.0F, y + collapsedHeight / 2.0F - 15.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     } 
     FontProvider.monoton.draw(context.getMatrices().peek().getPositionMatrix(), "G", x + 135.0F, y + 7.0F, 13.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     List<BooleanSetting> subSettings = (List<BooleanSetting>)this.setting.get();
     long enabledCount = subSettings.stream().filter(Setting::get).count();
     String current = (enabledCount == 0L) ? "Ничего :(" : ("Выбрано " + enabledCount + "/" + subSettings.size());
     float valX = x + 5.0F;
     float valY = y + (hasDesc ? 29 : 20);
     int r = 50;
     int g = 50;
     int b = 50;
     int a = (int)(200.0F * globalAlpha / 255.0F);
     int borderColor = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
     float maxDropHeight = (subSettings.size() * 12);
     float currentDropHeight = maxDropHeight * this.animation;
     float boxHeight = 16.0F + currentDropHeight;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX, valY - 2.0F, this.width - 10.0F, boxHeight, 3.0F, (new Color(200, 200, 200, (int)(10.0F * globalAlpha / 255.0F))).getRGB());
     RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), valX, valY - 2.0F, this.width - 10.0F, boxHeight, 3.0F, 0.1F, 0.5F, 1.0F, borderColor);
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), current, valX + 20.0F, valY + 2.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     FontProvider.monoton.draw(context.getMatrices().peek().getPositionMatrix(), "G", valX + 6.0F, valY + 1.5F, 9.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     context.getMatrices().push();
     float arrowX = valX + 135.0F;
     float arrowY = valY + 2.5F;
     float arrowSize = 6.0F;
     float centerX = arrowX + arrowSize / 2.0F;
     float centerY = arrowY + arrowSize / 2.0F;
     context.getMatrices().translate(centerX, centerY, 0.0F);
     context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F * this.animation));
     context.getMatrices().translate(-centerX, -centerY, 0.0F);
     FontProvider.arrows.draw(context.getMatrices().peek().getPositionMatrix(), "B", arrowX, arrowY, arrowSize, (new Color(255, 255, 255, globalAlpha)).getRGB());
     context.getMatrices().pop();
     if (this.animation > 0.01F) {
       float dropY = valY - 2.0F + 16.0F;
       if (currentDropHeight > 1.0F)
         RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX + 2.0F, dropY, this.width - 14.0F, 1.0F, 0.0F, (new Color(200, 200, 200, (int)(80.0F * this.animation * globalAlpha / 255.0F))).getRGB()); 
       int hoveredIndex = -1;
       float tempDropY = dropY;
       int i;
       for (i = 0; i < subSettings.size() && ((
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
       for (i = 0; i < subSettings.size() && ((
         i + 1) * 12) <= currentDropHeight + 12.0F; i++) {
         BooleanSetting boolSetting = subSettings.get(i);
         String mode = boolSetting.getName();
         boolean isEnabled = ((Boolean)boolSetting.get()).booleanValue();
         int textAlpha = (int)(globalAlpha * this.animation);
         int textDimAlpha = (int)(150.0F * this.animation * globalAlpha / 255.0F);
         int textColor = isEnabled ? (new Color(255, 255, 255, textAlpha)).getRGB() : (new Color(150, 150, 150, textDimAlpha)).getRGB();
         FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), mode, valX + 5.0F, dropY + 3.0F, 6.0F, textColor);
         if (i < subSettings.size() - 1) {
           int sepAlpha = (int)(80.0F * this.animation * globalAlpha / 255.0F);
           RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX + 2.0F, dropY + 12.0F, this.width - 14.0F, 1.0F, 0.0F, (new Color(200, 200, 200, sepAlpha)).getRGB());
         } 
         dropY += 12.0F;
       } 
     } 
   }
   
   public float getHeight() {
     String desc = this.setting.getDescription();
     float h = (desc != null && !desc.isEmpty()) ? 48.0F : 38.0F;
     h += (((List)this.setting.get()).size() * 12) * this.animation;
     return h;
   }
   
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
     String desc = this.setting.getDescription();
     boolean hasDesc = (desc != null && !desc.isEmpty());
     float valX = this.x + 5.0F;
     float valY = this.y + (hasDesc ? 29 : 20);
     if (mouseX >= valX && mouseX <= (valX + this.width - 10.0F) && mouseY >= (valY - 2.0F) && mouseY <= (valY - 2.0F + 16.0F) && (
       button == 1 || button == 0)) {
       this.expanded = !this.expanded;
       return true;
     } 
     if (this.expanded) {
       float dropY = valY - 2.0F + 16.0F;
       List<BooleanSetting> subSettings = (List<BooleanSetting>)this.setting.get();
       for (BooleanSetting boolSetting : subSettings) {
         if (mouseX >= valX && mouseX <= (valX + this.width - 10.0F) && mouseY >= dropY && mouseY < (dropY + 12.0F) && 
           button == 0) {
           boolSetting.set(Boolean.valueOf(!((Boolean)boolSetting.get()).booleanValue()));
           return true;
         } 
         dropY += 12.0F;
       } 
     } 
     return false;
   }
 }


