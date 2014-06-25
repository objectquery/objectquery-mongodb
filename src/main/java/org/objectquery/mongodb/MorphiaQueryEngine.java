package org.objectquery.mongodb;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.QueryEngine;
import org.objectquery.SelectMapQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;

public class MorphiaQueryEngine extends QueryEngine<Datastore> {

	@Override
	public <RET extends List<?>> RET execute(SelectQuery<?> query, Datastore engineSession) {
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
	public <RET extends List<M>, M> RET execute(SelectMapQuery<?, M> query, Datastore engineSession) {
		// TODO Auto-generated method stub
		return null;
	}

}
