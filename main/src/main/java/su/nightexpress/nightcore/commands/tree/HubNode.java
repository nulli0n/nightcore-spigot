package su.nightexpress.nightcore.commands.tree;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.NodeExecutor;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.context.Suggestions;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.*;

public class HubNode extends ExecutableNode {

    private String localizedName;

    public HubNode(@NonNull String name,
                   @NonNull String description,
                   @Nullable String permission,
                   @NonNull List<CommandRequirement> requirements,
                   @Nullable NodeExecutor executor,
                   @Nullable String localizedName,
                   boolean useHelpCommand) {
        super(name, description, permission, requirements, executor);
        this.setLocalizedName(localizedName);

        if (useHelpCommand) {
            this.addBranch(Commands.literal("help")
                .description(CoreLang.COMMAND_HELP_DESC)
                .executes((context, arguments) -> sendCommandList(context))
                .build());
        }
    }

    public void setLocalizedName(@Nullable String localizedName) {
        this.localizedName = localizedName;
    }

    @Override
    @NonNull
    public Collection<? extends CommandNode> getRelevantNodes(@NonNull ArgumentReader reader) {
        if (!this.children.isEmpty()) {
            String arg = reader.getCursorArgument();
            CommandNode literalNode = this.children.get(LowerCase.USER_LOCALE.apply(arg));
            if (literalNode != null) return Collections.singleton(literalNode);
        }

        return Collections.emptyList();
    }

    public void addBranch(@NonNull ExecutableNode node) {
        /*if ((node instanceof ArgumentCommandNode<?>)) {
            throw new UnsupportedOperationException("Could not add ArgumentCommandNode to the ChainCommandNode!");
        }*/
        this.addChildren(node);
    }

    @Override
    public boolean run(@NonNull CommandContext context) {
        if (this.executor != null) {
            this.executor.run(context, context.getArguments());
            return true;
        }

        this.sendCommandList(context);
        return true;
    }

    @Override
    public void parse(@NonNull ArgumentReader reader,
                      @NonNull CommandContextBuilder contextBuilder) throws CommandSyntaxException {
        contextBuilder.withNode(this, reader.getCursor());
        contextBuilder.withExecutor(this);
    }

    @Override
    public void suggests(@NonNull ArgumentReader reader, @NonNull CommandContext context,
                         @NonNull Suggestions suggestions) {
        CommandSender sender = context.getSender();

        suggestions.setSuggestions(this.children.values().stream()
            .filter(child -> child.hasPermission(sender))
            .map(CommandNode::getName)
            .toList());
    }

    @Override
    protected void provideSuggestions(@NonNull ArgumentReader reader, @NonNull CommandContext context,
                                      @NonNull Suggestions suggestions) {

    }

    @Override
    @NonNull
    public String getUsage() {
        return this.name;
    }

    @Override
    @NonNull
    public String getLocalizedName() {
        return this.localizedName != null ? this.localizedName : this.getName();
    }

    private boolean sendCommandList(@NonNull CommandContext context) {
        CommandSender sender = context.getSender();

        CoreLang.HELP_PAGE_GENERAL.message().send(sender, replacer -> replacer
            .replace(Placeholders.GENERIC_NAME, this.getLocalizedName())
            .replace(Placeholders.GENERIC_ENTRY, list -> {
                this.getChildren().stream().sorted(Comparator.comparing(CommandNode::getName)).forEach(children -> {
                    if (!children.hasPermission(sender)) return;
                    if (!(children instanceof ExecutableNode executable)) return;

                    list.add(CoreLang.HELP_PAGE_ENTRY.text()
                        .replace(Placeholders.GENERIC_COMMAND, (NodeUtils.formatLabel(this, context) + " " + executable
                            .getUsage()).trim())
                        .replace(Placeholders.GENERIC_DESCRIPTION, executable.getDescription())
                    );
                });
            }));

        return true;
    }
}
