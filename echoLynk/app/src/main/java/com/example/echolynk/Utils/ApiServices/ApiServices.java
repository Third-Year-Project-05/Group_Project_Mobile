package com.example.echolynk.Utils.ApiServices;



import com.example.echolynk.Model.HistoryMassage;
import com.example.echolynk.Model.SpeechToTextMassage;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {
    @GET("users/{userId}")
    Call<ResponseBody> getSpeechToTextMassage(@Path("userId") int userId);

    @Headers({
            "Content-Type: application/json",
            "x-vercel-protection-bypass: WzZBi8VJg6JKhYQXapGCThQhaaNWicLQ"
    })
    @POST("predict")
    Call<ResponseBody> postSpeechToTextMassage(@Body SpeechToTextMassage massages);
}
