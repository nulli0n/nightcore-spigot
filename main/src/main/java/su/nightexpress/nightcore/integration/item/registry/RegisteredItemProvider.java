package su.nightexpress.nightcore.integration.item.registry;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.integration.item.ItemProvider;

public record RegisteredItemProvider<I extends AdaptedItem>(ItemProvider provider, ConfigCodec<I> codec) {

}
