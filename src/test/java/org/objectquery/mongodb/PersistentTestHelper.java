package org.objectquery.mongodb;

import java.net.UnknownHostException;

import org.objectquery.mongodb.domain.Dog;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Home.HomeType;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class PersistentTestHelper {

	private static DB db;

	private static void initData() {
		DBCollection entityManager;
		entityManager = db.getCollection("test");

		Home tomHome = new Home();
		tomHome.setAddress("homeless");
		tomHome.setType(HomeType.HOUSE);
		
//		/tomHome = entityManager.merge(tomHome);

		Person tom = new Person();
		tom.setName("tom");
		tom.setHome(tomHome);
		//tom = entityManager.merge(tom);

		Home dudHome = new Home();
		dudHome.setAddress("moon");
		dudHome.setType(HomeType.HOUSE);
		//dudHome = entityManager.merge(dudHome);

		Person tomDud = new Person();
		tomDud.setName("tomdud");
		tomDud.setHome(dudHome);
		//tomDud = entityManager.merge(tomDud);

		Person tomMum = new Person();
		tomMum.setName("tommum");
		tomMum.setHome(dudHome);

		//tomMum = entityManager.merge(tomMum);

		Home dogHome = new Home();
		dogHome.setAddress("royal palace");
		dogHome.setType(HomeType.KENNEL);
		dogHome.setPrice(1000000);
		dogHome.setWeight(30);

		//dogHome = entityManager.merge(dogHome);

		Dog tomDog = new Dog();
		tomDog.setName("cerberus");
		tomDog.setOwner(tom);
		tomDog.setHome(dogHome);
		//tomDog = entityManager.merge(tomDog);

		tom.setDud(tomDud);
		tom.setMum(tomMum);
		tom.setDog(tomDog);
		tomDud.setDog(tomDog);
		//entityManager.persist(tomDud);
		//entityManager.persist(tom);
		//entityManager.getTransaction().commit();
		//entityManager.close();

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
