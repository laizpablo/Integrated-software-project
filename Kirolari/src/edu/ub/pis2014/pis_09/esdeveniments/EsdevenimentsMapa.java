package edu.ub.pis2014.pis_09.esdeveniments;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;

//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;

import edu.ub.pis2014.pis_09.kirolari.R;



public class EsdevenimentsMapa extends android.support.v4.app.FragmentActivity {
	private GoogleMap mapa;
	//private int vista = 0; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.fragment_mapa);
    	
    	mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    	CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(41.3888196,2.1628499)); //Posar localitzaci� centre BCN
    	CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
    	
    	mapa.moveCamera(center);
    	mapa.animateCamera(zoom);
    }
}
