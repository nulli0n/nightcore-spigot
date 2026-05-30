package su.nightexpress.nightcore.database.sql.query;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLUtils;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Deprecated
public class UpdateQuery /*extends AbstractQuery*/ {

    private final List<UpdateEntity> entities;
    private final String             table;

    private String sql;

    public UpdateQuery(@NonNull String table/*@NonNull String table, @NonNull String sql*/) {
        //super(table, sql);
        this.table = SQLUtils.escape(table);
        this.entities = new ArrayList<>();
    }

    public UpdateQuery append(@NonNull UpdateEntity entity) {
        String entitySQL = entity.createSQL(this.table);
        if (this.sql == null) {
            this.sql = entitySQL;
        }
        else if (!entitySQL.equalsIgnoreCase(this.sql)) {
            throw new IllegalStateException("Can not add SQLEntity with a different signature!");
        }

        this.entities.add(entity);
        return this;
    }

    @NonNull
    public static UpdateQuery create(@NonNull String table, @NonNull List<SQLValue> values) {
        return create(table, values, Collections.emptyList());
    }

    @NonNull
    public static UpdateQuery create(@NonNull String table, @NonNull List<SQLValue> values,
                                     @NonNull List<SQLCondition> conditions) {
        return create(table, UpdateEntity.create(values, conditions));
    }

    @NonNull
    public static UpdateQuery create(@NonNull String table, @NonNull UpdateEntity entity) {
        return new UpdateQuery(table).append(entity);
    }

    @NonNull
    public static UpdateQuery create(@NonNull String table, @NonNull Collection<UpdateEntity> entities) {
        UpdateQuery query = new UpdateQuery(table);
        entities.forEach(query::append);
        return query;
    }


    public boolean isEmpty() {
        return this.sql == null || this.table.isBlank() || this.sql.isBlank() || this.entities.isEmpty();
    }

    @NonNull
    public String getSQL() {
        return sql;
    }

    @NonNull
    public List<UpdateEntity> getEntities() {
        return entities;
    }
}
