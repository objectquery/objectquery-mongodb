package org.objectquery.mongodb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.DeleteQuery;
import org.objectquery.generic.GenericeDeleteQuery;
import org.objectquery.mongodb.domain.Other;
import org.objectquery.mongodb.domain.Person;

public class TestDeleteQuery {

	private Datastore collection;

	@Before
	public void beforeTest() {
		collection = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleDelete() {
		Other ot = new Other();
		ot.setText("text");
		collection.save(ot);
		DeleteQuery<Other> dq = new GenericeDeleteQuery<Other>(Other.class);
		int deleted = MongoDBObjectQuery.execute(dq, collection);
		Assert.assertTrue(deleted != 0);

	}

	@Test
	public void testSimpleDeleteGen() {
		DeleteQuery<Person> dq = new GenericeDeleteQuery<Person>(Person.class);
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(dq);
		Assert.assertEquals("{ }", q.getQuery().toString());
	}

	@Test
	public void testDeleteCondition() {
		Person to_delete = new Person();
		to_delete.setName("to-delete");
		collection.save(to_delete);

		DeleteQuery<Person> dq = new GenericeDeleteQuery<Person>(Person.class);
		dq.eq(dq.target().getName(), "to-delete");
		int deleted = MongoDBObjectQuery.execute(dq, collection);
		Assert.assertTrue(deleted != 0);
	}

	@Test
	public void testDeleteConditionGen() {

		DeleteQuery<Person> dq = new GenericeDeleteQuery<Person>(Person.class);
		dq.eq(dq.target().getName(), "to-delete");
		MongoDBQueryBuilder q = MongoDBObjectQuery.mongoDBBuilder(dq);
		Assert.assertEquals("{ \"$and\" : [ { \"name\" : \"to-delete\"}]}", q.getQuery().toString());
	}

	@After
	public void afterTest() {
		collection = null;
	}

}
