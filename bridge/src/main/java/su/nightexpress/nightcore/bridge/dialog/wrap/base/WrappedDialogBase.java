package su.nightexpress.nightcore.bridge.dialog.wrap.base;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBaseAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.util.Lists;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

public record WrappedDialogBase(@NonNull String title,
                                @Nullable String externalTitle,
                                boolean canCloseWithEscape,
                                boolean pause,
                                @NonNull WrappedDialogAfterAction afterAction,
                                @NonNull List<WrappedDialogBody> body,
                                @NonNull List<WrappedDialogInput> inputs) {

    @NonNull
    public <B> B adapt(@NonNull DialogBaseAdapter<B> adapter) {
        return adapter.adaptBase(this);
    }

    @NonNull
    @Deprecated
    public WrappedDialogBase replace(@NonNull UnaryOperator<String> operator) {
        String title = operator.apply(this.title);
        String externalTitle = this.externalTitle == null ? null : operator.apply(this.externalTitle);
        List<WrappedDialogBody> bodyReplaced = this.body.stream().map(other -> other.replace(operator)).toList();
        List<WrappedDialogInput> inputsReplaced = this.inputs.stream().map(other -> other.replace(operator)).toList();

        return new WrappedDialogBase(title, externalTitle, this.canCloseWithEscape, this.pause, this.afterAction, bodyReplaced, inputsReplaced);
    }

    public static final class Builder {

        private final String title;

        private String                   externalTitle;
        private boolean                  canCloseWithEscape = true;
        private boolean                  pause              = false;
        private WrappedDialogAfterAction afterAction;
        private List<WrappedDialogBody>  body;
        private List<WrappedDialogInput> inputs;

        public Builder(@NonNull String title) {
            this.afterAction = WrappedDialogAfterAction.CLOSE;
            this.body = Collections.emptyList();
            this.inputs = Collections.emptyList();
            this.title = title;
        }

        @NonNull
        public Builder externalTitle(@Nullable String externalTitle) {
            this.externalTitle = externalTitle;
            return this;
        }

        @NonNull
        public Builder canCloseWithEscape(boolean canCloseWithEscape) {
            this.canCloseWithEscape = canCloseWithEscape;
            return this;
        }

        @NonNull
        public Builder pause(boolean pause) {
            this.pause = pause;
            return this;
        }

        @NonNull
        public Builder afterAction(@NonNull WrappedDialogAfterAction afterAction) {
            this.afterAction = afterAction;
            return this;
        }

        @NonNull
        public Builder body(@NonNull WrappedDialogBody... body) {
            return this.body(Lists.newList(body));
        }

        @NonNull
        public Builder body(@NonNull List<WrappedDialogBody> body) {
            this.body = List.copyOf(body);
            return this;
        }

        @NonNull
        public Builder inputs(@NonNull WrappedDialogInput... inputs) {
            return this.inputs(Lists.newList(inputs));
        }

        @NonNull
        public Builder inputs(@NonNull List<WrappedDialogInput> inputs) {
            this.inputs = List.copyOf(inputs);
            return this;
        }

        @NonNull
        public WrappedDialogBase build() {
            return new WrappedDialogBase(this.title, this.externalTitle, this.canCloseWithEscape, this.pause, this.afterAction, this.body, this.inputs);
        }
    }
}
