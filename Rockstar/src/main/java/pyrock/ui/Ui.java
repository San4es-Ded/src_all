package pyrock.ui;







import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import jep.python.PyCallable;
import net.minecraft.util.Identifier;
import pyrock.classes.PySetting;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.internal.script.SingleValueSlider;
import rockstar.client.internal.script.TextInputField;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.internal.script.ToggleSwitchWidget;
import rockstar.client.setting.Setting;
import rockstar.client.RockstarClient;
import rockstar.client.internal.ui.SettingComponentFactory;
import rockstar.client.ui.UiElement;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.ColorSwatch;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class Ui {
   private final PyScreen screen;
   private final List<Node> collected = new ArrayList<>();

   public Ui(PyScreen localValue1) {
      this.screen = localValue1;
   }

   public Ui() {
      this.screen = null;
   }

   public void addRoot(Node localValue1) {
      if (this.screen != null) {
         this.screen.addRoot(localValue1);
      } else if (localValue1 != null) {
         this.collected.add(localValue1);
      }
   }

   public Node firstRoot() {
      return this.collected.isEmpty() ? null : this.collected.get(0);
   }

   public Node column() {
      return new Node(new UiContainer().internalMethod01863());
   }

   public Node row() {
      return new Node(new UiContainer().internalMethod05895());
   }

   public Node node(UiNode localValue1) {
      return new Node(localValue1);
   }

   public Node text(String localValue1, float localValue2, String localValue3, Object localValue4) {
      SizedFont localValue5 = font(localValue3, localValue2);
      UiElement localValue6;
      if (localValue4 instanceof PyCallable localValue7) {
         localValue6 = new UiElement().text(localValue5, localValue1, localValue1x -> callColor(localValue7));
      } else if (localValue4 instanceof ColorRGBA localValue8) {
         localValue6 = new UiElement().text(localValue5, localValue1, localValue8);
      } else {
         localValue6 = new UiElement().text(localValue5, localValue1, ThemeColors.internalField1613);
      }

      localValue6.interactive(false);
      return new Node(localValue6);
   }

   public Node textDyn(Object localValue1, float localValue2, String localValue3, Object localValue4) {
      SizedFont localValue5 = font(localValue3, localValue2);
      PyCallable localValue6 = (PyCallable)localValue1;
      Supplier localValue7 = () -> {
         try {
            Object localValue1x = localValue6.call(new Object[0]);
            return localValue1x == null ? "" : localValue1x.toString();
         } catch (Exception localValue2x) {
            return "";
         }
      };
      UiElement localValue8;
      if (localValue4 instanceof PyCallable localValue9) {
         localValue8 = new UiElement().text(localValue5, localValue7, localValue1x -> callColor(localValue9));
      } else if (localValue4 instanceof ColorRGBA localValue10) {
         localValue8 = new UiElement().text(localValue5, localValue7, localValue1x -> localValue10);
      } else {
         localValue8 = new UiElement().text(localValue5, localValue7, localValue0 -> ThemeColors.internalField1613);
      }

      localValue8.interactive(false);
      return new Node(localValue8);
   }

   public Node button(String localValue1) {
      UiElement localValue2 = new UiElement()
         .background(localValue0 -> ThemeColors.internalField1310.mulAlpha(0.1F + 0.12F * localValue0.hover()))
         .text(font("medium", 8.0F), localValue1, ThemeColors.internalField1613)
         .radius(6.0F)
         .padding(5.0F, 10.0F);
      localValue2.cursor(CursorType.internalField0567);
      return new Node(localValue2);
   }

   public Node switchWidget(PyCallable localValue1, PyCallable localValue2) {
      ToggleSwitchWidget localValue3 = new ToggleSwitchWidget(() -> truthy(localValue1));
      localValue3.internalMethod05792(() -> ThemeColors.internalField1614);
      localValue3.size(16.0F, 9.0F);
      localValue3.onClick(() -> {
         try {
            localValue2.call(new Object[]{!truthy(localValue1)});
         } catch (Exception localValue3x) {
            RockstarClient.internalField0572.error("[PyUi] switch", localValue3x);
         }
      });
      return new Node(localValue3);
   }

   public Node toggle(String localValue1, PyCallable localValue2, PyCallable localValue3) {
      UiContainer localValue4 = new UiContainer().internalMethod05895();
      localValue4.internalMethod09609();
      localValue4.internalMethod01855(TextAlignment.internalField0621).internalMethod07607(LayoutAlignment.internalField1377).internalMethod03062(6.0F);
      localValue4.internalMethod03907(new UiElement().text(font("medium", 8.0F), localValue1, ThemeColors.internalField1613).interactive(false));
      localValue4.internalMethod03907(this.switchWidget(localValue2, localValue3).element());
      return new Node(localValue4);
   }

   public Node sliderBar(PyCallable localValue1, PyCallable localValue2, float localValue3, float localValue4, float localValue5) {
      SingleValueSlider localValue6 = new SingleValueSlider(() -> floatOf(localValue1), localValue1x -> {
         try {
            localValue2.call(new Object[]{localValue1x});
         } catch (Exception localValue3x) {
            RockstarClient.internalField0572.error("[PyUi] slider", localValue3x);
         }
      }, localValue3, localValue4);
      if (localValue5 > 0.0F) {
         localValue6.internalMethod07013(localValue5);
      }

      return new Node(localValue6);
   }

   public Node slider(String localValue1, float localValue2, float localValue3, float localValue4, PyCallable localValue5, float localValue6) {
      float[] localValue7 = new float[]{localValue4};
      SingleValueSlider localValue8 = new SingleValueSlider(() -> localValue7[0], localValue2x -> {
         localValue7[0] = localValue2x;
         if (localValue5 != null) {
            try {
               localValue5.call(new Object[]{localValue2x});
            } catch (Exception localValue4x) {
               RockstarClient.internalField0572.error("[PyUi] slider", localValue4x);
            }
         }
      }, localValue2, localValue3);
      localValue8.internalMethod06319();
      if (localValue6 > 0.0F) {
         localValue8.internalMethod07013(localValue6);
      }

      UiContainer localValue9 = new UiContainer().internalMethod05895();
      localValue9.internalMethod09609();
      localValue9.internalMethod07607(LayoutAlignment.internalField1377).internalMethod03062(6.0F);
      localValue9.internalMethod03907(new UiElement().text(font("medium", 8.0F), localValue1, ThemeColors.internalField1613).interactive(false));
      localValue9.internalMethod03907(new UiElement().text(font("medium", 8.0F), () -> fmt(localValue7[0]), localValue0 -> ThemeColors.internalField1613).interactive(false));
      UiContainer localValue10 = new UiContainer().internalMethod01863();
      localValue10.internalMethod09609();
      localValue10.internalMethod03062(3.0F);
      localValue10.internalMethod03907(localValue9);
      localValue10.internalMethod03907(localValue8);
      return new Node(localValue10);
   }

   public Node setting(Object localValue1) {
      if (localValue1 instanceof PySetting localValue2) {
         localValue1 = localValue2.raw();
      }

      return new Node(SettingComponentFactory.internalMethod03724((Setting)localValue1));
   }

   public Node toggleC(PyCallable localValue1, PyCallable localValue2, ColorRGBA localValue3, ColorRGBA localValue4, ColorRGBA localValue5) {
      ToggleSwitchWidget localValue6 = new ToggleSwitchWidget(() -> truthy(localValue1));
      if (localValue3 != null) {
         localValue6.internalMethod07277(localValue3);
      }

      if (localValue4 != null) {
         localValue6.internalMethod05163(localValue4);
      }

      if (localValue5 != null) {
         localValue6.internalMethod08119(localValue5);
      }

      localValue6.size(38.0F, 22.0F);
      localValue6.onClick(() -> {
         try {
            localValue2.call(new Object[0]);
         } catch (Exception localValue2x) {
            RockstarClient.internalField0572.error("[PyUi] toggle", localValue2x);
         }
      });
      return new Node(localValue6);
   }

   public Node sliderC(
      PyCallable localValue1,
      PyCallable localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      ColorRGBA localValue6,
      ColorRGBA localValue7,
      ColorRGBA localValue8,
      ColorRGBA localValue9,
      float localValue10,
      float localValue11,
      float localValue12
   ) {
      SingleValueSlider localValue13 = new SingleValueSlider(() -> floatOf(localValue1), localValue1x -> {
         try {
            localValue2.call(new Object[]{localValue1x});
         } catch (Exception localValue3x) {
            RockstarClient.internalField0572.error("[PyUi] slider", localValue3x);
         }
      }, localValue3, localValue4);
      if (localValue5 > 0.0F) {
         localValue13.internalMethod07013(localValue5);
      }

      if (localValue6 != null) {
         localValue13.internalMethod06175(localValue6);
      }

      if (localValue7 != null) {
         localValue13.internalMethod06878(localValue7);
      }

      if (localValue8 != null) {
         localValue13.internalMethod07658(localValue8);
      }

      if (localValue9 != null) {
         localValue13.internalMethod07864(localValue9);
      }

      if (localValue10 >= 0.0F) {
         localValue13.internalMethod08796(localValue10);
      }

      if (localValue11 >= 0.0F) {
         localValue13.internalMethod09045(localValue11);
      }

      if (localValue12 > 0.0F) {
         localValue13.internalMethod00229(localValue12);
      }

      localValue13.internalMethod06319();
      return new Node(localValue13);
   }

   public Node textInput(PyCallable localValue1, PyCallable localValue2, String localValue3, ColorRGBA localValue4, ColorRGBA localValue5) {
      String localValue6 = "";

      try {
         Object localValue7 = localValue1.call(new Object[0]);
         localValue6 = localValue7 == null ? "" : localValue7.toString();
      } catch (Exception localValue8) {
      }

      TextInputField localValue9 = new TextInputField(font("regular", 7.0F), localValue6, localValue1x -> {
         try {
            localValue2.call(new Object[]{localValue1x});
         } catch (Exception localValue3x) {
            RockstarClient.internalField0572.error("[PyUi] text", localValue3x);
         }
      });
      localValue9.internalMethod01789(localValue3 != null ? localValue3 : "");
      if (localValue4 != null) {
         localValue9.internalMethod02926(localValue4);
      }

      if (localValue5 != null) {
         localValue9.internalMethod03704(localValue5);
      }

      localValue9.internalMethod01901(6.0F);
      localValue9.internalMethod06744();
      localValue9.internalMethod07996(22.0F);
      return new Node(localValue9);
   }

   public Node swatch(PyCallable localValue1) {
      ColorSwatch localValue2 = new ColorSwatch(() -> {
         try {
            return localValue1.call(new Object[0]) instanceof ColorRGBA localValue2x ? localValue2x : ThemeColors.internalField1312;
         } catch (Exception localValue3) {
            return ThemeColors.internalField1312;
         }
      });
      localValue2.internalMethod06125(14.0F, 14.0F);
      localValue2.interactive(false);
      return new Node(localValue2);
   }

   public Node icon(String localValue1, float localValue2, ColorRGBA localValue3) {
      UiElement localValue4 = new UiElement().icon(localValue1, localValue2, localValue3 != null ? localValue3 : ThemeColors.internalField1613);
      localValue4.size(localValue2, localValue2);
      return new Node(localValue4);
   }

   public Node image(Object localValue1, float localValue2, float localValue3, Object localValue4) {
      Identifier localValue5 = (Identifier)localValue1;
      UiElement localValue6 = new UiElement();
      if (localValue4 instanceof PyCallable localValue7) {
         localValue6.image(localValue5, localValue2, localValue3, localValue1x -> callColor(localValue7));
      } else if (localValue4 instanceof ColorRGBA localValue8) {
         localValue6.image(localValue5, localValue2, localValue3, localValue8);
      } else {
         localValue6.image(localValue5, localValue2, localValue3, (ColorRGBA)null);
      }

      localValue6.size(localValue2, localValue2);
      localValue6.interactive(false);
      return new Node(localValue6);
   }

   public Node space(float localValue1) {
      return new Node(new UiElement().size(localValue1, localValue1));
   }

   public Node divider() {
      UiElement localValue1 = new UiElement().background(ThemeColors.internalField1616);
      localValue1.fillWidth();
      localValue1.height(1.0F);
      return new Node(localValue1);
   }

   public ColorRGBA color(String localValue1) {
      if (localValue1 == null) {
         return ThemeColors.internalField1613;
      } else {
         String localValue2 = localValue1.toLowerCase();

         return switch (localValue2) {
            case "accent" -> ThemeColors.internalField1310;
            case "text" -> ThemeColors.internalField1613;
            case "background", "bg" -> ThemeColors.internalMethod07738();
            case "second" -> ThemeColors.internalField1614;
            case "outline" -> ThemeColors.internalField1616;
            case "white" -> ThemeColors.internalField1312;
            default -> ThemeColors.internalField1613;
         };
      }
   }

   private static SizedFont font(String localValue0, float localValue1) {
      float localValue2 = localValue1 > 0.0F ? localValue1 : 8.0F;
      return Fonts.internalMethod00574(localValue0 == null ? "medium" : localValue0).internalMethod01432(localValue2);
   }

   private static String fmt(float localValue0) {
      return localValue0 == Math.rint(localValue0) ? String.valueOf((int)localValue0) : String.format("%.1f", localValue0);
   }

   private static boolean truthy(PyCallable localValue0) {
      try {
         return Boolean.TRUE.equals(localValue0.call(new Object[0]));
      } catch (Exception localValue2) {
         return false;
      }
   }

   private static float floatOf(PyCallable localValue0) {
      try {
         return localValue0.call(new Object[0]) instanceof Number localValue2 ? localValue2.floatValue() : 0.0F;
      } catch (Exception localValue3) {
         return 0.0F;
      }
   }

   private static ColorRGBA callColor(PyCallable localValue0) {
      try {
         return localValue0.call(new Object[0]) instanceof ColorRGBA localValue2 ? localValue2 : ThemeColors.internalField1613;
      } catch (Exception localValue3) {
         return ThemeColors.internalField1613;
      }
   }
}
