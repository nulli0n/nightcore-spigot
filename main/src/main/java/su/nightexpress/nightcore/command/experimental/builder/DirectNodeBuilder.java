package su.nightexpress.nightcore.command.experimental.builder;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.argument.CommandArgument;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;
import su.nightexpress.nightcore.command.experimental.node.DirectExecutor;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class DirectNodeBuilder extends NodeBuilder<DirectNode, DirectNodeBuilder> {

    private final List<CommandArgument<?>> arguments;
    private final Map<String, CommandFlag> flags;

    private DirectExecutor executor;

    public DirectNodeBuilder(@NonNull NightCorePlugin plugin, @NonNull String... aliases) {
        super(plugin, aliases);
        this.arguments = new ArrayList<>();
        this.flags = new HashMap<>();
    }

    @Override
    @NonNull
    protected DirectNodeBuilder getThis() {
        return this;
    }

    @NonNull
    public DirectNodeBuilder withArgument(@NonNull ArgumentBuilder<?> builder) {
        return this.withArgument(builder.build());
    }

    @NonNull
    public DirectNodeBuilder withArgument(@NonNull CommandArgument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    @NonNull
    public DirectNodeBuilder withFlag(@NonNull FlagBuilder<?, ?> builder) {
        return this.withFlag(builder.build());
    }

    @NonNull
    public DirectNodeBuilder withFlag(@NonNull CommandFlag flag) {
        this.flags.put(flag.getName(), flag);
        return this;
    }

    @NonNull
    public DirectNodeBuilder executes(DirectExecutor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    @NonNull
    public DirectNode build() {
        return new DirectNode(this.plugin, this.name, this.aliases, this.description, this.permission, this.playerOnly, this.arguments, this.flags, this.executor);
    }
}
