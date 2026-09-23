package rockstar.client.internal.script;


import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;

public enum ThemeVariant {
   internalField0395(
      new ColorRGBA(255.0F, 255.0F, 255.0F),
      new ColorRGBA(12.0F, 12.0F, 12.0F),
      new ColorRGBA(24.0F, 24.0F, 27.0F),
      new ColorRGBA(32.0F, 32.0F, 32.0F),
      ColorRGBA.BLACK
   ),
   internalField0394(
      new ColorRGBA(10.0F, 10.0F, 10.0F),
      new ColorRGBA(229.0F, 229.0F, 229.0F),
      new ColorRGBA(255.0F, 255.0F, 255.0F),
      new ColorRGBA(32.0F, 32.0F, 32.0F),
      ColorRGBA.WHITE
   );

   private final ColorRGBA internalField0777;
   private final ColorRGBA internalField0776;
   private final ColorRGBA internalField1311;
   private final ColorRGBA internalField1312;
   private final ColorRGBA internalField1309;

   @Generated
   public ColorRGBA internalMethod01634() {
      return this.internalField0777;
   }

   @Generated
   public ColorRGBA internalMethod05867() {
      return this.internalField0776;
   }

   @Generated
   public ColorRGBA internalMethod08464() {
      return this.internalField1311;
   }

   @Generated
   public ColorRGBA internalMethod07702() {
      return this.internalField1312;
   }

   @Generated
   public ColorRGBA internalMethod08057() {
      return this.internalField1309;
   }

   @Generated
   private ThemeVariant(ColorRGBA localValue3, ColorRGBA localValue4, ColorRGBA localValue5, ColorRGBA localValue6, ColorRGBA localValue7) {
      this.internalField0777 = localValue3;
      this.internalField0776 = localValue4;
      this.internalField1311 = localValue5;
      this.internalField1312 = localValue6;
      this.internalField1309 = localValue7;
   }
}
