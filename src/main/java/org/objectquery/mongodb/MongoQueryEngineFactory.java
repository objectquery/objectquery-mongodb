package org.objectquery.mongodb;

import org.mongodb.morphia.Datastore;
import org.objectquery.QueryEngine;
import org.objectquery.QueryEngineFactory;

import com.mongodb.DBCollection;

public class MongoQueryEngineFactory implements QueryEngineFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <S> QueryEngine<S> createQueryEngine(Class<S> targetSession) {
		if (DBCollection.class.equals(targetSession))
			return createDefaultQueryEngine();
		if (Datastore.class.equals(targetSession))
			return (QueryEngine<S>) new MorphiaQueryEngine();
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> QueryEngine<T> createDefaultQueryEngine() {
		return (QueryEngine<T>) new MongoDBQueryEngine();
	}

}
