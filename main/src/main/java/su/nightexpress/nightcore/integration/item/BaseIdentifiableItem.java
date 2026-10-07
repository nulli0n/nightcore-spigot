package su.nightexpress.nightcore.integration.item;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public abstract class BaseIdentifiableItem implements AdaptedItem {

    protected final ItemIdData idData;

    protected BaseIdentifiableItem(ItemIdData idData) {
        this.idData = idData;
    }

    public ItemIdData getIdData() {
        return this.idData;
    }
}
