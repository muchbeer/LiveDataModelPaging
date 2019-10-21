package muchbeer.raum.livedatamodelpaging.screen;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import muchbeer.raum.data.R;
import muchbeer.raum.livedatamodelpaging.view.MainActivity;

public class MovieNotificationHelper {



        private static final String CHANNEL_ID = "Movie channel id";
        private static final int NOTIFICATION_ID=16;
        private static MovieNotificationHelper instance;
        private Context mContext;

        private MovieNotificationHelper(Context context) {
            this.mContext = context;
        }

        public static MovieNotificationHelper getInstance(Context context) {
            if(instance == null) {
                instance = new MovieNotificationHelper(context);

            }
            return instance;
        }

        public void createNotification() {
            createNotificationChannel();

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,0);
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cinema);

            Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                              .setSmallIcon(R.drawable.cinema)
                    .setLargeIcon(icon)
                    .setContentTitle("Dogs retrieved")
                    .setContentText("This is a notification to let you know..")
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(icon)
                            .bigLargeIcon(null)
                    )
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManagerCompat.from(mContext).notify(NOTIFICATION_ID, notification);
        }

        private void createNotificationChannel() {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                String name = "Gianna";
                String description = "Movies of the world from Cinemax";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

            }
        }
    }
