package su.nightexpress.nightcore.bridge;

import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;

public class Registries {

    public static final NightRegistry<Currency>       CURRENCY     = NightRegistry.create();
    public static final NightRegistry<ItemAdapter<?>> ITEM_ADAPTER = NightRegistry.create();

}
