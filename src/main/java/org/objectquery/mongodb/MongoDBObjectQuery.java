package org.objectquery.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.cache.EntityCache;
import org.objectquery.BaseQuery;
import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericBaseQuery;
import org.objectquery.generic.ObjectQueryException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDBObjectQuery {

	public static MongoDBQueryBuilder mongoDBBuilder(BaseQuery<?> query) {
		if ((query instanceof GenericBaseQuery<?>))
			return new MongoDBQueryBuilder((GenericBaseQuery<?>) query);
		throw new ObjectQueryException("The Query is instance of unconvertable implementation ", null);
	}

	@SuppressWarnings("unchecked")
	public static <RET extends List<?>> RET execute(SelectQuery<?> query, Datastore engineSession) {
		DBCollection collection = getCollection(query, engineSession);
		DBCursor cursor = executeSelect(query, collection);
		Mapper mapper = ((DatastoreImpl) engineSession).getMapper();
		EntityCache cache = mapper.createEntityCache();
		List<Object> res = new ArrayList<Object>();
		for (DBObject dbObject : cursor) {
			res.add(mapper.fromDBObject(((GenericBaseQuery<?>) query).getTargetClass(), dbObject, cache));
		}
		return (RET) res;
	}

	private static DBCursor executeSelect(SelectQuery<?> query, DBCollection collection) {
		MongoDBQueryBuilder builder = mongoDBBuilder(query);
		DBCursor cursor = collection.find(builder.getQuery(), builder.getProjections());
		return cursor;
	}

	private static DBCollection getCollection(BaseQuery<?> query, Datastore engineSession) {
		return engineSession.getCollection(((GenericBaseQuery<?>) query).getTargetClass());
	}

	@SuppressWarnings("unchecked")
	public static <RET extends List<?>> RET execute(SelectQuery<?> query, DBCollection engineSession) {
		return (RET) executeSelect(query, engineSession).toArray();
	}

	public static int execute(DeleteQuery<?> dq, Datastore engineSession) {
		return execute(dq, getCollection(dq, engineSession));
	}

	public static int execute(DeleteQuery<?> dq, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(dq);
		return engineSession.remove(builder.getQuery()).getN();
	}

	public static boolean execute(InsertQuery<?> ip, Datastore engineSession) {
		return execute(ip, getCollection(ip, engineSession));
	}

	public static boolean execute(InsertQuery<?> ip, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(ip);
		return engineSession.insert(builder.getData()).getCachedLastError().ok();
	}

	public static int execute(UpdateQuery<?> query, Datastore engineSession) {
		return execute(query, getCollection(query, engineSession));
	}

	public static int execute(UpdateQuery<?> query, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(query);
		return engineSession.updateMulti(builder.getQuery(), builder.getData()).getN();

	}
}
