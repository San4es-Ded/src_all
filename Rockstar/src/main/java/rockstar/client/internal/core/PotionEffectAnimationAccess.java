package rockstar.client.internal.core;




import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.internal.script.RollingDigitLabel;
import rockstar.client.animation.AnimatedValue;

public interface PotionEffectAnimationAccess {
    public AnimatedValue rockstar$getAnimPotion();

    public RollingDigitLabel rockstar$getTimeAnimation();
}

