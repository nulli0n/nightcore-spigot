package su.nightexpress.nightcore.database.sql.query;

import com.mongodb.client.model.UpdateManyModel;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoUpdateQuery implements IUpdateQuery {
    private final String collection;
    private final Bson filter;
    private final Bson update;

    public String getCollection() {
        return collection;
    }

    public Bson getFilter() {
        return filter;
    }

    public Bson getUpdate() {
        return update;
    }

    public UpdateManyModel<Document> toUpdateManyModel() {
        return new UpdateManyModel<>(filter, update);
    }

    public MongoUpdateQuery(String collection, Bson filter, Bson update) {
        this.collection = collection;
        this.filter = filter;
        this.update = update;
    }
}
