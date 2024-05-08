package su.nightexpress.nightcore.database.serialize;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.nightcore.util.ItemNbt;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("data64", ItemNbt.compress(item));
        return object;
    }

    @Override
    public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return ItemNbt.decompress(object.get("data64").getAsString());
    }

}
