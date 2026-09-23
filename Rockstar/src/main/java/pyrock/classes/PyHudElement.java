package pyrock.classes;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.auth.*;
import java.util.Objects;
import jep.python.PyCallable;
import lombok.Generated;
import net.minecraft.text.Text;
import pyrock.ui.Node;
import pyrock.ui.Ui;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.auth.ClientDataConfig;
import rockstar.client.internal.ui.AbstractHudWidget;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.ClientMessages;
import rockstar.client.ui.UiContainer;

public class PyHudElement extends AbstractHudWidget {
   private final PythonScript owner;
   private PyCallable renderer;
   private PyCallable layoutBuilder;
   private PyCallable signatureFn;
   private Object lastSig;
   private PyCallable visibleWhen;
   private boolean disposed;
   private boolean errored;

   public PyHudElement(PythonScript localValue1, String localValue2, String localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
      super(localValue2, localValue3);
      this.owner = localValue1;
      this.width = Math.max(1.0F, localValue4);
      this.height = Math.max(1.0F, localValue5);
      this.pos(localValue6, localValue7);
   }

   public PyHudElement renderer(PyCallable localValue1) {
      this.renderer = localValue1;
      this.errored = false;
      this.rebuild();
      return this;
   }

   public PyHudElement render(PyCallable localValue1) {
      return this.renderer(localValue1);
   }

   public PyHudElement layout(PyCallable localValue1) {
      this.layoutBuilder = localValue1;
      this.renderer = null;
      this.errored = false;
      this.rebuild();
      return this;
   }

   public PyHudElement signature(PyCallable localValue1) {
      this.signatureFn = localValue1;
      this.lastSig = null;
      return this;
   }

   @Override
   public UiContainer build() {
      if (this.layoutBuilder == null) {
         return null;
      } else {
         try (AutoCloseable localValue1 = PythonScript.internalMethod02561(this.owner)) {
            Ui localValue2 = new Ui();
            this.layoutBuilder.call(new Object[]{localValue2});
            Node localValue3 = localValue2.firstRoot();
            return localValue3 != null && localValue3.element() instanceof UiContainer localValue4 ? localValue4 : null;
         } catch (Throwable localValue8) {
            this.handleError("layout", localValue8);
            return null;
         }
      }
   }

   public PyHudElement visibleWhen(PyCallable localValue1) {
      this.visibleWhen = localValue1;
      this.errored = false;
      return this;
   }

   public PyHudElement size(double localValue1, double localValue3) {
      this.width = Math.max(1.0F, (float)localValue1);
      this.height = Math.max(1.0F, (float)localValue3);
      return this;
   }

   public PyHudElement width(double localValue1) {
      this.width = Math.max(1.0F, (float)localValue1);
      return this;
   }

   public PyHudElement height(double localValue1) {
      this.height = Math.max(1.0F, (float)localValue1);
      return this;
   }

   public PyHudElement position(double localValue1, double localValue3) {
      this.pos((float)localValue1, (float)localValue3);
      return this;
   }

   public PyHudElement shown(boolean localValue1) {
      this.setShowing(localValue1);
      return this;
   }

   public float renderAlpha() {
      return this.animation.internalMethod02881() * this.visible.internalMethod02881();
   }

   public float dragAlpha() {
      return this.dragAnim.internalMethod02881();
   }

   public boolean ownedBy(PythonScript localValue1) {
      return this.owner == localValue1;
   }

   public boolean remove() {
      this.dispose();
      ClientDataConfig.internalMethod02698(this.getName());
      boolean localValue1 = RockstarClient.getInstance().internalMethod01271().internalMethod09520().remove(this);
      if (localValue1) {
         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      }

      return localValue1;
   }

   public void dispose() {
      this.disposed = true;
      this.renderer = null;
      this.layoutBuilder = null;
      this.signatureFn = null;
      this.visibleWhen = null;
      this.setShowing(false);
   }

   @Override
   public boolean show() {
      if (!this.alive()) {
         return false;
      } else if (this.visibleWhen == null) {
         return true;
      } else {
         try {
            boolean localValue2;
            try (AutoCloseable localValue1 = PythonScript.internalMethod02561(this.owner)) {
               localValue2 = truthy(this.visibleWhen.call(new Object[0]));
            }

            return localValue2;
         } catch (Throwable localValue6) {
            this.handleError("visible", localValue6);
            return false;
         }
      }
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      if (this.alive()) {
         if (this.layoutBuilder != null) {
            if (this.signatureFn != null) {
               try (AutoCloseable localValue11 = PythonScript.internalMethod02561(this.owner)) {
                  Object localValue3 = this.signatureFn.call(new Object[0]);
                  if (!Objects.equals(localValue3, this.lastSig)) {
                     this.lastSig = localValue3;
                     this.rebuild();
                  }
               } catch (Throwable localValue8) {
                  this.handleError("signature", localValue8);
               }
            }

            super.renderComponent(localValue1);
         } else if (this.renderer == null) {
            this.renderFallback(localValue1);
         } else {
            try (AutoCloseable localValue2 = PythonScript.internalMethod02561(this.owner)) {
               this.renderer.call(new Object[]{localValue1, this});
            } catch (Throwable localValue10) {
               this.handleError("render", localValue10);
            }
         }
      }
   }

   private boolean alive() {
      return !this.disposed && !this.errored && (this.owner == null || this.owner.internalMethod08681());
   }

   private void renderFallback(UiRenderContext localValue1) {
      localValue1.drawClientRect(this.x, this.y, this.width, this.height, this.renderAlpha(), this.dragAlpha(), 3.0F);
      SizedFont localValue2 = Fonts.internalField0449.internalMethod01432(7.0F);
      localValue1.drawText(localValue2, this.getName(), this.x + 6.0F, this.y + this.height / 2.0F - localValue2.internalMethod04890() / 2.0F, ThemeColors.internalMethod08459());
   }

   private void handleError(String localValue1, Throwable localValue2) {
      if (!this.errored) {
         this.errored = true;
         this.renderer = null;
         this.visibleWhen = null;
         this.setShowing(false);
         String localValue3 = localValue2.getMessage();
         if (localValue3 == null || localValue3.isBlank()) {
            localValue3 = localValue2.getClass().getSimpleName();
         }

         int localValue4 = localValue3.indexOf(58);
         if (localValue4 >= 0 && localValue4 + 1 < localValue3.length()) {
            localValue3 = localValue3.substring(localValue4 + 1).trim();
         }

         ClientMessages.internalMethod09025(Text.of("[Python HUD Error] " + this.getName() + " (" + localValue1 + "): " + localValue3));
         RockstarClient.internalField0572.error("Python HUD error in '{}' during {}", new Object[]{this.getName(), localValue1, localValue2});
      }
   }

   private static boolean truthy(Object localValue0) {
      if (localValue0 == null) {
         return false;
      } else if (localValue0 instanceof Boolean localValue3) {
         return localValue3;
      } else if (localValue0 instanceof Number localValue2) {
         return localValue2.doubleValue() != 0.0;
      } else {
         return localValue0 instanceof CharSequence localValue1 ? !localValue1.isEmpty() : true;
      }
   }

   @Generated
   public PythonScript getOwner() {
      return this.owner;
   }

   @Generated
   public PyCallable getRenderer() {
      return this.renderer;
   }

   @Generated
   public PyCallable getLayoutBuilder() {
      return this.layoutBuilder;
   }

   @Generated
   public PyCallable getSignatureFn() {
      return this.signatureFn;
   }

   @Generated
   public Object getLastSig() {
      return this.lastSig;
   }

   @Generated
   public PyCallable getVisibleWhen() {
      return this.visibleWhen;
   }

   @Generated
   public boolean isDisposed() {
      return this.disposed;
   }

   @Generated
   public boolean isErrored() {
      return this.errored;
   }
}
