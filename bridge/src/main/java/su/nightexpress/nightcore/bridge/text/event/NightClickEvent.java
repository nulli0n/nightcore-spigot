package su.nightexpress.nightcore.bridge.text.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NightClickEvent {

    private final Action         action;
    private final WrappedPayload payload;

    public NightClickEvent(@NotNull NightClickEvent.Action action, @NotNull WrappedPayload payload) {
        this.action = action;
        this.payload = payload;
    }

    @NotNull
    public NightClickEvent.Action action() {
        return this.action;
    }

    @NotNull
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

        Action(@NotNull String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
