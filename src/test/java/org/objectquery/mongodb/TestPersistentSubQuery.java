package org.objectquery.mongodb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.generic.ProjectionType;
import org.objectquery.mongodb.domain.Dog;
import org.objectquery.mongodb.domain.Person;

public class TestPersistentSubQuery {

	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test
	public void testSubquerySimple() {
		SelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);

		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		subQuery.eq(subQuery.target().getName(), "tomdud");
		query.eq(query.target().getDud(), subQuery);

		// List<Person> res = JPAObjectQuery.buildQuery(query,
		// collection).getResultList();
		// Assert.assertEquals(1, res.size());
		// Assert.assertEquals(res.get(0).getName(), "tom");
		Assert.fail();
	}

	@Test
	public void testBackReferenceSubquery() {
		GenericSelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person target = query.target();
		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		subQuery.eq(subQuery.target().getDog().getName(), target.getDog().getName());
		subQuery.notEq(subQuery.target(), target);
		query.eq(query.target().getDud(), subQuery);

		// List<Person> res = JPAObjectQuery.buildQuery(query,
		// collection).getResultList();
		// Assert.assertEquals(1, res.size());
		// Assert.assertEquals(res.get(0).getName(), "tom");
		Assert.fail();
	}

	@Test
	public void testDoubleSubQuery() {

		GenericSelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person target = query.target();
		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		query.eq(target.getDud(), subQuery);
		subQuery.eq(subQuery.target().getDog().getName(), target.getDog().getName());
		SelectQuery<Dog> doubSubQuery = subQuery.subQuery(Dog.class);
		subQuery.eq(subQuery.target().getDog(), doubSubQuery);

		// doubSubQuery.notEq(doubSubQuery.target().getOwner(),
		// subQuery.target());
		// doubSubQuery.notEq(doubSubQuery.target().getOwner(),
		// query.target().getMom());

		// List<Person> res = JPAObjectQuery.buildQuery(query,
		// collection).getResultList();
		// Assert.assertEquals(1, res.size());
		// Assert.assertEquals(res.get(0).getName(), "tom");
		Assert.fail();

	}

	@Test
	public void testMultipleReferenceSubquery() {
		GenericSelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person target = query.target();
		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		subQuery.eq(subQuery.target().getName(), "tomdud");
		SelectQuery<Person> subQuery1 = query.subQuery(Person.class);
		subQuery1.eq(subQuery1.target().getName(), "tommum");
		query.eq(target.getDud(), subQuery);
		query.eq(target.getMom(), subQuery1);

		// List<Person> res = JPAObjectQuery.buildQuery(query,
		// collection).getResultList();
		// Assert.assertEquals(1, res.size());
		// Assert.assertEquals(res.get(0).getName(), "tom");
		Assert.fail();

	}

	@Test(expected = ObjectQueryException.class)
	public void testProjectionSubquery() {
		GenericSelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person target = query.target();
		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		// subQuery.eq(subQuery.target().getDog().getOwner(), target.getDud());
		query.prj(subQuery);

		// List<Person> res = JPAObjectQuery.buildQuery(query,
		// collection).getResultList();
		// Assert.assertEquals(3, res.size());
		// Assert.assertEquals(res.get(0), null);
		// Assert.assertEquals(res.get(1), null);
		// Assert.assertEquals(res.get(1), null);
		Assert.fail();
	}

	@Test(expected = ObjectQueryException.class)
	public void testOrderSubquery() {
		GenericSelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person target = query.target();
		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		// subQuery.eq(subQuery.target().getDog().getOwner(), target.getDud());
		query.order(subQuery);

		// JPAObjectQuery.buildQuery(query, collection).getResultList();

	}

	@Test(expected = ObjectQueryException.class)
	public void testHavingSubquery() {
		GenericSelectQuery<Person> query = new GenericSelectQuery<Person>(Person.class);
		Person target = query.target();
		SelectQuery<Person> subQuery = query.subQuery(Person.class);
		// subQuery.eq(subQuery.target().getDog().getOwner(), target.getDud());
		query.having(subQuery, ProjectionType.COUNT).eq(3D);

		// JPAObjectQuery.buildQuery(query, collection).getResultList();
	}

	@After
	public void afterTest() {
		datastore = null;
	}

}