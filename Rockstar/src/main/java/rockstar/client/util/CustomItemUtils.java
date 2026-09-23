package rockstar.client.util;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import pyrock.utility.render.ColorRGBA;

public final class CustomItemUtils implements MinecraftClientAccess {
   static final Set<String> internalField0546 = Set.of("Eternity", "Infinity", "Stinger", "Immortal", "Armortality", "Flash", "Cerber");
   private static final Map<String, String> internalField0544 = Map.ofEntries(
      Map.entry("eternity", "Eternity"),
      Map.entry("\u044d\u0442\u0435\u0440\u043d\u0438\u0442\u0438", "Eternity"),
      Map.entry("infinity", "Infinity"),
      Map.entry("\u0438\u043d\u0444\u0438\u043d\u0438\u0442\u0438", "Infinity"),
      Map.entry("stinger", "Stinger"),
      Map.entry("\u0441\u0442\u0438\u043d\u0433\u0435\u0440", "Stinger"),
      Map.entry("immortal", "Immortal"),
      Map.entry("\u0438\u043c\u043c\u043e\u0440\u0442\u0430\u043b\u0438\u0442\u0438", "Immortal"),
      Map.entry("\u0438\u043c\u043c\u043e\u0440\u0442\u0430\u043b", "Immortal"),
      Map.entry("armortality", "Armortality"),
      Map.entry("\u0430\u0440\u043c\u043e\u0440\u0442\u0430\u043b\u0438\u0442\u0438", "Armortality"),
      Map.entry("flash", "Flash"),
      Map.entry("\u0444\u043b\u0435\u0448", "Flash"),
      Map.entry("cerber", "Cerber"),
      Map.entry("\u0446\u0435\u0440\u0431\u0435\u0440", "Cerber")
   );
   private static final Map<String, String> internalField1197 = Map.ofEntries(
      Map.entry("\u0414\u0443\u0445 \u0410\u0440\u0435\u0441\u0430 \u043f\u044b\u043b\u0430\u0435\u0442", "\u0410\u0440\u0435\u0441\u0430"),
      Map.entry(
         "\u0416\u0438\u0432\u0443\u0447\u0435\u0441\u0442\u044c \u0442\u0435\u043c\u043d\u044b\u0445 \u0433\u043b\u0443\u0431\u0438\u043d",
         "\u0413\u0438\u0434\u0440\u044b"
      ),
      Map.entry(
         "\u0412\u0435\u0447\u043d\u0430\u044f \u043c\u0435\u0440\u0437\u043b\u043e\u0442\u0430 \u0441\u043a\u043e\u0432\u044b\u0432\u0430\u0435\u0442",
         "\u041c\u043e\u0440\u043e\u0437\u0430"
      ),
      Map.entry(
         "\u0428\u0451\u043f\u043e\u0442 \u0421\u0430\u0442\u0438\u0440\u0430 \u0437\u0432\u0443\u0447\u0438\u0442", "\u0421\u0430\u0442\u0438\u0440\u0430"
      ),
      Map.entry(
         "\u041c\u043e\u0449\u044c \u0422\u0438\u0442\u0430\u043d\u043e\u0432 \u043a\u0440\u0435\u043f\u043a\u0430", "\u0422\u0438\u0442\u0430\u043d\u0430"
      ),
      Map.entry(
         "\u0425\u0430\u043e\u0441 \u0438\u0441\u043a\u0430\u0436\u0430\u0435\u0442 \u0440\u0435\u0430\u043b\u044c\u043d\u043e\u0441\u0442\u044c",
         "\u0425\u0430\u043e\u0441\u0430"
      ),
      Map.entry("\u0425\u043e\u043b\u043e\u0434 \u042d\u0440\u0438\u0434\u044b \u0432\u0435\u0447\u0435\u043d", "\u042d\u0440\u0438\u0434\u0430"),
      Map.entry(
         "\u0417\u0432\u0435\u0440\u0438\u043d\u0430\u044f \u0434\u0438\u043a\u0430\u044f \u043c\u043e\u0449\u044c", "\u0411\u0435\u0441\u0442\u0438\u0438"
      ),
      Map.entry("\u0425\u0440\u0430\u043d\u0438\u0442 \u0432\u043e\u043b\u044e \u0418\u043a\u0430\u0440\u0430", "\u0418\u043a\u0430\u0440\u0430")
   );
   private static final Map<String, String> internalField1196 = Map.ofEntries(
      Map.entry("\u0412\u0438\u0445\u0440\u044c \u043d\u0435 \u0437\u043d\u0430\u0435\u0442 \u043f\u043e\u043a\u043e\u044f", "\u0412\u0438\u0445\u0440\u044f"),
      Map.entry(
         "\u041f\u043e\u0445\u0438\u0442\u0438\u0442\u0435\u043b\u044c \u043f\u0440\u0430\u0437\u0434\u043d\u0438\u043a\u0430 \u043b\u0435\u0433\u043e\u043a",
         "\u0413\u0440\u0438\u043d\u0447\u0430"
      ),
      Map.entry(
         "\u041f\u0435\u0447\u0430\u0442\u044c \u0440\u0430\u0437\u0436\u0438\u0433\u0430\u0435\u0442 \u044f\u0440\u043e\u0441\u0442\u044c",
         "\u0414\u0435\u043c\u043e\u043d\u0430"
      ),
      Map.entry(
         "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044c \u043d\u0435 \u0437\u043d\u0430\u0435\u0442 \u043f\u043e\u0449\u0430\u0434\u044b",
         "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f"
      ),
      Map.entry(
         "\u041b\u043e\u043c\u0430\u044e\u0449\u0430\u044f \u043f\u0440\u0435\u0433\u0440\u0430\u0434\u044b",
         "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f"
      ),
      Map.entry(
         "\u041d\u0435\u0441\u0451\u0442 \u0441\u0442\u0440\u043e\u0433\u0438\u0439 \u043f\u0440\u0438\u0433\u043e\u0432\u043e\u0440",
         "\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f"
      ),
      Map.entry(
         "\u041c\u0440\u0430\u043a \u0441\u0433\u0443\u0449\u0430\u0435\u0442\u0441\u044f \u0440\u044f\u0434\u043e\u043c", "\u041c\u0440\u0430\u043a\u0430"
      ),
      Map.entry(
         "\u0420\u0430\u0437\u0434\u043e\u0440 \u0436\u0430\u0436\u0434\u0435\u0442 \u0445\u0430\u043e\u0441\u0430",
         "\u0420\u0430\u0437\u0434\u043e\u0440\u0430"
      ),
      Map.entry(
         "\u0422\u0438\u0440\u0430\u043d \u043f\u043e\u0434\u0430\u0432\u043b\u044f\u0435\u0442 \u0441\u043b\u0430\u0431\u044b\u0445",
         "\u0422\u0438\u0440\u0430\u043d\u0430"
      ),
      Map.entry(
         "\u0427\u0438\u0441\u0442\u0430\u044f, \u0434\u0438\u043a\u0430\u044f \u0430\u0433\u0440\u0435\u0441\u0441\u0438\u044f",
         "\u042f\u0440\u043e\u0441\u0442\u0438"
      )
   );
   private static final Map<String, String> internalField1195 = Map.ofEntries(
      Map.entry("tal-vihrya", "\u0412\u0438\u0445\u0440\u044f"),
      Map.entry("tal-vihra", "\u0412\u0438\u0445\u0440\u044f"),
      Map.entry("tal-grincha", "\u0413\u0440\u0438\u043d\u0447\u0430"),
      Map.entry("tal-demona", "\u0414\u0435\u043c\u043e\u043d\u0430"),
      Map.entry("tal-krush", "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f"),
      Map.entry("tal-karatel", "\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f"),
      Map.entry("tal-mraka", "\u041c\u0440\u0430\u043a\u0430"),
      Map.entry("tal-razdora", "\u0420\u0430\u0437\u0434\u043e\u0440\u0430"),
      Map.entry("tal-tirana", "\u0422\u0438\u0440\u0430\u043d\u0430"),
      Map.entry("tal-yarosti", "\u042f\u0440\u043e\u0441\u0442\u0438"),
      Map.entry("tal-jarosti", "\u042f\u0440\u043e\u0441\u0442\u0438")
   );
   private static final Map<String, String> internalField1198 = Map.ofEntries(
      Map.entry("sph-aresa", "\u0410\u0440\u0435\u0441\u0430"),
      Map.entry("sph-gidra", "\u0413\u0438\u0434\u0440\u044b"),
      Map.entry("sph-moroza", "\u041c\u043e\u0440\u043e\u0437\u0430"),
      Map.entry("sph-satira", "\u0421\u0430\u0442\u0438\u0440\u0430"),
      Map.entry("sph-titana", "\u0422\u0438\u0442\u0430\u043d\u0430"),
      Map.entry("sph-haosa", "\u0425\u0430\u043e\u0441\u0430"),
      Map.entry("sph-erida", "\u042d\u0440\u0438\u0434\u0430"),
      Map.entry("sph-bestia", "\u0411\u0435\u0441\u0442\u0438\u0438"),
      Map.entry("sph-ikara", "\u0418\u043a\u0430\u0440\u0430")
   );
   static final Map<String, String> internalField1560 = Map.ofEntries(
      Map.entry("EXPLOSIVE_TRAP", "\u0412\u0437\u0440\u044b\u0432\u043d\u0430\u044f \u0442\u0440\u0430\u043f\u043a\u0430"),
      Map.entry("explosivetrap", "\u0412\u0437\u0440\u044b\u0432\u043d\u0430\u044f \u0442\u0440\u0430\u043f\u043a\u0430"),
      Map.entry("STUN_STAR", "\u0421\u0442\u0430\u043d"),
      Map.entry("ALTERNATIVE_TRAP", "\u0422\u0440\u0430\u043f\u043a\u0430"),
      Map.entry("ExplosiveStuff", "\u0412\u0437\u0440\u044b\u0432\u043d\u0430\u044f \u0448\u0442\u0443\u0447\u043a\u0430"),
      Map.entry("SnowBall", "\u0421\u043d\u0435\u0436\u043e\u043a"),
      Map.entry("SunHelmet", "\u0428\u043b\u0435\u043c \u0421\u043e\u043b\u043d\u0446\u0430"),
      Map.entry("desorientation", "\u0414\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f"),
      Map.entry("sheerdust", "\u042f\u0432\u043d\u0430\u044f \u043f\u044b\u043b\u044c"),
      Map.entry("godsaura", "\u0410\u0443\u0440\u0430 \u0431\u043e\u0433\u0430"),
      Map.entry("effect-item-diz", "\u0414\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f"),
      Map.entry("effect-item-dust", "\u042f\u0432\u043d\u0430\u044f \u043f\u044b\u043b\u044c"),
      Map.entry("effect-item-god", "\u0410\u0443\u0440\u0430 \u0431\u043e\u0433\u0430"),
      Map.entry("effect-item-trap", "\u0422\u0440\u0430\u043f\u043a\u0430"),
      Map.entry("effect-item-explosivetrap", "\u0412\u0437\u0440\u044b\u0432\u043d\u0430\u044f \u0442\u0440\u0430\u043f\u043a\u0430"),
      Map.entry("effect-item-snowball", "\u0421\u043d\u0435\u0436\u043e\u043a"),
      Map.entry("effect-item-stun", "\u0421\u0442\u0430\u043d"),
      Map.entry("attribute-item-tkryshitela", "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f"),
      Map.entry("attribute-item-tkaratela", "\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f"),
      Map.entry("attribute-item-tvihra", "\u0412\u0438\u0445\u0440\u044f"),
      Map.entry("attribute-item-ttirana", "\u0422\u0438\u0440\u0430\u043d\u0430"),
      Map.entry("attribute-item-tgrincha", "\u0413\u0440\u0438\u043d\u0447\u0430"),
      Map.entry("attribute-item-tdemona", "\u0414\u0435\u043c\u043e\u043d\u0430"),
      Map.entry("attribute-item-tmraka", "\u041c\u0440\u0430\u043a\u0430"),
      Map.entry("attribute-item-trazdora", "\u0420\u0430\u0437\u0434\u043e\u0440\u0430"),
      Map.entry("attribute-item-tyarosti", "\u042f\u0440\u043e\u0441\u0442\u0438"),
      Map.entry("attribute-item-saresa", "\u0410\u0440\u0435\u0441\u0430"),
      Map.entry("attribute-item-shydra", "\u0413\u0438\u0434\u0440\u044b"),
      Map.entry("attribute-item-smoroza", "\u041c\u043e\u0440\u043e\u0437\u0430"),
      Map.entry("attribute-item-ssatira", "\u0421\u0430\u0442\u0438\u0440\u0430"),
      Map.entry("attribute-item-stitana", "\u0422\u0438\u0442\u0430\u043d\u0430"),
      Map.entry("attribute-item-shaosa", "\u0425\u0430\u043e\u0441\u0430"),
      Map.entry("attribute-item-serida", "\u042d\u0440\u0438\u0434\u0430"),
      Map.entry("attribute-item-sbestii", "\u0411\u0435\u0441\u0442\u0438\u0438"),
      Map.entry("attribute-item-sikara", "\u0418\u043a\u0430\u0440\u0430"),
      Map.entry("attribute-item-safina", "\u0410\u0444\u0438\u043d\u044b"),
      Map.entry("trap", "\u0422\u0440\u0430\u043f\u043a\u0430"),
      Map.entry("plast", "\u041f\u043b\u0430\u0441\u0442"),
      Map.entry("disorientation", "\u0414\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f"),
      Map.entry("gods", "\u0411\u043e\u0436\u044c\u044f \u0430\u0443\u0440\u0430"),
      Map.entry("tornado", "\u041e\u0433\u043d\u0435\u043d\u043d\u044b\u0439 \u0441\u043c\u0435\u0440\u0447"),
      Map.entry("dust", "\u042f\u0432\u043d\u0430\u044f \u043f\u044b\u043b\u044c"),
      Map.entry("snowball", "\u0421\u043d\u0435\u0436\u043e\u043a"),
      Map.entry("krush", "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f"),
      Map.entry("karatelya", "\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f"),
      Map.entry("dedala", "\u0414\u0435\u0434\u0430\u043b\u0430"),
      Map.entry("grani", "\u0413\u0440\u0430\u043d\u0438"),
      Map.entry("garmonii", "\u0413\u0430\u0440\u043c\u043e\u043d\u0438\u0438"),
      Map.entry("ehidna", "\u0415\u0445\u0438\u0434\u043d\u044b"),
      Map.entry("tritona", "\u0422\u0440\u0438\u0442\u043e\u043d\u0430"),
      Map.entry("fenixa", "\u0424\u0435\u043d\u0438\u043a\u0441\u0430"),
      Map.entry("yarosti", "\u042f\u0440\u043e\u0441\u0442\u0438"),
      Map.entry("andromeda", "\u0410\u043d\u0434\u0440\u043e\u043c\u0435\u0434\u044b"),
      Map.entry("titana", "\u0422\u0438\u0442\u0430\u043d\u0430"),
      Map.entry("apollona", "\u0410\u043f\u043e\u043b\u043b\u043e\u043d\u0430"),
      Map.entry("astreya", "\u0410\u0441\u0442\u0440\u0435\u044f"),
      Map.entry("osirisa", "\u041e\u0441\u0438\u0440\u0438\u0441\u0430"),
      Map.entry("satira", "\u0421\u0430\u0442\u0438\u0440\u0430"),
      Map.entry("pandori", "\u041f\u0430\u043d\u0434\u043e\u0440\u044b"),
      Map.entry("himeri", "\u0425\u0438\u043c\u0435\u0440\u044b")
   );
   static final Map<String, ColorRGBA> internalField1559 = Map.ofEntries(
      Map.entry("ExplosiveStuff", ColorRGBA.fromHex("FF0000")),
      Map.entry("STUN_STAR", ColorRGBA.fromHex("E4E4E4")),
      Map.entry("EXPLOSIVE_TRAP", ColorRGBA.fromHex("7F83A5")),
      Map.entry("ALTERNATIVE_TRAP", ColorRGBA.fromHex("CE7FDA")),
      Map.entry("Eternity", ColorRGBA.fromHex("CD0078")),
      Map.entry("Infinity", ColorRGBA.fromHex("00B412")),
      Map.entry("Stinger", ColorRGBA.fromHex("FC0F00")),
      Map.entry("Immortal", ColorRGBA.fromHex("7700DF")),
      Map.entry("Armortality", ColorRGBA.fromHex("3E4E73")),
      Map.entry("Flash", ColorRGBA.fromHex("0080FA")),
      Map.entry("Cerber", ColorRGBA.fromHex("0080FA")),
      Map.entry("SunHelmet", ColorRGBA.fromHex("FCC700"))
   );
   public static final Map<Item, String> internalField0543 = Map.ofEntries(
      Map.entry(Items.NETHERITE_SCRAP, "\u0422\u0440\u0430\u043f\u043a\u0430"),
      Map.entry(Items.ENDER_EYE, "\u0414\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f"),
      Map.entry(Items.FIRE_CHARGE, "\u0421\u043c\u0435\u0440\u0447"),
      Map.entry(Items.DRIED_KELP, "\u041f\u043b\u0430\u0441\u0442"),
      Map.entry(Items.PHANTOM_MEMBRANE, "\u0410\u0443\u0440\u0430 \u0431\u043e\u0433\u0430"),
      Map.entry(Items.SUGAR, "\u041f\u044b\u043b\u044c"),
      Map.entry(Items.POPPED_CHORUS_FRUIT, "\u0422\u0440\u0430\u043f\u043a\u0430"),
      Map.entry(Items.NETHER_STAR, "\u0421\u0442\u0430\u043d"),
      Map.entry(Items.SNOWBALL, "\u0421\u043d\u0435\u0436\u043e\u043a"),
      Map.entry(Items.PRISMARINE_SHARD, "\u0412\u0437\u0440\u044b\u0432\u043d\u0430\u044f \u0442\u0440\u0430\u043f\u043a\u0430"),
      Map.entry(Items.FIREWORK_STAR, "\u0413\u0443\u043b\u044c")
   );
   static final ColorRGBA internalField0777 = ColorRGBA.fromHex("A9C7D8");
   static final ColorRGBA internalField0776 = ColorRGBA.fromHex("0080FA");
   static final ColorRGBA internalField1311 = ColorRGBA.fromHex("CD0078");
   static final ColorRGBA internalField1312 = ColorRGBA.fromHex("E700FA");
   static final ColorRGBA internalField1309 = ColorRGBA.fromHex("F397FA");

   public static CustomItemUtils.InternalType0254 internalMethod03238(ItemStack localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         NbtCompound localValue1 = ItemNbtUtils.internalMethod05003(localValue0);
         if (localValue1 != null) {
            CustomItemUtils.InternalType0254 localValue2 = internalMethod04079(localValue1);
            if (localValue2 != null) {
               return localValue2;
            }

            localValue2 = internalMethod08794(localValue0, localValue1);
            if (localValue2 != null) {
               return localValue2;
            }

            localValue2 = internalMethod07566(localValue0, localValue1);
            if (localValue2 != null) {
               return localValue2;
            }

            localValue2 = internalMethod01841(localValue0, localValue1);
            if (localValue2 != null) {
               return localValue2;
            }

            localValue2 = internalMethod08330(localValue0, localValue1);
            if (localValue2 != null) {
               return localValue2;
            }
         }

         return internalMethod08523(localValue0);
      } else {
         return null;
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod01841(ItemStack localValue0, NbtCompound localValue1) {
      if (localValue1.contains("sixtrap")) {
         String localValue5 = localValue1.getString("sixtrap").orElse("");
         String localValue8 = internalField1560.getOrDefault(localValue5, localValue5);
         return new CustomItemUtils.InternalType0254(
            CustomItemUtils.InternalType0253.internalField0242, localValue8, CustomItemUtils.InternalType0440.internalField1336
         );
      } else if (localValue1.contains("sixitem")) {
         String localValue4 = localValue1.getString("sixitem").orElse("");
         String localValue7 = internalField1560.getOrDefault(localValue4, localValue4);
         return new CustomItemUtils.InternalType0254(
            CustomItemUtils.InternalType0253.internalField0242, localValue7, CustomItemUtils.InternalType0440.internalField1336
         );
      } else {
         if (localValue1.contains("bettertalismans-talisman")) {
            String localValue2 = localValue1.getString("bettertalismans-talisman").orElse("");
            if (localValue0.getItem() == Items.PLAYER_HEAD) {
               String localValue6 = internalField1560.getOrDefault(localValue2, localValue2);
               return new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField0243, localValue6, CustomItemUtils.InternalType0440.internalField1336
               );
            }

            if (localValue0.getItem() == Items.TOTEM_OF_UNDYING) {
               String localValue3 = internalField1560.getOrDefault(localValue2, localValue2);
               return new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField1070, localValue3, CustomItemUtils.InternalType0440.internalField1336
               );
            }
         }

         return null;
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod07566(ItemStack localValue0, NbtCompound localValue1) {
      if (localValue1.contains("Donat Item")) {
         return new CustomItemUtils.InternalType0254(
            CustomItemUtils.InternalType0253.internalField1071, "SunHelmet", CustomItemUtils.InternalType0440.internalField0835
         );
      } else if (localValue1.contains("snowball")) {
         return new CustomItemUtils.InternalType0254(
            CustomItemUtils.InternalType0253.internalField0242, "SnowBall", CustomItemUtils.InternalType0440.internalField0835
         );
      } else if (localValue1.contains("explosive-thing")) {
         return new CustomItemUtils.InternalType0254(
            CustomItemUtils.InternalType0253.internalField0242, "ExplosiveStuff", CustomItemUtils.InternalType0440.internalField0835
         );
      } else {
         String localValue2 = internalMethod07095(localValue1, "PublicBukkitValues", "litetraps:item");
         if (localValue2 != null) {
            String localValue9 = switch (localValue2) {
               case "stun" -> "STUN_STAR";
               case "default" -> "ALTERNATIVE_TRAP";
               case "custom" -> "EXPLOSIVE_TRAP";
               default -> localValue2;
            };
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField0242, localValue9, CustomItemUtils.InternalType0440.internalField0835
            );
         } else {
            String localValue3 = internalMethod07095(localValue1, "PublicBukkitValues", "minecraft:talisman_type");
            if (localValue3 != null) {
               CustomItemUtils.InternalType0441 localValue4 = CustomItemUtils.InternalType0441.internalMethod02889(localValue3);
               HashMap localValue5 = new HashMap();
               NbtCompound localValue6 = localValue1.getCompound("PublicBukkitValues").orElseGet(net.minecraft.nbt.NbtCompound::new);
               if (localValue6.contains("minecraft:talisman_effect_speed")) {
                  localValue5.put("hms-speed", localValue6.getInt("minecraft:talisman_effect_speed").orElse(0));
               }

               if (localValue6.contains("minecraft:talisman_effect_damage")) {
                  localValue5.put("hms-damage", localValue6.getInt("minecraft:talisman_effect_damage").orElse(0));
               }

               if (localValue6.contains("minecraft:talisman_effect_armor")) {
                  localValue5.put("hms-armor", localValue6.getInt("minecraft:talisman_effect_armor").orElse(0));
               }

               CustomItemUtils.InternalType0253 localValue7 = localValue0.getItem() == Items.TOTEM_OF_UNDYING
                  ? CustomItemUtils.InternalType0253.internalField1070
                  : CustomItemUtils.InternalType0253.internalField0243;
               String localValue8 = internalMethod00944(localValue0);
               return new CustomItemUtils.InternalType0254(localValue7, localValue8, localValue4, CustomItemUtils.InternalType0440.internalField0835, localValue5);
            } else {
               return null;
            }
         }
      }
   }

   private static String internalMethod00944(ItemStack localValue0) {
      String localValue1 = ItemNbtUtils.internalMethod02181(localValue0).toLowerCase();
      Map localValue2 = Map.ofEntries(
         Map.entry("eternity", "Eternity"),
         Map.entry("\u1d07\u1d1b\u1d07\u0280\u0274\u026a\u1d1b\u028f", "Eternity"),
         Map.entry("\u044d\u0442\u0435\u0440\u043d\u0438\u0442\u0438", "Eternity"),
         Map.entry("infinity", "Infinity"),
         Map.entry("\u026a\u0274\ua730\u026a\u0274\u026a\u1d1b\u028f", "Infinity"),
         Map.entry("\u026a\u0274\u0493\u026a\u0274\u026a\u1d1b\u028f", "Infinity"),
         Map.entry("\u0438\u043d\u0444\u0438\u043d\u0438\u0442\u0438", "Infinity"),
         Map.entry("stinger", "Stinger"),
         Map.entry("\ua731\u1d1b\u026a\u0274\u0262\u1d07\u0280", "Stinger"),
         Map.entry("s\u1d1b\u026a\u0274\u0262\u1d07\u0280", "Stinger"),
         Map.entry("\u0441\u0442\u0438\u043d\u0433\u0435\u0440", "Stinger"),
         Map.entry("immortal", "Immortal"),
         Map.entry("\u026a\u1d0d\u1d0d\u1d0f\u0280\u1d1b\u1d00\u029f", "Immortal"),
         Map.entry("\u026a\u1d0d\u1d0d\u1d0f\u0280\u1d1b\u1d00\u029f\u026a\u1d1b\u028f", "Immortal"),
         Map.entry("\u0438\u043c\u043c\u043e\u0440\u0442\u0430\u043b", "Immortal"),
         Map.entry("armortality", "Armortality"),
         Map.entry("\u1d00\u0280\u1d0d\u1d0f\u0280\u1d1b\u1d00\u029f\u026a\u1d1b\u028f", "Armortality"),
         Map.entry("\u0430\u0440\u043c\u043e\u0440\u0442\u0430\u043b\u0438\u0442\u0438", "Armortality"),
         Map.entry("flash", "Flash"),
         Map.entry("\ua730\u029f\u1d00\ua731\u029c", "Flash"),
         Map.entry("\u0493\u029f\u1d00s\u029c", "Flash"),
         Map.entry("\u0444\u043b\u0435\u0448", "Flash"),
         Map.entry("cerber", "Cerber"),
         Map.entry("\u1d04\u1d07\u0280\u0299\u1d07\u0280", "Cerber"),
         Map.entry("\u0446\u0435\u0440\u0431\u0435\u0440", "Cerber")
      );

      for (Entry localValue4 : (Iterable<Entry>)(Iterable<?>)localValue2.entrySet()) {
         if (localValue1.contains((CharSequence)localValue4.getKey())) {
            return (String)localValue4.getValue();
         }
      }

      return null;
   }

   public static String internalMethod00580(String localValue0) {
      if (localValue0 == null) {
         return "";
      } else {
         String localValue1 = localValue0.toLowerCase(Locale.ROOT);
         if (localValue1.startsWith("sphere-")) {
            return "sph-" + localValue1.substring("sphere-".length());
         } else {
            return localValue1.startsWith("talisman-") ? "tal-" + localValue1.substring("talisman-".length()) : localValue1;
         }
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod08523(ItemStack localValue0) {
      boolean localValue1 = localValue0.getItem() == Items.TOTEM_OF_UNDYING;
      boolean localValue2 = localValue0.getItem() == Items.PLAYER_HEAD;
      if (!localValue1 && !localValue2) {
         return null;
      } else {
         List localValue3 = internalMethod06792(localValue0);
         if (localValue3.isEmpty()) {
            return null;
         } else {
            CustomItemUtils.InternalType0253 localValue4 = localValue1
               ? CustomItemUtils.InternalType0253.internalField1070
               : CustomItemUtils.InternalType0253.internalField0243;
            Map localValue5 = localValue1 ? internalField1196 : internalField1197;

            for (String localValue7 : (Iterable<String>)(Iterable<?>)localValue3) {
               for (Entry localValue9 : (Iterable<Entry>)(Iterable<?>)localValue5.entrySet()) {
                  if (localValue7.contains((CharSequence)localValue9.getKey())) {
                     return new CustomItemUtils.InternalType0254(localValue4, (String)localValue9.getValue(), null, CustomItemUtils.InternalType0440.internalField1338);
                  }
               }
            }

            return !localValue0.hasEnchantments()
               ? null
               : new CustomItemUtils.InternalType0254(localValue4, null, null, CustomItemUtils.InternalType0440.internalField1338);
         }
      }
   }

   private static List<String> internalMethod06792(ItemStack localValue0) {
      LoreComponent localValue1 = (LoreComponent)localValue0.get(DataComponentTypes.LORE);
      if (localValue1 == null) {
         return List.of();
      } else {
         ArrayList localValue2 = new ArrayList(localValue1.lines().size());

         for (Text localValue4 : localValue1.lines()) {
            localValue2.add(localValue4.getString());
         }

         return localValue2;
      }
   }

   public static CustomItemUtils.InternalType0254 internalMethod07586(ItemStack localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         CustomItemUtils.InternalType0254 localValue1 = internalMethod03238(localValue0);
         if (localValue1 != null) {
            return localValue1;
         } else {
            CustomItemUtils.InternalType0254 localValue2 = internalMethod07814(localValue0);
            if (localValue2 != null) {
               return localValue2;
            } else if (localValue0.getItem() == Items.TOTEM_OF_UNDYING && localValue0.hasEnchantments()) {
               return new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField1070, null, null, CustomItemUtils.InternalType0440.internalField1338
               );
            } else {
               return localValue0.getItem() == Items.PLAYER_HEAD && localValue0.hasEnchantments()
                  ? new CustomItemUtils.InternalType0254(
                     CustomItemUtils.InternalType0253.internalField0243, null, null, CustomItemUtils.InternalType0440.internalField1338
                  )
                  : null;
            }
         }
      } else {
         return null;
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod07814(ItemStack localValue0) {
      try {
         NbtCompound localValue2 = LegacyItemTypes.toNbt(localValue0, internalField0149.world.getRegistryManager());

         boolean localValue3 = localValue0.getItem() == Items.PLAYER_HEAD;
         boolean localValue4 = localValue0.getItem() == Items.TOTEM_OF_UNDYING;
         CustomItemUtils.InternalType0254 localValue5 = internalMethod06037(localValue2, localValue3, localValue4);
         if (localValue5 != null) {
            return localValue5;
         }

         if (localValue2.contains("components")) {
            NbtCompound localValue6 = localValue2.getCompound("components").orElseGet(net.minecraft.nbt.NbtCompound::new);
            if (localValue6.contains("minecraft:custom_data")) {
               NbtCompound localValue7 = localValue6.getCompound("minecraft:custom_data").orElseGet(net.minecraft.nbt.NbtCompound::new);
               localValue5 = internalMethod06037(localValue7, localValue3, localValue4);
               if (localValue5 != null) {
                  return localValue5;
               }

               if (localValue3 && localValue7.contains("display")) {
                  String localValue8 = internalMethod00081(localValue7);
                  if (localValue8 != null) {
                     return new CustomItemUtils.InternalType0254(
                        CustomItemUtils.InternalType0253.internalField0243, localValue8, null, CustomItemUtils.InternalType0440.internalField1338
                     );
                  }
               }
            }
         }

         if (localValue3 && localValue0.hasEnchantments()) {
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField0243, null, null, CustomItemUtils.InternalType0440.internalField1338
            );
         }
      } catch (Exception localValue9) {
      }

      return null;
   }

   private static CustomItemUtils.InternalType0254 internalMethod06037(NbtCompound localValue0, boolean localValue1, boolean localValue2) {
      if (!localValue0.contains("itemServiceId")) {
         return null;
      } else {
         NbtCompound localValue3 = localValue0.getCompound("itemServiceId").orElseGet(net.minecraft.nbt.NbtCompound::new);
         if (!localValue3.contains("name")) {
            return null;
         } else {
            String localValue4 = localValue3.getString("name").orElse("").toLowerCase();
            String localValue5 = internalMethod01655(localValue4);
            CustomItemUtils.InternalType0441 localValue6 = internalMethod06575(localValue4);
            boolean localValue7 = localValue4.startsWith("\u0442\u0430\u043b\u0438\u0441\u043c\u0430\u043d");
            if (localValue2 || localValue7) {
               return new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField1070, localValue5, localValue6, CustomItemUtils.InternalType0440.internalField0834
               );
            } else {
               return localValue1
                  ? new CustomItemUtils.InternalType0254(
                     CustomItemUtils.InternalType0253.internalField0243, localValue5, localValue6, CustomItemUtils.InternalType0440.internalField0834
                  )
                  : null;
            }
         }
      }
   }

   private static String internalMethod01655(String localValue0) {
      for (Entry localValue2 : internalField0544.entrySet()) {
         if (localValue0.contains((CharSequence)localValue2.getKey())) {
            return (String)localValue2.getValue();
         }
      }

      return null;
   }

   private static CustomItemUtils.InternalType0441 internalMethod06575(String localValue0) {
      if (localValue0.contains("\u043c\u0438\u0444\u0438\u0447\u0435\u0441\u043a") || localValue0.contains("mythic")) {
         return CustomItemUtils.InternalType0441.internalField1341;
      } else if (localValue0.contains("\u043b\u0435\u0433\u0435\u043d\u0434\u0430\u0440\u043d") || localValue0.contains("legendary")) {
         return CustomItemUtils.InternalType0441.internalField1339;
      } else if (!localValue0.contains("\u044d\u043f\u0438\u0447\u0435\u0441\u043a") && !localValue0.contains("epic")) {
         if (!localValue0.contains("\u043e\u0431\u044b\u0447\u043d") && !localValue0.contains("normal")) {
            for (String localValue2 : internalField0546) {
               if (localValue0.contains(localValue2.toLowerCase())) {
                  return CustomItemUtils.InternalType0441.internalField1340;
               }
            }

            return null;
         } else {
            return CustomItemUtils.InternalType0441.internalField0837;
         }
      } else {
         return CustomItemUtils.InternalType0441.internalField0836;
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod04079(NbtCompound localValue0) {
      String localValue1 = internalMethod07095(localValue0, "PublicBukkitValues", "spookyitems:spooky-item");
      if (localValue1 == null) {
         localValue1 = internalMethod01119(localValue0, "spooky-item");
      }

      if (localValue1 == null) {
         return null;
      } else {
         CustomItemUtils.InternalType0253 localValue2 = internalMethod07080(localValue1);
         return new CustomItemUtils.InternalType0254(localValue2, localValue1, CustomItemUtils.InternalType0440.internalField1335);
      }
   }

   private static CustomItemUtils.InternalType0253 internalMethod07080(String localValue0) {
      if (localValue0.startsWith("effect-item-") || localValue0.startsWith("schematic-item-")) {
         return CustomItemUtils.InternalType0253.internalField0242;
      } else if (localValue0.startsWith("attribute-item-t")) {
         return CustomItemUtils.InternalType0253.internalField1070;
      } else {
         return localValue0.startsWith("attribute-item-s")
            ? CustomItemUtils.InternalType0253.internalField0243
            : CustomItemUtils.InternalType0253.internalField1069;
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod08794(ItemStack localValue0, NbtCompound localValue1) {
      if (localValue1.contains("pyrotechnic-item")) {
         String localValue2 = localValue1.getCompound("pyrotechnic-item").orElseGet(net.minecraft.nbt.NbtCompound::new).getString("name").orElse("");
         if (!localValue2.isEmpty()) {
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField0242, localValue2, CustomItemUtils.InternalType0440.internalField0834
            );
         }
      }

      if (localValue1.contains("kringeItems")) {
         String localValue10 = localValue1.getCompound("kringeItems").orElseGet(net.minecraft.nbt.NbtCompound::new).getString("type").orElse("");
         if (!localValue10.isEmpty()) {
            if (localValue10.equals("SunHelmet")) {
               return new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField1071, localValue10, CustomItemUtils.InternalType0440.internalField0834
               );
            }

            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField0242, localValue10, CustomItemUtils.InternalType0440.internalField0834
            );
         }
      }

      if (localValue1.contains("kringeEffect")) {
         String localValue11 = localValue1.getCompound("kringeEffect").orElseGet(net.minecraft.nbt.NbtCompound::new).getString("type").orElse("");
         if (!localValue11.isEmpty()) {
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField1071, localValue11, CustomItemUtils.InternalType0440.internalField0834
            );
         }
      }

      if (localValue1.contains("sphereEffect")) {
         NbtCompound localValue13 = localValue1.getCompound("sphereEffect").orElseGet(net.minecraft.nbt.NbtCompound::new);
         String localValue14 = localValue13.getString("name").orElse("");
         String localValue15 = localValue13.getString("rank").orElse("");
         boolean localValue16 = localValue13.getBoolean("isMascot").orElse(false);
         String localValue17 = localValue13.getString("effects").orElse("");
         HashMap localValue18 = new HashMap();
         if (localValue17 != null && !localValue17.isEmpty()) {
            internalMethod01419(localValue17, localValue18);
         }

         CustomItemUtils.InternalType0441 localValue8 = CustomItemUtils.InternalType0441.internalMethod02889(localValue15);
         CustomItemUtils.InternalType0253 localValue9;
         if (localValue0.getItem() == Items.TOTEM_OF_UNDYING) {
            localValue9 = CustomItemUtils.InternalType0253.internalField1070;
         } else if (localValue0.getItem() == Items.PLAYER_HEAD) {
            localValue9 = CustomItemUtils.InternalType0253.internalField0243;
         } else {
            localValue9 = localValue16 ? CustomItemUtils.InternalType0253.internalField1070 : CustomItemUtils.InternalType0253.internalField0243;
         }

         return new CustomItemUtils.InternalType0254(localValue9, localValue14.isEmpty() ? null : localValue14, localValue8, CustomItemUtils.InternalType0440.internalField0834, localValue18);
      } else {
         if (localValue1.contains("itemServiceId")) {
            NbtCompound localValue12 = localValue1.getCompound("itemServiceId").orElseGet(net.minecraft.nbt.NbtCompound::new);
            String localValue3 = localValue12.getString("name").orElse("");
            if (!localValue3.isEmpty()) {
               String localValue4 = localValue3.toLowerCase();
               String localValue5 = internalMethod01655(localValue4);
               CustomItemUtils.InternalType0441 localValue6 = localValue5 != null ? CustomItemUtils.InternalType0441.internalField1340 : internalMethod06575(localValue4);
               CustomItemUtils.InternalType0253 localValue7;
               if (localValue0.getItem() == Items.TOTEM_OF_UNDYING) {
                  localValue7 = CustomItemUtils.InternalType0253.internalField1070;
               } else if (localValue0.getItem() == Items.PLAYER_HEAD) {
                  localValue7 = CustomItemUtils.InternalType0253.internalField0243;
               } else {
                  localValue7 = CustomItemUtils.InternalType0253.internalField1069;
               }

               return new CustomItemUtils.InternalType0254(localValue7, localValue5, localValue6, CustomItemUtils.InternalType0440.internalField0834);
            }
         }

         return null;
      }
   }

   private static CustomItemUtils.InternalType0254 internalMethod08330(ItemStack localValue0, NbtCompound localValue1) {
      String localValue2 = internalMethod07095(localValue1, "PublicBukkitValues", "minecraft:don-item");
      if (localValue2 == null) {
         localValue2 = internalMethod01119(localValue1, "don-item");
      }

      if (localValue2 != null) {
         String localValue6 = internalMethod00580(localValue2);
         if (localValue6.startsWith("tal-")) {
            String localValue8 = internalField1195.get(localValue6);
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField1070, localValue8, null, CustomItemUtils.InternalType0440.internalField1338
            );
         } else if (localValue6.startsWith("sph-")) {
            String localValue7 = internalField1198.get(localValue6);
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField0243, localValue7, null, CustomItemUtils.InternalType0440.internalField1338
            );
         } else if (localValue0.getItem() == Items.PLAYER_HEAD) {
            String localValue4 = internalMethod00081(localValue1);
            if (localValue4 == null) {
               localValue4 = internalMethod07789(localValue0);
            }

            return localValue4 != null
               ? new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField0243, localValue4, null, CustomItemUtils.InternalType0440.internalField1338
               )
               : new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField0243, null, null, CustomItemUtils.InternalType0440.internalField1338
               );
         } else {
            return new CustomItemUtils.InternalType0254(
               CustomItemUtils.InternalType0253.internalField0242, localValue2, CustomItemUtils.InternalType0440.internalField1338
            );
         }
      } else if (localValue0.getItem() == Items.TOTEM_OF_UNDYING && localValue0.hasEnchantments()) {
         String localValue5 = internalMethod05574(localValue1);
         if (localValue5 == null) {
            localValue5 = internalMethod08469(localValue0);
         }

         return new CustomItemUtils.InternalType0254(
            CustomItemUtils.InternalType0253.internalField1070, localValue5, null, CustomItemUtils.InternalType0440.internalField1338
         );
      } else {
         if (localValue0.getItem() == Items.PLAYER_HEAD) {
            String localValue3 = internalMethod00081(localValue1);
            if (localValue3 == null) {
               localValue3 = internalMethod07789(localValue0);
            }

            if (localValue3 != null) {
               return new CustomItemUtils.InternalType0254(
                  CustomItemUtils.InternalType0253.internalField0243, localValue3, null, CustomItemUtils.InternalType0440.internalField1338
               );
            }
         }

         return null;
      }
   }

   private static String internalMethod07789(ItemStack localValue0) {
      try {
         NbtCompound localValue2 = LegacyItemTypes.toNbt(localValue0, internalField0149.world.getRegistryManager());

         if (!localValue2.contains("components")) {
            return null;
         }

         NbtCompound localValue3 = localValue2.getCompound("components").orElseGet(net.minecraft.nbt.NbtCompound::new);
         if (!localValue3.contains("minecraft:lore")) {
            return null;
         }

         NbtList localValue4 = localValue3.getList("minecraft:lore").orElseGet(net.minecraft.nbt.NbtList::new);
         if (localValue4.isEmpty()) {
            return null;
         }

         String localValue5 = localValue4.getString(0).orElse("");

         for (Entry localValue7 : internalField1197.entrySet()) {
            if (localValue5.contains((CharSequence)localValue7.getKey())) {
               return (String)localValue7.getValue();
            }
         }
      } catch (Exception localValue8) {
      }

      return null;
   }

   private static String internalMethod08469(ItemStack localValue0) {
      try {
         NbtCompound localValue2 = LegacyItemTypes.toNbt(localValue0, internalField0149.world.getRegistryManager());

         if (!localValue2.contains("components")) {
            return null;
         }

         NbtCompound localValue3 = localValue2.getCompound("components").orElseGet(net.minecraft.nbt.NbtCompound::new);
         if (!localValue3.contains("minecraft:lore")) {
            return null;
         }

         NbtList localValue4 = localValue3.getList("minecraft:lore").orElseGet(net.minecraft.nbt.NbtList::new);
         if (localValue4.isEmpty()) {
            return null;
         }

         String localValue5 = localValue4.getString(0).orElse("");

         for (Entry localValue7 : internalField1196.entrySet()) {
            if (localValue5.contains((CharSequence)localValue7.getKey())) {
               return (String)localValue7.getValue();
            }
         }
      } catch (Exception localValue8) {
      }

      return null;
   }

   @Nullable
   public static String internalMethod05508(ItemStack localValue0) {
      NbtCompound localValue1 = ItemNbtUtils.internalMethod05003(localValue0);
      if (localValue1 == null) {
         return null;
      } else {
         if (localValue1.contains("PublicBukkitValues")) {
            NbtCompound localValue2 = localValue1.getCompound("PublicBukkitValues").orElseGet(net.minecraft.nbt.NbtCompound::new);
            if (localValue2.contains("minecraft:don-item")) {
               return localValue2.getString("minecraft:don-item").orElse("");
            }
         }

         if (localValue1.contains("minecraft:don-item")) {
            return localValue1.getString("minecraft:don-item").orElse("");
         } else {
            return localValue1.contains("don-item") ? localValue1.getString("don-item").orElse("") : null;
         }
      }
   }

   private static String internalMethod00081(NbtCompound localValue0) {
      if (localValue0.contains("display")) {
         NbtCompound localValue1 = localValue0.getCompound("display").orElseGet(net.minecraft.nbt.NbtCompound::new);
         if (localValue1.contains("Lore")) {
            NbtList localValue2 = localValue1.getList("Lore").orElseGet(net.minecraft.nbt.NbtList::new);
            if (!localValue2.isEmpty()) {
               String localValue3 = localValue2.getString(0).orElse("");

               for (Entry localValue5 : internalField1197.entrySet()) {
                  if (localValue3.contains((CharSequence)localValue5.getKey())) {
                     return (String)localValue5.getValue();
                  }
               }
            }
         }
      }

      return null;
   }

   private static String internalMethod05574(NbtCompound localValue0) {
      if (!localValue0.contains("display")) {
         return null;
      } else {
         NbtCompound localValue1 = localValue0.getCompound("display").orElseGet(net.minecraft.nbt.NbtCompound::new);
         if (!localValue1.contains("Lore")) {
            return null;
         } else {
            NbtList localValue2 = localValue1.getList("Lore").orElseGet(net.minecraft.nbt.NbtList::new);
            if (localValue2.isEmpty()) {
               return null;
            } else {
            String localValue3 = localValue2.getString(0).orElse("");

               for (Entry localValue5 : internalField1196.entrySet()) {
                  if (localValue3.contains((CharSequence)localValue5.getKey())) {
                     return (String)localValue5.getValue();
                  }
               }

               return null;
            }
         }
      }
   }

   private static void internalMethod01419(String localValue0, Map<String, Integer> localValue1) {
      String[] localValue2 = new String[]{"hms-speed", "hms-damage", "hms-armor"};

      for (String localValue6 : localValue2) {
         String localValue7 = "\"nbtName\":\"" + localValue6 + "\"";
         int localValue8 = localValue0.indexOf(localValue7);
         if (localValue8 != -1) {
            int localValue9 = localValue0.lastIndexOf("{", localValue8);
            int localValue10 = localValue0.indexOf("}", localValue8);
            if (localValue9 != -1 && localValue10 != -1) {
               String localValue11 = localValue0.substring(localValue9, localValue10 + 1);
               int localValue12 = localValue11.indexOf("\"lvl\":");
               if (localValue12 != -1) {
                  int localValue13 = localValue12 + 6;
                  int localValue14 = localValue13;

                  while (localValue14 < localValue11.length() && Character.isDigit(localValue11.charAt(localValue14))) {
                     localValue14++;
                  }

                  if (localValue14 > localValue13) {
                     localValue1.put(localValue6, Integer.parseInt(localValue11.substring(localValue13, localValue14)));
                  }
               }
            }
         }
      }
   }

   private static String internalMethod07095(NbtCompound localValue0, String localValue1, String localValue2) {
      if (!localValue0.contains(localValue1)) {
         return null;
      } else {
         NbtCompound localValue3 = localValue0.getCompound(localValue1).orElseGet(NbtCompound::new);
         if (!localValue3.contains(localValue2)) {
            return null;
         } else {
            String localValue4 = localValue3.getString(localValue2).orElse("");
            return localValue4.isEmpty() ? null : localValue4;
         }
      }
   }

   private static String internalMethod01119(NbtCompound localValue0, String localValue1) {
      if (!localValue0.contains(localValue1)) {
         return null;
      } else {
         String localValue2 = localValue0.getString(localValue1).orElse("");
         return localValue2.isEmpty() ? null : localValue2;
      }
   }

   public static CustomItemUtils.InternalType0253 internalMethod03237(ItemStack localValue0) {
      CustomItemUtils.InternalType0254 localValue1 = internalMethod03238(localValue0);
      return localValue1 != null ? localValue1.internalMethod03470() : null;
   }

   public static boolean internalMethod00027(ItemStack localValue0) {
      return internalMethod03238(localValue0) != null;
   }

   public static boolean internalMethod05347(ItemStack localValue0) {
      CustomItemUtils.InternalType0254 localValue1 = internalMethod03238(localValue0);
      return localValue1 != null && localValue1.internalMethod08994();
   }

   public static boolean internalMethod08344(ItemStack localValue0) {
      CustomItemUtils.InternalType0254 localValue1 = internalMethod03238(localValue0);
      return localValue1 != null && localValue1.internalMethod08988();
   }

   public static boolean internalMethod07776(ItemStack localValue0) {
      CustomItemUtils.InternalType0254 localValue1 = internalMethod03238(localValue0);
      return localValue1 != null && localValue1.internalMethod08992();
   }

   public static boolean internalMethod08120(ItemStack localValue0) {
      CustomItemUtils.InternalType0254 localValue1 = internalMethod03238(localValue0);
      return localValue1 != null && localValue1.internalMethod09491();
   }

   @Generated
   private CustomItemUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum InternalType0253 {
      internalField0242("\u0420\u0430\u0441\u0445\u043e\u0434\u043d\u0438\u043a"),
      internalField0243("\u0421\u0444\u0435\u0440\u0430"),
      internalField1070("\u0422\u0430\u043b\u0438\u0441\u043c\u0430\u043d"),
      internalField1071("\u0410\u0440\u0442\u0435\u0444\u0430\u043a\u0442"),
      internalField1069("\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u043e");

      private final String internalField0248;

      private InternalType0253(String localValue3) {
         this.internalField0248 = localValue3;
      }

      @Generated
      public String internalMethod07144() {
         return this.internalField0248;
      }
   }

   public static class InternalType0254 {
      private final CustomItemUtils.InternalType0253 internalField0242;
      private final String internalField0248;
      private final CustomItemUtils.InternalType0441 internalField0837;
      private final CustomItemUtils.InternalType0440 internalField0834;
      private final Map<String, Integer> internalField0543;

      public InternalType0254(
         CustomItemUtils.InternalType0253 localValue1,
         String localValue2,
         CustomItemUtils.InternalType0441 localValue3,
         CustomItemUtils.InternalType0440 localValue4,
         Map<String, Integer> localValue5
      ) {
         this.internalField0242 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0837 = localValue3;
         this.internalField0834 = localValue4;
         this.internalField0543 = localValue5 != null ? localValue5 : Map.of();
      }

      public InternalType0254(
         CustomItemUtils.InternalType0253 localValue1,
         String localValue2,
         CustomItemUtils.InternalType0441 localValue3,
         CustomItemUtils.InternalType0440 localValue4,
         String localValue5,
         int localValue6
      ) {
         this(localValue1, localValue2, localValue3, localValue4, localValue5 != null ? Map.of(localValue5, localValue6) : Map.of());
      }

      public InternalType0254(
         CustomItemUtils.InternalType0253 localValue1, String localValue2, CustomItemUtils.InternalType0441 localValue3, CustomItemUtils.InternalType0440 localValue4
      ) {
         this(localValue1, localValue2, localValue3, localValue4, Map.of());
      }

      public InternalType0254(CustomItemUtils.InternalType0253 localValue1, String localValue2, CustomItemUtils.InternalType0440 localValue3) {
         this(localValue1, localValue2, null, localValue3, Map.of());
      }

      public boolean internalMethod06307() {
         return this.internalField0543.containsKey("hms-speed");
      }

      public boolean internalMethod06310() {
         return this.internalField0543.containsKey("hms-damage") || this.internalField0543.containsKey("hms-armor");
      }

      public int internalMethod06306() {
         return this.internalField0543.getOrDefault("hms-speed", 0);
      }

      public int internalMethod06309() {
         return this.internalField0543.getOrDefault("hms-damage", 0);
      }

      public int internalMethod08985() {
         return this.internalField0543.getOrDefault("hms-armor", 0);
      }

      public int internalMethod08987() {
         return this.internalField0543.values().stream().max(Integer::compareTo).orElse(0);
      }

      public boolean internalMethod08986() {
         return this.internalField0248 != null && CustomItemUtils.internalField0546.contains(this.internalField0248);
      }

      public boolean internalMethod08988() {
         return this.internalField0242 == CustomItemUtils.InternalType0253.internalField0243;
      }

      public boolean internalMethod08992() {
         return this.internalField0242 == CustomItemUtils.InternalType0253.internalField1070;
      }

      public boolean internalMethod08994() {
         return this.internalField0242 == CustomItemUtils.InternalType0253.internalField0242;
      }

      public boolean internalMethod09491() {
         return this.internalField0242 == CustomItemUtils.InternalType0253.internalField1071;
      }

      public String internalMethod00671(ItemStack localValue1) {
         if (this.internalField0242 == CustomItemUtils.InternalType0253.internalField1071) {
            if (this.internalField0248 != null) {
               String localValue5 = CustomItemUtils.internalField1560.get(this.internalField0248);
               if (localValue5 != null) {
                  return localValue5;
               }
            }

            return this.internalField0242.internalMethod07144();
         } else if (this.internalField0242 != CustomItemUtils.InternalType0253.internalField0243
            && this.internalField0242 != CustomItemUtils.InternalType0253.internalField1070) {
            if (this.internalField0242 == CustomItemUtils.InternalType0253.internalField0242 && this.internalField0248 != null) {
               String localValue4 = CustomItemUtils.internalField1560.get(this.internalField0248);
               if (localValue4 != null) {
                  return localValue4;
               }
            }

            return ItemNbtUtils.internalMethod02181(localValue1);
         } else if (this.internalMethod08986()) {
            return this.internalField0242.internalMethod07144() + " " + this.internalField0248;
         } else if (this.internalField0837 != null) {
            String localValue3 = this.internalField0242 == CustomItemUtils.InternalType0253.internalField0243
               ? this.internalField0837.internalMethod04376()
               : this.internalField0837.internalMethod08542();
            return localValue3 + " " + this.internalField0242.internalMethod07144().toLowerCase();
         } else if (this.internalField0248 != null) {
            String localValue2 = CustomItemUtils.internalField1560.getOrDefault(this.internalField0248, this.internalField0248);
            return this.internalField0242.internalMethod07144() + " " + localValue2;
         } else {
            return this.internalField0242.internalMethod07144();
         }
      }

      public ColorRGBA internalMethod04624() {
         if (this.internalField0834 != CustomItemUtils.InternalType0440.internalField0834) {
            return null;
         } else {
            if (this.internalField0248 != null) {
               ColorRGBA localValue1 = CustomItemUtils.internalField1559.get(this.internalField0248);
               if (localValue1 != null) {
                  return localValue1;
               }
            }

            if (this.internalField0242 == CustomItemUtils.InternalType0253.internalField1071) {
               return CustomItemUtils.internalField0777;
            } else if (this.internalField0837 != null) {
               return switch (this.internalField0837) {
                  case internalField0836 -> CustomItemUtils.internalField1312;
                  case internalField1339 -> CustomItemUtils.internalField0776;
                  case internalField1341, internalField1340 -> CustomItemUtils.internalField1311;
                  default -> CustomItemUtils.internalField1309;
               };
            } else {
               return CustomItemUtils.internalField1309;
            }
         }
      }

      public ColorRGBA internalMethod01667(ItemStack localValue1) {
         return this.internalMethod04624() != null ? this.internalMethod04624() : ItemNbtUtils.internalMethod03624(localValue1.getName());
      }

      public boolean internalMethod02143(CustomItemUtils.InternalType0254 localValue1) {
         return localValue1 == null
            ? false
            : this.internalField0242 == localValue1.internalField0242
               && Objects.equals(this.internalField0248, localValue1.internalField0248)
               && this.internalField0837 == localValue1.internalField0837;
      }

      @Generated
      public CustomItemUtils.InternalType0253 internalMethod03470() {
         return this.internalField0242;
      }

      @Generated
      public String internalMethod01319() {
         return this.internalField0248;
      }

      @Generated
      public CustomItemUtils.InternalType0441 internalMethod07219() {
         return this.internalField0837;
      }

      @Generated
      public CustomItemUtils.InternalType0440 internalMethod07218() {
         return this.internalField0834;
      }

      @Generated
      public Map<String, Integer> internalMethod02706() {
         return this.internalField0543;
      }
   }

   public static enum InternalType0440 {
      internalField0834,
      internalField0835,
      internalField1338,
      internalField1336,
      internalField1335,
      internalField1337;
   }

   public static enum InternalType0441 {
      internalField0837("NORMAL", "\u041e\u0431\u044b\u0447\u043d\u0430\u044f", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439"),
      internalField0836("EPIC", "\u042d\u043f\u0438\u0447\u0435\u0441\u043a\u0430\u044f", "\u042d\u043f\u0438\u0447\u0435\u0441\u043a\u0438\u0439"),
      internalField1339(
         "LEGENDARY",
         "\u041b\u0435\u0433\u0435\u043d\u0434\u0430\u0440\u043d\u0430\u044f",
         "\u041b\u0435\u0433\u0435\u043d\u0434\u0430\u0440\u043d\u044b\u0439"
      ),
      internalField1341(
         "MYTHICAL", "\u041c\u0438\u0444\u0438\u0447\u0435\u0441\u043a\u0430\u044f", "\u041c\u0438\u0444\u0438\u0447\u0435\u0441\u043a\u0438\u0439"
      ),
      internalField1340("ETERNITY", "Eternity", "Eternity");

      private final String internalField0248;
      private final String internalField0247;
      private final String internalField1077;

      private InternalType0441(String localValue3, String localValue4, String localValue5) {
         this.internalField0248 = localValue3;
         this.internalField0247 = localValue4;
         this.internalField1077 = localValue5;
      }

      public static CustomItemUtils.InternalType0441 internalMethod02889(String localValue0) {
         if (localValue0 == null) {
            return null;
         } else {
            for (CustomItemUtils.InternalType0441 localValue4 : values()) {
               if (localValue4.internalField0248.equalsIgnoreCase(localValue0)) {
                  return localValue4;
               }
            }

            return null;
         }
      }

      @Generated
      public String internalMethod01994() {
         return this.internalField0248;
      }

      @Generated
      public String internalMethod04376() {
         return this.internalField0247;
      }

      @Generated
      public String internalMethod08542() {
         return this.internalField1077;
      }
   }
}
