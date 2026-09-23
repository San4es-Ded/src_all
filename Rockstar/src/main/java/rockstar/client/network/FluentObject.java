package rockstar.client.network;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class FluentObject<T extends FluentObject<T>> {
   private final Map<String, List<String>> internalField0543 = new HashMap<>();

   public FluentObject() {
   }

   public FluentObject(Map<String, List<String>> localValue1) {
      localValue1.forEach((localValue1x, localValue2) -> this.internalField0543.put(localValue1x.toLowerCase(Locale.ROOT), new ArrayList<>(localValue2)));
   }

   public Map<String, List<String>> internalMethod02866() {
      return Collections.unmodifiableMap(
         this.internalField0543.entrySet().stream().collect(Collectors.toMap(Entry::getKey, localValue0 -> new ArrayList<>(localValue0.getValue())))
      );
   }

   public List<String> internalMethod05016(String localValue1) {
      return this.internalField0543.get(localValue1.toLowerCase());
   }

   public Optional<String> internalMethod04855(String localValue1) {
      List localValue2 = this.internalField0543.get(localValue1.toLowerCase(Locale.ROOT));
      return localValue2 != null && !localValue2.isEmpty() ? Optional.of((String)localValue2.get(0)) : Optional.empty();
   }

   public Optional<String> internalMethod01411(String localValue1) {
      List localValue2 = this.internalField0543.get(localValue1.toLowerCase(Locale.ROOT));
      return localValue2 != null && !localValue2.isEmpty() ? Optional.of((String)localValue2.get(localValue2.size() - 1)) : Optional.empty();
   }

   public T internalMethod05702(String localValue1, String localValue2) {
      this.internalField0543.computeIfAbsent(localValue1.toLowerCase(Locale.ROOT), localValue0 -> new ArrayList<>()).add(localValue2);
      return (T)this;
   }

   public T internalMethod06905(HttpHeader... localValue1) {
      for (HttpHeader localValue5 : localValue1) {
         this.internalMethod05702(localValue5.internalMethod02839(), localValue5.internalMethod04441());
      }

      return (T)this;
   }

   public T internalMethod07017(Collection<HttpHeader> localValue1) {
      for (HttpHeader localValue3 : localValue1) {
         this.internalMethod05702(localValue3.internalMethod02839(), localValue3.internalMethod04441());
      }

      return (T)this;
   }

   public T internalMethod01193(String localValue1, String localValue2) {
      ArrayList localValue3 = new ArrayList();
      localValue3.add(localValue2);
      this.internalField0543.put(localValue1.toLowerCase(Locale.ROOT), localValue3);
      return (T)this;
   }

   public T internalMethod04804(HttpHeader... localValue1) {
      for (HttpHeader localValue5 : localValue1) {
         this.internalMethod01193(localValue5.internalMethod02839(), localValue5.internalMethod04441());
      }

      return (T)this;
   }

   public T internalMethod06577(Collection<HttpHeader> localValue1) {
      for (HttpHeader localValue3 : localValue1) {
         this.internalMethod01193(localValue3.internalMethod02839(), localValue3.internalMethod04441());
      }

      return (T)this;
   }

   public T internalMethod02805(String localValue1) {
      this.internalField0543.remove(localValue1.toLowerCase(Locale.ROOT));
      return (T)this;
   }

   public T internalMethod06481() {
      this.internalField0543.clear();
      return (T)this;
   }

   public boolean internalMethod03322(String localValue1) {
      return this.internalField0543.containsKey(localValue1.toLowerCase(Locale.ROOT));
   }

   public boolean internalMethod03653(String localValue1, String localValue2) {
      return this.internalField0543.get(localValue1.toLowerCase(Locale.ROOT)).contains(localValue2);
   }

   public boolean internalMethod06384(HttpHeader localValue1) {
      return this.internalMethod03653(localValue1.internalMethod02839(), localValue1.internalMethod04441());
   }

   public T internalMethod01848(BiConsumer<String, String> localValue1) {
      for (Entry localValue3 : this.internalField0543.entrySet()) {
         for (String localValue5 : (Iterable<String>)(Iterable<?>)(List)localValue3.getValue()) {
            localValue1.accept((String)localValue3.getKey(), localValue5);
         }
      }

      return (T)this;
   }
}
