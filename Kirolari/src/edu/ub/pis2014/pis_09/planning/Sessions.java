package edu.ub.pis2014.pis_09.planning;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ub.pis2014.pis_09.adapter.SessionsListAdapter;
import edu.ub.pis2014.pis_09.kirolari.Kirolari;
import edu.ub.pis2014.pis_09.kirolari.R;

   /**
     * Fragment que mostra les Sessions
     */
public class Sessions extends Activity {
	/*Variables globals*/
	public static final String ARG_OPTION_NUMBER = "option_number";
	private ListView llistaSessions;
	private ArrayList<Sessio> sessionsItems;
	private SessionsListAdapter adapter;
	private Kirolari kirolariControler;
    
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sessions); 
        String titol = getResources().getString(R.string.Sessions);
        setTitle(titol);

        //Creem la llista dels objectes que sortiran
		kirolariControler = (Kirolari) getApplicationContext();
		llistaSessions = (ListView) findViewById(R.id.list_sessions);
		if (kirolariControler.getPlanningUsuari().getLlistaSessions().size()>0){
			sessionsItems = kirolariControler.getPlanningUsuari().getLlistaSessions();
			adapter = new SessionsListAdapter(getApplicationContext(), sessionsItems);
			llistaSessions.setAdapter(adapter);
		}
		else{
			Log.e("llista","buida");
		}
		
		 // ListView Item Click Listener
		llistaSessions.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Show Alert 
                /*Toast info = Toast.makeText(getApplicationContext(), llistaSessions.getItemAtPosition(position).toString(), Toast.LENGTH_LONG);
                info.show();*/
                
                // alert Dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sessions.this);
				 
 		       // Setting Dialog Message
                alertDialog.setTitle(kirolariControler.getPlanningUsuari().getNomPlanning());
 		        alertDialog.setMessage(llistaSessions.getItemAtPosition(position).toString());
 		 
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

         }); 
  
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		
	}
	
}