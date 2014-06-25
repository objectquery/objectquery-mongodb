package org.objectquery.mongodb;

import org.junit.Test;
import org.objectquery.BaseSelectQuery;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.util.JSON;

public class TestSubQuery {

	private static String getQueryString(SelectQuery<Person> query) {
		return JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(query).getQuery());
	}

	@Test(expected = ObjectQueryException.class)
	public void testSubquerySimple() {
		SelectQuery<Person> query = new GenericSelectQuery<Person, Object>(Person.class);

		BaseSelectQuery<Person> subQuery = query.subQuery(Person.class);
		subQuery.eq(subQuery.target().getName(), "test");
		query.eq(query.target().getDud(), subQuery);

		getQueryString(query);

	}

}
