package edu.ub.pis2014.pis_09.activitat;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

import edu.ub.pis2014.pis_09.amics.Cuidam;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;

   /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public class IntroduirEntrenament extends Fragment {
    	
    	private String activitat;
    	ParseObject activitatParse= new ParseObject("Activitat"); // Class Name
    	private Kirolari kirolariControler;


        public IntroduirEntrenament() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_introduir_entrenament, container, false);
            String titol = "Introduir Entrenament";
            //final Context context = container.getContext();
            
    		//agafem la application per poder accedir a l'usuari que estem
            kirolariControler = (Kirolari) getActivity().getApplicationContext();

            
            final Spinner spinnerActivitat = (Spinner) rootView.findViewById(R.id.spinner_Esports);
            //Agafem del spinner l'activitat corresponent
        	spinnerActivitat.setOnItemSelectedListener(new OnItemSelectedListener() {
        		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        			activitat = spinnerActivitat.getSelectedItem().toString();
                }
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
			
				}
			});
        	
    		final EditText distancia = (EditText) rootView.findViewById(R.id.editText_Distancia);
    		final EditText duracio = (EditText) rootView.findViewById(R.id.editText_Dades_Duracio);
    		final EditText calories = (EditText) rootView.findViewById(R.id.editText_Calories);
    		
            
            //boto guardar entrenament
            final Button button_guardar = (Button) rootView.findViewById(R.id.button_guardarEntrenament);
            button_guardar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                		
                	activitatParse.put("idUsuari", kirolariControler.getUsuari().getId());
                	activitatParse.put("Activitat", activitat);
                	//distancia
                	if(distancia.getText().toString().equals(""))
                		activitatParse.put("Distancia", 0);
                	else
                		activitatParse.put("Distancia", Double.valueOf(distancia.getText().toString()));
                	//duracio
                	if(duracio.getText().toString().equals(""))
                    	activitatParse.put("Duracio", 0);
                	else
                    	activitatParse.put("Duracio", Double.valueOf(duracio.getText().toString()));
                	//calories
                	if(calories.getText().toString().equals("")){
                		if (!duracio.getText().toString().equals("")){
                			double cal;
                    		cal=calculCalories(Integer.valueOf(duracio.getText().toString()),spinnerActivitat.getSelectedItemPosition());
                        	activitatParse.put("Calories",cal);                			
                		}else{
                			activitatParse.put("Calories", 0);
                		}
                		
                	}else
                    	activitatParse.put("Calories", Double.valueOf(calories.getText().toString()));
                	
                	if(distancia.getText().toString().equals("") && duracio.getText().toString().equals("") && calories.getText().toString().equals("")){

                		Toast.makeText(getActivity().getApplicationContext(), R.string.dades_introduirEntrenament, Toast.LENGTH_LONG);

                	}
                	else{
	                	activitatParse.saveInBackground();
	                	
	                	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	
	    				// Setting Dialog Message
	    				alertDialog.setMessage(getString(R.string.kiroHauraDeDutxarse_text));
	    				
	    				ImageView image = new ImageView(getActivity());
	    		        image.setImageResource(R.drawable.kirolari_peste);
	    				alertDialog.setView(image);
	
	    				// Setting Positive Button
	    				alertDialog.setPositiveButton(getString(R.string.si),new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog,int which) {
	
	    						Bundle args;
	    				    	FragmentManager fragmentManager;
	    				    	Fragment fragment_cuidam = new Cuidam();
	    			   			args = new Bundle();
	    			   		    args.putInt(Cuidam.ARG_OPTION_NUMBER, 4);
	    			   		    fragment_cuidam.setArguments(args);
	    			   		    
	    			   		    fragmentManager = getActivity().getSupportFragmentManager();
	    			   		    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_cuidam).addToBackStack(null).commit();
	    					}
	    				});
	    			
	    				// Setting Negative Button
	    				alertDialog.setNegativeButton(getString(R.string.no),
	    						new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog, int which) {
	    						getActivity().getSupportFragmentManager().popBackStack();
	    					}
	    				});
	
	    				// Showing Alert Message
	    				alertDialog.show();
                	
                	}
                }
            });
            
            
            getActivity().setTitle(titol);
            
            return rootView;
        }
        
    	/**
    	 * Calcula les calories consumides (aproximadament) durant la realització de l'activitat
    	 * font: http://www.exercise4weightloss.com/calories-burned-during-exercise.html
    	 * @param duration
    	 * @param opcio
    	 * @return double calories
    	 */
    	private double calculCalories(int duration,int opcio){
    		double calories;
    		calories=(duration*calculMET(opcio)*3.5*(kirolariControler.getUsuari().getPes()))/200;		
    		return calories;
    	}
    	
    	/**
    	 * Calcula el Metabolic Equivalent of Task
    	 * @return double met 
    	 */
    	private double calculMET(int opcio){
    		double met;
    		///Calories per 30' d'activitat (aprox.){correr-325,bici-150,spinning-400,natacio-250,aerobic-180,fitness-110,Caminata-200,Ioga-150}
    		@SuppressWarnings("serial")
    		ArrayList<Integer>caloriesOpcio=new ArrayList<Integer>(){{
    			add(325);add(150);add(400);add(250);add(180);add(110);add(200);add(150);
    		}};
    		met=(caloriesOpcio.get(opcio)*200)/(30*3.5*kirolariControler.getUsuari().getPes());
    		return met;
    	}
}