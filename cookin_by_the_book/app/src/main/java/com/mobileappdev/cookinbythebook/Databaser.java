package com.mobileappdev.cookinbythebook;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Map;

public class Databaser {
    String TAG = "Databaser";
    FirebaseFirestore db;

    public void init() {
        db = FirebaseFirestore.getInstance();
    }

    public CollectionReference getStore(String storeName) {
        return db.collection(storeName);
    }

    // Util to find the owner's name based on their UUID
    public void getName(String UUID, UserCallback userCallback) {
        // place holder name
        db.collection("users").whereEqualTo(FieldPath.documentId(), UUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> userName = new ArrayList<String>();
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                String firstName = (String) document.getData().get("firstName");
                                String lastName = (String) document.getData().get("lastName");
                                userName.add(firstName);
                                userName.add(lastName);
                            }
                            userCallback.onCallback(userName);
                        } else {
                            userCallback.onCallback(new ArrayList<String>());
                        }
                    }
                });
    }


    public interface UserCallback {
        void onCallback(ArrayList<String> userName);
    }


//    public interface OnUserCompleteListener {
//        void onUsersFilled(ArrayList<String> strings);
//        void onError(Exception taskException);
//    }
}
