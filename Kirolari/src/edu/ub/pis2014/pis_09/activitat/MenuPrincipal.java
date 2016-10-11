package edu.ub.pis2014.pis_09.activitat;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.planning.NouPlanning;
import edu.ub.pis2014.pis_09.planning.Sessio;

 /**
   * Fragment that appears in the "content_frame", shows a planet
   */
public class MenuPrincipal extends Fragment {
	/*Definim les variables globals*/
    public static final String ARG_OPTION_NUMBER = "option_number";
    private View rootView;
    private Kirolari kirolariControler ;
    
    /*
     * Constructir de la classe MenuPrincipal que extend de Fragment
     */
    public MenuPrincipal() {
        	
            // Empty constructor required for fragment subclasses
    }

    /*
     * Classe onCreateView
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);
        int i = getArguments().getInt(ARG_OPTION_NUMBER);
        String titol = getResources().getStringArray(R.array.opcions)[i];
        kirolariControler = (Kirolari) getActivity().getApplicationContext();
        existeixPlanning();
        
        Log.i("Amiversari true", String.valueOf(kirolariControler.isAniversari()));
        
        if(kirolariControler.isAniversari()){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			
        	//Setting Title Message
			alertDialog.setTitle(R.string.notificacio_aniversariTitol);
			
			ImageView image = new ImageView(getActivity());
	        image.setImageResource(R.drawable.ic_birthday);
	        
			alertDialog.setView(image);
		    // Setting Dialog Message
		    alertDialog.setMessage(getString(R.string.notificacio_aniversari));
		 	//Setting Positive Button
		    alertDialog.setPositiveButton(getString(R.string.acceptar),
		    		new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog,int which) {
		    					dialog.cancel();
		    					kirolariControler.setAniversari(false);
				            }
		        		});
		        alertDialog.show();
		}
        
        /*Posem els escoltadors de cada boto*/
            
        // Continuar Planning
        final Button boto_cp = (Button) rootView.findViewById(R.id.button_ContinuarPlanning);
        boto_cp.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// Perform action on click
        		botoContinuarPlanning();
        	}
        });
            
        //Activitat Lliure
        final Button boto_AL = (Button) rootView.findViewById(R.id.button_ActivitatLliure);
        boto_AL.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		// Perform action on click
                botoActivitatLliure();
        	}
        });
            
        //Introduir Entrenament
        final Button boto_IE = (Button) rootView.findViewById(R.id.button_IntroduirEntrenament);
        boto_IE.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
        		botoIntroduirEntrenament();
                	
        	}
        });
            
        getActivity().setTitle(titol);
            
        
        
        return rootView;
    }

    /*
     * Funcio botoIntroduirEntrenament, t'obre un nou fragment.
     * En aquest fragment podrem guardar un entrenament que haguem fet sense el telefon
     */
    private void botoIntroduirEntrenament() {
    	//Iniciem el nou Fragment
    	Fragment fragment_introduirEntrenament = new IntroduirEntrenament();
    	FragmentManager fragmentManager = getFragmentManager();
    	fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_introduirEntrenament).addToBackStack(null).commit();
			
	}

    /*
     * Funcio botoActivitatLliure, obra una nova activity.
     * En aquesta acitivy fem una activitat i la registrem mentre es fa.
     */
    private void botoActivitatLliure() {
    	Fragment fragment_activitatLliure = new ActivitatLliure();
    	FragmentManager fragmentManager = getFragmentManager();
    	fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_activitatLliure).addToBackStack(null).commit();
    	
	}
    
    /*
     * Funcio botoContinuarPlanning, et permet continuar el teu planning o en el seu defecte
     * iniciar un nou planning si no tens cap comen�at. 
     */
    private void botoContinuarPlanning() {
    	
    	// Cas si tenim un planning iniciat
    	if (kirolariControler.getIdPlanningUsuari() !=-1){
    		//Creeem una nova activity que ens registra l'activitat
    		afegirEntrenamentCalendari("Kirolari", kirolariControler.getPlanningUsuari().getObjectius());
    		kirolariControler.buscarSessio().setCompletada(true);
    		
    		
    		// modifiquem la informacio del objecte planningUsuari
    		kirolariControler.incrementarSessions();
    		
    		existeixPlanning();
    		int duration = Toast.LENGTH_SHORT;    		
    		Toast toast = Toast.makeText(getActivity(), kirolariControler.diaSetmana(), duration);
    		toast.show();
    		
    		Intent intent = new Intent(getActivity(), RealitzarActivitat.class);
    		startActivity(intent);
    		
    	}
        	//Cas no tenim planning iniciat
    	else{
    		// Iniciem el proces per crear un nou planning
    		Fragment fragment_nou_planning = new NouPlanning();
    		Bundle args = new Bundle();
    		args.putInt(NouPlanning.ARG_OPTION_NUMBER, 3);
    		fragment_nou_planning.setArguments(args);
    		
    		FragmentManager fragmentManager = getFragmentManager();
    		fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_nou_planning).addToBackStack(null).commit();
    	}
			
    }
		
    /*
     * Funcio que gestiona els diferents moments en el qual es pot trobar 
     * l'usuari respecte planning i mostra diferents missatges segons l'estat.
     */
    private void existeixPlanning() {
    	
    	/*MOMENTANIII*/
    	//eliminarEntrenosCalendari();
    	
    	//Definim variables locals
    	final TextView text_Planning = (TextView) rootView.findViewById(R.id.TextView_MenuPrincipal);
    	final TextView text_NomPlanning = (TextView) rootView.findViewById(R.id.TextView_titol_planning);
    	Sessio sessio = kirolariControler.buscarSessio();

		// Si no tenim cap planning creat
    	if (kirolariControler.getIdPlanningUsuari() == -1){				
    		text_Planning.setText(R.string.MenuPrincipal_noPlanning_text);
    		final Button boto_cp = (Button) rootView.findViewById(R.id.button_ContinuarPlanning);
    		String[] navMenuTitles = getResources().getStringArray(R.array.opcions);
    		boto_cp.setText(navMenuTitles[3]);
    	}
    	
    	//Si te un dia de decans
    	else if(sessio == null){
    		text_NomPlanning.setText(kirolariControler.getPlanningUsuari().getNomPlanning());
    		text_Planning.setText(getText(R.string.MenuPrincipal_descansPlanning_text));
    		final Button boto_cp = (Button) rootView.findViewById(R.id.button_ContinuarPlanning);
    		boto_cp.setEnabled(false);		
    	}
    	
    	//Si te una activitat i no l'ha fet
    	else if (sessio!= null && !sessio.isCompletada()){
    		text_NomPlanning.setText(kirolariControler.getPlanningUsuari().getNomPlanning());
    		text_Planning.setText(getString(R.string.MenuPrincipal_Planning_text) +"\n"+kirolariControler.buscarSessio().getActivitat());
    		final Button boto_cp = (Button) rootView.findViewById(R.id.button_ContinuarPlanning);
    		boto_cp.setEnabled(true);
    	}
    	
    	//Si te una activitat i l'ha fet
    	else if(sessio!= null && sessio.isCompletada()){
    		text_NomPlanning.setText(kirolariControler.getPlanningUsuari().getNomPlanning());
    		text_Planning.setText(getText(R.string.MenuPrincipal_fetPlanning_text));
    		final Button boto_cp = (Button) rootView.findViewById(R.id.button_ContinuarPlanning);
    		boto_cp.setEnabled(false);
    	}
    }
    
    /*
     * Funcio eliminarEntrenosCalendari
     */
    @SuppressWarnings("unused")
	private void eliminarEntrenosCalendari() {
    	for(int i=0;i < kirolariControler.getidEvents().size(); i++){
        	Uri deleteUri = null;
        	deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, kirolariControler.getidEvents().get(i));
        	getActivity().getContentResolver().delete(deleteUri, null, null);
    	}
	}

	/*
	 * Funcio afegirEntrenamentCalendari, afeguim al calendari el nou entrenament
	 */
    private void afegirEntrenamentCalendari(String title, String description) {				
    	Sessio sessio = kirolariControler.buscarSessio();
    	final Calendar c = Calendar.getInstance();

    	if (c.get(Calendar.WEEK_OF_YEAR) - kirolariControler.getSetmanaBasePlanning() + 1 
    			<= kirolariControler.getPlanningUsuari().getDuracioSetmana()){
    		int dia = sessio.getDia();
        	int setmana = sessio.getSetmana();
        	
        	//Busquem la proxima sessio de l'entreno
        	sessio = null;
        	while(sessio == null && 
        			c.get(Calendar.WEEK_OF_YEAR) - kirolariControler.getSetmanaBasePlanning() + 1 
        			<= kirolariControler.getPlanningUsuari().getDuracioSetmana() ){
        		if(dia==7){
        			dia = 1;
        			setmana ++;
        		}
        		else{
        			dia++;
        		}
        		sessio = kirolariControler.buscarSessioPerParametre(dia, setmana);
        		
        	}
        	
        	int incrementDies = (sessio.getDia() - kirolariControler.buscarSessio().getDia())
        			+ (sessio.getSetmana()-kirolariControler.buscarSessio().getSetmana())*7;
        	//Definim la data
        	Calendar beginTime = Calendar.getInstance();
        	beginTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) + incrementDies);
        	beginTime.set(beginTime.get(Calendar.YEAR), beginTime.get(Calendar.MONTH), beginTime.get(Calendar.DAY_OF_MONTH), 8, 00);
        	Calendar endTime = Calendar.getInstance();
        	endTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) + incrementDies);
        	        	
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
        	   	
        	kirolariControler.afegirIdCalendari(Long.parseLong(uri.getLastPathSegment()));
        	if(kirolariControler.isNotificacions()){
        		kirolariControler.notificacioEntrenament(beginTime);
        		kirolariControler.notificacioFutur(beginTime);
        	}
        	
    	}
    	else{
    		kirolariControler.acabarPlanningAntic();
    	}
	}


}