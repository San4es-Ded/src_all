package su.sacura.util.type;

import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public interface ISetting {
  Setting<?> setVisible(Supplier<Boolean> paramSupplier);
}


