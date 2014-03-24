package org.objectquery.mongodb.domain;

import org.mongodb.morphia.annotations.Id;


public class Home {
	public enum HomeType {
		KENNEL, HOUSE
	};

	@Id
	private String id;
	private String address;
	private HomeType type;
	private int weight;
	private double price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public HomeType getType() {
		return type;
	}

	public void setType(HomeType type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
