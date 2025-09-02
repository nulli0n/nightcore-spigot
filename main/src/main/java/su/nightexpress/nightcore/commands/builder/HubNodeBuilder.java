package su.nightexpress.nightcore.commands.builder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.tree.HubNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.locale.entry.TextLocale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HubNodeBuilder extends ExecutableNodeBuilder<HubNode, HubNodeBuilder> {

    private final List<ExecutableNode> branches;

    private String  localizedName;
    private boolean useHelpCommand;

    public HubNodeBuilder(@NotNull String name) {
        super(name);
        this.branches = new ArrayList<>();
        this.useHelpCommand = true;
    }

    @Override
    @NotNull
    protected HubNodeBuilder getThis() {
        return this;
    }

    @Override
    @NotNull
    public HubNode build() {
        HubNode result = new HubNode(this.name, this.description, this.permission, this.requirements, this.executor, this.localizedName, this.useHelpCommand);

        this.branches.forEach(result::addBranch);

        return result;
    }

    @NotNull
    public HubNodeBuilder branch(@NotNull ExecutableNodeBuilder<?, ?>... branches) {
        for (ExecutableNodeBuilder<?, ?> builder : branches) {
            this.branches.add(builder.build());
        }
        return this.getThis();
    }

    @NotNull
    public HubNodeBuilder branch(@NotNull ExecutableNode... branches) {
        this.branches.addAll(Arrays.asList(branches));
        return this.getThis();
    }

    @NotNull
    public HubNodeBuilder localized(@NotNull TextLocale locale) {
        return this.localized(locale.text());
    }

    @NotNull
    public HubNodeBuilder localized(@NotNull String localizedName) {
        this.localizedName = localizedName;
        return this.getThis();
    }

    @NotNull
    public HubNodeBuilder withHelpCommand(boolean useHelpCommand) {
        this.useHelpCommand = useHelpCommand;
        return this.getThis();
    }
}
