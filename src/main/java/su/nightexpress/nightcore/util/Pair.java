package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Pair<F, S> {

    private final F first;
    private final S second;

    public Pair(@NotNull F first, @NotNull S second) {
        this.first = first;
        this.second = second;
    }

    @NotNull
    public F getFirst() {
        return this.first;
    }

    @NotNull
    public S getSecond() {
        return this.second;
    }

    @NotNull
    public Pair<S, F> swap() {
        return of(this.second, this.first);
    }

    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?> other) {
            return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
        }
        return false;
    }

    @NotNull
    public static <F, S> Pair<F, S> of(@NotNull F first, @NotNull S second) {
        return new Pair<>(first, second);
    }
}
