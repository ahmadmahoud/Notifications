package com.example.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btn_notification_on);
        text1 = findViewById(R.id.title);
        text2 = findViewById(R.id.message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_notificaion();
            }
        });

    }

    private void send_notificaion() {

        // لارسال الكلمات الى الاشعارات
        String title = text1.getText().toString();
        String message = text2.getText().toString();

        NotificationManager notificationManager = (NotificationManager)
        getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICAION_CHANEL_1 = "geecoders";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // الاكواد الخاصه لتغغير الصوت
            Uri uri = Uri.parse("android.resource://"+this.getPackageName()+"/" + R.raw.test);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
             .setUsage(AudioAttributes.USAGE_ALARM).build();

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICAION_CHANEL_1, "geecoder", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("geecoders chanel is test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(uri,audioAttributes);


            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICAION_CHANEL_1);

        // عمل صورة مكبرة لاحظ ان تكون لا تكون الصيغة svg ويفضل png
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pnginage);

        notificationBuilder
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setLargeIcon(myBitmap)

                // كيفية عرض النص كامل عند السحب
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.geecodersmessage)))

                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .setContentTitle(title)
                .setContentText(message)
                .setSubText("info");

        notificationManager.notify(1, notificationBuilder.build());


    } }
