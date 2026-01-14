package su.nightexpress.nightcore.user.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.AbstractDatabaseManager;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.user.UserInfo;
import su.nightexpress.nightcore.user.UserTemplate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class DefaultUserDataAccessor<U extends UserTemplate> implements UserDataAccessor<U> {

    private final AbstractDatabaseManager<?> databaseManager;
    private final UserDataSchema<U>          dataSchema;

    private final RowMapper<UserInfo> profileMapper;

    public DefaultUserDataAccessor(@NotNull AbstractDatabaseManager<?> databaseManager, @NotNull UserDataSchema<U> dataSchema) {
        this.databaseManager = databaseManager;
        this.dataSchema = dataSchema;

        this.profileMapper = resultSet -> {
            String name = this.dataSchema.getUserNameColumn().read(resultSet).orElseThrow();
            UUID uuid = this.dataSchema.getUserIdColumn().read(resultSet).orElseThrow();

            return new UserInfo(uuid, name);
        };
    }

    @Override
    public void addSynchronization(@NotNull Consumer<U> consumer) {
        this.databaseManager.addTableSync(this.dataSchema.getUsersTable(), resultSet -> {
            try {
                U user = this.dataSchema.getUserSelectStatement().map(resultSet);
                consumer.accept(user);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    @NotNull
    public List<U> loadAll() {
        return this.databaseManager.selectAny(this.dataSchema.getUsersTable(), this.dataSchema.getUserSelectStatement());
    }

    @Override
    @NotNull
    public List<UserInfo> loadProfiles() {
        return this.databaseManager.selectAny(this.dataSchema.getUsersTable(), SelectStatement
            .builder(this.profileMapper)
            .column(this.dataSchema.getUserIdColumn(), this.dataSchema.getUserNameColumn())
            .build()
        );
    }

    @Override
    @NotNull
    public Optional<U> loadByName(@NotNull String name) {
        return this.databaseManager.selectFirst(this.dataSchema.getUsersTable(), this.dataSchema.getUserSelectStatement(), Wheres.where(this.dataSchema.getUserNameColumn(), Operator.EQUALS_IGNORE_CASE, o -> name));
    }

    @Override
    @NotNull
    public Optional<U> loadById(@NotNull UUID uuid) {
        return this.databaseManager.selectFirst(this.dataSchema.getUsersTable(), this.dataSchema.getUserSelectStatement(), Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, o -> uuid));
    }

    @Override
    public boolean isExists(@NotNull String name) {
        return this.databaseManager.contains(this.dataSchema.getUsersTable(), Wheres.where(this.dataSchema.getUserNameColumn(), Operator.EQUALS_IGNORE_CASE, o -> name));
    }

    @Override
    public boolean isExists(@NotNull UUID uuid) {
        return this.databaseManager.contains(this.dataSchema.getUsersTable(), Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, o -> uuid));
    }

    @Override
    public void update(@NotNull U user) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserUpdateStatement(), user, Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, UserTemplate::getId));
    }

    @Override
    public void update(@NotNull Collection<U> collection) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserUpdateStatement(), collection, Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, UserTemplate::getId));
    }

    @Override
    public void tinyUpdate(@NotNull U user) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserTinyUpdateStatement(), user, Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, UserTemplate::getId));
    }

    @Override
    public void tinyUpdate(@NotNull Collection<U> collection) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserTinyUpdateStatement(), collection, Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, UserTemplate::getId));
    }

    @Override
    public void insert(@NotNull U user) {
        this.databaseManager.insert(this.dataSchema.getUsersTable(), this.dataSchema.getUserInsertStatement(), user);
    }

    @Override
    public void insert(@NotNull Collection<U> collection) {
        this.databaseManager.insert(this.dataSchema.getUsersTable(), this.dataSchema.getUserInsertStatement(), collection);
    }

    @Override
    public void deleteByName(@NotNull String name) {
        this.databaseManager.delete(this.dataSchema.getUsersTable(), Wheres.where(this.dataSchema.getUserNameColumn(), Operator.EQUALS_IGNORE_CASE, o -> name));
    }

    @Override
    public void deleteById(@NotNull UUID uuid) {
        this.databaseManager.delete(this.dataSchema.getUsersTable(), Wheres.where(this.dataSchema.getUserIdColumn(), Operator.EQUALS, o -> uuid));
    }
}
