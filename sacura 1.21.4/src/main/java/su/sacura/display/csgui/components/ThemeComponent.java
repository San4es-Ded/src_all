 package su.sacura.display.csgui.components;
 
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.client.gui.DrawContext;
 import net.minecraft.client.texture.AbstractTexture;
 import net.minecraft.util.Identifier;
 import su.sacura.display.csgui.components.module.ColorComponent;
 import su.sacura.display.csgui.components.module.Component;
 import su.sacura.display.csgui.helper.ThemeStorage;
 import su.sacura.display.csgui.themes.Theme;
 import su.sacura.display.csgui.themes.ThemeManager;
 import su.sacura.util.impl.math.helper.AnimationUtil;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.FontProvider;
 
 public class ThemeComponent extends Component {
   public Theme theme;
   
   private final List<ColorComponent> colorComponents = new ArrayList<>();
   
   private boolean expanded = false;
   
   private float progress = 0.0F;
   
   private long lastTime = 0L;
   
   private float delta = 0.0F;
   
   private float flashAlpha = 0.0F;
   
   public ThemeComponent(Theme theme, float width, float height) {
     super(width, height);
     this.theme = theme;
     this.colorComponents.add(new ColorComponent(theme.accentColor, width, 20.0F));
     this.colorComponents.add(new ColorComponent(theme.backgroundColor, width, 20.0F));
     this.colorComponents.add(new ColorComponent(theme.textColor, width, 20.0F));
     this.colorComponents.add(new ColorComponent(theme.borderColor, width, 20.0F));
   }
   
   public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
     this.x = x;
     this.y = y;
     long currentTime = System.nanoTime();
     if (this.lastTime == 0L)
       this.lastTime = currentTime; 
     this.delta = (float)(currentTime - this.lastTime) / 1.0E9F;
     this.lastTime = currentTime;
     if (this.delta > 0.1F)
       this.delta = 0.1F; 
     boolean isActive = (ThemeStorage.backgroundColor.getRGB() == ((Integer)this.theme.backgroundColor.get()).intValue() && ThemeStorage.accentColor.getRGB() == ((Integer)this.theme.accentColor.get()).intValue());
     this.progress = AnimationUtil.lerp(this.progress, isActive ? 1.0F : 0.0F, Math.min(1.0F, this.delta * 20.0F));
     float totalHeight = getHeight();
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), x, y, this.width, totalHeight, 5.0F, (new Color(255, 255, 255, (int)(10.0F * globalAlpha / 255.0F))).getRGB());
     if (this.flashAlpha > 0.0F) {
       RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), x, y, this.width, totalHeight, 5.0F, 1.0F, 0.2F, 1.0F, (new Color(255, 255, 255, (int)(this.flashAlpha * globalAlpha))).getRGB());
       this.flashAlpha -= this.delta * 2.0F;
     } 
     Color accent = ThemeStorage.accentColor;
     Color borderC = ThemeStorage.borderColor;
     Color textC = ThemeStorage.textColor;
     int a = (int)(200.0F * globalAlpha / 255.0F);
     int borderColor = (a & 0xFF) << 24 | (borderC.getRed() & 0xFF) << 16 | (borderC.getGreen() & 0xFF) << 8 | borderC.getBlue() & 0xFF;
     RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), x, y, this.width, totalHeight, 5.0F, 0.1F, 0.2F, 1.0F, borderColor);
     AbstractTexture abstractTexture = MinecraftClient.getInstance().getTextureManager().getTexture(Identifier.ofVanilla("sacura/images/ico.png"));
     int ico = abstractTexture.getGlId();
     int icoR = (int)AnimationUtil.lerp(200.0F, accent.getRed(), this.progress);
     int icoG = (int)AnimationUtil.lerp(200.0F, accent.getGreen(), this.progress);
     int icoB = (int)AnimationUtil.lerp(200.0F, accent.getBlue(), this.progress);
     int icoColor = globalAlpha << 24 | (icoR & 0xFF) << 16 | (icoG & 0xFF) << 8 | icoB & 0xFF;
     RenderUtils.texture(context.getMatrices().peek().getPositionMatrix(), x + 6.0F, y + 10.0F - 5.0F, 10.0F, 10.0F, ico, icoColor);
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.theme.name, x + 20.0F, y + 10.0F - 4.0F, 8.0F, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
     float switchWidth = 17.0F;
     float switchHeight = 12.0F;
     float switchX = x + this.width - switchWidth - 5.0F;
     float switchY = y + (20.0F - switchHeight) / 2.0F;
     int trackR = (int)AnimationUtil.lerp(60.0F, accent.getRed(), this.progress);
     int trackG = (int)AnimationUtil.lerp(60.0F, accent.getGreen(), this.progress);
     int trackB = (int)AnimationUtil.lerp(60.0F, accent.getBlue(), this.progress);
     int trackColor = globalAlpha << 24 | (trackR & 0xFF) << 16 | (trackG & 0xFF) << 8 | trackB & 0xFF;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), switchX, switchY, switchWidth, switchHeight, 5.0F, trackColor);
     float knobSize = switchHeight - 2.0F;
     float knobX = switchX + 2.0F + (switchWidth - knobSize - 2.0F) * this.progress;
     float knobY = switchY + 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), knobX, knobY, 8.0F, 8.0F, 3.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     String editText = "Edit";
     float editTextWidth = FontProvider.regular.getWidth(editText, 8.0F);
     float editButtonWidth = editTextWidth + 19.0F;
     float editButtonX = switchX - editButtonWidth - 4.0F;
     float editButtonY = y + 4.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), editButtonX, editButtonY, editButtonWidth, 12.0F, 2.0F, (new Color(40, 40, 40, globalAlpha)).getRGB());
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), editText, editButtonX + 14.0F, editButtonY + 1.0F, 8.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     FontProvider.icons.draw(context.getMatrices().peek().getPositionMatrix(), "C", editButtonX + 2.0F, editButtonY + 2.0F, 9.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
     if (!this.theme.author.equalsIgnoreCase("System")) {
       float btnSize = 12.0F;
       float gap = 4.0F;
       float saveX = editButtonX - btnSize - gap;
       float saveY = editButtonY;
       boolean saveHover = isMouseOver(mouseX, mouseY, saveX, saveY, btnSize, btnSize);
       if (this.expanded) {
         RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), saveX, saveY, btnSize, btnSize, 2.0F, (new Color(40, 40, 40, globalAlpha)).getRGB());
         FontProvider.icons.draw(context.getMatrices().peek().getPositionMatrix(), "L", saveX + 2.0F, saveY + 2.0F, 8.0F, saveHover ? (new Color(50, 255, 50)).getRGB() : (new Color(200, 200, 200, globalAlpha)).getRGB());
       } 
       float delX = (this.expanded ? saveX : editButtonX) - btnSize - gap;
       float delY = saveY;
       boolean delHover = isMouseOver(mouseX, mouseY, delX, delY, btnSize, btnSize);
       if (this.expanded || isHovered(mouseX, mouseY)) {
         RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), delX, delY, btnSize, btnSize, 2.0F, (new Color(40, 40, 40, globalAlpha)).getRGB());
         FontProvider.icons.draw(context.getMatrices().peek().getPositionMatrix(), "K", delX + 2.0F, delY + 2.0F, 8.0F, delHover ? (new Color(255, 50, 50)).getRGB() : (new Color(200, 200, 200, globalAlpha)).getRGB());
       } 
     } 
     if (this.expanded) {
       float currentY = y + 20.0F;
       for (ColorComponent c : this.colorComponents) {
         c.width = this.width;
         c.render(context, x, currentY, mouseX, mouseY, globalAlpha);
         currentY += c.getHeight();
       } 
     } 
   }
   
   public float getHeight() {
     float h = 20.0F;
     if (this.expanded)
       for (ColorComponent c : this.colorComponents)
         h += c.getHeight();  
     return h;
   }
   
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
     float switchWidth = 17.0F;
     float switchHeight = 12.0F;
     float switchX = this.x + this.width - switchWidth - 5.0F;
     float switchY = this.y + (20.0F - switchHeight) / 2.0F;
     if (isMouseOver(mouseX, mouseY, switchX, switchY, switchWidth, switchHeight) && button == 0) {
       ThemeManager.setTheme(this.theme);
       return true;
     } 
     String editText = "Edit";
     float editTextWidth = FontProvider.regular.getWidth(editText, 8.0F);
     float editButtonWidth = editTextWidth + 19.0F;
     float editButtonX = switchX - editButtonWidth - 4.0F;
     float editButtonY = this.y + 4.0F;
     if (isMouseOver(mouseX, mouseY, editButtonX, editButtonY, editButtonWidth, 12.0F) && button == 0) {
       this.expanded = !this.expanded;
       return true;
     } 
     if (!this.theme.author.equalsIgnoreCase("System")) {
       float btnSize = 12.0F;
       float gap = 4.0F;
       float saveX = editButtonX - btnSize - gap;
       float saveY = editButtonY;
       float delX = (this.expanded ? saveX : editButtonX) - btnSize - gap;
       float delY = saveY;
       if (this.expanded && isMouseOver(mouseX, mouseY, saveX, saveY, btnSize, btnSize) && button == 0) {
         ThemeManager.saveTheme(this.theme);
         return true;
       } 
       if ((this.expanded || isHovered(mouseX, mouseY)) && isMouseOver(mouseX, mouseY, delX, delY, btnSize, btnSize) && button == 0) {
         ThemeManager.deleteTheme(this.theme);
         return true;
       } 
     } 
     if (this.expanded)
       for (ColorComponent c : this.colorComponents) {
         if (c.mouseClicked(mouseX, mouseY, button)) {
           ThemeManager.setTheme(this.theme);
           return true;
         } 
       }  
     if (isHovered(mouseX, mouseY) && button == 0) {
       ThemeManager.setTheme(this.theme);
       return true;
     } 
     return false;
   }
   
   private boolean isMouseOver(double mouseX, double mouseY, float x, float y, float w, float h) {
     return (mouseX >= x && mouseX <= (x + w) && mouseY >= y && mouseY <= (y + h));
   }
   
   private boolean isHovered(double mouseX, double mouseY) {
     return (mouseX >= this.x && mouseX <= (this.x + this.width) && mouseY >= this.y && mouseY <= (this.y + 20.0F));
   }
   
   public void mouseReleased(int button) {
     if (this.expanded)
       for (ColorComponent c : this.colorComponents)
         c.mouseReleased(button);  
   }
 }


