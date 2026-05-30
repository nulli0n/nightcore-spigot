package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.util.Strings;

import java.util.function.UnaryOperator;

public record WrappedNumberRangeDialogInput(@NonNull String key,
                                            @NonNull String label,
                                            @NonNull String labelFormat,
                                            int width,
                                            float start, float end,
                                            @Nullable Float initial,
                                            @Nullable Float step) implements WrappedDialogInput {

    @Override
    @NonNull
    public <I> I adapt(@NonNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    @Override
    @NonNull
    public WrappedNumberRangeDialogInput replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedNumberRangeDialogInput(this.key, operator.apply(
            this.label), this.labelFormat, this.width, this.start, this.end, this.initial, this.step);
    }

    public static final class Builder {

        private final String key;
        private final String label;
        private final float  start;
        private final float  end;

        private int    width       = DialogDefaults.DEFAULT_NUMBER_INPUT_WIDTH;
        private String labelFormat = "options.generic_value";
        private Float  initial     = null;
        private Float  step        = null;

        public Builder(@NonNull String key, @NonNull String label, float start, float end) {
            this.key = Strings.filterForVariable(key);
            this.label = label;
            this.start = start;
            this.end = end;
        }

        @NonNull
        public Builder width(int width) {
            this.width = DialogDefaults.clampWidth(width);
            return this;
        }

        @NonNull
        public Builder labelFormat(@NonNull String labelFormat) {
            this.labelFormat = labelFormat;
            return this;
        }

        @NonNull
        public Builder initial(@Nullable Float initial) {
            if (initial != null) {
                initial = Math.clamp(initial, this.start, this.end);
            }

            this.initial = initial;
            return this;
        }

        @NonNull
        public Builder step(@Nullable Float step) {
            this.step = step == null ? null : Math.max(step, 0.01F);
            return this;
        }

        @NonNull
        public WrappedNumberRangeDialogInput build() {
            return new WrappedNumberRangeDialogInput(this.key, this.label, this.labelFormat, this.width, this.start, this.end, this.initial, this.step);
        }
    }
}
