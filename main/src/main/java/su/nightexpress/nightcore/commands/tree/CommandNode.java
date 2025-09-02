package su.nightexpress.nightcore.commands.tree;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.Suggestions;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.*;

public abstract class CommandNode {

    protected final String name;
    protected final String permission;

    protected final List<CommandRequirement> requirements;
    protected final Map<String, CommandNode> children;

    public CommandNode(@NotNull String name, @Nullable String permission, @NotNull List<CommandRequirement> requirements) {
        this.name = LowerCase.USER_LOCALE.apply(name);
        this.permission = permission;
        this.children = new LinkedHashMap<>();
        this.requirements = requirements;
    }

    public boolean canUse(@NotNull CommandSender sender) {
        return this.requirements.stream().allMatch(requirement -> requirement.test(sender));
    }

    public void suggests(@NotNull ArgumentReader reader, @NotNull CommandContext context, @NotNull Suggestions suggestions) {
        // The cursor at this time is set at new (next argument) position. But the CommandNode here is bound to the previous argument.
        // So LiteralNode will provide suggestions for its children ArgumentNode, and so on.
        // Only one children is allowed for all nodes except the Hub one. The HubNode overrides this method accordingly.
        List<CommandNode> children = new ArrayList<>(this.getChildren());
        if (children.isEmpty()) {
            suggestions.setSuggestions(Collections.emptyList());
            return;
        }

        CommandNode nextNode = children.getFirst();
        nextNode.provideSuggestions(reader, context, suggestions);
    }

    public abstract void parse(@NotNull ArgumentReader reader, @NotNull CommandContextBuilder contextBuilder) throws CommandSyntaxException;

    protected abstract void provideSuggestions(@NotNull ArgumentReader reader, @NotNull CommandContext context, @NotNull Suggestions suggestions);

    public boolean hasPermission(@NotNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    @NotNull
    public Collection<? extends CommandNode> getChildren() {
        return this.children.values();
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @NotNull
    public abstract Collection<? extends CommandNode> getRelevantNodes(@NotNull ArgumentReader reader);

    @Nullable
    public CommandNode getChild(@NotNull String name) {
        return this.children.get(name);
    }

    protected void addChildren(@NotNull CommandNode node) {
        this.children.put(node.getName(), node);
    }

    public boolean isRequired() {
        return false;
    }

    public boolean hasRequiredArguments() {
        return false;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getLocalizedName() {
        return this.name;
    }

    @NotNull
    public String getUsage() {
        return "";
    }

    @Nullable
    public String getPermission() {
        return this.permission;
    }

    @NotNull
    public List<CommandRequirement> getRequirements() {
        return this.requirements;
    }
}
