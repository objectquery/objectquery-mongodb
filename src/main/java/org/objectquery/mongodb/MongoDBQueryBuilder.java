package org.objectquery.mongodb;

import java.util.List;

import org.objectquery.generic.ConditionElement;
import org.objectquery.generic.ConditionGroup;
import org.objectquery.generic.ConditionItem;
import org.objectquery.generic.ConditionType;
import org.objectquery.generic.GenericBaseQuery;
import org.objectquery.generic.GenericInternalQueryBuilder;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.GroupType;
import org.objectquery.generic.Join;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.generic.PathItem;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoDBQueryBuilder {

	private DBObject query;

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
		if (!builder.getHavings().isEmpty())
			throw new ObjectQueryException("Mongodb Implementation doesn't support having operator");
		query = buildConditionGroup(builder);
	}

	private DBObject buildConditionGroup(ConditionGroup builder) {
		BasicDBObject object = new BasicDBObject();
		for (ConditionElement element : builder.getConditions()) {
			if (element instanceof ConditionGroup) {
				ConditionGroup group = (ConditionGroup) element;
				object.append(getGroupOperator(group.getType()), buildConditionGroup(group));
			} else if (element instanceof ConditionItem) {
				ConditionItem item = (ConditionItem) element;
				DBObject cur = getParent(object, item.getItem().getParent());
				if (ConditionType.EQUALS == item.getType()) {
					if (cur.put(item.getItem().getName(), item.getValue()) != null) {
						throw new ObjectQueryException("mutiple property in the same path are not supported by mongodb implementation");
					}
				} else {
					String operator = getOperator(item.getType());
					if (cur.put(item.getItem().getName(), new BasicDBObject(operator, item.getValue())) != null) {
						throw new ObjectQueryException("mutiple property in the same path are not supported by mongodb implementation");
					}

				}
				// object.append();
			}
		}

		return object;

	}

	private DBObject getParent(DBObject dbObject, PathItem item) {
		if (item.getParent() == null) {
			return dbObject;
		}
		DBObject cur;
		if (item.getParent().getParent() == null)
			cur = dbObject;
		else
			cur = getParent(dbObject, item.getParent());
		Object get = cur.get(item.getName());
		if (get != null) {
			if (!(get instanceof DBObject))
				throw new ObjectQueryException("mutiple property in the same path are not supported by mongodb implementation");
			return (DBObject) get;
		} else {
			DBObject newO = new BasicDBObject();
			cur.put(item.getName(), newO);
			return newO;
		}
	}

	private String getOperator(ConditionType type) {
		switch (type) {
		case GREATER:
			return "$gt";
		case GREATER_EQUALS:
			return "$gte";
		case LESS:
			return "$lt";
		case LESS_EQUALS:
			return "$lte";
		case IN:
			return "$in";
		case NOT_IN:
			return "$nin";
		case NOT_EQUALS:
			return "$ne";
			
		case BETWEEN:
		case LIKE:
		case LIKE_NOCASE:
		case NOT_CONTAINS:
		case CONTAINS:
		case NOT_LIKE_NOCASE:
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
		return null;
	}

	private String getGroupOperator(GroupType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public DBObject getQuery() {
		return query;
	}

	public DBObject getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
