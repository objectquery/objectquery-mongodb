package org.objectquery.mongodb;

import java.util.ArrayList;
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

	private DBObject buildConditionGroup(ConditionGroup group) {
		List<DBObject> list = new ArrayList<DBObject>();
		for (ConditionElement element : group.getConditions()) {
			if (element instanceof ConditionGroup) {
				list.add(buildConditionGroup((ConditionGroup) element));
			} else if (element instanceof ConditionItem) {
				ConditionItem item = (ConditionItem) element;
				list.add(getParent(item.getItem(), item));
			}
		}
		return new BasicDBObject(getGroupOperator(group.getType()), list);
	}

	private DBObject getParent(PathItem item, ConditionItem conditionItem) {
		BasicDBObject cur = null;
		while (item.getParent() != null) {
			if (cur == null) {
				if (ConditionType.EQUALS == conditionItem.getType())
					cur = new BasicDBObject(item.getName(), conditionItem.getValue());
				else
					cur = new BasicDBObject(item.getName(), new BasicDBObject(getOperator(conditionItem.getType()), conditionItem.getValue()));
			} else
				cur = new BasicDBObject(item.getName(), cur);
			item = item.getParent();
		}
		return cur;
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
			throw new ObjectQueryException("Operator " + type.name() + " not supported by mongodb");
		default:
			break;
		}
		// TODO Auto-generated method stub
		return null;
	}

	private String getGroupOperator(GroupType type) {
		switch (type) {
		case AND:
			return "$and";
		case OR:
			return "or";
		default:
			break;
		}
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
