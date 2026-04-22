package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class LangEntry extends ChildEntry {

    private final String key;
    private final String fallback;

    public LangEntry(@NonNull EntryGroup parent, @NonNull String key, @Nullable String fallback) {
        super(parent);
        this.key = key;
        this.fallback = fallback;
    }

    @NonNull
    public String getKey() {
        return this.key;
    }

    @Nullable
    public String getFallback() {
        return this.fallback;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NonNull
    public NightComponent toComponent() {
        return NightComponent.translatable(this.key, this.fallback, this.parent.style());
    }
}
