package su.nightexpress.nightcore.bridge.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

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

    @Nullable
    public String getText(@NotNull String key) {
        return !this.payload.has(key) ? null : this.payload.get(key).getAsString();
    }

    @Nullable
    public String getText(@NotNull String key, @NotNull String fallback) {
        return !this.payload.has(key) ? fallback : this.payload.get(key).getAsString();
    }

    @Nullable
    public Boolean getBoolean(@NotNull String key) {
        return !this.payload.has(key) ? null : this.payload.get(key).getAsBoolean();
    }

    public boolean getBoolean(@NotNull String key, boolean fallback) {
        return this.payload.has(key) && this.payload.get(key).getAsBoolean();
    }

    @Nullable
    public Float getFloat(@NotNull String key) {
        return !this.payload.has(key) ? null : this.payload.get(key).getAsFloat();
    }

    public float getFloat(@NotNull String key, float fallback) {
        return !this.payload.has(key) ? fallback : this.payload.get(key).getAsFloat();
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
