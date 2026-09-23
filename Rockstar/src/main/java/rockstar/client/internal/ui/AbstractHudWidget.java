package rockstar.client.internal.ui;










import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;

public abstract class AbstractHudWidget implements SettingOwner, MinecraftClientAccess {
   public float x;
   public float y;
   public float width;
   public float height;
   public final AnimatedValue animation = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   public final AnimatedValue visible = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   public final AnimatedValue selecting = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   public final AnimatedValue dragAnim = new AnimatedValue(300L, 0.0F, Easing.internalField1626);
   private final AnimatedValue blurAnim = new AnimatedValue(300L, 0.0F, Easing.internalField1626);
   private final AnimatedValue loadingAnim = new AnimatedValue(700L, 0.0F, Easing.internalField1624);
   public boolean showing;
   public boolean select;
   private float lastWidth;
   private boolean widthTracked;
   private List<Setting> settings = new ArrayList<>();
   public final InertialRect inertion = new InertialRect();
   public UiContainer flow;
   private long lastFrame;
   private boolean dragging;
   private float dragX;
   private float dragY;
   private float startDragX;
   private float startDragY;
   public final String name;
   public final String icon;
   private AbstractHudWidget.InternalType0167 anchorX;
   private AbstractHudWidget.InternalType0167 anchorY;

   public AbstractHudWidget(String localValue1, String localValue2) {
      this.name = localValue1;
      this.icon = localValue2;
   }

   public void render(UiRenderContext localValue1) {
      this.render(localValue1, 1.0F);
   }

   public void render(UiRenderContext localValue1, float localValue2) {
      this.update(localValue1);
      float localValue3 = this.animation.internalMethod02881() * this.visible.internalMethod02881();
      float localValue4 = localValue3 * localValue2;
      if (localValue4 != 0.0F) {
         float[] localValue6 = RenderSystem.getShaderColor().clone();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, localValue4));
         float localValue5 = 0.5F + localValue3 * 0.5F - 0.05F * this.selecting.internalMethod02881();
         HudRenderUtils.internalMethod08976(localValue1.getMatrices(), this.x + this.width / 2.0F, this.y + this.height / 2.0F, localValue5);

         try {
            this.renderComponent(localValue1);
         } finally {
            try {
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            } finally {
               RenderSystem.setShaderColor(localValue6[0], localValue6[1], localValue6[2], localValue6[3]);
            }
         }
      }
   }

   public UiContainer build() {
      return null;
   }

   public void rebuild() {
      this.flow = null;
      this.lastFrame = 0L;
      this.widthTracked = false;
   }

   public void renderComponent(UiRenderContext localValue1) {
      if (this.flow == null) {
         this.flow = this.build();
      }

      if (this.flow != null) {
         long localValue2 = System.currentTimeMillis();
         float localValue4 = this.lastFrame == 0L ? 16.0F : Math.min(64.0F, (float)(localValue2 - this.lastFrame));
         this.lastFrame = localValue2;
         this.flow.prepareRoot();
         this.flow.snapAt(this.x, this.y);
         this.flow.tick(localValue4, localValue1.internalMethod05259(), localValue1.internalMethod05261());
         this.width = this.flow.w();
         this.height = this.flow.h();
         if (this.syncWidth()) {
            this.flow.snapAt(this.x, this.y);
            this.flow.tick(0.0F, localValue1.internalMethod05259(), localValue1.internalMethod05261());
         }

         this.flow.draw(localValue1, Math.min(1.0F, this.animation.internalMethod02881() * this.visible.internalMethod02881()));
      }
   }

   private boolean syncWidth() {
      if (!this.widthTracked) {
         if (this.width <= 0.0F) {
            return false;
         } else {
            this.lastWidth = this.width;
            this.widthTracked = true;
            return false;
         }
      } else {
         float localValue1 = this.width - this.lastWidth;
         this.lastWidth = this.width;
         if (localValue1 != 0.0F && this.anchorsRightEdge()) {
            boolean localValue2 = this.x + this.width / 2.0F < ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0F;
            this.pushNeighbours(localValue1, localValue2);
            if (!localValue2 && !this.dragging) {
               this.x -= localValue1;
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean anchorsRightEdge() {
      return true;
   }

   private void pushNeighbours(float localValue1, boolean localValue2) {
      for (AbstractHudWidget localValue5 : RockstarClient.getInstance().internalMethod01271().internalMethod09520()) {
         if (localValue5 != this && localValue5.isShowing()) {
            float localValue6 = Math.min(this.y + this.height, localValue5.y + localValue5.height) - Math.max(this.y, localValue5.y);
            if (!(localValue6 <= 0.0F)) {
               float localValue7 = localValue2 ? localValue5.x - (this.x + this.width) : this.x - (localValue5.x + localValue5.width);
               if (!(localValue7 < -5.0F) && !(localValue7 > 25.0F)) {
                  localValue5.x += localValue2 ? localValue1 : -localValue1;
                  localValue5.x = Math.max(0.0F, Math.min(localValue5.x, ScreenMetricsAccess.internalField0389.internalMethod03585() - localValue5.width));
               }
            }
         }
      }
   }

   public void update(UiRenderContext localValue1) {
      this.syncWidth();
      this.dragAnim.internalMethod07062(this.dragging);
      this.animation.internalMethod06645(this.showing ? Easing.internalField0812 : Easing.internalField1328);
      this.animation.internalMethod07062(this.showing);
      this.visible.internalMethod06645(this.show() ? Easing.internalField0812 : Easing.internalField1328);
      this.visible.internalMethod07062(this.show());
      this.selecting.internalMethod07062(this.select);
      this.blurAnim.internalMethod07062(this.animation.internalMethod02881() >= 0.6F);
      if (this.dragging) {
         float localValue2 = Math.max(0.0F, ScreenMetricsAccess.internalField0389.internalMethod03585() - this.width);
         float localValue3 = Math.max(0.0F, ScreenMetricsAccess.internalField0389.internalMethod03589() - this.height);
         this.x = Math.clamp(localValue1.internalMethod05259() - this.dragX, 0.0F, localValue2);
         this.y = Math.clamp(localValue1.internalMethod05261() - this.dragY, 0.0F, localValue3);
         if (!(this instanceof DynamicIslandHud) && !rockstar.client.compat.InputCompat.hasControlDown()) {
            this.x = Math.clamp(this.x + this.snapDelta(AlignmentGuideLine.InternalType0164.internalField0502, this.x, this.width), 0.0F, localValue2);
            this.y = Math.clamp(this.y + this.snapDelta(AlignmentGuideLine.InternalType0164.internalField0501, this.y, this.height), 0.0F, localValue3);
         }
      }

      if (this.isHovered(localValue1) && this.animation.internalMethod02881() >= 1.0F) {
         CursorManager.internalMethod06882(CursorType.internalField0567);
      }
   }

   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.isHovered(localValue1, localValue3) && this.showing) {
         if (localValue5 == MouseButton.internalField0102) {
            this.beginDrag(localValue1, localValue3);
         } else if (localValue5 == MouseButton.internalField0101) {
            this.select = true;
            this.loadingAnim.internalMethod07060(0.0F);
            HudContextMenu localValue6 = new HudContextMenu((float)localValue1, (float)localValue3, 110.0F, 6.0F)
               .internalMethod04738(LanguageManager.internalMethod07214(this.settings.isEmpty() ? "actions" : "settings"))
               .internalMethod03873();

            for (Setting localValue8 : this.settings) {
               localValue6.internalMethod05995(localValue8);
            }

            localValue6.internalMethod03323(LanguageManager.internalMethod07214("remove"), "trash", localValue1x -> {
               this.setShowing(false);
               localValue1x.internalMethod05781(false);
               RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            }).internalMethod04092(() -> {
               this.select = false;
               RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            });
            RockstarClient.getInstance().internalMethod01271().internalMethod09283().add(localValue6);
         }
      }
   }

   public void beginDrag(double localValue1, double localValue3) {
      this.dragging = true;
      this.dragX = (float)(localValue1 - this.x);
      this.dragY = (float)(localValue3 - this.y);
      this.startDragX = this.x;
      this.startDragY = this.y;
   }

   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.dragging && localValue5 == MouseButton.internalField0102) {
         this.dragging = false;
         this.anchorX = this.anchorY = null;
         if (this.x != this.startDragX || this.y != this.startDragY) {
            RockstarClient.getInstance().internalMethod01271().internalMethod07612().internalMethod00453(this, this.startDragX, this.startDragY, this.x, this.y);
         }

         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      }
   }

   private float snapDelta(AlignmentGuideLine.InternalType0164 localValue1, float localValue2, float localValue3) {
      float localValue4 = 0.0F;
      float localValue5 = 3.0F;
      float[] localValue6 = anchors(localValue2, localValue3);

      for (AlignmentGuideLine localValue8 : RockstarClient.getInstance().internalMethod01271().internalMethod07611().internalMethod00333()) {
         if (localValue8.internalMethod04528() == localValue1) {
            for (float localValue12 : localValue6) {
               float localValue13 = localValue8.internalMethod05366() - localValue12;
               float localValue14 = Math.abs(localValue13);
               if (!(localValue14 > 2.0F) && !(localValue14 >= localValue5)) {
                  localValue4 = localValue13;
                  localValue5 = localValue14;
               }
            }
         }
      }

      if (localValue5 > 2.0F) {
         return 0.0F;
      } else {
         for (AlignmentGuideLine localValue16 : RockstarClient.getInstance().internalMethod01271().internalMethod07611().internalMethod00333()) {
            if (localValue16.internalMethod04528() == localValue1) {
               for (float localValue20 : localValue6) {
                  if (Math.abs(localValue16.internalMethod05366() - localValue20 - localValue4) <= 0.001F) {
                     localValue16.internalMethod07380(true);
                     return localValue4;
                  }
               }
            }
         }

         return localValue4;
      }
   }

   private static float[] anchors(float localValue0, float localValue1) {
      return new float[]{localValue0, localValue0 + localValue1 * 0.5F, localValue0 + localValue1};
   }

   public boolean show() {
      return true;
   }

   public boolean isHovered(float localValue1, float localValue2) {
      return UiUtils.internalMethod05785(this.x, this.y, this.width, this.height, localValue1, localValue2);
   }

   public boolean isHovered(double localValue1, double localValue3) {
      return UiUtils.internalMethod05785(this.x, this.y, this.width, this.height, localValue1, localValue3);
   }

   public boolean isHovered(UiRenderContext localValue1) {
      return this.isHovered((float)localValue1.internalMethod05259(), (float)localValue1.internalMethod05261());
   }

   public void pos(float localValue1, float localValue2) {
      this.x = localValue1;
      this.y = localValue2;
      this.anchorX = this.anchorY = null;
   }

   public void reanchor(float localValue1, float localValue2, float localValue3, float localValue4) {
      if (!this.dragging) {
         if (this.anchorX == null) {
            this.anchorX = zoneOf(this.x, this.width, localValue1);
         }

         if (this.anchorY == null) {
            this.anchorY = zoneOf(this.y, this.height, localValue2);
         }

         this.x = reanchorAxis(this.anchorX, this.x, this.width, localValue1, localValue3);
         this.y = reanchorAxis(this.anchorY, this.y, this.height, localValue2, localValue4);
      }
   }

   private static AbstractHudWidget.InternalType0167 zoneOf(float localValue0, float localValue1, float localValue2) {
      float localValue3 = localValue0 + localValue1 / 2.0F;
      float localValue4 = localValue2 / 3.0F;
      if (localValue3 <= localValue4) {
         return AbstractHudWidget.InternalType0167.internalField0765;
      } else {
         return localValue3 >= localValue2 - localValue4 ? AbstractHudWidget.InternalType0167.internalField1306 : AbstractHudWidget.InternalType0167.internalField0766;
      }
   }

   private static float reanchorAxis(AbstractHudWidget.InternalType0167 localValue0, float localValue1, float localValue2, float localValue3, float localValue4) {
      float localValue5 = switch (localValue0) {
         case internalField0765 -> localValue1;
         case internalField0766 -> localValue4 / 2.0F + (localValue1 + localValue2 / 2.0F - localValue3 / 2.0F) - localValue2 / 2.0F;
         case internalField1306 -> localValue4 - localValue2 - (localValue3 - (localValue1 + localValue2));
      };
      return Math.clamp(localValue5, 0.0F, Math.max(0.0F, localValue4 - localValue2));
   }

   public void setShowing(boolean localValue1) {
      boolean localValue2 = this.showing;
      this.showing = localValue1;
      if (localValue1 && !localValue2) {
         this.rebuild();
      }

      if (localValue2 != localValue1) {
         HudManager localValue3 = RockstarClient.getInstance().internalMethod01271();
         if (localValue3 != null) {
            localValue3.internalMethod00050(this);
         }
      }
   }

   @Generated
   public float getX() {
      return this.x;
   }

   @Generated
   public float getY() {
      return this.y;
   }

   @Generated
   public float getWidth() {
      return this.width;
   }

   @Generated
   public float getHeight() {
      return this.height;
   }

   @Generated
   public AnimatedValue getAnimation() {
      return this.animation;
   }

   @Generated
   public AnimatedValue getVisible() {
      return this.visible;
   }

   @Generated
   public AnimatedValue getSelecting() {
      return this.selecting;
   }

   @Generated
   public AnimatedValue getDragAnim() {
      return this.dragAnim;
   }

   @Generated
   public AnimatedValue getBlurAnim() {
      return this.blurAnim;
   }

   @Generated
   public AnimatedValue getLoadingAnim() {
      return this.loadingAnim;
   }

   @Generated
   public boolean isShowing() {
      return this.showing;
   }

   @Generated
   public boolean isSelect() {
      return this.select;
   }

   @Generated
   public float getLastWidth() {
      return this.lastWidth;
   }

   @Generated
   public boolean isWidthTracked() {
      return this.widthTracked;
   }

   @Generated
   @Override
   public List<Setting> getSettings() {
      return this.settings;
   }

   @Generated
   public InertialRect getInertion() {
      return this.inertion;
   }

   @Generated
   public UiContainer getFlow() {
      return this.flow;
   }

   @Generated
   public long getLastFrame() {
      return this.lastFrame;
   }

   @Generated
   public boolean isDragging() {
      return this.dragging;
   }

   @Generated
   public float getDragX() {
      return this.dragX;
   }

   @Generated
   public float getDragY() {
      return this.dragY;
   }

   @Generated
   public float getStartDragX() {
      return this.startDragX;
   }

   @Generated
   public float getStartDragY() {
      return this.startDragY;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public String getIcon() {
      return this.icon;
   }

   @Generated
   public AbstractHudWidget.InternalType0167 getAnchorX() {
      return this.anchorX;
   }

   @Generated
   public AbstractHudWidget.InternalType0167 getAnchorY() {
      return this.anchorY;
   }

   @Generated
   public void setX(float localValue1) {
      this.x = localValue1;
   }

   @Generated
   public void setY(float localValue1) {
      this.y = localValue1;
   }

   @Generated
   public void setWidth(float localValue1) {
      this.width = localValue1;
   }

   @Generated
   public void setHeight(float localValue1) {
      this.height = localValue1;
   }

   @Generated
   public void setSelect(boolean localValue1) {
      this.select = localValue1;
   }

   @Generated
   public void setLastWidth(float localValue1) {
      this.lastWidth = localValue1;
   }

   @Generated
   public void setWidthTracked(boolean localValue1) {
      this.widthTracked = localValue1;
   }

   @Generated
   public void setSettings(List<Setting> localValue1) {
      this.settings = localValue1;
   }

   @Generated
   public void setFlow(UiContainer localValue1) {
      this.flow = localValue1;
   }

   @Generated
   public void setLastFrame(long localValue1) {
      this.lastFrame = localValue1;
   }

   @Generated
   public void setDragging(boolean localValue1) {
      this.dragging = localValue1;
   }

   @Generated
   public void setDragX(float localValue1) {
      this.dragX = localValue1;
   }

   @Generated
   public void setDragY(float localValue1) {
      this.dragY = localValue1;
   }

   @Generated
   public void setStartDragX(float localValue1) {
      this.startDragX = localValue1;
   }

   @Generated
   public void setStartDragY(float localValue1) {
      this.startDragY = localValue1;
   }

   @Generated
   public void setAnchorX(AbstractHudWidget.InternalType0167 localValue1) {
      this.anchorX = localValue1;
   }

   @Generated
   public void setAnchorY(AbstractHudWidget.InternalType0167 localValue1) {
      this.anchorY = localValue1;
   }

   static enum InternalType0167 {
      internalField0765,
      internalField0766,
      internalField1306;
   }
}
