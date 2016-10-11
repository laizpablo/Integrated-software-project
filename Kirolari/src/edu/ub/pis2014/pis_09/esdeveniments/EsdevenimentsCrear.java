package edu.ub.pis2014.pis_09.esdeveniments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import edu.ub.pis2014.pis_09.amics.Amic;
import edu.ub.pis2014.pis_09.amics.Amics;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.registre.Usuari;

public class EsdevenimentsCrear extends Fragment {
	private Usuari usuari;
	private ArrayList<Amic> amicsUsuari;
	private Esdeveniment esdeveniment;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_crear_event,container,false);
		
		usuari = ((Kirolari) getActivity().getApplicationContext()).getUsuari();
		amicsUsuari = usuari.getAmics();
		esdeveniment = new Esdeveniment();
		
		final Button botoCrear = (Button) rootView.findViewById(R.id.crearEvent_button_crear);
		final Button botoAfegirParticipants = (Button) rootView.findViewById(R.id.button_participants);
        final EditText nom = (EditText) rootView.findViewById(R.id.editText_nomCrearEvent);
        final EditText descripcio = (EditText) rootView.findViewById(R.id.editText_descripcioCrearEvent);
        final EditText data = (EditText) rootView.findViewById(R.id.editText_dataCrearEvent);
        final EditText hora = (EditText) rootView.findViewById(R.id.editText_horaCrearEvent);
        final EditText numParticipants = (EditText) rootView.findViewById(R.id.editText_participantsCrearEvent);
        final Spinner zona = (Spinner) rootView.findViewById(R.id.spinner_zonaCrearEvent);
		final Spinner esport = (Spinner) rootView.findViewById(R.id.spinner_esportCrearEvent);
        
		if(usuari.getAmics().isEmpty())
			Amics.buscarAmics((Kirolari) getActivity().getApplicationContext());
        
        botoAfegirParticipants.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		final String[] nomAmics = new String[usuari.getAmics().size()];
        		
            	for(int index=0;index<nomAmics.length;index++)
            		nomAmics[index] = usuari.getAmics().get(index).getAlies();
            	
            	boolean[] participantsChecked = new boolean[amicsUsuari.size()];
            	//mirem si cada usuari es troba a la llista de participants per posar a true o false
            	//la seva respectiva participacio.
            	for(int i=0;i<amicsUsuari.size();i++)
        			participantsChecked[i] = esdeveniment.getParticipants().indexOf(amicsUsuari.get(i))!=-1;
        		
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
            	builder.setTitle("Selecciona els participants")
			    .setMultiChoiceItems(nomAmics, participantsChecked,new DialogInterface.OnMultiChoiceClickListener() {
			    	public void onClick(DialogInterface dialog, int item, boolean isChecked) {
			    		int numeroParticipants = Integer.parseInt(numParticipants.getText().toString());
			    		if(isChecked){
			            	numeroParticipants++;
			            	esdeveniment.afegeixParticipant(amicsUsuari.get(item));
			            }else{
			            	numeroParticipants--;
			            	esdeveniment.esborraParticipant(amicsUsuari.get(item));
			            }
			            numParticipants.setText(Integer.toString(numeroParticipants));
			    	}
			    });
            	
				// Showing Alert Message
				builder.show();
            }
        });
        
		
        botoCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boolean nomOmplert = false;
            	boolean descripcioOmplert = false;
            	boolean dataOmplert = false;
            	boolean horaOmplert = false;
            	
                // Comprobem que hagi ficat totes les dades
            	//NOM
        		if(nom.getText().toString().equals(""))
        			nom.setError("Camp no omplert");
        		else
        			nomOmplert=true;
        		
            	
        		//DESCRIPCIO
        		if(descripcio.getText().toString().equals(""))
        			descripcio.setError("Camp no omplert");		
        		else
        			descripcioOmplert=true;
        		
        		//DATA
        		String dataEvent = comprobaData(data.getText().toString());
        		if(dataEvent == null)
        			data.setError("Camp no omplert");
        		else
        			dataOmplert=true;

        		//HORA
        		boolean correcteHora = comprobaHora(hora.getText().toString());
        		
        		if(hora.getText().toString().equals(""))
        			hora.setError("Camp no omplert");		
        		else if(!correcteHora)
        			hora.setError("Camp no vàlid");
        		else
        			horaOmplert=true;
        		
        		//Assegurem que tots els camps estan omplerts
            	if(nomOmplert && descripcioOmplert && dataOmplert && horaOmplert){
            		//afegim esdeveniment al parse
	            	ParseObject myEvent = new ParseObject("Esdeveniments");
	            	myEvent.put("nom", nom.getText().toString());
	            	myEvent.put("descripcio", descripcio.getText().toString());
	            	myEvent.put("lloc", zona.getSelectedItem().toString());
	            	myEvent.put("esport", esport.getSelectedItem().toString());
	            	myEvent.put("hour", hora.getText().toString());
	            	
	            	String diaIHora = dataEvent+" "+hora.getText().toString();
	            	
	            	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            		Date date = null;
            		try{
						date = df.parse(diaIHora);
					}catch (Exception e){
						
					}
            		
	            	myEvent.put("date", date);
	            	try {
						myEvent.save();
					} catch (ParseException e) {
						CharSequence text = getString(R.string.error_crear_event);
		            	Toast.makeText(rootView.getContext(), text, Toast.LENGTH_SHORT).show();
					}
	            	
	            	int nParticipants = Integer.parseInt(numParticipants.getText().toString())-1;
	            	
	            	//Afegim noves entrades a la classe participants del Parse.
	            	ParseObject myParticipation = new ParseObject("Participants");
	            	myParticipation.put("idUsuari", usuari.getId());
	            	myParticipation.put("idEsdeveniment", myEvent.getObjectId());
	            	myParticipation.saveInBackground();
	            	
	            	for(int index=0;index<nParticipants;index++){
	            		ParseObject participation = new ParseObject("Participants");
	            		participation.put("idUsuari", esdeveniment.getParticipants().get(index).getId());
	            		participation.put("idEsdeveniment", myEvent.getObjectId());
	            		participation.saveInBackground();
	            		
	            		Amic amic = usuari.getAmicAmbID(esdeveniment.getParticipants().get(index).getId());
	            		esdeveniment.afegeixParticipant(amic);
	            	}
	        		
	            	esdeveniment.setId(myEvent.getObjectId());
	            	esdeveniment.setNom(myEvent.getString("nom"));
	            	esdeveniment.setEsport(myEvent.getString("esport"));
	            	esdeveniment.setLloc(myEvent.getString("lloc"));
	            	esdeveniment.setData((Date) myEvent.getDate("date"));
		        	
	            	usuari.getEsdeveniments().add(esdeveniment);

		        	CharSequence text = getString(R.string.valid_crear_event);
	            	Toast.makeText(rootView.getContext(), text, Toast.LENGTH_SHORT).show();
	            	
	            	nom.setText("");
	            	descripcio.setText("");
	            	data.setText("");
	            	hora.setText("");
	            	numParticipants.setText("1");
	            	esdeveniment = null;
            	}
            	
            	

            }
        });
		return rootView;
		
	}
	
	private String comprobaData(String data){
		data = data.replace(".", "-");
		data = data.replace("/", "-");
		String[] date = data.split("-");

        if(date.length == 3){
            if(Integer.parseInt(date[0])<=31 && Integer.parseInt(date[0])>0 && Integer.parseInt(date[1])<=12 &&
               Integer.parseInt(date[1])>0 && Integer.parseInt(date[2])<=2025 && Integer.parseInt(date[2])>=2010){
            	return data;
            }
        }
        
        return null;
	}
	
	private boolean comprobaHora(String hora){
		String[] hour = hora.split(":");
		
        if(hour.length == 2){
            if(Integer.parseInt(hour[0])<24 && Integer.parseInt(hour[0])>=0 &&
            		Integer.parseInt(hour[1])<60 && Integer.parseInt(hour[1])>=0){
            	return true;
            }
        }
        
        return false;
	}
}
