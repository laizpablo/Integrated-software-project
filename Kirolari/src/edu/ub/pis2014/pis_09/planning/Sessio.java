package edu.ub.pis2014.pis_09.planning;

/*
 * Classe del objecte Sessio
 */
public class Sessio {
	private int dia;
	private int setmana;
	private String descripcio;
	private String activitat;
	private boolean completada;
	private int duracio;
	
	
	/*
	 * Constructor de Sessio 
	 */
	public Sessio(int dia, int setmana, String descripcio, String activitat) {
		this.dia = dia;
		this.setmana = setmana;
		this.descripcio = descripcio;
		this.activitat = activitat;
		completada = false;
	}
	
	public Sessio(int dia, int setmana) {
		this.setmana = setmana;
		this.dia = dia;
		this.descripcio = null;
		this.activitat = null;
		this.duracio = 0;
		completada = false;
	}
	
	//Metodes Getters i Setters
	/*
	 * Funcio getDia
	 */
	public int getDia() {
		return dia;
	}
	
	/*
	 * Funcio SetDia
	 */
	public void setDia(int dia) {
		this.dia = dia;
	}
	
	/*
	 * Funcio isCompletada
	 */
	public boolean isCompletada() {
		return completada;
	}
	
	/*
	 * Funcio setCompletada
	 */
	public void setCompletada(boolean completada) {
		this.completada = completada;
	}

	/*
	 * Funcio getSetmana
	 */
	public int getSetmana() {
		return setmana;
	}
	
	/*
	 * Funcio setSetmana
	 */
	public void setSetmana(int setmana) {
		this.setmana = setmana;
	}
	
	/*
	 * Funcio getDescripcio
	 */
	public String getDescripcio() {
		return descripcio;
	}

	
	/*
	 * Funcio getActivitat
	 */
	public String getActivitat() {
		return activitat;
	}

	/*
	 * Funcio setActivitat
	 */
	public void setActivitat(String activitat) {
		this.activitat = activitat;
	}

	/*
	 * Funcio setDescripcio
	 */
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	/*
	 * Funcio toString
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Descripcio:\n\n"+ descripcio + "\n\nActivitat:\n\n" + activitat + ".";
	}
	
	/*
	 * Funcio toStringList
	 * @see java.lang.Object#toString()
	 */
	public String toStringList(){
		return "Dia " + dia + ", Setmana " + setmana;
	}

	public int getDuracio() {
		return duracio;
	}

	public void setDuracio(int duracio) {
		this.duracio = duracio;
	}

	
	
}