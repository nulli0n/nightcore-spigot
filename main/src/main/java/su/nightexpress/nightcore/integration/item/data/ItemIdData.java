package su.nightexpress.nightcore.integration.item.data;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;

public class ItemIdData implements Writeable {

    private final String itemId;
    private final int    amount;

    public ItemIdData(@NonNull String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    @NonNull
    public static ItemIdData read(@NonNull FileConfig config, @NonNull String path) {
        String itemId = config.getString(path + ".ID", "null");
        int amount = config.getInt(path + ".Amount");

        return new ItemIdData(itemId, amount);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".ID", this.itemId);
        config.set(path + ".Amount", this.amount);
    }

    @NonNull
    public String getItemId() {
        return this.itemId;
    }

    public int getAmount() {
        return this.amount;
    }
}
