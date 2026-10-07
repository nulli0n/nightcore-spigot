package su.nightexpress.nightcore.bridge;

import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;

public class Registries {

    @Deprecated
    public static final NightRegistry<String, Currency> CURRENCY = new NightRegistry<>();

}
