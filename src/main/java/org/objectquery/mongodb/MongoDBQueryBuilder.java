package org.objectquery.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.objectquery.SelectQuery;
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
import org.objectquery.generic.Order;
import org.objectquery.generic.OrderType;
import org.objectquery.generic.PathItem;
import org.objectquery.generic.Projection;
import org.objectquery.generic.SetValue;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoDBQueryBuilder {

	private DBObject query;
	private DBObject projections;
	private DBObject data;

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
		if (builder.getSets().isEmpty())
			throw new ObjectQueryException("Set at least a value for the update");
		this.data = buildValues(builder.getSets());
	}

	private void buildUpdate(Class<?> targetClass, GenericInternalQueryBuilder builder) {
		if (!builder.getConditions().isEmpty())
			query = buildConditionGroup(builder);
		else
			query = new BasicDBObject();
		if (builder.getSets().isEmpty())
			throw new ObjectQueryException("Set at least a value for the update");

		this.data = buildValues(builder.getSets());

	}

	private DBObject buildValues(List<SetValue> values) {
		DBObject obj = new BasicDBObject();
		for (SetValue value : values) {
			if (value.getValue() instanceof PathItem)
				throw new ObjectQueryException("unsupported path in the value of an update");
			if (value.getValue() instanceof SelectQuery<?>)
				throw new ObjectQueryException("unsupported query in the value of an update");
			PathItem item = value.getTarget();
			getParent(item.getParent(), obj).put(item.getName(), value.getValue());

		}
		return obj;
	}

	private void buildDelete(Class<?> targetClass, GenericInternalQueryBuilder builder) {
		if (!builder.getConditions().isEmpty())
			query = buildConditionGroup(builder);
		else
			query = new BasicDBObject();
	}

	private void buildSelect(Class<?> targetClass, GenericInternalQueryBuilder builder, List<Join> joins, String name) {
		// TODO Auto-generated method stub
		if (joins != null && !joins.isEmpty())
			throw new ObjectQueryException("Mongodb Implementation doesn't support join");
		if (!builder.getHavings().isEmpty())
			throw new ObjectQueryException("Mongodb Implementation doesn't support having operator");

		query = new BasicDBObject();
		if (!builder.getProjections().isEmpty()) {
			projections = buildProjection(builder.getProjections());
		}

		if (!builder.getConditions().isEmpty())
			query.put("$query", buildConditionGroup(builder));
		else
			query.put("$query", new BasicDBObject());
		if (!builder.getOrders().isEmpty())
			query.put("$orderby", buildOrder(builder.getOrders()));
	}

	private DBObject buildProjection(List<Projection> projectionsList) {
		DBObject obj = new BasicDBObject();
		for (Projection projection : projectionsList) {
			if (projection.getItem() instanceof PathItem) {
				PathItem item = ((PathItem) projection.getItem());
				getParent(item.getParent(), obj).put(item.getName(), new Integer(1));
			} else
				throw new ObjectQueryException("mongodb query generator doesn't support subquery");
		}
		return obj;
	}

	private DBObject buildOrder(List<Order> orders) {
		DBObject obj = new BasicDBObject();
		for (Order order : orders) {
			PathItem item = ((PathItem) order.getItem());
			getParent(item.getParent(), obj).put(item.getName(), order.getType() != OrderType.DESC ? new Integer(1) : new Integer(-1));
		}
		return obj;
	}

	private DBObject getParent(PathItem item, DBObject obj) {
		if (item.getParent() != null) {
			DBObject parent = getParent(item.getParent(), obj);
			DBObject prop = (DBObject) parent.get(item.getName());
			if (prop == null) {
				prop = new BasicDBObject();
				parent.put(item.getName(), prop);
			}
			return prop;
		} else {
			return obj;
		}

	}

	private DBObject buildConditionGroup(ConditionGroup group) {
		List<DBObject> list = new ArrayList<DBObject>();
		for (ConditionElement element : group.getConditions()) {
			if (element instanceof ConditionGroup) {
				list.add(buildConditionGroup((ConditionGroup) element));
			} else if (element instanceof ConditionItem) {
				ConditionItem item = (ConditionItem) element;
				list.add(getConditionParent(item));
			}
		}
		return new BasicDBObject(getGroupOperator(group.getType()), list);
	}

	private DBObject getConditionParent(ConditionItem conditionItem) {
		BasicDBObject cur = null;
		PathItem item = conditionItem.getItem();
		while (item.getParent() != null) {
			if (cur == null) {
				if (conditionItem.getValue() instanceof PathItem)
					throw new ObjectQueryException("mongodb implemantation doesn't support fields as condition value");
				if (conditionItem.getValue() instanceof SelectQuery<?>)
					throw new ObjectQueryException("mongodb implemantation doesn't support subquery");
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
		return null;
	}

	private String getGroupOperator(GroupType type) {
		switch (type) {
		case AND:
			return "$and";
		case OR:
			return "$or";
		default:
			break;
		}
		return null;
	}

	public DBObject getQuery() {
		return query;
	}

	public DBObject getProjections() {
		return projections;
	}

	public DBObject getData() {
		return data;
	}
}
