package su.nightexpress.nightcore.command.experimental.node;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.builder.ChainedNodeBuilder;
import su.nightexpress.nightcore.command.experimental.builder.NodeBuilder;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.*;

@Deprecated
public class ChainedNode extends CommandNode {

    private final String                   localized;
    private final Map<String, CommandNode> commandMap;

    private NodeExecutor fallback;

    public ChainedNode(@NotNull NightCorePlugin plugin,
                       @NotNull String name,
                       @NotNull String[] aliases,
                       @NotNull String description,
                       @Nullable String localized,
                       @Nullable String permission,
                       boolean playerOnly,
                       @Nullable NodeExecutor fallback,
                       @NotNull Map<String, CommandNode> commandMap) {
        super(plugin, name, aliases, description, permission, playerOnly);
        this.localized = localized == null ? StringUtil.capitalizeUnderscored(name) : localized;
        this.commandMap = new HashMap<>();
        this.setFallback(fallback);

        this.addChildren(DirectNode.builder(plugin, "help")
            .description(CoreLang.COMMAND_HELP_DESC)
            .permission(permission)
            .executes((context, arguments) -> this.sendCommandList(context))
        );

        commandMap.values().forEach(this::addChildren);
    }

    @NotNull
    public static ChainedNodeBuilder builder(@NotNull NightCorePlugin plugin, @NotNull String... aliases) {
        return new ChainedNodeBuilder(plugin, aliases);
    }

    @Override
    protected boolean onRun(@NotNull CommandContext context) {
        if (context.length() == 0 || context.getArgumentIndex() >= context.length()) {
            return this.onFallback(context);
        }

        String node = context.getArgs()[context.getArgumentIndex()];
        CommandNode children = this.commandMap.get(node);
        if (children == null) {
            return this.onFallback(context);
        }

        context.setArgumentIndex(context.getArgumentIndex() + 1);
        return children.run(context);
    }

    public void setFallback(@Nullable NodeExecutor fallback) {
        this.fallback = fallback;
    }

    private boolean onFallback(@NotNull CommandContext context) {
        if (this.fallback != null) {
            return this.fallback.run(context);
        }
        return this.sendCommandList(context);
    }

    private boolean sendCommandList(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();

        context.send(CoreLang.COMMAND_HELP_LIST.getMessage(), replacer -> replacer
            .replace(Placeholders.GENERIC_NAME, this.localized)
            .replace(Placeholders.GENERIC_ENTRY, list -> {
                this.getChildrens().stream().sorted(Comparator.comparing(CommandNode::getName)).forEach(children -> {
                    if (!children.hasPermission(sender)) return;

                    list.add(CoreLang.COMMAND_HELP_ENTRY.getString()
                        .replace(Placeholders.COMMAND_LABEL, children.getNameWithParents())
                        .replace(Placeholders.COMMAND_USAGE, children.getUsage())
                        .replace(Placeholders.COMMAND_DESCRIPTION, children.getDescription())
                    );
                });
            }));

        return true;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull TabContext context) {
        int index = context.getLastCommandIndex();

        if (index == context.length() - 1) {
            return this.getChildrens().stream().filter(node -> node.hasPermission(context.getSender())).map(CommandNode::getName).toList();
        }
        if (index >= context.length()) {
            return Collections.emptyList();
        }

        String node = context.getArg(index);
        CommandNode children = this.commandMap.get(node);
        if (children == null) return Collections.emptyList();

        context.setLastCommandIndex(index + 1);

        return children.getTab(context);
    }

    public void addChildren(@NotNull NodeBuilder<?, ?> builder) {
        this.addChildren(builder.build());
    }

    public void addChildren(@NotNull CommandNode children) {
        if (children.getParent() != null) return;

        this.commandMap.put(children.getName(), children);
        for (String alias : children.getAliases()) {
            this.commandMap.put(alias, children);
        }
        children.setParent(this);
    }

    public void removeChildren(@NotNull String alias) {
        this.commandMap.keySet().removeIf(key -> key.equalsIgnoreCase(alias));
    }

    @Nullable
    public CommandNode getChildren(@NotNull String alias) {
        return this.commandMap.get(alias);
    }

    @NotNull
    public Set<CommandNode> getChildrens() {
        return new HashSet<>(this.commandMap.values());
    }

    @NotNull
    public String getLocalized() {
        return this.localized;
    }

    @NotNull
    public Map<String, CommandNode> getCommandMap() {
        return this.commandMap;
    }
}
