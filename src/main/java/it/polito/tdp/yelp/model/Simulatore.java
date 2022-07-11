package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulatore {
	//DATI IN INGRESSO
	Graph<User, DefaultWeightedEdge> grafo;
	int X1;  //intervistatori
	int X2; //numero da intervistare
	
	//DATI IN USCITA
	int totGiorni;
	Map<Intervistatore, Integer> intervistati; //TIENE TRACCIA DEL NUM DI INTERVISTATI DA OGNI INTERVISTATORE
	
	//STATO DEL MONDO
	List<User> daIntervistare;
	List<Intervistatore>intervistatori;
	int contIntervistati;
	
	
	//CODA DEGLI EVENTI
	PriorityQueue<Event> queue;


	public Simulatore(Graph<User, DefaultWeightedEdge> grafo, int x1, int x2) {
		super();
		this.grafo = grafo;
		X1 = x1;
		X2 = x2;
	}
	
	public void init() {
		
		this.totGiorni = 0;
		this.daIntervistare = new ArrayList<>(this.grafo.vertexSet());
		this.intervistati = new TreeMap<>();
		this.intervistatori = new ArrayList<>();
		this.contIntervistati = 0;
		this.queue = new PriorityQueue<>();
		
		//CREO GLI INTERVISTATORI
		for(int i=1; i<= this.X1; i++) {
			Intervistatore in = new Intervistatore(i);
			this.intervistatori.add(in);
			this.intervistati.put(in, 0);
		}
		
		//POPOLO LA CODA DEGLI EVENTI - ASSEGNO PER IL PRIMO GIORNO UN UTENTE A CASO AD OGNI INTERVISTATORE
		for(Intervistatore i : intervistatori) {
			User intervistato = intervistatoRandom();
		//	this.intervistati.put(i, (this.intervistati.get(i)+1));  NON POSSO FARLO PERCHE' NON SO SE L'INTERVISTA ANDRA' A BUON FINE
			
			Event e = new Event(1, EventType.INTERVISTA, intervistato, i);
			this.queue.add(e);
		}
		this.totGiorni = 1;
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			if(this.contIntervistati == this.X2)
				break;
			else
				processEvent(e);
		}
	}
	
	
	public void processEvent(Event e) {
		switch(e.getType()) {
		
		
		case INTERVISTA:
			
			double prob = Math.random();
			
			if(prob < 0.6) {
				//INTERVISTA FINITA -> SCELGO TRA I PIU SIMILI DI QUELLO INTERVISTATO IL GIORNO PRIMA
				int giorno = e.getGiorno();
				User precedente = e.getIntervistato();
				
				//AGGIORNO STATISTICHE
				this.contIntervistati ++;
				this.totGiorni = giorno;
				this.intervistati.put(e.getIntervistatore(), this.intervistati.get(e.getIntervistatore())+1);
				
				//CONTROLLARE CHE SCELTO != NULL
				User prossimo = getSimile(precedente);
				if(prossimo != null) {
					//CREO NUOVO EVENTO
					Event nuovo = new Event(giorno+1, EventType.INTERVISTA, prossimo, e.getIntervistatore());
					this.queue.add(nuovo);
				}
			
			}else if(prob > 0.6 && prob < 0.8) {
				//CHIEDO GIORNO PAUSA
				int giorno = e.getGiorno();
				Event nuovo = new Event(giorno+1, EventType.PAUSA, null, e.getIntervistatore());
				this.queue.add(nuovo);
				
				this.totGiorni = giorno;
				//LA MAPPA CHE TIENE TRACCIA DEGLI INTERVISTATI NON DEVE ESSERE AGGIORNATA
			}else{
				//NON FINISCO L'INTERVISTA E FINISCO IL GIORNO DOPO 
				int giorno = e.getGiorno();
				
				Event nuovo = new Event(giorno+1,EventType.INTERVISTA, e.getIntervistato(), e.getIntervistatore());
				this.queue.add(nuovo);

				this.totGiorni = giorno;
				//ANCHE IN QUESTO CASO NON AGGIORNO LA MAPPA DEGLI INTERVISTATI PERCHE' L'UTENTE E' GIA' PRESENTE
			}
			break;
			
		case PAUSA:
			
			int giorno = e.getGiorno();
			//AGGIORNO STATISTICHE
			this.totGiorni = giorno;
			
			User prossimo = intervistatoRandom();
			
			if(prossimo != null) {
				Event nuovo = new Event(giorno+1, EventType.INTERVISTA, prossimo, e.getIntervistatore());
				this.queue.add(nuovo);
			}
			
			break;
		}
	}
	
	public User intervistatoRandom() {
		User scelto = null;
		
		if(this.daIntervistare.size() == 0)
			return null;
		
		int indice = (int) (Math.random()*this.daIntervistare.size());
		scelto = this.daIntervistare.get(indice);
		this.daIntervistare.remove(scelto);
		
		return scelto;
	}
	public User getSimile(User precedente){
		int similarita = 0;
		User scelto = null;
		List<User> simili = new ArrayList<>();
		
		if(Graphs.neighborListOf(this.grafo, precedente).size() == 0)
			return intervistatoRandom();
		
		for(User u : Graphs.neighborListOf(this.grafo, precedente )) {
			DefaultWeightedEdge e = this.grafo.getEdge(u, precedente);
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso>similarita)
				similarita = peso;
		}
		
		for(User u : Graphs.neighborListOf(this.grafo, precedente)) {
			DefaultWeightedEdge e = this.grafo.getEdge(u, precedente);
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso == similarita) {
				if(this.daIntervistare.contains(u))
					simili.add(u);
			}
		}
		//SE NON CI SONO SIMILI DISPONIBILI INTERVISTO UNO A CASO
		if(simili.size() == 0)
			return intervistatoRandom();
		int indice = (int) (Math.random()*simili.size());
		scelto = simili.get(indice);
		this.daIntervistare.remove(scelto);
		
		return scelto;
	}

	public int getTotGiorni() {
		return totGiorni;
	}

	public Map<Intervistatore, Integer> getIntervistati() {
		return intervistati;
	}
	
	
}
