package su.nightexpress.nightcore.database.sql.query;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLUtils;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UpdateQuery /*extends AbstractQuery*/ {

    private final List<UpdateEntity> entities;
    private final String             table;

    private String sql;

    public UpdateQuery(@NotNull String table/*@NotNull String table, @NotNull String sql*/) {
        //super(table, sql);
        this.table = SQLUtils.escape(table);
        this.entities = new ArrayList<>();
    }

    public UpdateQuery append(@NotNull UpdateEntity entity) {
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

    @NotNull
    public static UpdateQuery create(@NotNull String table, @NotNull List<SQLValue> values) {
        return create(table, values, Collections.emptyList());
    }

    @NotNull
    public static UpdateQuery create(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        return create(table, UpdateEntity.create(values, conditions));
    }

    @NotNull
    public static UpdateQuery create(@NotNull String table, @NotNull UpdateEntity entity) {
        return new UpdateQuery(table).append(entity);
    }

    @NotNull
    public static UpdateQuery create(@NotNull String table, @NotNull Collection<UpdateEntity> entities) {
        UpdateQuery query = new UpdateQuery(table);
        entities.forEach(query::append);
        return query;
    }



    public boolean isEmpty() {
        return this.sql == null || this.table.isBlank() || this.sql.isBlank() || this.entities.isEmpty();
    }

    @NotNull
    public String getSQL() {
        return sql;
    }

    @NotNull
    public List<UpdateEntity> getEntities() {
        return entities;
    }

    /*@NotNull
    public static UpdateQuery create(@NotNull String table, @NotNull List<SQLValue> values) {
        return create(table, values, Collections.emptyList());
    }

    @NotNull
    public static UpdateQuery create(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        List<String> whereColumns = new ArrayList<>();
        List<String> whereValues = new ArrayList<>();

        values.forEach(value -> {
            columnNames.add(value.getColumn().getNameEscaped() + " = ?");
            columnValues.add(value.getValue());
        });

        conditions.forEach(condition -> {
            whereColumns.add(condition.getColumn().getNameEscaped() + " " + condition.getType().getOperator() + " ?");
            whereValues.add(condition.getValue().getValue());
        });

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(SQLUtils.escape(table));
        builder.append(" SET ").append(String.join(",", columnNames));
        if (!whereColumns.isEmpty()) {
            builder.append(" WHERE ").append(String.join(" AND ", whereColumns));
        }

        String sql = builder.toString();

        return new UpdateQuery(sql, columnValues, whereValues);
    }

    @NotNull
    public List<String> getValues() {
        return values;
    }

    @NotNull
    public List<String> getWheres() {
        return wheres;
    }*/
}
