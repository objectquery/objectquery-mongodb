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

public class TestPersistentSelect {
	private Datastore datastore;

	@Before
	public void beforeTest() {
		datastore = PersistentTestHelper.getDb();
	}

	@Test
	public void testSimpleSelect() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");

		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);

		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getName(), "tom");
	}

	@Test
	public void testSimpleSelectWithutCond() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectPathValue() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDud().getHome(), target.getMom().getHome());
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getDud().getHome(), res.get(0).getMom().getHome());
	}

	@Test
	public void testSelectPathParam() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDud().getName(), "tomdud");
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getDud().getName(), "tomdud");
	}

	@Test
	public void testSelectCountThis() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target, ProjectionType.COUNT);
		List<Person> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(3L, res.get(0));
	}

	@Test
	public void testSelectPrjection() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.prj(target.getHome());
		qp.eq(target.getName(), "tom");
		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals("tom", res.get(0)[0]);
		Assert.assertEquals("homeless", ((Home) res.get(0)[1]).getAddress());
	}

	@Test
	public void testSelectOrder() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.order(target.getName());
		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals("tom", res.get(0));
		Assert.assertEquals("tomdud", res.get(1));
		Assert.assertEquals("tommum", res.get(2));
	}

	@Test
	public void testSelectOrderDesc() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.order(target.getName(), OrderType.DESC);
		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals("tommum", res.get(0));
		Assert.assertEquals("tomdud", res.get(1));
		Assert.assertEquals("tom", res.get(2));
	}

	@Test
	public void testSelectSimpleConditions() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
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

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
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

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
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

		GenericSelectQuery<Person> qp0 = new GenericSelectQuery<Person>(Person.class);
		Person target0 = qp0.target();
		qp0.eq(target0.getName(), "tom");

		List<Person> res0 = MongoDBObjectQuery.execute(qp0, datastore);
		Assert.assertEquals(1, res0.size());
		Person p = res0.get(0);

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.contains(target.getFriends(), p);
		qp.notContains(target.getFriends(), p);

		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(0, res.size());
	}

	@Test
	public void testSelectFunctionGrouping() {

		SelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());

		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(res.size(), 3);
		Assert.assertEquals(res.get(0)[1], 0d);
		Assert.assertEquals(res.get(1)[1], 0d);
		Assert.assertEquals(res.get(2)[1], 1000000d);
	}

	@Test
	public void testSelectOrderGrouping() {

		GenericSelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.order(qp.box(target.getPrice()), ProjectionType.MAX, OrderType.ASC);
		List<Home> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals(0d, res.get(0).getPrice(), 0);
		Assert.assertEquals(0d, res.get(1).getPrice(), 0);
		Assert.assertEquals(1000000d, res.get(2).getPrice(), 0);

	}

	@Test
	public void testSelectOrderGroupingPrj() {

		GenericSelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(qp.box(target.getPrice()), ProjectionType.MAX, OrderType.DESC);

		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals((Double) res.get(0)[1], 1000000d, 0);
		Assert.assertEquals((Double) res.get(1)[1], 0d, 0);
		Assert.assertEquals((Double) res.get(2)[1], 0d, 0);
	}

	@Test
	public void testSelectGroupHaving() {
		GenericSelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(qp.box(target.getPrice()), ProjectionType.MAX, OrderType.DESC);
		qp.having(qp.box(target.getPrice()), ProjectionType.MAX).eq(1000000d);

		List<Object[]> res = MongoDBObjectQuery.execute(qp, datastore);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals((Double) res.get(0)[1], 1000000d, 0);
	}

	@Test(expected = ObjectQueryException.class)
	public void testSelectBetweenCondition() {
		SelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
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
