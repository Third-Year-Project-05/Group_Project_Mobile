package com.example.echolynk.Utils;

import static android.content.ContentValues.TAG;
import static com.example.echolynk.Utils.FirebaseUtils.currentUserId;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PaymentUtils {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db= FirebaseFirestore.getInstance();
    private static final String userID=currentUserId();

    public static void countImageGeneration(Context context){
        db.collection("payments")
                .whereEqualTo("userId",userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    try {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            queryDocumentSnapshots.forEach(documentSnapshot -> {
                                int imageCount = Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("imageCount")).toString());
                                double totalCost  =Double.parseDouble(Objects.requireNonNull(documentSnapshot.get("totalCost")).toString()) ;
                                imageCount--;
                                totalCost-=0.016;

                                String documentId=documentSnapshot.getId();
                                db.collection("payments").document(documentId)
                                        .update("imageCount ",imageCount,"totalCost ",totalCost)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "countImageGeneration: Success");
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                            });
                        }
                    }catch (NullPointerException e){
                        Log.d(TAG, "Null pointer exception is fire");
                    }

                }).addOnFailureListener(e -> {
                    Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static void countSuggestionGeneration(Context context){
        db.collection("payments")
                .whereEqualTo("userId",userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    try {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            queryDocumentSnapshots.forEach(documentSnapshot -> {
                                int suggestionCount = Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("suggestionCount ")).toString());
                                double totalCost  =Double.parseDouble(Objects.requireNonNull(documentSnapshot.get("totalCost ")).toString()) ;
                                suggestionCount--;
                                totalCost-=0.002;


                                String documentId=documentSnapshot.getId();
                                db.collection("payments").document(documentId)
                                        .update("suggestionCount ",suggestionCount,"totalCost ",totalCost)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "countSuggestionGeneration: Success");
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                            });
                        }
                    }catch (NullPointerException e){
                        Log.d(TAG, "Null pointer exception is fire");
                    }

                }).addOnFailureListener(e -> {
                    Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
