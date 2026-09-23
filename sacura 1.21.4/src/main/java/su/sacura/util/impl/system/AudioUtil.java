 package su.sacura.util.impl.system;
 
 import java.io.ByteArrayInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.util.Optional;
 import javax.sound.sampled.AudioInputStream;
 import javax.sound.sampled.AudioSystem;
 import javax.sound.sampled.Clip;
 import javax.sound.sampled.FloatControl;
 import javax.sound.sampled.LineEvent;
 import net.minecraft.resource.Resource;
 import net.minecraft.util.Identifier;
 import su.sacura.Sacura;
 import su.sacura.features.modules.impl.display.ClientSoundsModule;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class AudioUtil implements MinecraftWrapper {
   public static void playSound(String name) {
     Identifier id = Identifier.of("mre", "sounds/" + name);
     mc.execute(() -> {
           try {
             Optional<Resource> resourceOpt = mc.getResourceManager().getResource(id);
             if (resourceOpt.isEmpty()) {
               System.err.println("Sound resource not found: " + String.valueOf(id));
               return;
             } 
             Resource resource = resourceOpt.get();
             InputStream in = resource.getInputStream();
             try {
               byte[] soundData = in.readAllBytes();
               ClientSoundsModule module = (ClientSoundsModule)Sacura.getInstance().getModuleManager().getModule(ClientSoundsModule.class);
               float volume = (module != null) ? (((Float)module.volume.get()).intValue() / 100.0F) : 1.0F;
               (new Thread(() -> playSoundFromBytes(soundData, volume))).start();
               if (in != null)
                 in.close(); 
             } catch (Throwable throwable) {
               if (in != null)
                 try {
                   in.close();
                 } catch (Throwable throwable1) {
                   throwable.addSuppressed(throwable1);
                 }  
               throw throwable;
             } 
           } catch (Exception e) {
             System.err.println("Error loading sound: " + e.getMessage());
           } 
         });
   }
   
   public static void playSoundFromFile(File file, float volume) {
     if (!file.exists()) {
       System.err.println("Sound file not found: " + file.getAbsolutePath());
       return;
     } 
     (new Thread(() -> {
           try {
             InputStream in = new FileInputStream(file);
             try {
               byte[] soundData = in.readAllBytes();
               playSoundFromBytes(soundData, volume);
               in.close();
             } catch (Throwable throwable) {
               try {
                 in.close();
               } catch (Throwable throwable1) {
                 throwable.addSuppressed(throwable1);
               } 
               throw throwable;
             } 
           } catch (Exception e) {
             System.err.println("Error playing sound from file: " + e.getMessage());
           } 
         })).start();
   }
   
   private static void playSoundFromBytes(byte[] soundData, float volume) {
     try {
       AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundData));
       try {
         Clip clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         try {
           FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
           float dB = (float)(20.0D * Math.log10(Math.max(1.0E-4D, volume)));
           gainControl.setValue(Math.min(Math.max(dB, gainControl.getMinimum()), gainControl.getMaximum()));
         } catch (IllegalArgumentException illegalArgumentException) {}
         clip.start();
         clip.addLineListener(e -> {
               if (e.getType() == LineEvent.Type.STOP)
                 clip.close(); 
             });
         if (audioInputStream != null)
           audioInputStream.close(); 
       } catch (Throwable throwable) {
         if (audioInputStream != null)
           try {
             audioInputStream.close();
           } catch (Throwable throwable1) {
             throwable.addSuppressed(throwable1);
           }  
         throw throwable;
       } 
     } catch (Exception e) {
       System.err.println("Error playing sound: " + e.getMessage());
     } 
   }
 }


