package com.example.finding_tory;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * FirestoreDB is a utility class that provides access to Firebase Firestore database instances and references.
 * It offers static methods to retrieve the Firestore database instance and specific collection references within the database.
 */
public class FirestoreDB {

    /**
     * Retrieves the singleton instance of FirebaseFirestore.
     * This method provides a central point of access to the Firebase Firestore database.
     *
     * @return The FirebaseFirestore instance.
     */
    public static FirebaseFirestore getDb() {
        return FirebaseFirestore.getInstance();
    }

    /**
     * Retrieves a reference to the 'items' collection in Firebase Firestore.
     * This collection is used to store and manage item-related data in the database.
     *
     * @return A CollectionReference pointing to the 'items' collection in Firestore.
     */
    public static CollectionReference getItemsRef() {
        return FirebaseFirestore.getInstance().collection("items");
    }
}
