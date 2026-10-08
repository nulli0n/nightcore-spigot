package su.nightexpress.nightcore.integration.item;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedDataItem;

@NullMarked
public abstract class BaseProviderDataItem<D> extends BaseProviderItem implements AdaptedDataItem<D> {

    protected final D data;

    public BaseProviderDataItem(String providerId, D data) {
        super(providerId);
        this.data = data;
    }

    @Override
    public D getData() {
        return this.data;
    }
}
