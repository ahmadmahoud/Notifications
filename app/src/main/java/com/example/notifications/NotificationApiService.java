package com.example.notifications;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiService {


    @Headers({
            "Authorization: key="+"AAAAdfacxxc:APA91bGE5bkZBLYJRyrUzQc_8_KTRZ2Z8KN9zPP380td9NQukts_4tFpFF8_JI0d87kHqpfxKOxT22O4-r2hBvSZoFbMgNQxZW6flGvbDcfIDpEIGYzeaYs7SlhmyoiET-SVwoGwCMYJ" ,
            "Content-Type: application/json"
    })
    @POST("fcm/send")
    Call<JsonObject> sendNotification(@Body JsonObject payload);

}
