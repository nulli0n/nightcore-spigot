package su.nightexpress.nightcore.commands.tree;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.NodeExecutor;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.context.Suggestions;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

import java.util.*;

public class LiteralNode extends ExecutableNode /*implements ArgumentTree*/ {

    private final Map<String, FlagNode> flags;

    public LiteralNode(@NonNull String name,
                       @NonNull String description,
                       @Nullable String permission,
                       @NonNull List<CommandRequirement> requirements,
                       @Nullable NodeExecutor executor) {
        super(name, description, permission, requirements, executor);
        this.flags = new LinkedHashMap<>();
    }

    @Override
    @NonNull
    public Collection<? extends CommandNode> getRelevantNodes(@NonNull ArgumentReader reader) {
        return this.getChildren(); // No validation required because LiteralCommandNode holds only one (or none) children argument.
    }

    public void setArguments(@NonNull CommandNode... arguments) {
        this.setArguments(Arrays.asList(arguments));
    }

    public void setArguments(@NonNull List<? extends CommandNode> arguments) {
        CommandNode parent = this;

        while (!arguments.isEmpty()) {
            CommandNode first = arguments.removeFirst();
            /*if (parent instanceof ArgumentNode<?> argumentNode && !argumentNode.isRequired() && first.isRequired()) {
                throw new IllegalStateException("Adding required argument after optional one!");
            }*/

            parent.getChildren().clear(); // Ensure the parent class won't hold more than 1 children.
            parent.addChildren(first); // Add children argument.
            parent = first; // Now use children as parent for a next argument.

            if (first instanceof FlagNode flagNode) {
                this.flags.put(first.getName(), flagNode);
            }
        }
    }

    /*public void setFlags(@NonNull List<FlagNode> flags) {
        flags.forEach(this::addChildren);
    }*/

    @Override
    public boolean run(@NonNull CommandContext context) {
        if (this.executor != null) {
            this.executor.run(context, context.getArguments());
            return true;
        }
        return false;
    }

    @Override
    public void parse(@NonNull ArgumentReader reader,
                      @NonNull CommandContextBuilder contextBuilder) throws CommandSyntaxException {
        contextBuilder.withNode(this, reader.getCursor());
        contextBuilder.withExecutor(this);
    }

    @Override
    protected void provideSuggestions(@NonNull ArgumentReader reader, @NonNull CommandContext context,
                                      @NonNull Suggestions suggestions) {
        suggestions.setSuggestions(List.of(this.name));
    }

    @NonNull
    public List<CommandNode> getArguments() {
        return NodeUtils.getArguments(this);
    }

    @NonNull
    public List<FlagNode> getFlags() {
        return new ArrayList<>(this.flags.values());
    }

    @Nullable
    public FlagNode getFlag(@NonNull String name) {
        return this.flags.get(name);
    }

    @Override
    public boolean hasRequiredArguments() {
        return this.getArguments().stream().anyMatch(CommandNode::isRequired);
    }

    @Override
    @NonNull
    public String getUsage() {
        StringBuilder builder = new StringBuilder().append(this.name);

        this.getArguments().forEach(node -> {
            builder.append(" ");
            builder.append(node.getUsage());
        });

        return builder.toString();
    }
}
