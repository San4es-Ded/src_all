package rockstar.client.notification;


import rockstar.client.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;

public enum NotificationType {
   internalField0704(
      "success",
      new ColorRGBA(237.0F, 255.0F, 249.0F),
      new ColorRGBA(98.0F, 255.0F, 0.0F),
      new ColorRGBA(171.0F, 255.0F, 132.0F),
      IconSprite.internalField1662
   ),
   internalField0705("error", ColorRGBA.RED, ColorRGBA.RED, ColorRGBA.RED, IconSprite.internalField1665),
   internalField1280(
      "info",
      new ColorRGBA(234.0F, 179.0F, 8.0F),
      new ColorRGBA(234.0F, 179.0F, 8.0F),
      new ColorRGBA(234.0F, 179.0F, 8.0F),
      IconSprite.internalField1664
   );

   private final String internalField0248;
   private final ColorRGBA internalField0777;
   private final ColorRGBA internalField0776;
   private final ColorRGBA internalField1311;
   private final IconSprite internalField0939;

   public static NotificationType internalMethod05735(String localValue0) {
      for (NotificationType localValue4 : values()) {
         if (localValue4.internalMethod07259().equalsIgnoreCase(localValue0)) {
            return localValue4;
         }
      }

      return internalField1280;
   }

   @Generated
   public String internalMethod07259() {
      return this.internalField0248;
   }

   @Generated
   public ColorRGBA internalMethod07136() {
      return this.internalField0777;
   }

   @Generated
   public ColorRGBA internalMethod03291() {
      return this.internalField0776;
   }

   @Generated
   public ColorRGBA internalMethod09009() {
      return this.internalField1311;
   }

   @Generated
   public IconSprite internalMethod01932() {
      return this.internalField0939;
   }

   @Generated
   private NotificationType(String localValue3, ColorRGBA localValue4, ColorRGBA localValue5, ColorRGBA localValue6, IconSprite localValue7) {
      this.internalField0248 = localValue3;
      this.internalField0777 = localValue4;
      this.internalField0776 = localValue5;
      this.internalField1311 = localValue6;
      this.internalField0939 = localValue7;
   }
}
