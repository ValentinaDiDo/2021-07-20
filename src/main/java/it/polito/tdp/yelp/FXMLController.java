/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.Simile;
import it.polito.tdp.yelp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	Graph<User, DefaultWeightedEdge> grafo;
	private boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer anno = this.cmbAnno.getValue();
    	String input = this.txtN.getText();
    	if(anno == null || input.equals("")) {
    		txtResult.setText("PER FAVORE COMPLETA TUTTI E DUE I CAMPI\n");
    	}else {
    		try {
    			int n = Integer.parseInt(input);
    			this.model.creaGrafo(n,anno);
    			
    			this.grafo = this.model.getGrafo();
    			this.grafoCreato = true;
    			
    			txtResult.setText("GRAFO CREATO");
    			txtResult.appendText("\nVERTICI: "+this.grafo.vertexSet().size());
    			txtResult.appendText("\nARCHI: "+ this.grafo.edgeSet().size());
    			
    			this.cmbUtente.getItems().clear();
    			List<User> user = new ArrayList<>(this.grafo.vertexSet());
    			Collections.sort(user);
    			this.cmbUtente.getItems().addAll(user);
    			
    		}catch(NumberFormatException e) {
    			e.printStackTrace();
    			txtResult.appendText("\nPER FAVORE INSERISCI SOLO NUMERI\n");
    		}
    	}
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	if(this.grafoCreato == false) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO");
    	}else {
    		User scelto = this.cmbUtente.getValue();
    		if(scelto == null) {
    			txtResult.appendText("\nSELEZIONA UN UTENTE");
    		}else {
    			
    			List<Simile> simili = this.model.getSimili(scelto);
    			txtResult.setText("UTENTI SIMILI A: "+scelto.toString());
    			for(Simile u : simili) {
    				txtResult.appendText("\n"+u.toString());
    			}
    		}
    	}
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	if(this.grafoCreato == false) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO");
    	}else {
    		String s1 = this.txtX1.getText();
    		String s2 = this.txtX2.getText();
    		
    		if(s1.equals("") || s2.equals("")) {
    			txtResult.appendText("\nPER FAVORE COMPLETA ENTRAMBI I CAMPI");
    		}else {
    			try {
    				int x1 = Integer.parseInt(s1);
    				int x2 = Integer.parseInt(s2);
    				
    				if(x2 > this.grafo.vertexSet().size()) {
    					txtResult.appendText("\nIL VALORE DI X1 DEVE ESSERE MINORE DEL NUMERO DI VERTICI");
    				}else if(x1 >= x2) {
    					txtResult.appendText("\nIL VALORE DI X2 DEVE ESSERE MINORE DI X1");
    				}else {
    					txtResult.setText("SIMULO");
    					
    					String stampe = this.model.simula(x1, x2);
    					txtResult.setText(stampe);
    				}
    					
    			}catch(NumberFormatException e) {
    				e.printStackTrace();
        			txtResult.appendText("\nPER FAVORE INSERISCI SOLO NUMERI\n");
    			}
    		}
    	}
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbAnno.getItems().clear();
    	for(int i=2005; i<=2013; i++) {
    		this.cmbAnno.getItems().add(i);
    	}
    }
}
