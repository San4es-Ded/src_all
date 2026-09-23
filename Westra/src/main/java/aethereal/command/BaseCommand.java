package aethereal.command;

import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.util.KeyUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.class_2172;

public abstract class BaseCommand implements Interface {
   protected final String b = this.getClass().getAnnotation(Command.class).a();

   public abstract void a(LiteralArgumentBuilder<class_2172> var1);

   public final void a(CommandDispatcher<class_2172> dispatcher) {
      LiteralArgumentBuilder<class_2172> builder = CaseInsensitiveLiteral.a(this.b);
      this.a(builder);
      dispatcher.register(builder);
      EventManager.a(this);
   }

   protected LiteralArgumentBuilder<class_2172> a(String name) {
      return CaseInsensitiveLiteral.a(name);
   }

   protected RequiredArgumentBuilder<class_2172, String> b(String name) {
      return RequiredArgumentBuilder.argument(name, StringArgumentType.string());
   }

   protected RequiredArgumentBuilder<class_2172, String> c(String name) {
      return RequiredArgumentBuilder.argument(name, StringArgumentType.greedyString());
   }

   protected RequiredArgumentBuilder<class_2172, String> d(String name) {
      return RequiredArgumentBuilder.argument(name, reader -> {
         int start = reader.getCursor();

         while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
         }

         return reader.getString().substring(start, reader.getCursor());
      });
   }

   protected RequiredArgumentBuilder<class_2172, Integer> e(String name) {
      return RequiredArgumentBuilder.argument(name, IntegerArgumentType.integer());
   }

   protected RequiredArgumentBuilder<class_2172, Float> f(String name) {
      return RequiredArgumentBuilder.argument(name, FloatArgumentType.floatArg());
   }

   protected String a(CommandContext<class_2172> context, String name) {
      return StringArgumentType.getString(context, name);
   }

   protected int b(CommandContext<class_2172> context, String name) {
      return IntegerArgumentType.getInteger(context, name);
   }

   protected float c(CommandContext<class_2172> context, String name) {
      return FloatArgumentType.getFloat(context, name);
   }

   protected SuggestionProvider<class_2172> a() {
      return (context, builder) -> {
         if (aM_.field_1724.field_3944 == null) {
            return builder.buildFuture();
         } else {
            Stream streamFilter = aM_.field_1724
               .field_3944
               .method_2880()
               .stream()
               .map(entry -> entry.method_2966().getName())
               .filter(name -> name != null && name.toLowerCase().startsWith(builder.getRemainingLowerCase() == null ? "" : builder.getRemainingLowerCase()));
            streamFilter.forEach(s -> builder.suggest((String)s));
            return builder.buildFuture();
         }
      };
   }

   protected SuggestionProvider<class_2172> b() {
      return (context, builder) -> {
         for (KeyUtil key : KeyUtil.values()) {
            if (key != KeyUtil.UNKNOWN) {
               builder.suggest(key.name());
            }
         }

         return builder.buildFuture();
      };
   }

   protected <T> SuggestionProvider<class_2172> a(Supplier<Collection<T>> itemsSupplier, Function<T, String> mapper) {
      return (context, builder) -> {
         String remaining = builder.getRemainingLowerCase() == null ? "" : builder.getRemainingLowerCase();
         Iterator it = itemsSupplier.get().iterator();

         while (it.hasNext()) {
            String name = mapper.apply((T)it.next());
            if (name != null && name.toLowerCase().startsWith(remaining)) {
               builder.suggest(name);
            }
         }

         return builder.buildFuture();
      };
   }
}
