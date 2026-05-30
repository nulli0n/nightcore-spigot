package su.nightexpress.nightcore.user.data;

import org.jspecify.annotations.NonNull;
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

    public DefaultUserDataAccessor(@NonNull AbstractDatabaseManager<?> databaseManager,
                                   @NonNull UserDataSchema<U> dataSchema) {
        this.databaseManager = databaseManager;
        this.dataSchema = dataSchema;

        this.profileMapper = resultSet -> {
            String name = this.dataSchema.getUserNameColumn().read(resultSet).orElseThrow();
            UUID uuid = this.dataSchema.getUserIdColumn().read(resultSet).orElseThrow();

            return new UserInfo(uuid, name);
        };
    }

    @Override
    public void addSynchronization(@NonNull Consumer<U> consumer) {
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
    @NonNull
    public List<U> loadAll() {
        return this.databaseManager.selectAny(this.dataSchema.getUsersTable(), this.dataSchema
            .getUserSelectStatement());
    }

    @Override
    @NonNull
    public List<UserInfo> loadProfiles() {
        return this.databaseManager.selectAny(this.dataSchema.getUsersTable(), SelectStatement
            .builder(this.profileMapper)
            .column(this.dataSchema.getUserIdColumn(), this.dataSchema.getUserNameColumn())
            .build()
        );
    }

    @Override
    @NonNull
    public Optional<U> loadByName(@NonNull String name) {
        return this.databaseManager.selectFirst(this.dataSchema.getUsersTable(), this.dataSchema
            .getUserSelectStatement(),
            Wheres.where(this.dataSchema.getUserNameColumn(), Operator.EQUALS_IGNORE_CASE, o -> name));
    }

    @Override
    @NonNull
    public Optional<U> loadById(@NonNull UUID uuid) {
        return this.databaseManager.selectFirst(this.dataSchema.getUsersTable(), this.dataSchema
            .getUserSelectStatement(),
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), o -> uuid));
    }

    @Override
    public boolean isExists(@NonNull String name) {
        return this.databaseManager.contains(this.dataSchema.getUsersTable(),
            Wheres.where(this.dataSchema.getUserNameColumn(), Operator.EQUALS_IGNORE_CASE, o -> name));
    }

    @Override
    public boolean isExists(@NonNull UUID uuid) {
        return this.databaseManager.contains(this.dataSchema.getUsersTable(),
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), o -> uuid));
    }

    @Override
    public void update(@NonNull U user) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserUpdateStatement(), user,
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), UserTemplate::getId));
    }

    @Override
    public void update(@NonNull Collection<U> collection) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserUpdateStatement(),
            collection,
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), UserTemplate::getId));
    }

    @Override
    public void tinyUpdate(@NonNull U user) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserTinyUpdateStatement(), user,
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), UserTemplate::getId));
    }

    @Override
    public void tinyUpdate(@NonNull Collection<U> collection) {
        this.databaseManager.update(this.dataSchema.getUsersTable(), this.dataSchema.getUserTinyUpdateStatement(),
            collection,
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), UserTemplate::getId));
    }

    @Override
    public void insert(@NonNull U user) {
        this.databaseManager.insert(this.dataSchema.getUsersTable(), this.dataSchema.getUserInsertStatement(), user);
    }

    @Override
    public void insert(@NonNull Collection<U> collection) {
        this.databaseManager.insert(this.dataSchema.getUsersTable(), this.dataSchema.getUserInsertStatement(),
            collection);
    }

    @Override
    public void deleteByName(@NonNull String name) {
        this.databaseManager.delete(this.dataSchema.getUsersTable(),
            Wheres.where(this.dataSchema.getUserNameColumn(), Operator.EQUALS_IGNORE_CASE, o -> name));
    }

    @Override
    public void deleteById(@NonNull UUID uuid) {
        this.databaseManager.delete(this.dataSchema.getUsersTable(),
            Wheres.whereUUID(this.dataSchema.getUserIdColumn(), o -> uuid));
    }
}
