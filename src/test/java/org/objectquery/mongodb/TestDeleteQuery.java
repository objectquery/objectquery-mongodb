package org.objectquery.mongodb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.DeleteQuery;
import org.objectquery.generic.GenericeDeleteQuery;
import org.objectquery.mongodb.domain.Other;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.DBCollection;

public class TestDeleteQuery {

	private DBCollection collection;

	@Before
	public void beforeTest() {
		collection = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleDelete() {
		Other ot = new Other();
		ot.setText("text");
		// collection.persist(ot);
		DeleteQuery<Other> dq = new GenericeDeleteQuery<Other>(Other.class);
		// int deleted = JPAObjectQuery.execute(dq, collection);
		// Assert.assertTrue(deleted != 0);
		Assert.fail();

	}

	@Test
	public void testSimpleDeleteGen() {
		DeleteQuery<Person> dq = new GenericeDeleteQuery<Person>(Person.class);
		// JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(dq);
		// Assert.assertEquals("delete org.objectquery.jpa.domain.Person ",
		// q.getQuery());
		Assert.fail();
	}

	@Test
	public void testDeleteCondition() {
		Person to_delete = new Person();
		to_delete.setName("to-delete");
		// collection.persist(to_delete);

		DeleteQuery<Person> dq = new GenericeDeleteQuery<Person>(Person.class);
		dq.eq(dq.target().getName(), "to-delete");
		// int deleted = JPAObjectQuery.execute(dq, collection);
		// Assert.assertTrue(deleted != 0);
		Assert.fail();
	}

	@Test
	public void testDeleteConditionGen() {

		DeleteQuery<Person> dq = new GenericeDeleteQuery<Person>(Person.class);
		dq.eq(dq.target().getName(), "to-delete");
		// JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(dq);
		// Assert.assertEquals("delete org.objectquery.jpa.domain.Person  where name  =  :name",
		// q.getQuery());
		Assert.fail();
	}

	@After
	public void afterTest() {
		collection = null;
	}

}
