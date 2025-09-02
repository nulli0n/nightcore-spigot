package su.nightexpress.nightcore.util.text.night.wrapper;

import org.jetbrains.annotations.NotNull;

public interface TagWrapper {

    @NotNull
    static SimpleTagWrapper simple(@NotNull String tag) {
        return new SimpleTagWrapper(tag, new String[0]);
    }

    @NotNull
    static SimpleTagWrapper withArguments(@NotNull String tag, @NotNull String... arguments) {
        return new SimpleTagWrapper(tag, arguments);
    }

    @NotNull String wrap(@NotNull String string);

    @NotNull
    default TagWrapper and(@NotNull TagWrapper other) {
        return str -> this.wrap(other.wrap(str));
    }
}
