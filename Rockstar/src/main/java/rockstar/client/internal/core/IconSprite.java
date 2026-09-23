package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;

public enum IconSprite {
   internalField0939(IconAtlas.internalField0942),
   internalField0940(IconAtlas.internalField0942),
   internalField1395(IconAtlas.internalField0942),
   internalField1394(IconAtlas.internalField0942),
   internalField1393(IconAtlas.internalField0942),
   internalField1392(IconAtlas.internalField0941),
   internalField1668(IconAtlas.internalField0941),
   internalField1669(IconAtlas.internalField0941),
   internalField1666(IconAtlas.internalField0941),
   internalField1667(IconAtlas.internalField0941),
   internalField1662(IconAtlas.internalField1396),
   internalField1665(IconAtlas.internalField1396),
   internalField1664(IconAtlas.internalField1396),
   internalField1663(IconAtlas.internalField0942);

   private final IconAtlas internalField0942;
   public final float internalField0205;

   private IconSprite(IconAtlas localValue3) {
      this.internalField0942 = localValue3;
      this.internalField0205 = localValue3.internalField0205;
      localValue3.internalField0205 = localValue3.internalField0205 + localValue3.internalMethod08819();
   }

   @Generated
   public IconAtlas internalMethod03213() {
      return this.internalField0942;
   }

   @Generated
   public float internalMethod01651() {
      return this.internalField0205;
   }
}
