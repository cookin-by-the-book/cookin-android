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
    public String getOwner(String UUID) {
        final String[] owner = {"Not found"};
//        Query ownerQuery = db.collection("users").whereEqualTo(FieldPath.documentId(), UUID);
//        Task ownerTask = ownerQuery.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                User user = document.toObject(User.class);
//                                owner[0] = (String) document.get("firstName");
//                            }
//                        }it
//                    }
//                });
        return owner[0];
    }

    // this is to do the db thing
//    public interface OnUserCompleteListener {
//        void onUsersFilled(ArrayList<String> strings);
//        void onError(Exception taskException);
//    }
}
