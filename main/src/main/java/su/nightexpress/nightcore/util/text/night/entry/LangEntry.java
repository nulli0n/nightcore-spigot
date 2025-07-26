package su.nightexpress.nightcore.util.text.night.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class LangEntry extends ChildEntry {

    private final String key;
    private final String fallback;

    public LangEntry(@NotNull EntryGroup parent, @NotNull String key, @Nullable String fallback) {
        super(parent);
        this.key = key;
        this.fallback = fallback;
    }

    @NotNull
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
    @NotNull
    public NightComponent toComponent() {
        return NightComponent.translatable(this.key, this.fallback, this.parent.style());
    }
}
