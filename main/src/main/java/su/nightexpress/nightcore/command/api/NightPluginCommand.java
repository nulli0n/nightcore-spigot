package su.nightexpress.nightcore.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Deprecated
public interface NightPluginCommand extends NightCommand, TabExecutor {

    @Nullable
    NightCommand getDefaultCommand();

    void addDefaultCommand(@NonNull NightCommand command);

    Command getBackend();

    void setBackend(@NonNull Command backend);

    //@NonNull NightCommand findChildren(@NonNull String[] args);
}
