package su.nightexpress.nightcore.util.text.night.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class TextEntry extends ChildEntry {

    private final String text;

    public TextEntry(@NotNull EntryGroup parent, @NotNull String text) {
        super(parent);
        this.text = text;
    }

    @NotNull
    public String text() {
        return this.text;
    }

    @Override
    public int textLength() {
        int legnth = 0;
        for (int index = 0; index < this.text.length(); index++) {
            char letter = this.text.charAt(index);
            if (Character.isWhitespace(letter)) continue;

            legnth++;
        }

        return legnth;
    }

    @Override
    @NotNull
    public NightComponent toComponent() {
        return NightComponent.text(this.text, this.parent.style());
    }

    @Override
    public String toString() {
        return "TextEntry{" +
            "text='" + text + '\'' +
            '}';
    }
}
