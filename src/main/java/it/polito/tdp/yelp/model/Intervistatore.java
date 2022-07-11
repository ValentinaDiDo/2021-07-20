package it.polito.tdp.yelp.model;

import java.util.Objects;

public class Intervistatore implements Comparable<Intervistatore>{
	private int id;

	public Intervistatore(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intervistatore other = (Intervistatore) obj;
		return id == other.id;
	}

	@Override
	public int compareTo(Intervistatore o) {
		// TODO Auto-generated method stub
		return (int)(this.id-o.id);
	}

	@Override
	public String toString() {
		return id+"";
	}
	
	
}
