package su.nightexpress.nightcore.bridge.dialog.wrap;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.response.DialogResponseHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogBase;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.WrappedDialogType;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;

public record WrappedDialog(@NotNull WrappedDialogBase base, @NotNull WrappedDialogType type, @NotNull Map<String, DialogResponseHandler> responseHandlers) {

    public static class Builder {

        private final Map<String, DialogResponseHandler> responseHandlers;

        private WrappedDialogBase base;
        private WrappedDialogType type;

        public Builder() {
            this.responseHandlers = new HashMap<>();
        }

        @NotNull
        public Builder base(@NotNull WrappedDialogBase base) {
            this.base = base;
            return this;
        }

        @NotNull
        public Builder type(@NotNull WrappedDialogType type) {
            this.type = type;
            return this;
        }

        @NotNull
        public Builder handleResponse(@NotNull String identifier, @NotNull DialogResponseHandler handler) {
            this.responseHandlers.put(LowerCase.INTERNAL.apply(identifier), handler);
            return this;
        }

        @NotNull
        public WrappedDialog build() {
            Preconditions.checkNotNull(this.base, "Dialog must have a base!");
            Preconditions.checkNotNull(this.type, "Dialog must have a type!");

            return new WrappedDialog(this.base, this.type, this.responseHandlers);
        }
    }
}
