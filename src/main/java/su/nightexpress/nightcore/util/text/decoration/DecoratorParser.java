package su.nightexpress.nightcore.util.text.decoration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DecoratorParser {

    @Nullable ParsedDecorator parse(@NotNull String str);
}
