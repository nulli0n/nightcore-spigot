package su.nightexpress.nightcore.bridge.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Numbers;

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

    @Nullable
    private static Integer asInt(JsonElement element) {
        if (element == null || element.isJsonNull()) return null;

        if (element.isJsonPrimitive()) {
            var prim = element.getAsJsonPrimitive();
            if (prim.isNumber()) {
                return prim.getAsInt();
            }
            else if (prim.isString()) {
                String s = prim.getAsString();
                return Numbers.parseInteger(s).orElse(null);
            }
        }

        return null;
    }

    @Nullable
    private static Float asFloat(JsonElement element) {
        if (element == null || element.isJsonNull()) return null;

        if (element.isJsonPrimitive()) {
            var prim = element.getAsJsonPrimitive();
            if (prim.isNumber()) {
                return prim.getAsFloat();
            }
            else if (prim.isString()) {
                String s = prim.getAsString();
                return Numbers.parseFloat(s).orElse(null);
            }
        }

        return null;
    }

    @Nullable
    private static Double asDouble(JsonElement element) {
        if (element == null || element.isJsonNull()) return null;

        if (element.isJsonPrimitive()) {
            var prim = element.getAsJsonPrimitive();
            if (prim.isNumber()) {
                return prim.getAsDouble();
            }
            else if (prim.isString()) {
                String s = prim.getAsString();
                return Numbers.parseDouble(s).orElse(null);
            }
        }

        return null;
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
    public Optional<Double> getDouble(@NotNull String key) {
        return this.get(key).map(NightNbtHolder::asDouble);
    }

    public double getDouble(@NotNull String key, double fallback) {
        return this.getDouble(key).orElse(fallback);
    }

    @NotNull
    public Optional<Float> getFloat(@NotNull String key) {
        return this.get(key).map(NightNbtHolder::asFloat);
    }

    public float getFloat(@NotNull String key, float fallback) {
        return this.getFloat(key).orElse(fallback);
    }

    @NotNull
    public Optional<Integer> getInt(@NotNull String key) {
        return this.get(key).map(NightNbtHolder::asInt);
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
