package edu.ub.pis2014.pis_09.esdeveniments;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.ub.pis2014.pis_09.adapter.EsdevenimentListAdapter;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.registre.Usuari;

public class EsdevenimentsCercar extends Fragment {
	public final static int ACTIVITY_CREATE=1;
	
	private ListView llista;
	private ArrayList<Esdeveniment> events;
	private EsdevenimentListAdapter adapter;
	private Usuari usuari;
	private ProgressDialog proDialog;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_cercar_event,container,false);
		usuari = ((Kirolari) getActivity().getApplicationContext()).getUsuari();
		
		final EditText textNom = (EditText) rootView.findViewById(R.id.editText_nomEvent_cercarEvent);
		final Spinner zona = (Spinner) rootView.findViewById(R.id.spinner_barris_cercarEvent);
		final Spinner esport = (Spinner) rootView.findViewById(R.id.spinner_Esports_crearEvent);
		final TextView eventNotFound = (TextView) rootView.findViewById(R.id.text_eventNotFound);
        final Button buscar = (Button) rootView.findViewById(R.id.button_cercar);
        final CheckBox checkZona = (CheckBox) rootView.findViewById(R.id.checkBox_zona_cercarEvent);
        final CheckBox checkEsport = (CheckBox) rootView.findViewById(R.id.checkBox_esport_cercarEvent);
        
        
        llista = (ListView) rootView.findViewById(R.id.list_events);
		
        llista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,final int position, long id) {
				final Esdeveniment selected = events.get(position);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		 
		        // Setting Dialog Message
		        alertDialog.setMessage(getString(R.string.afegir_esdev));
		 
		        // Setting Positive Button
		        alertDialog.setPositiveButton(getString(R.string.si),
		        		new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog,int which) {
				            	usuari.afegeixEsdeveniment(selected);
				            	
				            	//afegim esdeveniment al parse
				            	ParseObject myParseObject = new ParseObject("Participants");
				            	myParseObject.put("idUsuari", usuari.getId());
				        		myParseObject.put("idEsdeveniment", selected.getId());
				        		myParseObject.saveEventually();
				            }
		        		});
		 
		        // Setting Negative Button
		        alertDialog.setNegativeButton(getString(R.string.no),
		        		new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	dialog.cancel();
				            }
				        });
		 
		        // Showing Alert Message
		        alertDialog.show();
			}
		});
        
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            	
            	startLoading();
            	Thread thread = new Thread(){
            		@Override
            		public void run() {
            			String text = textNom.getText().toString();
                    	if(!text.isEmpty()){
                    		events = new ArrayList<Esdeveniment>();
                    		
                    		String zone = null,sport = null;
                    		if(checkZona.isChecked())
                    			zone = zona.getSelectedItem().toString();
                    		if(checkEsport.isChecked())
                    			sport = esport.getSelectedItem().toString();
                    		buscar(text,zone,sport);
                    		
                    		
                    	
                    		getActivity().runOnUiThread(new Runnable(){
        		        		public void run(){
        		        			if(!events.isEmpty())
                            			eventNotFound.setText("");
                            		else
                            			eventNotFound.setText(getResources().getString(R.string.eventsNotFound));
        		        			adapter = new EsdevenimentListAdapter(getActivity());
                        			adapter.setLlistaEsdeveniments(events);
                        			llista.setAdapter(adapter);
        		            		textNom.setText("");
        		        		}
        		        		
        		        	});
                    		
                    	}
                    	stopLoading();
            	    }
            	};
            	thread.start();
            	
            }
        });
		
		return rootView;
		
		
	}
	
	private void buscar(String nom,String zona,String esport){
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Esdeveniments");
    	query.whereContains("nom",nom);
    	if(esport!=null)
    		query.whereEqualTo("esport", esport);
    	if(zona!=null)
    		query.whereEqualTo("lloc", zona);
    	
    	try {
			List<ParseObject> list = query.find();
			if (!list.isEmpty()){
				for(ParseObject es: list){
					if(usuari.indexOfEsdeveniment(es.getObjectId())==-1){
						Esdeveniment event = new Esdeveniment(es.getObjectId(),es.getString("nom"),es.getString("descripcio"),es.getString("esport"),es.getString("lloc"));
						event.setData(es.getDate("date"));
						
						events.add(event);
					}
		        }
    	    }
		} catch (Exception e) {
			CharSequence text = getString(R.string.error_buscar_event);
        	Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
    }
	/*
     * Funcio que mostra un ProgressDialog
     */
    private void startLoading(){
    	proDialog = new ProgressDialog(getActivity());
        proDialog.setMessage(getString(R.string.buscant));
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
