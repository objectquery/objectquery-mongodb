package org.objectquery.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.SelectQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.generic.OrderType;
import org.objectquery.generic.ProjectionType;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.util.JSON;

public class TestSimpleQuery {

	@Test
	public void testBaseCondition() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");

		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"name\" : \"tom\"}]}}", JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
	}

	@Test
	public void testDupliedPath() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.eq(target.getName(), "tom2");
		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"name\" : \"tom\"} , { \"name\" : \"tom2\"}]}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
	}

	@Test
	public void testDottedPath() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.eq(target.getDud().getName(), "tom3");
		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"dog.name\" : \"tom\"} , { \"dud.name\" : \"tom3\"}]}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));

	}

	@Test
	public void testProjection() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.eq(target.getDog().getName(), "tom");

		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"dog.name\" : \"tom\"}]}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
		Assert.assertEquals("{ \"name\" : 1}", JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getProjections()));

	}

	@Test(expected = ObjectQueryException.class)
	public void testProjectionCountThis() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target, ProjectionType.COUNT);
		qp.eq(target.getDog().getName(), "tom");

		MongoDBObjectQuery.mongoDBBuilder(qp);

	}

	@Test
	public void testSelectOrder() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName());

		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"dog.name\" : \"tom\"}]} , \"$orderby\" : { \"name\" : 1}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
	}

	@Test
	public void testOrderAsc() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName(), OrderType.ASC);

		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"dog.name\" : \"tom\"}]} , \"$orderby\" : { \"name\" : 1}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));

	}

	@Test
	public void testOrderDesc() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName(), OrderType.DESC);
		qp.order(target.getDog().getName(), OrderType.DESC);

		Assert.assertEquals(
				"{ \"$query\" : { \"$and\" : [ { \"dog.name\" : \"tom\"}]} , \"$orderby\" : { \"name\" : -1 , \"dog\" : { \"name\" : -1}}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
	}

	@Test(expected = ObjectQueryException.class)
	public void testOrderGrouping() {

		GenericSelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.eq(target.getAddress(), "homeless");
		qp.order(qp.box(target.getPrice()), ProjectionType.COUNT, OrderType.ASC);

		MongoDBObjectQuery.mongoDBBuilder(qp);
	}

	@Test(expected = ObjectQueryException.class)
	public void testOrderGroupingPrj() {

		GenericSelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.COUNT);
		qp.order(qp.box(target.getPrice()), ProjectionType.COUNT, OrderType.ASC);
		MongoDBObjectQuery.mongoDBBuilder(qp);
	}

	@Test
	public void testAllSimpleConditions() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.gt(target.getName(), "tom");
		qp.lt(target.getName(), "tom");
		qp.gtEq(target.getName(), "tom");
		qp.ltEq(target.getName(), "tom");
		qp.notEq(target.getName(), "tom");

		Assert.assertEquals(
				"{ \"$query\" : { \"$and\" : [ { \"name\" : \"tom\"} , { \"name\" : { \"$gt\" : \"tom\"}} , { \"name\" : { \"$lt\" : \"tom\"}} , { \"name\" : { \"$gte\" : \"tom\"}}"
						+ " , { \"name\" : { \"$lte\" : \"tom\"}} , { \"name\" : { \"$ne\" : \"tom\"}}]}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
	}

	@Test(expected = ObjectQueryException.class)
	public void testLikeConditions() {
		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.like(target.getName(), "tom");
		qp.notLike(target.getName(), "tom");
		qp.likeNc(target.getName(), "tom");
		qp.notLikeNc(target.getName(), "tom");
		MongoDBObjectQuery.mongoDBBuilder(qp).getQuery();
	}

	@Test
	public void testINCondition() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		List<String> pars = new ArrayList<String>();
		pars.add("tom");
		qp.in(target.getName(), pars);
		qp.notIn(target.getName(), pars);
		Assert.assertEquals("{ \"$query\" : { \"$and\" : [ { \"name\" : { \"$in\" : [ \"tom\"]}} , { \"name\" : { \"$nin\" : [ \"tom\"]}}]}}",
				JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery()));
	}

	@Test(expected = ObjectQueryException.class)
	public void testContainsCondition() {

		GenericSelectQuery<Person> qp = new GenericSelectQuery<Person>(Person.class);
		Person target = qp.target();
		Person p = new Person();
		qp.contains(target.getFriends(), p);
		qp.notContains(target.getFriends(), p);

		JSON.serialize(MongoDBObjectQuery.mongoDBBuilder(qp).getQuery());
	}

	@Test(expected = ObjectQueryException.class)
	public void testProjectionGroup() {

		SelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());

		MongoDBObjectQuery.mongoDBBuilder(qp);

	}

	@Test(expected = ObjectQueryException.class)
	public void testProjectionGroupHaving() {

		SelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());
		qp.having(qp.box(target.getPrice()), ProjectionType.MAX).eq(0D);

		MongoDBObjectQuery.mongoDBBuilder(qp);

	}

	@Test(expected = ObjectQueryException.class)
	public void testBetweenCondition() {
		SelectQuery<Home> qp = new GenericSelectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.between(qp.box(target.getPrice()), 20D, 30D);

		MongoDBObjectQuery.mongoDBBuilder(qp).getQuery();
	}
}
