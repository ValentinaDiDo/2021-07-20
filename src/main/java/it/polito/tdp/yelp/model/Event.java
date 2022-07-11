package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event>{

	public enum EventType{
		INTERVISTA,
		PAUSA
	}
	private int giorno;
	private EventType type;
	private User intervistato;
	private Intervistatore intervistatore;
	public Event(int giorno, EventType type, User intervistato, Intervistatore intervistatore) {
		super();
		this.giorno = giorno;
		this.type = type;
		this.intervistato = intervistato;
		this.intervistatore = intervistatore;
	}
	public int getGiorno() {
		return giorno;
	}
	public EventType getType() {
		return type;
	}
	public User getIntervistato() {
		return intervistato;
	}
	public Intervistatore getIntervistatore() {
		return intervistatore;
	}
	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return (int)(this.giorno-o.giorno);
	}
	
	
}
