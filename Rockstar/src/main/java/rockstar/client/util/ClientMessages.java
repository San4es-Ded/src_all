package rockstar.client.util;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import pyrock.utility.render.ColorRGBA;

public final class ClientMessages implements MinecraftClientAccess {
   private static final Text internalField0125 = Text.literal("[%s]".formatted("NoName"))
      .styled(localValue0 -> localValue0.withColor(new ColorRGBA(140.0F, 80.0F, 255.0F).getRGB()));

   public static void internalMethod04056(ClientMessages.InternalType0214 localValue0, Text localValue1) {
      internalMethod01535(localValue0, localValue1, true);
   }

   public static void internalMethod01809(Text localValue0) {
      if (internalField0149.player != null) {
         internalMethod01535(ClientMessages.InternalType0214.internalField1130, localValue0, false);
      }
   }

   public static void internalMethod03058(Text localValue0) {
      internalMethod01535(ClientMessages.InternalType0214.internalField0349, localValue0, false);
   }

   public static void internalMethod09025(Text localValue0) {
      internalMethod01535(ClientMessages.InternalType0214.internalField0350, localValue0, false);
   }

   private static void internalMethod01535(ClientMessages.InternalType0214 localValue0, Text localValue1, boolean localValue2) {
      if (internalField0149.player != null) {
         MutableText localValue3 = Text.literal("").append(localValue1.copy()).styled(localValue1x -> localValue1x.withColor(localValue0.internalMethod01141().getRGB()));
         internalField0149.player.sendMessage(internalField0125.copy().append(" ").append(localValue3), localValue2);
      }
   }

   public static void internalMethod07664(Text localValue0) {
      if (internalField0149.player != null) {
         internalField0149.player.sendMessage(localValue0, false);
      }
   }

   @Generated
   private ClientMessages() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum InternalType0214 {
      internalField0349("Warning", new ColorRGBA(247.0F, 206.0F, 59.0F)),
      internalField0350("Error", new ColorRGBA(242.0F, 79.0F, 68.0F)),
      internalField1130("Info", new ColorRGBA(87.0F, 126.0F, 255.0F));

      private final String internalField0248;
      private final ColorRGBA internalField0777;

      @Generated
      public String internalMethod04603() {
         return this.internalField0248;
      }

      @Generated
      public ColorRGBA internalMethod01141() {
         return this.internalField0777;
      }

      @Generated
      private InternalType0214(String localValue3, ColorRGBA localValue4) {
         this.internalField0248 = localValue3;
         this.internalField0777 = localValue4;
      }
   }
}
