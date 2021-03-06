package org.objectquery.mongodb.domain;

import java.util.List;

import org.mongodb.morphia.annotations.Id;

public class Person {
	@Id
	private String id;
	private String name;
	private String surname;
	private List<Person> friends;
	private Person mom;
	private Person dud;
	private Home home;
	private Dog dog;

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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Person> getFriends() {
		return friends;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}

	public Person getMom() {
		return mom;
	}

	public void setMum(Person mum) {
		this.mom = mum;
	}

	public Person getDud() {
		return dud;
	}

	public void setDud(Person dud) {
		this.dud = dud;
	}

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public Dog getDog() {
		return dog;
	}

	public void setDog(Dog dog) {
		this.dog = dog;
	}

}
