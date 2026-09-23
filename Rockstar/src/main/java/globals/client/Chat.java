package globals.client;


import rockstar.client.internal.game.*;
import globals.client.messages.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import lombok.Generated;
import rockstar.client.internal.game.SmoothScrollHandler;

public class Chat {
   public static final int MAX_MESSAGES = 400;
   public static final int HISTORY_PAGE = 100;
   private final String name;
   private final SmoothScrollHandler scrollHandler = new SmoothScrollHandler();
   private final Object messagesLock = new Object();
   private final TreeMap<Long, Message> messages = new TreeMap<>();
   private volatile List<Long> idsCache = List.of();
   private volatile List<Message> messagesCache = List.of();
   private volatile List<Chat.InternalType0217> entriesCache = List.of();
   private volatile Set<Long> first = Set.of();
   private volatile long lastMessage;
   private volatile long lastMessageTime = 0L;
   private volatile boolean moreHistory = true;
   private volatile long historyRequestedAt = 0L;

   public Chat(String localValue1) {
      this.name = localValue1;
   }

   public List<Long> messageIdsSnapshot() {
      return this.idsCache;
   }

   public List<Message> messagesSnapshot() {
      return this.messagesCache;
   }

   public List<Chat.InternalType0217> entriesSnapshot() {
      return this.entriesCache;
   }

   public Message getMessage(long localValue1) {
      synchronized (this.messagesLock) {
         return this.messages.get(localValue1);
      }
   }

   public Message getLatestMessage() {
      synchronized (this.messagesLock) {
         return this.messages.isEmpty() ? null : this.messages.lastEntry().getValue();
      }
   }

   public long oldestId() {
      synchronized (this.messagesLock) {
         return this.messages.isEmpty() ? 0L : this.messages.firstKey();
      }
   }

   public int size() {
      synchronized (this.messagesLock) {
         return this.messages.size();
      }
   }

   public boolean hasMessage(long localValue1) {
      synchronized (this.messagesLock) {
         return this.messages.containsKey(localValue1);
      }
   }

   public boolean hasNonBlankMessage(long localValue1) {
      synchronized (this.messagesLock) {
         Message localValue4 = this.messages.get(localValue1);
         return localValue4 != null && !localValue4.text().isBlank();
      }
   }

   public boolean isFirstMessage(long localValue1) {
      return this.first.contains(localValue1);
   }

   public void appendMessage(long localValue1, Message localValue3) {
      synchronized (this.messagesLock) {
         this.messages.put(localValue1, localValue3);

         while (this.messages.size() > 400) {
            this.messages.pollFirstEntry();
            this.moreHistory = true;
         }

         this.rebuild();
      }
   }

   public void removeMessage(long localValue1) {
      synchronized (this.messagesLock) {
         if (this.messages.remove(localValue1) != null) {
            this.rebuild();
         }
      }
   }

   public void mergeHistory(Map<Long, Message> localValue1, boolean localValue2) {
      synchronized (this.messagesLock) {
         boolean localValue4 = false;
         List localValue5 = List.copyOf(localValue1.keySet());

         for (int localValue6 = localValue5.size() - 1; localValue6 >= 0; localValue6--) {
            if (this.messages.size() >= 400 && !this.messages.containsKey(localValue5.get(localValue6))) {
               localValue4 = true;
               break;
            }

            this.messages.putIfAbsent((Long)localValue5.get(localValue6), (Message)localValue1.get(localValue5.get(localValue6)));
         }

         this.moreHistory = localValue2 || localValue4;
         this.rebuild();
      }
   }

   public void scrollToBottom() {
      this.scrollHandler.internalMethod02328();
   }

   public void resetMessageAnimations() {
      synchronized (this.messagesLock) {
         for (Message localValue3 : this.messages.values()) {
            localValue3.animation().internalMethod02883();
         }
      }
   }

   private void rebuild() {
      HashSet localValue1 = new HashSet();
      ArrayList localValue2 = new ArrayList(this.messages.size());
      String localValue3 = null;

      for (Entry localValue5 : this.messages.entrySet()) {
         String localValue6 = ((Message)localValue5.getValue()).author().username();
         if (!localValue6.equals(localValue3)) {
            localValue1.add((Long)localValue5.getKey());
         }

         localValue3 = localValue6;
         localValue2.add(new Chat.InternalType0217((Long)localValue5.getKey(), (Message)localValue5.getValue()));
      }

      this.lastMessage = this.messages.isEmpty() ? 0L : this.messages.lastKey();
      this.first = Set.copyOf(localValue1);
      this.idsCache = List.copyOf(this.messages.keySet());
      this.messagesCache = List.copyOf(this.messages.values());
      this.entriesCache = List.copyOf(localValue2);
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public SmoothScrollHandler getScrollHandler() {
      return this.scrollHandler;
   }

   @Generated
   public long getLastMessage() {
      return this.lastMessage;
   }

   @Generated
   public void setLastMessage(long localValue1) {
      this.lastMessage = localValue1;
   }

   @Generated
   public long getLastMessageTime() {
      return this.lastMessageTime;
   }

   @Generated
   public void setLastMessageTime(long localValue1) {
      this.lastMessageTime = localValue1;
   }

   @Generated
   public boolean isMoreHistory() {
      return this.moreHistory;
   }

   @Generated
   public void setMoreHistory(boolean localValue1) {
      this.moreHistory = localValue1;
   }

   @Generated
   public long getHistoryRequestedAt() {
      return this.historyRequestedAt;
   }

   @Generated
   public void setHistoryRequestedAt(long localValue1) {
      this.historyRequestedAt = localValue1;
   }

   public static final class InternalType0217 {
      private final long id;
      private final Message message;

      public InternalType0217(long localValue1, Message localValue3) {
         this.id = localValue1;
         this.message = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0217[id=" + this.id() + ", message=" + this.message() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.id());
         result = 31 * result + java.util.Objects.hashCode(this.message());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         Chat.InternalType0217 other = (Chat.InternalType0217) localValue1;
         return java.util.Objects.equals(this.id(), other.id())
            && java.util.Objects.equals(this.message(), other.message());
      }

      public long id() {
         return this.id;
      }

      public Message message() {
         return this.message;
      }
   }
}
