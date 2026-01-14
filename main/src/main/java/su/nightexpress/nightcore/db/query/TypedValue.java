package su.nightexpress.nightcore.db.query;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Deprecated
public class TypedValue<T>  {

    private final String              sql;
    private final Function<T, String> extractor;

    public TypedValue(@NotNull String sql, @NotNull Function<T, String> extractor) {
        this.sql = sql;
        this.extractor = extractor;
    }

    @NotNull
    public String toSQL() {
        return this.sql;
    }

    @NotNull
    public String extract(T entity) {
        return this.extractor.apply(entity);
    }
}
