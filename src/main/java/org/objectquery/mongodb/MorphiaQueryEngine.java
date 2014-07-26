package org.objectquery.mongodb;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.QueryEngine;
import org.objectquery.SelectMapQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.ObjectQueryException;

public class MorphiaQueryEngine extends QueryEngine<Datastore> {

	@Override
	public List<?> execute(SelectQuery<?> query, Datastore engineSession) {
		return MongoDBObjectQuery.execute(query, engineSession);
	}

	@Override
	public int execute(DeleteQuery<?> dq, Datastore engineSession) {
		return MongoDBObjectQuery.execute(dq, engineSession);
	}

	@Override
	public boolean execute(InsertQuery<?> ip, Datastore engineSession) {
		return MongoDBObjectQuery.execute(ip, engineSession);
	}

	@Override
	public int execute(UpdateQuery<?> query, Datastore engineSession) {
		return MongoDBObjectQuery.execute(query, engineSession);
	}

	@Override
	public <M> List<M> execute(SelectMapQuery<?, M> query, Datastore engineSession) {
		throw new ObjectQueryException("the Mongdb implementation doesn't support mapped query");
	}

}
