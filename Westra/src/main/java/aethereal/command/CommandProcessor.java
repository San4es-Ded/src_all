package aethereal.command;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.EventManager;
import aethereal.core.NativeMethodLookup;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2172;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_637;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class CommandProcessor extends BaseProcessor {
   @Generated
   private static final Logger_2 b = LoggerFactory.a(CommandProcessor.class);
   private final List<BaseCommand> d = new ArrayList<>();
   private final WayCommand e = new WayCommand();
   private final GPSCommand f = new GPSCommand();
   private final LayoutCommand g = new LayoutCommand();
   private final RCTCommand h = new RCTCommand();
   private final BlockESPCommand i = new BlockESPCommand();
   private final String k = ".";
   private final CommandDispatcher<class_2172> c = new CommandDispatcher(new CaseInsensitiveLiteral.a());
   private final class_637 j = new class_637((class_634)null, class_310.method_1551());

   @Compile
   @Override
   public void setup() {
      this.a(
         this.e,
         this.f,
         this.g,
         this.h,
         this.i,
         new AHCommand(),
         new MacrosCommand(),
         new FriendCommand(),
         new StaffCommand(),
         new WardenCommand(),
         new ConfigCommand(),
         new BindCommand(),
         new VClipCommand(),
         new HClipCommand(),
         new CCCommand(),
         new RecordCommand(),
         new AnhookCommand(),
         new FakePlayerCommand(),
         new AbCommand(),
         new TapiCommand(),
         new SchemCommand()
      );
   }

   @Generated
   public CommandDispatcher<class_2172> a() {
      return this.c;
   }

   @Generated
   public List<BaseCommand> b() {
      return this.d;
   }

   @Generated
   public WayCommand c() {
      return this.e;
   }

   @Generated
   public GPSCommand d() {
      return this.f;
   }

   @Generated
   public LayoutCommand e() {
      return this.g;
   }

   @Generated
   public RCTCommand f() {
      return this.h;
   }

   @Generated
   public BlockESPCommand g() {
      return this.i;
   }

   @Generated
   public class_637 h() {
      return this.j;
   }

   @Generated
   public String i() {
      return ".";
   }

   @Override
   public void unSetup() {
   }

   public void a(BaseCommand... commands) {
      for (BaseCommand command : commands) {
         this.d.add(command);
         command.a(this.c);
      }
   }

   public void a(String message, CallbackInfo ci) {
      if (!EventManager.d() && message != null && !message.isEmpty() && message.startsWith(this.i())) {
         String command = message.substring(this.i().length()).trim();
         if (!command.isEmpty()) {
            try {
               ParseResults<class_2172> results = this.c.parse(command, this.j);

               for (ParsedCommandNode<class_2172> parsed : results.getContext().getNodes()) {
                  if (parsed.getNode() instanceof LiteralCommandNode<class_2172> literal) {
                     int typedLength = parsed.getRange().getLength();
                     if (typedLength != literal.getLiteral().length()) {
                        return;
                     }
                  }
               }

               this.c.execute(results);
               ci.cancel();
            } catch (CommandSyntaxException var10) {
               System.out.println("Failure command: " + var10.getMessage());
            } catch (RuntimeException var11) {
               System.out.println("Failure command: " + var11.getMessage());
            }
         }
      }
   }

   public static <T> RequiredArgumentBuilder<class_2172, T> a(String name, ArgumentType<T> type) {
      return RequiredArgumentBuilder.argument(name, type);
   }

   static {
      NativeMethodLookup.lookup(CommandProcessor.class, 22);
   }
}
