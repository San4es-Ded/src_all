package haron.modules.visuals;

import haron.audio.SoundPlayer;
import haron.events.CriticalHitEvent;
import haron.events.AttackTargetEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(a="Hit Sounds", b="Plays a sound when you hit a target.", c=ModuleCategory.VISUALS)
public class HitSounds
extends HaronModule {
    private final ModeSetting sound = new ModeSetting("Звук", new String[]{"Обычный", "Колокол", "Бонк", "Пузырь", "Поп", "Уву", "Стон"}, "Обычный");
    private final NumberSetting volume = new NumberSetting("Громкость", 1.0f, 0.1f, 2.0f, 0.1f);
    private final BooleanSetting onlyPlayers = new BooleanSetting("Только игроки", false);

    private void playSound(String string) {
        float f = ((Float)this.volume.k()).floatValue();
        switch (string) {
            case "Обычный": {
                SoundPlayer.playRandomHit(f);
                break;
            }
            case "Стон": {
                SoundPlayer.playRandomMoan(f);
                break;
            }
            case "Колокол": {
                SoundPlayer.play("bell", f);
                break;
            }
            case "Бонк": {
                SoundPlayer.play("bonk", f);
                break;
            }
            case "Пузырь": {
                SoundPlayer.play("bubble", f);
                break;
            }
            case "Поп": {
                SoundPlayer.play("poߋ", f);
                break;
            }
            case "Уву": {
                SoundPlayer.play("uwu", f);
                break;
            }
            default: {
                SoundPlayer.playRandomHit(f);
            }
        }
    }

    @EventHandler
    public void a(AttackTargetEvent dt813s2) {
        String string;
        if (dt813s2.getEntity() == null) {
            return;
        }
        if (!(this.onlyPlayers.get() && !(dt813s2.getEntity() instanceof PlayerEntity) || (string = this.sound.d()).equals("Обычный"))) {
            this.playSound(string);
        }
    }

    @EventHandler
    public void a(CriticalHitEvent bjxb112) {
        if (bjxb112.getEntity() == null) {
            return;
        }
        if (!this.onlyPlayers.get() || bjxb112.getEntity() instanceof PlayerEntity) {
            this.playSound(this.sound.d());
        }
    }
}

