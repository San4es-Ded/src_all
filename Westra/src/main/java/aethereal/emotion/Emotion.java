package aethereal.emotion;

public enum Emotion {
   WAVE("Помахать", 3.6F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         float angle = (float)Math.sin(progress * 12.0F) * 0.6F;
         pose.j = -0.5F;
         pose.l = angle;
      }
   },
   SHY("Стеснение", 3.4F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         pose.a = 0.3F;
         pose.g = -0.5F;
         pose.j = -0.5F;
      }
   },
   DANCE("Танец", 4.2F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         float phase = (float)Math.sin(progress * 8.0F);
         pose.e = phase * 0.4F;
         pose.g = phase * 0.6F;
         pose.j = -phase * 0.6F;
      }
   },
   CLAP("Аплодисменты", 3.0F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         float clap = (float)Math.abs(Math.sin(progress * 14.0F)) * 0.4F;
         pose.g = -0.8F;
         pose.j = -0.8F;
         pose.h = clap;
         pose.k = -clap;
      }
   },
   BOW("Поклон", 4.2F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         float bow = (float)Math.sin(progress * Math.PI) * 0.8F;
         pose.d = bow;
         pose.a = bow * 0.5F;
      }
   },
   FACEPALM("Фейспалм", 3.4F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         pose.j = -1.6F;
         pose.k = -0.3F;
         pose.a = 0.4F;
      }
   },
   POINT("Указать", 2.6F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         pose.j = -1.4F;
      }
   },
   TWERK("Твёрк", 3.6F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         float twerk = (float)Math.abs(Math.sin(progress * 12.0F)) * 0.5F;
         pose.d = 0.4F;
         pose.e = twerk;
      }
   },
   SIT("Присесть", 4.0F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         pose.m = -1.4F;
         pose.p = -1.4F;
         pose.g = -0.3F;
         pose.j = -0.3F;
      }
   },
   SALUTE("Отдать честь", 2.8F) {
      @Override
      public void a(EmotionPose pose, float progress) {
         pose.j = -2.2F;
         pose.k = -0.55F;
         pose.a = -0.1F;
      }
   };

   private final String a;
   private final float b;

   private Emotion(String name, float duration) {
      this.a = name;
      this.b = duration;
   }

   public String a() {
      return this.a;
   }

   public float b() {
      return this.b;
   }

   public abstract void a(EmotionPose var1, float var2);
}
