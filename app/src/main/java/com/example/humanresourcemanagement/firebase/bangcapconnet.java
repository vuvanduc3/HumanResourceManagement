package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.example.humanresourcemanagement.model.BangCap;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class bangcapconnet {
    private static final String TAG = "BangCapListActivity";
    private FirebaseFirestore db;

    // Constructor with Context
    public bangcapconnet(Context context) {
        // Initialize Firebase with Context
        FirebaseApp.initializeApp(context);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Method to get the list of bang cap
    public void getBangCapList(OnBangCapListReceivedListener listener) {
        CollectionReference bangcapRef = db.collection("bangcap");
        bangcapRef.get()
                .addOnCompleteListener(task -> {
                    List<BangCap> bangCapList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BangCap bangCap = document.toObject(BangCap.class);
                            bangCapList.add(bangCap);
                        }
                        listener.onBangCapListReceived(bangCapList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        listener.onBangCapListError(task.getException());
                    }
                });
    }

    // Method to get a single bang cap by ID
    public void getBangCapById(String bangcap_id, OnBangCapReceivedListener listener) {
        CollectionReference bangcapRef = db.collection("bangcap");
        bangcapRef.whereEqualTo("bangcap_id", bangcap_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        BangCap bangCap = document.toObject(BangCap.class);
                        listener.onBangCapReceived(bangCap);
                    } else {
                        Log.w(TAG, "No BangCap found with ID: " + bangcap_id);
                        listener.onBangCapError(task.getException());
                    }
                });
    }

    // Method to add a new BangCap
    public void addBangCap(BangCap bangCap, OnOperationCompleteListener listener) {
        CollectionReference bangcapRef = db.collection("bangcap");

        // Fetch all documents to find the highest bangcap_id
        bangcapRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int maxId = 0;

                // Loop through documents to find the highest bangcap_id
                for (DocumentSnapshot document : task.getResult()) {
                    String id = document.getId();
                    if (id.startsWith("bc")) {
                        try {
                            int numericId = Integer.parseInt(id.substring(2));
                            if (numericId > maxId) {
                                maxId = numericId;
                            }
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Invalid bangcap_id format: " + id);
                        }
                    }
                }

                // Generate a new bangcap_id with the incremented value
                String newDocumentId = "bc" + String.format("%02d", maxId + 1);
                bangCap.setBangcap_id(newDocumentId); // Set the new ID in the BangCap object

                // Add BangCap with the new document ID
                bangcapRef.document(newDocumentId)
                        .set(bangCap)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "BangCap added with ID: " + newDocumentId);
                            listener.onOperationSuccess();
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error adding BangCap", e);
                            listener.onOperationError(e);
                        });
            } else {
                Log.w(TAG, "Error retrieving bangcap documents", task.getException());
                listener.onOperationError(task.getException());
            }
        });
    }



    // Method to update an existing BangCap by document ID
    public void updateBangCap(String documentId, BangCap updatedBangCap, OnOperationCompleteListener listener) {
        db.collection("bangcap").document(documentId)
                .set(updatedBangCap)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "BangCap successfully updated!");
                    listener.onOperationSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating BangCap", e);
                    listener.onOperationError(e);
                });
    }

    // Method to delete a BangCap by document ID
    public void deleteBangCap(String documentId, OnOperationCompleteListener listener) {
        db.collection("bangcap").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "BangCap successfully deleted!");
                    listener.onOperationSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting BangCap", e);
                    listener.onOperationError(e);
                });
    }

    // Interface to receive BangCap result
    public interface OnBangCapReceivedListener {
        void onBangCapReceived(BangCap bangCap);
        void onBangCapError(Exception e);
    }

    // Interface to receive list of BangCap
    public interface OnBangCapListReceivedListener {
        void onBangCapListReceived(List<BangCap> bangCapList);
        void onBangCapListError(Exception e);
    }

    // Interface for operation completion callbacks
    public interface OnOperationCompleteListener {
        void onOperationSuccess();
        void onOperationError(Exception e);
    }
}
