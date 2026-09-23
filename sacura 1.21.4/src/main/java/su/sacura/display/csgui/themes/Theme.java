 package su.sacura.display.csgui.themes;
 
 import java.awt.Color;
 import su.sacura.features.modules.settings.impl.ColorSetting;
 
 public class Theme {
   public String name;
   
   public String author;
   
   public ColorSetting backgroundColor;
   
   public ColorSetting accentColor;
   
   public ColorSetting textColor;
   
   public ColorSetting borderColor;
   
   public Theme(String name, String author, Color backgroundColor, Color accentColor, Color textColor, Color borderColor) {
     this.name = name;
     this.author = author;
     this.backgroundColor = new ColorSetting("Смена фона", Integer.valueOf(backgroundColor.getRGB()));
     this.accentColor = new ColorSetting("Акцент", Integer.valueOf(accentColor.getRGB()));
     this.textColor = new ColorSetting("Цвет текста", Integer.valueOf(textColor.getRGB()));
     this.borderColor = new ColorSetting("Цвет обводки", Integer.valueOf(borderColor.getRGB()));
   }
 }


