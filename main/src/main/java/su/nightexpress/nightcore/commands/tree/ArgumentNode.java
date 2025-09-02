package su.nightexpress.nightcore.commands.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.context.ParsedArgument;
import su.nightexpress.nightcore.commands.context.Suggestions;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.Collection;
import java.util.List;

public class ArgumentNode<T> extends CommandNode /*implements ArgumentTree*/ {

    private final ArgumentType<T> type;
    private final boolean required;

    private String localizedName;
    private SuggestionsProvider customSuggestions;

    public ArgumentNode(@NotNull String name,
                        @NotNull ArgumentType<T> type,
                        @Nullable String permission,
                        @NotNull List<CommandRequirement> requirements,
                        boolean required,
                        @Nullable String localizedName,
                        @Nullable SuggestionsProvider customSuggestions) {
        super(name, permission, requirements);
        this.type = type;
        this.required = required;
        this.setLocalizedName(localizedName);
        this.setCustomSuggestions(customSuggestions);
    }

    public void setLocalizedName(@Nullable String localizedName) {
        this.localizedName = localizedName;
    }

    public void setCustomSuggestions(@Nullable SuggestionsProvider customSuggestions) {
        this.customSuggestions = customSuggestions;
    }

    @Override
    @NotNull
    public Collection<? extends CommandNode> getRelevantNodes(@NotNull ArgumentReader reader) {
        return this.getChildren(); // Only one (or none) children node is expected.
    }

    @Override
    public void parse(@NotNull ArgumentReader reader, @NotNull CommandContextBuilder contextBuilder) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        String string = reader.getCursorArgument();

        T result = this.type.parse(contextBuilder, string);

        ParsedArgument<T> parsed = new ParsedArgument<>(result, cursor);
        contextBuilder.withArgument(this.name, parsed);
        contextBuilder.withNode(this, cursor);
    }

    @Override
    public void provideSuggestions(@NotNull ArgumentReader reader, @NotNull CommandContext context, @NotNull Suggestions suggestions) {
        SuggestionsProvider provider = this.customSuggestions == null ? this.type : this.customSuggestions;
        String input = reader.getCursorArgument();
        List<String> values = provider.suggest(reader, context);
        suggestions.setSuggestions(Lists.getSequentialMatches(values, input));
    }

    @NotNull
    private List<CommandNode> getArguments() {
        return NodeUtils.getArguments(this);
    }

    @Override
    public boolean hasRequiredArguments() {
        return this.getArguments().stream().anyMatch(CommandNode::isRequired);
    }

    @Override
    @NotNull
    public String getUsage() {
        String format = (this.isRequired() ? CoreLang.COMMAND_USAGE_REQUIRED_ARGUMENT : CoreLang.COMMAND_USAGE_OPTIONAL_ARGUMENT).text();

        return format.replace(Placeholders.GENERIC_NAME, this.getLocalizedName());
    }

    @Override
    @NotNull
    public String getLocalizedName() {
        return this.localizedName != null ? this.localizedName : this.getName();
    }

    @NotNull
    public ArgumentType<T> getType() {
        return this.type;
    }

    @NotNull
    public SuggestionsProvider getCustomSuggestions() {
        return this.customSuggestions;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }
}
