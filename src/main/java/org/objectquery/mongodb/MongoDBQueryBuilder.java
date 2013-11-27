package org.objectquery.mongodb;

import java.util.List;

import org.objectquery.generic.ConditionGroup;
import org.objectquery.generic.GenericBaseQuery;
import org.objectquery.generic.GenericInternalQueryBuilder;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.Join;
import org.objectquery.generic.ObjectQueryException;

import com.mongodb.DBObject;

public class MongoDBQueryBuilder {

	public MongoDBQueryBuilder(GenericBaseQuery<?> query) {
		GenericInternalQueryBuilder builder = (GenericInternalQueryBuilder) query.getBuilder();
		switch (builder.getQueryType()) {
		case SELECT:
			buildSelect(query.getTargetClass(), builder, ((GenericSelectQuery<?>) query).getJoins(), query.getRootPathItem().getName());
			break;
		case DELETE:
			buildDelete(query.getTargetClass(), builder);
			break;
		case INSERT:
			buildInsert(query.getTargetClass(), builder);
			break;
		case UPDATE:
			buildUpdate(query.getTargetClass(), builder);
			break;
		default:
			break;
		}
	}

	private void buildInsert(Class<?> targetClass, GenericInternalQueryBuilder builder) {
		// TODO Auto-generated method stub

	}

	private void buildUpdate(Class<?> targetClass, GenericInternalQueryBuilder builder) {
		// TODO Auto-generated method stub

	}

	private void buildDelete(Class<?> targetClass, GenericInternalQueryBuilder builder) {
		// TODO Auto-generated method stub

	}

	private void buildSelect(Class<?> targetClass, GenericInternalQueryBuilder builder, List<Join> joins, String name) {
		// TODO Auto-generated method stub
		if (joins != null && !joins.isEmpty())
			throw new ObjectQueryException("Mongodb Implementation doesn't support join");
		if (!builder.getProjections().isEmpty())
			throw new ObjectQueryException("Mongodb Implementation doesn't support projections");
		if (!builder.getHavings().isEmpty())
			throw new ObjectQueryException("Mongodb Implementation doesn't support having operator");
		buildConditionGroup(builder);
	}

	private void buildConditionGroup(ConditionGroup builder) {
		

	}

	public DBObject getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public DBObject getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
