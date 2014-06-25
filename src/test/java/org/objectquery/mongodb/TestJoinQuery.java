package org.objectquery.mongodb;

import org.junit.Test;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.mongodb.domain.Person;

public class TestJoinQuery {

	@Test(expected = ObjectQueryException.class)
	public void testSimpleJoin() {
		SelectQuery<Person> query = new GenericSelectQuery<Person, Object>(Person.class);
		Person joined = query.join(Person.class);
		query.eq(query.target().getMom(), joined);

		MongoDBObjectQuery.mongoDBBuilder(query);
	}

}
