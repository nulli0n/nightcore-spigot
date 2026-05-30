package su.nightexpress.nightcore.util.format.adaptive;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface VariableReplacer<T> {

    @NonNull
    String replace(@NonNull T source, @NonNull Player player);
}