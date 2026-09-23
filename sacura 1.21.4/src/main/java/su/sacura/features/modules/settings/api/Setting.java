 package su.sacura.features.modules.settings.api;
 
 import java.util.function.Supplier;
 import su.sacura.util.type.ISetting;
 
 public class Setting<Value> implements ISetting {
   Value defaultVal;
   
   String settingName;
   
   String description;
   
   public Supplier<Boolean> visible = () -> Boolean.valueOf(true);
   
   public Setting(String name, Value defaultVal) {
     this.settingName = name;
     this.defaultVal = defaultVal;
   }
   
   public String getName() {
     return this.settingName;
   }
   
   public void set(Value value) {
     this.defaultVal = value;
   }
   
   public Setting<?> setVisible(Supplier<Boolean> bool) {
     this.visible = bool;
     return this;
   }
   
   public Setting<Value> setDescription(String description) {
     this.description = description;
     return this;
   }
   
   public String getDescription() {
     return this.description;
   }
   
   public Value get() {
     return this.defaultVal;
   }
 }


