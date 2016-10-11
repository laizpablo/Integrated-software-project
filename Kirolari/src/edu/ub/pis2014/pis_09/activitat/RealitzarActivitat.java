package edu.ub.pis2014.pis_09.activitat;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseObject;

import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;

/**
 * 
 * @author PIS_09
 *
 */
public class RealitzarActivitat  extends android.support.v4.app.FragmentActivity { 
	private GoogleMap mapa;
	private double distancia=0;
	//private boolean acabar=false;
	private LocationManager mlocManager;
	private LocationListener mlocListener;
	private int hora_inici;
	private int hora_fi;
	private Double lon_inici;
	private Double lat_inici;
	private double lat_prov;
	private double lon_prov;
	private int durada;
	private Calendar c;
	private double calories;
	private PolylineOptions lineas;
	private Kirolari kirolariControler;
	private Chronometer tempsCronometre;
	private TextView textCalories;
	private TextView textDistancia;
	private int opcio;
	@SuppressWarnings("serial")
	private ArrayList<String> lActivitats=new ArrayList<String>(){{
		add("Córrer");add("Bicicleta");add("Spinning");add("Natació");add("Aeròbic");add("Fitness");add("Caminata");add("Ioga");
	}};
	
	private Activitat activitat;
	private ParseObject activitatParse; 
	
	
	public RealitzarActivitat() {

	}

	/**
	 * onCreate()
	 * @see android.app.Application#onCreate()
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mapa_activitat);
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_activitat)).getMap();
		final ArrayList<LatLng> ubicacions =new ArrayList<LatLng>();;
		lineas = new PolylineOptions();
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		kirolariControler = (Kirolari) getApplicationContext();
		
		tempsCronometre = (Chronometer) findViewById(R.id.realitzarActivitat_temps);
		textCalories = (TextView) findViewById(R.id.realitzarActivitat_calories);
		textDistancia = (TextView) findViewById(R.id.realitzarActivitat_distancia);
		
		//Guardem activitat
		final Button boto_acabar = (Button) findViewById(R.id.button_acabar_activitatlliure);
        boto_acabar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boto_acabar();
            }
        });
        
        final Button boto_iniciar = (Button) findViewById(R.id.button_iniciar_activitatlliure);
        boto_iniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boto_iniciar();
        

	}

    /**
     * S'activa quan premem el boto de comenÃ§ar Activitat
     */
	private void boto_iniciar(){
		//S'activa el boto d'acabar quan premem el d'iniciar.
    	boto_acabar.setEnabled(true);
    	opcio=(Integer) getIntent().getSerializableExtra("opcio");
    	tempsCronometre.setBase(SystemClock.elapsedRealtime());
    	tempsCronometre.start();
    	
    	activitat=new Activitat();
    	c=Calendar.getInstance();
    	hora_inici=c.get(Calendar.HOUR_OF_DAY);
    	hora_inici=hora_inici*60+c.get(Calendar.MINUTE);                        	
    	//ubicacions.clear();            	
    	mlocListener = new LocationListener() {
			/**
			 * Cada cop que actualitza la localitzaciï¿½, calcula la distï¿½ncia entre el punt
			 * actual i l'anterior i el suma a la distï¿½ncia ja recorreguda. El primer cop que
			 * localitza, centra el mapa en la posiciï¿½        				
			 */
			@Override
			public void onLocationChanged(Location loc) {
				ubicacions.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
	            if ((lat_inici==null) & (lon_inici == null)) {
	            	lat_inici=loc.getLatitude();
	            	lon_inici=loc.getLongitude();
	            	lon_prov=lon_inici;
	            	lat_prov=lat_inici;
	            	activitat.setLatitud_inici(lat_inici);
	            	activitat.setLongitud_inici(lon_inici);
	            	CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(lat_inici,lon_inici));
	            	mapa.moveCamera(center);
	            } else {
	            	Location loctn = new Location(" ");
	            	loctn.setLatitude(lat_prov);
	            	loctn.setLongitude(lon_prov);
	            	
	            	
	            	distancia=distancia+(loc.distanceTo(loctn)/1000);//distance(lat_prov,lon_prov,loc.getLatitude(),loc.getLongitude(),'K');
	            	lineas.add(new LatLng(lat_prov,lon_prov));
	            	lineas.add(new LatLng(loc.getLatitude(),loc.getLongitude()));
	            	lineas.color(Color.BLUE);
	            	lineas.width(5);
	            	mapa.addPolyline(lineas);
	            	lat_prov=loc.getLatitude();
	            	lon_prov=loc.getLongitude();
	            	activitat.setDistancia(distancia); //en km
	            	
	            	calculTotal(opcio);
	            	
	            	textDistancia.setText(String.valueOf((double)Math.round(distancia*100)/100));
	            	textCalories.setText(String.valueOf((double)Math.round(calories*100)/100));
	            }
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onProviderEnabled(String provider){
			}
			
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras){
			
			}


			};
			mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
	
		    }
		});
        
		if (mapa != null) { 
		    CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(41.3888196,2.1628499)); //Posar localitzaciï¿½ centre BCN
			CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
			
			mapa.moveCamera(center);
			mapa.animateCamera(zoom);
		}
	}
            
	/**
	 * S'activa en premer el boto per acabar l'activitat
	 */
	private void boto_acabar(){
		

		tempsCronometre.stop();
    	calculTotal(opcio);

    	if (mlocManager==null) {
        	mlocManager.removeUpdates(mlocListener);
    	}

    	//Guardem l'objecte al parse
    	activitatParse = new ParseObject("Activitat");
    	activitatParse.put("Activitat", lActivitats.get(opcio));
    	activitatParse.put("Duracio", activitat.getDurada());
    	activitatParse.put("Distancia", activitat.getDistancia());
    	activitatParse.put("Calories", activitat.getCalories());
    	activitatParse.put("idUsuari", kirolariControler.getUsuari().getId());
    	activitatParse.saveInBackground();
    	
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.realitzar_activitat, menu);
		return true;
	}
	
	public void onBackPressed(){
		//no fem res ja que no deixarem sortir de la pantalla fins que no s'acabi l'activitat
		finish();
	}
	

	
	

	/**
	 * Calcula les calories consumides (aproximadament) durant la realitzaciÃ³ de l'activitat
	 * font: http://www.exercise4weightloss.com/calories-burned-during-exercise.html
	 * @param duration
	 * @param opcio
	 * @return double calories
	 */
	private double calculCalories(int duration,int opcio){
		double calories;
		calories=(duration*calculMET(opcio)*3.5*(kirolariControler.getUsuari().getPes()))/200;		
		return calories;
	}
	
	/**
	 * Calcula el Metabolic Equivalent of Task
	 * @return double met 
	 */
	private double calculMET(int opcio){
		double met;
		///Calories per 30' d'activitat (aprox.){correr-325,bici-150,spinning-400,natacio-250,aerobic-180,fitness-110,Caminata-200,Ioga-150}
		@SuppressWarnings("serial")
		ArrayList<Integer>caloriesOpcio=new ArrayList<Integer>(){{
			add(325);add(150);add(400);add(250);add(180);add(110);add(200);add(150);
		}};
		met=(caloriesOpcio.get(opcio)*200)/(30*3.5*kirolariControler.getUsuari().getPes());
		return met;
	}
	
	private void calculTotal(int opcio){
		//Agafem les dades
    	c=Calendar.getInstance();
    	hora_fi=c.get(Calendar.HOUR_OF_DAY);
    	hora_fi=hora_fi*60+c.get(Calendar.MINUTE);
    	durada=Math.abs(hora_fi-hora_inici);
    	activitat.setDurada(durada);
    	calories=calculCalories(durada,opcio);
    	activitat.setCalories(calories);
	}

}   
   