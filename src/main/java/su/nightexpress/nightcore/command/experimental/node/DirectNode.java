package su.nightexpress.nightcore.command.experimental.node;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.argument.CommandArgument;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArgument;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.builder.DirectNodeBuilder;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;
import su.nightexpress.nightcore.command.experimental.flag.ContentFlag;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectNode extends CommandNode implements DirectExecutor {

    private final List<CommandArgument<?>> arguments;
    private final Map<String, CommandFlag> flags;
    private final DirectExecutor           executor;

    private final int requiredArguments;

    public DirectNode(@NotNull NightCorePlugin plugin,
                      @NotNull String name,
                      @NotNull String[] aliases,
                      @NotNull String description,
                      @Nullable String permission,
                      boolean playerOnly,
                      @NotNull List<CommandArgument<?>> arguments,
                      @NotNull Map<String, CommandFlag> flags,
                      @NotNull DirectExecutor executor) {
        super(plugin, name, aliases, description, permission, playerOnly);
        this.arguments = Collections.unmodifiableList(arguments);
        this.flags = Collections.unmodifiableMap(flags);
        this.executor = executor;
        this.requiredArguments = (int) this.arguments.stream().filter(CommandArgument::isRequired).count();
    }

    @NotNull
    public static DirectNodeBuilder builder(@NotNull NightCorePlugin plugin, @NotNull String... aliases) {
        return new DirectNodeBuilder(plugin, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        return this.executor.execute(context, arguments);
    }

    @Override
    protected boolean onRun(@NotNull CommandContext context) {
        CommandSender sender = context.getSender();
        String[] args = context.getArgs();
        int index = context.getArgumentIndex();
        //System.out.println("plaincmd args = " + Arrays.toString(args));
        //System.out.println("plaincmd index = " + index);

        ParsedArguments parsedArguments = new ParsedArguments();
        for (CommandArgument<?> argument : this.arguments) {
            if (index >= args.length) break;

            if (!argument.hasPermission(sender)) {
                context.errorPermission();
                return false;
            }

            String arg;
            // must be last.
            if (argument.isComplex()) {
                /*StringBuilder builder = new StringBuilder();
                for (int textIndex = index; textIndex < args.length; textIndex++) {
                    String text = args[textIndex];
                    if (text.charAt(0) == CommandFlag.PREFIX) break;

                    if (!builder.isEmpty()) builder.append(" ");
                    builder.append(text);

                    index++;
                }*/
                arg = /*builder.toString();*/Stream.of(args).skip(index).collect(Collectors.joining(" "));
            }
            else {
                arg = args[index++];
            }

            //String
            ParsedArgument<?> parsedArgument = argument.parse(arg, context);
            if (parsedArgument == null) {
                return context.sendFailure(argument.getFailureMessage()
                    .replace(Placeholders.GENERIC_VALUE, arg)
                    .replace(Placeholders.GENERIC_NAME, argument.getLocalized())
                );
            }

            parsedArguments.add(argument, parsedArgument);
        }

        if (parsedArguments.getArgumentMap().size() < this.requiredArguments) {
            return context.sendFailure(CoreLang.ERROR_COMMAND_USAGE.getMessage(this.plugin)
                .replace(Placeholders.COMMAND_LABEL, this.getNameWithParents())
                .replace(Placeholders.COMMAND_USAGE, this.getUsage()));
        }

        if (!this.flags.isEmpty() && index < args.length) {
            for (int flagIndex = index; flagIndex < args.length; flagIndex++) {
                String arg = args[flagIndex];
                if (arg.charAt(0) != CommandFlag.PREFIX) continue;

                int delimiterIndex = arg.indexOf(ContentFlag.DELIMITER);
                boolean hasDelimiter = delimiterIndex != -1;

                String flagName = (hasDelimiter ? arg.substring(0, delimiterIndex) : arg).substring(1);
                CommandFlag flag = this.getFlag(flagName);
                if (flag == null || parsedArguments.hasFlag(flag) || !flag.hasPermission(sender)) continue;

                if (flag instanceof ContentFlag<?> contentFlag) {
                    if (!hasDelimiter) continue;

                    String content = arg.substring(delimiterIndex + 1);
                    if (content.isEmpty()) continue;

                    ParsedArgument<?> parsed = contentFlag.parse(content, context);
                    if (parsed == null) {
                        context.send(CoreLang.ERROR_COMMAND_PARSE_FLAG.getMessage()
                            .replace(Placeholders.GENERIC_VALUE, content)
                            .replace(Placeholders.GENERIC_NAME, flag.getName())
                        );
                        continue;
                    }

                    parsedArguments.addFlag(flag, parsed);
                }
                else {
                    parsedArguments.addFlag(flag, new ParsedArgument<>(true));
                }
            }
        }

        return this.execute(context, parsedArguments);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull TabContext context) {
        if (this.isPlayerOnly() && context.getPlayer() == null) return Collections.emptyList();

        int index = context.getArgs().length - (context.getIndex() + 1);
        //System.out.println("index = " + index);
        //System.out.println("arguments.size() = " + arguments.size());
        if (index >= this.arguments.size()) {
            if (!this.arguments.isEmpty()) {
                CommandArgument<?> latestArgument = this.arguments.get(this.arguments.size() - 1);
                if (latestArgument.isComplex()) return this.getArgumentSamples(latestArgument, context);
            }

            List<String> samples = new ArrayList<>();

            this.getFlags().forEach(commandFlag -> {
                if (!commandFlag.hasPermission(context.getSender())) return;
                if (commandFlag instanceof ContentFlag<?> contentFlag) {
                    samples.add(contentFlag.getSampled());
                }
                else {
                    samples.add(commandFlag.getPrefixed());
                }
            });

            return samples;
        }

        CommandArgument<?> argument = this.arguments.get(index);
        return this.getArgumentSamples(argument, context);
    }

    private List<String> getArgumentSamples(@NotNull CommandArgument<?> argument, @NotNull TabContext context) {
        if (!argument.hasPermission(context.getSender())) return Collections.emptyList();

        //System.out.println("argument = " + argument);
        return argument.getSamples(context);
    }

    @Override
    @NotNull
    public String getUsage() {
        StringBuilder labelBuilder = new StringBuilder();

        this.arguments.forEach(argument -> {
            if (!labelBuilder.isEmpty()) {
                labelBuilder.append(" ");
            }
            labelBuilder.append(argument.getLocalized());
        });

        StringBuilder flagBuilder = new StringBuilder();
        this.flags.values().forEach(commandFlag -> {
            if (!flagBuilder.isEmpty()) {
                flagBuilder.append(" ");
            }
            flagBuilder.append(commandFlag.getPrefixedFormatted());
        });

        if (!flagBuilder.isEmpty()) {
            labelBuilder.append(" ").append(flagBuilder);
        }

        return labelBuilder.toString();
    }

    @NotNull
    public List<CommandArgument<?>> getArguments() {
        return arguments;
    }

    @Nullable
    public CommandFlag getFlag(@NotNull String name) {
        return this.flags.get(name.toLowerCase());
    }

    @NotNull
    public Collection<CommandFlag> getFlags() {
        return this.flags.values();
    }
}
