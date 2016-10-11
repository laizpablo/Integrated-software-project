package edu.ub.pis2014.pis_09.planning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.ub.pis2014.pis_09.activitat.Activitat;
import edu.ub.pis2014.pis_09.adapter.HistorialListAdapter;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;

   /**
     * Fragment that appears in the "content_frame", shows a planet
     */
public class Historial extends Fragment {
	public static final String ARG_OPTION_NUMBER = "option_number";
    private View rootView;
    private ArrayList<Activitat>llistaActivitats;
    private int temps=0;
    private double distancia=0.0d;
    private double calories=0.0d;
  	private ProgressDialog proDialog;
   	private Activity activity;
   	private boolean listenerDisable;
   	private ListView llistaHistorial;
  	private HistorialListAdapter adapter;
    private Kirolari kirolariControler;
    //Ens indica el numero del mes que volem veure, si volem veure l'anual el mes es -1
    private int mes=-1;
    private int contActivitats;
    //ArrayList per guardar les diferents dades segons els mesos per passar-lo a l'historial
    private ArrayList<Double> distanciesMes;
    private ArrayList<Integer> tempsMes;
    private ArrayList<Double> caloriesMes;
	private ArrayList<Double> distanciesDia;
	private ArrayList<Integer> tempsDia;
	private ArrayList<Double> caloriesDia;
                
    /**
     * Constructor   
     */
	public Historial() {
            // Empty constructor required for fragment subclasses      	
	}

	/**
	 * Funcio onCreate
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	
		rootView = inflater.inflate(R.layout.fragment_historial, container, false);
		int i = getArguments().getInt(ARG_OPTION_NUMBER);
		String titol = getResources().getStringArray(R.array.opcions)[i];
		getActivity().setTitle(titol);
		activity = getActivity();
		llistaActivitats = new ArrayList<Activitat>();
		final Spinner spinnerAnys = (Spinner) rootView.findViewById(R.id.historial_spinner_anys);
		final Spinner spinnerMesos = (Spinner) rootView.findViewById(R.id.historial_spinner_mesos);
		listenerDisable = false;    
		kirolariControler = (Kirolari) getActivity().getApplicationContext();

		/*
		 * Listener de l'spinner d'anys, crida a cercar historial total si esta seleccionat "Total"
		 * o segons el mes seleccionat a spinnerMesos si esta seleccionat "2014"
		 */
        spinnerAnys.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        		if(spinnerAnys.getSelectedItem().toString().equals("Total")){
        			spinnerMesos.setEnabled(false);
		            listenerDisable = false;
		           	mes=-1;
		            startLoading();
		            Thread thread = new Thread(){
		            	@Override
		            	public void run() {
		            		cercarHistorial(mes);
		            	}
		            };
		            thread.start();
        		}else if(spinnerAnys.getSelectedItem().toString().equals("2014")){
        			spinnerMesos.setEnabled(true);
        			listenerDisable = true;
        			
        			mes=spinnerMesos.getSelectedItemPosition()-1;
        			startLoading();
        			Thread thread = new Thread(){
        				@Override
        				public void run() {
        					cercarHistorial(mes);
        				}
        			};
        			thread.start();
		        }

            }
        	@Override
			public void onNothingSelected(AdapterView<?> arg0) {		
			}
        });
        /*
         * Crida cercarHistorial segons el mes seleccionat a l'spinner
         */
        spinnerMesos.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
        		mes=position-1;	
			    if(listenerDisable){
			    	startLoading();
				    Thread thread = new Thread(){
				    	@Override
				    	public void run() {
				    		cercarHistorial(mes);
				    	}
				    };
				    thread.start();
			    }
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
            	
        });

        /*
         * Guarda les dades que necessitem per l'historial grafic en un bundle i inicialitza el fragment de l'historial grafic
         */
        final Button button_grafic = (Button) rootView.findViewById(R.id.historial_button_grafic);
        button_grafic.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Bundle args=new Bundle();
        		args.putSerializable("DistanciesMes", (Serializable)distanciesMes);
        		args.putSerializable("CaloriesMes", (Serializable)caloriesMes);
        		args.putSerializable("TempsMes", (Serializable)tempsMes);
        		args.putSerializable("DistanciesDia",(Serializable)distanciesDia);
        		args.putSerializable("TempsDia",(Serializable)tempsDia);
        		args.putSerializable("CaloriesDia",(Serializable)caloriesDia);
        		args.putInt("mesHistorial",mes);
        		args.putInt("opcioHistorial",1);
        		Fragment historial_grafic = new HistorialGrafic();
                historial_grafic.setArguments(args);
        		FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, historial_grafic).commit();	
            }
        });
            
        return rootView;
	}

	/**
	 * crea el listView 
	 * @see HistorialListAdapter
	 */
	private void crearListView() {
		llistaHistorial = (ListView) rootView.findViewById(R.id.list_activitats);
		if(!llistaActivitats.isEmpty()){
			adapter = new HistorialListAdapter(getActivity().getApplicationContext(), llistaActivitats);
			llistaHistorial.setAdapter(adapter);
		}
		// ListView Item Click Listener
		llistaHistorial.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// alert Dialog
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
				// Setting Dialog Message
				alertDialog.setTitle(((Activitat) llistaHistorial.getItemAtPosition(position)).getActivitat());
				alertDialog.setMessage(llistaHistorial.getItemAtPosition(position).toString());
				// Setting Positive Button
				alertDialog.setPositiveButton(getString(R.string.acceptar),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						//No fem res, nomes informem
					}
				});
				// Showing Alert Message
				alertDialog.show();
		     	}
		}); 
	}

	/**
	 * Cerca l'historial de l'usuari al PARSE i, segons el mes seleccionat, mostra les activitats realitzades en aquell mes
	 * i les guarda adequadament per passar-les a l'historial grafic
	 * @param mesActivitat
	 */
	private void cercarHistorial(int mesActivitat) {
		mes = mesActivitat;
		calories=0.0d;
    	distancia=0.0d;
    	temps=0;
		ParseQuery<ParseObject> queryHistorial = ParseQuery.getQuery("Activitat");
		queryHistorial.whereEqualTo("idUsuari",kirolariControler.getUsuari().getId());
		queryHistorial.findInBackground(new FindCallback<ParseObject>() {
		    @SuppressWarnings({ "deprecation", "serial" })
			public void done(List<ParseObject> historialList, ParseException e) {
		        if (e == null) {
		        	contActivitats=0;
		            //Convertim la llista en objecte d'activitats
		        	llistaActivitats.clear();
		        	/*
		        	 * Per guardar les diferents dades per mesos per tal de mostrar-los al grafic anual
		        	 */
		        	distanciesMes=new ArrayList<Double>() {{
		            	add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);
		            }};
		            tempsMes=new ArrayList<Integer>(){{
		            	add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);
		            }};
		            caloriesMes=new ArrayList<Double>() {{
		            	add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);
		            }};
		            /*
		             * Per guardar les dades per dia per tal de mostrar-les en el grafic mensual
		             */
		            caloriesDia=new ArrayList<Double>() {{
		    			add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d);
		            	add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d);        
		            }};
		            
		            distanciesDia=new ArrayList<Double>() {{
		            	add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d);
		            	add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d); add(0.0d); add(0.0d);add(0.0d);
		            }};
		            tempsDia=new ArrayList<Integer>(){{
		    			add(0); add(0); add(0);add(0); add(0); add(0);add(0); add(0); add(0);add(0); add(0); add(0);add(0); add(0); add(0);add(0);add(0);
		            	add(0); add(0); add(0);add(0); add(0); add(0);add(0); add(0); add(0);add(0); add(0); add(0);add(0); add(0); add(0);add(0);add(0);
		            }};
		            
		            /*
		             * Recorre l'historial
		             */
		        	for(int i=0; i < historialList.size();i++){
		        		//Comprova el mes en que s'ha realitzat l'activitat
		        		if (mes==-1||mes==historialList.get(i).getCreatedAt().getMonth()){
		        			//Afegeix l'activitat a la llista d'activitats
		        			llistaActivitats.add(new Activitat(historialList.get(i).getString("Activitat"),historialList.get(i).getDouble("Distancia"),
			        				historialList.get(i).getInt("Duracio"),historialList.get(i).getDouble("Calories"),historialList.get(i).getCreatedAt()));
		        			//Calcula el consum de calories distancia i temps
		        			calories=calories+historialList.get(i).getDouble("Calories");
		        			distancia=distancia+historialList.get(i).getDouble("Distancia");
		        			temps=temps+historialList.get(i).getInt("Duracio");

		        			//Calcula les calories, distancia i temps de cada dia en cas de que volguem veure les dades d'un mes concret
		        			if(mes!=-1){
		        				String day = (String) android.text.format.DateFormat.format("dd", historialList.get(i).getCreatedAt());
		        				caloriesDia.set(Integer.parseInt(day)+1, caloriesDia.get(Integer.parseInt(day)+1)+historialList.get(i).getDouble("Calories"));
				        		distanciesDia.set(Integer.parseInt(day)+1, distanciesDia.get(Integer.parseInt(day)+1)+historialList.get(i).getDouble("Distancia"));
				        		tempsDia.set(Integer.parseInt(day)+1, tempsDia.get(Integer.parseInt(day)+1)+historialList.get(i).getInt("Duracio"));
		        			}
		        			contActivitats+=1;
		        		}
		        		
		        		//Per guardar les calories/distancia/temps consumits per mesos
		        		caloriesMes.set(historialList.get(i).getCreatedAt().getMonth(), caloriesMes.get(historialList.get(i).getCreatedAt().getMonth())+historialList.get(i).getDouble("Calories"));
		        		distanciesMes.set(historialList.get(i).getCreatedAt().getMonth(), distanciesMes.get(historialList.get(i).getCreatedAt().getMonth())+historialList.get(i).getDouble("Distancia"));
		        		tempsMes.set(historialList.get(i).getCreatedAt().getMonth(), tempsMes.get(historialList.get(i).getCreatedAt().getMonth())+historialList.get(i).getInt("Duracio"));		        		
		        	}
		        	activity.runOnUiThread(new Runnable(){
		        		public void run(){
		        			TextView numCalories=(TextView) rootView.findViewById(R.id.historial_textView_numCalories);
				        	numCalories.setText(String.valueOf((double)Math.round(calories*100)/100));
				        	
				        	TextView numDistancia=(TextView) rootView.findViewById(R.id.historial_textView_numDistancia);
				        	numDistancia.setText(String.valueOf((double)Math.round(distancia*100)/100)+" km");
				        	
				        	TextView numTemps=(TextView) rootView.findViewById(R.id.historial_textView_numTemps);
				        	numTemps.setText(String.valueOf(temps)+" min");
				        	
				        	TextView numPes=(TextView) rootView.findViewById(R.id.historial_textView_numPes);
				        	numPes.setText(String.valueOf((double)Math.round(kirolariControler.getUsuari().getPes()*100)/100)+" kg");
				        	
				        	TextView numSessions=(TextView) rootView.findViewById(R.id.historial_textView_numSessions);
				        	numSessions.setText(String.valueOf(contActivitats));
				        	
				        	if(llistaActivitats.isEmpty()) {
				        		llistaActivitats.add(new Activitat());
				        	}
				        	crearListView();
				        	Log.i("Historial", "final runOnUiThread");
				        	stopLoading();
		        		}
		        		
		        	});
		        	
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
		
	}

    /*
     * Funcio que crear un Progress dialog mentre carregar l'usuari
     */
    private void startLoading(){
    	proDialog = new ProgressDialog(rootView.getContext());
        proDialog.setMessage(getString(R.string.carregant));
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
        
    }
    
    /*
     * Funcio que para el Progress dialog
     */
	private void stopLoading(){
    	proDialog.dismiss();
    } 

}
