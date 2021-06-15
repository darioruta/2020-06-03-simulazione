package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		m.creaGrafo(0.3);
		
		List<GiocatoreConPeso> res = m.getMigliori();
		GiocatoreConPeso best = res.get(0);
		
		System.out.println("TOP PLAYER:"+ m.migliore);
		System.out.println("Avversari Battuti\n");
		
		for (GiocatoreConPeso gg : res) {
			System.out.println(gg.getP1().getPlayerID()+" "+ gg.getP1().getName()+" "+ gg.getSommaMinuti());
		}
		
	}

}
