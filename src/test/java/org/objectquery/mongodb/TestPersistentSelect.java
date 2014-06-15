package org.objectquery.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.generic.OrderType;
import org.objectquery.generic.ProjectionType;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.DBObject;

public class TestPersistentSelect {
	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleSelect() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");

		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);

		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getName(), "tom");
	}

	@Test
	public void testSimpleSelectWithutCond() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectPathValue() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDud().getHome(), target.getMom().getHome());
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getDud().getHome(), res.get(0).getMom().getHome());
	}

	@Test
	public void testSelectPathParam() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.eq(target.getHome().getAddress(), "homeless");
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getHome().getAddress(), "homeless");
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectCountThis() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.prj(target, ProjectionType.COUNT);
		MongoDBObjectQuery.execute(qp, datastore);
	}

	@Test
	public void testSelectPrjection() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.prj(target.getHome());
		qp.eq(target.getName(), "tom");
		List<DBObject> res = MongoDBObjectQuery.execute(qp, datastore.getCollection(Person.class));
		Assert.assertEquals(1, res.size());
		Assert.assertEquals("tom", res.get(0).get("name"));
		Assert.assertEquals("homeless", ((DBObject) res.get(0).get("home")).get("address"));
	}

	@Test
	public void testSelectOrder() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.order(target.getName());
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals("tom", res.get(0).getName());
		Assert.assertEquals("tomdud", res.get(1).getName());
		Assert.assertEquals("tommum", res.get(2).getName());
	}

	@Test
	public void testSelectOrderDesc() {
		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.order(target.getName(), OrderType.DESC);
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals("tommum", res.get(0).getName());
		Assert.assertEquals("tomdud", res.get(1).getName());
		Assert.assertEquals("tom", res.get(2).getName());
	}

	@Test
	public void testSelectSimpleConditions() {

		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.gt(target.getName(), "tom");
		qp.lt(target.getName(), "tom");
		qp.gtEq(target.getName(), "tom");
		qp.ltEq(target.getName(), "tom");
		qp.notEq(target.getName(), "tom");
		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(0, res.size());

	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectLikeConditions() {

		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.like(target.getName(), "tom");
		qp.notLike(target.getName(), "tom");
		qp.likeNc(target.getName(), "tom");
		qp.notLikeNc(target.getName(), "tom");
		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(0, res.size());

	}

	@Test
	public void testSelectINCondition() {

		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();

		List<String> pars = new ArrayList<String>();
		pars.add("tommy");
		qp.in(target.getName(), pars);
		qp.notIn(target.getName(), pars);

		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(0, res.size());
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectContainsCondition() {

		GenericSelectQuery<Person, Object> qp0 = new GenericSelectQuery<Person, Object>(Person.class);
		Person target0 = qp0.target();
		qp0.eq(target0.getName(), "tom");

		List<Person> res0 = MongoDBObjectQuery.execute(qp0, datastore);
		Assert.assertEquals(1, res0.size());
		Person p = res0.get(0);

		GenericSelectQuery<Person, Object> qp = new GenericSelectQuery<Person, Object>(Person.class);
		Person target = qp.target();
		qp.contains(target.getFriends(), p);
		qp.notContains(target.getFriends(), p);

		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(0, res.size());
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectFunctionGrouping() {

		SelectQuery<Home> qp = new GenericSelectQuery<Home, Object>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());

		MongoDBObjectQuery.execute(qp, datastore);
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectOrderGrouping() {

		SelectQuery<Home> qp = new GenericSelectQuery<Home, Object>(Home.class);
		Home target = qp.target();
		qp.order(qp.box(target.getPrice()), ProjectionType.MAX, OrderType.ASC);
		MongoDBObjectQuery.execute(qp, datastore);

	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectOrderGroupingPrj() {

		SelectQuery<Home> qp = new GenericSelectQuery<Home, Object>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(qp.box(target.getPrice()), ProjectionType.MAX, OrderType.DESC);

		MongoDBObjectQuery.execute(qp, datastore);
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectGroupHaving() {
		SelectQuery<Home> qp = new GenericSelectQuery<Home, Object>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(qp.box(target.getPrice()), ProjectionType.MAX, OrderType.DESC);
		qp.having(qp.box(target.getPrice()), ProjectionType.MAX).eq(1000000d);

		MongoDBObjectQuery.execute(qp, datastore);
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectBetweenCondition() {
		SelectQuery<Home> qp = new GenericSelectQuery<Home, Object>(Home.class);
		Home target = qp.target();
		qp.between(qp.box(target.getPrice()), 100000D, 2000000D);

		List<Home> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getPrice(), 1000000d, 0);
	}

	@After
	public void afterTest() {
		datastore = null;
	}
}
