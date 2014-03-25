package org.objectquery.mongodb;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.objectquery.mongodb.domain.Dog;
import org.objectquery.mongodb.domain.Home;
import org.objectquery.mongodb.domain.Home.HomeType;
import org.objectquery.mongodb.domain.Person;

import com.mongodb.MongoClient;

public class PersistentTestHelper {

	private static Datastore ds;

	private static void initData() {

		Home tomHome = new Home();
		tomHome.setAddress("homeless");
		tomHome.setType(HomeType.HOUSE);

		Person tom = new Person();
		tom.setName("tom");
		tom.setHome(tomHome);

		Home dudHome = new Home();
		dudHome.setAddress("moon");
		dudHome.setType(HomeType.HOUSE);
		ds.save(dudHome);
		Person tomDud = new Person();
		tomDud.setName("tomdud");
		tomDud.setHome(dudHome);
		ds.save(tomDud);
		Person tomMum = new Person();
		tomMum.setName("tommum");
		tomMum.setHome(dudHome);
		ds.save(tomMum);
		Home dogHome = new Home();
		dogHome.setAddress("royal palace");
		dogHome.setType(HomeType.KENNEL);
		dogHome.setPrice(1000000);
		dogHome.setWeight(30);
		ds.save(dogHome);
		Dog tomDog = new Dog();
		tomDog.setName("cerberus");
		// tomDog.setOwner(tom);
		tomDog.setHome(dogHome);
		ds.save(tomDog);

		tom.setDud(tomDud);
		tom.setMum(tomMum);
		tom.setDog(tomDog);
		tomDud.setDog(tomDog);

		ds.save(tom);

	}

	public static Datastore getDb() {
		if (ds == null) {
			try {

				final MongoClient client = new MongoClient("localhost");
				Morphia morphia = new Morphia();
				morphia.map(Person.class, Home.class, Dog.class);
				ds = morphia.createDatastore(client, "testDb");
				initData();
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						ds.getCollection(Person.class).drop();
						ds.getCollection(Home.class).drop();
						ds.getCollection(Dog.class).drop();
						client.close();
					}
				});
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}
		return ds;
	}

}
