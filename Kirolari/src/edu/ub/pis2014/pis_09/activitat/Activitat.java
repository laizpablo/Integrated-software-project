package edu.ub.pis2014.pis_09.activitat;


import java.util.Date;

public class Activitat {
   private String activitat;
   private double longitud_inici;
   private double latitud_inici;
   private Date hora_inici;
   private double longitud_fi;
   private double latitud_fi;
   private double distancia;
   private Date hora_fi;
   private int durada;
   private double calories;
   private Date dia;
   
    
   /**
    * Constructor sense par�metres.
    */
   public Activitat(){
	   activitat="";
   }
   

    
   public Activitat(String activitat, double distancia, int durada, double calories, Date dia) {
	super();
	this.activitat = activitat;
	this.distancia = distancia;
	this.durada = durada;
	this.calories = calories;
	this.dia = dia;
}



/**
    * Entra l'hora i el lloc on s'ha finalitzat l'activitat.
    * @param longitud_fi
    * @param latitud_fi
    * @param alcada_fi
    * @param hora_fi
    */

    
   //getters i setters
   public String getActivitat() {
       return activitat;
   }

   public void setActivitat(String activitat) {
       this.activitat = activitat;
   }

   public double getLongitud_inici() {
       return longitud_inici;
   }

   public void setLongitud_inici(double longitud_inici) {
       this.longitud_inici = longitud_inici;
   }

   public double getLatitud_inici() {
       return latitud_inici;
   }

   public void setLatitud_inici(double latitud_inici) {
       this.latitud_inici = latitud_inici;
   }



   public Date getHora_inici() {
       return hora_inici;
   }

   public void setHora_inici(Date hora_inici) {
       this.hora_inici = hora_inici;
   }

   public double getLongitud_fi() {
       return longitud_fi;
   }

   public void setLongitud_fi(double longitud_fi) {
       this.longitud_fi = longitud_fi;
   }


   public Date getHora_fi() {
       return hora_fi;
   }

   public void setHora_fi(Date hora_fi) {
       this.hora_fi = hora_fi;
   }

   public int getDurada() {
       return durada;
   }

   public void setDurada(int durada) {
       this.durada = durada;
   }

   public double getCalories() {
	return calories;
   }


   public void setCalories(double calories) {
	   this.calories = calories;
   }


   public double getLatitud_fi() {
       return latitud_fi;
   }

   public void setLatitud_fi(double latitud_fi) {
       this.latitud_fi = latitud_fi;
   }

   public double getDistancia() {
	   return distancia;
   }


   public void setDistancia(double distancia) {
	   this.distancia = distancia;
   }


   @Override
   public String toString() {
       return " Distancia: "+String.valueOf((double)Math.round(distancia*100)/100)+"\n Calories: "+String.valueOf((double)Math.round(calories*100)/100)+"\n Durada: "+durada;
   }

   public Date getDia() {
	   return dia;
   }

   public void setDia(Date dia) {
	   this.dia = dia;
   }

}