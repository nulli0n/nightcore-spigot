package su.nightexpress.nightcore.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public interface NightPluginCommand extends NightCommand, TabExecutor {

    @Nullable NightCommand getDefaultCommand();

    void addDefaultCommand(@NotNull NightCommand command);

    Command getBackend();

    void setBackend(@NotNull Command backend);

    //@NotNull NightCommand findChildren(@NotNull String[] args);
}
