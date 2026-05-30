package su.nightexpress.nightcore.user.data;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.db.statement.template.UpdateStatement;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.user.UserTemplate;

import java.util.UUID;

public interface UserDataSchema<U extends UserTemplate> {

    @NonNull
    Table getUsersTable();

    @NonNull
    Column<UUID> getUserIdColumn();

    @NonNull
    Column<String> getUserNameColumn();

    @NonNull
    SelectStatement<U> getUserSelectStatement();

    @NonNull
    InsertStatement<U> getUserInsertStatement();

    @NonNull
    UpdateStatement<U> getUserUpdateStatement();

    @NonNull
    UpdateStatement<U> getUserTinyUpdateStatement();
}
