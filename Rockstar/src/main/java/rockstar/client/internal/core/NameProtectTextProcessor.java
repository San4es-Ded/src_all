package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;

public final class NameProtectTextProcessor {
   private static final StringBuilder internalField0208 = new StringBuilder(256);

   private NameProtectTextProcessor() {
   }

   public static CharSequence internalMethod04584(OrderedText localValue0) {
      internalField0208.setLength(0);
      localValue0.accept((localValue0x, localValue1, localValue2) -> {
         internalField0208.appendCodePoint(localValue2);
         return true;
      });
      return internalField0208;
   }

   public static OrderedText internalMethod04840(OrderedText localValue0, NameProtectModule localValue1) {
      ArrayList localValue2 = new ArrayList();
      ArrayList localValue3 = new ArrayList();
      localValue0.accept((localValue2x, localValue3x, localValue4x) -> {
         if (localValue3.isEmpty() || !((Style)localValue2.getLast()).equals(localValue3x)) {
            localValue2.add(localValue3x);
            localValue3.add(new StringBuilder());
         }

         ((StringBuilder)localValue3.getLast()).appendCodePoint(localValue4x);
         return true;
      });
      if (localValue3.isEmpty()) {
         return null;
      } else {
         UnaryOperator<String> localValue4 = localValue1::internalMethod04954;
         ArrayList localValue5 = new ArrayList(localValue3.size());
         StringBuilder localValue6 = new StringBuilder();
         boolean localValue7 = false;

         for (int localValue8 = 0; localValue8 < localValue3.size(); localValue8++) {
            String localValue9 = ((StringBuilder)localValue3.get(localValue8)).toString();
            String localValue10 = localValue4.apply(localValue9);
            localValue7 |= !localValue10.equals(localValue9);
            localValue6.append(localValue10);
            localValue5.add(OrderedText.styledForwardsVisitedString(localValue10, (Style)localValue2.get(localValue8)));
         }

         if (!localValue7) {
            return null;
         } else {
            String localValue11 = localValue6.toString();
            return !localValue4.apply(localValue11).equals(localValue11)
               ? OrderedText.styledForwardsVisitedString(localValue4.apply(localValue11), (Style)localValue2.getFirst())
               : OrderedText.concat(localValue5);
         }
      }
   }
}
