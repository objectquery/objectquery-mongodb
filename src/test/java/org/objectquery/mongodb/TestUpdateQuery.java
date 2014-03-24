package org.objectquery.mongodb;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericUpdateQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Other;
import org.objectquery.mongodb.domain.Person;

public class TestUpdateQuery {

	private Datastore collection;

	@Before
	public void beforeTest() {
		collection = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleUpdate() {
		Other home = new Other();
		home.setText("old-address");
//		collection.merge(home);

		UpdateQuery<Other> query = new GenericUpdateQuery<Other>(Other.class);
		query.set(query.target().getText(), "new-address");
		query.eq(query.target().getText(), "old-address");

//		int res = JPAObjectQuery.execute(query, collection);
//		Assert.assertEquals(1, res);
		Assert.fail();
	}

	@Test
	public void testSimpleUpdateGen() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.eq(query.target().getAddress(), "old-address");
//		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(query);
//		Assert.assertEquals("update org.objectquery.jpa.domain.Home set address = :address where address  =  :address1", q.getQuery());
		Assert.fail();
	}

	@Test(expected = ObjectQueryException.class)
	public void testSimpleNestedUpdate() {
		UpdateQuery<Person> query = new GenericUpdateQuery<Person>(Person.class);
		query.set(query.target().getHome().getAddress(), "new-address");
		query.eq(query.target().getHome().getAddress(), "old-address");
//		JPAObjectQuery.execute(query, collection);
		Assert.fail();
	}

	@Test(expected = ObjectQueryException.class)
	public void testSimpleNestedUpdateGen() {
		UpdateQuery<Person> query = new GenericUpdateQuery<Person>(Person.class);
		query.set(query.target().getHome().getAddress(), "new-address");
		query.eq(query.target().getHome().getAddress(), "old-address");

//		JPAObjectQuery.jpqlGenerator(query);
		Assert.fail();
	}

	@Test
	public void testMultipleNestedUpdate() {
		Other home = new Other();
		home.setText("2old-address");
//		collection.merge(home);

		UpdateQuery<Other> query = new GenericUpdateQuery<Other>(Other.class);
		query.set(query.target().getText(), "new-address");
		query.set(query.box(query.target().getPrice()), 1d);
		query.eq(query.target().getText(), "2old-address");
//		int res = JPAObjectQuery.execute(query, collection);
//		Assert.assertEquals(1, res);
		Assert.fail();
	}

	@Test
	public void testMultipleNestedUpdateGen() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.set(query.box(query.target().getPrice()), 1d);
		query.eq(query.target().getAddress(), "old-address");

//		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(query);
//		Assert.assertEquals("update org.objectquery.jpa.domain.Home set address = :address,price = :price where address  =  :address1", q.getQuery());
		Assert.fail();
	}

	@After
	public void afterTest() {
		collection = null;
	}

}
