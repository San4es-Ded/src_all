package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Packet;
import aethereal.core.Westra;
import aethereal.event.BackendEvent;
import aethereal.event.TickEvent;
import aethereal.util.ServerUtil;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.class_1533;
import net.minecraft.class_1799;
import net.minecraft.class_1806;
import net.minecraft.class_1937;
import net.minecraft.class_22;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_3620;
import net.minecraft.class_3965;
import net.minecraft.class_2350.class_2351;

@ModuleRegister(
   a = "Captcha Solver",
   b = "Автоматически решает капчу при входе на сервер FunTime",
   c = Category.Player
)
public class CaptchaSolver extends Module {
   private byte[] b;

   @EventTarget
   public void a(TickEvent event) {
      if (ServerUtil.a.c() && Westra.h().f().g() && aM_.field_1765 instanceof class_3965 class_3965Var) {
         aM_.field_1687
            .method_8390(
               class_1533.class, new class_238(class_3965Var.method_17777()).method_1014(0.5), frame -> frame.method_6940().method_7909() instanceof class_1806
            )
            .stream()
            .findFirst()
            .ifPresent(this::a);
      }
   }

   @EventTarget
   public void a(BackendEvent event) {
      Packet packet = event.d();
      String code;
      if (event.b() && "captcha".equals(packet.b()) && (code = packet.a().a(packet.c(), "code")) != null) {
         aM_.field_1724.field_3944.method_45729(code);
      }
   }

   private void a(class_1533 origin) {
      class_1937 world = origin.method_37908();
      class_2350 facing = origin.method_5735();
      boolean alongZ = facing.method_10166() == class_2351.field_11051;
      boolean flip = facing == class_2350.field_11043 || facing == class_2350.field_11034;
      List<class_1533> frames = world.method_8390(
         class_1533.class,
         origin.method_5829().method_1014(16.0),
         frame -> frame.method_5735() == facing
            && frame.method_6940().method_7909() instanceof class_1806
            && class_1806.method_8001(frame.method_6940(), world) != null
      );
      int minU = Integer.MAX_VALUE;
      int maxU = Integer.MIN_VALUE;
      int minV = Integer.MAX_VALUE;
      int maxV = Integer.MIN_VALUE;
      Iterator<class_1533> it = frames.iterator();

      while (it.hasNext()) {
         class_2338 pos = it.next().method_59940();
         int u = alongZ ? pos.method_10263() : pos.method_10260();
         minU = Math.min(minU, u);
         maxU = Math.max(maxU, u);
         minV = Math.min(minV, pos.method_10264());
         maxV = Math.max(maxV, pos.method_10264());
      }

      int width = (maxU - minU + 1) * 128;
      int height = (maxV - minV + 1) * 128;
      BufferedImage image = new BufferedImage(width, height, 2);
      Graphics2D graphics = image.createGraphics();

      for (class_1533 frame2 : frames) {
         class_1799 stack = frame2.method_6940();
         class_22 state = class_1806.method_8001(stack, world);
         if (state != null && state.field_122 != null && state.field_122.length == 16384) {
            BufferedImage tile = new BufferedImage(128, 128, 2);

            for (int i3 = 0; i3 < state.field_122.length; i3++) {
               int raw = state.field_122[i3] & 255;
               int color = raw < 4 ? 0 : class_3620.method_38480(raw);
               tile.setRGB(i3 % 128, i3 / 128, color);
            }

            class_2338 pos2 = frame2.method_59940();
            int u2 = alongZ ? pos2.method_10263() : pos2.method_10260();
            int i;
            int i2;
            if (flip) {
               i = maxU;
               i2 = u2;
            } else {
               i = u2;
               i2 = minU;
            }

            int x = (i - i2) * 128;
            int y = (maxV - pos2.method_10264()) * 128;
            int rotation = frame2.method_6934() & 3;
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
            transform.rotate(Math.toRadians(rotation * 90.0), 64.0, 64.0);
            graphics.drawImage(tile, transform, (ImageObserver)null);
         }
      }

      graphics.dispose();
      this.a(image);
   }

   private void a(BufferedImage image) {
      try {
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         ImageIO.write(image, "png", stream);
         byte[] bytes = stream.toByteArray();
         if (!Arrays.equals(bytes, this.b)) {
            this.b = bytes;
            Westra.h().f().a(false, "captcha", "bytes", Base64.getEncoder().encodeToString(bytes));
         }
      } catch (Exception var4) {
      }
   }
}
