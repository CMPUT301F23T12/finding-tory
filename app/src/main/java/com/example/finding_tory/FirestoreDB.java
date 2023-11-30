package com.example.finding_tory;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * FirestoreDB is a utility class that provides access to Firebase Firestore database instances and references.
 * It offers static methods to retrieve the Firestore database instance and specific collection references within the database.
 */
public class FirestoreDB {
    // debugMode true prevents any modifications to the Firestore DB, retrieval is still allowed
    private static boolean debugMode = false;

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
     * Retrieves a reference to the 'users' collection in Firebase Firestore.
     * This collection is used to store and manage user-related data in the database.
     *
     * @return A CollectionReference pointing to the 'users' collection in Firestore.
     */
    public static CollectionReference getUsersRef() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    /**
     * Retrieves a reference to the 'inventories' collection of a specific user in Firebase Firestore.
     * This collection is nested within the 'users' collection and is used to store and manage inventory-related data for each user.
     *
     * @param username The username identifying the specific user.
     * @return A CollectionReference pointing to the 'inventories' collection of the specified user in Firestore.
     */
    public static CollectionReference getInventoriesRef(String username) {
        return FirebaseFirestore.getInstance().collection("users").document(username).collection("inventories");
    }

    /**
     * Retrieves a reference to the 'items' collection in Firebase Firestore.
     * This collection is used to store and manage item-related data in the database.
     *
     * @return A CollectionReference pointing to the 'items' collection in Firestore.
     */
    public static CollectionReference getItemsRef(String username, String inventoryName) {
        return FirebaseFirestore.getInstance().collection("users").document(username).collection("inventories").document(inventoryName).collection("items");
    }

    /**
     * Deletes an item from the Firestore database.
     *
     * @param username  The username identifying the specific user.
     * @param inventory The Inventory from which to delete the item.
     * @param item      The Item to be deleted.
     */
    public static void deleteItemDB(String username, Inventory inventory, Item item) {
        FirestoreDB.getItemsRef(username, inventory.getInventoryName()).document(item.getDescription()).delete();
    }

    /**
     * Edits an item in Firestore by set method which overwrites the existing data
     *
     * @param username     The username identifying the specific user.
     * @param inventory    The Inventory containing the item to be edited.
     * @param existingItem The existing item to be edited.
     * @param updatedItem  The updated item
     */
    public static void editItemFromFirestore(String username, Inventory inventory, Item existingItem, Item updatedItem) {
        // Must delete existingItem for cases where user updates the description
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getItemsRef(username, inventory.getInventoryName()).document(existingItem.getDescription()).delete();
            FirestoreDB.getItemsRef(username, inventory.getInventoryName()).document(updatedItem.getDescription()).set(updatedItem).addOnSuccessListener(aVoid -> {
            }).addOnFailureListener(e -> {
                // Handle failure
                e.printStackTrace();
            });
        }
    }

    /**
     * Getter method for debug mode
     *
     * @return debugMode
     */
    public static boolean isDebugMode() {
        return debugMode;
    }
}
