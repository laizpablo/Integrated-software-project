package edu.ub.pis2014.pis_09.kirolari;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private Kirolari kirolariControler;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("alarmreciver","onrecive");
		generateNotification(context,"Hi how are you?");
		
	}

	@SuppressWarnings("deprecation")

	private void generateNotification(Context context, String message) {
		kirolariControler = (Kirolari) context.getApplicationContext();
		int icon = R.drawable.iconoapp;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon,kirolariControler.getPlanningUsuari().getNomPlanning() , when);
		String title = kirolariControler.getPlanningUsuari().getNomPlanning();
		String subTitle = kirolariControler.getPlanningUsuari().getObjectius();
		//Aplicacio que obrim al cliclar la notificacio
		Intent notificationIntent = new Intent(context, KirolariActivity.class);
		notificationIntent.putExtra("content", kirolariControler.getPlanningUsuari().getNomPlanning());
		PendingIntent intent2 = PendingIntent.getActivity(context, 0 , notificationIntent, 0);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		notification.setLatestEventInfo(context, title, subTitle, intent2);
		//To play the default sound with your notification:
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
		 
		
	}

}