package edu.ub.pis2014.pis_09.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.ub.pis2014.pis_09.activitat.Activitat;
import edu.ub.pis2014.pis_09.kirolari.R;

public class HistorialListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Activitat> llistaActivitats;
	
	public HistorialListAdapter(Context context, ArrayList<Activitat> llista){
		this.context = context;
		this.llistaActivitats = llista;
	}

	@SuppressLint("SimpleDateFormat")
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.historial_list_item, null); //(R.layout.amics_list_item, null);
        }
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy"); 
		String dia = null;
		try {
			dia = df.format(llistaActivitats.get(position).getDia());
		} catch (Exception e) {
			dia = " ";
		}

		TextView txtDia = (TextView) convertView.findViewById(R.id.textView_dia);
		txtDia.setText(dia);
        
        TextView txtActivitat = (TextView) convertView.findViewById(R.id.textView_Activitat);
        txtActivitat.setText(llistaActivitats.get(position).getActivitat());
        
        TextView txtDurada = (TextView) convertView.findViewById(R.id.textView_durada);
        if (llistaActivitats.get(position).getActivitat().equals("")){
        	txtDurada.setText(" ");
        }else {
        	txtDurada.setText(Integer.toString(llistaActivitats.get(position).getDurada())+" ");
        }
        
        
        return convertView;
	}
	
	public void setLlistaAmics(ArrayList<Activitat> llista){
		this.llistaActivitats = llista;
	}

	@Override
	public int getCount() {
		return llistaActivitats.size();
	}

	@Override
	public Object getItem(int position) {
		return llistaActivitats.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
