package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class TextEntry extends ChildEntry {

    private final String text;

    public TextEntry(@NonNull EntryGroup parent, @NonNull String text) {
        super(parent);
        this.text = text;
    }

    @NonNull
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
    @NonNull
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
