package su.nightexpress.nightcore.bridge.text.contents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NightPlayerHeadObjectContents implements NightObjectContents {

    public static final boolean DEFAULT_HAT = true;

    private final @Nullable String                     name;
    private final @Nullable UUID                       id;
    private final           List<NightProfileProperty> properties;
    private final           boolean                    hat;
    private final @Nullable NightKey                   texture;

    NightPlayerHeadObjectContents(@Nullable String name,
                                  @Nullable UUID id,
                                  @NotNull List<NightProfileProperty> properties,
                                  boolean hat,
                                  @Nullable NightKey texture) {
        this.name = name;
        this.id = id;
        if (properties.isEmpty()) {
            this.properties = Collections.emptyList();
        }
        else {
            this.properties = List.copyOf(properties);
        }
        this.hat = hat;
        this.texture = texture;
    }

    @NotNull
    public static NightProfileProperty property(@NotNull String name, @NotNull String value) {
        return new NightProfileProperty(name, value, null);
    }

    @NotNull
    public static NightProfileProperty property(@NotNull String name, @NotNull String value, @Nullable String signature) {
        return new NightProfileProperty(name, value, signature);
    }

    @Override
    @NotNull
    public <T> T adapt(@NotNull ObjectContentsAdapter<T> adapter) {
        return adapter.adaptContents(this);
    }

    @Nullable
    public String name() {
        return this.name;
    }

    @Nullable
    public UUID id() {
        return this.id;
    }

    @NotNull
    public List<NightProfileProperty> profileProperties() {
        return this.properties;
    }

    public boolean hat() {
        return this.hat;
    }

    @Nullable
    public NightKey texture() {
        return this.texture;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NightPlayerHeadObjectContents that)) return false;
        return hat == that.hat && Objects.equals(name, that.name) && Objects.equals(id, that.id) && Objects.equals(properties, that.properties) && Objects.equals(texture, that.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, properties, hat, texture);
    }

    @Override
    public String toString() {
        return "NightPlayerHeadObjectContents{" +
            "name='" + name + '\'' +
            ", id=" + id +
            ", properties=" + properties +
            ", hat=" + hat +
            ", texture=" + texture +
            '}';
    }

    public static final class NightProfileProperty {

        private final String name;
        private final String value;
        private final @Nullable String signature;

        NightProfileProperty(@NotNull String name, @NotNull String value, @Nullable String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        @NotNull
        public String name() {
            return this.name;
        }

        @NotNull
        public String value() {
            return this.value;
        }

        @Nullable
        public String signature() {
            return this.signature;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NightProfileProperty that)) return false;
            return Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(signature, that.signature);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value, signature);
        }

        @Override
        public String toString() {
            return "ProfilePropertyImpl{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
        }
    }
}
