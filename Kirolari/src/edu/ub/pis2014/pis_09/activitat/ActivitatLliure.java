package edu.ub.pis2014.pis_09.activitat;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.ub.pis2014.pis_09.kirolari.R;

   /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public class ActivitatLliure extends Fragment {
    	private int opcio;



        public ActivitatLliure() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activitat_lliure, container, false);
            String titol = "Activitat Lliure";
            
            Spinner spinnerAct= (Spinner) rootView.findViewById(R.id.spinner_esports);
            spinnerAct.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					opcio=position;
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
            });
                        
            //Realitzar una activitat
            final Button boto_comencar = (Button) rootView.findViewById(R.id.button_Comencar2);
            boto_comencar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                	Intent intent = new Intent(getActivity(), RealitzarActivitat.class);
                	intent.putExtra("opcio", (Serializable)opcio);
                    startActivity(intent); 
                }
            });
            /*
            LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    		LocationListener mlocListener = new LocationListener() {
    			
    			@Override
    			public void onLocationChanged(Location loc) {
    				//text="My current location is: \n Latitud = "+loc.getLatitude()+"\n Longitud = "+loc.getLongitude();
    				//Toast.makeText( getApplicationContext(), text, Toast.LENGTH_LONG).show();
    				//TextView lloc = (TextView) findViewById(R.id.textView1);
    				//lloc.setText(text);
    			}
    			
    			@Override
    			public void onProviderDisabled(String provider) {
    				Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
    				//text="Disabled";
    			}
    			
    			@Override
    			public void onProviderEnabled(String provider){
    				Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();

    			}
    			
    			@Override
    			public void onStatusChanged(String provider, int status,Bundle extras){
    			
    			}
    		};
    		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
			*/
            getActivity().setTitle(titol);
            return rootView;
        }
    }