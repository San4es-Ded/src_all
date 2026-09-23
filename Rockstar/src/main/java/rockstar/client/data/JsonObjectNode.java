package rockstar.client.data;


import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonObjectNode extends JsonNode implements Iterable<Entry<String, JsonNode>> {
   private final JsonObject internalField0539;

   public JsonObjectNode() {
      this(new JsonObject());
   }

   public JsonObjectNode(JsonObject localValue1) {
      super(localValue1);
      this.internalField0539 = localValue1;
   }

   public JsonObject internalMethod02940() {
      return this.internalField0539;
   }

   public JsonObjectNode internalMethod05129(String localValue1, @Nullable JsonElement localValue2) {
      this.internalField0539.add(localValue1, localValue2);
      return this;
   }

   public JsonObjectNode internalMethod05254(String localValue1, @Nullable JsonNode localValue2) {
      this.internalField0539.add(localValue1, localValue2 == null ? null : localValue2.internalMethod03947());
      return this;
   }

   public JsonObjectNode internalMethod01038(String localValue1, boolean localValue2) {
      this.internalField0539.addProperty(localValue1, localValue2);
      return this;
   }

   public JsonObjectNode internalMethod05728(String localValue1, @Nullable Number localValue2) {
      this.internalField0539.addProperty(localValue1, localValue2);
      return this;
   }

   public JsonObjectNode internalMethod03412(String localValue1, @Nullable String localValue2) {
      this.internalField0539.addProperty(localValue1, localValue2);
      return this;
   }

   public JsonObjectNode internalMethod00571(JsonObject localValue1) {
      localValue1.asMap().forEach(this.internalField0539::add);
      return this;
   }

   public JsonObjectNode internalMethod01558(JsonObjectNode localValue1) {
      localValue1.internalMethod02940().asMap().forEach(this.internalField0539::add);
      return this;
   }

   public JsonNode internalMethod01059(String localValue1) {
      JsonElement localValue2 = this.internalField0539.remove(localValue1);
      return localValue2 == null ? null : JsonNode.internalMethod07323(localValue2);
   }

   public JsonObjectNode internalMethod07126() {
      this.internalField0539.asMap().clear();
      return this;
   }

   public boolean internalMethod03645(String localValue1) {
      return this.internalField0539.has(localValue1);
   }

   public boolean internalMethod02179(String localValue1) {
      return this.internalField0539.has(localValue1) && this.internalField0539.get(localValue1).isJsonObject();
   }

   public boolean internalMethod08204(String localValue1) {
      return this.internalField0539.has(localValue1) && this.internalField0539.get(localValue1).isJsonArray();
   }

   public boolean internalMethod08911(String localValue1) {
      return this.internalField0539.has(localValue1) && this.internalField0539.get(localValue1).isJsonPrimitive();
   }

   public boolean internalMethod08064(String localValue1) {
      return this.internalField0539.has(localValue1)
         && this.internalField0539.get(localValue1).isJsonPrimitive()
         && this.internalField0539.get(localValue1).getAsJsonPrimitive().isBoolean();
   }

   public boolean internalMethod07773(String localValue1) {
      return this.internalField0539.has(localValue1)
         && this.internalField0539.get(localValue1).isJsonPrimitive()
         && this.internalField0539.get(localValue1).getAsJsonPrimitive().isNumber();
   }

   public boolean internalMethod09165(String localValue1) {
      return this.internalField0539.has(localValue1)
         && this.internalField0539.get(localValue1).isJsonPrimitive()
         && this.internalField0539.get(localValue1).getAsJsonPrimitive().isString();
   }

   public JsonNode internalMethod03705(String localValue1) {
      return this.internalMethod05253(localValue1, null);
   }

   public JsonNode internalMethod05253(String localValue1, @Nullable JsonNode localValue2) {
      return this.internalMethod03645(localValue1) ? JsonNode.internalMethod07323(this.internalField0539.get(localValue1)) : localValue2;
   }

   public Optional<JsonNode> internalMethod01842(String localValue1) {
      return Optional.ofNullable(this.internalMethod05253(localValue1, null));
   }

   @Nonnull
   public JsonNode internalMethod07988(String localValue1) {
      JsonNode localValue2 = this.internalMethod05253(localValue1, null);
      if (localValue2 == null) {
         throw new NoSuchElementException("No element found for key: " + localValue1);
      } else {
         return localValue2;
      }
   }

   public JsonObjectNode internalMethod01060(String localValue1) {
      return this.internalMethod06510(localValue1, null);
   }

   public JsonObjectNode internalMethod06510(String localValue1, @Nullable JsonObjectNode localValue2) {
      return this.internalMethod02179(localValue1) ? this.internalMethod05253(localValue1, localValue2).internalMethod04512() : localValue2;
   }

   public Optional<JsonObjectNode> internalMethod06509(String localValue1) {
      return Optional.ofNullable(this.internalMethod06510(localValue1, null));
   }

   @Nonnull
   public JsonObjectNode internalMethod03706(String localValue1) {
      JsonObjectNode localValue2 = this.internalMethod06510(localValue1, null);
      if (localValue2 == null) {
         throw new NoSuchElementException("No object found for key: " + localValue1);
      } else {
         return localValue2;
      }
   }

   public JsonArrayNode internalMethod01053(String localValue1) {
      return this.internalMethod06008(localValue1, null);
   }

   public JsonArrayNode internalMethod06008(String localValue1, @Nullable JsonArrayNode localValue2) {
      return this.internalMethod08204(localValue1) ? this.internalMethod05253(localValue1, localValue2).internalMethod05819() : localValue2;
   }

   public Optional<JsonArrayNode> internalMethod07737(String localValue1) {
      return Optional.ofNullable(this.internalMethod06008(localValue1, null));
   }

   @Nonnull
   public JsonArrayNode internalMethod03703(String localValue1) {
      JsonArrayNode localValue2 = this.internalMethod06008(localValue1, null);
      if (localValue2 == null) {
         throw new NoSuchElementException("No array found for key: " + localValue1);
      } else {
         return localValue2;
      }
   }

   public JsonPrimitiveNode internalMethod01061(String localValue1) {
      return this.internalMethod06004(localValue1, null);
   }

   public JsonPrimitiveNode internalMethod06004(String localValue1, @Nullable JsonPrimitiveNode localValue2) {
      return this.internalMethod08911(localValue1) ? this.internalMethod05253(localValue1, localValue2).internalMethod04513() : localValue2;
   }

   public Optional<JsonPrimitiveNode> internalMethod08653(String localValue1) {
      return Optional.ofNullable(this.internalMethod06004(localValue1, null));
   }

   @Nonnull
   public JsonPrimitiveNode internalMethod03707(String localValue1) {
      JsonPrimitiveNode localValue2 = this.internalMethod06004(localValue1, null);
      if (localValue2 == null) {
         throw new NoSuchElementException("No primitive found for key: " + localValue1);
      } else {
         return localValue2;
      }
   }

   public boolean internalMethod09831(String localValue1) {
      return this.internalMethod05145(localValue1, false);
   }

   public boolean internalMethod05145(String localValue1, boolean localValue2) {
      return this.internalMethod08064(localValue1) ? this.internalField0539.get(localValue1).getAsBoolean() : localValue2;
   }

   public boolean internalMethod09389(String localValue1) {
      if (!this.internalMethod08064(localValue1)) {
         throw new NoSuchElementException("No boolean found for key: " + localValue1);
      } else {
         return this.internalMethod09831(localValue1);
      }
   }

   public byte internalMethod03639(String localValue1) {
      return this.internalMethod05139(localValue1, (byte)0);
   }

   public byte internalMethod05139(String localValue1, byte localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsByte() : localValue2;
   }

   public byte internalMethod02173(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No byte found for key: " + localValue1);
      } else {
         return this.internalMethod03639(localValue1);
      }
   }

   public short internalMethod03644(String localValue1) {
      return this.internalMethod05144(localValue1, (short)0);
   }

   public short internalMethod05144(String localValue1, short localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsShort() : localValue2;
   }

   public short internalMethod02178(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No short found for key: " + localValue1);
      } else {
         return this.internalMethod03644(localValue1);
      }
   }

   public int internalMethod03642(String localValue1) {
      return this.internalMethod05142(localValue1, 0);
   }

   public int internalMethod05142(String localValue1, int localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsInt() : localValue2;
   }

   public OptionalInt internalMethod01681(String localValue1) {
      return this.internalMethod07773(localValue1) ? OptionalInt.of(this.internalField0539.get(localValue1).getAsInt()) : OptionalInt.empty();
   }

   public int internalMethod02176(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No int found for key: " + localValue1);
      } else {
         return this.internalMethod03642(localValue1);
      }
   }

   public long internalMethod03643(String localValue1) {
      return this.internalMethod05143(localValue1, 0L);
   }

   public long internalMethod05143(String localValue1, long localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsLong() : localValue2;
   }

   public OptionalLong internalMethod06911(String localValue1) {
      return this.internalMethod07773(localValue1) ? OptionalLong.of(this.internalField0539.get(localValue1).getAsLong()) : OptionalLong.empty();
   }

   public long internalMethod02177(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No long found for key: " + localValue1);
      } else {
         return this.internalMethod03643(localValue1);
      }
   }

   public float internalMethod03641(String localValue1) {
      return this.internalMethod05141(localValue1, 0.0F);
   }

   public float internalMethod05141(String localValue1, float localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsFloat() : localValue2;
   }

   public float internalMethod02175(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No float found for key: " + localValue1);
      } else {
         return this.internalMethod03641(localValue1);
      }
   }

   public double internalMethod03640(String localValue1) {
      return this.internalMethod05140(localValue1, 0.0);
   }

   public double internalMethod05140(String localValue1, double localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsDouble() : localValue2;
   }

   public OptionalDouble internalMethod00427(String localValue1) {
      return this.internalMethod07773(localValue1) ? OptionalDouble.of(this.internalField0539.get(localValue1).getAsDouble()) : OptionalDouble.empty();
   }

   public double internalMethod02174(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No double found for key: " + localValue1);
      } else {
         return this.internalMethod03640(localValue1);
      }
   }

   public Number internalMethod02265(String localValue1) {
      return this.internalMethod03685(localValue1, null);
   }

   public Number internalMethod03685(String localValue1, @Nullable Number localValue2) {
      return this.internalMethod07773(localValue1) ? this.internalField0539.get(localValue1).getAsNumber() : localValue2;
   }

   public Optional<Number> internalMethod08161(String localValue1) {
      return Optional.ofNullable(this.internalMethod03685(localValue1, null));
   }

   @Nonnull
   public Number internalMethod03215(String localValue1) {
      if (!this.internalMethod07773(localValue1)) {
         throw new NoSuchElementException("No number found for key: " + localValue1);
      } else {
         return this.internalMethod02265(localValue1);
      }
   }

   public String internalMethod02501(String localValue1) {
      return this.internalMethod01170(localValue1, null);
   }

   public String internalMethod01170(String localValue1, @Nullable String localValue2) {
      return this.internalMethod09165(localValue1) ? this.internalField0539.get(localValue1).getAsString() : localValue2;
   }

   public Optional<String> internalMethod08532(String localValue1) {
      return Optional.ofNullable(this.internalMethod01170(localValue1, null));
   }

   @Nonnull
   public String internalMethod03457(String localValue1) {
      String localValue2 = this.internalMethod01170(localValue1, null);
      if (localValue2 == null) {
         throw new NoSuchElementException("No string found for key: " + localValue1);
      } else {
         return localValue2;
      }
   }

   public int internalMethod00947() {
      return this.internalField0539.size();
   }

   public boolean internalMethod00948() {
      return this.internalField0539.isEmpty();
   }

   public Set<String> internalMethod06355() {
      return this.internalField0539.keySet();
   }

   public Set<Entry<String, JsonNode>> internalMethod02843() {
      return this.internalField0539.entrySet().stream().map(JsonObjectNode.InternalType0227::new).collect(Collectors.toSet());
   }

   @Nonnull
   @Override
   public Iterator<Entry<String, JsonNode>> iterator() {
      return new JsonObjectNode.InternalType0226(this.internalField0539.entrySet().iterator());
   }

   public Stream<Entry<String, JsonNode>> internalMethod02622() {
      return StreamSupport.stream(this.spliterator(), false);
   }

   public Map<String, JsonNode> internalMethod06351() {
      HashMap localValue1 = new HashMap();

      for (Entry localValue3 : this.internalField0539.entrySet()) {
         localValue1.put(localValue3.getKey(), JsonNode.internalMethod07323((JsonElement)localValue3.getValue()));
      }

      return localValue1;
   }

   public <T> Map<String, T> internalMethod00966(Function<JsonNode, T> localValue1) {
      HashMap localValue2 = new HashMap();

      for (Entry localValue4 : this.internalField0539.entrySet()) {
         localValue2.put(localValue4.getKey(), localValue1.apply(JsonNode.internalMethod07323((JsonElement)localValue4.getValue())));
      }

      return localValue2;
   }

   static class InternalType0226 implements Iterator<Entry<String, JsonNode>> {
      private final Iterator<Entry<String, JsonElement>> internalField0587;

      public InternalType0226(Iterator<Entry<String, JsonElement>> localValue1) {
         this.internalField0587 = localValue1;
      }

      @Override
      public boolean hasNext() {
         return this.internalField0587.hasNext();
      }

      @Override
      public Entry<String, JsonNode> next() {
         return new JsonObjectNode.InternalType0227(this.internalField0587.next());
      }

      @Override
      public void remove() {
         this.internalField0587.remove();
      }
   }

   static class InternalType0227 implements Entry<String, JsonNode> {
      private final Entry<String, JsonElement> internalField0090;

      public InternalType0227(Entry<String, JsonElement> localValue1) {
         this.internalField0090 = localValue1;
      }

      @Override
      public String getKey() {
         return this.internalField0090.getKey();
      }

      @Override
      public JsonNode getValue() {
         return JsonNode.internalMethod07323(this.internalField0090.getValue());
      }

      @Override
      public JsonNode setValue(JsonNode localValue1) {
         JsonElement localValue2 = this.internalField0090.setValue(localValue1.internalMethod03947());
         return localValue2 == null ? null : JsonNode.internalMethod07323(localValue2);
      }
   }
}
