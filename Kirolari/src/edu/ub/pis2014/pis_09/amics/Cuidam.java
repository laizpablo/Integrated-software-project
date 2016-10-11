
package edu.ub.pis2014.pis_09.amics;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import edu.ub.pis2014.pis_09.kirolari.R;

   /**
    * Fragment that appears in the "content_frame", shows a planet
    */
    public class Cuidam extends Fragment {
        public static final String ARG_OPTION_NUMBER = "option_number";
    	private ImageView animacioDutxa;
    	private ImageView animacioJugar;
    	private ImageView animacioDormir;
    	private ImageView animacioVestir;
        private ImageButton buttonDutxa;
        private ImageButton buttonJugar;
        private ImageButton buttonDormir;
        private ImageButton buttonVestir;
        private AnimationDrawable animacioDutxaDrawable;
        private AnimationDrawable animacioJocDrawable;
        private AnimationDrawable animacioDormirDrawable;

        private boolean isDutxaRunning=false;
        private boolean isJocRunning=false;
        private boolean isDormirRunning=false;
        private boolean isVestirRunning=false;
        


        public Cuidam() {
            // Emstaticpty constructor required for fragment subclasses
        }

        public void stopAll(){
        	if(isDutxaRunning){
        		Log.i("Cuidem","StopAll isDutxaRunning");
        		animacioDutxaDrawable.stop(); 
        		animacioDutxa.setBackgroundResource(R.anim.animacio_buida);
        		animacioDutxaDrawable = (AnimationDrawable)animacioDutxa.getBackground();
        		isDutxaRunning=false;
        	}
        	
        	if(isJocRunning){
        		Log.i("Cuidem","StopAll isJocRunning");
        		animacioJocDrawable.stop(); 		
        		animacioJugar.setBackgroundResource(R.anim.animacio_buida);
        		animacioJocDrawable = (AnimationDrawable)animacioJugar.getBackground();
        		isJocRunning=false;
        	}
        	
        	if(isDormirRunning){
        		Log.i("Cuidem","StopAll isDormirRunning");
        		animacioDormirDrawable.stop();
        		animacioDormir.setBackgroundResource(R.anim.animacio_buida);
        		animacioDormirDrawable = (AnimationDrawable)animacioDormir.getBackground();
        		isDormirRunning=false; 
        	}
        	if(isVestirRunning){
        		animacioVestir.setVisibility(View.INVISIBLE);
        	}
        	
        }
        
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_cuidam, container, false);
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int i = getArguments().getInt(ARG_OPTION_NUMBER);
            String titol = getResources().getStringArray(R.array.opcions)[i];
            getActivity().setTitle(titol);
         
            
            
            //DUTXA
            animacioDutxa = (ImageView)rootView.findViewById(R.id.Cuidam_ImatgeDutxa);
            animacioDutxa.setVisibility(View.INVISIBLE);
            buttonDutxa = (ImageButton)rootView.findViewById(R.id.imageButton_cuidamDutxa);
            buttonDutxa.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					stopAll();
					animacioDutxa.setVisibility(View.VISIBLE);				
					animacioDutxa.setBackgroundResource(R.anim.animacio_cuidam_dutxa);
					animacioDutxaDrawable = (AnimationDrawable)animacioDutxa.getBackground();
					animacioDutxaDrawable.start(); 
					isDutxaRunning=true;
				}
				
			});

            //JUGAR
            animacioJugar = (ImageView)rootView.findViewById(R.id.Cuidam_ImatgeJugar);
            animacioJugar.setVisibility(View.INVISIBLE);
            buttonJugar = (ImageButton)rootView.findViewById(R.id.imageButton_cuidamJugar);
            buttonJugar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					stopAll();
					animacioJugar.setVisibility(View.VISIBLE);
					animacioJugar.setBackgroundResource(R.anim.animacio_cuidam_jugar);
					animacioJocDrawable = (AnimationDrawable)animacioJugar.getBackground();
					animacioJocDrawable.start();
					isJocRunning=true;
				}
				
			});
          
            
            //DORMIR
            animacioDormir = (ImageView)rootView.findViewById(R.id.Cuidam_ImatgeDormir);
            animacioDormir.setVisibility(View.INVISIBLE);
            buttonDormir = (ImageButton)rootView.findViewById(R.id.imageButton_cuidamDormir);
            buttonDormir.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					stopAll();
					animacioDormir.setVisibility(View.VISIBLE);
					animacioDormir.setBackgroundResource(R.anim.animacio_cuidam_dormir);
					animacioDormirDrawable = (AnimationDrawable)animacioDormir.getBackground();
					animacioDormirDrawable.start();
					isDormirRunning=true;
				}
				
			});
            
            //VESTIR
            animacioVestir = (ImageView)rootView.findViewById(R.id.Cuidam_ImatgeVestir);
            animacioVestir.setVisibility(View.INVISIBLE);
            buttonVestir = (ImageButton)rootView.findViewById(R.id.imageButton_cuidamVestir);
            buttonVestir.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					stopAll();
					Drawable normal = getResources().getDrawable(R.drawable.kirolari_cuidam);
            		animacioVestir.setImageDrawable(normal);
					animacioVestir.setVisibility(View.VISIBLE);
					final CharSequence[] items = {"Carrer", "Aerobic 80's", "Fitness", "Piscina", "Tennis"};
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		            builder.setTitle("Selecciona l'estil");
		            builder.setItems(items, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int item) {
		                     // Do something with the selection
		                	if(item==0){
		                		//Carrer
		                		Drawable carrer = getResources().getDrawable(R.drawable.kirolari_vestir_carrer);
		                		animacioVestir.setImageDrawable(carrer);
		                	}
		                	else if(item==1){
		                		//Aerobic
		                		Drawable carrer = getResources().getDrawable(R.drawable.kirolari_vestir_disco);
		                		animacioVestir.setImageDrawable(carrer);
		                	}
		                	else if(item==2){
		                		//Musculitos
		                		Drawable carrer = getResources().getDrawable(R.drawable.kirolari_vestir_gymgorra);
		                		animacioVestir.setImageDrawable(carrer);
		                	}
		                	else if(item==3){
		                		//Piscina
		                		Drawable carrer = getResources().getDrawable(R.drawable.kirolari_vestir_piscina);
		                		animacioVestir.setImageDrawable(carrer);
		                	}
		                	else{
		                		//Tennis
		                		Drawable carrer = getResources().getDrawable(R.drawable.kirolari_vestir_tennis);
		                		animacioVestir.setImageDrawable(carrer);
		                	}
		                }
		            });
		            AlertDialog alert = builder.create();
		            alert.show();
		            
		            isVestirRunning=true;
				}
				
			});


            return rootView;
        }
		
		
		
		   
		
	
    }