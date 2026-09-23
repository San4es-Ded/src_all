package aethereal.autobuy;

import aethereal.config.AttributeCondition;
import aethereal.config.AttributeProcessor;
import aethereal.config.DescriptionProcessor;
import aethereal.config.EnchantmentProcessor;
import aethereal.config.NBTProcessor;
import aethereal.config.PotionCondition;
import aethereal.config.PotionProcessor;
import aethereal.render.AnimationUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1893;
import net.minecraft.class_5134;
import net.minecraft.class_9296;
import net.minecraft.class_9331;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;

public enum AutoBuyEntry {
   POPPER_POTION(
      "[★] Хлопушка",
      class_1802.field_8436,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5909, 10, 200))
         .a(new PotionCondition(class_1294.field_5904, 5, 300))
         .a(new PotionCondition(class_1294.field_5919, 10, 100))
         .a(new PotionCondition(class_1294.field_5912, 1, 3600)),
      16711680
   ),
   HOLY_WATER(
      "[★] Святая вода",
      class_1802.field_8436,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5924, 2, 900))
         .a(new PotionCondition(class_1294.field_5905, 2, 12000))
         .a(new PotionCondition(class_1294.field_5915, 2, 0)),
      16777215
   ),
   RAGE_POTION(
      "[★] Зелье Гнева",
      class_1802.field_8436,
      new PotionProcessor().a(new PotionCondition(class_1294.field_5910, 5, 600)).a(new PotionCondition(class_1294.field_5909, 4, 600)),
      10040115
   ),
   PALLADIN_POTION(
      "[★] Зелье Палладина",
      class_1802.field_8436,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5907, 1, 12000))
         .a(new PotionCondition(class_1294.field_5918, 1, 12000))
         .a(new PotionCondition(class_1294.field_5914, 3, 1200))
         .a(new PotionCondition(class_1294.field_5905, 1, 18000)),
      65535
   ),
   ASSASSIN_POTION(
      "[★] Зелье Ассасина",
      class_1802.field_8436,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5910, 4, 1200))
         .a(new PotionCondition(class_1294.field_5904, 3, 6000))
         .a(new PotionCondition(class_1294.field_5917, 1, 1200))
         .a(new PotionCondition(class_1294.field_5921, 2, 0)),
      3355443
   ),
   RADIATION_POTION(
      "[★] Зелье Радиации",
      class_1802.field_8436,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5899, 2, 300))
         .a(new PotionCondition(class_1294.field_5920, 2, 300))
         .a(new PotionCondition(class_1294.field_5909, 3, 300))
         .a(new PotionCondition(class_1294.field_5903, 5, 300))
         .a(new PotionCondition(class_1294.field_5912, 1, 300)),
      3329330
   ),
   SLEEPING_PILL(
      "[★] Снотворное",
      class_1802.field_8436,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5911, 2, 1800))
         .a(new PotionCondition(class_1294.field_5901, 2, 200))
         .a(new PotionCondition(class_1294.field_5920, 3, 1800))
         .a(new PotionCondition(class_1294.field_5919, 1, 200)),
      4737096
   ),
   TALISMAN_CRUSHER(
      "[★] Талисман Крушителя",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23716, 4.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23721, 3.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23725, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23724, 2.0, class_1323.field_6328))
   ),
   TALISMAN_DISCORD(
      "[★] Талисман Раздора",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 4.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23719, 0.10000001817743467, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_23723, 0.10000001817743467, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_23724, -3.0, class_1323.field_6328))
   ),
   TALISMAN_TYRANT(
      "[★] Талисман Тирана",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23724, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, -4.0, class_1323.field_6328))
   ),
   TALISMAN_RAGE(
      "[★] Талисман Ярости",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 5.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, -4.0, class_1323.field_6328))
   ),
   TALISMAN_WHIRLWIND(
      "[★] Талисман Вихря",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23716, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23719, 0.1500000347540806, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_23723, 0.1500000347540806, class_1323.field_6330))
   ),
   TALISMAN_GLOOM(
      "[★] Талисман Мрака",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23724, 1.5, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, 1.5, class_1323.field_6328))
   ),
   TALISMAN_DEMON(
      "[★] Талисман Демона",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 2.5, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23723, 0.10000001817743467, class_1323.field_6330))
   ),
   TALISMAN_PUNISHER(
      "[★] Талисман Карателя xxx",
      class_1802.field_8288,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 7.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, -4.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23719, 0.10000001817743467, class_1323.field_6330))
   ),
   SPHERE_CHAOS(
      "[★] Сфера Хаоса",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23716, -4.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23724, 1.5, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23721, 2.5, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23719, 0.07000004566537438, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_23723, 0.13000000024761527, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_49078, 0.09000001839414021, class_1323.field_6330)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODY0MTkwMCwKICAicHJvZmlsZUlkIiA6ICIxNzRjZmRiNGEzY2I0M2I1YmZjZGU0MjRjM2JiMmM2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJhZWwxOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2E3YWU3Y2RjZjYxNmU4YjdhNDIyMWE2MjFiMjQzNTc1M2M2MGVkNmEyNThlYTA2MGRhZTMwMDJmZmU5ZTI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
   ),
   SPHERE_SATYR(
      "[★] Сфера Сатира",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23728, -0.10000000768432185, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_23723, 0.1500000347540806, class_1323.field_6330)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODYwODUyOCwKICAicHJvZmlsZUlkIiA6ICJkMTQ4NjFiM2UwZmM0Njk5OTFlMTcyNTllMzdiZjZhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJyYXhpdG9jbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NzFhOWE0OThiNGZhNWVjNDkzNjJmOWJjODhlZGE0ZjUyYjA0ZGU0OWQ3NWFhM2NhMzMyYTFmZWExYWEwZTU3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
   ),
   SPHERE_BEAST(
      "[★] Сфера Бестии",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23724, 1.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, 4.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23719, 0.10000001817743467, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_23723, 0.10000001817743467, class_1323.field_6330)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0MzgzNDkzMCwKICAicHJvZmlsZUlkIiA6ICI1MzUzNWIxN2M0ZDY0NWQ0YWUwY2U2ZjM4Zjk0NTFjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVYml2aXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQxMWFjMTczODFiOWZjZTliYWIzYzcyYWZkYjdmMTk4NTcwZGFmNDczMmJkODExZDMxYzIyN2Q4MGZhMzliMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"
   ),
   SPHERE_ARES(
      "[★] Сфера Ареса",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 6.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23724, -2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, -2.0, class_1323.field_6328)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzc3NDI1NSwKICAicHJvZmlsZUlkIiA6ICJhYWMxYjA2OWNkMjE0NWE2ODNlNzQxNzE4MDcxMGU4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJqdXNhbXUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE2YWRjNmJhZmNiNTdmZDcwN2RlZTdkZDZhNzM2ZmUxMjY3MTFkNTNhMWZkNmNlNzg5ZGE0MWIzYmUxM2YyYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"
   ),
   SPHERE_HYDRA(
      "[★] Сфера Гидры",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23716, 4.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23724, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_51576, 0.5, class_1323.field_6330))
         .a(new AttributeCondition(class_5134.field_51583, 0.5, class_1323.field_6330)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODUzMjE4MywKICAicHJvZmlsZUlkIiA6ICI1OGZmZWI5NTMxNGQ0ODcwYTQwYjVjYjQyZDRlYTU5OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2luREJuZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzYzExOGQ2OTZkOTEwZTU0ZGUwMmNhNGQ4MDc1NDNmOWIxOGMwMDhjOTgzOGQyZmY2OTM3NzYyMmZiMWQzMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"
   ),
   SPHERE_ICARUS(
      "[★] Сфера Икара",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23721, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, 2.0, class_1323.field_6328)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODU4MjQ5MSwKICAicHJvZmlsZUlkIiA6ICJhZWNkODIxZTQyYzE0ZDJlOThmNTA1OTg1MWI5OWMzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb2RyaVgyMDc1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2ODAzZTZkNTY2N2EyZDYxMDYyOGJjM2IzMmY4NjNjZGE0OTVjNDY1NjE2ZGU2NTVjYjMyOTkzM2I2MWFmNzciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ=="
   ),
   SPHERE_ERIS(
      "[★] Сфера Эрида",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23726, 1.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23716, 2.0, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_47758, 1.0, class_1323.field_6328)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVEZXZKYWRlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZlNGUyZjEwNDdmM2VjNmU5ZTQ1OTE4NDczOWUzM2I3YzFmYzYzYWQ4MjAyYmRhYjlmMDI0NTA4YWRkMjNlNWIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ=="
   ),
   SPHERE_TITAN(
      "[★] Сфера Титана",
      class_1802.field_8575,
      new AttributeProcessor()
         .a(new AttributeCondition(class_5134.field_23724, 2.5, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23725, 2.5, class_1323.field_6328))
         .a(new AttributeCondition(class_5134.field_23719, -0.15000007945819008, class_1323.field_6330)),
      "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM1NDQ1NTE5MiwKICAicHJvZmlsZUlkIiA6ICJkOTcwYzEzZTM4YWI0NzlhOTY1OGM1ZDQ1MjZkMTM0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmltcHlMYWNlODUxMjciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFlOTY5ODQ1OGI3ODQxYzk2YWU0ZjI0ZWM4NGFlMDE3MjQxMDA2NDFjNTY0ZTJhN2IxODVmNDA2ZThlZDIzIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
   ),
   TRAP("[★] Трапка", class_1802.field_22021, new DescriptionProcessor().a("Каст: Нерушимая клетка")),
   CLEAR_DUST("[★] Явная пыль", class_1802.field_8479, new DescriptionProcessor().a("Каст: Световая вспышка")),
   SNOWBALL("[★] Снежок заморозка", class_1802.field_8543, new DescriptionProcessor().a("Каст: Ледяная сфера")),
   GOD_AURA("[★] Божья аура", class_1802.field_8614, new DescriptionProcessor().a("Каст: Божественная аура")),
   DISORIENTATION("[★] Дезориентация", class_1802.field_8449, new DescriptionProcessor().a("Каст: Звуковая волна")),
   STRATUM("[★] Пласт", class_1802.field_8551, new DescriptionProcessor().a("Каст: Нерушимая стена")),
   CRUSHER_SWORD(
      "xxx Меч Крушителя xxx",
      class_1802.field_22022,
      new EnchantmentProcessor().b(class_1893.field_9121),
      new DescriptionProcessor().a("[★] Оригинальный предмет").a("Опытный", 3).a("Вампиризм", 2).a("Окисление", 2).a("Яд", 3).a("Детекция", 3)
   ),
   CRUSHER_MACE(
      "xxx Булава Крушителя xxx",
      class_1802.field_49814,
      new DescriptionProcessor().a("[★] Оригинальный предмет").a("Опытный", 3).a("Вампиризм", 2).a("Окисление", 2).a("Яд", 3).a("Детекция", 3)
   ),
   CRUSHER_TRIDENT(
      "xxx Трезубец Крушителя xxx",
      class_1802.field_8547,
      new DescriptionProcessor().a("[★] Оригинальный предмет").a("Скаут", 3).a("Ступор", 3).a("Притяжение", 2).a("Возвращение").a("Подрывник")
   ),
   CRUSHER_PICKAXE(
      "xxx Кирка Крушителя xxx",
      class_1802.field_22024,
      new DescriptionProcessor().a("[★] Оригинальный предмет").a("Бульдозер", 2).a("Опытный", 3).a("Магнит").a("Авто-Плавка").a("Паутина").a("Пингер")
   ),
   CRUSHER_CROSSBOW("xxx Арбалет Крушителя xxx", class_1802.field_8399, new DescriptionProcessor().a("[★] Оригинальный предмет")),
   CRUSHER_HELMET(
      "xxx Шлем Крушителя xxx",
      class_1802.field_22027,
      new EnchantmentProcessor()
         .a(class_1893.field_9111, 5)
         .a(class_1893.field_9095, 5)
         .a(class_1893.field_9107, 5)
         .a(class_1893.field_9096, 5)
         .a(class_1893.field_9119, 5)
         .a(class_1893.field_9101, 1)
         .a(class_1893.field_9105, 1)
         .a(class_1893.field_9127, 3)
         .b(class_1893.field_9097)
   ),
   CRUSHER_CHESTPLATE(
      "xxx Нагрудник Крушителя xxx",
      class_1802.field_22028,
      new EnchantmentProcessor()
         .a(class_1893.field_9111, 5)
         .a(class_1893.field_9095, 5)
         .a(class_1893.field_9107, 5)
         .a(class_1893.field_9096, 5)
         .a(class_1893.field_9119, 5)
         .a(class_1893.field_9101, 1)
         .b(class_1893.field_9097)
   ),
   CRUSHER_LEGGINGS(
      "xxx Поножи Крушителя xxx",
      class_1802.field_22029,
      new EnchantmentProcessor()
         .a(class_1893.field_9111, 5)
         .a(class_1893.field_9095, 5)
         .a(class_1893.field_9107, 5)
         .a(class_1893.field_9096, 5)
         .a(class_1893.field_9119, 5)
         .a(class_1893.field_9101, 1)
         .b(class_1893.field_9097)
   ),
   CRUSHER_BOOTS(
      "xxx Ботинки Крушителя xxx",
      class_1802.field_22030,
      new EnchantmentProcessor()
         .a(class_1893.field_9111, 5)
         .a(class_1893.field_9095, 5)
         .a(class_1893.field_9107, 5)
         .a(class_1893.field_9096, 5)
         .a(class_1893.field_9119, 5)
         .a(class_1893.field_9101, 1)
         .a(class_1893.field_9129, 4)
         .a(class_1893.field_23071, 3)
         .a(class_1893.field_9128, 3)
         .b(class_1893.field_9097)
   ),
   PICK_SPHERES("[★] Отмычка к Сферам", class_1802.field_8366, new DescriptionProcessor().a("С Сферами")),
   GOD_TOUCH("[★] Божье касание", class_1802.field_8335, new DescriptionProcessor().a("Может добыть спавнер")),
   POWER_STRIKE("[★] Мощный удар", class_1802.field_8335, new DescriptionProcessor().a("Может разрушить бедрок")),
   MIST_COMMON("Обычный мист", class_1802.field_17346, new DescriptionProcessor().a("Уровень лута: Обычный")),
   MIST_RICH("Богатый мист", class_1802.field_17346, new DescriptionProcessor().a("Уровень лута: Богатый")),
   MIST_LEGENDARY("Легендарный мист", class_1802.field_23842, new DescriptionProcessor().a("Уровень лута: Легендарный")),
   SKIN_INEVITABLE("[★] Неизбежный скин", class_1802.field_8407, new DescriptionProcessor().a("получаете Неизбежный скин")),
   SKIN_DRAGON("[★] Драконий скин", class_1802.field_8407, new DescriptionProcessor().a("получаете Драконий скин")),
   CHUNK_LOADER_1("[★] Прогрузчик чанков [1x1]", class_1802.field_8238, new DescriptionProcessor().a("прогружаемой области (1x1)")),
   CHUNK_LOADER_3("[★] Прогрузчик чанков [3x3]", class_1802.field_8238, new DescriptionProcessor().a("прогружаемой области (3x3)")),
   CHUNK_LOADER_5("[★] Прогрузчик чанков [5x5]", class_1802.field_8238, new DescriptionProcessor().a("прогружаемой области (5x5)")),
   REGION_25("[★] Регион 25x25", class_1802.field_8799, new DescriptionProcessor().a("Размер: 25x25 блоков")),
   AIRDROP("[★] Аирдроп", class_1802.field_8530, new DescriptionProcessor().a("призыва Аирдропа")),
   BLOCK_DAMAGER("[★] Блок дамагер", class_1802.field_16538, new DescriptionProcessor().a("Каст: Нанесение урона")),
   FLY_MODIFIER("[⚡] Модификатор полёта", class_1802.field_8153, new DescriptionProcessor().a("Доступ к /fly")),
   FIX_MODIFIER("[⚡] Модификатор починки", class_1802.field_27063, new DescriptionProcessor().a("Доступ к /fix")),
   PRIVILEGE_KEY("[★] Ключ от кейса с Привилегиями", class_1802.field_47315, new DescriptionProcessor().a("Открывает: Кейс с Привилегиями")),
   TOKEN_KEY("[★] Ключ от кейса с Токенами", class_1802.field_47315, new DescriptionProcessor().a("Открывает: Кейс с Токенами")),
   WHITE_TNT("[★] Вайт", class_1802.field_8626, new DescriptionProcessor().a("в 10 раз сильнее обычного").b("способен взорвать обсидиан")),
   BLACK_TNT("[★] Блэк", class_1802.field_8626, new DescriptionProcessor().a("способен взорвать обсидиан")),
   BLOOD_ARROW(
      "Кровавая стрела",
      class_1802.field_8087,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5911, 3, 60))
         .a(new PotionCondition(class_1294.field_5919, 1, 40))
         .a(new PotionCondition(class_1294.field_5901, 1, 40))
         .a(new PotionCondition(class_1294.field_5916, 1, 100))
   ),
   FROST_ARROW(
      "Стрела обледенения",
      class_1802.field_8087,
      new PotionProcessor().a(new PotionCondition(class_1294.field_5909, 10, 100)).a(new PotionCondition(class_1294.field_5901, 3, 40))
   ),
   AGONY_ARROW(
      "Мучительная стрела",
      class_1802.field_8087,
      new PotionProcessor()
         .a(new PotionCondition(class_1294.field_5909, 3, 100))
         .a(new PotionCondition(class_1294.field_5920, 3, 100))
         .a(new PotionCondition(class_1294.field_5899, 3, 100))
   ),
   GOLDEN_APPLE("Золотое яблоко", class_1802.field_8463),
   ENCHANTED_GOLDEN_APPLE("Чарка", class_1802.field_8367),
   SPAWNER("Спавнер", class_1802.field_8849),
   EMERALD_ORE("Изумрудная руда", class_1802.field_8837),
   DRAGON_HEAD("Голова дракона", class_1802.field_8712),
   DRAGON_EGG("Яйцо дракона", class_1802.field_8840),
   ELYTRA("Элитры", class_1802.field_8833),
   BEACON("Маяк", class_1802.field_8668),
   VILLAGER_SPAWN_EGG("Яйцо призыва крестьянина", class_1802.field_8086),
   ENDERMAN_SPAWN_EGG("Яйцо призыва эндермена", class_1802.field_8374),
   NETHERITE_UPGRADE("Отделка незеритовая", class_1802.field_41946),
   DIAMOND("Алмаз", class_1802.field_8477),
   NETHER_STAR("Звезда Незера", class_1802.field_8137),
   ANCIENT_DEBRIS("Древние обломки", class_1802.field_22019),
   NETHERITE_INGOT("Незеритовый слиток", class_1802.field_22020);

   private final String ay;
   private final Object az;
   private final class_1792 aA;
   private final AttributeProcessor aB;
   private final EnchantmentProcessor aC;
   private final DescriptionProcessor aD;
   private final NBTProcessor aE;
   private final PotionProcessor aF;
   private final AnimationUtil aG = new AnimationUtil();
   private double aH = 1.0;
   private boolean aI = false;

   private AutoBuyEntry(String displayName, class_1792 item) {
      this(displayName, item, null, null, null, null, null, null);
   }

   private AutoBuyEntry(String displayName, class_1792 item, PotionProcessor potionProcessor) {
      this(displayName, item, null, null, null, null, potionProcessor, null);
   }

   private AutoBuyEntry(String displayName, class_1792 item, AttributeProcessor attributeProcessor) {
      this(displayName, item, attributeProcessor, null, null, null, null, null);
   }

   private AutoBuyEntry(String displayName, class_1792 item, AttributeProcessor attributeProcessor, Object profile) {
      this(displayName, item, attributeProcessor, null, null, null, null, profile);
   }

   private AutoBuyEntry(String displayName, class_1792 item, DescriptionProcessor descriptionProcessor) {
      this(displayName, item, null, null, descriptionProcessor, null, null, null);
   }

   private AutoBuyEntry(String displayName, class_1792 item, EnchantmentProcessor enchantmentProcessor, DescriptionProcessor descriptionProcessor) {
      this(displayName, item, null, enchantmentProcessor, descriptionProcessor, null, null, null);
   }

   private AutoBuyEntry(String displayName, class_1792 item, EnchantmentProcessor enchantmentProcessor) {
      this(displayName, item, null, enchantmentProcessor, null, null, null, null);
   }

   private AutoBuyEntry(String displayName, class_1792 item, PotionProcessor potionProcessor, Object profile) {
      this(displayName, item, null, null, null, null, potionProcessor, profile);
   }

   private AutoBuyEntry(
      String displayName,
      class_1792 item,
      AttributeProcessor attributeProcessor,
      EnchantmentProcessor enchantmentProcessor,
      DescriptionProcessor descriptionProcessor,
      NBTProcessor nbtProcessor,
      PotionProcessor potionProcessor,
      Object profile
   ) {
      this.ay = displayName;
      this.aA = item;
      this.aB = attributeProcessor;
      this.aC = enchantmentProcessor;
      this.aD = descriptionProcessor;
      this.aE = nbtProcessor;
      this.aF = potionProcessor;
      this.az = profile;
   }

   @Generated
   public String b() {
      return this.ay;
   }

   @Generated
   public Object c() {
      return this.az;
   }

   @Generated
   public class_1792 d() {
      return this.aA;
   }

   @Generated
   public AttributeProcessor e() {
      return this.aB;
   }

   @Generated
   public EnchantmentProcessor f() {
      return this.aC;
   }

   @Generated
   public DescriptionProcessor g() {
      return this.aD;
   }

   @Generated
   public NBTProcessor h() {
      return this.aE;
   }

   @Generated
   public PotionProcessor i() {
      return this.aF;
   }

   @Generated
   public AnimationUtil j() {
      return this.aG;
   }

   @Generated
   public void a(double price) {
      this.aH = price;
   }

   @Generated
   public double k() {
      return this.aH;
   }

   @Generated
   public void a(boolean status) {
      this.aI = status;
   }

   @Generated
   public boolean l() {
      return this.aI;
   }

   public class_1799 a() {
      class_1799 stack = new class_1799(this.aA);
      if (this.az instanceof Integer && this.aF != null) {
         class_9331 class_9331Var = class_9334.field_49651;
         Optional optionalEmpty2 = Optional.empty();
         Optional optionalEmpty;
         if (this.az instanceof Integer integer) {
            optionalEmpty = Optional.of(integer);
         } else {
            optionalEmpty = Optional.empty();
         }

         stack.method_57379(
            class_9331Var,
            new class_1844(
               optionalEmpty2,
               optionalEmpty,
               this.aF.a().stream().map(effect -> new class_1293(effect.a(), effect.c(), effect.b() - 1)).toList(),
               Optional.empty()
            )
         );
      }

      if (this.az instanceof String string) {
         GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(string.getBytes()), this.name().toLowerCase(Locale.ROOT));
         gameProfile.getProperties().put("textures", new Property("textures", string));
         stack.method_57379(class_9334.field_49617, new class_9296(gameProfile));
      }

      return stack;
   }

   public boolean a(class_1799 stack) {
      return stack.method_31574(this.aA)
         && (this.aB == null || this.aB.a(stack))
         && (this.aC == null || this.aC.a(stack))
         && (this.aD == null || this.aD.a(stack))
         && (this.aE == null || this.aE.a(stack))
         && (this.aF == null || this.aF.a(stack));
   }
}
