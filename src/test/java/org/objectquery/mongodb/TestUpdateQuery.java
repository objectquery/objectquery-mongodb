package org.objectquery.mongodb;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.QueryCondition;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericUpdateQuery;
import org.objectquery.mongodb.domain.Document;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Other;
import org.objectquery.mongodb.domain.Person;

public class TestUpdateQuery {

	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleUpdate() {
		Other home = new Other();
		home.setText("old-address");
		datastore.save(home);

		UpdateQuery<Other> query = new GenericUpdateQuery<Other>(Other.class);
		query.set(query.target().getText(), "new-address");
		query.eq(query.target().getText(), "old-address");

		int res = MongoDBObjectQuery.execute(query, datastore);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testSimpleUpdateGen() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.eq(query.target().getAddress(), "old-address");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(query);
		Assert.assertEquals("{ \"$and\" : [ { \"address\" : \"old-address\"}]}", q.getQuery().toString());
		Assert.assertEquals("{ \"$set\" : { \"address\" : \"new-address\"}}", q.getData().toString());
	}

	@Test()
	public void testSimpleNestedUpdate() {
		UpdateQuery<Document> query = new GenericUpdateQuery<Document>(Document.class);
		query.set(query.target().getMetadata().getAuthor(), "the real Author");
		query.eq(query.target().getMetadata().getAuthor(), "the Author");
		int res = MongoDBObjectQuery.execute(query, datastore);
		Assert.assertEquals(1, res);
	}

	@Test()
	public void testSimpleNestedUpdateGen() {
		UpdateQuery<Person> query = new GenericUpdateQuery<Person>(Person.class);
		query.set(query.target().getHome().getAddress(), "new-address");
		query.eq(query.target().getHome().getAddress(), "old-address");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(query);
		Assert.assertEquals("{ \"$and\" : [ { \"home.address\" : \"old-address\"}]}", q.getQuery().toString());
		Assert.assertEquals("{ \"$set\" : { \"home\" : { \"address\" : \"new-address\"}}}", q.getData().toString());
	}

	@Test
	public void testMultirecodUpdateGen() {
		UpdateQuery<Document> query = new GenericUpdateQuery<Document>(Document.class);
		query.set(query.target().getTitle(), "a title");
		QueryCondition or = query.or();
		or.eq(query.target().getTitle(), "first update");
		or.eq(query.target().getTitle(), "second update");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(query);
		Assert.assertEquals("{ \"$and\" : [ { \"$or\" : [ { \"title\" : \"first update\"} , { \"title\" : \"second update\"}]}]}", q.getQuery().toString());
		Assert.assertEquals("{ \"$set\" : { \"title\" : \"a title\"}}", q.getData().toString());
	}

	@Test
	public void testMultirecodUpdate() {
		UpdateQuery<Document> query = new GenericUpdateQuery<Document>(Document.class);
		query.set(query.target().getTitle(), "a title");
		QueryCondition or = query.or();
		or.eq(query.target().getTitle(), "first update");
		or.eq(query.target().getTitle(), "second update");
		int res = MongoDBObjectQuery.execute(query, datastore);
		Assert.assertEquals(2, res);
	}

	@Test
	public void testMultipleUpdate() {
		Other home = new Other();
		home.setText("2old-address");
		datastore.save(home);

		UpdateQuery<Other> query = new GenericUpdateQuery<Other>(Other.class);
		query.set(query.target().getText(), "new-address");
		query.set(query.box(query.target().getPrice()), 1d);
		query.eq(query.target().getText(), "2old-address");
		int res = MongoDBObjectQuery.execute(query, datastore);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testMultipleUpdateGen() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.set(query.box(query.target().getPrice()), 1d);
		query.eq(query.target().getAddress(), "old-address");

		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(query);
		Assert.assertEquals("{ \"$and\" : [ { \"address\" : \"old-address\"}]}", q.getQuery().toString());
		Assert.assertEquals("{ \"$set\" : { \"address\" : \"new-address\" , \"price\" : 1.0}}", q.getData().toString());
	}

	@After
	public void afterTest() {
		datastore = null;
	}

}
