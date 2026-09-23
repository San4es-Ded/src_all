package aethereal.handler;

import aethereal.core.EventManager;

public class BaseHandler {
   public BaseHandler() {
      if (!this.getClass().isAnnotationPresent(Handler_2.class)) {
         throw new IllegalStateException("Обработчик " + this.getClass().getSimpleName() + " должен иметь @Handler!");
      } else {
         EventManager.a(this);
      }
   }
}
