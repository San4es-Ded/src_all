package wtf.wyvern.core.discord.utils;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import wtf.wyvern.core.discord.callbacks.DisconnectedCallback;
import wtf.wyvern.core.discord.callbacks.ErroredCallback;
import wtf.wyvern.core.discord.callbacks.JoinGameCallback;
import wtf.wyvern.core.discord.callbacks.JoinRequestCallback;
import wtf.wyvern.core.discord.callbacks.ReadyCallback;
import wtf.wyvern.core.discord.callbacks.SpectateGameCallback;

public class DiscordEventHandlers extends Structure {
   public DisconnectedCallback disconnected;
   public JoinRequestCallback joinRequest;
   public SpectateGameCallback spectateGame;
   public ReadyCallback ready;
   public ErroredCallback errored;
   public JoinGameCallback joinGame;

   protected List<String> getFieldOrder() {
      return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
   }

   public static class Builder {
      private final DiscordEventHandlers handlers = new DiscordEventHandlers();

      public DiscordEventHandlers build() {
         return this.handlers;
      }

      public Builder disconnected(DisconnectedCallback callback) {
         this.handlers.disconnected = callback;
         return this;
      }

      public Builder errored(ErroredCallback callback) {
         this.handlers.errored = callback;
         return this;
      }

      public Builder ready(ReadyCallback callback) {
         this.handlers.ready = callback;
         return this;
      }

      public Builder joinRequest(JoinRequestCallback callback) {
         this.handlers.joinRequest = callback;
         return this;
      }

      public Builder joinGame(JoinGameCallback callback) {
         this.handlers.joinGame = callback;
         return this;
      }

      public Builder spectateGame(SpectateGameCallback callback) {
         this.handlers.spectateGame = callback;
         return this;
      }
   }
}