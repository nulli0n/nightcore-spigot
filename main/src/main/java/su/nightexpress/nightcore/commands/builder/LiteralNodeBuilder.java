package su.nightexpress.nightcore.commands.builder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.tree.ArgumentNode;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.FlagNode;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiteralNodeBuilder extends ExecutableNodeBuilder<LiteralNode, LiteralNodeBuilder> {

    private final List<ArgumentNode<?>> arguments;
    private final List<FlagNode> flags;

    public LiteralNodeBuilder(@NotNull String literal) {
        super(literal);
        this.arguments = new ArrayList<>();
        this.flags = new ArrayList<>();
    }

    @Override
    @NotNull
    protected LiteralNodeBuilder getThis() {
        return this;
    }

    @NotNull
    public LiteralNodeBuilder withArguments(@NotNull ArgumentNodeBuilder<?>... argument) {
        for (ArgumentNodeBuilder<?> builder : argument) {
            this.arguments.add(builder.build());
        }
        return this.getThis();
    }

    @NotNull
    public LiteralNodeBuilder withArguments(@NotNull ArgumentNode<?>... argument) {
        this.arguments.addAll(Arrays.asList(argument));
        return getThis();
    }

    @NotNull
    public LiteralNodeBuilder withFlags(@NotNull String... flags) {
        for (String name : flags) {
            this.withFlags(new FlagNode(name));
        }
        return this.getThis();
    }

    @NotNull
    public LiteralNodeBuilder withFlags(@NotNull FlagNode... flags) {
        this.flags.addAll(Arrays.asList(flags));
        return getThis();
    }

    @Override
    @NotNull
    public LiteralNode build() {
        LiteralNode result = new LiteralNode(this.name, this.description, this.permission, this.requirements, this.executor);

        List<CommandNode> args = new ArrayList<>();
        args.addAll(this.arguments);
        args.addAll(this.flags);
        result.setArguments(args);

        return result;
    }
}
