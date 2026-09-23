 package su.sacura.display.csgui.components;
 
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.client.gui.DrawContext;
 import net.minecraft.client.texture.AbstractTexture;
 import net.minecraft.util.Identifier;
 import su.sacura.display.csgui.components.module.BindComponent;
 import su.sacura.display.csgui.components.module.BooleanComponent;
 import su.sacura.display.csgui.components.module.ColorComponent;
 import su.sacura.display.csgui.components.module.Component;
 import su.sacura.display.csgui.components.module.ModeComponent;
 import su.sacura.display.csgui.components.module.ModeListComponent;
 import su.sacura.display.csgui.components.module.SliderComponent;
 import su.sacura.display.csgui.components.module.StringComponent;
 import su.sacura.display.csgui.helper.ThemeStorage;
 import su.sacura.features.modules.api.bind.Bind;
 import su.sacura.features.modules.api.core.Module;
 import su.sacura.features.modules.settings.impl.BindSetting;
 import su.sacura.features.modules.settings.impl.BooleanSetting;
 import su.sacura.features.modules.settings.impl.ColorSetting;
 import su.sacura.features.modules.settings.impl.ModeListSetting;
 import su.sacura.features.modules.settings.impl.ModeSetting;
 import su.sacura.features.modules.settings.impl.SliderSetting;
 import su.sacura.features.modules.settings.impl.StringSetting;
 import su.sacura.util.impl.math.helper.AnimationUtil;
 import su.sacura.util.impl.render.RenderUtils;
 import su.sacura.util.impl.render.providers.ColorProvider;
 import su.sacura.util.impl.render.providers.FontProvider;
 import su.sacura.util.impl.system.KeyUtil;
 import su.sacura.util.type.ISetting;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class ModuleComponent implements MinecraftWrapper {
   public Module module;
   
   public float x;
   
   public float y;
   
   public float width;
   
   public float height;
   
   private float progress = 0.0F;
   
   private long lastTime = 0L;
   
   private float delta = 0.0F;
   
   private final List<Component> components = new ArrayList<>();
   
   private boolean binding = false;
   
   private boolean settingKey = false;
   
   private float popupX = -1.0F;
   
   private float popupY = -1.0F;
   
   private boolean dragging = false;
   
   private float dragX;
   
   private float dragY;
   
   private float toggleHoldAnimation = 0.0F;
   
   public float flashAlpha = 0.0F;
   
   private float popupAlpha = 0.0F;
   
   public ModuleComponent(Module module, float width, float height) {
     this.module = module;
     this.width = width;
     this.height = height;
     for (ISetting setting : module.getSettings()) {
       if (setting instanceof BooleanSetting) {
         this.components.add(new BooleanComponent((BooleanSetting)setting, width, 20.0F));
         continue;
       } 
       if (setting instanceof SliderSetting) {
         this.components.add(new SliderComponent((SliderSetting)setting, width, 20.0F));
         continue;
       } 
       if (setting instanceof ModeSetting) {
         this.components.add(new ModeComponent((ModeSetting)setting, width, 20.0F));
         continue;
       } 
       if (setting instanceof ModeListSetting) {
         this.components.add(new ModeListComponent((ModeListSetting)setting, width, 20.0F));
         continue;
       } 
       if (setting instanceof ColorSetting) {
         this.components.add(new ColorComponent((ColorSetting)setting, width, 20.0F));
         continue;
       } 
       if (setting instanceof BindSetting) {
         this.components.add(new BindComponent((BindSetting)setting, width, 20.0F));
         continue;
       } 
       if (setting instanceof StringSetting)
         this.components.add(new StringComponent((StringSetting)setting, width, 20.0F)); 
     } 
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
     float totalHeight = getTotalHeight();
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), x, y, this.width, totalHeight, 5.0F, (new Color(255, 255, 255, (int)(10.0F * globalAlpha / 255.0F))).getRGB());
     if (this.flashAlpha > 0.0F) {
       RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), x, y, this.width, totalHeight, 5.0F, 1.0F, 0.2F, 1.0F, (new Color(255, 255, 255, (int)(this.flashAlpha * globalAlpha))).getRGB());
       this.flashAlpha -= this.delta * 2.0F;
     } 
     this.progress = AnimationUtil.lerp(this.progress, this.module.enable ? 1.0F : 0.0F, Math.min(1.0F, this.delta * 20.0F));
     int r = 50;
     int g = 50;
     int b = 50;
     int a = (int)(200.0F * globalAlpha / 255.0F);
     Color accent = ThemeStorage.accentColor;
     Color borderC = ThemeStorage.borderColor;
     Color textC = ThemeStorage.textColor;
     int borderColor = (a & 0xFF) << 24 | (borderC.getRed() & 0xFF) << 16 | (borderC.getGreen() & 0xFF) << 8 | borderC.getBlue() & 0xFF;
     RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), x, y, this.width, totalHeight, 5.0F, 0.1F, 0.2F, 1.0F, borderColor);
     int trackR = (int)AnimationUtil.lerp(60.0F, accent.getRed(), this.progress);
     int trackG = (int)AnimationUtil.lerp(60.0F, accent.getGreen(), this.progress);
     int trackB = (int)AnimationUtil.lerp(60.0F, accent.getBlue(), this.progress);
     int trackColor = globalAlpha << 24 | (trackR & 0xFF) << 16 | (trackG & 0xFF) << 8 | trackB & 0xFF;
     AbstractTexture abstractTexture = MinecraftClient.getInstance().getTextureManager().getTexture(Identifier.ofVanilla("sacura/images/ico.png"));
     int ico = abstractTexture.getGlId();
     int icoR = (int)AnimationUtil.lerp(200.0F, accent.getRed(), this.progress);
     int icoG = (int)AnimationUtil.lerp(200.0F, accent.getGreen(), this.progress);
     int icoB = (int)AnimationUtil.lerp(200.0F, accent.getBlue(), this.progress);
     int icoColor = globalAlpha << 24 | (icoR & 0xFF) << 16 | (icoG & 0xFF) << 8 | icoB & 0xFF;
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.module.name, x + 20.0F, y + this.height / 2.0F - 4.0F, 8.0F, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
     RenderUtils.texture(context.getMatrices().peek().getPositionMatrix(), x + 6.0F, y + this.height / 2.0F - 5.0F, 10.0F, 10.0F, ico, icoColor);
     float switchWidth = 17.0F;
     float switchHeight = 12.0F;
     float switchX = x + this.width - switchWidth - 5.0F;
     float switchY = y + (this.height - switchHeight) / 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), switchX, switchY, switchWidth, switchHeight, 5.0F, trackColor);
     float knobSize = switchHeight - 2.0F;
     float knobX = switchX + 2.0F + (switchWidth - knobSize - 2.0F) * this.progress;
     float knobY = switchY + 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), knobX, knobY, 8.0F, 8.0F, 3.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     String bindText = (this.module.bind.getKey() != -1) ? KeyUtil.getKey(this.module.bind.getKey()) : "n/a";
     float bindTextWidth = FontProvider.regular.getWidth(bindText, 8.0F);
     float bindButtonWidth = bindTextWidth + 19.0F;
     float bindButtonX = switchX - bindButtonWidth - 4.0F;
     float bindButtonY = y + (this.height - 12.0F) / 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), bindButtonX, bindButtonY, bindButtonWidth, 12.0F, 2.0F, (new Color(40, 40, 40, globalAlpha)).getRGB());
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), bindText, bindButtonX + 14.0F, bindButtonY + 1.0F, 8.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
     FontProvider.icons.draw(context.getMatrices().peek().getPositionMatrix(), "C", bindButtonX + 2.0F, bindButtonY + 2.0F, 9.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
     float currentY = y + this.height;
     for (Component component : this.components) {
       boolean visible = false;
       if (component instanceof BooleanComponent) {
         visible = ((Boolean)((BooleanComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof SliderComponent) {
         visible = ((Boolean)((SliderComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ModeComponent) {
         visible = ((Boolean)((ModeComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ModeListComponent) {
         visible = ((Boolean)((ModeListComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ColorComponent) {
         visible = ((Boolean)((ColorComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof BindComponent) {
         visible = ((Boolean)((BindComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof StringComponent) {
         visible = ((Boolean)((StringComponent)component).setting.visible.get()).booleanValue();
       } 
       if (visible) {
         component.width = this.width;
         component.render(context, x, currentY, mouseX, mouseY, globalAlpha);
         currentY += component.getHeight();
       } 
     } 
   }
   
   public void renderBindPopup(DrawContext context, int globalAlpha) {
     this.popupAlpha = AnimationUtil.lerp(this.popupAlpha, this.binding ? 1.0F : 0.0F, this.delta * 1.0F);
     if (this.popupAlpha < 0.01F)
       return; 
     int animatedGlobalAlpha = (int)(globalAlpha * this.popupAlpha);
     float popupWidth = 90.0F;
     float popupHeight = 40.0F;
     float titleBarHeight = 6.0F;
     RenderUtils.blur(context.getMatrices().peek().getPositionMatrix(), this.popupX, this.popupY, popupWidth, popupHeight, 5.0F, 15.0F, ColorProvider.rgba(255, 255, 255, animatedGlobalAlpha));
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.popupX, this.popupY, popupWidth, popupHeight, 5.0F, (new Color(25, 25, 25, (int)((230 * animatedGlobalAlpha) / 255.0F))).getRGB());
     RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), this.popupX, this.popupY, popupWidth, popupHeight, 5.0F, 0.1F, 0.2F, 1.0F, (new Color(200, 200, 200, (int)((65 * animatedGlobalAlpha) / 255.0F))).getRGB());
     float contentY = this.popupY + titleBarHeight - 2.0F;
     float switchWidth = 78.0F;
     float switchHeight = 14.0F;
     float switchX = this.popupX + (popupWidth - switchWidth) / 2.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), switchX, contentY, switchWidth, switchHeight, 4.0F, (new Color(40, 40, 40, animatedGlobalAlpha)).getRGB());
     boolean isToggle = (this.module.bind.getMode() == Bind.BindMode.TOGGLE);
     this.toggleHoldAnimation = AnimationUtil.lerp(this.toggleHoldAnimation, isToggle ? 0.0F : 1.0F, Math.min(1.0F, this.delta * 10.0F));
     float halfSwitchWidth = switchWidth / 2.0F;
     float animatedX = switchX + halfSwitchWidth * this.toggleHoldAnimation;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), animatedX, contentY, halfSwitchWidth, switchHeight, 4.0F, (new Color(113, 106, 223, animatedGlobalAlpha)).getRGB());
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), "Toggle", switchX + 8.0F, contentY + 3.0F, 7.0F, isToggle ? (new Color(255, 255, 255, animatedGlobalAlpha)).getRGB() : (new Color(192, 192, 192, animatedGlobalAlpha)).getRGB());
     FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), "Hold", switchX + halfSwitchWidth + 12.0F, contentY + 3.0F, 7.0F, !isToggle ? (new Color(255, 255, 255, animatedGlobalAlpha)).getRGB() : (new Color(192, 192, 192, animatedGlobalAlpha)).getRGB());
     float keyRectY = contentY + switchHeight + 2.0F;
     float keyRectHeight = 16.0F;
     String bindText = this.settingKey ? "Your key..." : KeyUtil.getKey(this.module.bind.getKey());
     float keyRectWidth = popupWidth - 10.0F;
     RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.popupX + 5.0F, keyRectY, keyRectWidth, keyRectHeight, 4.0F, (new Color(40, 40, 40, animatedGlobalAlpha)).getRGB());
     FontProvider.icons.draw(context.getMatrices().peek().getPositionMatrix(), "C", this.popupX + 9.0F, keyRectY + 3.0F, 11.0F, (new Color(180, 180, 180, animatedGlobalAlpha)).getRGB());
     FontProvider.regular.drawCentered(context.getMatrices().peek().getPositionMatrix(), bindText, this.popupX + 47.0F, keyRectY + 5.0F, 6.0F, (new Color(255, 255, 255, animatedGlobalAlpha)).getRGB());
   }
   
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
     if (this.binding && this.settingKey) {
       this.module.bind.setKey(button);
       this.settingKey = false;
       return true;
     } 
     if (this.binding) {
       float popupWidth = 90.0F;
       float popupHeight = 40.0F;
       float titleBarHeight = 6.0F;
       if (mouseX >= this.popupX && mouseX <= (this.popupX + popupWidth) && mouseY >= this.popupY && mouseY <= (this.popupY + popupHeight)) {
         if (mouseY <= (this.popupY + titleBarHeight) && button == 0) {
           this.dragging = true;
           this.dragX = (float)(mouseX - this.popupX);
           this.dragY = (float)(mouseY - this.popupY);
           return true;
         } 
         float contentY = this.popupY + titleBarHeight - 2.0F;
         float f1 = 78.0F;
         float f2 = 14.0F;
         float f3 = this.popupX + (popupWidth - f1) / 2.0F;
         float halfSwitchWidth = f1 / 2.0F;
         if (mouseY >= contentY && mouseY <= (contentY + f2)) {
           if (mouseX >= f3 && mouseX <= (f3 + halfSwitchWidth)) {
             this.module.bind.setMode(Bind.BindMode.TOGGLE);
           } else if (mouseX >= (f3 + halfSwitchWidth) && mouseX <= (f3 + f1)) {
             this.module.bind.setMode(Bind.BindMode.HOLD);
           } 
           return true;
         } 
         float keyRectY = contentY + f2 + 6.0F;
         if (mouseY >= keyRectY) {
           this.settingKey = true;
           return true;
         } 
       } else {
         this.binding = false;
         this.settingKey = false;
       } 
     } 
     float switchWidth = 17.0F;
     float switchHeight = 12.0F;
     float switchX = this.x + this.width - switchWidth - 5.0F;
     float switchY = this.y + (this.height - switchHeight) / 2.0F;
     if (mouseX >= switchX && mouseX <= (switchX + switchWidth) && mouseY >= switchY && mouseY <= (switchY + switchHeight) && 
       button == 0) {
       this.module.toggle();
       return true;
     } 
     String bindText = (this.module.bind.getKey() != -1) ? KeyUtil.getKey(this.module.bind.getKey()) : "n/a";
     float bindTextWidth = FontProvider.regular.getWidth(bindText, 8.0F);
     float bindButtonWidth = bindTextWidth + 19.0F;
     float bindButtonX = switchX - bindButtonWidth - 4.0F;
     float bindButtonY = this.y + (this.height - 12.0F) / 2.0F;
     if (mouseX >= bindButtonX && mouseX <= (bindButtonX + bindButtonWidth) && mouseY >= bindButtonY && mouseY <= (bindButtonY + 12.0F) && button == 0) {
       this.binding = !this.binding;
       this.settingKey = false;
       if (this.binding) {
         this.popupX = bindButtonX;
         this.popupY = bindButtonY + 14.0F;
       } 
       return true;
     } 
     float currentY = this.y + this.height;
     for (Component component : this.components) {
       boolean visible = false;
       if (component instanceof BooleanComponent) {
         visible = ((Boolean)((BooleanComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof SliderComponent) {
         visible = ((Boolean)((SliderComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ModeComponent) {
         visible = ((Boolean)((ModeComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ModeListComponent) {
         visible = ((Boolean)((ModeListComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ColorComponent) {
         visible = ((Boolean)((ColorComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof BindComponent) {
         visible = ((Boolean)((BindComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof StringComponent) {
         visible = ((Boolean)((StringComponent)component).setting.visible.get()).booleanValue();
       } 
       if (visible) {
         if (component.mouseClicked(mouseX, mouseY, button))
           return true; 
         currentY += component.getHeight();
       } 
     } 
     return false;
   }
   
   public boolean mouseReleased(int button) {
     if (button == 0)
       this.dragging = false; 
     for (Component component : this.components)
       component.mouseReleased(button); 
     return false;
   }
   
   public boolean mouseDragged(double mouseX, double mouseY, int button) {
     if (this.dragging && button == 0) {
       this.popupX = (float)(mouseX - this.dragX);
       this.popupY = (float)(mouseY - this.dragY);
       return true;
     } 
     return false;
   }
   
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
     if (this.binding && this.settingKey) {
       if (keyCode == 256) {
         this.settingKey = false;
         return true;
       } 
       if (keyCode == 261) {
         this.module.bind.setKey(-1);
         this.settingKey = false;
         return true;
       } 
       this.module.bind.setKey(keyCode);
       this.settingKey = false;
       return true;
     } 
     for (Component component : this.components) {
       if (component.keyPressed(keyCode, scanCode, modifiers))
         return true; 
     } 
     return false;
   }
   
   public boolean charTyped(char chr, int modifiers) {
     for (Component component : this.components) {
       if (component.charTyped(chr, modifiers))
         return true; 
     } 
     return false;
   }
   
   public float getTotalHeight() {
     float totalHeight = this.height;
     for (Component component : this.components) {
       boolean visible = false;
       if (component instanceof BooleanComponent) {
         visible = ((Boolean)((BooleanComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof SliderComponent) {
         visible = ((Boolean)((SliderComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ModeComponent) {
         visible = ((Boolean)((ModeComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ModeListComponent) {
         visible = ((Boolean)((ModeListComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof ColorComponent) {
         visible = ((Boolean)((ColorComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof BindComponent) {
         visible = ((Boolean)((BindComponent)component).setting.visible.get()).booleanValue();
       } else if (component instanceof StringComponent) {
         visible = ((Boolean)((StringComponent)component).setting.visible.get()).booleanValue();
       } 
       if (visible)
         totalHeight += component.getHeight(); 
     } 
     return totalHeight;
   }
   
   public boolean isBinding() {
     return this.binding;
   }
 }


