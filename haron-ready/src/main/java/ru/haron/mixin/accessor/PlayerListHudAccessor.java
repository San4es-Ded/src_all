package ru.haron.mixin.accessor;

import net.minecraft.text.Text;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerListHud.class)
public interface PlayerListHudAccessor {
    @Accessor(value="footer")
    public Text getFooter();

    @Accessor(value="visible")
    public void setVisible(boolean var1);

    @Accessor(value="visible")
    public boolean isVisible();

    @Accessor(value="header")
    public Text getHeader();
}
