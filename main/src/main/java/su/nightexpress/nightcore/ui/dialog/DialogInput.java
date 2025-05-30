package su.nightexpress.nightcore.ui.dialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.NightMessage;

public class DialogInput {

    private final String text;
    private final String textRaw;
    private final String textLegacy;

//    public DialogInput(@NotNull AsyncPlayerChatEvent event) {
//        this(event.getMessage());
//    }

    public DialogInput(@NotNull String text) {
        this.text = text;
        this.textRaw = NightMessage.stripAll(text);
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

//    @NotNull
//    public UniDouble asUniDouble() {
//        return this.asUniDouble(0, 0);
//    }
//
//    @NotNull
//    public UniDouble asUniDouble(double min, double max) {
//        String[] split = this.textRaw.split(" ");
//        return UniDouble.of(NumberUtil.getDoubleAbs(split[0], min), NumberUtil.getDoubleAbs(split.length >= 2 ? split[1] : split[0], max));
//    }
//
//    @NotNull
//    public UniInt asUniInt() {
//        return this.asUniDouble().asInt();
//    }
//
//    public double asAnyDouble(double def) {
//        return NumberUtil.getAnyDouble(this.textRaw, def);
//    }

    @Nullable
    public <E extends Enum<E>> E asEnum(@NotNull Class<E> clazz) {
        return StringUtil.getEnum(this.textRaw, clazz).orElse(null);
    }

    @NotNull
    public <E extends Enum<E>> E asEnum(@NotNull Class<E> clazz, @NotNull E def) {
        return StringUtil.getEnum(this.textRaw, clazz).orElse(def);
    }

    @NotNull
    public String getText() {
        return this.text;
    }

    @NotNull
    public String getTextRaw() {
        return this.textRaw;
    }

    @NotNull
    public String getTextLegacy() {
        return this.textLegacy;
    }
}
