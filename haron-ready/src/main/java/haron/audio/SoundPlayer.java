package haron.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public final class SoundPlayer {
    private static final String SOUND_DIR = "assets/haron/sounds/";
    private static final Random RANDOM = new Random();
    private static final String[] HIT_SOUNDS = new String[]{"hit1", "hit2", "hit3"};
    private static final String[] MOAN_SOUNDS = new String[]{"moan1", "moan2", "moan3", "moan4"};

    public static void play(String string, float f) {
        if (string == null || string.isBlank()) {
            SoundPlayer.playRandomHit(f);
        } else {
            SoundPlayer.playResource(SoundPlayer.$sf$0(string.toLowerCase(Locale.ROOT)), f);
        }
    }

    private static void applyVolume(Clip clip, float f) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl floatControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            floatControl.setValue(Math.max(floatControl.getMinimum(), Math.min((float)(20.0 * Math.log10(Math.max(0.01f, f))), floatControl.getMaximum())));
        }
    }

    private static void playResource(String string, float f) {
        Thread thread = new Thread(() -> {
            try {
                InputStream inputStream = SoundPlayer.class.getClassLoader().getResourceAsStream(string);
                if (inputStream == null) {
                    if (inputStream != null) {
                        inputStream.close();
                        return;
                    }
                    return;
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        SoundPlayer.applyVolume(clip, f);
                        clip.start();
                        clip.addLineListener(lineEvent -> {
                            if (lineEvent.getType() == LineEvent.Type.STOP) {
                                clip.close();
                            }
                        });
                        if (audioInputStream != null) {
                            audioInputStream.close();
                        }
                        bufferedInputStream.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    catch (Throwable throwable) {
                        if (audioInputStream != null) {
                            try {
                                audioInputStream.close();
                            }
                            catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                }
                catch (Throwable throwable) {
                    try {
                        bufferedInputStream.close();
                    }
                    catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                    throw throwable;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }, "Haron Sound Player");
        thread.setDaemon(true);
        thread.start();
    }

    public static void playRandomHit(float f) {
        SoundPlayer.playResource(SoundPlayer.$sf$0(HIT_SOUNDS[RANDOM.nextInt(HIT_SOUNDS.length)]), f);
    }

    public static void playRandomMoan(float f) {
        SoundPlayer.playResource(SoundPlayer.$sf$0(MOAN_SOUNDS[RANDOM.nextInt(MOAN_SOUNDS.length)]), f);
    }

    private SoundPlayer() {
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "assets/haron/sounds/" + string + ".wav";
    }
}

