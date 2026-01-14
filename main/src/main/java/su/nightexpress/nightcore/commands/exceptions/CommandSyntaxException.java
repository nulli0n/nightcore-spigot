package su.nightexpress.nightcore.commands.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

public class CommandSyntaxException extends Exception {

    private final MessageLocale messageLocale;
    private final String        value;

    // TODO Add support for premade prefixed LangMessages

    public CommandSyntaxException(@NotNull MessageLocale messageLocale, @Nullable String value) {
        this.messageLocale = messageLocale;
        this.value = value;
    }

    @NotNull
    public static CommandSyntaxException generic() {
        return new CommandSyntaxException(CoreLang.COMMAND_SYNTAX_GENERIC_ERROR, null);
    }

    @NotNull
    public static CommandSyntaxException custom(@NotNull MessageLocale locale) {
        return new CommandSyntaxException(locale, null);
    }

    @NotNull
    public static CommandSyntaxException dynamic(@NotNull MessageLocale locale, @NotNull String value) {
        return new CommandSyntaxException(locale, value);
    }

    @NotNull
    public MessageLocale getMessageLocale() {
        return this.messageLocale;
    }

    @Nullable
    public String getValue() {
        return this.value;
    }
}
