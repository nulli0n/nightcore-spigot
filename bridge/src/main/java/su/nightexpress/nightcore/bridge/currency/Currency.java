package su.nightexpress.nightcore.bridge.currency;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.UnaryOperator;

public interface Currency {

    boolean isDummy();

    @NotNull UnaryOperator<String> replacePlaceholders();

    double floorIfNeeded(double amount);

    @NotNull String formatValue(double amount);

    @NotNull String format(double amount);

    @NotNull String applyFormat(@NotNull String format, double amount);



    double getBalance(@NotNull Player player);

    double getBalance(@NotNull UUID playerId);

    void give(@NotNull Player player, double amount);

    void give(@NotNull UUID playerId, double amount);

    void take(@NotNull Player player, double amount);

    void take(@NotNull UUID playerId, double amount);



    boolean canHandleDecimals();

    boolean canHandleOffline();



    @NotNull String getOriginalId();

    @NotNull String getInternalId();

    @NotNull String getName();

    @NotNull String getFormat();

    @NotNull ItemStack getIcon(); // TODO NightItem
}
