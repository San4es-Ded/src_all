package wtf.wyvern.core.macro;

import com.google.common.reflect.TypeToken;
import java.util.HashSet;
import java.util.Set;
import wtf.wyvern.core.filemanager.api.ManagerFileAbstract;

public class MacroManager extends ManagerFileAbstract<Macro> {
   public MacroManager() {
      super("macro.json", "", (new TypeToken<Set<Macro>>() {
      }).getType(), HashSet::new);
   }

   public boolean removeMacro(Macro macro) {
      return this.getItems().remove(macro);
   }
}