package rockstar.client.internal.core;



import rockstar.client.module.*;
import rockstar.client.*;
import lombok.Generated;

public enum MenuCategoryTab {
   internalField0377("Combat", ModuleCategory.COMBAT, IconSprite.internalField0939, IconSprite.internalField1392),
   internalField0376("Movement", ModuleCategory.MOVEMENT, IconSprite.internalField0940, IconSprite.internalField1668),
   internalField1137("Visuals", ModuleCategory.VISUALS, IconSprite.internalField1395, IconSprite.internalField1669),
   internalField1139("Player", ModuleCategory.PLAYER, IconSprite.internalField1394, IconSprite.internalField1666),
   internalField1138("Other", ModuleCategory.OTHER, IconSprite.internalField1393, IconSprite.internalField1667);

   private final String internalField0248;
   private final ModuleCategory internalField0405;
   private final IconSprite internalField0939;
   private final IconSprite internalField0940;
   private SpriteAnimationPlayer internalField0944;

   @Generated
   public String internalMethod02856() {
      return this.internalField0248;
   }

   @Generated
   public ModuleCategory internalMethod03547() {
      return this.internalField0405;
   }

   @Generated
   public IconSprite internalMethod00605() {
      return this.internalField0939;
   }

   @Generated
   public IconSprite internalMethod01333() {
      return this.internalField0940;
   }

   @Generated
   public SpriteAnimationPlayer internalMethod00607() {
      return this.internalField0944;
   }

   @Generated
   private MenuCategoryTab(String localValue3, ModuleCategory localValue4, IconSprite localValue5, IconSprite localValue6) {
      this.internalField0248 = localValue3;
      this.internalField0405 = localValue4;
      this.internalField0939 = localValue5;
      this.internalField0940 = localValue6;
   }

   @Generated
   public void internalMethod03186(SpriteAnimationPlayer localValue1) {
      this.internalField0944 = localValue1;
   }
}
