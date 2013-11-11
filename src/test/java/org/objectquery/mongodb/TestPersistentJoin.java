package org.objectquery.mongodb;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.JoinType;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.DBCollection;

public class TestPersistentJoin {

	private DBCollection collection;

	@Before
	public void beforeTest() {
		collection = PersistentTestHelper.getDb();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSimpleJoin() {
		SelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person joined = query.join(Person.class);
		query.eq(query.target().getMom(), joined);

//		List<Person> persons = JPAObjectQuery.buildQuery(query, collection).getResultList();
//		Assert.assertEquals(1, persons.size());
		Assert.fail();
	}

	@Test(expected=ObjectQueryException.class)
	@SuppressWarnings("unchecked")
	public void testTypedJoin() {
		SelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person joined = query.join(Person.class, JoinType.LEFT);
		query.eq(query.target().getMom(), joined);

//		List<Person> persons = JPAObjectQuery.buildQuery(query, collection).getResultList();
//		Assert.assertEquals(1, persons.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTypedPathJoin() {
		SelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person joined = query.join(query.target().getMom(), Person.class, JoinType.LEFT);
		query.eq(joined.getName(), "tommum");

//		List<Person> persons = JPAObjectQuery.buildQuery(query, collection).getResultList();
//		Assert.assertEquals(1, persons.size());
		Assert.fail();
	}

	@After
	public void afterTest() {
		collection = null;
	}

}
