package su.nightexpress.nightcore.integration.item.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;

public class ItemIdData implements Writeable {

    private final String itemId;
    private final int amount;

    public ItemIdData(@NotNull String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    @NotNull
    public static ItemIdData read(@NotNull FileConfig config, @NotNull String path) {
        String itemId = config.getString(path + ".ID", "null");
        int amount = config.getInt(path + ".Amount");

        return new ItemIdData(itemId, amount);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".ID", this.itemId);
        config.set(path + ".Amount", this.amount);
    }

    @NotNull
    public String getItemId() {
        return this.itemId;
    }

    public int getAmount() {
        return this.amount;
    }
}
