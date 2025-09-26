package su.nightexpress.nightcore.bridge.text.event;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;

public interface WrappedPayload {

    record Text(@NotNull String value) implements WrappedPayload {}

    record Int(int integer) implements WrappedPayload {}

    record Custom(@NotNull NightKey key, @NotNull NightNbtHolder nbt) implements WrappedPayload {}

    record Dialog(@NotNull WrappedDialog dialog) implements WrappedPayload {}

    @NotNull
    static WrappedPayload.Text string(@NotNull String value) {
        return new Text(value);
    }

    @NotNull
    static WrappedPayload.Int integer(int integer) {
        return new Int(integer);
    }

    @NotNull
    static WrappedPayload.Dialog dialog(@NotNull WrappedDialog dialog) {
        return new Dialog(dialog);
    }

    // BinaryTagHolder nbt
    @NotNull
    static WrappedPayload.Custom custom(@NotNull NightKey key, @NotNull NightNbtHolder nbt) {
        return new Custom(key, nbt);
    }
}
