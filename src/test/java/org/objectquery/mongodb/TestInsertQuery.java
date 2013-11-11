package org.objectquery.mongodb;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.InsertQuery;
import org.objectquery.generic.GenericInsertQuery;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Other;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.DBCollection;

public class TestInsertQuery {

	private DBCollection collection;

	@Before
	public void beforeTest() {
		collection = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleInsert() {
		InsertQuery<Other> ip = new GenericInsertQuery<Other>(Other.class);
		ip.set(ip.target().getText(), "test");
//		OrientDBObjectQuery.execute(ip, collection);
		Assert.fail();
	}

	@Test
	public void testSimpleInsertGen() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getName(), "test");
//		OrientDBQueryGenerator q = OrientDBObjectQuery.orientdbGenerator(ip);
//		Assert.assertEquals("insert into Person (name)values(:name)", q.getQuery());
		Assert.fail();
	}

	@Test
	public void testMultipInsert() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
//		Home res = OrientDBObjectQuery.execute(ip, collection);
//		Assert.assertNotNull(res);
		Assert.fail();
	}

	@Test
	public void testMultipInsertGen() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
//		OrientDBQueryGenerator q = OrientDBObjectQuery.orientdbGenerator(ip);
//		Assert.assertEquals("insert into Home (price,weight)values(:price,:weight)", q.getQuery());
		Assert.fail();
	}

	@Test
	public void testDupicateFieldInsert() {
		InsertQuery<Other> ip = new GenericInsertQuery<Other>(Other.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.target().getText(), "aa");
//		Other res = OrientDBObjectQuery.execute(ip, collection);
//		Assert.assertNotNull(res);
		Assert.fail();
	}

	@Test
	public void testNestedInsert() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getDud().getName(), "test");
//		OrientDBQueryGenerator q = OrientDBObjectQuery.orientdbGenerator(ip);
//		Assert.assertEquals("insert into Person (dud.name)values(:dudname)", q.getQuery());
		Assert.fail();
	}
	@After
	public void afterTest() {
		collection = null;
	}

}
