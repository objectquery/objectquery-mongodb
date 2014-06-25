package org.objectquery.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.InsertQuery;
import org.objectquery.generic.GenericInsertQuery;
import org.objectquery.mongodb.domain.Document;
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
		assertTrue(MongoDBObjectQuery.execute(ip, datastore));
	}

	@Test
	public void testSimpleInsertGen() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getName(), "test");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(ip);
		assertEquals("{ \"name\" : \"test\"}", q.getData().toString());
	}

	@Test
	public void testMultipInsert() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
		assertTrue(MongoDBObjectQuery.execute(ip, datastore));
	}

	@Test
	public void testMultipInsertGen() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(ip);
		assertEquals("{ \"price\" : 4.0 , \"weight\" : 6}", q.getData().toString());

	}

	@Test
	public void testNestedInsertGen() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getDud().getName(), "test");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(ip);
		assertEquals("{ \"dud\" : { \"name\" : \"test\"}}", q.getData().toString());
	}

	@Test
	public void testNestedInsert() {
		InsertQuery<Document> ip = new GenericInsertQuery<Document>(Document.class);
		ip.set(ip.target().getMetadata().getAuthor(), "test");
		assertTrue(MongoDBObjectQuery.execute(ip, datastore));
	}

	@After
	public void afterTest() {
		datastore = null;
	}

}
