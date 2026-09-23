package rockstar.client.internal.ui;




import rockstar.client.i18n.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import globals.shared.proto.Packets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RemoteInventoryScreen extends Screen {
   private final String internalField0248;
   private final String internalField0247;
   private final String internalField1077;
   private final String internalField1076;
   private String internalField1079;
   private AdminInventorySnapshot internalField0467;
   private int internalField1054;

   private RemoteInventoryScreen(String localValue1, String localValue2, String localValue3, String localValue4) {
      super(Text.literal("invsee"));
      this.internalField0248 = localValue1;
      this.internalField0247 = localValue2;
      this.internalField1077 = localValue3;
      this.internalField1076 = localValue4;
   }

   public static void internalMethod02744(String localValue0, String localValue1, String localValue2, String localValue3) {
      MinecraftClient localValue4 = MinecraftClient.getInstance();
      localValue4.send(() -> localValue4.setScreen(new RemoteInventoryScreen(localValue0, localValue1, localValue2, localValue3)));
   }

   public static void internalMethod02069(Packets.InternalType0498 localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      localValue1.execute(() -> {
         if (localValue1.currentScreen instanceof RemoteInventoryScreen localValue2) {
            if (localValue0.from().equalsIgnoreCase(localValue2.internalField0248) || localValue0.nickname().equalsIgnoreCase(localValue2.internalField0248)) {
               if (localValue2.internalField1079 == null) {
                  localValue2.internalField1079 = localValue0.from();
               } else if (!localValue2.internalField1079.equals(localValue0.from())) {
                  return;
               }

               AdminInventorySnapshot localValue4 = AdminInventorySnapshot.internalMethod06909(localValue0);
               if (localValue4 != null) {
                  localValue2.internalField0467 = localValue4;
               }
            }
         }
      });
   }

   public void init() {
      this.internalMethod01051(0, 0, "");
   }

   public void tick() {
      this.internalField1054++;
      if (this.internalField1054 % 10 == 0) {
         this.internalMethod01051(0, 0, "");
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      int localValue5 = (this.width - 176) / 2;
      int localValue6 = (this.height - 166) / 2;
      context.drawTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, HandledScreen.BACKGROUND_TEXTURE, localValue5, localValue6, 0.0F, 0.0F, 176, 166, 256, 256);
      String localValue7 = this.internalField0467 != null ? this.internalField0467.internalMethod05975() : this.internalField0248;
      String localValue8 = this.internalField1079 != null && !this.internalField1079.equalsIgnoreCase(localValue7) ? localValue7 + " (" + this.internalField1079 + ")" : localValue7;
      context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(localValue8), this.width / 2, localValue6 - 12, -1);
      this.internalMethod02192(context, localValue5, localValue6, mouseX, mouseY);
      if (this.internalField0467 == null) {
         MutableText localValue13 = Text.literal(
               LanguageManager.internalMethod07214(this.internalField1054 > 200 ? "commands.admin.invsee.timeout" : "commands.admin.invsee.waiting")
            )
            .formatted(this.internalField1054 > 200 ? Formatting.RED : Formatting.GRAY);
         context.drawCenteredTextWithShadow(this.textRenderer, localValue13, this.width / 2, localValue6 + 166 + 6, -1);
      } else {
         for (int localValue9 = 0; localValue9 < 46; localValue9++) {
            int localValue10 = internalMethod06708(localValue9);
            if (localValue10 != Integer.MIN_VALUE) {
               this.internalMethod02366(context, this.internalField0467.internalMethod01242(localValue9), localValue5 + localValue10, localValue6 + internalMethod06756(localValue9));
            }
         }

         int localValue12 = this.internalMethod01875(mouseX - localValue5, mouseY - localValue6);
         if (localValue12 >= 0) {
            context.fill(
               localValue5 + internalMethod06708(localValue12),
               localValue6 + internalMethod06756(localValue12),
               localValue5 + internalMethod06708(localValue12) + 16,
               localValue6 + internalMethod06756(localValue12) + 16,
               -2130706433
            );
         }

         ItemStack localValue14 = this.internalField0467.internalMethod03217();
         if (!localValue14.isEmpty()) {
            context.drawItem(localValue14, mouseX - 8, mouseY - 8);
            context.drawStackOverlay(this.textRenderer, localValue14, mouseX - 8, mouseY - 8);
         } else if (localValue12 >= 0) {
            ItemStack localValue11 = this.internalField0467.internalMethod01242(localValue12);
            if (!localValue11.isEmpty()) {
               context.drawItemTooltip(this.textRenderer, localValue11, mouseX, mouseY);
            }
         }
      }
   }

   private void internalMethod02192(DrawContext localValue1, int localValue2, int localValue3, int localValue4, int localValue5) {
      if (this.client != null && this.client.world != null) {
         String localValue6 = this.internalField0467 != null ? this.internalField0467.internalMethod05975() : this.internalField0248;

         for (AbstractClientPlayerEntity localValue8 : this.client.world.getPlayers()) {
            if (localValue8.getName().getString().equalsIgnoreCase(localValue6)) {
               InventoryScreen.drawEntity(localValue1, localValue2 + 26, localValue3 + 8, localValue2 + 75, localValue3 + 78, 30, 0.0625F, localValue4, localValue5, localValue8);
               return;
            }
         }
      }
   }

   private void internalMethod02366(DrawContext localValue1, ItemStack localValue2, int localValue3, int localValue4) {
      if (!localValue2.isEmpty()) {
         localValue1.drawItem(localValue2, localValue3, localValue4);
         localValue1.drawStackOverlay(this.textRenderer, localValue2, localValue3, localValue4);
      }
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      double mouseX = click.x();
      double mouseY = click.y();
      int button = click.button();
      if (this.internalField0467 != null && (button == 0 || button == 1)) {
         int localValue6 = (this.width - 176) / 2;
         int localValue7 = (this.height - 166) / 2;
         int localValue8 = this.internalMethod01875((int)mouseX - localValue6, (int)mouseY - localValue7);
         if (localValue8 < 0) {
            if (!this.internalField0467.internalMethod03217().isEmpty()) {
               this.internalMethod01051(-999, button, SlotActionType.PICKUP.name());
            }

            return true;
         } else {
            this.internalMethod01051(localValue8, button, click.hasShift() ? SlotActionType.QUICK_MOVE.name() : SlotActionType.PICKUP.name());
            return true;
         }
      } else {
         return super.mouseClicked(click, doubled);
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      int keyCode = input.key();
      if (this.client == null) {
         return super.keyPressed(input);
      } else if (!input.isEscape() && !this.client.options.inventoryKey.matchesKey(input)) {
         if (this.internalField0467 == null) {
            return super.keyPressed(input);
         } else {
            int localValue4 = (int)(this.client.mouse.getX() * this.client.getWindow().getScaledWidth() / this.client.getWindow().getWidth());
            int localValue5 = (int)(this.client.mouse.getY() * this.client.getWindow().getScaledHeight() / this.client.getWindow().getHeight());
            int localValue6 = this.internalMethod01875(localValue4 - (this.width - 176) / 2, localValue5 - (this.height - 166) / 2);
            if (localValue6 < 0) {
               return super.keyPressed(input);
            } else if (this.client.options.dropKey.matchesKey(input)) {
               this.internalMethod01051(localValue6, input.hasCtrlOrCmd() ? 1 : 0, SlotActionType.THROW.name());
               return true;
            } else {
               for (int localValue7 = 0; localValue7 < this.client.options.hotbarKeys.length; localValue7++) {
                  if (this.client.options.hotbarKeys[localValue7].matchesKey(input)) {
                     this.internalMethod01051(localValue6, localValue7, SlotActionType.SWAP.name());
                     return true;
                  }
               }

               return super.keyPressed(input);
            }
         }
      } else {
         this.close();
         return true;
      }
   }

   private void internalMethod01051(int localValue1, int localValue2, String localValue3) {
      RockstarClient.getInstance()
         .internalMethod06050()
         .send(new Packets.InternalType0085(this.internalField0248, this.internalField0247, this.internalField1077, this.internalField1076, localValue1, localValue2, localValue3));
   }

   private int internalMethod01875(int localValue1, int localValue2) {
      for (int localValue3 = 0; localValue3 < 46; localValue3++) {
         int localValue4 = internalMethod06708(localValue3);
         if (localValue4 != Integer.MIN_VALUE) {
            int localValue5 = internalMethod06756(localValue3);
            if (localValue1 >= localValue4 && localValue1 < localValue4 + 16 && localValue2 >= localValue5 && localValue2 < localValue5 + 16) {
               return localValue3;
            }
         }
      }

      return -1;
   }

   private static int internalMethod06708(int localValue0) {
      if (localValue0 == 0) {
         return 154;
      } else if (localValue0 <= 4) {
         return 98 + (localValue0 - 1) % 2 * 18;
      } else if (localValue0 <= 8) {
         return 8;
      } else if (localValue0 <= 35) {
         return 8 + (localValue0 - 9) % 9 * 18;
      } else if (localValue0 <= 44) {
         return 8 + (localValue0 - 36) * 18;
      } else {
         return localValue0 == 45 ? 77 : Integer.MIN_VALUE;
      }
   }

   private static int internalMethod06756(int localValue0) {
      if (localValue0 == 0) {
         return 28;
      } else if (localValue0 <= 4) {
         return 18 + (localValue0 - 1) / 2 * 18;
      } else if (localValue0 <= 8) {
         return 8 + (localValue0 - 5) * 18;
      } else if (localValue0 <= 35) {
         return 84 + (localValue0 - 9) / 9 * 18;
      } else if (localValue0 <= 44) {
         return 142;
      } else {
         return localValue0 == 45 ? 62 : Integer.MIN_VALUE;
      }
   }
}
