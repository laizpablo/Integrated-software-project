package edu.ub.pis2014.pis_09.planning;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

import edu.ub.pis2014.pis_09.kirolari.R;

   /**
     * Fragment mostra un grafic amb dades de l'historial
     */
public class HistorialGrafic extends Fragment {
 
	private int opcio=1; //Per tenir en compte la opcio escollida al radiobutton
    private ArrayList<Double> distanciaMes=new ArrayList<Double>();
    private ArrayList<Double> caloriesMes=new ArrayList<Double>();
    private ArrayList<Integer> tempsMes=new ArrayList<Integer>();
    private ArrayList<Double> distanciesDia=new ArrayList<Double>();
 	private ArrayList<Integer> tempsDia=new ArrayList<Integer>();
 	private ArrayList<Double> caloriesDia=new ArrayList<Double>();
 	@SuppressWarnings("serial")
	private ArrayList<String> mesos=new ArrayList<String>(){{
 		add("Gener");add("Febrer");add("Març");add("Abril");add("Maig");add("Juny");add("Juliol");add("Agost");add("Setembre");add("Octubre");add("Novembre");add("Desembre");
 	}};
 	private int mes;//Si es -1 mostra l'historial anual
                
		
	public HistorialGrafic() {
            // Empty constructor required for fragment subclasse				
    }
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_historial_grafic, container, false);
        String titol = "Historial";
        getActivity().setTitle(titol);
        /*
         * Obtenim les dades enviades de l'historial a traves del bundle
         */
        final Bundle args=this.getArguments();
        @SuppressWarnings("unchecked")
		ArrayList<Double> arrayList = ((ArrayList<Double>) args.getSerializable("DistanciesMes"));
		distanciaMes=arrayList;//getActivity().getIntent().getSerializableExtra("DistanciesMes"));
        @SuppressWarnings("unchecked")
		ArrayList<Double> arrayList2 = (ArrayList<Double>) args.getSerializable("CaloriesMes");
		caloriesMes=arrayList2;
        @SuppressWarnings("unchecked")
		ArrayList<Integer> arrayList3 = (ArrayList<Integer>) args.getSerializable("TempsMes");
		tempsMes=arrayList3;
		@SuppressWarnings("unchecked")
		ArrayList<Double> arrayList4 = (ArrayList<Double>) args.getSerializable("DistanciesDia");
		distanciesDia=arrayList4;
		@SuppressWarnings("unchecked")
		ArrayList<Double> arrayList5 = (ArrayList<Double>) args.getSerializable("CaloriesDia");
		caloriesDia= arrayList5;
		@SuppressWarnings("unchecked")
		ArrayList<Integer> arrayList6 = (ArrayList<Integer>) args.getSerializable("TempsDia");
		tempsDia = arrayList6;
        mes=(args.getInt("mesHistorial"));
        opcio=args.getInt("opcioHistorial");

        //Per comprovar que tenim seleccionada la opcio bona
        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroupHistGrafic); 
        if (opcio==2) {
          	radioGroup.check(R.id.historialGrafic_radioButton_historialDistancia);
        }else if (opcio==3){
        	radioGroup.check(R.id.historialGrafic_radioButton_historialTemps);
        }else{
           	radioGroup.check(R.id.historialGrafic_radioButton_historialCalories);
        }
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked){
                    if (checkedRadioButton.getId()==R.id.historialGrafic_radioButton_historialDistancia) {
                       	opcio=2;
                    } else if (checkedRadioButton.getId()==R.id.historialGrafic_radioButton_historialCalories){
                       	opcio=1;                        	
                    } else if (checkedRadioButton.getId()==R.id.historialGrafic_radioButton_historialTemps){
                      	opcio=3;
                    }
                }
                //Carrega un nou fragment que mostri el grafic amb la opcio que volem    
                Fragment historial_grafic = new HistorialGrafic();
                args.putInt("opcioHistorial",opcio);
                historial_grafic.setArguments(args);
        		FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, historial_grafic).commit();
                // graficGraphView(rootView,opcio);
            }
                
        });
        graficGraphView(rootView,opcio);                    
        return rootView;
    }

    /**
     * Genera un grafic segons la opcio i segons si es vol veure el grafic anual o el d'un sol mes
     * @param rootView
     * @param opcio
     */
    private void graficGraphView(View rootView,int opcio) {
    	//mostra la distancia anual per mesos 
		if(opcio==2 && distanciaMes.size()==12 && mes==-1) {
			//Les dades per al grafic
			GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(0,0)
					, new GraphViewData(1, distanciaMes.get(0))
		            , new GraphViewData(2, distanciaMes.get(1))
		            , new GraphViewData(3, distanciaMes.get(2))
		            , new GraphViewData(4, distanciaMes.get(3))
		            , new GraphViewData(5, distanciaMes.get(4))
					, new GraphViewData(6, distanciaMes.get(5))
		            , new GraphViewData(7, distanciaMes.get(6))
		            , new GraphViewData(8, distanciaMes.get(7))
		            , new GraphViewData(9, distanciaMes.get(8))
					, new GraphViewData(10, distanciaMes.get(9))
		            , new GraphViewData(11, distanciaMes.get(10))
		            , new GraphViewData(12, distanciaMes.get(11))
		    });
		 
			//Crea un grafic de barres
		    BarGraphView graphView = new BarGraphView(getActivity(),"Distancia");
		    //Afegeix les dades
		    graphView.addSeries(exampleSeries);
		    //
		    graphView.getGraphViewStyle().setNumVerticalLabels(13);
		    graphView.getGraphViewStyle().setNumHorizontalLabels(12);
		    graphView.setHorizontalLabels(new String[] {"G", "F", "Mr", "Ab","Mi","Jn","Jl","Ag","Oc","N","D"," "});
		 
		    try {
		        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		        layout.addView(graphView);
		    } catch (NullPointerException e) {
		            // something to handle the NPE.
		    }
			
		//mostra les calories anuals per mesos    
		}else if (opcio==1 && caloriesMes.size()==12 && mes==-1){
			GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(0,0)
					, new GraphViewData(1, caloriesMes.get(0))
		            , new GraphViewData(2, caloriesMes.get(1))
		            , new GraphViewData(3, caloriesMes.get(2))
		            , new GraphViewData(4, caloriesMes.get(3))
		            , new GraphViewData(5, caloriesMes.get(4))
		            , new GraphViewData(6, caloriesMes.get(5))
		            , new GraphViewData(7, caloriesMes.get(6))
		            , new GraphViewData(8, caloriesMes.get(7))
		            , new GraphViewData(9, caloriesMes.get(8))
		            , new GraphViewData(10, caloriesMes.get(9))
		            , new GraphViewData(11, caloriesMes.get(10))
		            , new GraphViewData(12, caloriesMes.get(11))
		    });
		 
		    BarGraphView graphView = new BarGraphView(getActivity(),"Calories");
		    graphView.addSeries(exampleSeries);
		    graphView.getGraphViewStyle().setNumVerticalLabels(13);
		    graphView.getGraphViewStyle().setNumHorizontalLabels(12);
		    graphView.setHorizontalLabels(new String[] {"G", "F", "Mr", "Ab","Mi","Jn","Jl","Ag","Oc","N","D"," "});// data
		    try {
		        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		        layout.addView(graphView);
		    } catch (NullPointerException e) {
		            // something to handle the NPE.
		    }
			
		//mostra el temps anual per mesos    
		}else if (opcio==3 && tempsMes.size()==12 && mes==-1){
			GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(0,0)
					, new GraphViewData(1, tempsMes.get(0))
					, new GraphViewData(2, tempsMes.get(1))
					, new GraphViewData(3, tempsMes.get(2))
					, new GraphViewData(4, tempsMes.get(3))
					, new GraphViewData(5, tempsMes.get(4))
					, new GraphViewData(6, tempsMes.get(5))
					, new GraphViewData(7, tempsMes.get(6))
					, new GraphViewData(8, tempsMes.get(7))
					, new GraphViewData(9, tempsMes.get(8))
					, new GraphViewData(10, tempsMes.get(9))
					, new GraphViewData(11, tempsMes.get(10))
					, new GraphViewData(12, tempsMes.get(11))
		    });
		 
		    BarGraphView graphView = new BarGraphView(getActivity(), "Temps");
		    graphView.addSeries(exampleSeries);
		    graphView.getGraphViewStyle().setNumVerticalLabels(13);
		    graphView.getGraphViewStyle().setNumHorizontalLabels(12);
		    graphView.setHorizontalLabels(new String[] {"G", "F", "Mr", "Ab","Mi","Jn","Jl","Ag","Oc","N","D",""});// data

		 
		    try {
		        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		        layout.addView(graphView);
		    } catch (NullPointerException e) {
		        // something to handle the NPE.
		    }
			
		//mostra la distancia per dies del mes escollit a l'historial    
		}else if (opcio==2 && distanciesDia.size()>=31 && mes!=-1) {
			GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(0,0)
					, new GraphViewData(1, distanciesDia.get(0))
		            , new GraphViewData(2, distanciesDia.get(1))
		            , new GraphViewData(3, distanciesDia.get(2))
		            , new GraphViewData(4, distanciesDia.get(3))
		            , new GraphViewData(5, distanciesDia.get(4))
					, new GraphViewData(6, distanciesDia.get(5))
		            , new GraphViewData(7, distanciesDia.get(6))
		            , new GraphViewData(8, distanciesDia.get(7))
		            , new GraphViewData(9, distanciesDia.get(8))
					, new GraphViewData(10, distanciesDia.get(9))
		            , new GraphViewData(11, distanciesDia.get(10))
		            , new GraphViewData(12, distanciesDia.get(11))
					, new GraphViewData(13, distanciesDia.get(12))
		            , new GraphViewData(14, distanciesDia.get(13))
		            , new GraphViewData(15, distanciesDia.get(14))
		            , new GraphViewData(16, distanciesDia.get(15))
					, new GraphViewData(17, distanciesDia.get(16))
		            , new GraphViewData(18, distanciesDia.get(17))
		            , new GraphViewData(19, distanciesDia.get(18))
		            , new GraphViewData(20, distanciesDia.get(19))
					, new GraphViewData(21, distanciesDia.get(20))
		            , new GraphViewData(22, distanciesDia.get(21))
		            , new GraphViewData(23, distanciesDia.get(22))
					, new GraphViewData(24, distanciesDia.get(23))
		            , new GraphViewData(25, distanciesDia.get(24))
					, new GraphViewData(26, distanciesDia.get(25))
		            , new GraphViewData(27, distanciesDia.get(26))
		            , new GraphViewData(28, distanciesDia.get(27))
					, new GraphViewData(29, distanciesDia.get(28))
					, new GraphViewData(30, distanciesDia.get(29))
					, new GraphViewData(31, distanciesDia.get(30))
		    });
			BarGraphView graphView = new BarGraphView(getActivity(),mesos.get(mes)); 
			graphView.addSeries(exampleSeries);
			graphView.getGraphViewStyle().setNumVerticalLabels(10);
			graphView.getGraphViewStyle().setNumHorizontalLabels(29);
			graphView.setHorizontalLabels(new String[] {"1","3","5","7","9","11","13","15","17","19","21","23","25","27","29","31",""});
			graphView.getGraphViewStyle().setTextSize(15);		 			
		    try {
		        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		        layout.addView(graphView);
		    } catch (NullPointerException e) {
		            // something to handle the NPE.
		    }
		    
		//mostra les calories per dia del mes escollit a l'historial    
		} else if (opcio==1 && caloriesDia.size()>=31 && mes!=-1) {
			GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(0,caloriesDia.get(0))
					, new GraphViewData(1, caloriesDia.get(1))
		            , new GraphViewData(2, caloriesDia.get(2))
		            , new GraphViewData(3, caloriesDia.get(3))
		            , new GraphViewData(4, caloriesDia.get(4))
		            , new GraphViewData(5, caloriesDia.get(5))
					, new GraphViewData(6, caloriesDia.get(6))
		            , new GraphViewData(7, caloriesDia.get(7))
		            , new GraphViewData(8, caloriesDia.get(8))
		            , new GraphViewData(9, caloriesDia.get(9))
					, new GraphViewData(10, caloriesDia.get(10))
					,new GraphViewData(11, distanciesDia.get(11))
		            , new GraphViewData(12, caloriesDia.get(12))
					, new GraphViewData(13, caloriesDia.get(13))
		            , new GraphViewData(14, caloriesDia.get(14))
		            , new GraphViewData(15, caloriesDia.get(15))
		            , new GraphViewData(16, caloriesDia.get(16))
					, new GraphViewData(17, caloriesDia.get(17))
		            , new GraphViewData(18, caloriesDia.get(18))
		            , new GraphViewData(19, caloriesDia.get(19))
		            , new GraphViewData(20, caloriesDia.get(20))
					, new GraphViewData(21, caloriesDia.get(21))
		            , new GraphViewData(22, caloriesDia.get(22))
		            , new GraphViewData(23, caloriesDia.get(23))
					, new GraphViewData(24, caloriesDia.get(24))
		            , new GraphViewData(25, caloriesDia.get(25))
					, new GraphViewData(26, caloriesDia.get(26))
		            , new GraphViewData(27, caloriesDia.get(27))
		            , new GraphViewData(28, caloriesDia.get(28))
					, new GraphViewData(29, caloriesDia.get(29))
					, new GraphViewData(30, caloriesDia.get(30))
		    });		 
			BarGraphView graphView = new BarGraphView(getActivity(),mesos.get(mes)); 
			graphView.addSeries(exampleSeries);
			graphView.getGraphViewStyle().setNumVerticalLabels(10);
			graphView.getGraphViewStyle().setNumHorizontalLabels(29);
			graphView.setHorizontalLabels(new String[] {"1","3","5","7","9","11","13","15","17","19","21","23","25","27","29","31",""});
			graphView.getGraphViewStyle().setTextSize(15);
		    try {
		        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		        layout.addView(graphView);
		    } catch (NullPointerException e) {
		            // something to handle the NPE.
		    }
		    
		//mostra el temps per dia del mes escollit a l'historial    
		} else if (opcio==3 && tempsDia.size()>=31 && mes!=-1) {
			GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(1, distanciesDia.get(0))
					, new GraphViewData(1, tempsDia.get(0))
		            , new GraphViewData(2, tempsDia.get(1))
		            , new GraphViewData(3, tempsDia.get(2))
		            , new GraphViewData(4, tempsDia.get(3))
		            , new GraphViewData(5, tempsDia.get(4))
					, new GraphViewData(6, tempsDia.get(5))
		            , new GraphViewData(7, tempsDia.get(6))
		            , new GraphViewData(8, tempsDia.get(7))
		            , new GraphViewData(9, tempsDia.get(8))
					, new GraphViewData(10, tempsDia.get(9))
		            , new GraphViewData(11, tempsDia.get(10))
		            , new GraphViewData(12, tempsDia.get(11))
					, new GraphViewData(13, tempsDia.get(12))
		            , new GraphViewData(14, tempsDia.get(13))
		            , new GraphViewData(15, tempsDia.get(14))
		            , new GraphViewData(16, tempsDia.get(15))
					, new GraphViewData(17, tempsDia.get(16))
		            , new GraphViewData(18, tempsDia.get(17))
		            , new GraphViewData(19, tempsDia.get(18))
		            , new GraphViewData(20, tempsDia.get(19))
					, new GraphViewData(21, tempsDia.get(20))
		            , new GraphViewData(22, tempsDia.get(21))
		            , new GraphViewData(23, tempsDia.get(22))
					, new GraphViewData(24, tempsDia.get(23))
		            , new GraphViewData(25, tempsDia.get(24))
					, new GraphViewData(26, tempsDia.get(25))
		            , new GraphViewData(27, tempsDia.get(26))
		            , new GraphViewData(28, tempsDia.get(27))
					, new GraphViewData(29, tempsDia.get(28))
					, new GraphViewData(30, tempsDia.get(29))
					, new GraphViewData(31, tempsDia.get(30))
		    });
		 
		    BarGraphView graphView = new BarGraphView(getActivity(),mesos.get(mes));
		    graphView.addSeries(exampleSeries);
		    graphView.getGraphViewStyle().setNumVerticalLabels(10);
		    graphView.getGraphViewStyle().setNumHorizontalLabels(29);
		    graphView.setHorizontalLabels(new String[] {"1","3","5","7","9","11","13","15","17","19","21","23","25","27","29","31",""});
		    graphView.getGraphViewStyle().setTextSize(15);

		    try {
		        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		        layout.addView(graphView);
		    } catch (NullPointerException e) {
		            // something to handle the NPE.
		    }
		}
			 
	}
		
}