package su.nightexpress.nightcore.util.bridge;

import org.jetbrains.annotations.NotNull;

public interface Software {

    @NotNull String getName();

    boolean isSpigot();

    boolean isPaper();
}
