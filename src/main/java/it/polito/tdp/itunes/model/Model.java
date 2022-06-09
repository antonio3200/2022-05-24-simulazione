package it.polito.tdp.itunes.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	ItunesDAO dao ;
	Map<Integer,Track> idMap;
	SimpleWeightedGraph<Track,DefaultWeightedEdge> grafo;

	public Model() {
		dao = new ItunesDAO();
		idMap= new HashMap<>();
		dao.getAllTracks(idMap);
	}
	
	public List<Genre> getGeneri(){
		return this.dao.getAllGenres();
	}
	
	public void creaGrafo(Genre genere) {
		List<Track> vertici=dao.getCanzoniGenere(genere, idMap);
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo vertici
		for(Track t : vertici) {
			grafo.addVertex(t);
		}
		List<Arco> archi=dao.getArchi(genere, idMap);
		for(Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo,a.getT1(),a.getT2(),a.getPeso());
		}
		System.out.format("Inseriti: %d vertici, %d archi\n", grafo.vertexSet().size(), grafo.edgeSet().size());
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Arco> getArcoDeltaMassimo(){
		List<Arco> result= new LinkedList<Arco>();
		int max=0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			int peso=(int)this.grafo.getEdgeWeight(e);
			if(peso>max) {
				result.clear();
				result.add(new Arco(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),peso));
				max=peso;
			}
			else if(peso==max) {
				result.add(new Arco(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),peso));
			}
		}
		return result;
	}
	
	public boolean grafoEsistente() {
		if(this.grafo==null) 
			return false;
		return true;
		}
	}
	
