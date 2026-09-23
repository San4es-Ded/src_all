package rockstar.client.inventory;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SlotCollection<T extends InventorySlot> {
   protected final List<T> internalField0416;

   public SlotCollection(List<T> localValue1) {
      this.internalField0416 = localValue1;
   }

   @Nullable
   public T internalMethod02510(Item localValue1) {
      return this.internalField0416.stream().filter(localValue1x -> localValue1x.internalMethod03381(localValue1)).findFirst().orElse(null);
   }

   @Nullable
   public T internalMethod03297(Predicate<ItemStack> localValue1) {
      return this.internalField0416.stream().filter(localValue1x -> localValue1x.internalMethod07038(localValue1)).findFirst().orElse(null);
   }

   public List<T> internalMethod07613(Item localValue1) {
      return this.internalField0416.stream().filter(localValue1x -> localValue1x.internalMethod03381(localValue1)).toList();
   }

   public List<T> internalMethod00168(Predicate<ItemStack> localValue1) {
      return this.internalField0416.stream().filter(localValue1x -> localValue1x.internalMethod07038(localValue1)).toList();
   }

   @Nullable
   public T internalMethod01069() {
      return this.internalField0416.stream().filter(InventorySlot::internalMethod06664).findFirst().orElse(null);
   }

   public boolean internalMethod05924(Item localValue1) {
      return this.internalField0416.stream().anyMatch(localValue1x -> localValue1x.internalMethod03381(localValue1));
   }

   public int internalMethod05923(Item localValue1) {
      return this.internalField0416.stream().filter(localValue1x -> localValue1x.internalMethod03381(localValue1)).mapToInt(localValue0 -> localValue0.internalMethod03427().getCount()).sum();
   }

   public SlotCollection<InventorySlot> internalMethod07591(SlotCollection<? extends InventorySlot> localValue1) {
      ArrayList localValue2 = new ArrayList(this.internalField0416.size() + localValue1.internalField0416.size());
      localValue2.addAll(this.internalField0416);
      localValue2.addAll(localValue1.internalField0416);
      return new SlotCollection<>(localValue2);
   }

   public SlotCollection<InventorySlot> internalMethod05722(InventorySlot localValue1) {
      ArrayList localValue2 = new ArrayList<>(this.internalField0416);
      localValue2.add(localValue1);
      return new SlotCollection<>(localValue2);
   }

   @Generated
   public List<T> internalMethod02638() {
      return this.internalField0416;
   }
}
