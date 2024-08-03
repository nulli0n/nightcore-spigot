package su.nightexpress.nightcore.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.CommandFlag;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.api.NightCommand;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.util.*;
import java.util.stream.Stream;

@Deprecated
public abstract class AbstractCommand<P extends NightCorePlugin> implements NightCommand {

    protected final P                           plugin;
    private final   String[]                    aliases;
    private final   Map<String, NightCommand>   childrens;
    private final   Map<String, CommandFlag<?>> commandFlags;
    private final   PlaceholderMap              placeholderMap;

    private NightCommand parent;
    private String       permission;
    private String       usage;
    private String       description;
    private boolean      playerOnly;

    public AbstractCommand(@NotNull P plugin, @NotNull String[] aliases) {
        this(plugin, aliases, (String) null);
    }

    public AbstractCommand(@NotNull P plugin, @NotNull String[] aliases, @Nullable Permission permission) {
        this(plugin, aliases, permission == null ? null : permission.getName());
    }

    public AbstractCommand(@NotNull P plugin, @NotNull String[] aliases, @Nullable String permission) {
        this.plugin = plugin;
        this.aliases = Stream.of(aliases).map(String::toLowerCase).toArray(String[]::new);
        this.permission = permission;
        this.childrens = new TreeMap<>();
        this.commandFlags = new HashMap<>();
        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.COMMAND_DESCRIPTION, this::getDescription)
            .add(Placeholders.COMMAND_USAGE, this::getUsage)
            .add(Placeholders.COMMAND_LABEL, this::getLabelWithParents);

    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    protected abstract void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result);

    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (player.hasPermission(CorePerms.COMMAND_FLAGS)) {
            return this.getFlags().stream().map(CommandFlag::getNamePrefixed).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public final void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (this.isPlayerOnly() && !(sender instanceof Player)) {
            this.errorSender(sender);
            return;
        }
        if (!this.hasPermission(sender)) {
            this.errorPermission(sender);
            return;
        }

        List<String> cleanArgs = new ArrayList<>();
        Map<CommandFlag<?>, StringBuilder> flagContent = new HashMap<>();
        CommandFlag<?> lastFlag = null;

        for (String arg : args) {
            CommandFlag<?> found = arg.charAt(0) == CommandFlag.PREFIX ? this.getFlag(arg.substring(1)) : null;

            if (found != null) {
                flagContent.put(found, new StringBuilder());
                lastFlag = found;
            }
            else if (lastFlag != null) {
                StringBuilder builder = flagContent.get(lastFlag);
                if (!builder.isEmpty()) builder.append(" ");

                builder.append(arg);
            }
            else {
                cleanArgs.add(arg);
            }
        }
        if (!sender.hasPermission(CorePerms.COMMAND_FLAGS)) {
            flagContent.clear();
        }

        CommandResult result = new CommandResult(label, cleanArgs.toArray(new String[0]), flagContent);

        this.onExecute(sender, result);
    }

    public final void addChildren(@NotNull NightCommand children) {
        if (children.getParent() != null) return;

        Stream.of(children.getAliases()).forEach(alias -> {
            this.childrens.put(alias, children);
        });
        children.setParent(this);
    }

    @Override
    public final void removeChildren(@NotNull String alias) {
        this.childrens.keySet().removeIf(key -> key.equalsIgnoreCase(alias));
    }

    @Override
    @Nullable
    public NightCommand getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable NightCommand parent) {
        this.parent = parent;
    }

    @Override
    @Nullable
    public final NightCommand getChildren(@NotNull String alias) {
        return this.childrens.get(alias);
    }

    @Override
    @NotNull
    public Collection<NightCommand> getChildrens() {
        return this.childrens.values();
    }

    @Override
    @NotNull
    public final String[] getAliases() {
        return this.aliases;
    }

    @Override
    @Nullable
    public final String getPermission() {
        return this.permission;
    }

    @Override
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    @Override
    @Nullable
    public CommandFlag<?> getFlag(@NotNull String name) {
        return this.commandFlags.get(name.toLowerCase());
    }

    @Override
    public void addFlag(@NotNull CommandFlag<?> flag) {
        this.commandFlags.put(flag.getName(), flag);
    }

    @Override
    @NotNull
    public Collection<CommandFlag<?>> getFlags() {
        return this.commandFlags.values();
    }

    @Override
    @NotNull
    public String getUsage() {
        return this.usage == null ? "" : this.usage;
    }

    @Override
    public void setUsage(@NotNull String usage) {
        this.usage = usage;
    }

    @Override
    @NotNull
    public String getDescription() {
        return this.description == null ? "" : this.description;
    }

    @Override
    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    @Override
    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    @Override
    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    protected final void errorUsage(@NotNull CommandSender sender) {
        CoreLang.ERROR_COMMAND_USAGE.getMessage(plugin).replace(this.replacePlaceholders()).send(sender);
    }

    protected final void errorPermission(@NotNull CommandSender sender) {
        CoreLang.ERROR_NO_PERMISSION.getMessage(plugin).send(sender);
    }

    protected final void errorPlayer(@NotNull CommandSender sender) {
        CoreLang.ERROR_INVALID_PLAYER.getMessage(plugin).send(sender);
    }

    protected final void errorSender(@NotNull CommandSender sender) {
        CoreLang.ERROR_COMMAND_PLAYER_ONLY.getMessage(plugin).send(sender);
    }

    protected final void errorNumber(@NotNull CommandSender sender, @NotNull String from) {
        CoreLang.ERROR_INVALID_NUMBER.getMessage(plugin).replace(Placeholders.GENERIC_VALUE, from).send(sender);
    }
}
