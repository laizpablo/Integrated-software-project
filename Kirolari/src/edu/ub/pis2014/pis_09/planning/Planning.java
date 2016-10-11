package edu.ub.pis2014.pis_09.planning;

import java.util.ArrayList;


public class Planning {
	private String nomPlanning;
	private int duracioSetmana;
	private int id;
	private String objectius;
	private ArrayList<Sessio> llistaSessions;


	public Planning() {
		id = -1;
	}
	
	public Planning(String nomPlanning, int duracioSetmana, String objectius, int id ) {
		this.setNomPlanning(nomPlanning);
		this.setDuracioSetmana(duracioSetmana);
		this.setObjectius(objectius);
		this.setId(id);
		llistaSessions = new ArrayList<Sessio>();

	}

	public int getDuracioSetmana() {
		return duracioSetmana;
	}

	public void setDuracioSetmana(int duracioSetmana) {
		this.duracioSetmana = duracioSetmana;
	}

	public String getNomPlanning() {
		return nomPlanning;
	}

	public void setNomPlanning(String nomPlanning) {
		this.nomPlanning = nomPlanning;
	}

	public String getObjectius() {
		return objectius;
	}

	public void setObjectius(String objectius) {
		this.objectius = objectius;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Sessio> getLlistaSessions() {
		return llistaSessions;
	}

	public void setLlistaSessions(ArrayList<Sessio> llistaSessions) {
		this.llistaSessions = llistaSessions;
	}

}
