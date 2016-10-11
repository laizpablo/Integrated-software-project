package edu.ub.pis2014.pis_09.esdeveniments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.ub.pis2014.pis_09.adapter.AmicListAdapter;
import edu.ub.pis2014.pis_09.amics.Amic;
import edu.ub.pis2014.pis_09.amics.Amics;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.registre.Usuari;

public class EsdevenimentInfo extends Activity {
	private Esdeveniment esdeveniment;
	private Usuari usuari;
	private ArrayList<Amic> amicsNoParticipants;
	private ProgressDialog proDialog;
	private ListView participants;
	private AmicListAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_event);
		
		usuari = ((Kirolari) getApplicationContext()).getUsuari();
		amicsNoParticipants = new ArrayList<Amic>();
		
        final TextView descripcio = (TextView) findViewById(R.id.text_descripcioEvent);
        final TextView data = (TextView) findViewById(R.id.text_dataEvent);
        participants = (ListView) findViewById(R.id.list_participants);
        final Button boto = (Button) findViewById(R.id.button_participants);
        final TextView zona = (TextView) findViewById(R.id.text_zonaEvent);
        final TextView esport = (TextView) findViewById(R.id.text_esportEvent);
        
        esdeveniment = (Esdeveniment) getIntent().getExtras().get("ESDEVENIMENT");
        startLoading();
    	Thread thread = new Thread(){
    	@Override
    		public void run() {
    			obteParticipants(esdeveniment);
    			getAmicsNoParticipants();
        		stopLoading();
    	    }
    	};
    	thread.start();
        
		
        setTitle(esdeveniment.getNom());
        descripcio.setText(esdeveniment.getDescripcio());
        zona.setText(esdeveniment.getLloc());
        esport.setText(esdeveniment.getEsport());
		data.setText(esdeveniment.getData());
		
        
        adapter = new AmicListAdapter(this, esdeveniment.getParticipants());
        participants.setAdapter(adapter);
		
		boto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		String[] nomAmics = new String[amicsNoParticipants.size()];
        		
            	for(int index=0;index<nomAmics.length;index++)
            		nomAmics[index] = amicsNoParticipants.get(index).getAlies();
            	
            	boolean[] participantsChecked = new boolean[amicsNoParticipants.size()];
            	//mirem si cada usuari es troba a la llista de participants per posar a true o false
            	//la seva respectiva participacio.
            	for(int i=0;i<amicsNoParticipants.size();i++)
        			participantsChecked[i] = esdeveniment.getParticipants().indexOf(amicsNoParticipants.get(i))!=-1;
        		
                AlertDialog.Builder builder = new AlertDialog.Builder(EsdevenimentInfo.this);
            	builder.setTitle("Selecciona els participants")
			    .setMultiChoiceItems(nomAmics, participantsChecked,new DialogInterface.OnMultiChoiceClickListener() {
			    	public void onClick(DialogInterface dialog, int item, boolean isChecked) {
			    		if(isChecked)
			    			afegeixParticipant(amicsNoParticipants.get(item));
			            	
			    		else
			    			
			            	esborraParticipant(amicsNoParticipants.get(item));
			    		
			    		adapter.setLlistaAmics(esdeveniment.getParticipants());
			    		participants.setAdapter(adapter);
			    	}
			    });
            	
				// Showing Alert Message
				builder.show();
            }
		});
		
	}

	private void afegeixParticipant(Amic amic) {
		esdeveniment.afegeixParticipant(amic);
		
		//Pugem al PARSE el nou PARTICIPANT.
		ParseObject myParseObject = new ParseObject("Participants");
		myParseObject.put("idUsuari", amic.getId());
		myParseObject.put("idEsdeveniment", esdeveniment.getId());
		myParseObject.saveEventually();
	}
	
	private void esborraParticipant(Amic amic) {
		esdeveniment.esborraParticipant(amic);
		
		//Pugem al PARSE el nou PARTICIPANT.
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Participants");
		query.whereEqualTo("idUsuari", amic.getId());
		query.whereEqualTo("idEsdeveniment", esdeveniment.getId());
		try {
			query.getFirst().deleteEventually();
		}catch (ParseException e) {
		}
		
	}
	
	private void getAmicsNoParticipants(){
		if(usuari.getAmics().isEmpty())
			Amics.buscarAmics((Kirolari) getApplicationContext());
		for(Amic amic:usuari.getAmics()){
			if(esdeveniment.indexParticipant(amic.getId())==-1)
				amicsNoParticipants.add(amic);
		}
	}

	private void obteParticipants(Esdeveniment event) {
		//Busquem quantes relacions d'amics te aquest usuari.
		ParseQuery<ParseObject> queryParticipants = ParseQuery.getQuery("Participants");
		queryParticipants.whereEqualTo("idEsdeveniment", event.getId());
	
		try {
			List<ParseObject> listParticipants = queryParticipants.find();
		
			if (!listParticipants.isEmpty()){

				//Ara busquem als usuaris.
				List<ParseQuery<ParseUser>> queryList = new ArrayList<ParseQuery<ParseUser>>();
	
				for(ParseObject participant: listParticipants){
					if(esdeveniment.indexParticipant(participant.getObjectId())==-1){
						Amic amic = usuari.getAmicAmbID(participant.getObjectId());
						if(amic == null){
							ParseQuery<ParseUser> queryUsuari = ParseUser.getQuery();
							queryUsuari.whereEqualTo("objectId",participant.getString("idUsuari"));
							queryList.add(queryUsuari);
						}else
							event.afegeixParticipant(amic);
					}
				}
				
				if(!queryList.isEmpty()){
					ParseQuery<ParseUser> query = ParseQuery.or(queryList);
		
					List<ParseUser> listUsuari = query.find();
					
					for(ParseUser el: listUsuari){
						Amic amic = new Amic(el.getObjectId(),el.getString("alias"),el.getEmail());
						event.afegeixParticipant(amic);
					}
				}
				EsdevenimentInfo.this.runOnUiThread(new Runnable(){
	        		public void run(){
	        			adapter.setLlistaAmics(esdeveniment.getParticipants());
	    	    		participants.setAdapter(adapter);
	        		}
	        		
	        	});
				
			}

		
		}catch (Exception e){
			EsdevenimentInfo.this.runOnUiThread(new Runnable(){
        		public void run(){
        			CharSequence text = getString(R.string.error_buscar_participants);
                	Toast.makeText(EsdevenimentInfo.this, text, Toast.LENGTH_SHORT).show();
        		}
        	});
		}
	}
	
	/*
     * Funcio que crear un Progress dialog
     */
    private void startLoading(){
    	proDialog = new ProgressDialog(EsdevenimentInfo.this);
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
        proDialog = null;
    } 
}
