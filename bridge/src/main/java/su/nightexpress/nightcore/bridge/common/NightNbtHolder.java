package su.nightexpress.nightcore.bridge.common;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import su.nightexpress.nightcore.util.Numbers;

@NullMarked
public class NightNbtHolder {

    private final JsonObject payload;

    private NightNbtHolder(JsonObject payload) {
        this.payload = payload;
    }

    public static NightNbtHolder fromJson(JsonElement nbt) {
        return new NightNbtHolder(nbt instanceof JsonObject object ? object : nbt.getAsJsonObject());
    }

    public static Builder builder() {
        return new Builder();
    }

    public JsonElement payload() {
        return this.payload;
    }

    public String asString() {
        return this.payload.toString();
    }

    public Optional<JsonElement> get(String key) {
        return !this.payload.has(key) ? Optional.empty() : Optional.of(this.payload.get(key));
    }

    private static boolean asBoolean(@Nullable JsonElement element) {
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
    private static Integer asInt(@Nullable JsonElement element) {
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
    private static Float asFloat(@Nullable JsonElement element) {
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
    private static Double asDouble(@Nullable JsonElement element) {
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

    public Optional<String> getText(String key) {
        return this.get(key).map(JsonElement::getAsString);
    }

    public String getText(String key, String fallback) {
        return this.getText(key).orElse(fallback);
    }

    public Optional<Boolean> getBoolean(String key) {
        return this.get(key).map(NightNbtHolder::asBoolean);
    }

    public boolean getBoolean(String key, boolean fallback) {
        return this.getBoolean(key).orElse(fallback);
    }

    public Optional<Double> getDouble(String key) {
        return this.get(key).map(NightNbtHolder::asDouble);
    }

    public double getDouble(String key, double fallback) {
        return this.getDouble(key).orElse(fallback);
    }

    public Optional<Float> getFloat(String key) {
        return this.get(key).map(NightNbtHolder::asFloat);
    }

    public float getFloat(String key, float fallback) {
        return this.getFloat(key).orElse(fallback);
    }

    public Optional<Integer> getInt(String key) {
        return this.get(key).map(NightNbtHolder::asInt);
    }

    public int getInt(String key, int fallback) {
        return this.getInt(key).orElse(fallback);
    }

    @NullMarked
    public static class Builder {

        private final JsonObject object;

        public Builder() {
            this.object = new JsonObject();
        }

        public Builder put(String key, String string) {
            this.object.addProperty(key, string);
            return this;
        }

        public Builder put(String key, boolean value) {
            this.object.addProperty(key, value);
            return this;
        }

        public Builder put(String key, float value) {
            this.object.addProperty(key, value);
            return this;
        }

        public NightNbtHolder build() {
            return NightNbtHolder.fromJson(this.object);
        }
    }
}
