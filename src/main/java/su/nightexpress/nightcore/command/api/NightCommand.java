package su.nightexpress.nightcore.command.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.CommandFlag;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.util.placeholder.Placeholder;

import java.util.Collection;
import java.util.List;

@Deprecated
public interface NightCommand extends Placeholder {

    @NotNull List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args);

    void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    void addChildren(@NotNull NightCommand children);

    void removeChildren(@NotNull String alias);

    default boolean hasPermission(@NotNull CommandSender sender) {
        return this.getPermission() == null || sender.hasPermission(this.getPermission());
    }

    @NotNull
    default String getLabelWithParents() {
        StringBuilder builder = new StringBuilder();
        NightCommand parent = this.getParent();
        while (parent != null) {
            builder.insert(0, parent.getAliases()[0] + " ");
            parent = parent.getParent();
        }
        builder.append(this.getAliases()[0]);
        return builder.toString();
    }

    @Nullable NightCommand getParent();

    void setParent(@Nullable NightCommand parent);

    @Nullable NightCommand getChildren(@NotNull String alias);

    @NotNull Collection<NightCommand> getChildrens();

    @NotNull String[] getAliases();

    @Nullable String getPermission();

    default void setPermission(@Nullable Permission permission) {
        this.setPermission(permission == null ? null : permission.getName());
    }

    void setPermission(@Nullable String permission);

    @Nullable CommandFlag<?> getFlag(@NotNull String name);

    @NotNull Collection<CommandFlag<?>> getFlags();

    default void addFlag(@NotNull CommandFlag<?>... flags) {
        for (CommandFlag<?> flag : flags) this.addFlag(flag);
    }

    void addFlag(@NotNull CommandFlag<?> flag);

    @NotNull String getUsage();

    default void setUsage(@NotNull LangString string) {
        this.setUsage(string.getString());
    }

    void setUsage(@NotNull String usage);

    @NotNull String getDescription();

    default void setDescription(@NotNull LangString string) {
        this.setDescription(string.getString());
    }

    void setDescription(@NotNull String description);

    boolean isPlayerOnly();

    void setPlayerOnly(boolean playerOnly);
}
