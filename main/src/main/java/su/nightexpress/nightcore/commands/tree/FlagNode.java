package su.nightexpress.nightcore.commands.tree;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.context.Suggestions;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.Collection;
import java.util.Collections;

public class FlagNode extends CommandNode {

    private static final String PREFIX = "-";

    public FlagNode(@NotNull String name) {
        super(name, null, Collections.emptyList());
    }

    @Override
    @NotNull
    public Collection<? extends CommandNode> getRelevantNodes(@NotNull ArgumentReader reader) {
        return this.getChildren(); // Only one (or none) children node is expected.
    }

    // This node "hacks" the command system because flags should be accessible in any order and be all optional
    // I hate the Minecraft commands system it sucks even with brigadier, and im not really happy with the workaround below.
    // But unlike the previous nightcore command API, the flag nodes are "capped" to their real amount, so no more infinite flag suggestions at the end of a command,
    // and flag suggestions are fixed for 2+ flags.

    // Okay so, we have a reference to ExecutableNode in ContextBuilder which we can use to get a list of all available flags.
    // Flags (nodes) are stored in its own map inside LiteralNode (it's the only node that can hold them).
    // Flags are added as "child" nodes of each one and ArgumentNodes when building LiteralNode, so a node tree looks like this:
    //      LiteralNode -> ArgumentNode -> ArgumentNode -> FlagNode -> FlagNode
    // This way the parser can go through all flag nodes.
    // In #parse here, we read user input instead of using current flag node's name because the flag order is kinda "undetermined".
    // In #provideSuggestions, we obtain a list of all flags of the LiteralNode and suggest only missing ones.

    @Override
    public void parse(@NotNull ArgumentReader reader, @NotNull CommandContextBuilder contextBuilder) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        String string = reader.getCursorArgument();

        contextBuilder.withNode(this, cursor);

        if (!string.startsWith(PREFIX)) return;
        if (string.length() < 2) return;

        String flagName = string.substring(1);

        ExecutableNode executable = contextBuilder.getExecutor();
        if (!(executable instanceof LiteralNode parent)) return;

        FlagNode realFlag = parent.getFlag(flagName);
        if (realFlag == null) return;

        contextBuilder.withFlag(realFlag.getName());
    }

    @Override
    protected void provideSuggestions(@NotNull ArgumentReader reader, @NotNull CommandContext context, @NotNull Suggestions suggestions) {
        ExecutableNode executable = context.getExecutor();
        if (!(executable instanceof LiteralNode parent)) return;

        suggestions.setSuggestions(parent.getFlags().stream().filter(flagNode -> !context.hasFlag(flagNode.getName())).map(FlagNode::getPrefixed).toList());
    }

    @Override
    @NotNull
    public String getUsage() {
        return CoreLang.COMMAND_USAGE_OPTIONAL_FLAG.text().replace(Placeholders.GENERIC_NAME, this.getPrefixed());
    }

    @NotNull
    public String getPrefixed() {
        return PREFIX + this.name;
    }
}
