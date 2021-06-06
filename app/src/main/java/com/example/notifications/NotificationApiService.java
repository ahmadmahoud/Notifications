package com.example.notifications;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiService {


    @Headers({
            "Authorization: key="+"paste your token here" ,
            "Content-Type: application/json"
    })
    @POST("fcm/send")
    Call<JsonObject> sendNotification(@Body JsonObject payload);

}
