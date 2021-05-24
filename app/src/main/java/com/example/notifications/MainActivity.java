package com.example.notifications;

import androidx.annotation.NonNull;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gettoken();
    }

    // تشغيل الاشعارات
//    private void send_notificaion() {
//
//        // لارسال الكلمات الى الاشعارات
//        String title = text1.getText().toString();
//        String message = text2.getText().toString();
//
//        NotificationManager notificationManager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICAION_CHANEL_1 = "geecoders";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            // الاكواد الخاصه لتغغير الصوت
//            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.test);
//
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_ALARM).build();
//
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICAION_CHANEL_1, "geecoder", NotificationManager.IMPORTANCE_HIGH);
//
//            notificationChannel.setDescription("geecoders chanel is test");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.BLUE);
//            notificationChannel.setVibrationPattern(new long[]{ 0 });
//            notificationChannel.enableVibration(true);
//            notificationChannel.setSound(uri, audioAttributes);
//
//
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICAION_CHANEL_1);
//
//        // عمل صورة مكبرة لاحظ ان تكون لا تكون الصيغة svg ويفضل png
//        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pnginage);
//
//        notificationBuilder
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setShowWhen(true)
//                .setLargeIcon(myBitmap)
//
//                // كيفية عرض النص كامل عند السحب
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(getString(R.string.geecodersmessage)))
//
////                .setStyle(new NotificationCompat.InboxStyle()
////                        .addLine("messageSnippet1")
////                        .addLine("messageSnippet2"))
//
//                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSubText("info");
//
//        notificationManager.notify(1, notificationBuilder.build());
//
//
//    }

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

//            data.addProperty(key.getText().toString(), value.getText().toString());
        // add data payload
        payload.add("data", data);
        return payload;
    }

    // { "to" : "token value"
    // "data" : {  title_value , "message" : " Message value "  }
    // }


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
