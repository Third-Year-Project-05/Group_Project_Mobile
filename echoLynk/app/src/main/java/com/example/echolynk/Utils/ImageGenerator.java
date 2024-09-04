package com.example.echolynk.Utils;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.echolynk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageGenerator {
    private static final String apiKey = "sk-proj-XmRmjjHi5qvdQbkFT7tXT3BlbkFJEZpbfe9lRYALeoVAKEx6";
    private static String stringOutput = "";
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    ImageView responseView;
    private Dialog dialog;

    private void showPopup(String imageUrl) {
        Log.d(TAG, "showPopup: " + imageUrl);
        dialog = new Dialog(this.dialog.getContext());
        dialog.setContentView(R.layout.popup_layout);

        Button closePopupButton = dialog.findViewById(R.id.closePopupButton);
        responseView = dialog.findViewById(R.id.imageView);

        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void Generate(String text) {

        // Get the ProgressBar from the layout
        ProgressBar progressBar = this.dialog.findViewById(R.id.progressBarLiveChatImageGenaretor);

        // Show the ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("prompt", text);
            jsonObject.put("size", "256x256");

            Log.d(TAG, "Request to Dalle: " + jsonObject.toString());

        } catch (JSONException e) {

            Toast.makeText(this.dialog.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        String imageEndPoint = "https://api.openai.com/v1/images/generations";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, imageEndPoint, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            stringOutput = response.getJSONArray("data").
                                    getJSONObject(0).
                                    getString("url");

                            Log.d(TAG, "Request to Dalle: " + stringOutput);
                            new ImageGenerator.FetchImage(stringOutput).start();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d(TAG, "Request to GPT-3: " + stringOutput);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Request to GPT-3: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> mapHeaders = new HashMap<>();

                mapHeaders.put("Content-Type", "application/json");
                mapHeaders.put("Authorization", "Bearer " + apiKey);

                return mapHeaders;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
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

        Volley.newRequestQueue(this.dialog.getContext()).add(jsonObjectRequest);

    }

    class FetchImage extends Thread{
        String URL;
        Bitmap bitmap;

        FetchImage(String URL){
            this.URL = URL;
        }

        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(dialog.getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });


            try {
                InputStream inputStream = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                        showPopup(URL);

                    }
                    responseView.setImageBitmap(bitmap);
                    dialog.show();


                }
            });
        }

    }

}
