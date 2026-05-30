package su.nightexpress.nightcore.db.sql.query;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.AbstractUser;
import su.nightexpress.nightcore.db.AbstractUserDataManager;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;
import su.nightexpress.nightcore.db.sql.query.impl.DeleteQuery;
import su.nightexpress.nightcore.db.sql.query.impl.InsertQuery;
import su.nightexpress.nightcore.db.sql.query.impl.UpdateQuery;

import java.util.UUID;

@Deprecated
public class UserQueries {

    @NonNull
    public static DeleteQuery<Long> deleteByLastOnline() {
        return new DeleteQuery<Long>().where(AbstractUserDataManager.COLUMN_USER_LAST_ONLINE, WhereOperator.SMALLER,
            String::valueOf);
    }

    @NonNull
    public static DeleteQuery<UUID> deleteByUUID() {
        return new DeleteQuery<UUID>().where(AbstractUserDataManager.COLUMN_USER_ID, WhereOperator.EQUAL,
            String::valueOf);
    }

    @NonNull
    public static <U extends AbstractUser> UpdateQuery<U> updateCommons() {
        return new UpdateQuery<U>()
            .setValue(AbstractUserDataManager.COLUMN_USER_NAME, AbstractUser::getName)
            .setValue(AbstractUserDataManager.COLUMN_USER_DATE_CREATED, user -> String.valueOf(user.getDateCreated()))
            .setValue(AbstractUserDataManager.COLUMN_USER_LAST_ONLINE, user -> String.valueOf(user.getLastOnline()))
            .where(AbstractUserDataManager.COLUMN_USER_ID, WhereOperator.EQUAL, user -> user.getId().toString());
    }

    @NonNull
    public static <U extends AbstractUser> InsertQuery<U> insert() {
        return new InsertQuery<U>()
            .setValue(AbstractUserDataManager.COLUMN_USER_ID, user -> user.getId().toString())
            .setValue(AbstractUserDataManager.COLUMN_USER_NAME, AbstractUser::getName)
            .setValue(AbstractUserDataManager.COLUMN_USER_DATE_CREATED, user -> String.valueOf(user.getDateCreated()))
            .setValue(AbstractUserDataManager.COLUMN_USER_LAST_ONLINE, user -> String.valueOf(user.getLastOnline()));
    }
}
