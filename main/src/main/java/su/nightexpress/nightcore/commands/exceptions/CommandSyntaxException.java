package su.nightexpress.nightcore.commands.exceptions;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

public class CommandSyntaxException extends Exception {

    private final MessageLocale messageLocale;
    private final String        value;

    // TODO Add support for premade prefixed LangMessages

    public CommandSyntaxException(@NonNull MessageLocale messageLocale, @Nullable String value) {
        this.messageLocale = messageLocale;
        this.value = value;
    }

    @NonNull
    public static CommandSyntaxException generic() {
        return new CommandSyntaxException(CoreLang.COMMAND_SYNTAX_GENERIC_ERROR, null);
    }

    @NonNull
    public static CommandSyntaxException custom(@NonNull MessageLocale locale) {
        return new CommandSyntaxException(locale, null);
    }

    @NonNull
    public static CommandSyntaxException dynamic(@NonNull MessageLocale locale, @NonNull String value) {
        return new CommandSyntaxException(locale, value);
    }

    @NonNull
    public MessageLocale getMessageLocale() {
        return this.messageLocale;
    }

    @Nullable
    public String getValue() {
        return this.value;
    }
}
