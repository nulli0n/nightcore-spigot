package su.nightexpress.nightcore.bridge.text.event;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class NightClickEvent {

    private final Action         action;
    private final WrappedPayload payload;

    public NightClickEvent(NightClickEvent.@NonNull Action action, @NonNull WrappedPayload payload) {
        this.action = action;
        this.payload = payload;
    }

    public NightClickEvent.@NonNull Action action() {
        return this.action;
    }

    @NonNull
    public WrappedPayload payload() {
        return this.payload;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        final NightClickEvent that = (NightClickEvent) other;
        return this.action == that.action && Objects.equals(this.payload, that.payload);
    }

    @Override
    public int hashCode() {
        int result = this.action.hashCode();
        result = (31 * result) + this.payload.hashCode();
        return result;
    }

    public enum Action {

        OPEN_URL("open_url"),
        OPEN_FILE("open_file"),
        RUN_COMMAND("run_command"),
        SUGGEST_COMMAND("suggest_command"),
        CHANGE_PAGE("change_page"),
        COPY_TO_CLIPBOARD("copy_to_clipboard"),
        SHOW_DIALOG("show_dialog"),
        CUSTOM("custom"),
        ;

        private final String name;

        Action(@NonNull String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
