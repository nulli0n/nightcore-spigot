package su.nightexpress.nightcore.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoNamespace;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.query.IUpdateQuery;
import su.nightexpress.nightcore.database.sql.query.MongoUpdateQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public abstract class AbstractMongoDBDataHandler<P extends NightCorePlugin> extends AbstractDelegatedDataHandler<P> {
    protected final DatabaseConfig config;
    private MongoDatabase database;
    private MongoClient client;

    protected AbstractMongoDBDataHandler(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
    }

    @Override
    public void onLoad() {
        if (this.config != null) {
            if (this.config.getSaveInterval() > 0) {
                this.addTask(this.plugin.createAsyncTask(this::onSave).setSecondsInterval(this.config.getSaveInterval() * 60));
            }

            if (this.config.getSyncInterval() > 0) {
                this.addTask(this.plugin.createAsyncTask(this::onSynchronize).setSecondsInterval(this.config.getSyncInterval()));
                this.plugin.info("Enabled data synchronization with " + config.getSyncInterval() + " seconds interval.");
            }

            if (this.config.isPurgeEnabled() && this.config.getPurgePeriod() > 0) {
                this.onPurge();
            }

            ConnectionString connString = new ConnectionString(this.config.getMongoConnectionString());
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .retryWrites(true)
                    .codecRegistry(pojoCodecRegistry)
                    .build();
            client = MongoClients.create(settings);
            database = client.getDatabase(this.config.getMongoDatabaseName());
        } else {
            this.plugin.error("Data config is null!");
        }
    }

    @Override
    public void onShutdown() {
        this.onSave();
        client.close();
    }

    @Nullable
    public AbstractConnector getConnector() {
        return null;
    }

    @Nullable
    protected final Connection getConnection() {
        return null;
    }

    public void createTable(@NotNull String table, @NotNull List<SQLColumn> columns) {
        try {
            database.createCollection(table);
        } catch (MongoCommandException ignored) {
        } // When there are duplicate tables
    }

    public void renameTable(@NotNull String from, @NotNull String to) {
        database.getCollection(from).renameCollection(new MongoNamespace(database.getName(), to));
    }

    public void addColumn(@NotNull String table, @NotNull SQLValue... columns) {
        // We don't need this in MongoDB because it's schemaless
    }

    public void renameColumn(@NotNull String table, @NotNull SQLValue... columns) {
        // We don't need this in MongoDB because it's schemaless
    }

    public void dropColumn(@NotNull String table, @NotNull SQLColumn... columns) {
        // We don't need this in MongoDB because it's schemaless
    }

    public boolean hasColumn(@NotNull String table, @NotNull SQLColumn column) {
        // We don't need this in MongoDB because it's schemaless
        return true;
    }

    public void insert(@NotNull String table, @NotNull List<SQLValue> values) {
        Document document = new Document();
        for (SQLValue value : values) {
            document.append(value.getColumn().getName(), value.getConvertedValue());
        }
        database.getCollection(table).insertOne(document);
    }

    private Bson generateFilters(SQLCondition... conditions) {
        if (conditions.length == 0) return new BsonDocument();
        List<Bson> filters = new ArrayList<>();
        for (SQLCondition condition : conditions) {
            filters.add(switch (condition.getType()) {
                case EQUAL -> Filters.eq(condition.getValue().getColumn().getName(), condition.getValue().getConvertedValue());
                case NOT_EQUAL ->
                        Filters.ne(condition.getValue().getColumn().getName(), condition.getValue().getConvertedValue());
                case GREATER -> Filters.gt(condition.getValue().getColumn().getName(), condition.getValue().getConvertedValue());
                case SMALLER -> Filters.lt(condition.getValue().getColumn().getName(), condition.getValue().getConvertedValue());
            });
        }
        return Filters.and(filters);
    }

    public void update(@NotNull String table, @NotNull List<SQLValue> values, @NotNull SQLCondition... conditions) {
        List<Bson> updates = new ArrayList<>();
        for (SQLValue value : values) {
            updates.add(Updates.set(value.getColumn().getName(), value.getConvertedValue()));
        }
        database.getCollection(table).updateMany(generateFilters(conditions), Updates.combine(updates));
    }

    @Override
    public @NotNull IUpdateQuery updateQuery(@NotNull String collection, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        List<Bson> updates = new ArrayList<>();
        for (SQLValue value : values) {
            updates.add(Updates.set(value.getColumn().getName(), value.getConvertedValue()));
        }
        return new MongoUpdateQuery(collection, generateFilters(conditions.toArray(new SQLCondition[]{})), Updates.combine(updates));
    }

    @Override
    public void executeUpdate(@NotNull IUpdateQuery query) {
        var mongoQuery = (MongoUpdateQuery) query;
        database.getCollection(mongoQuery.getCollection()).updateMany(mongoQuery.getFilter(), mongoQuery.getUpdate());
    }

    @Override
    public void executeUpdate(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        update(table, values, conditions.toArray(new SQLCondition[0]));
    }

    @Override
    public void executeUpdates(@NotNull List<IUpdateQuery> queries) {
        var mappedMongoQueries = new HashMap<String, List<MongoUpdateQuery>>();
        for (var query : queries) {
            var mongoQuery = (MongoUpdateQuery) query;
            if (!mappedMongoQueries.containsKey(mongoQuery.getCollection())) {
                mappedMongoQueries.put(mongoQuery.getCollection(), new ArrayList<>());
            }
            mappedMongoQueries.get(mongoQuery.getCollection()).add(mongoQuery);
        }
        for (var entry : mappedMongoQueries.entrySet()) {
            var collection = entry.getKey();
            var mongoQueries = entry.getValue().stream().map(MongoUpdateQuery::toUpdateManyModel).toList();
            database.getCollection(collection).bulkWrite(mongoQueries);
        }
    }

    public void delete(@NotNull String table, @NotNull SQLCondition... conditions) {
        database.getCollection(table).deleteMany(generateFilters(conditions));
    }

    public boolean contains(@NotNull String table, @NotNull SQLCondition... conditions) {
        return database.getCollection(table).countDocuments(generateFilters(conditions)) > 0;
    }

    public boolean contains(@NotNull String table, @NotNull List<SQLColumn> columns, @NotNull SQLCondition... conditions) {
        return database.getCollection(table).countDocuments(generateFilters(conditions)) > 0;
    }

    @NotNull
    public <T> Optional<T> load(@NotNull String table, @NotNull Function<ResultSet, T> function,
                                @NotNull List<SQLColumn> columns,
                                @NotNull List<SQLCondition> conditions) {
        List<T> list = this.load(table, function, columns, conditions, 1);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @NotNull
    public <T> List<T> load(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction,
                            @NotNull List<SQLColumn> columns,
                            @NotNull List<SQLCondition> conditions,
                            int amount) {
        List<T> list = new ArrayList<>();
        FindIterable<BsonDocument> documents = database.getCollection(table).find(generateFilters(conditions.toArray(new SQLCondition[0])), BsonDocument.class);
        try {
            var resultSet = new MongoResultSet(documents.cursor());
            while (resultSet.next() && (amount < 0 || list.size() < amount)) {
                list.add(dataFunction.apply(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}