package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class KeybindEntry extends ChildEntry {

    private final String key;

    public KeybindEntry(@NonNull EntryGroup parent, @NonNull String key) {
        super(parent);
        this.key = key;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NonNull
    public NightComponent toComponent() {
        return NightComponent.keybind(this.key, this.parent.style());
    }
}
