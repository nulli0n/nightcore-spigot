package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;

public abstract class LangEntry implements LangElement {

    protected final String path;
    protected final String defaultText;

    public LangEntry(@NotNull String path, @NotNull String defaultText) {
        this.path = path;
        this.defaultText = defaultText;
    }

    @NotNull
    public String getPath() {
        return this.path;
    }

    @NotNull
    public String getDefaultText() {
        return this.defaultText;
    }
}
