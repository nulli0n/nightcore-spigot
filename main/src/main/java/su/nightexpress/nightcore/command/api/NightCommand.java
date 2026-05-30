package su.nightexpress.nightcore.command.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.command.CommandFlag;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.util.placeholder.Placeholder;

import java.util.Collection;
import java.util.List;

@Deprecated
public interface NightCommand extends Placeholder {

    @NonNull
    List<String> getTab(@NonNull Player player, int arg, @NonNull String[] args);

    void execute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args);

    void addChildren(@NonNull NightCommand children);

    void removeChildren(@NonNull String alias);

    default boolean hasPermission(@NonNull CommandSender sender) {
        return this.getPermission() == null || sender.hasPermission(this.getPermission());
    }

    @NonNull
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

    @Nullable
    NightCommand getParent();

    void setParent(@Nullable NightCommand parent);

    @Nullable
    NightCommand getChildren(@NonNull String alias);

    @NonNull
    Collection<NightCommand> getChildrens();

    @NonNull
    String[] getAliases();

    @Nullable
    String getPermission();

    default void setPermission(@Nullable Permission permission) {
        this.setPermission(permission == null ? null : permission.getName());
    }

    void setPermission(@Nullable String permission);

    @Nullable
    CommandFlag<?> getFlag(@NonNull String name);

    @NonNull
    Collection<CommandFlag<?>> getFlags();

    default void addFlag(@NonNull CommandFlag<?>... flags) {
        for (CommandFlag<?> flag : flags) this.addFlag(flag);
    }

    void addFlag(@NonNull CommandFlag<?> flag);

    @NonNull
    String getUsage();

    default void setUsage(@NonNull LangString string) {
        this.setUsage(string.getString());
    }

    void setUsage(@NonNull String usage);

    @NonNull
    String getDescription();

    default void setDescription(@NonNull LangString string) {
        this.setDescription(string.getString());
    }

    void setDescription(@NonNull String description);

    boolean isPlayerOnly();

    void setPlayerOnly(boolean playerOnly);
}
