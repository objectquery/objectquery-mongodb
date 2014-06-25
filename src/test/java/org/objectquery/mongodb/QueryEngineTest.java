package org.objectquery.mongodb;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.objectquery.QueryEngine;

import com.mongodb.DBCollection;

public class QueryEngineTest {

	@Test
	public void testFactory() {
		QueryEngine<DBCollection> instance = QueryEngine.instance(DBCollection.class);
		assertTrue(instance instanceof MongoDBQueryEngine);
	}

	@Test
	public void testFactoryMorphia() {
		QueryEngine<Datastore> instance = QueryEngine.instance(Datastore.class);
		assertTrue(instance instanceof MorphiaQueryEngine);
	}

	@Test
	public void testDefaultFactory() {
		QueryEngine<DBCollection> instance = QueryEngine.defaultInstance();
		assertTrue(instance instanceof MongoDBQueryEngine);
	}
}
