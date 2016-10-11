package edu.ub.pis2014.pis_09.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.ub.pis2014.pis_09.esdeveniments.Esdeveniment;
import edu.ub.pis2014.pis_09.kirolari.R;

public class EsdevenimentListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Esdeveniment> llistaEsdeveniments;
	
	public EsdevenimentListAdapter(Context context){
		this.context = context;
		llistaEsdeveniments = new ArrayList<Esdeveniment>();
	}

	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.esdeveniments_list_item, parent,false);
        }
		TextView txtNom = (TextView) convertView.findViewById(R.id.textView_nomEsdeveniment);
        TextView txtData = (TextView) convertView.findViewById(R.id.textView_dataEsdeveniment);
		TextView txtParticipants = (TextView) convertView.findViewById(R.id.textView_participantsEsdeveniment);
        
        txtNom.setText(llistaEsdeveniments.get(position).getNom());
        
        txtData.setText(llistaEsdeveniments.get(position).getData());
		
		ParseQuery<ParseObject> queryParticipants = ParseQuery.getQuery("Participants");
		queryParticipants.whereEqualTo("idEsdeveniment", llistaEsdeveniments.get(position).getId());
	
		String textParticipants = null;
		try {
			textParticipants = convertView.getResources().getString(R.string.events_participants);
			textParticipants += queryParticipants.count();
		}catch(Exception e){
		}
		txtParticipants.setText(textParticipants);
        
        return convertView;
	}
	
	public void setLlistaEsdeveniments(ArrayList<Esdeveniment> llista){
		llistaEsdeveniments = llista;
	}

	@Override
	public int getCount() {
		return llistaEsdeveniments.size();
	}

	@Override
	public Object getItem(int position) {
		return llistaEsdeveniments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
