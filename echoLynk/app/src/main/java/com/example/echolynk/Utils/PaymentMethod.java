package com.example.echolynk.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethod {

    private final Context context;
    private final PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration configuration;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userId;

    public PaymentMethod(Context context) {
        this.context = context;
        paymentSheet = new PaymentSheet((ComponentActivity) context, this::onPaymentSheetResult);
        fetchApi();
    }



    public void firePaymentMethod(String userId){
        this.userId=userId;
        Log.d("Configuration", paymentIntentClientSecret);
        if (paymentIntentClientSecret != null) {
            Log.d("Configuration", configuration.toString());
            paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                    new PaymentSheet.Configuration("Codes Easy",configuration));
        }
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled){
            Toast.makeText(context,"Canceled !",Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(context,((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage(),Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            db.collection("users").document(userId)
                    .update("isPremium", true) // Set the `isPremium` field to true
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context,"Successfully",Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context,"Failed to update isPremium value.", Toast.LENGTH_SHORT).show();
                    });
        }else {
            Toast.makeText(context,"Something is wrong.",Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchApi(){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://python-backend-taupe.vercel.app/payment-sheet";


        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject= new JSONObject(response);
                    configuration=new PaymentSheet.CustomerConfiguration(
                            jsonObject.getString("customer"),
                            jsonObject.getString("ephemeralKey")
                    );

                    paymentIntentClientSecret=jsonObject.getString("paymentIntent");
                    PaymentConfiguration.init(context.getApplicationContext(), jsonObject.getString("publishableKey"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                    Log.d("onErrorResponse 1", error.toString());
                    Log.d("onErrorResponse 2", error.getLocalizedMessage());
                } catch (NullPointerException e){
                    Log.d("null pointer exception", "null pointer exception is fire");
                }
                // method to handle errors.

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();

                params.put("authKey","abc");

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}
