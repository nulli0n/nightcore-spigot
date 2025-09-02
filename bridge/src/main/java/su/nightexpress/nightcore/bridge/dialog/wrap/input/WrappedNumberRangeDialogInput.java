package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.util.Strings;

import java.util.function.UnaryOperator;

public record WrappedNumberRangeDialogInput(@NotNull String key,
                                            @NotNull String label,
                                            @NotNull String labelFormat,
                                            int width,
                                            float start, float end,
                                            @Nullable Float initial,
                                            @Nullable Float step) implements WrappedDialogInput {

    @Override
    @NotNull
    public <I> I adapt(@NotNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    @Override
    @NotNull
    public WrappedNumberRangeDialogInput replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedNumberRangeDialogInput(this.key, operator.apply(this.label), this.labelFormat,  this.width, this.start, this.end, this.initial, this.step);
    }

    public static final class Builder {

        private final String    key;
        private final String label;
        private final float     start;
        private final float     end;

        private int    width       = 200; // TODO Config
        private String labelFormat = "options.generic_value";
        private Float  initial     = null;
        private Float  step        = null;

        public Builder(@NotNull String key, @NotNull String label, float start, float end) {
            this.key = Strings.filterForVariable(key);
            this.label = label;
            this.start = start;
            this.end = end;
        }

        @NotNull
        public Builder width(int width) {
            this.width = Math.clamp(width, 1, 1024); // TODO Config
            return this;
        }

        @NotNull
        public Builder labelFormat(@NotNull String labelFormat) {
            this.labelFormat = labelFormat;
            return this;
        }

        @NotNull
        public Builder initial(@Nullable Float initial) {
            if (initial != null) {
                initial = Math.clamp(initial, this.start, this.end);
            }

            this.initial = initial;
            return this;
        }

        @NotNull
        public Builder step(@Nullable Float step) {
            this.step = step == null ? null : Math.max(step, 0.01F);
            return this;
        }

        @NotNull
        public WrappedNumberRangeDialogInput build() {
            return new WrappedNumberRangeDialogInput(this.key, this.label, this.labelFormat, this.width, this.start, this.end, this.initial, this.step);
        }
    }
}
