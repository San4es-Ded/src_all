package ru.haron.mixin.accessor;

import net.minecraft.client.session.Session;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor(value="session")
    public Session getSession();

    @Mutable
    @Accessor(value="session")
    public void setSession(Session var1);

    @Invoker(value="doItemUse")
    public void invokeDoItemUse();

    @Invoker(value="doAttack")
    public boolean invokeDoAttack();
}
