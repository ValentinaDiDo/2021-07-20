package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {

	YelpDao dao;
	List<User> user;
	Map<String, User> mUser;
	Graph<User, DefaultWeightedEdge> grafo;
	List<Adiacenza> adiacenze;
	Simulatore simulatore;
	
	
	public Model() {
		this.dao = new YelpDao();
	}
	public void creaGrafo(int n, Integer anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.mUser = new TreeMap<>();
		this.user = this.dao.getUsersReviews(n);
		for(User u : user) {
			mUser.put(u.getUserId(), u);
		}
		
		Graphs.addAllVertices(this.grafo, user);
		
		this.adiacenze = this.dao.getAdiacenze(anno, mUser);
		
		for(Adiacenza a : adiacenze) {
			Graphs.addEdge(this.grafo, a.getU1(), a.getU2(), a.getPeso());
		}
	}
	public Graph<User, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Simile> getSimili(User scelto){
		int similarita = 0;
		List<Simile> result = new ArrayList<>();
		
		for(User u : Graphs.neighborListOf(this.grafo, scelto)) {
			DefaultWeightedEdge e = this.grafo.getEdge(u, scelto);
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso>similarita)
				similarita = peso;
		}
		
		for(User u : Graphs.neighborListOf(this.grafo, scelto)) {
			DefaultWeightedEdge e = this.grafo.getEdge(u, scelto);
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso == similarita) {
				Simile s = new Simile(u, peso);
				result.add(s);
			}
		}
		
		return result;
	}
	
	public String simula(int x1, int x2) {
		this.simulatore = new Simulatore(this.grafo, x1, x2);
		this.simulatore.init();
		this.simulatore.run();
		
		Map<Intervistatore, Integer> interviste = this.simulatore.getIntervistati();
		int giorni = this.simulatore.getTotGiorni();
		
		String statistiche = "GIORNI PER EFFETTUARE LE INTERVISTE: "+giorni+"\n\nINTERVISTE:";
		for(Intervistatore i : interviste.keySet()) {
			statistiche+= "\nIntervistatore: "+i+" Numero intervistati: "+interviste.get(i);
		}
		return statistiche;
		
	}
}
