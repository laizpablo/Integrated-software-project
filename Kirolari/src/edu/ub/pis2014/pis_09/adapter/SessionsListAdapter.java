package edu.ub.pis2014.pis_09.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.ub.pis2014.pis_09.kirolari.R;
import edu.ub.pis2014.pis_09.planning.Sessio;

/*
 * Classe SessionsListAdapter que extend BaseAdapter
 */
public class SessionsListAdapter extends BaseAdapter {
	/*Variables globals*/
	private Context context;
	private ArrayList<Sessio> llistaSessions;
	
	/*
	 * Constructor de la classe SessionsListAdapter
	 * Rep com a parametres Context i una ArrayList<Sessio>
	 */
	public SessionsListAdapter (Context context, ArrayList<Sessio> llista){
		this.context = context;
		this.llistaSessions = llista;
	}

	/*
	 * Funcio getView
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 * Retorna un objecte View
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sessions_list_item, null); //(R.layout.amics_list_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.textView_sessions);
        txtTitle.setText(llistaSessions.get(position).toStringList());
        
        return convertView;
	}
	
	@Override
	/*
	 * Funcio sobreescrita getCount
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return llistaSessions.size();
	}

	@Override
	/*
	 * Funcio sobreescrita getItem
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return llistaSessions.get(position);
	}

	@Override
	/*
	 * Funcio sobreescrita getItemId
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}
}

