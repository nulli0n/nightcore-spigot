package su.nightexpress.nightcore.bridge.paper;

import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface UnsafeEntityIdProvider {

    /**
     * Retrieves the next available entity ID.
     *
     * @param world The world context (required for modern Paper implementations, ignored in legacy).
     * @return The next entity ID.
     */
    int getNextEntityId(@Nullable World world);
}