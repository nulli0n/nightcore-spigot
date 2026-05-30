package su.nightexpress.nightcore.commands.builder;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.tree.ArgumentNode;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.FlagNode;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiteralNodeBuilder extends ExecutableNodeBuilder<LiteralNode, LiteralNodeBuilder> {

    private final List<ArgumentNode<?>> arguments;
    private final List<FlagNode>        flags;

    public LiteralNodeBuilder(@NonNull String literal) {
        super(literal);
        this.arguments = new ArrayList<>();
        this.flags = new ArrayList<>();
    }

    @Override
    @NonNull
    protected LiteralNodeBuilder getThis() {
        return this;
    }

    @NonNull
    public LiteralNodeBuilder withArguments(@NonNull ArgumentNodeBuilder<?>... argument) {
        for (ArgumentNodeBuilder<?> builder : argument) {
            this.arguments.add(builder.build());
        }
        return this.getThis();
    }

    @NonNull
    public LiteralNodeBuilder withArguments(@NonNull ArgumentNode<?>... argument) {
        this.arguments.addAll(Arrays.asList(argument));
        return getThis();
    }

    @NonNull
    public LiteralNodeBuilder withFlags(@NonNull String... flags) {
        for (String name : flags) {
            this.withFlags(new FlagNode(name));
        }
        return this.getThis();
    }

    @NonNull
    public LiteralNodeBuilder withFlags(@NonNull FlagNode... flags) {
        this.flags.addAll(Arrays.asList(flags));
        return getThis();
    }

    @Override
    @NonNull
    public LiteralNode build() {
        LiteralNode result = new LiteralNode(this.name, this.description, this.permission, this.requirements, this.executor);

        List<CommandNode> args = new ArrayList<>();
        args.addAll(this.arguments);
        args.addAll(this.flags);
        result.setArguments(args);

        return result;
    }
}
