package su.nightexpress.nightcore.integration.currency.type;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.integration.currency.impl.DummyCurrency;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

import java.util.UUID;
import java.util.function.UnaryOperator;

public abstract class AbstractCurrency implements Currency {

    protected final String originalId;
    protected final String internalId;

    public AbstractCurrency(@NonNull String id) {
        this(id, id);
    }

    public AbstractCurrency(@NonNull String originalId, @NonNull String internalId) {
        this.originalId = originalId;
        this.internalId = LowerCase.INTERNAL.apply(internalId);
    }

    @Override
    @NonNull
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
    @NonNull
    public String formatValue(double amount) {
        return NumberUtil.format(this.floorIfNeeded(amount));
    }

    @Override
    @NonNull
    public String format(double amount) {
        String format = this.replacePlaceholders().apply(this.getFormat()
            .replace(Placeholders.GENERIC_AMOUNT, this.formatValue(amount))
            .replace(Placeholders.GENERIC_NAME, this.getName())
        );

        if (CoreConfig.ECONOMY_PLACEHOLDERS_API_FORMAT.get()) {
            format = CommonPlaceholders.setPAPIPlaceholders(null, format);
        }

        return format;
    }

    @Override
    @NonNull
    public String applyFormat(@NonNull String format, double amount) {
        return this.replacePlaceholders().apply(format).replace(Placeholders.GENERIC_AMOUNT, this.format(amount));
    }

    @Override
    public double queryBalance(@NonNull Player player) {
        return this.queryBalance(player.getUniqueId());
    }

    @Override
    public double queryBalance(@NonNull UUID playerId) {
        return Players.findById(playerId).map(this::queryBalanceDirect).orElse(0D);
    }

    protected abstract double queryBalanceDirect(@NonNull Player player);

    @Override
    public void deposit(@NonNull Player player, double amount) {
        if (player.isOnline()) {
            this.depositDirect(player, amount);
            return;
        }

        this.depositAsync(player, amount);
    }

    @Override
    public void deposit(@NonNull UUID playerId, double amount) {
        Player player = Players.getPlayer(playerId);
        if (player != null) {
            this.depositDirect(player, amount);
            return;
        }

        this.depositAsync(playerId, amount);
    }

    protected abstract void depositDirect(@NonNull Player player, double amount);

    @Override
    public void withdraw(@NonNull Player player, double amount) {
        if (player.isOnline()) {
            this.withdrawDirect(player, amount);
            return;
        }

        this.withdrawAsync(player, amount);
    }

    @Override
    public void withdraw(@NonNull UUID playerId, double amount) {
        Player player = Players.getPlayer(playerId);
        if (player != null) {
            this.withdrawDirect(player, amount);
            return;
        }

        this.withdrawAsync(playerId, amount);
    }

    protected abstract void withdrawDirect(@NonNull Player player, double amount);

    @Override
    @NonNull
    public String getOriginalId() {
        return this.originalId;
    }

    @Override
    @NonNull
    public String getInternalId() {
        return this.internalId;
    }
}
