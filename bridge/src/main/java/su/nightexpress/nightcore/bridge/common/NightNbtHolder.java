package su.nightexpress.nightcore.bridge.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NightNbtHolder {

    private final JsonObject payload;

    private NightNbtHolder(@NotNull JsonObject payload) {
        this.payload = payload;
    }

    @NotNull
    public static NightNbtHolder fromJson(@NotNull JsonElement nbt) {
        return new NightNbtHolder(nbt instanceof JsonObject object ? object : nbt.getAsJsonObject());
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public JsonElement payload() {
        return this.payload;
    }

    @NotNull
    public String asString() {
        return this.payload.toString();
    }

    @NotNull
    public Optional<JsonElement> get(@NotNull String key) {
        return !this.payload.has(key) ? Optional.empty() : Optional.of(this.payload.get(key));
    }

    private static boolean asBoolean(JsonElement element) {
        if (element == null || element.isJsonNull()) return false;

        if (element.isJsonPrimitive()) {
            var prim = element.getAsJsonPrimitive();
            if (prim.isBoolean()) {
                return prim.getAsBoolean();
            }
            else if (prim.isNumber()) {
                return prim.getAsInt() != 0;
            }
            else if (prim.isString()) {
                String s = prim.getAsString();
                return s.equalsIgnoreCase("true") || s.equals("1");
            }
        }

        return false;
    }

    @NotNull
    public Optional<String> getText(@NotNull String key) {
        return this.get(key).map(JsonElement::getAsString);
    }

    @NotNull
    public String getText(@NotNull String key, @NotNull String fallback) {
        return this.getText(key).orElse(fallback);
    }

    @NotNull
    public Optional<Boolean> getBoolean(@NotNull String key) {
        return this.get(key).map(NightNbtHolder::asBoolean);
    }

    public boolean getBoolean(@NotNull String key, boolean fallback) {
        return this.getBoolean(key).orElse(false);
    }

    @NotNull
    public Optional<Float> getFloat(@NotNull String key) {
        return this.get(key).map(JsonElement::getAsFloat);
    }

    public float getFloat(@NotNull String key, float fallback) {
        return this.getFloat(key).orElse(fallback);
    }

    @NotNull
    public Optional<Integer> getInt(@NotNull String key) {
        return this.get(key).map(JsonElement::getAsInt);
    }

    public int getInt(@NotNull String key, int fallback) {
        return this.getInt(key).orElse(fallback);
    }

    public static class Builder {

        private final JsonObject object;

        public Builder() {
            this.object = new JsonObject();
        }

        @NotNull
        public Builder put(@NotNull String key, @NotNull String string) {
            this.object.addProperty(key, string);
            return this;
        }

        @NotNull
        public Builder put(@NotNull String key, boolean value) {
            this.object.addProperty(key, value);
            return this;
        }

        @NotNull
        public Builder put(@NotNull String key, float value) {
            this.object.addProperty(key, value);
            return this;
        }

        @NotNull
        public NightNbtHolder build() {
            return NightNbtHolder.fromJson(this.object);
        }
    }
}
