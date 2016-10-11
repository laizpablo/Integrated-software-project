package edu.ub.pis2014.pis_09.kirolari;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import edu.ub.pis2014.pis_09.planning.Planning;
import edu.ub.pis2014.pis_09.planning.Sessions;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	Kirolari control;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = (Kirolari) getActivity().getApplicationContext();
        addPreferencesFromResource(R.xml.preferences);
        completarDades();
        
        
        Preference logOut = (Preference)findPreference("logout");
	    logOut.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				tancarSessio();     	
				return false;
			}
		});
	    //Reiniciem el planning
	    Preference reiniciarPlanning = (Preference)findPreference("reset");
	    reiniciarPlanning.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if(control.getIdPlanningUsuari()!=-1)
					eliminarPlanning();
				return false;
			}
		});
	    
	    //Activem o desactivem les notificacions
	    final Preference notificacions = (Preference)findPreference("notificacions");
	    notificacions.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				notificacions();
				return false;
			}
		});
	    //Activem o desactivem les notificacions
	    final Preference sessions = (Preference)findPreference("sessions");
	    sessions.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				veureSessions();
				return true;
			}
		});
	    
	    final Preference sobreNosalters = (Preference)findPreference("help");
	    sobreNosalters.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				sobreNosaltres();
				return true;
			}
		});
    }
    
    
    private void sobreNosaltres() {
        // alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		
        alertDialog.setMessage(R.string.sobre_nosaltres);
	 
        // Setting Positive Button
        alertDialog.setPositiveButton(getString(R.string.acceptar),
    		new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
		            	dialog.cancel();
    			}
        	}
        );
        // Showing Alert Message
        alertDialog.show();
	}

	/*
     * Funcio que introduiex les dades a l'usuari
     */
    private void completarDades() {
		final EditTextPreference nom = (EditTextPreference) findPreference("nom");
		nom.setSummary(control.getUsuari().getNom());
	    nom.setText((String) nom.getSummary());
	    final EditTextPreference cognom = (EditTextPreference) findPreference("cognom");
	    cognom.setSummary(control.getUsuari().getCognoms());
	    cognom.setText((String) cognom.getSummary());
	    final EditTextPreference alies = (EditTextPreference) findPreference("alies");
	    alies.setSummary(control.getUsuari().getAlies());
	    alies.setText((String) alies.getSummary());
	    final EditTextPreference email = (EditTextPreference) findPreference("email");
	    email.setSummary(control.getUsuari().getCorreu());
	    email.setText((String) email.getSummary());
	    final EditTextPreference alcada = (EditTextPreference) findPreference("alcada");
	    alcada.setSummary(Double.toString(control.getUsuari().getAlcada()));
	    alcada.setText((String) alcada.getSummary());
	    final EditTextPreference pes = (EditTextPreference) findPreference("pes");
	    pes.setSummary(Double.toString(control.getUsuari().getPes()));
	    pes.setText((String) pes.getSummary());
	    final ListPreference sexe = (ListPreference) findPreference("sexe");
	    sexe.setSummary(control.getUsuari().getSexe());
	}

	/*
     * Funcio que mostra les sessions del planning, si tens algun iniciat
     */
    protected void veureSessions() {
		if(control.getIdPlanningUsuari()!=-1){
			Intent i = new Intent(getActivity(),Sessions.class);
	    	startActivity(i);
		}
		else{
			Toast.makeText(getActivity(), R.string.no_planning, Toast.LENGTH_SHORT).show();	
		}
		
	}

	/*
     * Funcio que activa/desactiva  les notificacions
     */
    protected void notificacions() {
		CharSequence text;
		if(control.isNotificacions()){
			 text = "Notificacions desactivades.";
		}else{
			 text = "Notificacions activades.";
		}
		
		Toast.makeText(getActivity().getBaseContext(), text, Toast.LENGTH_SHORT).show();
    	control.setNotificacions(!control.isNotificacions());
	}

	/*
     * Funcio que elimina el planning
     */
    protected void eliminarPlanning() {
		CharSequence text = "Planning Eliminat";
    	Toast.makeText(getActivity().getBaseContext(), text, Toast.LENGTH_SHORT).show();

    	control.setPlanningUsuari(new Planning());
    	control.setIdPlanningUsuari(-1);
    	control.setDiaBasePlanning(-1);
    	control.setSetmanaBasePlanning(-1);
  		//Guardem la informacio necessaria al sharedPreference
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("idPlanning", -1);
        editor.putInt("diaBase", -1);
        editor.putInt("setmanaBase",-1);
        editor.commit();
        control.acabarPlanningAntic();
		
	}
    
	/*
     * Funcio que tanca sessio
     */
	protected void tancarSessio() {
		CharSequence text = "Sessio Tancada";
    	Toast.makeText(getActivity().getBaseContext(), text, Toast.LENGTH_SHORT).show();
    	
    	control.logOut();
    	control.setSessioIniciada(false);
    	
    	Intent i = new Intent(getActivity(), kirolariSignIn.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
		
	}

	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		SharedPreferences sharedPref = control.getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		switch (key) {
			case "nom":
				editor.putString(key,sharedPreferences.getString(key,""));
				control.getUsuari().setNom(sharedPreferences.getString(key,""));
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				break;
				
			case "cognom":
				editor.putString(key,sharedPreferences.getString(key,""));
				control.getUsuari().setCognoms(sharedPreferences.getString(key,""));
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				
				break;
				
			case "alies":
				editor.putString(key,sharedPreferences.getString(key,""));
				control.getUsuari().setAlies(sharedPreferences.getString(key,""));
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				break;
				
			case "alcada":
				double alcada = Double.parseDouble(sharedPreferences.getString(key,""));
				double result = (int)(alcada*100)/100d;
				result = Math.rint(result*100)/100;
				
				editor.putFloat(key,(float) result);
				control.getUsuari().setAlcada(result);
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				break;
				
			case "pes":
				double pes = Double.parseDouble(sharedPreferences.getString(key,""));
				result = (int)(pes*100)/100d;
				result = Math.rint(result*100)/100;
				
				editor.putFloat(key,(float) result);
				control.getUsuari().setPes(result);
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				break;
				
			case "email":
				editor.putString(key,sharedPreferences.getString(key,""));
				control.getUsuari().setCorreu(sharedPreferences.getString(key,""));
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				break;
				
			case "sexe":
				editor.putString(key,sharedPreferences.getString(key,""));
				control.getUsuari().setSexe(sharedPreferences.getString(key,""));
				findPreference(key).setSummary(sharedPreferences.getString(key,""));
				break;
	
			default:
				break;
		}
	    editor.commit();
		
	}
}