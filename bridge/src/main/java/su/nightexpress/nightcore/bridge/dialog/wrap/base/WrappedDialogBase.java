package su.nightexpress.nightcore.bridge.dialog.wrap.base;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBaseAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Collections;
import java.util.List;

public record WrappedDialogBase(@NotNull NightComponent title,
                                @Nullable NightComponent externalTitle,
                                boolean canCloseWithEscape,
                                boolean pause,
                                @NotNull WrappedDialogAfterAction afterAction,
                                @NotNull List<WrappedDialogBody> body,
                                @NotNull List<WrappedDialogInput> inputs) {

    @NotNull
    public <B> B adapt(@NotNull DialogBaseAdapter<B> adapter) {
        return adapter.adaptBase(this);
    }

    public static final class Builder {

        private final NightComponent title;

        private NightComponent externalTitle;
        private boolean canCloseWithEscape = true;
        private boolean                  pause = false;
        private WrappedDialogAfterAction afterAction;
        private List<WrappedDialogBody>  body;
        private List<WrappedDialogInput> inputs;

        public Builder(@NotNull NightComponent title) {
            this.afterAction = WrappedDialogAfterAction.CLOSE;
            this.body = Collections.emptyList();
            this.inputs = Collections.emptyList();
            this.title = title;
        }

        @NotNull
        public Builder externalTitle(@Nullable NightComponent externalTitle) {
            this.externalTitle = externalTitle;
            return this;
        }

        @NotNull
        public Builder canCloseWithEscape(boolean canCloseWithEscape) {
            this.canCloseWithEscape = canCloseWithEscape;
            return this;
        }

        @NotNull
        public Builder pause(boolean pause) {
            this.pause = pause;
            return this;
        }

        @NotNull
        public Builder afterAction(@NotNull WrappedDialogAfterAction afterAction) {
            this.afterAction = afterAction;
            return this;
        }

        @NotNull
        public Builder body(@NotNull WrappedDialogBody... body) {
            return this.body(Lists.newList(body));
        }

        @NotNull
        public Builder body(@NotNull List<WrappedDialogBody> body) {
            this.body = List.copyOf(body);
            return this;
        }

        @NotNull
        public Builder inputs(@NotNull WrappedDialogInput... inputs) {
            return this.inputs(Lists.newList(inputs));
        }

        @NotNull
        public Builder inputs(@NotNull List<WrappedDialogInput> inputs) {
            this.inputs = List.copyOf(inputs);
            return this;
        }

        @NotNull
        public WrappedDialogBase build() {
            return new WrappedDialogBase(this.title, this.externalTitle, this.canCloseWithEscape, this.pause, this.afterAction, this.body, this.inputs);
        }
    }
}
