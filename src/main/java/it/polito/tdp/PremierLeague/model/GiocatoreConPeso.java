package it.polito.tdp.PremierLeague.model;

public class GiocatoreConPeso {
	
	private Player p1;
	private int sommaMinuti;
	
	
	public GiocatoreConPeso(Player p1, int sommaMinuti) {
		super();
		this.p1 = p1;
		this.sommaMinuti = sommaMinuti;
	}


	public Player getP1() {
		return p1;
	}


	public void setP1(Player p1) {
		this.p1 = p1;
	}


	public int getSommaMinuti() {
		return sommaMinuti;
	}


	public void setSommaMinuti(int sommaMinuti) {
		this.sommaMinuti = sommaMinuti;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sommaMinuti;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GiocatoreConPeso other = (GiocatoreConPeso) obj;
		if (sommaMinuti != other.sommaMinuti)
			return false;
		return true;
	}
	
	
	

}
