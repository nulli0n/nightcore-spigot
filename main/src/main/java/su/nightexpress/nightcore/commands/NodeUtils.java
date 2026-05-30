package su.nightexpress.nightcore.commands;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NodeUtils {

    @NonNull
    public static String formatLabel(@NonNull ExecutableNode node, @NonNull CommandContext context) {
        return formatLabel(node, context.getNodesPriorTo(node));
    }

    @NonNull
    public static String formatLabel(@NonNull ExecutableNode node, @NonNull CommandContextBuilder context) {
        return formatLabel(node, context.getNodesPriorTo(node));
    }

    @NonNull
    private static String formatLabel(@NonNull ExecutableNode node, @NonNull List<CommandNode> priors) {
        String parent = priors.stream().map(CommandNode::getName).collect(Collectors.joining(" "));
        String current = node.getUsage();

        return (parent + " " + current).trim();
    }

    @NonNull
    public static List<CommandNode> getArguments(@NonNull CommandNode source) {
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

    @NonNull
    public static List<CommandNode> getNodesPriorTo(@NonNull Collection<? extends CommandNode> source,
                                                    @NonNull CommandNode target) {
        List<CommandNode> list = new ArrayList<>();

        for (CommandNode node : source) {
            if (node == target) break;
            list.add(node);
        }

        return list;
    }
}
