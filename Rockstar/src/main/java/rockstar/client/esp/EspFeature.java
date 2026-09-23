package rockstar.client.esp;




import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import lombok.Generated;
import net.minecraft.entity.Entity;

public abstract class EspFeature implements SettingOwner, MinecraftClientAccess, ScreenMetricsAccess {
   private final String internalField0248;
   private final Set<EntityTargetType> internalField0546;
   private final Set<PlayerTargetType> internalField0545;
   private final Set<ItemTargetType> internalField1200;
   private final List<Setting> internalField0416 = new ArrayList<>();
   private final Map<EntityTargetType, BooleanSetting> internalField0543 = new HashMap<>();
   private final Map<PlayerTargetType, BooleanSetting> internalField0544 = new HashMap<>();
   private final Map<ItemTargetType, BooleanSetting> internalField1197 = new HashMap<>();
   private final Map<EntityTargetType, KeybindSetting> internalField1196 = new HashMap<>();
   private final Map<PlayerTargetType, KeybindSetting> internalField1195 = new HashMap<>();
   private final Map<ItemTargetType, KeybindSetting> internalField1198 = new HashMap<>();
   private final Map<Setting, Set<EntityTargetType>> internalField1560 = new HashMap<>();
   private final Map<String, Map<EntityTargetType, Setting>> internalField1559 = new LinkedHashMap<>();
   private final Map<String, Map<PlayerTargetType, Setting>> internalField1558 = new LinkedHashMap<>();
   private final Map<String, Map<ItemTargetType, Setting>> internalField1561 = new LinkedHashMap<>();

   protected EspFeature(String localValue1, EntityTargetType... localValue2) {
      this(localValue1, new ItemTargetType[0], localValue2);
   }

   protected EspFeature(String localValue1, ItemTargetType[] localValue2, EntityTargetType... localValue3) {
      this.internalField0248 = localValue1;
      this.internalField0546 = localValue3.length > 0 ? new LinkedHashSet<>(Arrays.asList(localValue3)) : new LinkedHashSet<>(Arrays.asList(EntityTargetType.values()));
      this.internalField0545 = this.internalField0546.contains(EntityTargetType.internalField0027)
         ? new LinkedHashSet<>(Arrays.asList(PlayerTargetType.values()))
         : new LinkedHashSet<>();
      this.internalField1200 = this.internalField0546.contains(EntityTargetType.internalField0964) && localValue2.length > 0
         ? new LinkedHashSet<>(Arrays.asList(localValue2))
         : new LinkedHashSet<>();
   }

   protected BooleanSetting internalMethod02236(String localValue1) {
      BooleanSetting localValue2 = null;

      for (EntityTargetType localValue4 : this.internalField0546) {
         if (localValue4 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue12 : this.internalField0545) {
               BooleanSetting localValue13 = new BooleanSetting(this, localValue1);
               KeybindSetting localValue14 = new KeybindSetting(this, localValue1 + ".bind");
               this.internalField0544.put(localValue12, localValue13);
               this.internalField1195.put(localValue12, localValue14);
               if (localValue2 == null) {
                  localValue2 = localValue13;
               }
            }
         } else if (localValue4 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue11 : this.internalField1200) {
               BooleanSetting localValue7 = new BooleanSetting(this, localValue1);
               KeybindSetting localValue8 = new KeybindSetting(this, localValue1 + ".bind");
               this.internalField1197.put(localValue11, localValue7);
               this.internalField1198.put(localValue11, localValue8);
               if (localValue2 == null) {
                  localValue2 = localValue7;
               }
            }
         } else {
            BooleanSetting localValue5 = new BooleanSetting(this, localValue1);
            KeybindSetting localValue6 = new KeybindSetting(this, localValue1 + ".bind");
            this.internalField0543.put(localValue4, localValue5);
            this.internalField1196.put(localValue4, localValue6);
            if (localValue2 == null) {
               localValue2 = localValue5;
            }
         }
      }

      return localValue2;
   }

   protected void internalMethod02246(EntityTargetType... localValue1) {
      for (EntityTargetType localValue5 : localValue1) {
         BooleanSetting localValue6 = this.internalField0543.get(localValue5);
         if (localValue6 != null) {
            localValue6.internalMethod06630();
         }
      }
   }

   protected void internalMethod02197(PlayerTargetType... localValue1) {
      for (PlayerTargetType localValue5 : localValue1) {
         BooleanSetting localValue6 = this.internalField0544.get(localValue5);
         if (localValue6 != null) {
            localValue6.internalMethod06630();
         }
      }
   }

   protected void internalMethod07091(ItemTargetType... localValue1) {
      for (ItemTargetType localValue5 : localValue1) {
         BooleanSetting localValue6 = this.internalField1197.get(localValue5);
         if (localValue6 != null) {
            localValue6.internalMethod06630();
         }
      }
   }

   protected <T extends Setting> T internalMethod06106(Function<EspFeature, T> localValue1) {
      Setting localValue2 = null;
      HashMap localValue3 = new HashMap();
      HashMap localValue4 = new HashMap();
      HashMap localValue5 = new HashMap();

      for (EntityTargetType localValue7 : this.internalField0546) {
         if (localValue7 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue14 : this.internalField0545) {
               Setting localValue15 = (Setting)localValue1.apply(this);
               localValue4.put(localValue14, localValue15);
               if (localValue2 == null) {
                  localValue2 = localValue15;
               }
            }
         } else if (localValue7 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue9 : this.internalField1200) {
               Setting localValue10 = (Setting)localValue1.apply(this);
               localValue5.put(localValue9, localValue10);
               if (localValue2 == null) {
                  localValue2 = localValue10;
               }
            }
         } else {
            Setting localValue8 = (Setting)localValue1.apply(this);
            localValue3.put(localValue7, localValue8);
            if (localValue2 == null) {
               localValue2 = localValue8;
            }
         }
      }

      String localValue11 = localValue2 != null ? localValue2.getName() : "";
      this.internalField1559.put(localValue11, localValue3);
      this.internalField1558.put(localValue11, localValue4);
      this.internalField1561.put(localValue11, localValue5);
      return (T)localValue2;
   }

   protected <T extends Setting> T internalMethod02651(EspFeature.InternalType0049<T> localValue1) {
      Setting localValue2 = null;
      HashMap localValue3 = new HashMap();
      HashMap localValue4 = new HashMap();
      HashMap localValue5 = new HashMap();

      for (EntityTargetType localValue7 : this.internalField0546) {
         if (localValue7 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue16 : this.internalField0545) {
               BooleanSetting localValue17 = this.internalField0544.get(localValue16);
               Setting localValue18 = localValue1.create(this, localValue17);
               localValue4.put(localValue16, localValue18);
               if (localValue2 == null) {
                  localValue2 = localValue18;
               }
            }
         } else if (localValue7 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue15 : this.internalField1200) {
               BooleanSetting localValue10 = this.internalField1197.get(localValue15);
               Setting localValue11 = localValue1.create(this, localValue10);
               localValue5.put(localValue15, localValue11);
               if (localValue2 == null) {
                  localValue2 = localValue11;
               }
            }
         } else {
            BooleanSetting localValue8 = this.internalField0543.get(localValue7);
            Setting localValue9 = localValue1.create(this, localValue8);
            localValue3.put(localValue7, localValue9);
            if (localValue2 == null) {
               localValue2 = localValue9;
            }
         }
      }

      String localValue12 = localValue2 != null ? localValue2.getName() : "";
      this.internalField1559.put(localValue12, localValue3);
      this.internalField1558.put(localValue12, localValue4);
      this.internalField1561.put(localValue12, localValue5);
      return (T)localValue2;
   }

   protected <T extends Setting> T internalMethod04340(String localValue1, EspFeature.InternalType0273<T> localValue2) {
      Setting localValue3 = null;
      HashMap localValue4 = new HashMap();
      HashMap localValue5 = new HashMap();
      HashMap localValue6 = new HashMap();

      for (EntityTargetType localValue8 : this.internalField0546) {
         if (localValue8 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue18 : this.internalField0545) {
               BooleanSetting localValue20 = this.internalField0544.get(localValue18);
               BooleanSetting localValue21 = this.internalMethod02672(localValue1, localValue18);
               Setting localValue22 = localValue2.create(this, localValue20, localValue21);
               localValue5.put(localValue18, localValue22);
               if (localValue3 == null) {
                  localValue3 = localValue22;
               }
            }
         } else if (localValue8 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue17 : this.internalField1200) {
               BooleanSetting localValue19 = this.internalField1197.get(localValue17);
               BooleanSetting localValue12 = this.internalMethod02616(localValue1, localValue17);
               Setting localValue13 = localValue2.create(this, localValue19, localValue12);
               localValue6.put(localValue17, localValue13);
               if (localValue3 == null) {
                  localValue3 = localValue13;
               }
            }
         } else {
            BooleanSetting localValue9 = this.internalField0543.get(localValue8);
            BooleanSetting localValue10 = this.internalMethod05940(localValue1, localValue8);
            Setting localValue11 = localValue2.create(this, localValue9, localValue10);
            localValue4.put(localValue8, localValue11);
            if (localValue3 == null) {
               localValue3 = localValue11;
            }
         }
      }

      String localValue14 = localValue3 != null ? localValue3.getName() : "";
      this.internalField1559.put(localValue14, localValue4);
      this.internalField1558.put(localValue14, localValue5);
      this.internalField1561.put(localValue14, localValue6);
      return (T)localValue3;
   }

   protected <T extends Setting> T internalMethod06933(EspFeature.InternalType0050<T> localValue1) {
      Setting localValue2 = null;
      HashMap localValue3 = new HashMap();
      HashMap localValue4 = new HashMap();
      HashMap localValue5 = new HashMap();

      for (EntityTargetType localValue7 : this.internalField0546) {
         if (localValue7 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue16 : this.internalField0545) {
               BooleanSetting localValue17 = this.internalField0544.get(localValue16);
               Setting localValue18 = localValue1.create(this, localValue17, EntityTargetType.internalField0027);
               localValue4.put(localValue16, localValue18);
               if (localValue2 == null) {
                  localValue2 = localValue18;
               }
            }
         } else if (localValue7 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue15 : this.internalField1200) {
               BooleanSetting localValue10 = this.internalField1197.get(localValue15);
               Setting localValue11 = localValue1.create(this, localValue10, EntityTargetType.internalField0964);
               localValue5.put(localValue15, localValue11);
               if (localValue2 == null) {
                  localValue2 = localValue11;
               }
            }
         } else {
            BooleanSetting localValue8 = this.internalField0543.get(localValue7);
            Setting localValue9 = localValue1.create(this, localValue8, localValue7);
            localValue3.put(localValue7, localValue9);
            if (localValue2 == null) {
               localValue2 = localValue9;
            }
         }
      }

      String localValue12 = localValue2 != null ? localValue2.getName() : "";
      this.internalField1559.put(localValue12, localValue3);
      this.internalField1558.put(localValue12, localValue4);
      this.internalField1561.put(localValue12, localValue5);
      return (T)localValue2;
   }

   protected <T extends Setting> T internalMethod01887(String localValue1, String localValue2, String localValue3, EspFeature.InternalType0048<T> localValue4) {
      Setting localValue5 = null;
      HashMap localValue6 = new HashMap();
      HashMap localValue7 = new HashMap();
      HashMap localValue8 = new HashMap();

      for (EntityTargetType localValue10 : this.internalField0546) {
         if (localValue10 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue22 : this.internalField0545) {
               BooleanSetting localValue24 = this.internalField0544.get(localValue22);
               BooleanSetting localValue26 = this.internalMethod02672(localValue1, localValue22);
               BooleanSetting localValue28 = this.internalMethod02672(localValue2, localValue22);
               BooleanSetting localValue29 = this.internalMethod02672(localValue3, localValue22);
               Setting localValue30 = localValue4.create(this, localValue24, localValue26, localValue28, localValue29);
               localValue7.put(localValue22, localValue30);
               if (localValue5 == null) {
                  localValue5 = localValue30;
               }
            }
         } else if (localValue10 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue21 : this.internalField1200) {
               BooleanSetting localValue23 = this.internalField1197.get(localValue21);
               BooleanSetting localValue25 = this.internalMethod02616(localValue1, localValue21);
               BooleanSetting localValue27 = this.internalMethod02616(localValue2, localValue21);
               BooleanSetting localValue16 = this.internalMethod02616(localValue3, localValue21);
               Setting localValue17 = localValue4.create(this, localValue23, localValue25, localValue27, localValue16);
               localValue8.put(localValue21, localValue17);
               if (localValue5 == null) {
                  localValue5 = localValue17;
               }
            }
         } else {
            BooleanSetting localValue11 = this.internalField0543.get(localValue10);
            BooleanSetting localValue12 = this.internalMethod05940(localValue1, localValue10);
            BooleanSetting localValue13 = this.internalMethod05940(localValue2, localValue10);
            BooleanSetting localValue14 = this.internalMethod05940(localValue3, localValue10);
            Setting localValue15 = localValue4.create(this, localValue11, localValue12, localValue13, localValue14);
            localValue6.put(localValue10, localValue15);
            if (localValue5 == null) {
               localValue5 = localValue15;
            }
         }
      }

      String localValue18 = localValue5 != null ? localValue5.getName() : "";
      this.internalField1559.put(localValue18, localValue6);
      this.internalField1558.put(localValue18, localValue7);
      this.internalField1561.put(localValue18, localValue8);
      return (T)localValue5;
   }

   protected <T extends Setting> T internalMethod06698(String localValue1, String localValue2, EspFeature.InternalType0272<T> localValue3) {
      Setting localValue4 = null;
      HashMap localValue5 = new HashMap();
      HashMap localValue6 = new HashMap();
      HashMap localValue7 = new HashMap();

      for (EntityTargetType localValue9 : this.internalField0546) {
         if (localValue9 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue20 : this.internalField0545) {
               BooleanSetting localValue22 = this.internalField0544.get(localValue20);
               BooleanSetting localValue24 = this.internalMethod02672(localValue1, localValue20);
               BooleanSetting localValue25 = this.internalMethod02672(localValue2, localValue20);
               Setting localValue26 = localValue3.create(this, localValue22, localValue24, localValue25);
               localValue6.put(localValue20, localValue26);
               if (localValue4 == null) {
                  localValue4 = localValue26;
               }
            }
         } else if (localValue9 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue19 : this.internalField1200) {
               BooleanSetting localValue21 = this.internalField1197.get(localValue19);
               BooleanSetting localValue23 = this.internalMethod02616(localValue1, localValue19);
               BooleanSetting localValue14 = this.internalMethod02616(localValue2, localValue19);
               Setting localValue15 = localValue3.create(this, localValue21, localValue23, localValue14);
               localValue7.put(localValue19, localValue15);
               if (localValue4 == null) {
                  localValue4 = localValue15;
               }
            }
         } else {
            BooleanSetting localValue10 = this.internalField0543.get(localValue9);
            BooleanSetting localValue11 = this.internalMethod05940(localValue1, localValue9);
            BooleanSetting localValue12 = this.internalMethod05940(localValue2, localValue9);
            Setting localValue13 = localValue3.create(this, localValue10, localValue11, localValue12);
            localValue5.put(localValue9, localValue13);
            if (localValue4 == null) {
               localValue4 = localValue13;
            }
         }
      }

      String localValue16 = localValue4 != null ? localValue4.getName() : "";
      this.internalField1559.put(localValue16, localValue5);
      this.internalField1558.put(localValue16, localValue6);
      this.internalField1561.put(localValue16, localValue7);
      return (T)localValue4;
   }

   protected <T extends Setting> T internalMethod06485(String localValue1, Function<BooleanSetting, T> localValue2) {
      Setting localValue3 = null;
      HashMap localValue4 = new HashMap();
      HashMap localValue5 = new HashMap();
      HashMap localValue6 = new HashMap();

      for (EntityTargetType localValue8 : this.internalField0546) {
         if (localValue8 == EntityTargetType.internalField0027) {
            for (PlayerTargetType localValue17 : this.internalField0545) {
               BooleanSetting localValue18 = this.internalMethod02672(localValue1, localValue17);
               Setting localValue19 = (Setting)localValue2.apply(localValue18);
               localValue5.put(localValue17, localValue19);
               if (localValue3 == null) {
                  localValue3 = localValue19;
               }
            }
         } else if (localValue8 == EntityTargetType.internalField0964 && !this.internalField1200.isEmpty()) {
            for (ItemTargetType localValue16 : this.internalField1200) {
               BooleanSetting localValue11 = this.internalMethod02616(localValue1, localValue16);
               Setting localValue12 = (Setting)localValue2.apply(localValue11);
               localValue6.put(localValue16, localValue12);
               if (localValue3 == null) {
                  localValue3 = localValue12;
               }
            }
         } else {
            BooleanSetting localValue9 = this.internalMethod05940(localValue1, localValue8);
            Setting localValue10 = (Setting)localValue2.apply(localValue9);
            localValue4.put(localValue8, localValue10);
            if (localValue3 == null) {
               localValue3 = localValue10;
            }
         }
      }

      String localValue13 = localValue3 != null ? localValue3.getName() : "";
      this.internalField1559.put(localValue13, localValue4);
      this.internalField1558.put(localValue13, localValue5);
      this.internalField1561.put(localValue13, localValue6);
      return (T)localValue3;
   }

   protected <T extends Setting> T internalMethod06816(Function<EspFeature, T> localValue1) {
      Setting localValue2 = null;
      HashMap localValue3 = new HashMap();

      for (PlayerTargetType localValue5 : this.internalField0545) {
         Setting localValue6 = (Setting)localValue1.apply(this);
         localValue3.put(localValue5, localValue6);
         if (localValue2 == null) {
            localValue2 = localValue6;
         }
      }

      String localValue7 = localValue2 != null ? localValue2.getName() : "";
      this.internalField1558.put(localValue7, localValue3);
      return (T)localValue2;
   }

   public <T extends Setting> T internalMethod05940(String localValue1, EntityTargetType localValue2) {
      Map localValue3 = this.internalField1559.get(localValue1);
      return (T)(localValue3 != null ? localValue3.get(localValue2) : null);
   }

   public <T extends Setting> T internalMethod02672(String localValue1, PlayerTargetType localValue2) {
      Map localValue3 = this.internalField1558.get(localValue1);
      return (T)(localValue3 != null ? localValue3.get(localValue2) : null);
   }

   public <T extends Setting> T internalMethod02616(String localValue1, ItemTargetType localValue2) {
      Map localValue3 = this.internalField1561.get(localValue1);
      return (T)(localValue3 != null ? localValue3.get(localValue2) : null);
   }

   protected void internalMethod00430(Setting localValue1, EntityTargetType... localValue2) {
      this.internalField1560.put(localValue1, new HashSet<>(Arrays.asList(localValue2)));
   }

   public void internalMethod06967() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public boolean internalMethod05593(EntityTargetType localValue1) {
      return this.internalField0546.contains(localValue1);
   }

   public boolean internalMethod05541(PlayerTargetType localValue1) {
      return this.internalField0545.contains(localValue1);
   }

   public boolean internalMethod02299(ItemTargetType localValue1) {
      return this.internalField1200.contains(localValue1);
   }

   public boolean internalMethod06206(EntityTargetType localValue1) {
      if (!EspManager.internalMethod03145()) {
         return false;
      } else {
         BooleanSetting localValue2 = this.internalField0543.get(localValue1);
         return localValue2 != null && localValue2.internalMethod04496();
      }
   }

   public boolean internalMethod06170(PlayerTargetType localValue1) {
      if (!EspManager.internalMethod03145()) {
         return false;
      } else {
         BooleanSetting localValue2 = this.internalField0544.get(localValue1);
         return localValue2 != null && localValue2.internalMethod04496();
      }
   }

   public boolean internalMethod02927(ItemTargetType localValue1) {
      if (!EspManager.internalMethod03145()) {
         return false;
      } else {
         BooleanSetting localValue2 = this.internalField1197.get(localValue1);
         return localValue2 != null && localValue2.internalMethod04496();
      }
   }

   public boolean internalMethod06968() {
      if (!EspManager.internalMethod03145()) {
         return false;
      } else {
         for (BooleanSetting localValue2 : this.internalField0543.values()) {
            if (localValue2.internalMethod04496()) {
               return true;
            }
         }

         for (BooleanSetting localValue5 : this.internalField0544.values()) {
            if (localValue5.internalMethod04496()) {
               return true;
            }
         }

         for (BooleanSetting localValue6 : this.internalField1197.values()) {
            if (localValue6.internalMethod04496()) {
               return true;
            }
         }

         return false;
      }
   }

   public void internalMethod06439(UiRenderContext localValue1, Entity localValue2, float localValue3, float localValue4, EntityTargetType localValue5, PlayerTargetType localValue6) {
   }

   public BooleanSetting internalMethod02947(EntityTargetType localValue1) {
      return this.internalField0543.get(localValue1);
   }

   public BooleanSetting internalMethod06156(PlayerTargetType localValue1) {
      return this.internalField0544.get(localValue1);
   }

   public BooleanSetting internalMethod06073(ItemTargetType localValue1) {
      return this.internalField1197.get(localValue1);
   }

   public KeybindSetting internalMethod02946(EntityTargetType localValue1) {
      return this.internalField1196.get(localValue1);
   }

   public KeybindSetting internalMethod06155(PlayerTargetType localValue1) {
      return this.internalField1195.get(localValue1);
   }

   public KeybindSetting internalMethod06072(ItemTargetType localValue1) {
      return this.internalField1198.get(localValue1);
   }

   public boolean internalMethod02761(int localValue1) {
      boolean localValue2 = false;

      for (Entry localValue4 : this.internalField1196.entrySet()) {
         BooleanSetting localValue5 = this.internalField0543.get(localValue4.getKey());
         if (localValue5 != null && ((KeybindSetting)localValue4.getValue()).internalMethod02165(localValue1)) {
            localValue5.toggle();
            localValue2 = true;
         }
      }

      for (Entry localValue8 : this.internalField1195.entrySet()) {
         BooleanSetting localValue10 = this.internalField0544.get(localValue8.getKey());
         if (localValue10 != null && ((KeybindSetting)localValue8.getValue()).internalMethod02165(localValue1)) {
            localValue10.toggle();
            localValue2 = true;
         }
      }

      for (Entry localValue9 : this.internalField1198.entrySet()) {
         BooleanSetting localValue11 = this.internalField1197.get(localValue9.getKey());
         if (localValue11 != null && ((KeybindSetting)localValue9.getValue()).internalMethod02165(localValue1)) {
            localValue11.toggle();
            localValue2 = true;
         }
      }

      return localValue2;
   }

   public List<Setting> internalMethod03913(EntityTargetType localValue1) {
      ArrayList localValue2 = new ArrayList();
      Set localValue3 = this.internalMethod08032();

      for (Setting localValue5 : this.internalField0416) {
         if (!localValue3.contains(localValue5)) {
            Set localValue6 = this.internalField1560.get(localValue5);
            if (localValue6 == null || localValue6.contains(localValue1)) {
               localValue2.add(localValue5);
            }
         }
      }

      return localValue2;
   }

   public List<Setting> internalMethod06578(EntityTargetType localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (Map localValue4 : this.internalField1559.values()) {
         Setting localValue5 = (Setting)localValue4.get(localValue1);
         if (localValue5 != null) {
            localValue2.add(localValue5);
         }
      }

      return localValue2;
   }

   public List<Setting> internalMethod04288(PlayerTargetType localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (Map localValue4 : this.internalField1558.values()) {
         Setting localValue5 = (Setting)localValue4.get(localValue1);
         if (localValue5 != null) {
            localValue2.add(localValue5);
         }
      }

      return localValue2;
   }

   public List<Setting> internalMethod00082(ItemTargetType localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (Map localValue4 : this.internalField1561.values()) {
         Setting localValue5 = (Setting)localValue4.get(localValue1);
         if (localValue5 != null) {
            localValue2.add(localValue5);
         }
      }

      return localValue2;
   }

   private Set<Setting> internalMethod08032() {
      HashSet localValue1 = new HashSet();
      localValue1.addAll(this.internalField0543.values());
      localValue1.addAll(this.internalField0544.values());
      localValue1.addAll(this.internalField1197.values());
      localValue1.addAll(this.internalField1196.values());
      localValue1.addAll(this.internalField1195.values());
      localValue1.addAll(this.internalField1198.values());

      for (Map localValue3 : this.internalField1559.values()) {
         localValue1.addAll(localValue3.values());
      }

      for (Map localValue6 : this.internalField1558.values()) {
         localValue1.addAll(localValue6.values());
      }

      for (Map localValue7 : this.internalField1561.values()) {
         localValue1.addAll(localValue7.values());
      }

      return localValue1;
   }

   public JsonObject internalMethod06190() {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("name", this.internalField0248);
      JsonObject localValue2 = new JsonObject();

      for (Entry localValue4 : this.internalField0543.entrySet()) {
         localValue2.add(((EntityTargetType)localValue4.getKey()).internalMethod00613(), ((BooleanSetting)localValue4.getValue()).toJson());
      }

      localValue1.add("enabledByType", localValue2);
      JsonObject localValue16 = new JsonObject();

      for (Entry localValue5 : this.internalField0544.entrySet()) {
         localValue16.add(((PlayerTargetType)localValue5.getKey()).internalMethod03561(), ((BooleanSetting)localValue5.getValue()).toJson());
      }

      localValue1.add("enabledByPlayerSubType", localValue16);
      JsonObject localValue18 = new JsonObject();

      for (Entry localValue6 : this.internalField1197.entrySet()) {
         localValue18.add(((ItemTargetType)localValue6.getKey()).internalMethod04152(), ((BooleanSetting)localValue6.getValue()).toJson());
      }

      localValue1.add("enabledByItemSubType", localValue18);
      JsonObject localValue20 = new JsonObject();

      for (Entry localValue7 : this.internalField1196.entrySet()) {
         localValue20.add(((EntityTargetType)localValue7.getKey()).internalMethod00613(), ((KeybindSetting)localValue7.getValue()).toJson());
      }

      localValue1.add("bindsByType", localValue20);
      JsonObject localValue22 = new JsonObject();

      for (Entry localValue8 : this.internalField1195.entrySet()) {
         localValue22.add(((PlayerTargetType)localValue8.getKey()).internalMethod03561(), ((KeybindSetting)localValue8.getValue()).toJson());
      }

      localValue1.add("bindsByPlayerSubType", localValue22);
      JsonObject localValue24 = new JsonObject();

      for (Entry localValue9 : this.internalField1198.entrySet()) {
         localValue24.add(((ItemTargetType)localValue9.getKey()).internalMethod04152(), ((KeybindSetting)localValue9.getValue()).toJson());
      }

      localValue1.add("bindsByItemSubType", localValue24);
      JsonObject localValue26 = new JsonObject();

      for (Entry localValue10 : this.internalField1559.entrySet()) {
         JsonObject localValue11 = new JsonObject();

         for (Entry localValue13 : (Iterable<Entry>)(Iterable<?>)((Map)localValue10.getValue()).entrySet()) {
            localValue11.add(((EntityTargetType)localValue13.getKey()).internalMethod00613(), ((Setting)localValue13.getValue()).toJson());
         }

         localValue26.add((String)localValue10.getKey(), localValue11);
      }

      localValue1.add("perTypeSettings", localValue26);
      JsonObject localValue28 = new JsonObject();

      for (Entry localValue31 : this.internalField1558.entrySet()) {
         JsonObject localValue34 = new JsonObject();

         for (Entry localValue14 : (Iterable<Entry>)(Iterable<?>)((Map)localValue31.getValue()).entrySet()) {
            localValue34.add(((PlayerTargetType)localValue14.getKey()).internalMethod03561(), ((Setting)localValue14.getValue()).toJson());
         }

         localValue28.add((String)localValue31.getKey(), localValue34);
      }

      localValue1.add("perPlayerSubTypeSettings", localValue28);
      JsonObject localValue30 = new JsonObject();

      for (Entry localValue35 : this.internalField1561.entrySet()) {
         JsonObject localValue38 = new JsonObject();

         for (Entry localValue15 : (Iterable<Entry>)(Iterable<?>)((Map)localValue35.getValue()).entrySet()) {
            localValue38.add(((ItemTargetType)localValue15.getKey()).internalMethod04152(), ((Setting)localValue15.getValue()).toJson());
         }

         localValue30.add((String)localValue35.getKey(), localValue38);
      }

      localValue1.add("perItemSubTypeSettings", localValue30);
      JsonObject localValue33 = new JsonObject();
      Set localValue36 = this.internalMethod08032();

      for (Setting localValue41 : this.internalField0416) {
         if (!localValue36.contains(localValue41)) {
            localValue33.add(localValue41.getName(), localValue41.toJson());
         }
      }

      localValue1.add("globalSettings", localValue33);
      return localValue1;
   }

   public void internalMethod03652(JsonObject localValue1) {
      if (localValue1.has("enabledByType")) {
         JsonObject localValue2 = localValue1.getAsJsonObject("enabledByType");

         for (Entry localValue4 : this.internalField0543.entrySet()) {
            String localValue5 = ((EntityTargetType)localValue4.getKey()).internalMethod00613();
            if (localValue2.has(localValue5)) {
               ((BooleanSetting)localValue4.getValue()).fromJson(localValue2.get(localValue5));
            }
         }
      }

      if (localValue1.has("enabledByPlayerSubType")) {
         JsonObject localValue10 = localValue1.getAsJsonObject("enabledByPlayerSubType");

         for (Entry localValue28 : this.internalField0544.entrySet()) {
            String localValue37 = ((PlayerTargetType)localValue28.getKey()).internalMethod03561();
            if (localValue10.has(localValue37)) {
               ((BooleanSetting)localValue28.getValue()).fromJson(localValue10.get(localValue37));
            }
         }
      }

      if (localValue1.has("enabledByItemSubType")) {
         JsonObject localValue11 = localValue1.getAsJsonObject("enabledByItemSubType");

         for (Entry localValue29 : this.internalField1197.entrySet()) {
            String localValue38 = ((ItemTargetType)localValue29.getKey()).internalMethod04152();
            if (localValue11.has(localValue38)) {
               ((BooleanSetting)localValue29.getValue()).fromJson(localValue11.get(localValue38));
            }
         }
      }

      if (localValue1.has("bindsByType")) {
         JsonObject localValue12 = localValue1.getAsJsonObject("bindsByType");

         for (Entry localValue30 : this.internalField1196.entrySet()) {
            String localValue39 = ((EntityTargetType)localValue30.getKey()).internalMethod00613();
            if (localValue12.has(localValue39)) {
               ((KeybindSetting)localValue30.getValue()).fromJson(localValue12.get(localValue39));
            }
         }
      }

      if (localValue1.has("bindsByPlayerSubType")) {
         JsonObject localValue13 = localValue1.getAsJsonObject("bindsByPlayerSubType");

         for (Entry localValue31 : this.internalField1195.entrySet()) {
            String localValue40 = ((PlayerTargetType)localValue31.getKey()).internalMethod03561();
            if (localValue13.has(localValue40)) {
               ((KeybindSetting)localValue31.getValue()).fromJson(localValue13.get(localValue40));
            }
         }
      }

      if (localValue1.has("bindsByItemSubType")) {
         JsonObject localValue14 = localValue1.getAsJsonObject("bindsByItemSubType");

         for (Entry localValue32 : this.internalField1198.entrySet()) {
            String localValue41 = ((ItemTargetType)localValue32.getKey()).internalMethod04152();
            if (localValue14.has(localValue41)) {
               ((KeybindSetting)localValue32.getValue()).fromJson(localValue14.get(localValue41));
            }
         }
      }

      if (localValue1.has("perTypeSettings")) {
         JsonObject localValue15 = localValue1.getAsJsonObject("perTypeSettings");

         for (Entry localValue33 : this.internalField1559.entrySet()) {
            String localValue42 = (String)localValue33.getKey();
            if (localValue15.has(localValue42)) {
               JsonObject localValue6 = localValue15.getAsJsonObject(localValue42);

               for (Entry localValue8 : (Iterable<Entry>)(Iterable<?>)((Map)localValue33.getValue()).entrySet()) {
                  String localValue9 = ((EntityTargetType)localValue8.getKey()).internalMethod00613();
                  if (localValue6.has(localValue9)) {
                     ((Setting)localValue8.getValue()).fromJson(localValue6.get(localValue9));
                  }
               }
            }
         }
      }

      if (localValue1.has("perPlayerSubTypeSettings")) {
         JsonObject localValue16 = localValue1.getAsJsonObject("perPlayerSubTypeSettings");

         for (Entry localValue34 : this.internalField1558.entrySet()) {
            String localValue43 = (String)localValue34.getKey();
            if (localValue16.has(localValue43)) {
               JsonObject localValue46 = localValue16.getAsJsonObject(localValue43);

               for (Entry localValue50 : (Iterable<Entry>)(Iterable<?>)((Map)localValue34.getValue()).entrySet()) {
                  String localValue52 = ((PlayerTargetType)localValue50.getKey()).internalMethod03561();
                  if (localValue46.has(localValue52)) {
                     ((Setting)localValue50.getValue()).fromJson(localValue46.get(localValue52));
                  }
               }
            }
         }
      }

      if (localValue1.has("perItemSubTypeSettings")) {
         JsonObject localValue17 = localValue1.getAsJsonObject("perItemSubTypeSettings");

         for (Entry localValue35 : this.internalField1561.entrySet()) {
            String localValue44 = (String)localValue35.getKey();
            if (localValue17.has(localValue44)) {
               JsonObject localValue47 = localValue17.getAsJsonObject(localValue44);

               for (Entry localValue51 : (Iterable<Entry>)(Iterable<?>)((Map)localValue35.getValue()).entrySet()) {
                  String localValue53 = ((ItemTargetType)localValue51.getKey()).internalMethod04152();
                  if (localValue47.has(localValue53)) {
                     ((Setting)localValue51.getValue()).fromJson(localValue47.get(localValue53));
                  }
               }
            }
         }
      }

      if (localValue1.has("globalSettings")) {
         JsonObject localValue18 = localValue1.getAsJsonObject("globalSettings");
         Set localValue27 = this.internalMethod08032();

         for (Setting localValue45 : this.internalField0416) {
            if (!localValue27.contains(localValue45) && localValue18.has(localValue45.getName())) {
               localValue45.fromJson(localValue18.get(localValue45.getName()));
            }
         }
      }
   }

   @Generated
   public String internalMethod01940() {
      return this.internalField0248;
   }

   @Generated
   public Set<EntityTargetType> internalMethod07099() {
      return this.internalField0546;
   }

   @Generated
   public Set<PlayerTargetType> internalMethod03615() {
      return this.internalField0545;
   }

   @Generated
   public Set<ItemTargetType> internalMethod08691() {
      return this.internalField1200;
   }

   @Generated
   @Override
   public List<Setting> getSettings() {
      return this.internalField0416;
   }

   @Generated
   public Map<EntityTargetType, BooleanSetting> internalMethod07098() {
      return this.internalField0543;
   }

   @Generated
   public Map<PlayerTargetType, BooleanSetting> internalMethod03614() {
      return this.internalField0544;
   }

   @Generated
   public Map<ItemTargetType, BooleanSetting> internalMethod08690() {
      return this.internalField1197;
   }

   @Generated
   public Map<EntityTargetType, KeybindSetting> internalMethod08031() {
      return this.internalField1196;
   }

   @Generated
   public Map<PlayerTargetType, KeybindSetting> internalMethod08235() {
      return this.internalField1195;
   }

   @Generated
   public Map<ItemTargetType, KeybindSetting> internalMethod09115() {
      return this.internalField1198;
   }

   @Generated
   public Map<Setting, Set<EntityTargetType>> internalMethod09370() {
      return this.internalField1560;
   }

   @Generated
   public Map<String, Map<EntityTargetType, Setting>> internalMethod09827() {
      return this.internalField1559;
   }

   @Generated
   public Map<String, Map<PlayerTargetType, Setting>> internalMethod09589() {
      return this.internalField1558;
   }

   @Generated
   public Map<String, Map<ItemTargetType, Setting>> internalMethod09608() {
      return this.internalField1561;
   }

   @FunctionalInterface
   protected interface InternalType0048<T extends Setting> {
      T create(EspFeature localValue1, BooleanSetting localValue2, BooleanSetting localValue3, BooleanSetting localValue4, BooleanSetting localValue5);
   }

   @FunctionalInterface
   protected interface InternalType0049<T extends Setting> {
      T create(EspFeature localValue1, BooleanSetting localValue2);
   }

   @FunctionalInterface
   protected interface InternalType0050<T extends Setting> {
      T create(EspFeature localValue1, BooleanSetting localValue2, EntityTargetType localValue3);
   }

   @FunctionalInterface
   protected interface InternalType0272<T extends Setting> {
      T create(EspFeature localValue1, BooleanSetting localValue2, BooleanSetting localValue3, BooleanSetting localValue4);
   }

   @FunctionalInterface
   protected interface InternalType0273<T extends Setting> {
      T create(EspFeature localValue1, BooleanSetting localValue2, BooleanSetting localValue3);
   }
}
