package rockstar.client.data;


import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonArrayNode extends JsonNode implements Iterable<JsonNode> {
   private final JsonArray internalField0368;

   public JsonArrayNode() {
      this(new JsonArray());
   }

   public JsonArrayNode(JsonArray localValue1) {
      super(localValue1);
      this.internalField0368 = localValue1;
   }

   public JsonArray internalMethod01532() {
      return this.internalField0368;
   }

   public JsonArrayNode internalMethod06917(@Nullable JsonElement localValue1) {
      this.internalField0368.add(localValue1);
      return this;
   }

   public JsonArrayNode internalMethod07220(@Nullable JsonNode localValue1) {
      this.internalField0368.add(localValue1 == null ? null : localValue1.internalMethod03947());
      return this;
   }

   public JsonArrayNode internalMethod07565(boolean localValue1) {
      this.internalField0368.add(localValue1);
      return this;
   }

   public JsonArrayNode internalMethod05613(@Nullable Number localValue1) {
      this.internalField0368.add(localValue1);
      return this;
   }

   public JsonArrayNode internalMethod03270(@Nullable String localValue1) {
      this.internalField0368.add(localValue1);
      return this;
   }

   public JsonArrayNode internalMethod02062(JsonArray localValue1) {
      this.internalField0368.addAll(localValue1);
      return this;
   }

   public JsonArrayNode internalMethod06065(JsonArrayNode localValue1) {
      this.internalField0368.addAll(localValue1.internalMethod01532());
      return this;
   }

   public JsonNode internalMethod07602(int localValue1, JsonElement localValue2) {
      JsonElement localValue3 = this.internalField0368.set(localValue1, localValue2);
      return localValue3 == null ? null : JsonNode.internalMethod07323(localValue3);
   }

   public JsonNode internalMethod05615(int localValue1, JsonNode localValue2) {
      JsonElement localValue3 = this.internalField0368.set(localValue1, localValue2.internalMethod03947());
      return localValue3 == null ? null : JsonNode.internalMethod07323(localValue3);
   }

   public JsonNode internalMethod02307(int localValue1, boolean localValue2) {
      JsonElement localValue3 = this.internalField0368.set(localValue1, new JsonPrimitive(localValue2));
      return localValue3 == null ? null : JsonNode.internalMethod07323(localValue3);
   }

   public JsonNode internalMethod03110(int localValue1, Number localValue2) {
      JsonElement localValue3 = this.internalField0368.set(localValue1, new JsonPrimitive(localValue2));
      return localValue3 == null ? null : JsonNode.internalMethod07323(localValue3);
   }

   public JsonNode internalMethod00946(int localValue1, String localValue2) {
      JsonElement localValue3 = this.internalField0368.set(localValue1, new JsonPrimitive(localValue2));
      return localValue3 == null ? null : JsonNode.internalMethod07323(localValue3);
   }

   public JsonNode internalMethod06594(int localValue1) {
      return JsonNode.internalMethod07323(this.internalField0368.get(localValue1));
   }

   public JsonObjectNode internalMethod06595(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod04512();
   }

   public JsonArrayNode internalMethod06590(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod05819();
   }

   public JsonPrimitiveNode internalMethod06596(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod04513();
   }

   public boolean internalMethod05557(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod08655();
   }

   public byte internalMethod05551(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod07300();
   }

   public short internalMethod05556(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod07304();
   }

   public int internalMethod05554(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod07306();
   }

   public long internalMethod05555(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod07303();
   }

   public float internalMethod05553(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod07302();
   }

   public double internalMethod05552(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod07301();
   }

   public Number internalMethod02653(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod00748();
   }

   public String internalMethod02840(int localValue1) {
      return this.internalMethod06594(localValue1).internalMethod00968();
   }

   public JsonNode internalMethod07402(int localValue1) {
      JsonElement localValue2 = this.internalField0368.remove(localValue1);
      return localValue2 == null ? null : JsonNode.internalMethod07323(localValue2);
   }

   public boolean internalMethod07327(@Nullable JsonElement localValue1) {
      return this.internalField0368.remove((JsonElement)(localValue1 == null ? JsonNull.INSTANCE : localValue1));
   }

   public boolean internalMethod03560(@Nullable JsonNode localValue1) {
      return this.internalField0368.remove((JsonElement)(localValue1 == null ? JsonNull.INSTANCE : localValue1.internalMethod03947()));
   }

   public boolean internalMethod05558(boolean localValue1) {
      return this.internalMethod07327(new JsonPrimitive(localValue1));
   }

   public boolean internalMethod01428(Number localValue1) {
      return this.internalMethod07327(new JsonPrimitive(localValue1));
   }

   public boolean internalMethod06020(String localValue1) {
      return this.internalMethod07327(new JsonPrimitive(localValue1));
   }

   public boolean internalMethod01342(JsonArray localValue1) {
      boolean localValue2 = false;

      for (JsonElement localValue4 : localValue1) {
         localValue2 |= this.internalField0368.remove(localValue4);
      }

      return localValue2;
   }

   public boolean internalMethod01944(JsonArrayNode localValue1) {
      return this.internalMethod01342(localValue1.internalMethod01532());
   }

   public JsonArrayNode internalMethod01561() {
      while (!this.internalField0368.isEmpty()) {
         this.internalField0368.remove(0);
      }

      return this;
   }

   public boolean internalMethod05602(int localValue1) {
      return localValue1 >= 0 && localValue1 < this.internalField0368.size();
   }

   public boolean internalMethod05193(@Nullable JsonElement localValue1) {
      return this.internalField0368.contains((JsonElement)(localValue1 == null ? JsonNull.INSTANCE : localValue1));
   }

   public boolean internalMethod04448(@Nullable JsonNode localValue1) {
      return this.internalField0368.contains((JsonElement)(localValue1 == null ? JsonNull.INSTANCE : localValue1.internalMethod03947()));
   }

   public int internalMethod06014() {
      return this.internalField0368.size();
   }

   public boolean internalMethod06015() {
      return this.internalField0368.isEmpty();
   }

   @Nonnull
   @Override
   public Iterator<JsonNode> iterator() {
      return new JsonArrayNode.InternalType0219(this.internalField0368.iterator());
   }

   public Stream<JsonNode> internalMethod07405() {
      return StreamSupport.stream(this.spliterator(), false);
   }

   public List<JsonNode> internalMethod04259() {
      ArrayList localValue1 = new ArrayList();

      for (JsonElement localValue3 : this.internalField0368) {
         localValue1.add(JsonNode.internalMethod07323(localValue3));
      }

      return localValue1;
   }

   public <T> List<T> internalMethod00047(Function<JsonNode, T> localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (JsonElement localValue4 : this.internalField0368) {
         localValue2.add(localValue1.apply(JsonNode.internalMethod07323(localValue4)));
      }

      return localValue2;
   }

   static class InternalType0219 implements Iterator<JsonNode> {
      private final Iterator<JsonElement> internalField0587;

      public InternalType0219(Iterator<JsonElement> localValue1) {
         this.internalField0587 = localValue1;
      }

      @Override
      public boolean hasNext() {
         return this.internalField0587.hasNext();
      }

      @Override
      public JsonNode next() {
         return JsonNode.internalMethod07323(this.internalField0587.next());
      }

      @Override
      public void remove() {
         this.internalField0587.remove();
      }
   }
}
