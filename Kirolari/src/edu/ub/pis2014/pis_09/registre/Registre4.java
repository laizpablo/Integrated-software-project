
package edu.ub.pis2014.pis_09.registre;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.kirolari.kirolariSignIn;

public class Registre4 extends Activity{
	
	Usuari user;
	//ParseObject myParseObject = new ParseObject("Usuari"); // Class Name
	ParseUser myParseUser = new ParseUser();
	
	@SuppressLint("SimpleDateFormat")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registre4);
				
		user = (Usuari) getIntent().getSerializableExtra("user");
		
		TextView email = (TextView) findViewById(R.id.registre4_textCanviEmail);
		email.setText(user.getCorreu());
		TextView contrasenya = (TextView) findViewById(R.id.registre4_textCanviPassword);
		contrasenya.setText(user.getContrasenya());
		TextView nom = (TextView) findViewById(R.id.registre4_textCanviNom);
		nom.setText(user.getNom());
		TextView cognom = (TextView) findViewById(R.id.registre4_textCanviCognoms);
		cognom.setText(user.getCognoms());
		TextView alies = (TextView) findViewById(R.id.registre4_textCanviAlias);
		alies.setText(user.getAlies());
		TextView dataNaix = (TextView) findViewById(R.id.registre4_textCanviDataNaix);
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		String stringdataNaix = sdf.format(user.getDataNaixement());
		dataNaix.setText(stringdataNaix);
		TextView altura = (TextView) findViewById(R.id.registre4_textCanviAltura);
		altura.setText(Double.toString(user.getAlcada()));
		TextView pes = (TextView) findViewById(R.id.registre4_textCanviPes);
		pes.setText(Double.toString(user.getPes()));
		TextView sexe = (TextView) findViewById(R.id.registre4_textCanviSexe);
		sexe.setText(user.getSexe());
		
		

		
		final Button button = (Button) findViewById(R.id.registre4_button_cancelar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //New activity
            	Intent i = new Intent(Registre4.this, kirolariSignIn.class);
            	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(i);
            }
        });
        
        final Button button1 = (Button) findViewById(R.id.registre4_button_registrarse);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //New activity            	
            	
            	myParseUser.setUsername(user.getCorreu());
            	myParseUser.setEmail(user.getCorreu());
            	myParseUser.setPassword(user.getContrasenya());
            	myParseUser.put("name", user.getNom());
            	myParseUser.put("surname", user.getCognoms());
            	myParseUser.put("alias", user.getAlies());
            	myParseUser.put("birthdate", user.getDataNaixement());
            	myParseUser.put("height",user.getAlcada());
            	myParseUser.put("weight", user.getPes());
            	myParseUser.put("sex", user.getSexe());
            	
            	myParseUser.signUpInBackground(new SignUpCallback() {
					@Override
					public void done(ParseException arg0) {}
				});
            	
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(Registre4.this);
				
            	//Setting Title Message
				  alertDialog.setTitle(R.string.informacio);
				  
				  //setting icon message
				 alertDialog.setIcon(R.drawable.icon_alert);
				  
 		       // Setting Dialog Message
 		        alertDialog.setMessage(getString(R.string.verificacioEmail));
 		 
 		        // Setting Positive Button
 		        alertDialog.setPositiveButton(getString(R.string.acceptar),
 		        		new DialogInterface.OnClickListener() {
 				            public void onClick(DialogInterface dialog,int which) {
 				            	Intent i = new Intent(Registre4.this, kirolariSignIn.class);
 				            	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 				            	startActivity(i);
 				            }
 		        		});

 		        alertDialog.show();
            }
        });
		
	}

}

