package su.nightexpress.nightcore.language.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.message.MessageOptions;

public interface MessageDecorator {

    void apply(@NotNull MessageOptions options, @NotNull String value);
}
