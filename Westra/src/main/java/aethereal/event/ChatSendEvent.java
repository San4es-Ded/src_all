package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;

public class ChatSendEvent extends Event implements IEvent {
   private final String a;

   @Generated
   public ChatSendEvent(String content) {
      this.a = content;
   }

   @Generated
   public String b() {
      return this.a;
   }
}
