package wtf.wyvern.core.request;

import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import java.util.LinkedList;
import java.util.Queue;
import wtf.wyvern.core.events.impl.player.EventRotate;
import wtf.wyvern.core.events.impl.player.EventUpdate;

public class ScriptManager {
   private final Queue<ScriptTask> tasks = new LinkedList();

   public ScriptManager() {
      EventManager.register(this);
   }

   public void tick(Object event) {
      ScriptTask currentTask = (ScriptTask)this.tasks.peek();
      if (currentTask != null) {
         boolean finished = currentTask.tryTick(event);
         if (finished) {
            this.tasks.poll();
         }

      }
   }

   @EventTarget
   public void rotateTick(EventRotate tickRotate) {
      this.tick(tickRotate);
   }

   @EventTarget(0)
   public void updateTick(EventUpdate tickUpdate) {
      this.tick(tickUpdate);
   }

   public void addTask(ScriptTask task) {
      this.tasks.add(task);
   }

   public boolean isFinished() {
      return this.tasks.isEmpty();
   }

   public static class ScriptTask {
      private final Queue<Step<?>> steps = new LinkedList();
      private int idleTicks = 0;
      private int maxIdleTicks = 400;

      public ScriptTask withMaxIdleTicks(int maxIdleTicks) {
         this.maxIdleTicks = Math.max(1, maxIdleTicks);
         return this;
      }

      public <E> ScriptTask schedule(Class<E> eventClass, StepTask<E> action) {
         this.steps.add(new Step(eventClass, action));
         return this;
      }

      public boolean tryTick(Object event) {
         Step<?> nextStep = (Step)this.steps.peek();
         if (nextStep == null) {
            return true;
         } else {
            boolean progressed = false;
            if (nextStep.eventClass.isInstance(event)) {
               boolean stepDone = nextStep.execute(event);
               if (stepDone) {
                  this.steps.poll();
                  progressed = true;
               }
            }

            if (progressed) {
               this.idleTicks = 0;
               return this.steps.isEmpty();
            } else {
               ++this.idleTicks;
               if (this.idleTicks > this.maxIdleTicks) {
                  this.steps.clear();
                  return true;
               } else {
                  return false;
               }
            }
         }
      }

      public boolean isCompleted() {
         return this.steps.isEmpty();
      }

      private static class Step<E> {
         private final Class<E> eventClass;
         private final StepTask<E> action;

         Step(Class<E> eventClass, StepTask<E> action) {
            this.eventClass = eventClass;
            this.action = action;
         }

         boolean execute(Object event) {
            return this.action.accept(this.eventClass.cast(event));
         }
      }

      @FunctionalInterface
      public interface StepTask<E> {
         boolean accept(E var1);
      }
   }
}