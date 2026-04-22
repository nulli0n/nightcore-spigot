package su.nightexpress.nightcore.util.text.night.wrapper;

import org.jspecify.annotations.NonNull;

public interface TagWrapper {

    @NonNull
    static SimpleTagWrapper simple(@NonNull String tag) {
        return new SimpleTagWrapper(tag, new String[0]);
    }

    @NonNull
    static SimpleTagWrapper withArguments(@NonNull String tag, @NonNull String... arguments) {
        return new SimpleTagWrapper(tag, arguments);
    }

    @NonNull
    String wrap(@NonNull String string);

    @NonNull
    default TagWrapper and(@NonNull TagWrapper other) {
        return str -> this.wrap(other.wrap(str));
    }
}
