package it.polito.tdp.yelp.model;

public class Simile {
	private User user;
	private int grado;
	public Simile(User user, int grado) {
		super();
		this.user = user;
		this.grado = grado;
	}
	public User getUser() {
		return user;
	}
	public int getGrado() {
		return grado;
	}
	@Override
	public String toString() {
		return this.user.toString()+" ("+this.grado+")";
	}
	
	
}
