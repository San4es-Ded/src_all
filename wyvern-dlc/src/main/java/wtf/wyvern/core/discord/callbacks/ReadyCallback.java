package wtf.wyvern.core.discord.callbacks;

import com.sun.jna.Callback;
import wtf.wyvern.core.discord.utils.DiscordUser;

public interface ReadyCallback extends Callback {
   void apply(DiscordUser var1);
}