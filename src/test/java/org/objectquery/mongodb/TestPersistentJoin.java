package org.objectquery.mongodb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.mongodb.domain.Person;

public class TestPersistentJoin {

	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test(expected = ObjectQueryException.class)
	public void testSimpleJoin() {
		SelectQuery<Person> query = new GenericSelectQuery<Person,Object>(Person.class);
		Person joined = query.join(Person.class);
		query.eq(query.target().getMom(), joined);

		MongoDBObjectQuery.execute(query, datastore);
	}

	@After
	public void afterTest() {
		datastore = null;
	}

}
