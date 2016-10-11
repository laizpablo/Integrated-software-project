package edu.ub.pis2014.pis_09.kirolari;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import edu.ub.pis2014.pis_09.planning.Planning;
import edu.ub.pis2014.pis_09.planning.Sessio;
import edu.ub.pis2014.pis_09.registre.Usuari;


/*
 * Classe Kirolari que extend d'Application
 * Es el controlador de l'aplicacio
 */
public class Kirolari extends Application{
	//AQUI ES DEFINEIXEN LES VARIABLES QUE VOLEM GUARDAR 

	private Planning planningUsuari;
	private Usuari usuari;
	private int diaBasePlanning = -1;
	private int setmanaBasePlanning = -1;
	private ArrayList<Long> idEvents; 
	private Context context;
	private boolean sessioIniciada;
	private boolean notificacions;
	private static PendingIntent pendingIntent;
	private static AlarmManager alarmManager;
	private boolean aniversari;
	private int dayUser;
	private int monthUser;
	
	/**
	 * Funcio onCreate
	 * @see android.app.Application#onCreate()
	 */
	public void onCreate(){
		
		//Inicialitzem parse
		Parse.initialize(this, "nAxbK5l0Lu721IsQU9bZF2cblBvrZ0egJaGH2EGO", "uWp0oFjI4UvziJnWaPqz0lQd4YEer6pg1l7fJFeI");
		PushService.setDefaultPushCallback(this, kirolariSignIn.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
				
		//ARA HE FET UN NOU, PERO AQUI S'HAURIA DE CARREGAR L'USUARI QUE HA INICIAT SESSIO!
		usuari = new Usuari();
		idEvents = new ArrayList<Long>();
		planningUsuari = new Planning();
		context = getApplicationContext();
		sessioIniciada = false;
		aniversari = false;
		
		//Importem les dades del SharedPreference
		importarPreference();
	}
	
	/*
	 * Funcio que importa les preferencies guardades
	 */
	@SuppressLint("SimpleDateFormat")
	private void importarPreference() {
    	SharedPreferences sharedPref = context.getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
    	notificacions = sharedPref.getBoolean("notificacions", false);
    	sessioIniciada = sharedPref.getBoolean("sessio", false);
    	//Estats kiro
    	//Com tenim una sessio iniciada tenm informacio
    	if(sessioIniciada){
    		//Ficar tota la informacio com toca, seguint l'exemple
    		usuari.setNom(sharedPref.getString("nom", ""));
    		usuari.setCognoms(sharedPref.getString("cognom", ""));
    		usuari.setAlies(sharedPref.getString("alies", ""));
    		usuari.setCorreu(sharedPref.getString("email", ""));
    		usuari.setContrasenya(sharedPref.getString("contrasenya", ""));
    		usuari.setId(sharedPref.getString("objectId", ""));
    		usuari.setPes((sharedPref.getFloat("pes",70.0f)));
    		usuari.setAlcada((sharedPref.getFloat("alcada",1.7f)));
    		usuari.setSexe((sharedPref.getString("sexe","")));
    		
    		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy"); 
    		Date myDate = null;
    		try {
				myDate = df.parse(sharedPref.getString("data", ""));
				Calendar calendar = new GregorianCalendar();
	    		int month      = calendar.get(Calendar.MONTH); 
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // Jan = 0, not 1
				dayUser = Integer.parseInt(sharedPref.getString("data", " ").substring(0, 2));
				monthUser = Integer.parseInt(sharedPref.getString("data", " ").substring(3, 5));
				
	    		if(month+1==monthUser && dayOfMonth == dayUser){
	    			aniversari = true;
	    		}

			}catch (java.text.ParseException e) {
				e.printStackTrace();
				Log.i("Kirolari","error Aniversari");
			}
    		usuari.setDataNaixement(myDate);
    		
    		
    		planningUsuari.setId(sharedPref.getInt("idPlanning", -1));
    		setDiaBasePlanning(sharedPref.getInt("diaBase", -1));
    		setSetmanaBasePlanning(sharedPref.getInt("setmanaBase", -1));
    		//Importem tot el planning
    		importarPlanningXML(planningUsuari.getId());
    		
    		
    		
    	}
    	
    	
	}

	//Getter i setter per poder controlar el planning
	/**
	 * Funcio getIdPlanningUsuari, retorna la id del planning que s'esta fent
	 */
	public int getIdPlanningUsuari(){
		return usuari.getIdPlanning();
	}
	
	/**
	 * Funcio setIdPlanningUsuari, guarda l'id del planning que li pasem
	 */
	public void setIdPlanningUsuari(int idPlanning){
		usuari.setIdPlanning(idPlanning);
	}
	//CREEM ELS METODES QUE PODEM NECESITAR AMB LES VARIABLES

	/**
	 * Funcio setPlanningUsuari, guarda el planning que li pasem
	 */
	public void setPlanningUsuari(Planning planningUsuari) {
		this.planningUsuari = planningUsuari;
	}
	
	/**
	 * Funcio diaSetmana, retorna el dia i setmana del planning
	 */
	public String diaSetmana(){
		Calendar c = Calendar.getInstance();
		int dia = (c.get(Calendar.DAY_OF_MONTH)-diaBasePlanning+1) % 7;
		if (dia == 0) dia = 7;
		int setmana = c.get(Calendar.WEEK_OF_YEAR)-setmanaBasePlanning+1;
		
		return "dia:"+ Integer.toString(dia)+" setmana: " +Integer.toString(setmana);
	}

	/**
	 * Funcio getPlanningUsuari, retorna el planning de l'usuari
	 */
	public Planning getPlanningUsuari() {
		return planningUsuari;
	}

	/**
	 * Funcio getDiaBasePlanning, retorna el dia base del planning
	 */
	public int getDiaBasePlanning() {
		return diaBasePlanning;
	}

	/*public int getSetmanaPlanning() {
		return setmanaBasePlanning;
	}*/

	/**
	 * Funcio afegirIdCalendari, afegeix la Id de l'entreno
	 */
	public void afegirIdCalendari(long eventID) {
		idEvents.add(eventID);
	}

	/**
	 * Funcio idEvents
	 */
	public ArrayList<Long> getidEvents() {
		return idEvents;
	}

	/**
	 * Funcio setDiaPlanning, guarda el dia base del planning
	 */
	public void setDiaBasePlanning(int mDay) {
		diaBasePlanning = mDay;
	}

	/**
	 * Funcio setSetmanaBasePlanning, guarda el setmana base del planning
	 */
	public void setSetmanaBasePlanning(int setmana) {
		setmanaBasePlanning = setmana;
	}

	/**
	 * 	Funcio getSetmanaBasePlanning, retorna el setmana base del planning
	 */
	public int getSetmanaBasePlanning() {
		return setmanaBasePlanning;
	}
	
	/**
	 * @return the usuari
	 */
	public Usuari getUsuari() {
		return usuari;
	}

	/**
	 * @param usuari the usuari to set
	 */
	public void setUsuari(Usuari usuari) {
		this.usuari = usuari;
	}

	/*
	 * Funcio buscarSessio, retorna la sessio corresponent amb el dia i setmana del planning
	 */
	public Sessio buscarSessio() {
		if (getIdPlanningUsuari() != -1){
			// calculem per quina sessio del planning anem
			Calendar c = Calendar.getInstance();
			int dia = (c.get(Calendar.DAY_OF_MONTH)-diaBasePlanning+1) % 7;
			if (dia == 0) dia = 7;
			int setmana = c.get(Calendar.WEEK_OF_YEAR)-setmanaBasePlanning+1;
			//Busquem la sessio
			for(int i = 0; i< getPlanningUsuari().getLlistaSessions().size(); i++){
				if(getPlanningUsuari().getLlistaSessions().get(i).getDia() == dia
						&& getPlanningUsuari().getLlistaSessions().get(i).getSetmana() == setmana){
					for (int j = 0; j<i ;j++){
						if(!getPlanningUsuari().getLlistaSessions().get(j).isCompletada()){
							getPlanningUsuari().getLlistaSessions().get(j).setCompletada(true);
							incrementarSessions();
						}
					}
					return getPlanningUsuari().getLlistaSessions().get(i);
				}

			}
		}
		return null;
	}

	/*
	 * Funcio que controla les sessions "completades" del planning
	 */
	public void incrementarSessions() {
		ParseQuery<ParseObject> queryPlanning = ParseQuery.getQuery("PlanningsUsuaris");
		queryPlanning.whereEqualTo("correu", getUsuari().getCorreu());
		queryPlanning.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> llista, ParseException e) {
		        if (e == null) {
		            ParseQuery<ParseObject> querymodificar = ParseQuery.getQuery("PlanningsUsuaris");
		    		// Retrieve the object by id
		            querymodificar.getInBackground(llista.get(0).getObjectId(), new GetCallback<ParseObject>() {
		    		  public void done(ParseObject planningUsuari, ParseException e) {
		    		    if (e == null) {
		    		    	// Now let's update it with some new data. In this case, only cheatMode and score
		    		    	// will get sent to the Parse Cloud. playerName hasn't changed.
		    		    	planningUsuari.increment("sessionsCompletades");
		    		    	planningUsuari.saveInBackground();
		    		    }
		    		  }
		    		});
		        } else {
		            Log.e("MenuPrincipal", "Error");
		        }
		    }
		});
		
	}

	/**
	 * Funcio buscarSessioPerParametre, busca una sessio a partir del dia i de la setmana
	 */
	public Sessio buscarSessioPerParametre(int dia, int setmana) {
		for(int i = 0; i< getPlanningUsuari().getLlistaSessions().size(); i++){
			if(getPlanningUsuari().getLlistaSessions().get(i).getDia() == dia
					&& getPlanningUsuari().getLlistaSessions().get(i).getSetmana() == setmana){
				return getPlanningUsuari().getLlistaSessions().get(i);
			}
		}
		return null;
	}

	public void logOut() {
		usuari = null;
  		//Guardem la informacio necessaria al sharedPreference
        SharedPreferences sharedPref = context.getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sessio", false);
        
        ParseUser.logOut();
        
        // Exemple perque et facis una idea
       // editor.putInt("nom",usuari.get(0).getString("name"));

        editor.commit();
	}
	
	/*
	 * Funcio que que tradueix un xml en objectes java per crear un nou planning
	 */
	protected void importarPlanningXML(int id){
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();
		    InputStream in_s = getApplicationContext().getAssets().open("plannings.xml");
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            parseXML(parser, id);

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Funcio que que tradueix un xml en objectes java
	 */
	private void parseXML(XmlPullParser parser, int id) throws XmlPullParserException,IOException{
		ArrayList<Sessio> sessions = null;
		Sessio sessio = null; 
        int eventType = parser.getEventType();
        boolean planning = false;
        int setmana = 0;
        
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	sessions = new ArrayList<Sessio>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    //Quan sigui el planning corresponent el guardem
                    if(name.equals("planning")){
                    	planning = false;
                    	if(parser.getAttributeValue(0).equals(Integer.toString(id))){
                    		planning = true;
                    		planningUsuari = new Planning();
                    		planningUsuari.setId(id);
                    	}
                    }
                    else if(planningUsuari != null && planning){
                    	if(name.equals("nomPlanning")){
                    		String nomPlanning = parser.nextText();
                    		planningUsuari.setNomPlanning(nomPlanning);
                    	}
                    	else if(name.equals("objectiu")){
                    		String objectiu = parser.nextText();
                    		planningUsuari.setObjectius(objectiu);
                    	}
                    	else if(name.equals("duracioSetmanes")){
                    		String duracio = parser.nextText();
                    		planningUsuari.setDuracioSetmana(Integer.parseInt(duracio));
                    	}
                    	else if(name.equals("setmana")){
                    		sessio = null;
                    		String setmanaPlanning = parser.getAttributeValue(0);
                    		setmana = Integer.parseInt(setmanaPlanning);
                    		
                    	}
                    	else if(name.equals("dia")){
                    		String diaPlanning = parser.getAttributeValue(0);
                    		sessio = new Sessio(Integer.parseInt(diaPlanning), setmana);
                    	}
                    	else if(name.equals("activitat")){
                    		String activitat = parser.nextText();
                    		sessio.setActivitat(activitat);
                    	} 
                    	else if(name.equals("descripcioActivitat")){
                    		String descripcio = parser.nextText();
                    		sessio.setDescripcio(descripcio);
                    	}
                    	else if(name.equals("duracio")){
                    		String duracio = parser.nextText();
                    		sessio.setDuracio(Integer.parseInt(duracio));
                    		sessions.add(sessio);
                    		sessio = null;
                    	}
                    }
                    
                    break;
            }
            eventType = parser.next();
        }


     	// Guardem la informacio al controler
        planningUsuari.setLlistaSessions(sessions);
  		setPlanningUsuari(planningUsuari);
  		setIdPlanningUsuari(id);
  		
	
	}

	public boolean isSessioIniciada() {
		return sessioIniciada;
	}

	public void setSessioIniciada(boolean sessioIniciada) {
		this.sessioIniciada = sessioIniciada;
	}


	public boolean isNotificacions() {
		return notificacions;
	}

	public void setNotificacions(boolean notificacions) {
		SharedPreferences sharedPref = getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("notificacions", notificacions);
		editor.commit();
		this.notificacions = notificacions;
	}
	
    /*
     * Funcio notificacioEntrenament, crea una notificio per recordar
     * al usuari que haur� de fer un entrenament
     */
	public void notificacioEntrenament(Calendar beginTime) {
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setSmallIcon(R.drawable.iconoapp);
		mBuilder.setContentTitle(getString(R.string.notificacio_entrenament));
		mBuilder.setAutoCancel(true);
		
		int dia =  beginTime.get(Calendar.DAY_OF_MONTH);
		int mes = beginTime.get(Calendar.MONTH)+1;
		mBuilder.setContentText(getString(R.string.notificacio_dia) +" "+ dia +" "+ getString(R.string.notificacio_mes) +" "+ mes +" "+ 
				getString(R.string.notificacio_activitat));
		
		Intent resultIntent = new Intent();
		
		PendingIntent resultPendingIntent;
		resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mBuilder.setContentIntent(resultPendingIntent);
		
		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		
	}
	
	
	/*
	 * Funcio que fa que un planning estigui acabat
	 */
	public void acabarPlanningAntic(){
		setIdPlanningUsuari(-1);
		//Actualitzem una dada
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PlanningsUsuaris");
		query.whereEqualTo("correu", getUsuari().getCorreu());
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> llista, ParseException e) {
		        if (e == null) {
		            ParseQuery<ParseObject> querymodificar = ParseQuery.getQuery("PlanningsUsuaris");
		            int pos = 0;
		    		// Retrieve the object by id
		            querymodificar.getInBackground(llista.get(pos).getObjectId(), new GetCallback<ParseObject>() {
		    		  public void done(ParseObject planningUsuari, ParseException e) {
		    		    if (e == null) {
		    		    	// Now let's update it with some new data. In this case, only cheatMode and score
		    		    	// will get sent to the Parse Cloud. playerName hasn't changed.
		    		    	//planningUsuari.put("acabat", true);
		    		    	planningUsuari.deleteInBackground();
		    		    }
		    		  }
		    		});
		        } else {
		            Log.e("MenuPrincipal", "Error eliminant planning");
		        }
		    }
		});
	}

	public void notificacioFutur(Calendar beginTime) {
		//Configurem la notificacio
		Intent intentsOpen = new Intent(this, AlarmReceiver.class);
		intentsOpen.setAction("com.manish.alarm.ACTION");
		pendingIntent = PendingIntent.getBroadcast(this, 111, intentsOpen, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, beginTime.getTimeInMillis(), 0, pendingIntent);
	}
	
    /*
     * Funcio que comprova si l'usuaari esta conectat a internet
     */
    public boolean estaConectat(){
    	Context context = getApplicationContext();
    	ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (connectMgr != null) {
    		NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo();
    		if (netInfo != null) {
    			for (NetworkInfo net : netInfo) {
    				if (net.getState() == NetworkInfo.State.CONNECTED) {
    					return true;
    				}
    			}
    		}
    	} 
    	return false;
    }

	public boolean isAniversari() {
		return aniversari;
	}

	public void setAniversari(boolean aniversari) {
		this.aniversari = aniversari;
	}
    
}
