package su.nightexpress.nightcore.util.text.night.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TagContent {

    private final String first;
    private final String second;

    public TagContent(@NotNull String first, @Nullable String second) {
        this.first = first;
        this.second = second;
    }

    public boolean hasBoth() {
        return this.second != null;
    }

    @NotNull
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
