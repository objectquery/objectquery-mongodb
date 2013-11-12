package org.objectquery.mongodb;

import java.util.List;

import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.QueryEngine;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;

import com.mongodb.DBCollection;

public class MongoDBQueryEngine extends QueryEngine<DBCollection> {

	@Override
	public <RET extends List<?>> RET execute(SelectQuery<?> query, DBCollection engineSession) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int execute(DeleteQuery<?> dq, DBCollection engineSession) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean execute(InsertQuery<?> ip, DBCollection engineSession) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int execute(UpdateQuery<?> query, DBCollection engineSession) {
		// TODO Auto-generated method stub
		return 0;
	}

}
