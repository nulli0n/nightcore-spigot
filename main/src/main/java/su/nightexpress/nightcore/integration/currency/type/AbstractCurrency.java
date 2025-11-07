package su.nightexpress.nightcore.integration.currency.type;

import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.integration.currency.impl.DummyCurrency;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Plugins;

import java.util.function.UnaryOperator;

public abstract class AbstractCurrency implements Currency {

    protected final String originalId;
    protected final String internalId;

    public AbstractCurrency(@NotNull String id) {
        this(id, id);
    }

    public AbstractCurrency(@NotNull String originalId, @NotNull String internalId) {
        this.originalId = originalId;
        this.internalId = LowerCase.INTERNAL.apply(internalId);
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.CURRENCY.replacer(this);
    }

    @Override
    public final boolean isDummy() {
        return this == DummyCurrency.INSTANCE;
    }

    @Override
    public double floorIfNeeded(double amount) {
        if (!this.canHandleDecimals()) return Math.floor(amount);

        return amount;
    }

    @Override
    @NotNull
    public String formatValue(double amount) {
        return NumberUtil.format(this.floorIfNeeded(amount));
    }

    @Override
    @NotNull
    public String format(double amount) {
        String format = this.replacePlaceholders().apply(this.getFormat()
            .replace(Placeholders.GENERIC_AMOUNT, this.formatValue(amount))
            .replace(Placeholders.GENERIC_NAME, this.getName())
        );

        if (CoreConfig.ECONOMY_PLACEHOLDERS_API_FORMAT.get() && Plugins.hasPlaceholderAPI()) {
            format = PlaceholderAPI.setPlaceholders(null, format);
        }

        return format;
    }

    @Override
    @NotNull
    public String applyFormat(@NotNull String format, double amount) {
        return this.replacePlaceholders().apply(format).replace(Placeholders.GENERIC_AMOUNT, this.format(amount));
    }

    @Override
    @NotNull
    public String getOriginalId() {
        return this.originalId;
    }

    @Override
    @NotNull
    public String getInternalId() {
        return this.internalId;
    }
}
