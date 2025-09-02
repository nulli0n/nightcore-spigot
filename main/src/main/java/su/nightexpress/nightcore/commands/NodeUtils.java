package su.nightexpress.nightcore.commands;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NodeUtils {

    @NotNull
    public static String formatLabel(@NotNull ExecutableNode node, @NotNull CommandContext context) {
        return formatLabel(node, context.getNodesPriorTo(node));
    }

    @NotNull
    public static String formatLabel(@NotNull ExecutableNode node, @NotNull CommandContextBuilder context) {
        return formatLabel(node, context.getNodesPriorTo(node));
    }

    @NotNull
    private static String formatLabel(@NotNull ExecutableNode node, @NotNull List<CommandNode> priors) {
        String parent = priors.stream().map(CommandNode::getName).collect(Collectors.joining(" "));
        String current = node.getUsage();

        return (parent + " " + current).trim();
    }

    @NotNull
    public static List<CommandNode> getArguments(@NotNull CommandNode source) {
        List<CommandNode> args = new ArrayList<>();

        CommandNode parent = source;
        while (parent.hasChildren()) {
            CommandNode node = new ArrayList<>(parent.getChildren()).getFirst();
            //if (!(node instanceof ArgumentNode<?> argNode)) break;

            args.add(node);
            parent = node;
        }

        return args;
    }

    @NotNull
    public static List<CommandNode> getNodesPriorTo(@NotNull Collection<? extends CommandNode> source, @NotNull CommandNode target) {
        List<CommandNode> list = new ArrayList<>();

        for (CommandNode node : source) {
            if (node == target) break;
            list.add(node);
        }

        return list;
    }
}
