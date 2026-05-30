package su.nightexpress.nightcore.bridge.text.event;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;

public interface WrappedPayload {

    record Text(@NonNull String value) implements WrappedPayload {
    }

    record Int(int integer) implements WrappedPayload {
    }

    record Custom(@NonNull NightKey key, @NonNull NightNbtHolder nbt) implements WrappedPayload {
    }

    record Dialog(@NonNull WrappedDialog dialog) implements WrappedPayload {
    }

    static WrappedPayload.@NonNull Text string(@NonNull String value) {
        return new Text(value);
    }

    static WrappedPayload.@NonNull Int integer(int integer) {
        return new Int(integer);
    }

    static WrappedPayload.@NonNull Dialog dialog(@NonNull WrappedDialog dialog) {
        return new Dialog(dialog);
    }

    // BinaryTagHolder nbt
    static WrappedPayload.@NonNull Custom custom(@NonNull NightKey key, @NonNull NightNbtHolder nbt) {
        return new Custom(key, nbt);
    }
}
