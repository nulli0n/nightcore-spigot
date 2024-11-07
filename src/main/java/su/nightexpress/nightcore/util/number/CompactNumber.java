package su.nightexpress.nightcore.util.number;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.NumberUtil;

public class CompactNumber {

    private final double value;
    private final NumberShortcut shortcut;

    public CompactNumber(double value, @Nullable NumberShortcut shortcut) {
        this.value = value;
        this.shortcut = shortcut;
    }

    @NotNull
    public String format() {
        String num = NumberUtil.format(NumberUtil.round(this.value, 1));
        if (this.shortcut != null) num += this.shortcut.getSymbol();

        return num;
    }

    public double getValue() {
        return this.value;
    }

    @Nullable
    public NumberShortcut getShortcut() {
        return this.shortcut;
    }
}
