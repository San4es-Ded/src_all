package ru.wild.automation;

import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.Address;
import net.minecraft.client.network.AllowedAddressResolver;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.text.Text;
import ru.wild.network.ProxyManager;
import ru.wild.util.text.ChatLogger;

public final class HeadlessBotConnector {
   private static final Pattern instance = Pattern.compile("[A-Za-z0-9_]{1,16}");

   private HeadlessBotConnector() {
   }

   public static boolean handle(String var0, String var1) {
      return handle(var0, var1, "");
   }

   public static boolean handle(String var0, String var1, String var30) {
      var0 = var0 == null ? "" : var0.trim();
      var1 = var1 == null ? "" : var1.trim();
      if (!instance.matcher(var0).matches()) {
         compute(var0.isEmpty() ? "?" : var0, "§cnickname must contain 1-16 latin letters, digits or '_'");
         return false;
      } else if (var1.isEmpty()) {
         compute(var0, "§cserver address is empty");
         return false;
      } else if (!HeadlessBotEngine.update(var0)) {
         compute(var0, "§ca bot with this nickname is already connecting or online");
         return false;
      } else {
         String var2 = var0;
         String var3 = var1;
         Thread var6 = new Thread(() -> {
            List<String> var7 = candidates(var30, HeadlessBotEngine.proxy(var2));
            int var8 = Math.min(var7.size(), 10);

            for (int var9 = 0; var9 < var8; var9++) {
               String var10 = var7.get(var9);
               long var11 = HeadlessBotEngine.process(var2, var3, var10);
               if (var11 < 0L) {
                  if (var9 == 0) {
                     HeadlessBotEngine.apply(var2);
                     compute(var2, "§ccould not start a new connection attempt");
                     return;
                  }

                  var11 = -1L;

                  for (int var12 = 0; var12 < 20 && var11 < 0L; var12++) {
                     try {
                        Thread.sleep(100L);
                     } catch (InterruptedException var13) {
                        Thread.currentThread().interrupt();
                        return;
                     }

                     var11 = HeadlessBotEngine.process(var2, var3, var10);
                  }

                  if (var11 < 0L) {
                     break;
                  }
               }

               attempt(var2, var3, var10, var11);
               HeadlessBotSession var14 = null;
               long var15 = System.currentTimeMillis();

               while (true) {
                  HeadlessBotEngine.ServerEntry var16 = HeadlessBotEngine.handle(var2);
                  if (var16 == null) {
                     break;
                  }

                  if (var16.state() == HeadlessBotEngine.Status.JOINED) {
                     HeadlessBotEngine.proxy(var2, var10);
                     return;
                  }

                  if (var16.bot() != null) {
                     var14 = var16.bot();
                  }

                  if (var16.state() == HeadlessBotEngine.Status.ERROR
                     || var16.state() == HeadlessBotEngine.Status.DISCONNECTED
                     || var16.state() == HeadlessBotEngine.Status.SAVED) {
                     break;
                  }

                  if (System.currentTimeMillis() - var15 > 20000L) {
                     break;
                  }

                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var17) {
                     Thread.currentThread().interrupt();
                     return;
                  }
               }

               if (var14 != null) {
                  HeadlessBotEngine.compute(var14);
               }

               if (var9 + 1 < var8) {
                  compute(var2, "§7прокси §f" + var10 + " §7не подошёл, пробую следующий ...");
               }
            }

            if (var8 > 1 || !var7.get(0).isEmpty()) {
               compute(var2, "§cне удалось войти ни через один прокси (попыток: " + var8 + ")");
            }
         }, "WildBot-" + var2);
         var6.setDaemon(true);
         var6.start();
         return true;
      }
   }

   private static List<String> candidates(String var0, String var1) {
      List<String> var2 = new ArrayList<>();
      if (var0 != null && !var0.isBlank()) {
         var2.add(var0.trim());
      }

      if (var1 != null && !var1.isBlank() && !var2.contains(var1.trim())) {
         var2.add(var1.trim());
      }

      for (String var3 : ProxyManager.pool()) {
         if (var3 != null && !var3.isBlank() && !var2.contains(var3)) {
            var2.add(var3);
         }
      }

      if (var2.isEmpty()) {
         var2.add("");
      }

      return var2;
   }

   private static void attempt(String var2, String var3, String var30, long var4) {
               HeadlessBotSession var4x = null;
               boolean var16 = false /* VF: Semaphore variable */;

               label185: {
                  label184: {
                     label183: {
                        label182: {
                           label181: {
                              label180: {
                                 label202: {
                                    label203: {
                                       try {
                                          var16 = true;
                                          if (!HeadlessBotEngine.handle(var2, var4, Thread.currentThread())) {
                                             var16 = false;
                                             break label185;
                                          }

                                          handle(var2, var4, "resolving " + var3 + " ...");
                                          ServerAddress var5 = ServerAddress.parse(var3);
                                          Optional var19 = AllowedAddressResolver.DEFAULT.resolve(var5);
                                          if (var19.isEmpty()) {
                                             String var21 = "cannot resolve address: " + var3;
                                             HeadlessBotEngine.handle(var2, var4, var21);
                                             handle(var2, var4, "§c" + var21);
                                             var16 = false;
                                             break label184;
                                          }

                                          if (!HeadlessBotEngine.process(var2, var4)) {
                                             var16 = false;
                                             break label183;
                                          }

                                          InetSocketAddress var20 = ((Address)var19.get()).getInetSocketAddress();
                                          String var8x = "resolved -> " + var20.getHostString() + ":" + var20.getPort() + ", connecting ...";
                                          HeadlessBotEngine.handle(var2, var4, HeadlessBotEngine.Status.CONNECTING, var8x);
                                          handle(var2, var4, var8x);
                                          boolean var9 = MinecraftClient.getInstance().options.shouldUseNativeTransport();
                                          ClientConnection var10 = new ClientConnection(NetworkSide.CLIENTBOUND);
                                          ProxyManager.attach(var10, var30.isEmpty() ? HeadlessBotEngine.proxy(var2) : var30);
                                          var4x = new HeadlessBotSession(var2, var10);
                                          ChannelFuture var11 = ClientConnection.connect(var20, var9, var10);

                                          while (!var11.awaitUninterruptibly(100L)) {
                                             if (Thread.currentThread().isInterrupted() || !HeadlessBotEngine.process(var2, var4)) {
                                                var11.cancel(true);
                                                var10.disconnect(Text.literal("Bot connection cancelled"));
                                                var16 = false;
                                                break label182;
                                             }
                                          }

                                          if (!var11.isSuccess()) {
                                             throw new IllegalStateException("TCP connection failed", var11.cause());
                                          }

                                          if (!HeadlessBotEngine.process(var2, var4)) {
                                             var10.disconnect(Text.literal("Bot connection cancelled"));
                                             var16 = false;
                                             break label181;
                                          }

                                          var10.connect(var5.getAddress(), var5.getPort(), new HeadlessBotLoginHandler(var10, var4x));
                                          if (!HeadlessBotEngine.handle(var2, var4, var4x)) {
                                             if (HeadlessBotEngine.process(var2, var4)) {
                                                String var12 = "connection closed before the login session became active";
                                                HeadlessBotEngine.handle(var2, var4, var12);
                                                handle(var2, var4, "§c" + var12);
                                             }

                                             HeadlessBotEngine.compute(var4x);
                                             var16 = false;
                                             break label180;
                                          }

                                          HeadlessBotEngine.handle(var2, var4, HeadlessBotEngine.Status.LOGIN, "Handshake sent, waiting for login ...");
                                          if (!HeadlessBotEngine.process(var2, var4)) {
                                             HeadlessBotEngine.compute(var4x);
                                             var16 = false;
                                             break label202;
                                          }

                                          var10.send(new LoginHelloC2SPacket(var2, handle(var2)));
                                          handle(var2, var4, "handshake sent, waiting for login ...");
                                          var16 = false;
                                          break label203;
                                       } catch (Throwable var17) {
                                          boolean var6x = HeadlessBotEngine.process(var2, var4);
                                          String var7x = "connect error: " + var17.getClass().getSimpleName() + ": " + handle(var17);
                                          if (var6x) {
                                             HeadlessBotEngine.handle(var2, var4, var7x);
                                             handle(var2, var4, "§c" + var7x);
                                          }

                                          if (var4x != null) {
                                             HeadlessBotEngine.compute(var4x);
                                          }

                                          var17.printStackTrace();
                                          var16 = false;
                                       } finally {
                                          if (var16) {
                                             HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                                             HeadlessBotEngine.handle(var2, var4);
                                          }
                                       }

                                       HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                                       HeadlessBotEngine.handle(var2, var4);
                                       return;
                                    }

                                    HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                                    HeadlessBotEngine.handle(var2, var4);
                                    return;
                                 }

                                 HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                                 HeadlessBotEngine.handle(var2, var4);
                                 return;
                              }

                              HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                              HeadlessBotEngine.handle(var2, var4);
                              return;
                           }

                           HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                           HeadlessBotEngine.handle(var2, var4);
                           return;
                        }

                        HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                        HeadlessBotEngine.handle(var2, var4);
                        return;
                     }

                     HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                     HeadlessBotEngine.handle(var2, var4);
                     return;
                  }

                  HeadlessBotEngine.process(var2, var4, Thread.currentThread());
                  HeadlessBotEngine.handle(var2, var4);
                  return;
               }

               HeadlessBotEngine.process(var2, var4, Thread.currentThread());
               HeadlessBotEngine.handle(var2, var4);
   }

   public static UUID handle(String var0) {
      return UUID.nameUUIDFromBytes(("OfflinePlayer:" + var0).getBytes(StandardCharsets.UTF_8));
   }

   static void process(String var0, String var1) {
      HeadlessBotEngine.compute(var0, var1);
      compute(var0, var1);
   }

   static void handle(String var0, long var1, String var3) {
      if (HeadlessBotEngine.compute(var0, var1)) {
         HeadlessBotEngine.process(var0, var1, var3);
         compute(var0, var3);
      }
   }

   public static void handle(HeadlessBotSession var0, String var1) {
      if (var0 != null && HeadlessBotEngine.resolve(var0)) {
         HeadlessBotEngine.compute(var0, var1);
         compute(var0.handle(), var1);
      }
   }

   private static void compute(String var0, String var1) {
      String var2 = "[WildBot] " + var0 + ": " + var1;
      System.out.println(var2.replaceAll("§.", ""));
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3 != null) {
         var3.execute(() -> {
            try {
               ChatLogger.handle("§7[Bot] §f" + var0 + " §7» " + var1);
            } catch (Throwable var3x) {
            }
         });
      }
   }

   private static String handle(Throwable var0) {
      String var1 = var0.getMessage();
      return var1 != null && !var1.isBlank() ? var1 : "no details";
   }
}
