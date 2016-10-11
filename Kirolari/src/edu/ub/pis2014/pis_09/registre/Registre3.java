
package edu.ub.pis2014.pis_09.registre;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import edu.ub.pis2014.pis_09.kirolari.R;

public class Registre3 extends Activity {
	private int campAlturaBuit=1;
	private int campPesBuit=1;
	private int campSexeBuit=1;
	private int sexeHomeSelect=0;
	private String textSexe;
	private Usuari user;

	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registre3);
		
		user = (Usuari) getIntent().getSerializableExtra("user");
		
		final EditText altura = (EditText) findViewById(R.id.registre3_editText_altura);
		final EditText pes = (EditText) findViewById(R.id.registre3_editText_pes);
        final RadioGroup sexe = (RadioGroup)findViewById(R.id.registre3_radioGroup_sexe);
        
       
        sexe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	campSexeBuit=0;
				// TODO Auto-generated method stub
		        if (checkedId == R.id.registre3_radioGroup_sexeHome){
		           sexeHomeSelect=1;
		        }else if (checkedId == R.id.registre3_radioGroup_sexeDona){
		        	sexeHomeSelect=0;
		        }
			}
		});
        
        
		final Button button = (Button) findViewById(R.id.registre3_button_seguent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	//ALTURA
        		if(altura.getText().toString().equals("")){
        			//falta per omplir aquest camp
        			altura.setError("Camp no omplert");	
        			campAlturaBuit=1;		
        		}else{
        			campAlturaBuit=0;
        		}
        		
        		//PES
        		if(pes.getText().toString().equals("")){
        			//falta per omplir aquest camp
        			pes.setError("Camp no omplert");	
        			campPesBuit=1;		
        		}else{
        			campPesBuit=0;
        		}
        		
        		//SEXE
        		if(campSexeBuit==1){
        			textSexe="HOME";        			
        		}else{
        			if(sexeHomeSelect == 1){
            			textSexe="HOME";		
            		}else{
            			textSexe="DONA";		
            		}
        		}
        		
        		
        		//Assegurem que tots els camps estan omplerts
            	if(campAlturaBuit==0 && campPesBuit==0){
            		
            		user.setAlcada(Double.valueOf(altura.getText().toString()));
            		user.setPes((Double.valueOf(pes.getText().toString())));
            		user.setSexe(textSexe);
            		
            		//New activity
                	Intent i = new Intent(Registre3.this, Registre4.class);
            		i.putExtra("user", (Serializable)user);
                	startActivity(i);
            		
            	}
                
            }
        });
		
	}

}

