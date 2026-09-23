package pyrock.classes;



import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import pyrock.utility.render.PyAssets;
import pyrock.utility.render.PyDynamicTexture;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.script.SkinTextureLoader;
import rockstar.client.internal.core.SkinOverrideStore;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

public class PySkins implements MinecraftClientAccess {
   private final PythonScript creator = PythonScript.internalMethod00581();

   public void set(Object localValue1, Object localValue2, Object localValue3, Object localValue4, Object localValue5) {
      if (localValue2 != null) {
         this.skin(localValue1, localValue2);
      }

      if (localValue3 != null) {
         this.cape(localValue1, localValue3);
      }

      if (localValue4 != null) {
         this.elytra(localValue1, localValue4);
      }

      if (localValue5 != null) {
         this.model(localValue1, localValue5);
      }
   }

   public void skin(Object localValue1, Object localValue2) {
      String localValue3 = this.target(localValue1);
      if (localValue2 == null) {
         SkinOverrideStore.InternalType0180 localValue6 = SkinOverrideStore.internalMethod00141(this.owner(), localValue3);
         localValue6.internalMethod06120(null);
         localValue6.internalMethod03414(null);
         SkinOverrideStore.internalMethod05341(localValue3);
      } else {
         Identifier localValue4 = this.texture(localValue2);
         if (localValue4 != null) {
            SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod06120(localValue4);
         } else {
            String localValue5 = String.valueOf(localValue2);
            if (this.isPath(localValue5)) {
               this.await(
                  SkinTextureLoader.internalMethod00977(this.file(localValue5)),
                  localValue2x -> SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod06120(localValue2x),
                  localValue5
               );
            } else {
               this.await(SkinTextureLoader.internalMethod06608(localValue5), localValue3x -> {
                  if (localValue3x == null) {
                     throw new IllegalStateException("\u043d\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u0430 \u0441 \u043d\u0438\u043a\u043e\u043c " + localValue5);
                  } else {
                     SkinOverrideStore.InternalType0180 localValue4x = SkinOverrideStore.internalMethod00141(this.owner(), localValue3);
                     localValue4x.internalMethod06120(localValue3x.body().texturePath());
                     localValue4x.internalMethod03414(localValue3x.model());
                  }
               }, localValue5);
            }
         }
      }
   }

   public void cape(Object localValue1, Object localValue2) {
      String localValue3 = this.target(localValue1);
      if (localValue2 == null) {
         SkinOverrideStore.InternalType0180 localValue7 = SkinOverrideStore.internalMethod00141(this.owner(), localValue3);
         localValue7.internalMethod03225(false);
         localValue7.internalMethod05017(null);
         SkinOverrideStore.internalMethod05341(localValue3);
      } else if (Boolean.FALSE.equals(localValue2)) {
         SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod03225(true);
      } else {
         Identifier localValue4 = this.texture(localValue2);
         if (localValue4 != null) {
            SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod05017(localValue4);
         } else {
            String localValue5 = String.valueOf(localValue2);
            if (localValue5.toLowerCase(Locale.ROOT).startsWith("optifine:")) {
               String localValue6 = localValue5.substring("optifine:".length()).trim();
               this.await(
                  SkinTextureLoader.internalMethod09023(localValue6), localValue2x -> SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod05017(localValue2x), localValue5
               );
            } else if (this.isPath(localValue5)) {
               this.await(
                  SkinTextureLoader.internalMethod08540(this.file(localValue5)),
                  localValue2x -> SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod05017(localValue2x),
                  localValue5
               );
            } else {
               this.await(
                  SkinTextureLoader.internalMethod06608(localValue5),
                  localValue3x -> {
                     if (localValue3x != null && localValue3x.cape() != null) {
                        SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod05017(localValue3x.cape().texturePath());
                     } else {
                        throw new IllegalStateException(
                           "\u0443 \u0438\u0433\u0440\u043e\u043a\u0430 " + localValue5 + " \u043d\u0435\u0442 \u043f\u043b\u0430\u0449\u0430"
                        );
                     }
                  },
                  localValue5
               );
            }
         }
      }
   }

   public void elytra(Object localValue1, Object localValue2) {
      String localValue3 = this.target(localValue1);
      if (localValue2 == null) {
         SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod08337(null);
         SkinOverrideStore.internalMethod05341(localValue3);
      } else {
         Identifier localValue4 = this.texture(localValue2);
         if (localValue4 != null) {
            SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod08337(localValue4);
         } else {
            String localValue5 = String.valueOf(localValue2);
            if (this.isPath(localValue5)) {
               this.await(
                  SkinTextureLoader.internalMethod08540(this.file(localValue5)),
                  localValue2x -> SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod08337(localValue2x),
                  localValue5
               );
            } else {
               this.await(
                  SkinTextureLoader.internalMethod06608(localValue5),
                  localValue3x -> {
                     if (localValue3x != null && localValue3x.elytra() != null) {
                        SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod08337(localValue3x.elytra().texturePath());
                     } else {
                        throw new IllegalStateException(
                           "\u0443 \u0438\u0433\u0440\u043e\u043a\u0430 "
                              + localValue5
                              + " \u043d\u0435\u0442 \u0441\u0432\u043e\u0435\u0439 \u044d\u043b\u0438\u0442\u0440\u044b"
                        );
                     }
                  },
                  localValue5
               );
            }
         }
      }
   }

   public void model(Object localValue1, Object localValue2) {
      String localValue3 = this.target(localValue1);
      if (localValue2 == null) {
         SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod03414(null);
         SkinOverrideStore.internalMethod05341(localValue3);
      } else {
         String localValue4 = String.valueOf(localValue2).trim().toLowerCase(Locale.ROOT);

         PlayerSkinType localValue5 = switch (localValue4) {
            case "slim", "alex", "thin" -> PlayerSkinType.SLIM;
            case "wide", "steve", "classic", "default" -> PlayerSkinType.WIDE;
            default -> throw new IllegalArgumentException(
               "\u043c\u043e\u0434\u0435\u043b\u044c \u0431\u044b\u0432\u0430\u0435\u0442 slim \u0438\u043b\u0438 wide, \u0430 \u043d\u0435 " + localValue2
            );
         };
         SkinOverrideStore.internalMethod00141(this.owner(), localValue3).internalMethod03414(localValue5);
      }
   }

   public void reset(Object localValue1) {
      SkinOverrideStore.internalMethod03776(this.target(localValue1));
   }

   public void clear() {
      SkinOverrideStore.internalMethod02767(this.owner());
   }

   public List<String> targets() {
      return SkinOverrideStore.internalMethod02624();
   }

   public Map<String, Object> get(Object localValue1) {
      HashMap localValue2 = new HashMap();
      SkinOverrideStore.InternalType0180 localValue3 = SkinOverrideStore.internalMethod05128(this.target(localValue1));
      if (localValue3 == null) {
         return localValue2;
      } else {
         if (localValue3.internalMethod03751() != null) {
            localValue2.put("skin", localValue3.internalMethod03751().toString());
         }

         if (localValue3.internalMethod00837() != null) {
            localValue2.put("cape", localValue3.internalMethod00837().toString());
         }

         if (localValue3.internalMethod08803() != null) {
            localValue2.put("elytra", localValue3.internalMethod08803().toString());
         }

         if (localValue3.internalMethod05326() != null) {
            localValue2.put("model", localValue3.internalMethod05326().asString());
         }

         localValue2.put("hide_cape", localValue3.internalMethod04687());
         return localValue2;
      }
   }

   private String target(Object localValue1) {
      if (localValue1 == null) {
         return this.self();
      } else if (localValue1 instanceof PlayerEntity localValue4) {
         return localValue4.getGameProfile().name();
      } else if (localValue1 instanceof Entity localValue3) {
         return localValue3.getName().getString();
      } else {
         String localValue2 = String.valueOf(localValue1).trim();
         if (localValue2.isEmpty() || localValue2.equalsIgnoreCase("self") || localValue2.equalsIgnoreCase("me")) {
            return this.self();
         } else {
            return !localValue2.equals("*") && !localValue2.equalsIgnoreCase("all") ? localValue2 : "*";
         }
      }
   }

   private String self() {
      return internalField0149.player != null ? internalField0149.player.getGameProfile().name() : internalField0149.getSession().getUsername();
   }

   private Identifier texture(Object localValue1) {
      if (localValue1 instanceof Identifier localValue3) {
         return localValue3;
      } else {
         return localValue1 instanceof PyDynamicTexture localValue2 ? localValue2.identifier() : null;
      }
   }

   private boolean isPath(String localValue1) {
      String localValue2 = localValue1.toLowerCase(Locale.ROOT);
      return localValue2.startsWith("http://") || localValue2.startsWith("https://") || localValue2.contains("/") || localValue2.contains("\\") || localValue2.endsWith(".png");
   }

   private String file(String localValue1) {
      String localValue2 = localValue1.toLowerCase(Locale.ROOT);
      if (!localValue2.startsWith("http://") && !localValue2.startsWith("https://")) {
         Path localValue3 = PyAssets.resolve(localValue1);
         if (!Files.isRegularFile(localValue3)) {
            throw new IllegalArgumentException("\u0444\u0430\u0439\u043b\u0430 \u043d\u0435\u0442: " + localValue3);
         } else {
            return localValue3.toString();
         }
      } else {
         return localValue1;
      }
   }

   private <T> void await(CompletableFuture<T> localValue1, Consumer<T> localValue2, String localValue3) {
      localValue1.thenAccept(localValue2)
         .exceptionally(
            localValue1x -> {
               Throwable localValue2x = localValue1x.getCause() != null ? localValue1x.getCause() : localValue1x;
               RockstarClient.internalField0572
                  .warn(
                     "\u0421\u043a\u0438\u043d\u044b: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0432\u0437\u044f\u0442\u044c \u043e\u0431\u043b\u0438\u043a \u0438\u0437 {}: {}",
                     localValue3,
                     localValue2x.getMessage()
                  );
               return null;
            }
         );
   }

   private Object owner() {
      PythonScript localValue1 = PythonScript.internalMethod00581();
      if (localValue1 != null) {
         return localValue1;
      } else {
         return this.creator != null ? this.creator : this;
      }
   }
}
