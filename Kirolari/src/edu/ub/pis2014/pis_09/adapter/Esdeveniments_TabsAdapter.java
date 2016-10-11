package edu.ub.pis2014.pis_09.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.ub.pis2014.pis_09.esdeveniments.EsdevenimentsCercar;
import edu.ub.pis2014.pis_09.esdeveniments.EsdevenimentsCrear;
import edu.ub.pis2014.pis_09.esdeveniments.EsdevenimentsMeus;

public class Esdeveniments_TabsAdapter extends FragmentPagerAdapter {

	public Esdeveniments_TabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return new EsdevenimentsMeus();
		case 1:
			return new EsdevenimentsCrear();
		case 2:
			return new EsdevenimentsCercar();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
