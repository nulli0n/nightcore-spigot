package su.nightexpress.nightcore.util.text.night.tag;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class TagContent {

    private final String first;
    private final String second;

    public TagContent(@NonNull String first, @Nullable String second) {
        this.first = first;
        this.second = second;
    }

    public boolean hasBoth() {
        return this.second != null;
    }

    @NonNull
    public String first() {
        return this.first;
    }

    public String second() {
        return this.second;
    }

    @Override
    public String toString() {
        return "TagContent{" + "first='" + first + '\'' + ", second='" + second + '\'' + '}';
    }
}
