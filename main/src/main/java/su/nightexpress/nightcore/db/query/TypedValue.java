package su.nightexpress.nightcore.db.query;

import org.jspecify.annotations.NonNull;

import java.util.function.Function;

@Deprecated
public class TypedValue<T> {

    private final String              sql;
    private final Function<T, String> extractor;

    public TypedValue(@NonNull String sql, @NonNull Function<T, String> extractor) {
        this.sql = sql;
        this.extractor = extractor;
    }

    @NonNull
    public String toSQL() {
        return this.sql;
    }

    @NonNull
    public String extract(T entity) {
        return this.extractor.apply(entity);
    }
}
