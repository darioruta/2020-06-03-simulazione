package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	Graph<Player, DefaultWeightedEdge> grafo;
	Map<Integer, Player> idMap;
	PremierLeagueDAO dao;
	public Player migliore;

	
	public Model() {
	
		this.idMap = new HashMap<>();
		this.dao = new PremierLeagueDAO();
		this.dao.listAllPlayers(idMap);
		migliore =null;
	}
	
	public String creaGrafo (double x) {
		this.grafo= new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> (DefaultWeightedEdge.class);
		
		List<Player> vertici = this.dao.getVertici(x, idMap);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		List<Adiacenza> archi = this.dao.getArchi(idMap);
		
		for (Adiacenza aa : archi ) {
			if (this.grafo.containsVertex(aa.getPlayer1()) && this.grafo.containsVertex(aa.getPlayer2())) {
				Graphs.addEdgeWithVertices(this.grafo, aa.getPlayer1(), aa.getPlayer2(), aa.getPeso());
			}
		}

		return String.format("Grafo creato con %d vertici e %d archi\n",
                this.grafo.vertexSet().size(),
                this.grafo.edgeSet().size()) ;
		
	}
	
	public class ComparatorePesoDecrescente implements Comparator<GiocatoreConPeso>{

		@Override
		public int compare(GiocatoreConPeso o1, GiocatoreConPeso o2) {
			// TODO Auto-generated method stub
			return -(o1.getSommaMinuti()-o2.getSommaMinuti());
		}
		
	}
	
	public List<GiocatoreConPeso> getMigliori(){
		
		List<GiocatoreConPeso> result = new ArrayList<GiocatoreConPeso>();
		int somma =-100000;
		for (Player p : this.grafo.vertexSet()) {
			if (this.grafo.outDegreeOf(p)> somma) {
				somma = this.grafo.outDegreeOf(p);
				migliore = p;
			}
			
		}
		
		for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(migliore)) {
			
			GiocatoreConPeso gTemp = new GiocatoreConPeso(this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e));
			result.add(gTemp);
			
			
		}
		
		
		Collections.sort(result, new ComparatorePesoDecrescente());
	
		
		return result;
		
	}
	
}
