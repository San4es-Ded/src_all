package rockstar.client.internal.core;


import rockstar.client.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ExpiringTokenCache<T extends ExpiringToken> {
   private final ChangeListenerRegistry internalField0080 = new ChangeListenerRegistry();
   private final IoSupplier<T> internalField0073;
   private final Object internalField0290;
   private T internalField0867;

   public ExpiringTokenCache(IoSupplier<T> localValue1) {
      this(localValue1, new Object());
   }

   public ExpiringTokenCache(IoSupplier<T> localValue1, Object localValue2) {
      this.internalField0073 = localValue1;
      this.internalField0290 = localValue2;
   }

   public T internalMethod03112() {
      return this.internalField0867;
   }

   public boolean internalMethod04847() {
      return this.internalField0867 != null;
   }

   public boolean internalMethod04851() {
      return this.internalField0867 == null || this.internalField0867.internalMethod01680();
   }

   public T internalMethod03989() throws IOException {
      this.internalMethod09148();
      return this.internalField0867;
   }

   @SneakyThrows(IOException.class)
   public T internalMethod08952() {
      return this.internalMethod03989();
   }

   public CompletableFuture<T> internalMethod05880() {
      return CompletableFuture.supplyAsync(this::internalMethod08952);
   }

   public boolean internalMethod09148() throws IOException {
      synchronized (this.internalField0290) {
         if (this.internalMethod04851()) {
            this.internalMethod09103();
            return true;
         } else {
            return false;
         }
      }
   }

   @SneakyThrows(IOException.class)
   public boolean internalMethod09149() {
      return this.internalMethod09148();
   }

   public CompletableFuture<Boolean> internalMethod03036() {
      return CompletableFuture.supplyAsync(this::internalMethod09149);
   }

   public T internalMethod09103() throws IOException {
      synchronized (this.internalField0290) {
         this.internalMethod06986(this.internalField0073.get());
         return this.internalField0867;
      }
   }

   @SneakyThrows(IOException.class)
   public T internalMethod08307() {
      return this.internalMethod09103();
   }

   public CompletableFuture<T> internalMethod08628() {
      return CompletableFuture.supplyAsync(this::internalMethod08307);
   }

   @Internal
   public void internalMethod06986(T localValue1) {
      synchronized (this.internalField0290) {
         ExpiringToken localValue3 = this.internalField0867;
         this.internalField0867 = (T)localValue1;
         this.internalField0080.internalMethod07273(localValue3, localValue1);
      }
   }

   @Generated
   public ChangeListenerRegistry internalMethod04962() {
      return this.internalField0080;
   }
}
