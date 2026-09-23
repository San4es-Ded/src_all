package rockstar.client.internal.script;




import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.text.Text;

public class StaffListStore {
   public static final String internalField0248 = "MODER";
   public static final int internalField0227 = 16;
   private final Set<StaffListStore.InternalType0181> internalField0546 = new LinkedHashSet<>();

   public final void internalMethod03183(String localValue1, String localValue2) {
      if (localValue1 != null && !localValue1.isBlank()) {
         String localValue3 = localValue1.trim();
         if (internalMethod01245(localValue3) > 16) {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("commands.staff.name_too_long", 16)));
         } else {
            String localValue4 = localValue2 != null && !localValue2.isBlank() ? localValue2.trim() : "MODER";
            if (internalMethod01245(localValue4) > 16) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("commands.staff.prefix_too_long", 16)));
            } else {
               StaffListStore.InternalType0181 localValue5 = new StaffListStore.InternalType0181(localValue3, localValue4);
               if (this.internalField0546.stream().anyMatch(localValue1x -> localValue1x.internalMethod00138().equalsIgnoreCase(localValue3))) {
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.staff.exists", localValue3)));
               } else {
                  this.internalField0546.add(localValue5);
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.staff.added", localValue4, localValue3)));
                  this.internalMethod01961();
               }
            }
         }
      } else {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.staff.empty_name")));
      }
   }

   public final void internalMethod01246(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         String localValue2 = localValue1.trim();
         boolean localValue3 = this.internalField0546.removeIf(localValue1x -> localValue1x.internalMethod00138().equalsIgnoreCase(localValue2));
         if (localValue3) {
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.staff.removed", localValue2)));
            this.internalMethod01961();
         } else {
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.staff.not_exists", localValue2)));
         }
      } else {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.staff.empty_name")));
      }
   }

   public final void internalMethod01959() {
      if (this.internalField0546.isEmpty()) {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.staff.empty")));
      } else {
         this.internalField0546.clear();
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.staff.cleared")));
         this.internalMethod01961();
      }
   }

   public final void internalMethod01421(Collection<StaffListStore.InternalType0181> localValue1) {
      this.internalField0546.clear();
      if (localValue1 != null) {
         for (StaffListStore.InternalType0181 localValue3 : localValue1) {
            if (localValue3 != null && !localValue3.internalMethod00138().isBlank()) {
               this.internalField0546.add(localValue3);
            }
         }
      }
   }

   public final List<StaffListStore.InternalType0181> internalMethod07279() {
      return List.copyOf(this.internalField0546);
   }

   public final void internalMethod03199(Collection<StaffListStore.InternalType0181> localValue1) {
      this.internalField0546.clear();
      if (localValue1 != null) {
         for (StaffListStore.InternalType0181 localValue3 : localValue1) {
            if (localValue3 != null
               && !localValue3.internalMethod00138().isBlank()
               && internalMethod01245(localValue3.internalMethod00138()) <= 16
               && internalMethod01245(localValue3.internalMethod04906()) <= 16
               && !this.internalField0546.stream().anyMatch(localValue1x -> localValue1x.internalMethod00138().equalsIgnoreCase(localValue3.internalMethod00138()))) {
               this.internalField0546.add(new StaffListStore.InternalType0181(localValue3.internalMethod00138(), localValue3.internalMethod04906()));
            }
         }
      }
   }

   private static int internalMethod01245(String localValue0) {
      return localValue0.codePointCount(0, localValue0.length());
   }

   private void internalMethod01961() {
      if (RockstarClient.getInstance().internalMethod03371() != null) {
         RockstarClient.getInstance().internalMethod03371().internalMethod05165("staff");
         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      }
   }

   public static final class InternalType0181 {
      private final String internalField0248;
      private final String internalField0247;

      public InternalType0181(String localValue1, String localValue2) {
         localValue1 = localValue1 == null ? "" : localValue1.trim();
         localValue2 = localValue2 != null && !localValue2.isBlank() ? localValue2.trim() : "MODER";
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0181[name=" + this.internalField0248 + ", prefix=" + this.internalField0247 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         StaffListStore.InternalType0181 other = (StaffListStore.InternalType0181) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247);
      }

      public String internalMethod00138() {
         return this.internalField0248;
      }

      public String internalMethod04906() {
         return this.internalField0247;
      }
   }
}
