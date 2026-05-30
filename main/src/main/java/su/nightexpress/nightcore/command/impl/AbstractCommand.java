package su.nightexpress.nightcore.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    protected final P                         plugin;
    private final String[]                    aliases;
    private final Map<String, NightCommand>   childrens;
    private final Map<String, CommandFlag<?>> commandFlags;
    private final PlaceholderMap              placeholderMap;

    private NightCommand parent;
    private String       permission;
    private String       usage;
    private String       description;
    private boolean      playerOnly;

    public AbstractCommand(@NonNull P plugin, @NonNull String[] aliases) {
        this(plugin, aliases, (String) null);
    }

    public AbstractCommand(@NonNull P plugin, @NonNull String[] aliases, @Nullable Permission permission) {
        this(plugin, aliases, permission == null ? null : permission.getName());
    }

    public AbstractCommand(@NonNull P plugin, @NonNull String[] aliases, @Nullable String permission) {
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
    @NonNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    protected abstract void onExecute(@NonNull CommandSender sender, @NonNull CommandResult result);

    @NonNull
    public List<String> getTab(@NonNull Player player, int arg, @NonNull String[] args) {
        if (player.hasPermission(CorePerms.COMMAND_FLAGS)) {
            return this.getFlags().stream().map(CommandFlag::getNamePrefixed).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public final void execute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
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

    public final void addChildren(@NonNull NightCommand children) {
        if (children.getParent() != null) return;

        Stream.of(children.getAliases()).forEach(alias -> {
            this.childrens.put(alias, children);
        });
        children.setParent(this);
    }

    @Override
    public final void removeChildren(@NonNull String alias) {
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
    public final NightCommand getChildren(@NonNull String alias) {
        return this.childrens.get(alias);
    }

    @Override
    @NonNull
    public Collection<NightCommand> getChildrens() {
        return this.childrens.values();
    }

    @Override
    @NonNull
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
    public CommandFlag<?> getFlag(@NonNull String name) {
        return this.commandFlags.get(name.toLowerCase());
    }

    @Override
    public void addFlag(@NonNull CommandFlag<?> flag) {
        this.commandFlags.put(flag.getName(), flag);
    }

    @Override
    @NonNull
    public Collection<CommandFlag<?>> getFlags() {
        return this.commandFlags.values();
    }

    @Override
    @NonNull
    public String getUsage() {
        return this.usage == null ? "" : this.usage;
    }

    @Override
    public void setUsage(@NonNull String usage) {
        this.usage = usage;
    }

    @Override
    @NonNull
    public String getDescription() {
        return this.description == null ? "" : this.description;
    }

    @Override
    public void setDescription(@NonNull String description) {
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

    protected final void errorUsage(@NonNull CommandSender sender) {
        CoreLang.ERROR_COMMAND_USAGE.getMessage(plugin).replace(this.replacePlaceholders()).send(sender);
    }

    protected final void errorPermission(@NonNull CommandSender sender) {
        CoreLang.ERROR_NO_PERMISSION.getMessage(plugin).send(sender);
    }

    protected final void errorPlayer(@NonNull CommandSender sender) {
        CoreLang.ERROR_INVALID_PLAYER.getMessage(plugin).send(sender);
    }

    protected final void errorSender(@NonNull CommandSender sender) {
        CoreLang.ERROR_COMMAND_PLAYER_ONLY.getMessage(plugin).send(sender);
    }

    protected final void errorNumber(@NonNull CommandSender sender, @NonNull String from) {
        CoreLang.ERROR_INVALID_NUMBER.getMessage(plugin).replace(Placeholders.GENERIC_VALUE, from).send(sender);
    }
}
