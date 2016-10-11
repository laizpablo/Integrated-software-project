/**
 * 
 */
package edu.ub.pis2014.pis_09.registre;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.ub.pis2014.pis_09.amics.Amic;
import edu.ub.pis2014.pis_09.esdeveniments.Esdeveniment;


public class Usuari implements Serializable{
	private static final long serialVersionUID = 1L;

	//private enum Genere {home,dona};

	private String id;
	private String nom;
	private String cognoms;
	private String correu;
	private String contrasenya;
	private String alies;
	private Date dataNaixement;
	private double alcada;
	private double pes;
	private String sexe;
	private ArrayList<Amic> amics;
	private ArrayList<Esdeveniment> esdeveniments;

	private int idPlanning;

	public Usuari() {
		idPlanning = -1;
		amics = new ArrayList<Amic>();
		esdeveniments = new ArrayList<Esdeveniment>();
		id = null;
		
		
    }

	public String getNom() {
		
		return nom;
	}

	public void setNom(final String nom) {
		this.nom = nom;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.put("name", nom);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}

	public String getCognoms() {
		return cognoms;
	}

	public void setCognoms(final String cognoms) {
		this.cognoms = cognoms;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.put("surname", cognoms);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}

	public String getCorreu() {
		return correu;
	}

	public void setCorreu(final String correu) {
		this.correu = correu;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.setEmail(correu);
				      user.setUsername(correu);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(final String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public String getAlies() {
		return alies;
	}

	public void setAlies(final String alies) {
		this.alies = alies;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.put("alias", alies);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}

	public Date getDataNaixement() {
		return dataNaixement;
	}

	public void setDataNaixement(Date dataNaixement) {
		this.dataNaixement = dataNaixement;
	}

	public double getAlcada() {
		return alcada;
	}

	public void setAlcada(final double alcada) {
		this.alcada = alcada;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.put("height", alcada);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}

	public double getPes() {
		return pes;
	}

	public void setPes(final double pes) {
		this.pes = pes;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.put("weight", pes);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}
	
	public String getSexe() {
		return sexe;
	}
	
	public void setSexe(final String sexe) {
		this.sexe= sexe;
		if(id!=null){
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			 
			// Retrieve the object by id
			query.getInBackground(id, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
				      user.put("sex", sexe);
				      user.saveEventually();
				    }
					
				}
			});
		}
	}
		
	
	public ArrayList<Amic> getAmics() {
		return amics;
	}

	public void setAmics(ArrayList<Amic> amics) {
		this.amics = amics;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIdPlanning() {
		return idPlanning;
	}

	public void setIdPlanning(int idPlanning) {
		this.idPlanning = idPlanning;
	}
	
	/**
	 * @return the esdeveniments
	 */
	public ArrayList<Esdeveniment> getEsdeveniments() {
		return esdeveniments;
	}

	/**
	 * @param esdeveniments the esdeveniments to set
	 */
	public void setEsdeveniments(ArrayList<Esdeveniment> esdeveniments) {
		this.esdeveniments = esdeveniments;
	}

	public void afegeixAmic(Amic amic){
		amics.add(amic);
	}
	
	public void afegeixAmic(int index, Amic amic){
		amics.add(index,amic);
	}

	public void eliminaAmic(Amic amic) {
		amics.remove(amic);
		
	}
	
	public void afegeixEsdeveniment(Esdeveniment event){
		esdeveniments.add(event);
	}
	
	public void abandonaEsdeveniment(Esdeveniment e) {
		esdeveniments.remove(e);
		
	}

	public Amic getAmicAmbID(String idParticipant) {
		for(Amic amic:amics){
			if(idParticipant.equals(amic.getId()))
				return amic;
		}
		return null;
	}
	
	public int indexOfEsdeveniment(String idEsdeveniment) {
		for(int i=0;i<esdeveniments.size();i++){
			if(idEsdeveniment.equals(esdeveniments.get(i).getId()))
				return i;
		}
		return -1;
	}
	

}
