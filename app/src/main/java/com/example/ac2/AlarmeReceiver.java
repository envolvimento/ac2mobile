import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String nome = intent.getStringExtra("nome");

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "med_channel")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Hora de tomar seu remédio")
                .setContentText("Não esqueça de tomar: " + nome)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel("med_channel", "Lembretes de Medicamentos", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(canal);
        }

        nm.notify(nome.hashCode(), builder.build());
    }
}
