package edu.ub.pis2014.pis_09.esdeveniments;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.ub.pis2014.pis_09.amics.Amic;

public class Esdeveniment implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String nom;
	private String descripcio;
	private String esport;
	private ArrayList<Amic> participants;
	private Date data;
	private String zona;
	
	public Esdeveniment(String id,String nom,String descripcio,String esport,String zona){
		this.id = id;
		this.nom = nom;
		this.descripcio = descripcio;
		this.esport = esport;
		this.zona = zona;
		participants = new ArrayList<Amic>();
	}
	
	public Esdeveniment() {
		participants = new ArrayList<Amic>();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @return the participants
	 */
	public ArrayList<Amic> getParticipants() {
		return participants;
	}
	/**
	 * @return the esport
	 */
	public String getEsport() {
		return esport;
	}
	/**
	 * @return the descripcio
	 */
	public String getDescripcio() {
		return descripcio;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String date = null;
		try{
			date = df.format(data);
		}catch (Exception e){
			date = "dd-MM-yyyy HH:mm";
		}
		
		return date;
	}
	/**
	 * @return the lloc
	 */
	public String getLloc() {
		return zona;
	}
	
	
	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(ArrayList<Amic> participants) {
		this.participants = participants;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @param esport the esport to set
	 */
	public void setEsport(String esport) {
		this.esport = esport;
	}
	/**
	 * @param descripcio the descripcio to set
	 */
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}
	/**
	 * @param lloc the lloc to set
	 */
	public void setLloc(String zona) {
		this.zona = zona;
	}
	
	public void afegeixParticipant(Amic participantNou){
		participants.add(participantNou);
	}
	
	public void afegeixParticipant(int index, Amic participantNou){
		participants.add(index,participantNou);
	}
	
	public void esborraParticipant(Amic participant){
		participants.remove(participant);
	}
	
	/**
	 * Busca un usuari a la llista de participants i retorna el seu index a la llista
	 * o -1 si no el troba
	 * @param objectId
	 * @return index en la llista de l'usuari o -1 si no est�
	 */
	public int indexParticipant(String objectId){
		for(int i=0;i<participants.size();i++){
			if(participants.get(i).getId().equals(objectId))
				return i;
		}
		return -1;
	}
}
