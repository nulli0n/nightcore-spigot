package su.nightexpress.nightcore.integration.item;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public abstract class BaseProviderItem implements AdaptedItem {

    protected final String providerId;

    public BaseProviderItem(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String getProviderId() {
        return this.providerId;
    }
}
