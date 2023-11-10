package com.example.finding_tory;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDB {


    public static FirebaseFirestore getDb() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getItemsRef() {
        return FirebaseFirestore.getInstance().collection("items");
    }
}
