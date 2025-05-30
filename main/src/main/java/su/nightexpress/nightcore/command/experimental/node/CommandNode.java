package su.nightexpress.nightcore.command.experimental.node;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.TabContext;

import java.util.List;
import java.util.stream.Stream;

public abstract class CommandNode implements NodeExecutor {

    protected final NightCorePlugin plugin;
    protected final String          name;
    protected final String[]        aliases;
    protected final String          description;
    protected final String          permission;
    protected final boolean         playerOnly;

    protected CommandNode parent;

    public CommandNode(@NotNull NightCorePlugin plugin,
                       @NotNull String name,
                       @NotNull String[] aliases,
                       @NotNull String description,
                       @Nullable String permission,
                       boolean playerOnly) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        this.aliases = Stream.of(aliases).map(String::toLowerCase).toArray(String[]::new);
        this.description = description;
        this.permission = permission;
        this.playerOnly = playerOnly;
    }

    @NotNull
    public abstract List<String> getTab(@NotNull TabContext context);

    @Override
    public boolean run(@NotNull CommandContext context) {
        if (this.isPlayerOnly() && !(context.getSender() instanceof Player)) {
            context.errorPlayerOnly();
            return false;
        }
        if (!this.hasPermission(context.getSender())) {
            context.errorPermission();
            return false;
        }

        return this.onRun(context);
    }

    protected abstract boolean onRun(@NotNull CommandContext context);

    public boolean hasPermission(@NotNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    @NotNull
    public String getNameWithParents() {
        StringBuilder builder = new StringBuilder();

        CommandNode parent = this.getParent();
        while (parent != null) {
            if (!builder.isEmpty()) {
                builder.insert(0, " ");
            }
            builder.insert(0, parent.getName());
            parent = parent.getParent();
        }

        builder.append(" ").append(this.getName());

        return builder.toString().strip();
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String[] getAliases() {
        return this.aliases;
    }

    @NotNull
    public String getUsage() {
        return "";
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public CommandNode getParent() {
        return parent;
    }

    protected void setParent(@Nullable CommandNode parent) {
        this.parent = parent;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }
}
