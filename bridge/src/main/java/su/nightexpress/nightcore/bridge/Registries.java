package su.nightexpress.nightcore.bridge;

import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;

public class Registries {

    public static final NightRegistry<String, Currency>       CURRENCY     = new NightRegistry<>();
    public static final NightRegistry<String, ItemAdapter<?>> ITEM_ADAPTER = new NightRegistry<>();

}
