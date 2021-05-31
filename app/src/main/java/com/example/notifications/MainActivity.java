package com.example.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText text1, text2;
    Button push;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = findViewById(R.id.title);
        text2 = findViewById(R.id.message);
        push = findViewById(R.id.btn_notification_on);

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_notificaion();
            }
        });

//        gettoken();


    }

    // تشغيل الاشعارات
    private void send_notificaion() {

        String title = text1.getText().toString();
        String message = text2.getText().toString();

        // نقل المستخدم الى الاشعار المطلوب
        Map<String,String> data = new HashMap();
        String usetid = data.get("userid");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("key",usetid);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICAION_CHANEL_1 = "geecoders";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // الاكواد الخاصه لتغغير الصوت
            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.test);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM).build();

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICAION_CHANEL_1, "geecoder", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("geecoders chanel is test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(uri, audioAttributes);

            notificationManager.createNotificationChannel(notificationChannel);

        }
        // عمل صورة مكبرة لاحظ ان تكون لا تكون الصيغة svg ويفضل png
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pnginage);

         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICAION_CHANEL_1)

                .setAutoCancel(true)
                 .setContentIntent(pendIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                 .setLargeIcon(myBitmap)
//                .setLargeIcon(myBitmap)

                // كيفية عرض النص كامل عند السحب عند طريق سهم في الاعلى
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(getString(R.string.geecodersmessage)))

                // عرض التفاصيل عند السحب بدون السهم
//                .setStyle(new NotificationCompat.InboxStyle()
//                        .addLine("messageSnippet1"))

                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .setContentTitle(title)
                .setContentText(message)
                .setSubText("Geecoders");

        notificationManager.notify(0, notificationBuilder.build());


// لعمل اكثر من اشعار بدلا من اشعار واحد (multiple notifications)

//        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
//        notificationManager.notify(m, notificationBuilder.build());

    }

    // نتيجة الحصول على token المستخدم
    private void gettoken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String deviceToken = task.getResult();
                Log.i(TAG, "the result token is : " + deviceToken);
            }
        });
    }

    // كود يقوم بتحويل اكواد الجيسون
    private JsonObject buildNotificationPayload() {  // { object } يتعامل مع ,  [ Array ] يتعامل مع  + compose notification json payload
        JsonObject payload = new JsonObject();
        payload.addProperty("to", "Token");
        // compose data payload here
        JsonObject data = new JsonObject();
        data.addProperty("title", "Title value");
        data.addProperty("message", "Message value");

        // add data payload
        payload.add("data", data);
        return payload;

        // { "to" : "token value"
        // "data" : {  title_value , "message" : " Message value "  }
        // }

    }


    // دفع الاشعار يمكنك عمل زر واستدعائها بداخله
    private void pushNotification() {
        JsonObject payload = buildNotificationPayload();
        // send notification to receiver ID
        ApiClient.getApiService().sendNotification(payload).enqueue(
                new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Notification send successful",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        String errormessage = t.getMessage();
                        Toast.makeText(MainActivity.this, errormessage, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onFailure: is " + errormessage);
                    }
                });
    }

}
