package pulse.modules.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.net.ssl.SSLSocketFactory;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import pulse.client.MinecraftContext;
import pulse.events.ChatSendEvent;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;

@ModuleInfo(a = "Pulse IRC", b = "Чат с другими пользователями клиента. Напиши .irc <текст>", c = ModuleCategory.UTILITIES)
public class PulseIRC extends ClientModule implements MinecraftContext {
   private static final String SERVER = "irc.libera.chat";
   private static final int PORT = 6697;
   private static final String CHANNEL = "#pulse-client";
   private volatile Socket socket;
   private volatile PrintWriter out;
   private final AtomicBoolean joined = new AtomicBoolean(false);
   private final AtomicBoolean running = new AtomicBoolean(false);
   private final AtomicBoolean reconnecting = new AtomicBoolean(false);
   private final ConcurrentLinkedQueue<String> pendingMessages = new ConcurrentLinkedQueue<>();
   private String nick;

   public PulseIRC() {
      this.collectSettings();
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.nick = this.buildNick();
      this.running.set(true);
      this.joined.set(false);
      this.connect();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.running.set(false);
      this.joined.set(false);
      this.sendRaw("QUIT :Pulse Client");
      this.closeSocket();
   }

   @EventHandler(priority = 200)
   public void onChat(ChatSendEvent chatSendEvent) {
      String message = chatSendEvent.getMessage();
      if (message != null && message.startsWith(".irc")) {
         chatSendEvent.a();
         if (message.startsWith(".irc ")) {
            String strTrim = message.substring(5).trim();
            if (strTrim.isEmpty()) {
               return;
            }

            if (!this.isEnabled()) {
               this.printSystem("Модуль Pulse IRC выключен", 16733525);
            } else if (!this.joined.get()) {
               this.pendingMessages.offer(strTrim);
               this.printChat(this.nick, strTrim, true);
               this.connect();
            } else {
               this.sendRaw("PRIVMSG #pulse-client :" + strTrim);
               this.printChat(this.nick, strTrim, true);
            }
         }
      }
   }

   private void connect() {
      if (this.running.get() && !this.joined.get() && this.reconnecting.compareAndSet(false, true)) {
         Thread thread = new Thread(() -> {
            try {
               while (this.running.get() && !this.joined.get()) {
                  try {
                     Socket nextSocket = SSLSocketFactory.getDefault().createSocket();
                     nextSocket.connect(new InetSocketAddress("irc.libera.chat", 6697), 10000);
                     nextSocket.setSoTimeout(300000);
                     this.socket = nextSocket;
                     this.out = new PrintWriter(new OutputStreamWriter(nextSocket.getOutputStream(), StandardCharsets.UTF_8), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(nextSocket.getInputStream(), StandardCharsets.UTF_8));
                     this.sendRaw("NICK " + this.nick);
                     this.sendRaw("USER pulse 0 * :Pulse Client");

                     String line;
                     while (this.running.get() && (line = reader.readLine()) != null) {
                        this.handleLine(line);
                     }
                  } catch (Exception var15) {
                  } finally {
                     this.joined.set(false);
                     this.closeSocket();
                  }

                  if (this.running.get()) {
                     try {
                        Thread.sleep(3000L);
                     } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        break;
                     }
                  }
               }
            } finally {
               this.reconnecting.set(false);
               if (this.running.get() && !this.joined.get()) {
                  this.connect();
               }
            }
         }, "Pulse-IRC");
         thread.setDaemon(true);
         thread.start();
      }
   }

   private void flushPending() {
      String message;
      while (this.joined.get() && (message = this.pendingMessages.poll()) != null) {
         this.sendRaw("PRIVMSG #pulse-client :" + message);
      }
   }

   private void closeSocket() {
      try {
         if (this.socket != null) {
            this.socket.close();
         }
      } catch (Exception var2) {
      }

      this.socket = null;
      this.out = null;
   }

   private void handleLine(String str) {
      if (str.startsWith("PING ")) {
         this.sendRaw("PONG " + str.substring(5));
      }

      String strSubstring3 = "";
      String strSubstring = "";
      String strTrim = str;
      if (str.startsWith(":")) {
         int iIndexOf = str.indexOf(32);
         if (iIndexOf < 0) {
            return;
         }

         strSubstring3 = str.substring(1, iIndexOf);
         strTrim = str.substring(iIndexOf + 1).trim();
      }

      int iIndexOf2 = strTrim.indexOf(" :");
      if (iIndexOf2 >= 0) {
         strSubstring = strTrim.substring(iIndexOf2 + 2);
         strTrim = strTrim.substring(0, iIndexOf2).trim();
      }

      String[] strArrSplit = strTrim.split(" ", 2);
      String str3 = strArrSplit[0];
      String str2 = strArrSplit.length > 1 ? strArrSplit[1] : "";
      String strSubstring2 = strSubstring3.contains("!") ? strSubstring3.substring(0, strSubstring3.indexOf(33)) : strSubstring3;
      switch (str3) {
         case "001":
            this.sendRaw("JOIN #pulse-client");
            break;
         case "433":
            this.nick = this.buildNick();
            this.sendRaw("NICK " + this.nick);
            break;
         case "366":
            this.joined.set(true);
            this.flushPending();
            this.printSystem("Подключён · #pulse-client", 5635925);
            break;
         case "PRIVMSG":
            if (this.joined.get() && !strSubstring2.equalsIgnoreCase(this.nick) && str2.trim().equalsIgnoreCase("#pulse-client")) {
               this.printChat(strSubstring2, strSubstring, false);
            }
            break;
         case "KICK":
            if ((str2.contains(" ") ? str2.split(" ")[1] : str2).equalsIgnoreCase(this.nick)) {
               this.joined.set(false);
               this.printSystem("Кикнут из #pulse-client. Переподключение...", 16755200);

               try {
                  Thread.sleep(3000L);
               } catch (InterruptedException var13) {
                  if (this.running.get()) {
                     this.sendRaw("JOIN #pulse-client");
                  }
               }
            }
      }
   }

   private void printChat(String sender, String message, boolean self) {
      Text line = Text.literal("")
         .append(lit("[IRC] ", 8146431))
         .append(lit("<" + sender + "> ", self ? 10395294 : 14737632))
         .append(lit(message, 16777215));
      c.execute(() -> {
         if (c.player != null) {
            c.player.sendMessage(line, false);
         }
      });
   }

   private void printSystem(String message, int color) {
      Text line = Text.literal("").append(lit("[IRC] ", 8146431)).append(lit(message, color));
      c.execute(() -> {
         if (c.player != null) {
            c.player.sendMessage(line, false);
         }
      });
   }

   private static Text lit(String str, int i) {
      return Text.literal(str).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(i)));
   }

   private void sendRaw(String str) {
      PrintWriter printWriter = this.out;
      if (printWriter != null) {
         printWriter.println(str);
      }
   }

   private String buildNick() {
      String playerName = c.player != null ? c.player.getName().getString() : "Pulse";
      String clean = playerName.replaceAll("[^a-zA-Z0-9_]", "");
      if (clean.isEmpty()) {
         clean = "Pulse";
      }

      if (clean.length() > 7) {
         clean = clean.substring(0, 7);
      }

      String suffix = Long.toUnsignedString(System.nanoTime(), 36);
      if (suffix.length() > 7) {
         suffix = suffix.substring(suffix.length() - 7);
      }

      return clean + "_" + suffix;
   }
}
