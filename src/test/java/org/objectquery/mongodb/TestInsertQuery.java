package org.objectquery.mongodb;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.InsertQuery;
import org.objectquery.generic.GenericInsertQuery;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Other;
import org.objectquery.mongodb.domain.Person;

public class TestInsertQuery {

	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleInsert() {
		InsertQuery<Other> ip = new GenericInsertQuery<Other>(Other.class);
		ip.set(ip.target().getText(), "test");
		Assert.assertTrue(MongoDBObjectQuery.execute(ip, datastore));
	}

	@Test
	public void testSimpleInsertGen() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getName(), "test");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(ip);
		Assert.assertEquals("{ \"name\" : \"test\"}", q.getData().toString());
	}

	@Test
	public void testMultipInsert() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
		Assert.assertTrue(MongoDBObjectQuery.execute(ip, datastore));
	}

	@Test
	public void testMultipInsertGen() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(ip);
		Assert.assertEquals("{ \"price\" : 4.0 , \"weight\" : 6}", q.getData().toString());

	}

	@Test
	public void testNestedInsertGen() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getDud().getName(), "test");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(ip);
		Assert.assertEquals("{ \"dud\" : { \"name\" : \"test\"}}", q.getData().toString());
	}

	@Test
	public void testNestedInsert() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getDud().getName(), "test");
		Assert.assertTrue(MongoDBObjectQuery.execute(ip, datastore));
	}

	@After
	public void afterTest() {
		datastore = null;
	}

}
