package edu.ub.pis2014.pis_09.kirolari;

import android.app.Activity;
import android.os.Bundle;

public class Settings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
 
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}