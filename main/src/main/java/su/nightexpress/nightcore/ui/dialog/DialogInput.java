package su.nightexpress.nightcore.ui.dialog;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@Deprecated
public class DialogInput {

    private final String text;
    private final String textRaw;
    private final String textLegacy;

    //    public DialogInput(@NonNull AsyncPlayerChatEvent event) {
    //        this(event.getMessage());
    //    }

    public DialogInput(@NonNull String text) {
        this.text = text;
        this.textRaw = NightMessage.stripTags(text);
        this.textLegacy = NightMessage.asLegacy(text);
    }

    public int asIntAbs() {
        return this.asIntAbs(0);
    }

    public int asIntAbs(int def) {
        return NumberUtil.getIntegerAbs(this.textRaw, def);
    }

    public int asInt(int def) {
        return NumberUtil.getAnyInteger(this.textRaw, def);
    }

    public double asDoubleAbs() {
        return this.asDoubleAbs(0D);
    }

    public double asDoubleAbs(double def) {
        return NumberUtil.getDoubleAbs(this.textRaw, def);
    }

    public double asDouble(double def) {
        return NumberUtil.getAnyDouble(this.textRaw, def);
    }

    //    @NonNull
    //    public UniDouble asUniDouble() {
    //        return this.asUniDouble(0, 0);
    //    }
    //
    //    @NonNull
    //    public UniDouble asUniDouble(double min, double max) {
    //        String[] split = this.textRaw.split(" ");
    //        return UniDouble.of(NumberUtil.getDoubleAbs(split[0], min), NumberUtil.getDoubleAbs(split.length >= 2 ? split[1] : split[0], max));
    //    }
    //
    //    @NonNull
    //    public UniInt asUniInt() {
    //        return this.asUniDouble().asInt();
    //    }
    //
    //    public double asAnyDouble(double def) {
    //        return NumberUtil.getAnyDouble(this.textRaw, def);
    //    }

    @Nullable
    public <E extends Enum<E>> E asEnum(@NonNull Class<E> clazz) {
        return StringUtil.getEnum(this.textRaw, clazz).orElse(null);
    }

    @NonNull
    public <E extends Enum<E>> E asEnum(@NonNull Class<E> clazz, @NonNull E def) {
        return StringUtil.getEnum(this.textRaw, clazz).orElse(def);
    }

    @NonNull
    public String getText() {
        return this.text;
    }

    @NonNull
    public String getTextRaw() {
        return this.textRaw;
    }

    @NonNull
    public String getTextLegacy() {
        return this.textLegacy;
    }
}
