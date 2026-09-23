package rockstar.client.util;




import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.ibm.icu.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import lombok.Generated;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class TextUtils implements MinecraftClientAccess {
   private static final List<String> internalField0416 = Arrays.asList(
      "The",
      "Super",
      "Mega",
      "Ultra",
      "Power",
      "Master",
      "Great",
      "Hyper",
      "Quantum",
      "Atomic",
      "Cosmic",
      "Turbo",
      "Mighty",
      "Fantastic",
      "Legendary",
      "Epic",
      "Glorious",
      "Incredible",
      "Marvelous",
      "Supreme",
      "Stellar",
      "Dynamic",
      "Heroic",
      "Valiant",
      "Brave",
      "Noble",
      "Radiant",
      "Brilliant",
      "Bold",
      "Fearless",
      "Fierce",
      "Savage",
      "Infinite",
      "Storm",
      "Thunder",
      "Lightning",
      "Solar",
      "Lunar",
      "Galactic",
      "Nebula",
      "Phoenix",
      "Titan",
      "Colossal",
      "Majestic",
      "Regal",
      "Royal",
      "Sovereign",
      "Auroral",
      "Divine",
      "Ethereal",
      "Fiery",
      "Flaming",
      "Gigahertz",
      "Hypersonic",
      "Infernal",
      "Jovial",
      "Kaleidoscopic",
      "Luminous",
      "Magnetic",
      "Nebulous",
      "Olympian",
      "Pulsar",
      "Quasar",
      "Radiant",
      "Spectral",
      "Stellar",
      "Tachyon",
      "Umbra",
      "Vortex",
      "Warp",
      "Xenon",
      "Yellowstone",
      "Zephyr"
   );
   private static final List<String> internalField0417 = Arrays.asList(
      "Swift",
      "Fierce",
      "Sneaky",
      "Brave",
      "Savage",
      "Fearless",
      "Stealthy",
      "Valiant",
      "Bold",
      "Cunning",
      "Mighty",
      "Noble",
      "Resolute",
      "Vigilant",
      "Relentless",
      "Intrepid",
      "Daring",
      "Gallant",
      "Tenacious",
      "Ferocious",
      "Unyielding",
      "Audacious",
      "Courageous",
      "Indomitable",
      "Dauntless",
      "Unstoppable",
      "Determined",
      "Invincible",
      "Unbreakable",
      "Epic",
      "Legendary",
      "Mythic",
      "Heroic",
      "Glorious",
      "Triumphant",
      "Fearsome",
      "Imposing",
      "Stalwart",
      "Stout",
      "Steadfast",
      "Grim",
      "Resolute",
      "Fateful",
      "Loyal",
      "Trusty",
      "Staunch",
      "Hardy",
      "Doughty",
      "Unflinching",
      "Unfaltering",
      "Brisk",
      "Keen",
      "Alert",
      "Quick",
      "Agile",
      "Nimble",
      "Lithe",
      "Spry",
      "Energetic",
      "Vibrant",
      "Dynamic",
      "Lively",
      "Sprightly",
      "Active",
      "Forceful",
      "Vigorous",
      "Spirited",
      "Animated",
      "Robust",
      "Brawny",
      "Muscular",
      "Husky",
      "Strong",
      "Tough",
      "Solid",
      "Sturdy",
      "Hefty",
      "Powerful",
      "Mighty",
      "Colossal",
      "Gigantic",
      "Mammoth",
      "Titanic",
      "Towering",
      "Massive",
      "Monumental",
      "Heroic",
      "Bravehearted",
      "Gutsy",
      "Doughty",
      "Unyielding",
      "Unwavering",
      "Ironwilled",
      "Strong-willed",
      "Unshakeable",
      "Elfie"
   );
   private static final List<String> internalField1145 = Arrays.asList(
      "Wolf",
      "Tiger",
      "Lion",
      "Eagle",
      "Panther",
      "Dragon",
      "Phoenix",
      "Bear",
      "Leopard",
      "Hawk",
      "Falcon",
      "Cheetah",
      "Jaguar",
      "Griffin",
      "Raven",
      "Fox",
      "Shark",
      "Viper",
      "Cobra",
      "Falcon",
      "Crocodile",
      "Raptor",
      "Condor",
      "Lynx",
      "Ocelot",
      "Cougar",
      "Puma",
      "Hound",
      "Bison",
      "Mammoth",
      "Rhino",
      "Buffalo",
      "Stallion",
      "Mustang",
      "Pegasus",
      "Wyvern",
      "Cerberus",
      "Minotaur",
      "Chimera",
      "Hydra",
      "Kraken",
      "Basilisk",
      "Manticore",
      "Unicorn",
      "Sphinx",
      "Grizzly",
      "Kodiak",
      "Polar Bear",
      "Sabertooth",
      "Direwolf",
      "Orca",
      "Narwhal",
      "Walrus",
      "Beluga",
      "Elephant",
      "Hippo",
      "Gorilla",
      "Orangutan",
      "Chimpanzee",
      "Baboon",
      "Mongoose",
      "Ferret",
      "Weasel",
      "Otter",
      "Badger",
      "Wolverine",
      "Honey Badger",
      "Lizard",
      "Iguana",
      "Gecko",
      "Komodo Dragon",
      "Monitor Lizard",
      "Tortoise",
      "Turtle",
      "Alligator",
      "Caiman",
      "Anaconda",
      "Python",
      "Boa",
      "Eel",
      "Swordfish",
      "Marlin",
      "Barracuda",
      "Piranha",
      "Penguin",
      "Albatross",
      "Seagull",
      "Pelican",
      "Stork",
      "Heron",
      "Flamingo",
      "MasTyp6ek",
      "Tigr",
      "Legacy",
      "Masha"
   );
   private static final List<String> internalField1146 = Arrays.asList(
      "Gamer",
      "Player",
      "Ninja",
      "Warrior",
      "Champion",
      "Legend",
      "Hero",
      "Master",
      "Conqueror",
      "Slayer",
      "Guardian",
      "Knight",
      "Paladin",
      "Crusader",
      "Ranger",
      "Assassin",
      "Mage",
      "Sorcerer",
      "Wizard",
      "Enchanter",
      "Necromancer",
      "Berserker",
      "Gladiator",
      "Samurai",
      "Viking",
      "Pirate",
      "Outlaw",
      "Mercenary",
      "Hunter",
      "Scout",
      "Rogue",
      "Thief",
      "Sentinel",
      "Protector",
      "Savior",
      "Defender",
      "Avenger",
      "Warlord",
      "Commander",
      "Captain",
      "General",
      "Marshal",
      "Overlord",
      "Monarch",
      "Emperor",
      "King",
      "Queen",
      "Prince",
      "Princess",
      "Duke",
      "Duchess",
      "Baron",
      "Baroness",
      "Lord",
      "Lady",
      "Warden",
      "Sentinel",
      "Crusader",
      "Champion",
      "Virtuoso",
      "Adept",
      "Prodigy",
      "Savant",
      "Genius",
      "Maven",
      "Whiz",
      "Ace",
      "Virtuoso",
      "Expert",
      "Specialist",
      "Technician",
      "Strategist",
      "Tactician",
      "Operative",
      "Agent",
      "Spy",
      "Infiltrator",
      "Saboteur",
      "Shadow",
      "Phantom",
      "Specter",
      "Shade",
      "Mystic",
      "Seer",
      "Oracle",
      "Prophet",
      "Visionary",
      "Dreamer",
      "Illusionist",
      "Conjurer",
      "Invoker",
      "Diviner",
      "Alchemist",
      "Shaman",
      "Druid",
      "Elementalist",
      "Geomancer",
      "Pyromancer",
      "Hydromancer",
      "Aeromancer",
      "Archon",
      "Brawler",
      "Catalyst",
      "Dynamo",
      "Energizer",
      "Flux",
      "Fusion",
      "Gizmo",
      "Hacker",
      "Innovator",
      "Juggernaut",
      "Kinetix",
      "Luminary",
      "Marauder",
      "Nomad",
      "Operator",
      "Pioneer",
      "Quickshot",
      "Rascal",
      "Slasher",
      "Titan",
      "Umbra",
      "Vanguard",
      "Warden",
      "Pro",
      "Xenon",
      "Yokai",
      "Zealot",
      "Zorro",
      "Zoltar"
   );
   private static final SimpleDateFormat internalField0032 = new SimpleDateFormat("HH:mm", Locale.getDefault());
   private static final Random internalField0362 = new Random();
   public static String internalMethod07495() {
      String localValue0 = internalMethod07437(internalField0416);
      String localValue1 = internalMethod07437(internalField0417);
      String localValue2 = internalMethod07437(internalField1145);
      String localValue3 = internalMethod07437(internalField1146);
      String localValue4 = internalField0362.nextInt(100) < 30 ? String.valueOf(2000 + internalField0362.nextInt(26)) : "";
      ArrayList localValue5 = new ArrayList();
      if (internalField0362.nextBoolean()) {
         localValue5.add(localValue0);
      }

      if (internalField0362.nextBoolean()) {
         localValue5.add(localValue1);
      }

      if (internalField0362.nextBoolean()) {
         localValue5.add(localValue2);
      }

      if (internalField0362.nextBoolean()) {
         localValue5.add(localValue3);
      }

      if (localValue5.isEmpty()) {
         localValue5.add(localValue0);
      }

      if (localValue5.size() < 2) {
         localValue5.add(internalField0362.nextBoolean() ? localValue1 : localValue2);
      }

      String localValue6 = String.join("", localValue5) + localValue4;
      if (internalField0362.nextInt(100) < 20) {
         localValue6 = localValue6 + (internalField0362.nextBoolean() ? "52" : "69");
      } else {
         localValue6 = localValue6 + internalMethod03358(2 + internalField0362.nextInt(3));
      }

      if (localValue6.length() > 16) {
         localValue6 = localValue6.substring(localValue6.length() - 16);
      }

      return localValue6;
   }

   public static String internalMethod00670(double localValue0) {
      if (localValue0 == (int)localValue0) {
         return String.valueOf((int)localValue0);
      } else {
         String localValue2 = String.format("%.2f", localValue0).replace(",", ".").replaceAll("\\.?0+$", "");
         return localValue2.endsWith(".") ? localValue2.replace(".", "") : localValue2;
      }
   }

   public static String internalMethod07254(double localValue0) {
      return String.format("%.1f", localValue0).replace(",", ".");
   }

   private static String internalMethod07437(List<String> localValue0) {
      return (String)localValue0.get(internalField0362.nextInt(localValue0.size()));
   }

   private static String internalMethod03358(int localValue0) {
      StringBuilder localValue1 = new StringBuilder();

      for (int localValue2 = 0; localValue2 < localValue0; localValue2++) {
         localValue1.append(internalField0362.nextInt(10));
      }

      return localValue1.toString();
   }

   public static String internalMethod05852(String localValue0) {
      if (localValue0.endsWith("\u0430")) {
         return "\u0430";
      } else if (localValue0.endsWith("a")) {
         return "\u0430";
      } else if (localValue0.endsWith("y")) {
         return "\u043e";
      } else if (localValue0.endsWith("\u044e")) {
         return "o";
      } else if (localValue0.endsWith("u")) {
         return "o";
      } else if (localValue0.endsWith("\u044f")) {
         return "\u0430";
      } else if (localValue0.endsWith("\u044b")) {
         return "\u044b";
      } else {
         return localValue0.endsWith("\u0438") ? "\u044b" : "";
      }
   }

   public static String internalMethod07212(float localValue0) {
      double localValue1 = Math.abs(localValue0);
      long localValue3 = (long)Math.floor(localValue1);
      double localValue5 = localValue1 - localValue3;
      if (localValue5 > 1.0E-9) {
         return "\u0430";
      } else {
         int localValue7 = (int)(localValue3 % 100L);
         if (localValue7 >= 11 && localValue7 <= 14) {
            return "\u043e\u0432";
         } else {
            return switch (localValue7 % 10) {
               case 1 -> "";
               case 2, 3, 4 -> "\u0430";
               default -> "\u043e\u0432";
            };
         }
      }
   }

   public static String internalMethod05759(float localValue0) {
      Language localValue1 = LanguageManager.internalMethod00625();

      return switch (localValue1) {
         case internalField0164 -> internalMethod08785(localValue0);
         case internalField1031 -> internalMethod08470(localValue0);
         case internalField1032 -> internalMethod08194(localValue0);
         case internalField0165 -> internalMethod08908(localValue0);
      };
   }

   private static String internalMethod08785(float localValue0) {
      double localValue1 = Math.abs(localValue0);
      long localValue3 = (long)Math.floor(localValue1);
      double localValue5 = localValue1 - localValue3;
      if (localValue5 > 1.0E-9) {
         return "\u0430";
      } else {
         int localValue7 = (int)(localValue3 % 100L);
         if (localValue7 >= 11 && localValue7 <= 14) {
            return "\u043e\u0432";
         } else {
            return switch (localValue7 % 10) {
               case 1 -> "";
               case 2, 3, 4 -> "\u0430";
               default -> "\u043e\u0432";
            };
         }
      }
   }

   private static String internalMethod08470(float localValue0) {
      double localValue1 = Math.abs(localValue0);
      long localValue3 = (long)Math.floor(localValue1);
      double localValue5 = localValue1 - localValue3;
      if (localValue5 > 1.0E-9) {
         return "\u0438";
      } else {
         int localValue7 = (int)(localValue3 % 100L);
         if (localValue7 >= 11 && localValue7 <= 14) {
            return "\u0456\u0432";
         } else {
            return switch (localValue7 % 10) {
               case 1 -> "";
               case 2, 3, 4 -> "\u0438";
               default -> "\u0456\u0432";
            };
         }
      }
   }

   private static String internalMethod08194(float localValue0) {
      double localValue1 = Math.abs(localValue0);
      long localValue3 = (long)Math.floor(localValue1);
      double localValue5 = localValue1 - localValue3;
      if (localValue5 > 1.0E-9) {
         return "y";
      } else if (localValue3 == 1L) {
         return "";
      } else {
         return localValue3 >= 2L && localValue3 <= 4L ? "y" : "\u00f3w";
      }
   }

   private static String internalMethod08908(float localValue0) {
      double localValue1 = Math.abs(localValue0);
      return localValue1 == 1.0 ? "" : "s";
   }

   public static String internalMethod04982(int localValue0) {
      return KeybindUtils.internalMethod07434(KeybindUtils.internalMethod02857(localValue0)) + internalMethod08327(KeybindUtils.internalMethod02025(localValue0));
   }

   private static String internalMethod08327(int localValue0) {
      if (localValue0 >= 0 && localValue0 <= 7) {
         return switch (localValue0) {
            case 0 -> LanguageManager.internalMethod07214("mouse.lmb");
            case 1 -> LanguageManager.internalMethod07214("mouse.rmb");
            case 2 -> LanguageManager.internalMethod07214("mouse.mmb");
            case 3 -> "MOUSE4";
            case 4 -> "MOUSE5";
            case 5 -> "MOUSE6";
            case 6 -> "MOUSE7";
            case 7 -> "MOUSE8";
            default -> "MOUSE" + localValue0;
         };
      } else if (localValue0 <= -1) {
         return "NONE";
      } else {
         String localValue1 = InputUtil.fromKeyCode(new net.minecraft.client.input.KeyInput(localValue0, -1, 0)).getTranslationKey();
         localValue1 = localValue1.replace("key.keyboard.", "")
            .replace("key.", "")
            .replace(".", "")
            .replace("left", "l")
            .replace("right", "r")
            .replace("apostrophe", "apost")
            .replace("printscreen", "prtsc")
            .replace("graveaccent", "grave")
            .replace("control", "ctrl");
         return localValue1.toUpperCase();
      }
   }

   public static String internalMethod04048() {
      return internalField0032.format(new Date());
   }

   public static String internalMethod08270() {
      LocalDate localValue0 = LocalDate.now();
      String[] localValue1 = new String[]{
         "time.days.monday", "time.days.tuesday", "time.days.wednesday", "time.days.thursday", "time.days.friday", "time.days.saturday", "time.days.sunday"
      };
      String[] localValue2 = new String[]{
         "time.months.january",
         "time.months.february",
         "time.months.march",
         "time.months.april",
         "time.months.may",
         "time.months.june",
         "time.months.july",
         "time.months.august",
         "time.months.september",
         "time.months.october",
         "time.months.november",
         "time.months.december"
      };
      DayOfWeek localValue3 = localValue0.getDayOfWeek();
      String localValue4 = LanguageManager.internalMethod07214(localValue1[localValue3.getValue() - 1]);
      int localValue5 = localValue0.getDayOfMonth();
      Month localValue6 = localValue0.getMonth();
      String localValue7 = LanguageManager.internalMethod07214(localValue2[localValue6.getValue() - 1]);
      return String.format("%s, %d %s", localValue4, localValue5, localValue7);
   }

   public static void internalMethod05864(String localValue0) {
      internalField0149.keyboard.setClipboard(localValue0);
   }

   public static String internalMethod06864(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         StringBuilder localValue1 = new StringBuilder(localValue0.length());
         int localValue2 = 0;
         boolean localValue3 = false;

         for (int localValue4 = 0; localValue4 <= localValue0.length(); localValue4++) {
            if (localValue4 != localValue0.length() && !Character.isWhitespace(localValue0.charAt(localValue4))) {
               char localValue7 = localValue0.charAt(localValue4);
               int localValue6 = "\u1d00\u0299\u1d04\u1d05\u1d07\u0493\ua730\u0262\u029c\u026a\u1d0a\u1d0b\u029f\u1d0d\u0274\u1d0f\u1d18\ua7af\u01eb\u0280\ua731\u0455\u1d1b\u1d1c\u1d20\u1d21\u1d61\u028f\u1d22"
                  .indexOf(localValue7);
               if (localValue6 < 0) {
                  localValue1.append(localValue7);
               } else {
                  localValue1.append("ABCDEFFGHIJKLMNOPQQRSSTUVWXYZ".charAt(localValue6));
                  localValue3 = true;
               }
            } else {
               if (localValue3) {
                  for (int localValue5 = localValue2; localValue5 < localValue1.length(); localValue5++) {
                     localValue1.setCharAt(localValue5, Character.toUpperCase(localValue1.charAt(localValue5)));
                  }
               }

               if (localValue4 < localValue0.length()) {
                  localValue1.append(localValue0.charAt(localValue4));
               }

               localValue2 = localValue1.length();
               localValue3 = false;
            }
         }

         return localValue1.toString();
      } else {
         return "";
      }
   }

   public static MutableText internalMethod00893(Text localValue0, FontFamily localValue1) {
      if (localValue0 == null) {
         return Text.empty();
      } else {
         StringBuilder localValue2 = new StringBuilder();
         ArrayList localValue3 = new ArrayList();
         BitSet localValue4 = new BitSet();
         localValue0.visit((localValue3x, localValue4x) -> {
            localValue4x.codePoints().forEach(localValue4xx -> {
               String localValue5x = RankStyleRegistry.internalMethod01705(localValue4xx);
               if (localValue5x != null) {
                  boolean localValue6x = RankStyleRegistry.internalMethod05046(localValue4xx);
                  localValue4.set(localValue2.length(), localValue2.length() + localValue5x.length());

                  for (int localValue7x = 0; localValue7x < localValue5x.length(); localValue7x++) {
                     localValue2.append(localValue5x.charAt(localValue7x));
                     localValue3.add(localValue6x ? localValue3x.withColor(RankStyleRegistry.internalMethod03418(localValue4xx, localValue7x, localValue5x.length(), 1.0F, 0) & 16777215) : localValue3x);
                  }
               } else {
                  localValue2.appendCodePoint(localValue4xx);

                  while (localValue3.size() < localValue2.length()) {
                     localValue3.add(localValue3x);
                  }
               }
            });
            return Optional.empty();
         }, Style.EMPTY);
         int localValue5 = 0;

         for (int localValue6 = 0; localValue5 <= localValue2.length(); localValue5++) {
            if (localValue5 >= localValue2.length() || Character.isWhitespace(localValue2.charAt(localValue5))) {
               int localValue7 = localValue4.nextSetBit(localValue6);
               if (localValue7 >= 0 && localValue7 < localValue5) {
                  for (int localValue8 = localValue6; localValue8 < localValue5; localValue8++) {
                     localValue2.setCharAt(localValue8, Character.toUpperCase(localValue2.charAt(localValue8)));
                  }
               }

               localValue6 = localValue5 + 1;
            }
         }

         StringBuilder localValue11 = new StringBuilder();
         ArrayList localValue12 = new ArrayList();
         boolean localValue13 = localValue1 != null && localValue1.internalMethod04943('a');

         for (int localValue14 = 0; localValue14 < localValue2.length(); localValue14++) {
            char localValue9 = localValue2.charAt(localValue14);
            char localValue10 = localValue11.isEmpty() ? 0 : localValue11.charAt(localValue11.length() - 1);
            if (localValue9 == 167) {
               localValue14++;
            } else if ((localValue9 != ' ' || localValue10 != 0 && localValue10 != ' ') && (localValue9 == ' ' || !localValue13 || localValue1.internalMethod04943(localValue9))) {
               if (localValue9 == ']' && localValue10 == '[') {
                  localValue11.deleteCharAt(localValue11.length() - 1);
                  localValue12.removeLast();
               } else {
                  localValue11.append(localValue9);
                  localValue12.add((Style)localValue3.get(localValue14));
               }
            }
         }

         while (!localValue11.isEmpty() && localValue11.charAt(localValue11.length() - 1) == ' ') {
            localValue11.deleteCharAt(localValue11.length() - 1);
            localValue12.removeLast();
         }

         MutableText localValue15 = Text.empty();
         int localValue16 = 0;

         for (int localValue17 = 1; localValue17 <= localValue11.length(); localValue17++) {
            if (localValue17 >= localValue11.length() || !((Style)localValue12.get(localValue17)).equals(localValue12.get(localValue16))) {
               localValue15.append(Text.literal(localValue11.substring(localValue16, localValue17)).setStyle((Style)localValue12.get(localValue16)));
               localValue16 = localValue17;
            }
         }

         return localValue15;
      }
   }

   public static MutableText internalMethod04889(String localValue0) {
      if (localValue0.startsWith("[\u2605]")) {
         String localValue1 = localValue0.substring("[\u2605]".length());
         MutableText localValue2 = Text.literal("[\u2605]").formatted(Formatting.RED);
         MutableText localValue3 = Text.literal(localValue1).formatted(Formatting.GOLD);
         return Text.literal("").append(localValue2).append(localValue3);
      } else {
         return Text.literal(localValue0);
      }
   }

   @Generated
   private TextUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
