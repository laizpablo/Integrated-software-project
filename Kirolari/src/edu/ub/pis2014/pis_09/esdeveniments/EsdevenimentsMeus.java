package edu.ub.pis2014.pis_09.esdeveniments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.ub.pis2014.pis_09.adapter.EsdevenimentListAdapter;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.registre.Usuari;

public class EsdevenimentsMeus extends Fragment {
	private ListView llista;
	private EsdevenimentListAdapter adapter;
	private Usuari usuari;
	private ProgressDialog proDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_events_meus,container,false);
		
		usuari = ((Kirolari) getActivity().getApplicationContext()).getUsuari();
		final TextView noEvents = (TextView) rootView.findViewById(R.id.text_noEvent);
        llista = (ListView) rootView.findViewById(R.id.list_esdevenimentsMeus);
        
        startLoading();
    	Thread thread = new Thread(){
    	@Override
    		public void run() {
	    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Participants");
	        	query.whereEqualTo("idUsuari", usuari.getId());
	    		//Si existeix algun esdeveniment nou, els busquem.
	            try {
	    			if(query.count() != usuari.getEsdeveniments().size())
	    				buscarEsdeveniments(query);
	    			getActivity().runOnUiThread(new Runnable(){
		        		public void run(){
		        			adapter = new EsdevenimentListAdapter(getActivity().getApplicationContext());
		        	        actualitzaLlista(noEvents);
		            		stopLoading();
		        		}
		        		
		        	});
	    		}catch (ParseException e) {
	    			
	    			getActivity().runOnUiThread(new Runnable(){
		        		public void run(){
		        			CharSequence text = getString(R.string.error_buscar_event);
		        			Toast.makeText(rootView.getContext(), text, Toast.LENGTH_SHORT).show();
		        			stopLoading();
		        		}
		        		
		        	});
	    		}
    		}
    	};
    	thread.start();
    	
    	//Si mantenim pressionat ens dona la possibilitat d'abandonar l'esdeveniment
        llista.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View arg1,final int position, long id) {
				final Esdeveniment selected = (Esdeveniment) usuari.getEsdeveniments().get(position);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());				 

		        // Setting Dialog Message
		        alertDialog.setMessage(getString(R.string.abandonar_esdev));
		 
		        // Setting Positive Button
		        alertDialog.setPositiveButton(getString(R.string.si),
		        		new DialogInterface.OnClickListener() {
		        			public void onClick(DialogInterface dialog,int which) {
		        				//eliminem l'esdeveniment de la llista de l'usuari.
				            	usuari.abandonaEsdeveniment(selected);
				            	
		        				//ho fem tambe del parse
				            	abandonarEsdeveniment(selected);
				            	
				            	actualitzaLlista(noEvents);
				            }
		        		});

		        // Setting Negative Button
		        alertDialog.setNegativeButton(getString(R.string.no),
		        		new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int which) {
				            	dialog.cancel();
				            }
				        });
		 
		        // Showing Alert Message
		        alertDialog.show();
				return false;
			}
		});
		
		//si cliquem sobre l'esdeveniment podrem veure la seva informacio
		llista.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
				Intent i = new Intent(getActivity(),EsdevenimentInfo.class);
				i.putExtra("ESDEVENIMENT",usuari.getEsdeveniments().get(position));
				startActivity(i);
				
			}
		});
        return rootView;
	} 
	
	private void abandonarEsdeveniment(Esdeveniment event) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Participants");
    	query.whereEqualTo("idUsuari", usuari.getId());
    	query.whereEqualTo("idEsdeveniment", event.getId());
    	
    	try {
			ParseObject participacio = query.getFirst();
			participacio.deleteInBackground();
			if(query.count()==0){
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Esdeveniments");
		    	query2.whereEqualTo("objectId", event.getId());
		    	ParseObject esdeveniment = query.getFirst();
		    	esdeveniment.deleteInBackground();
			}
		}catch (ParseException e) {
			CharSequence text = getString(R.string.error_abandonar_event);
        	Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void buscarEsdeveniments(ParseQuery<ParseObject> queryParticipant) throws ParseException{
		List<ParseObject> list = queryParticipant.find();
		if (!list.isEmpty()){
			 //Ara busquem els events.
			List<ParseQuery<ParseObject>> queryList = new ArrayList<ParseQuery<ParseObject>>();
			for(ParseObject participacio: list){
				ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("Esdeveniments");
				queryEvents.whereEqualTo("objectId",participacio.getString("idEsdeveniment"));
				queryList.add(queryEvents);
			}
			
			ParseQuery<ParseObject> query = ParseQuery.or(queryList);
	    	
			list = query.find();
			Esdeveniment event = null;
			for(ParseObject esdev: list){
				if(usuari.indexOfEsdeveniment(esdev.getObjectId())==-1){
					event = new Esdeveniment(esdev.getObjectId(),esdev.getString("nom"),esdev.getString("descripcio"),esdev.getString("esport"),esdev.getString("lloc"));
					event.setData(esdev.getDate("date"));
				}
				
				usuari.afegeixEsdeveniment(event);
			}
		}
    }
	
	private void actualitzaLlista(TextView noEvents){
		//Mirem si la llista esta buida
    	if(usuari.getEsdeveniments().isEmpty())
    		noEvents.setText(getResources().getString(R.string.noEvents));
    	else
    		noEvents.setText("");
    	
    	adapter.setLlistaEsdeveniments(usuari.getEsdeveniments());
		llista.setAdapter(adapter);
	}
	
	/*
     * Funcio que crear un Progress dialog mentre carregar l'usuari
     */
    private void startLoading(){
    	proDialog = new ProgressDialog(getActivity());
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