package org.objectquery.mongodb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.BaseSelectQuery;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.mongodb.domain.Person;

public class TestPersistentSubQuery {

	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test(expected = ObjectQueryException.class)
	public void testSubquerySimple() {
		SelectQuery<Person> query = new GenericSelectQuery<Person,Object>(Person.class);

		BaseSelectQuery<Person> subQuery = query.subQuery(Person.class);
		subQuery.eq(subQuery.target().getName(), "tomdud");
		query.eq(query.target().getDud(), subQuery);
		MongoDBObjectQuery.execute(query, datastore);
	}

	@After
	public void afterTest() {
		datastore = null;
	}

}