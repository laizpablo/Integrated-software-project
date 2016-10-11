package edu.ub.pis2014.pis_09.registre;


import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.ub.pis2014.pis_09.kirolari.R;

public class Registre1 extends Activity {
	private int campEmailBuit=1;
	private int campPssBuit=1;
	private int campConfirmPssBuit=1;
	private int psswdEquals=1;
	private int usuariRepetit=1;
	Usuari user= new Usuari();
	ParseUser myParseUser = new ParseUser();

	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registre1);
    	final EditText email = (EditText) findViewById(R.id.registre1_editText_email);
		final EditText psswd = (EditText) findViewById(R.id.registre1_editText_pwd);
		final EditText psswdConfirm = (EditText) findViewById(R.id.registre1_editText_confirmarpwd);


		final Button buttonSeguent = (Button) findViewById(R.id.registre1_button_seguent);
		buttonSeguent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	//EMAIL
        		if(email.getText().toString().equals("")){
        			//falta per omplir aquest camp
        			email.setError("Camp no omplert");	
        			campEmailBuit=1;		
        		}else{
        			campEmailBuit=0;
        			
        			if(isEmailValid(email.getText().toString())){
        				ParseQuery<ParseUser> query = ParseUser.getQuery();
        				query.whereEqualTo("email", email.getText().toString());
        				try {
                			List<ParseUser> usuari = query.find();
                			if(usuari==null || usuari.size()==0){
                				//no s'ha trobat cap usuari
                				Log.e("usuariTrobat","no");
                				usuariRepetit=0;
                			}else{
                				Log.e("usuariTrobat","si");
                				usuariRepetit=1;
                				email.setError("Aquest email ja està registrat");

                			}
                		} 
                    	catch (com.parse.ParseException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				
        			}else{
        				email.setError("email no valid");
        			}
            		
        		}
        		
            	
        		//PASSWORD
        		if(psswd.getText().toString().equals("")){
        			//falta per omplir aquest camp
        			psswd.setError("Camp no omplert");
        			campPssBuit=1;			
        		}else{
        			campPssBuit=0;
        		}
        		
        		//CONFIRM PASSWORD
        		if(psswdConfirm.getText().toString().equals("")){
        			//falta per omplir aquest camp
        			psswdConfirm.setError("Camp no omplert");
        			campConfirmPssBuit=1;
        						
        		}else{
        			campConfirmPssBuit=0;
        			if(psswd.getText().toString().equals(psswdConfirm.getText().toString())){
        				psswdEquals=0;
        			}
        			else{
        				psswdEquals=1;
        				psswdConfirm.setError("No coincideixen les contrasenyes");
        			}
        			
        		}

        		//Assegurem que tots els camps estan omplerts
            	if(campEmailBuit==0 && campPssBuit==0 && campConfirmPssBuit==0 && psswdEquals==0 && usuariRepetit==0){
            		user.setCorreu(email.getText().toString());
            		user.setContrasenya(psswd.getText().toString());
            		//New activity
            		Intent i = new Intent(Registre1.this, Registre2.class);
            		i.putExtra("user", (Serializable)user);
                	startActivity(i);
            	}
            	
            }
        });
	}
	
	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	/**
	 * Override
	 */
	public void OnBackPressed(){
		finish();
	}

}

