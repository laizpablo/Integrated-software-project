package edu.ub.pis2014.pis_09.amics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.ub.pis2014.pis_09.adapter.AmicListAdapter;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;

public class Amics extends Fragment{
	private final static int ADD_FRIEND_ACTIVITY = 1;
	public static final String ARG_OPTION_NUMBER = "option_number";

	private ListView llista;
	private AmicListAdapter adapter;
	private Kirolari control;
	private ProgressDialog proDialog;
	private Activity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_amics, container, false);
		int i = getArguments().getInt("option_number");
		String titol = getResources().getStringArray(R.array.opcions)[i];
		getActivity().setTitle(titol);

		control = (Kirolari) getActivity().getApplicationContext();
		final Button buscar = (Button) rootView.findViewById(R.id.button_buscarAmics);
		final TextView noFriends = (TextView) rootView.findViewById(R.id.text_emptyFriends);
		llista = (ListView) rootView.findViewById(R.id.list_amicsMeus);
		activity = getActivity();

		//Si la llista esta buida comprovem si te amics.
		if(control.getUsuari().getAmics().isEmpty()){
			startLoading();
        	Thread thread = new Thread(){
        		@Override
        		public void run() {
        			buscarAmics(control);
                	activity.runOnUiThread(new Runnable(){
		        		public void run(){
		        			adapter = new AmicListAdapter(getActivity(),control.getUsuari().getAmics());
		            		actualitzaLlista(noFriends);
		            		stopLoading();
		        		}
		        		
		        	});
            		
        	    }
        	};
        	thread.start();
		}
		else{
			adapter = new AmicListAdapter(getActivity(),control.getUsuari().getAmics());
    		actualitzaLlista(noFriends);
		}
		
		buscar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Cridem la nova activity
				Intent i = new Intent(getActivity(),AmicsCercar.class);
				startActivityForResult(i, ADD_FRIEND_ACTIVITY);
			}
		});

		llista.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				final Amic selected = (Amic) llista.getItemAtPosition(position);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Message
				alertDialog.setMessage(getString(R.string.eliminar_amic));

				// Setting Positive Button
				alertDialog.setPositiveButton(getString(R.string.si),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						control.getUsuari().eliminaAmic(selected);

						actualitzaLlista(noFriends);

						//eliminem l'amic del parse
						eliminarAmic(selected);
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


		
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_FRIEND_ACTIVITY) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				String idAmicNou = data.getStringExtra("idAmic");

				//Pugem al PARSE el nou AMIC.
				ParseObject myParseObject = new ParseObject("Amics");
				myParseObject.put("idUsuari", control.getUsuari().getId());
				myParseObject.put("idAmic", idAmicNou);
				myParseObject.saveInBackground();

				TextView noFriends = (TextView) getActivity().findViewById(R.id.text_emptyFriends);
				actualitzaLlista(noFriends);
			}
		}
	}


	private void eliminarAmic(Amic selected) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Amics");
		query.whereEqualTo("idUsuari", control.getUsuari().getId());
		query.whereEqualTo("idAmic", selected.getId());

		try {
			ParseObject user = query.getFirst();
			user.deleteInBackground();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void buscarAmics(Kirolari controlador){
		//Busquem quantes relacions d'amics te aquest usuari.
		ParseQuery<ParseObject> queryAmics = ParseQuery.getQuery("Amics");
		queryAmics.whereEqualTo("idUsuari", controlador.getUsuari().getId());

		try {
			List<ParseObject> listAmics = queryAmics.find();

			if (!listAmics.isEmpty()){
				//Ara busquem als usuaris.
				List<ParseQuery<ParseUser>> queryList = new ArrayList<ParseQuery<ParseUser>>();

				for(ParseObject amic: listAmics){
					ParseQuery<ParseUser> queryUsuari = ParseUser.getQuery();
					queryUsuari.whereEqualTo("objectId",amic.getString("idAmic"));
					queryList.add(queryUsuari);
				}

				ParseQuery<ParseUser> query = ParseQuery.or(queryList);

				List<ParseUser> listUsuari = query.find();
				
				ArrayList<Amic> amics = controlador.getUsuari().getAmics();
				for(ParseUser el: listUsuari){
					Amic amic = new Amic(el.getObjectId(),el.getString("alias"),el.getEmail());

					int i = 0, index = amics.size();
					while(i<amics.size() && index == amics.size()){
						if(amic.getAlies().compareToIgnoreCase(amics.get(i).getAlies()) < 0)
							index = i;

						i++;
					}

					controlador.getUsuari().afegeixAmic(index,amic);
				}
			}
		}catch (Exception e1){
			e1.printStackTrace();
		}
	}

	private void actualitzaLlista(TextView noFriends){
		//Mirem si la llista esta buida
		if(control.getUsuari().getAmics().isEmpty())
			noFriends.setText(getResources().getString(R.string.noFriends));
		else
			noFriends.setText("");

		adapter.setLlistaAmics(control.getUsuari().getAmics());
		llista.setAdapter(adapter);
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