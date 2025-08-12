package su.nightexpress.nightcore.util.text.night.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class KeybindEntry extends ChildEntry {

    private final String key;

    public KeybindEntry(@NotNull EntryGroup parent, @NotNull String key) {
        super(parent);
        this.key = key;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NotNull
    public NightComponent toComponent() {
        return NightComponent.keybind(this.key, this.parent.style());
    }
}
