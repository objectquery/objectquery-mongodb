package org.objectquery.mongodb.domain;

import org.mongodb.morphia.annotations.Id;


public class Other {

	@Id
	private String id;
	private String text;
	private double price;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
