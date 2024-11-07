package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.example.humanresourcemanagement.model.Skills;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class skillconnet {
    private static final String TAG = "SkillListActivity";
    private FirebaseFirestore db;

    // Constructor with Context
    public skillconnet(Context context) {
        // Initialize Firebase with Context
        FirebaseApp.initializeApp(context);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Method to get the list of bang cap
    public void getSkillList(OnSkillListReceivedListener listener) {
        CollectionReference skillRef = db.collection("skills");
        skillRef.get()
                .addOnCompleteListener(task -> {
                    List<Skills> skillsList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Skills skills = document.toObject(Skills.class);
                            skillsList.add(skills);
                        }
                        listener.onSkillListReceived(skillsList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        listener.onSkillListError(task.getException());
                    }
                });
    }

    // Method to get a single bang cap by ID
    public void getSkillById(String mask, OnSkillReceivedListener listener) {
        CollectionReference skillRef = db.collection("skills");
        skillRef.whereEqualTo("mask", mask)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Skills skills = document.toObject(Skills.class);
                        listener.onSkillReceived(skills);
                    } else {
                        Log.w(TAG, "No BangCap found with ID: " + mask);
                        listener.onSkillError(task.getException());
                    }
                });
    }

    // Method to add a new BangCap
    public void addSkill(Skills skills, OnOperationCompleteListener listener) {
        CollectionReference skillRef = db.collection("skills");

        // Fetch all documents to find the highest bangcap_id
        skillRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int maxId = 0;

                // Loop through documents to find the highest bangcap_id
                for (DocumentSnapshot document : task.getResult()) {
                    String id = document.getId();
                    if (id.startsWith("sk")) {
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
                String newDocumentId = "sk" + String.format("%02d", maxId + 1);
                skills.setMask(newDocumentId); // Set the new ID in the BangCap object

                // Add BangCap with the new document ID
                skillRef.document(newDocumentId)
                        .set(skills)
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
    public void updateSkill(String documentId, Skills updatedSkill, OnOperationCompleteListener listener) {
        db.collection("skills").document(documentId)
                .set(updatedSkill)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Skill successfully updated!");
                    listener.onOperationSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating skill", e);
                    listener.onOperationError(e);
                });
    }

    // Method to delete a BangCap by document ID
    public void deleteSkill(String documentId, OnOperationCompleteListener listener) {
        db.collection("skills").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "skill successfully deleted!");
                    listener.onOperationSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting skill", e);
                    listener.onOperationError(e);
                });
    }

    // Interface to receive BangCap result
    public interface OnSkillReceivedListener {
        void onSkillReceived(Skills skills);
        void onSkillError(Exception e);
    }

    // Interface to receive list of BangCap
    public interface OnSkillListReceivedListener {
        void onSkillListReceived(List<Skills> skillsList);
        void onSkillListError(Exception e);
    }

    // Interface for operation completion callbacks
    public interface OnOperationCompleteListener {
        void onOperationSuccess();
        void onOperationError(Exception e);
    }
}
