package su.nightexpress.nightcore.util.text.tag.api;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;

@Deprecated
public interface ContentTag {

    @Nullable
    Decorator parse(@NonNull String str);
}
