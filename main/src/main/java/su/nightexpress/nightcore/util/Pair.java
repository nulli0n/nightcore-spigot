package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

@Deprecated
public class Pair<F, S> {

    private final F first;
    private final S second;

    public Pair(@NonNull F first, @NonNull S second) {
        this.first = first;
        this.second = second;
    }

    @NonNull
    public F getFirst() {
        return this.first;
    }

    @NonNull
    public S getSecond() {
        return this.second;
    }

    @NonNull
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

    @NonNull
    public static <F, S> Pair<F, S> of(@NonNull F first, @NonNull S second) {
        return new Pair<>(first, second);
    }
}
