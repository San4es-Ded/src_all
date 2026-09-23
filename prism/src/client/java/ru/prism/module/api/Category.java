package ru.prism.module.api;


import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.satoshi.EaseInOutQuad;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    COMBAT("Combat","B"),
    VISUALS("Visuals","Q"),
    WORLD("World","H"),
    HUD("HUD","N"),
    UTILITIES("Utilities","L");
    private final String name;
    private final String icon;



    public ru.prism.utils.animation.satoshi.Animation alphaS = new EaseInOutQuad(300,1);
    public ru.prism.utils.animation.satoshi.Animation alphaS2 = new EaseInOutQuad(300,1);


    public Animation animation = new Animation();

}
