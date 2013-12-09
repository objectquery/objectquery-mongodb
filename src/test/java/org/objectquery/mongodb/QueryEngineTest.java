package org.objectquery.mongodb;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.QueryEngine;

import com.mongodb.DBCollection;

public class QueryEngineTest {

	@Test
	public void testFactory() {
		QueryEngine<DBCollection> instance = QueryEngine.instance(DBCollection.class);
		Assert.assertTrue(instance instanceof MongoDBQueryEngine);
	}

	@Test
	public void testDefalutFactory() {
		QueryEngine<DBCollection> instance = QueryEngine.defaultInstance();
		Assert.assertTrue(instance instanceof MongoDBQueryEngine);
	}
}
