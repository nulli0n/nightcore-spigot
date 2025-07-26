package su.nightexpress.nightcore.bridge.text.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class NightTranslationArgument {

    private static final NightComponent TRUE  = NightComponent.text("true");
    private static final NightComponent FALSE = NightComponent.text("false");

    @NotNull
    public static NightTranslationArgument bool(boolean value) {
        return new NightTranslationArgument(value);
    }

    @NotNull
    public static NightTranslationArgument numeric(@NotNull Number value) {
        return new NightTranslationArgument(value);
    }

    @NotNull
    public static NightTranslationArgument component(@NotNull NightComponent value) {
        return new NightTranslationArgument(value);
    }

    private final Object value;

    NightTranslationArgument(@NotNull Object value) {
        this.value = value;
    }

    @NotNull
    public Object value() {
        return this.value;
    }

    @NotNull
    public NightComponent asComponent() {
        if (this.value instanceof NightComponent) {
            return (NightAbstractComponent) this.value;
        }

        if (this.value instanceof Boolean b) {
            return b ? TRUE : FALSE;
        }

        return NightComponent.text(String.valueOf(this.value));
    }
}
