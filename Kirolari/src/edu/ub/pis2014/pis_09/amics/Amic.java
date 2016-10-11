package edu.ub.pis2014.pis_09.amics;

import java.io.Serializable;

public class Amic implements Serializable{
	private static final long serialVersionUID = -9155396635401670325L;

	private String id;
	private String alies;
	private String email;
	
	public Amic(String id,String alies,String email){
		this.alies = alies;
		this.email = email;
		this.id = id;
	}

	/**
	 * @return the alies
	 */
	public String getAlies() {
		return alies;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param alies the alies to set
	 */
	public void setAlies(String alies) {
		this.alies = alies;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
}
