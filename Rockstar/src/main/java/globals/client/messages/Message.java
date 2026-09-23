package globals.client.messages;


import rockstar.client.animation.*;
import globals.shared.proto.Packets;
import java.util.List;
import lombok.Generated;
import pyrock.utility.render.Rect;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;

public class Message {
   private final Packets.InternalType0451 author;
   private final String text;
   private final boolean self;
   private Rect rect = Rect.EMPTY;
   private Rect nameRect = Rect.EMPTY;
   private List<Message.InternalType0423> mentions = List.of();
   private Message.InternalType0020 layout;
   private float nameWidth = -1.0F;
   private static final Easing APPEAR = Easing.internalMethod05127(0.0, 0.55, 0.45, 1.0);
   private final AnimatedValue animation = new AnimatedValue(260L, APPEAR);

   @Generated
   public Packets.InternalType0451 author() {
      return this.author;
   }

   @Generated
   public String text() {
      return this.text;
   }

   @Generated
   public boolean self() {
      return this.self;
   }

   @Generated
   public Rect rect() {
      return this.rect;
   }

   @Generated
   public Rect nameRect() {
      return this.nameRect;
   }

   @Generated
   public List<Message.InternalType0423> mentions() {
      return this.mentions;
   }

   @Generated
   public Message.InternalType0020 layout() {
      return this.layout;
   }

   @Generated
   public float nameWidth() {
      return this.nameWidth;
   }

   @Generated
   public AnimatedValue animation() {
      return this.animation;
   }

   @Generated
   public Message(Packets.InternalType0451 localValue1, String localValue2, boolean localValue3) {
      this.author = localValue1;
      this.text = localValue2;
      this.self = localValue3;
   }

   @Generated
   public Message rect(Rect localValue1) {
      this.rect = localValue1;
      return this;
   }

   @Generated
   public Message nameRect(Rect localValue1) {
      this.nameRect = localValue1;
      return this;
   }

   @Generated
   public Message mentions(List<Message.InternalType0423> localValue1) {
      this.mentions = localValue1;
      return this;
   }

   @Generated
   public Message layout(Message.InternalType0020 localValue1) {
      this.layout = localValue1;
      return this;
   }

   @Generated
   public Message nameWidth(float localValue1) {
      this.nameWidth = localValue1;
      return this;
   }

   public static final class InternalType0020 {
      private final float maxWidth;
      private final List<String> lines;
      private final float width;

      public InternalType0020(float localValue1, List<String> localValue2, float localValue3) {
         this.maxWidth = localValue1;
         this.lines = localValue2;
         this.width = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0020[maxWidth=" + this.maxWidth() + ", lines=" + this.lines() + ", width=" + this.width() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.maxWidth());
         result = 31 * result + java.util.Objects.hashCode(this.lines());
         result = 31 * result + java.util.Objects.hashCode(this.width());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         Message.InternalType0020 other = (Message.InternalType0020) localValue1;
         return java.util.Objects.equals(this.maxWidth(), other.maxWidth())
            && java.util.Objects.equals(this.lines(), other.lines())
            && java.util.Objects.equals(this.width(), other.width());
      }

      public float maxWidth() {
         return this.maxWidth;
      }

      public List<String> lines() {
         return this.lines;
      }

      public float width() {
         return this.width;
      }
   }

   public static final class InternalType0423 {
      private final String username;
      private final Rect rect;

      public InternalType0423(String localValue1, Rect localValue2) {
         this.username = localValue1;
         this.rect = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0423[username=" + this.username() + ", rect=" + this.rect() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.username());
         result = 31 * result + java.util.Objects.hashCode(this.rect());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         Message.InternalType0423 other = (Message.InternalType0423) localValue1;
         return java.util.Objects.equals(this.username(), other.username())
            && java.util.Objects.equals(this.rect(), other.rect());
      }

      public String username() {
         return this.username;
      }

      public Rect rect() {
         return this.rect;
      }
   }
}
