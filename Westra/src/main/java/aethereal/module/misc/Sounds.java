package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.io.BufferedInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import lombok.Generated;
import net.minecraft.class_2960;

@ModuleRegister(
   a = "Sounds",
   b = "Воспроизводит выбранные звуки при определённых игровых событиях",
   c = Category.Misc
)
public class Sounds extends Module {
   private final ModeSetting b = new ModeSetting("Звук для воспроизведения", "Тип 1", "Тип 1", "Тип 2", "Тип 3", "Тип 4").a(selected -> this.a(this.e(true)));
   private final SliderSetting c = new SliderSetting("Громкость воспроизведения", 0.25F, 0.0F, 1.0F, 0.05F);
   private final ExecutorService d = Executors.newSingleThreadExecutor(runnable -> {
      Thread thread = new Thread(runnable, "ClientSound-Thread");
      thread.setDaemon(true);
      return thread;
   });

   @Generated
   public SliderSetting q() {
      return this.c;
   }

   public Sounds() {
      this.a(new Setting[]{this.b, this.c});
   }

   public void d(boolean active) {
      if (this.m()) {
         this.a(this.e(active));
      }
   }

   public void a(String filename) {
      if (filename != null && !filename.isEmpty() && aM_.method_1478() != null) {
         this.d
            .execute(
               () -> {
                  try {
                     AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                        new BufferedInputStream(aM_.method_1478().open(class_2960.method_60655("westra", "sounds/" + filename)))
                     );
                     Clip clip = AudioSystem.getClip();
                     clip.open(audioStream);
                     if (clip.isControlSupported(Type.MASTER_GAIN)) {
                        FloatControl gain = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), (float)(20.0 * Math.log10(Math.max(this.c.c(), 1.0E-4F))))));
                     }

                     clip.start();
                     clip.addLineListener(event -> {
                        if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                           clip.close();
                        }
                     });
                  } catch (Exception var5) {
                     var5.printStackTrace();
                  }
               }
            );
      }
   }

   public String e(boolean active) {
      String var2 = this.b.c();
      switch (var2) {
         case "Тип 1":
            return active ? "enable.wav" : "disable.wav";
         case "Тип 2":
            return active ? "enable1.wav" : "disable1.wav";
         case "Тип 3":
            return active ? "enable2.wav" : "disable2.wav";
         case "Тип 4":
            return active ? "enable3.wav" : "disable3.wav";
         default:
            return null;
      }
   }
}
