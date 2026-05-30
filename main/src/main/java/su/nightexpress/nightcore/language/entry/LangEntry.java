package su.nightexpress.nightcore.language.entry;

import org.jspecify.annotations.NonNull;

@Deprecated
public abstract class LangEntry implements LangElement {

    protected final String path;
    protected final String defaultText;

    public LangEntry(@NonNull String path, @NonNull String defaultText) {
        this.path = path;
        this.defaultText = defaultText;
    }

    @NonNull
    public String getPath() {
        return this.path;
    }

    @NonNull
    public String getDefaultText() {
        return this.defaultText;
    }
}
