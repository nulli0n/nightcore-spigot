package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;

public interface ContentTag {

    @Nullable
    Decorator parse(@NotNull String str);
}
