
package edu.ub.pis2014.pis_09.registre;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.ub.pis2014.pis_09.kirolari.R;

public class Registre2 extends Activity {
	private int campNomBuit=1;
	private int campDataValid=1;
	private Usuari user;
	
	private TextView textData;
	private String stringData;
	private String any;
	private ImageButton canviData;
	private int year;
    private int month;
    private int day;
 
    static final int DATE_PICKER_ID = 1111; 
 
	static final int DATE_DIALOG_ID = 999;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registre2);

		user = (Usuari) getIntent().getSerializableExtra("user");
	
		//inicialitzem els camps d'usuari
		final EditText nom = (EditText) findViewById(R.id.registre2_editText_nom);
		final EditText cognom = (EditText) findViewById(R.id.registre2_editText_cognoms);
		final EditText alies = (EditText) findViewById(R.id.registre2_editText_alies);
		//final EditText dataNaixement = (EditText) findViewById(R.id.registre2_textData);
		
		//Inicialitzacions pel datepicker
		textData = (TextView) findViewById(R.id.registre2_textData);
		// Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        
        // Show current date        
        textData.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));
        
        if(month+1<10){
			 stringData = (day + "-0" + (month +1)+"-"+ year + " " + "08:00:00");
		 }else{
			 stringData = (day + "-" + (month +1)+"-"+ year + " " + "08:00:00");
		 }


        //SELECCIONAR DATA
        canviData = (ImageButton) findViewById(R.id.registre2_canviData);
        canviData.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				 showDialog(DATE_PICKER_ID);
			}
		});

		
		//BOTO SEGUENT
		final Button buttonSeguent = (Button) findViewById(R.id.registre2_button_seguent);
		buttonSeguent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
			public void onClick(View v) {
            	
            	//NOM
        		if(nom.getText().toString().equals("")){
        			//falta per omplir aquest camp
        			nom.setError("Camp no omplert");	
        			campNomBuit=1;		
        		}else{
        			campNomBuit=0;
        		}

        		any = stringData.substring(6,10);
        		Calendar cal = Calendar.getInstance();
        		if(Integer.parseInt(any) < cal.get(Calendar.YEAR)-13){
        			campDataValid=0;
        		}else{
        			textData.setError("");
        			CharSequence text = "Has de tenir minim 14 anys per poder registrar-te";
    	    		int duration = Toast.LENGTH_SHORT;

    	    		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
    	    		toast.show();
        			campDataValid=1;
        		}
        		
        		
            	//Assegurem que tots els camps obligatoris estan omplerts
            	if(campNomBuit==0 && campDataValid==0){
            		Log.e("entraif", "si");
            		
            		user.setNom(nom.getText().toString());
            		user.setCognoms(cognom.getText().toString());
            		if(alies.getText().toString().equals("")){
            			user.setAlies(nom.getText().toString());
            		}else{
            			user.setAlies(alies.getText().toString());
            		}
            		
            		
            		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
            		Date myDate = null;
            		try {
						myDate = df.parse(stringData);
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
            		user.setDataNaixement(myDate);



                	//New activity
                	Intent i = new Intent(Registre2.this, Registre3.class);
            		i.putExtra("user", (Serializable)user);
                	startActivity(i);
            	}
                
            }
        });
		
	}

	
	 @Override
	 protected Dialog onCreateDialog(int id) {
		 switch (id) {
		 case DATE_PICKER_ID:
			 // open datepicker dialog.
			 // set date picker for current date
			 // add pickerListener listner to date picker
			 return new DatePickerDialog(this, pickerListener, year ,month ,day);
		 }
		 return null;
	 }
	 
	 private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 // when dialog box is closed, below method will be called.
		 @Override
		 public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			 year  = selectedYear;
			 month = selectedMonth;
			 day   = selectedDay;
			 // Show selected date
			 textData.setText(new StringBuilder().append(day)
					 .append("-").append(month + 1).append("-").append(year)
					 .append(" "));
			 
			 if(month+1<10){
				 stringData = (day + "-0" + (month +1)+"-"+ year + " " + "08:00:00");
			 }else{
				 stringData = (day + "-" + (month +1)+"-"+ year + " " + "08:00:00");
			 }
			 if(day<10){
				 stringData = "0"+stringData; 
				 Log.i("data", stringData);
			 }


		 }
	 };

}

