package org.objectquery.mongodb.domain;

import org.mongodb.morphia.annotations.Id;

public class Dog {

	@Id
	private String id;
	private String name;
	// private Person owner;
	private Home home;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public Person getOwner() {
	// return owner;
	// }
	//
	// public void setOwner(Person owner) {
	// this.owner = owner;
	// }

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

}
