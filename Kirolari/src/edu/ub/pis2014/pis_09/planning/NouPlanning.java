package edu.ub.pis2014.pis_09.planning;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.parse.ParseObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;

   /**
     * Fragment that appears in the "content_frame", shows a planet
     */
 public class NouPlanning extends Fragment {
	 public static final String ARG_OPTION_NUMBER = "option_number";
     private Spinner objectiuSpinner;
     private Kirolari kirolariControler;
     private int mYear;
 	 private int mMonth;
 	 private int mDay;
 	 private long eventID;
 	 private TextView text_descripcioPlanning;

        
     public NouPlanning() {
            // Empty constructor required for fragment subclasses
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	 final View rootView = inflater.inflate(R.layout.fragment_nou_planning, container, false);
         int i = getArguments().getInt(ARG_OPTION_NUMBER);
         text_descripcioPlanning = (TextView) rootView.findViewById(R.id.planning_descripcioPlanning);
         String titol = getResources().getStringArray(R.array.opcions)[i];
         getActivity().setTitle(titol);
         posarDescripcio(1);  
         kirolariControler = (Kirolari) getActivity().getApplicationContext();
            
         objectiuSpinner = (Spinner) rootView.findViewById(R.id.planning_objectiuSpinner);
         objectiuSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        		 posarDescripcio(objectiuSpinner.getSelectedItemPosition()+1);

             }
        	 @Override
        	 public void onNothingSelected(AdapterView<?> arg0) {
        		 // TODO Auto-generated method stub		
        	 }
         });
         
         //Boto guardar planning
         final Button button_guardar = (Button) rootView.findViewById(R.id.button_guardar);
 		 button_guardar.setOnClickListener(new View.OnClickListener() {
 			 public void onClick(View v) {
 				 botoGuardar();
 			 }
 		 });
             
 		 //Boto cancelar planning
 		 final Button button_cancelar = (Button) rootView.findViewById(R.id.button_cancelar);
 		 button_cancelar.setOnClickListener(new View.OnClickListener() {
 			 public void onClick(View v) {
 				 botoCancelar();
 			 }
 		 });
         return rootView;
     }
     
     /*
      * Funcio que dona l'objectiu del planning seleccionat 
      */
     private void posarDescripcio(int id) {
    	 XmlPullParserFactory pullParserFactory;
    	 Planning planning = new Planning();
    	 try {
    		 pullParserFactory = XmlPullParserFactory.newInstance();
    		 XmlPullParser parser = pullParserFactory.newPullParser();
    		 InputStream in_s = getActivity().getApplicationContext().getAssets().open("plannings.xml");
    		 parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    		 parser.setInput(in_s, null);
    		 planning = parseXMLObjectiu(parser, id);
 	        
    		 text_descripcioPlanning.setText(planning.getObjectius()+"\nDuracio: "+planning.getDuracioSetmana() + " setmanes.");

    	 } catch (XmlPullParserException e) {
    		 e.printStackTrace();
    	 } catch (IOException e) {
    		 e.printStackTrace();
    	 }
    	 
     }

	private Planning parseXMLObjectiu(XmlPullParser parser, int id) throws XmlPullParserException, IOException {
		
		Planning planningUsuari = null;
        int eventType = parser.getEventType();
        boolean planning = false;
        
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
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
                    }
                    break;
            }
            eventType = parser.next();
        }
        return planningUsuari;
	}

	/*
 	 * Funcio botoCancelar, implementa la opcio quan es vol cancel�lar un planning
 	 */
 	private void botoCancelar() {
 		CharSequence text = "Nou Planning cancel.lat!";
 		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
 		getActivity().getSupportFragmentManager().popBackStack();
 		getActivity().getSupportFragmentManager().popBackStack();
 			
 	}

 	/*
 	 * Funcio botoGuardar, implementa la opcio quan es vol guardar el planning
 	 */
 	private void botoGuardar() {
 		//Comprovem si hi ha un planning antic
 		if(kirolariControler.getIdPlanningUsuari()!=-1){
 			kirolariControler.acabarPlanningAntic();
 		}
 		
 		//Cridem a la funcio CrearNouPlanningXML
 		crearNouPlanningXML(objectiuSpinner.getSelectedItemPosition()+1);
 		
 		CharSequence text = "Nou Planning creat!";
 		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
 		//pasar a la classe sessions
         	
 		/*Fragment fragment_sessions = new Sessions();
 		//Enviem les dades necesaries
 		FragmentManager fragmentManager = getFragmentManager();
 		fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_sessions).addToBackStack(null).commit();*/
 		
 		Intent i = new Intent(getActivity(),Sessions.class);
     	startActivity(i);
     	getActivity().getSupportFragmentManager().popBackStack();
 		getActivity().getSupportFragmentManager().popBackStack();
 	}
 	
 	/*
	 * Funcio afegirEntrenamentCalendari, afeguim al calendari el nou entrenament
	 */
	private long afegirEntrenamentCalendari(String title, String description) {				
			final Calendar c = Calendar.getInstance();
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			mYear = c.get(Calendar.YEAR);
        	mMonth = c.get(Calendar.MONTH);
        	mDay = c.get(Calendar.DAY_OF_MONTH);
        	
        	//configuracio perque comencin un dilluns
			//diumenge +1
			if(dayOfWeek==1){
				mDay++;
			}
			//dilluns +0
			else if(dayOfWeek==2){
				
			}
			//dimarts +6
			else if(dayOfWeek==3){
				mDay = mDay +6;
			}
			//dimecres +5
			else if(dayOfWeek==4){
				mDay = mDay + 5;
			}
			//dijous +4
			else if(dayOfWeek==5){
				mDay = mDay + 4;
			}
			//divendres +3
			else if(dayOfWeek==6){
				mDay = mDay + 3;
			}
			//Dissabte +2
			else if(dayOfWeek==7){
				mDay = mDay + 2;
			}

        	//Definim la data
        	Calendar beginTime = Calendar.getInstance();
        	beginTime.set(mYear, mMonth, mDay, 8, 00);
        	Calendar endTime = Calendar.getInstance();
        	endTime.set(mYear, mMonth, mDay);
        	        	
        	ContentResolver cr = getActivity().getContentResolver();
        	ContentValues values = new ContentValues();
        	values.put(Events.DTSTART, beginTime.getTimeInMillis());
        	values.put(Events.DTEND, endTime.getTimeInMillis());
        	values.put(Events.TITLE, title);
        	values.put(Events.DESCRIPTION, description);
        	values.put(Events.CALENDAR_ID, 1);
        	values.put(Events.EVENT_TIMEZONE, "Barcelona");
        	values.put(Events.HAS_ALARM, true);
        	values.put(Events.ALL_DAY, true);
        	Uri uri = cr.insert(Events.CONTENT_URI, values);        	
        	
        	kirolariControler.setDiaBasePlanning(beginTime.get(Calendar.DAY_OF_MONTH));
        	kirolariControler.setSetmanaBasePlanning(beginTime.get(Calendar.WEEK_OF_YEAR));
        	
        	//Mirem si l'usuari te activada aquesta opcio
        	if(kirolariControler.isNotificacions()){
        		kirolariControler.notificacioEntrenament(beginTime);
        		kirolariControler.notificacioFutur(beginTime);
        	}

        	return Long.parseLong(uri.getLastPathSegment());
        	
		}

	/*
	 * Funcio que que tradueix un xml en objectes java per crear un nou planning
	 */
	private void crearNouPlanningXML(int id){
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();
		    InputStream in_s = getActivity().getApplicationContext().getAssets().open("plannings.xml");
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
		Planning planningUsuari = null;
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


        // Afegim un entrenament en el calendari de l'usuari
     	eventID = afegirEntrenamentCalendari("KiroLari",planningUsuari.getObjectius());
     	kirolariControler.afegirIdCalendari(eventID);
     	// Guardem la informacio al controler
     	planningUsuari.setLlistaSessions(sessions);
  		kirolariControler.setPlanningUsuari(planningUsuari);
  		kirolariControler.setIdPlanningUsuari(id);
  		
  		//guardem les dades importats al sharedPreference
  		guardaInformacio();
  		
  		//Creem una copia de seguretat al servidor
  		copiaSeguretat();
	}
	
	/*
	 * Guarda l'informacio necessaria 
	 */
	private void guardaInformacio() {
		Context context = getActivity().getApplicationContext();
  		//Guardem la informacio necessaria al sharedPreference
        SharedPreferences sharedPref = context.getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("idPlanning", kirolariControler.getIdPlanningUsuari());
        editor.putInt("diaBase", kirolariControler.getDiaBasePlanning());
        editor.putInt("setmanaBase", kirolariControler.getSetmanaBasePlanning());
        editor.commit();
		
	}

	/*
	 * Funcio que fa una copia de seguretat al servidor
	 * respecte el nou planning de l'usuari.
	 */
	private void copiaSeguretat() {
		try{
			ParseObject crearPlanning = new ParseObject("PlanningsUsuaris");
			
			crearPlanning.put("correu",kirolariControler.getUsuari().getCorreu());
			crearPlanning.put("idPlanning",kirolariControler.getIdPlanningUsuari());
			crearPlanning.put("diaBase",kirolariControler.getDiaBasePlanning());
			crearPlanning.put("setmanaBase",kirolariControler.getSetmanaBasePlanning());
			crearPlanning.put("acabat",false);
			crearPlanning.put("sessionsCompletades",0);
			crearPlanning.saveInBackground();
		}
		catch(Exception e){
			Log.i("NouPlanning","error copiaParse");
		}
	}
}