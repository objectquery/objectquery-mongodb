package org.objectquery.mongodb;

import java.util.List;

import org.objectquery.BaseQuery;
import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericBaseQuery;
import org.objectquery.generic.ObjectQueryException;

import com.mongodb.DBCollection;

public class MongoDBObjectQuery {

	public static MongoDBQueryBuilder mongoDBBuilder(BaseQuery<?> query) {
		if ((query instanceof GenericBaseQuery<?>))
			return new MongoDBQueryBuilder((GenericBaseQuery<?>) query);
		throw new ObjectQueryException("The Query is instance of unconvertable implementation ", null);
	}

	public static <RET extends List<?>> RET execute(SelectQuery<?> query, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(query);
		engineSession.find(builder.getQuery());
		return null;
	}

	public static int execute(DeleteQuery<?> dq, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(dq);
		return engineSession.remove(builder.getQuery()).getN();
	}

	public static boolean execute(InsertQuery<?> ip, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(ip);
		return engineSession.insert(builder.getQuery()).getN() > 0;
	}

	public static int execute(UpdateQuery<?> query, DBCollection engineSession) {
		MongoDBQueryBuilder builder = mongoDBBuilder(query);
		return engineSession.update(builder.getData(), builder.getQuery()).getN();
	}
}
