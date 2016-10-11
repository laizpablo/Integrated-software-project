package edu.ub.pis2014.pis_09.amics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.ub.pis2014.pis_09.adapter.AmicListAdapter;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.registre.Usuari;

public class AmicsCercar extends Activity{
	private ListView llistaAmics;
	private ArrayList<Amic> amicsItems;
	private AmicListAdapter adapter;
	private Usuari usuari;
	private ProgressDialog proDialog;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amics_cercar);
        String titol = getResources().getString(R.string.afegirAmic);
        setTitle(titol);

        
        usuari = ((Kirolari) getApplicationContext()).getUsuari();
        llistaAmics = (ListView) findViewById(R.id.list_amicsTrobats);
        
        llistaAmics.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

				final Amic selected = amicsItems.get(position);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(AmicsCercar.this);
				 
		        // Setting Dialog Message
		        alertDialog.setMessage(getString(R.string.afegir_amic));
		 
		        // Setting Positive "Yes" Button
		        alertDialog.setPositiveButton(getString(R.string.si),
		        		new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog,int which) {
				            	afegeixAmic(selected);
				            	
				            	Intent i = new Intent();
				            	i.putExtra("idAmic", selected.getId());
				            	
				            	// ends this Activity
				            	setResult(RESULT_OK, i);
				            	finish();
				            }
		        		});
		 
		        // Setting Negative "NO" Button
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
		
        final EditText textAlies = (EditText) findViewById(R.id.editText_aliesAmic);
		final TextView userNotFound = (TextView) findViewById(R.id.text_userNotFound);
        final ImageButton buscar = (ImageButton) findViewById(R.id.button_buscarAmic);
        
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final String text = textAlies.getText().toString();
            	if(!text.isEmpty()){
            		startLoading();
                	Thread thread = new Thread(){
                		@Override
                		public void run() {
                			amicsItems = new ArrayList<Amic>();
                    		buscar("alias","email",text);
                    		
                    		AmicsCercar.this.runOnUiThread(new Runnable(){
        		        		public void run(){
        		        			if(!amicsItems.isEmpty())
        		            			userNotFound.setText("");
        		            		else
        		            			userNotFound.setText(getResources().getString(R.string.usersNotFound));
        		            		
        		            		adapter = new AmicListAdapter(getApplicationContext(),amicsItems);
        		            		llistaAmics.setAdapter(adapter);
        		            		stopLoading();
        		        		}
        		        		
        		        	});
                        	//buscats = true;
                	    }
                	};
                	thread.start();

            	}
            }
        });
	}
	
	private void buscar(String camp1,String camp2,String text){
		ParseQuery<ParseUser> query1 = ParseUser.getQuery();
    	query1.whereContains(camp1,text);
    	ParseQuery<ParseUser> query2 = ParseUser.getQuery();
    	query2.whereContains(camp2,text);
    	
    	List<ParseQuery<ParseUser>> queryList = new ArrayList<ParseQuery<ParseUser>>();
    	queryList.add(query1);
    	queryList.add(query2);
    	ParseQuery<ParseUser> query = ParseQuery.or(queryList);
    	try {
			List<ParseUser> list = query.find();
			if (!list.isEmpty()){
				for(ParseUser el: list){
					String idAmicNou = el.getObjectId();

					boolean trobat = false;
					//Busquem si ja tenim aquest amic a la llista.
					for(Amic amic: usuari.getAmics()){
						if(amic.getId().equals(idAmicNou))
							trobat = true;
					}
					
					//Ens assegurem de que no mostrem el propi usuari si el troba.
					if(!trobat && !idAmicNou.equals(usuari.getId())){
						Amic amicNou = new Amic(idAmicNou,el.getString("alias"),el.getEmail());
						amicsItems.add(amicNou);
					}
		        }
    	    }
		}catch (ParseException e) {
			e.printStackTrace();
		}
    }
	

	/**
	 * Mètode que afegeix un amic per tal que estiguin per ordre alfabètic segons l'àlies.
	 */
	private void afegeixAmic(Amic selected) {
		ArrayList<Amic> amics = usuari.getAmics();
		int i = 0, index = amics.size();
		while(i<amics.size() && index == amics.size()){
			if(selected.getAlies().compareToIgnoreCase(amics.get(i).getAlies()) < 0)
				index = i;
			
			i++;
		}
		
		usuari.afegeixAmic(index,selected);
	}
	
	/*
     * Funcio que mostra un ProgressDialog
     */
    private void startLoading(){
    	proDialog = new ProgressDialog(AmicsCercar.this);
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