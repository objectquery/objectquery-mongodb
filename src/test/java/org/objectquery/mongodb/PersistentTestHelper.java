package org.objectquery.mongodb;

import java.net.UnknownHostException;

import org.objectquery.mongodb.domain.Dog;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Home.HomeType;
import org.objectquery.mongodb.domain.Person;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class PersistentTestHelper {

	private static DB db;

	private static void initData() {
		DBCollection collection;
		collection = db.getCollection("test");
		Gson gson = new Gson();

		Home tomHome = new Home();
		tomHome.setAddress("homeless");
		tomHome.setType(HomeType.HOUSE);

		Person tom = new Person();
		tom.setName("tom");
		tom.setHome(tomHome);

		Home dudHome = new Home();
		dudHome.setAddress("moon");
		dudHome.setType(HomeType.HOUSE);

		Person tomDud = new Person();
		tomDud.setName("tomdud");
		tomDud.setHome(dudHome);

		Person tomMum = new Person();
		tomMum.setName("tommum");
		tomMum.setHome(dudHome);

		Home dogHome = new Home();
		dogHome.setAddress("royal palace");
		dogHome.setType(HomeType.KENNEL);
		dogHome.setPrice(1000000);
		dogHome.setWeight(30);

		Dog tomDog = new Dog();
		tomDog.setName("cerberus");
//		tomDog.setOwner(tom);
		tomDog.setHome(dogHome);

		tom.setDud(tomDud);
		tom.setMum(tomMum);
		tom.setDog(tomDog);
		tomDud.setDog(tomDog);

		System.out.println(gson.toJson(tom));
		DBObject tomObj = (DBObject) JSON.parse(gson.toJson(tom));
		collection.save(tomObj);

	}

	public static DBCollection getDb() {
		if (db == null) {
			try {
				final MongoClient client = new MongoClient("localhost");
				db = client.getDB("testDb");
				initData();
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						client.close();
					}
				});
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}
		return db.getCollection("test");
	}

}
