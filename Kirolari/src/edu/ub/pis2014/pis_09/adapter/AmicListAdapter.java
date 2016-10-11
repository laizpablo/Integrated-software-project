package edu.ub.pis2014.pis_09.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.ub.pis2014.pis_09.amics.Amic;
import edu.ub.pis2014.pis_09.kirolari.R;

public class AmicListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Amic> llistaAmics;
	
	public AmicListAdapter(Context context,ArrayList<Amic> llista){
		this.context = context;
		this.llistaAmics = llista;
	}

	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.amics_list_item, null); //(R.layout.amics_list_item, null);
        }

		TextView txtAlies = (TextView) convertView.findViewById(R.id.textView_aliesAmicTrobat);
        txtAlies.setText(llistaAmics.get(position).getAlies());
        
        TextView txtEmail = (TextView) convertView.findViewById(R.id.textView_emailAmicTrobat);
        txtEmail.setText(llistaAmics.get(position).getEmail());
        
        return convertView;
	}
	
	public void setLlistaAmics(ArrayList<Amic> llista){
		this.llistaAmics = llista;
	}

	@Override
	public int getCount() {
		return llistaAmics.size();
	}

	@Override
	public Object getItem(int position) {
		return llistaAmics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
