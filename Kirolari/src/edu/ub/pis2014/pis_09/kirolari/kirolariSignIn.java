package edu.ub.pis2014.pis_09.kirolari;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import edu.ub.pis2014.pis_09.registre.Registre1;
import edu.ub.pis2014.pis_09.registre.Usuari;



@SuppressLint("SimpleDateFormat")
public class kirolariSignIn extends Activity {

	private int acces = 0;
	private Kirolari kirolariControler;
	private ProgressDialog proDialog;
	private boolean estatInternet;
	
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Treiem la barra superior
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.signin_kirolari);
		kirolariControler = (Kirolari) getApplicationContext();
		estatInternet = false;
		
		//Comprovem si la sessio esta iniciada
		
		if(kirolariControler.isSessioIniciada()){
			carregarDades();
			estatInternet = true;
			Intent i = new Intent(kirolariSignIn.this, KirolariActivity.class);
	    	startActivity(i);
		}
		
		//Mirem si hem de comprovar la connexio a internet
		if(!estatInternet){
			//Comprovem si el dispositiu esta conectat a internet
			if (!kirolariControler.estaConectat()){
	    		CharSequence text = "Not Connexion";
	    		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	    		
	            AlertDialog.Builder alertDialog = new AlertDialog.Builder(kirolariSignIn.this);
				 
			    // Setting Dialog Message
			    alertDialog.setMessage(getString(R.string.text_error_internet));
			 
			    // Setting Positive Button
			    alertDialog.setPositiveButton(getString(R.string.acceptar),
			    		new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog,int which) {
					    		//No fem res, nomes informem
					        }
			    });
			    // Showing Alert Message
			    alertDialog.show();
			}
		}
		
		//ANIMACIO
		ImageView myAnimation = (ImageView)findViewById(R.id.signin_animacio);
		final AnimationDrawable myAnimationDrawable = (AnimationDrawable)myAnimation.getDrawable();
		myAnimation.post(new Runnable(){
			@Override
			public void run() {
				myAnimationDrawable.start();
			}
		});
		
		
		final EditText email = (EditText) findViewById(R.id.editText_Usuari);
		final EditText pss = (EditText) findViewById(R.id.editText_Contrasenya);
		final TextView recuperarPss = (TextView) findViewById(R.id.text_olvidarContrasenya);
		final Button button_entrar = (Button) findViewById(R.id.button_entrar);
		final Button button_registrar = (Button) findViewById(R.id.button_registrar);

		
		//ENTRAR
		button_entrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(!email.getText().toString().equals("") && !pss.getText().toString().equals("") && kirolariControler.estaConectat()){
                	startLoading();
                	Thread thread = new Thread(){
                	@Override
                		public void run() {
                	         iniciarSessio(email, pss);
                	    }
                	};
                	thread.start();

            	}
            }
        });
        
        //REGISTRAR
        button_registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //New activity
            	if(kirolariControler.estaConectat()){
	            	Intent i = new Intent(kirolariSignIn.this, Registre1.class);
	            	startActivityForResult(i, 0);
            	}
            }
        });
        
        //RECUPERAR PSS
        recuperarPss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(kirolariSignIn.this);
				  final EditText inputEmail = new EditText(kirolariSignIn.this);  
				  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				                        LinearLayout.LayoutParams.MATCH_PARENT,
				                        LinearLayout.LayoutParams.MATCH_PARENT);
				  inputEmail.setLayoutParams(lp);
				  alertDialog.setView(inputEmail);
				  
				  //Setting Title Message
				  alertDialog.setTitle(R.string.informacio);
				  
				  //setting icon message
				  alertDialog.setIcon(R.drawable.icon_alert);
				 
	 		       // Setting Dialog Message
				  alertDialog.setMessage(getString(R.string.textDialog_contrasenya));
	 		 
	 		        // Setting Positive Button
				  alertDialog.setPositiveButton(getString(R.string.acceptar),new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog,int which) {
	 		        		//Enviem un email per canviar la contrasenya
	 		        		ParseUser.requestPasswordResetInBackground(inputEmail.getText().toString(), new RequestPasswordResetCallback() {
	 		        			public void done(ParseException e) {
	 		        				if (e == null){
	 		        					// An email was successfully sent with reset instructions.
	 		        					CharSequence text = "Correu enviat!";
	 		        		        	Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
	 		        				}else{
	 		        					// Something went wrong. Look at the ParseException to see what's up.
	 		        					CharSequence text = "Error al enviar el correu";
	 		        		        	Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
	 		        				}
								}
	 		        		});
	 		        		
	 		        	}
	 		        });
	 		        
	 		       alertDialog.setNegativeButton(getString(R.string.cancelar),new DialogInterface.OnClickListener() {
	 		        	public void onClick(DialogInterface dialog,int which) {
	 		        		//Cancelar
	 		        		dialog.cancel();
	 		        	}
	 		        });
	 		 
	 		        // Showing Alert Message
	 		        alertDialog.show();

			}
		});

	}

	/*
	 * M�tode que carrega les dades si s'ha iniciat sessio previament en el mateix dispositiu
	 */
    private void carregarDades() {
    	if(kirolariControler.estaConectat()){
	    	ParseQuery<ParseObject> queryPlanning = ParseQuery.getQuery("PlanningsUsuaris");
			queryPlanning.whereEqualTo("correu", kirolariControler.getUsuari().getCorreu());
			try {
				List<ParseObject> planning = queryPlanning.find();
				for(int i=0; i<planning.size();i++){
					if(!planning.get(i).getBoolean("acabat")){
						for(int j= 0; j<planning.get(i).getInt("sessionsCompletades");j++){
							kirolariControler.getPlanningUsuari().getLlistaSessions().get(j).setCompletada(true);
						}
					}
				}
					
			}
			catch (ParseException e1) {
				e1.printStackTrace();
			}
    	}
		
		
	}

	private void iniciarSessio(EditText email, EditText pss) {
    	ParseUser currentUser = null;
    	try {
			Log.v("errorLogIn", "try");
			ParseUser.logIn(email.getText().toString(), pss.getText().toString());
			Log.v("errorLogIn", "no");
			acces=1;
			currentUser = ParseUser.getCurrentUser();
			if (currentUser != null && currentUser.getBoolean("emailVerified")) {
				// current user is valid
				kirolariControler.setUsuari(new Usuari());
				kirolariControler.getUsuari().setNom(currentUser.getString("name"));
				kirolariControler.getUsuari().setCognoms(currentUser.getString("surname"));
				kirolariControler.getUsuari().setAlies(currentUser.getString("alias"));
				kirolariControler.getUsuari().setCorreu(currentUser.getEmail());
				kirolariControler.getUsuari().setContrasenya(currentUser.getString("password"));
				kirolariControler.getUsuari().setDataNaixement(currentUser.getDate("birthdate"));
				double alcada = currentUser.getDouble("height");
				alcada = Math.rint(alcada*100)/100;
				kirolariControler.getUsuari().setAlcada(alcada);
				double pes = currentUser.getDouble("weight");
				pes = Math.rint(alcada*100)/100;
				kirolariControler.getUsuari().setPes(pes);
				kirolariControler.getUsuari().setSexe(currentUser.getString("sex"));
				kirolariControler.getUsuari().setId(currentUser.getObjectId());
				
				//Guardem a les preferencies
				SharedPreferences sharedPref = kirolariControler.getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putBoolean("sessio", true);
				kirolariControler.setSessioIniciada(true); 
				
			    editor.putString("nom",currentUser.getString("name"));
			    editor.putString("cognom",currentUser.getString("surname"));
			    editor.putString("alies",currentUser.getString("alias"));
			    editor.putString("email",currentUser.getEmail());
			    editor.putString("contrasenya",currentUser.getString("password"));
			    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
				String stringdataNaix = sdf.format(currentUser.getDate("birthdate"));
			    editor.putString("data", stringdataNaix);
			    editor.putFloat("alcada", (float) alcada);
			    editor.putFloat("pes", (float) pes);
			    editor.putString("sexe",currentUser.getString("sex"));
			    editor.putString("objectId", currentUser.getObjectId());
			    editor.putBoolean("estatkiroPudor", false);
			    editor.commit();
			}else{
				// current user not valid, ask for credentials
				acces=0;
			}
			
		}catch (ParseException e1){
			acces=0;
			Log.v("errorLogIn", "si");
			e1.printStackTrace();
		}
    	
		if(acces==1){
			ParseQuery<ParseObject> queryPlanning = ParseQuery.getQuery("PlanningsUsuaris");
			queryPlanning.whereEqualTo("correu", kirolariControler.getUsuari().getCorreu());
			try {
				List<ParseObject> planning = queryPlanning.find();
				for(int i=0; i<planning.size();i++){
					if(!planning.get(i).getBoolean("acabat")){
						// ho guardem a la classe application
						kirolariControler.setIdPlanningUsuari((planning.get(i).getInt("idPlanning")));
						kirolariControler.setDiaBasePlanning((planning.get(i).getInt("diaBase")));
						kirolariControler.setSetmanaBasePlanning((planning.get(i).getInt("setmanaBase")));
	
						//ho guardem a les preferencies
						SharedPreferences sharedPref = kirolariControler.getSharedPreferences("Preferencies", Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPref.edit();		
						editor.putInt("idPlanning", kirolariControler.getIdPlanningUsuari());
						editor.putInt("diaBase", kirolariControler.getDiaBasePlanning());
						editor.putInt("setmanaBase", kirolariControler.getSetmanaBasePlanning());
						editor.commit();
						kirolariControler.importarPlanningXML(kirolariControler.getIdPlanningUsuari());
						//Posem les que estan completades
						for(int j= 0; j<planning.get(i).getInt("sessionsCompletades");j++){
							kirolariControler.getPlanningUsuari().getLlistaSessions().get(j).setCompletada(true);
						}
						carregarDades();
					}
				}
			}
			catch (ParseException e1) {
				e1.printStackTrace();
			}
    	
			//New activity
			Log.v("acces", "si");
        	Intent i = new Intent(kirolariSignIn.this, KirolariActivity.class);

        	startActivity(i);
        	Log.i("KirolarySignIn","stopLoading()");
        	stopLoading();
		}else{
			
			Log.v("acces", "no");

			if(currentUser!=null){
				Log.e("KirolariSignIn","Verifica el email");
			}
			else{
				Log.e("KirolariSignIn","Error en les dades");
			}
			Log.i("KirolarySignIn","stopLoading()");
			stopLoading();
		}
    	
		
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Tancar l'aplicaci� en tirar enrere des de la primera pantalla
    @Override
    public void onBackPressed(){
    	//finish();
    }


    
    /*
     * Funcio que crear un Progress dialog mentre carregar l'usuari
     */
    private void startLoading(){
    	proDialog = new ProgressDialog(kirolariSignIn.this);
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





