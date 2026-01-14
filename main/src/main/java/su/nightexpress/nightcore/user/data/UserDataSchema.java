package su.nightexpress.nightcore.user.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.db.statement.template.UpdateStatement;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.user.UserTemplate;

import java.util.UUID;

public interface UserDataSchema<U extends UserTemplate> {

    @NotNull Table getUsersTable();

    @NotNull Column<UUID> getUserIdColumn();

    @NotNull Column<String> getUserNameColumn();

    @NotNull SelectStatement<U> getUserSelectStatement();

    @NotNull InsertStatement<U> getUserInsertStatement();

    @NotNull UpdateStatement<U> getUserUpdateStatement();

    @NotNull UpdateStatement<U> getUserTinyUpdateStatement();
}
