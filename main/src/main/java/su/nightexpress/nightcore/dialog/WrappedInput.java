package su.nightexpress.nightcore.dialog;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.night.NightMessage;
import su.nightexpress.nightcore.util.wrapper.UniDouble;
import su.nightexpress.nightcore.util.wrapper.UniInt;

@Deprecated
public class WrappedInput {

    private final String text;
    private final String textRaw;
    private final String textColored;

    public WrappedInput(@NonNull AsyncPlayerChatEvent event) {
        this(event.getMessage());
    }

    public WrappedInput(@NonNull String text) {
        this.text = text;
        this.textRaw = NightMessage.stripTags(text);
        this.textColored = NightMessage.asLegacy(text);
    }

    public int asInt() {
        return this.asInt(0);
    }

    public int asInt(int def) {
        return NumberUtil.getIntegerAbs(this.getTextRaw(), def);
    }

    public int asAnyInt(int def) {
        return NumberUtil.getAnyInteger(this.getTextRaw(), def);
    }

    public double asDouble() {
        return this.asDouble(0D);
    }

    public double asDouble(double def) {
        return NumberUtil.getDoubleAbs(this.getTextRaw(), def);
    }

    @NonNull
    public UniDouble asUniDouble() {
        return this.asUniDouble(0, 0);
    }

    @NonNull
    public UniDouble asUniDouble(double min, double max) {
        String[] split = this.getTextRaw().split(" ");
        return UniDouble.of(NumberUtil.getDoubleAbs(split[0], min), NumberUtil.getDoubleAbs(
            split.length >= 2 ? split[1] : split[0], max));
    }

    @NonNull
    public UniInt asUniInt() {
        return this.asUniDouble().asInt();
    }

    public double asAnyDouble(double def) {
        return NumberUtil.getAnyDouble(this.getTextRaw(), def);
    }

    @Nullable
    public <E extends Enum<E>> E asEnum(@NonNull Class<E> clazz) {
        return StringUtil.getEnum(this.getTextRaw(), clazz).orElse(null);
    }

    @NonNull
    public <E extends Enum<E>> E asEnum(@NonNull Class<E> clazz, @NonNull E def) {
        return StringUtil.getEnum(this.getTextRaw(), clazz).orElse(def);
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    public String getTextRaw() {
        return textRaw;
    }

    @NonNull
    public String getTextColored() {
        return textColored;
    }
}
