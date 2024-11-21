package com.example.echolynk.View;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.SignInSignUp.SignIn;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    UserModel userModel;
    private static final String endPoint = "https://python-backend-taupe.vercel.app/?vercelToolbarCode=jCED6y0PtFzX-P0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        requestSuggestion();
        Log.d("ABC", "Connection Initialization. 0");

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (FirebaseUtils.isLoggedIn()){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            Log.d("codeishere", "7");
            startActivity(intent);
        }else {
            Intent intent = new Intent(SplashActivity.this, SignIn.class);
            Log.d("codeishere", "8");
            startActivity(intent);
        }


    }

    private void requestSuggestion() {

        Log.d("ABC", "Connection Initialization. 1");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endPoint,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ABC", "Connection Initialization. 2");
                        Log.d("Response --> ", response.toString());
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ABC", "Connection Initialization. error");
                Log.d(TAG, "Request to GPT-3 error: " + error.getMessage());
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Log.d("ABC", "Connection Initialization. 3");

                Map<String, String> mapHeaders = new HashMap<>();

                mapHeaders.put("Content-Type", "application/json");
                mapHeaders.put("x-vercel-protection-bypass", "WzZBi8VJg6JKhYQXapGCThQhaaNWicLQ");

                return mapHeaders;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, "VolleyError" + volleyError.getMessage());
                return super.parseNetworkError(volleyError);
            }
        };

        int timeOutPeriod = 60000;

        RetryPolicy policy = new DefaultRetryPolicy(
                timeOutPeriod,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        jsonObjectRequest.setRetryPolicy(policy);

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }


}