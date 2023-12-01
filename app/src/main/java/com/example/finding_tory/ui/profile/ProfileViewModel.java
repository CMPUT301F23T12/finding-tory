package com.example.finding_tory.ui.profile;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finding_tory.FirestoreDB;
import com.example.finding_tory.Inventory;
import com.example.finding_tory.User;
import com.example.finding_tory.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<String> userFetchError = new MutableLiveData<>();
    private MutableLiveData<Integer> totalInventories = new MutableLiveData<>();
    private MutableLiveData<Integer> totalItems = new MutableLiveData<>();
    private MutableLiveData<Double> totalValue = new MutableLiveData<>();

    public ProfileViewModel() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            FirebaseFirestore db = FirestoreDB.getDb();
            db.collection("users").document(currentUserId)
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            userFetchError.setValue("Listen failed: " + e.getMessage());
                            return;
                        }
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            userData.setValue(user);
                            // You can now update your total inventories, items, and value
                            updateInventoryData(currentUserId);
                        } else {
                            userFetchError.setValue("No user data found.");
                        }
                    });
        } else {
            userFetchError.setValue("User not logged in.");
        }
    }

    private void updateInventoryData(String userId) {
        FirebaseFirestore db = FirestoreDB.getDb();
        db.collection("users").document(userId).collection("inventories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Assuming Inventory has a proper constructor and getters to handle Firestore data
                    List<Inventory> inventories = queryDocumentSnapshots.toObjects(Inventory.class);
                    totalInventories.setValue(inventories.size());
                    Log.d("ProfileViewModel", "Total Inventories Updated: " + inventories.size()); // Log statement

                    int itemsCount = 0;
                    double valueSum = 0;
                    for (Inventory inventory : inventories) {
                        itemsCount += inventory.getItems().size();
                        for (Item item : inventory.getItems()) {
                            valueSum += item.getEstimatedValue();
                        }
                    }
                    totalItems.setValue(itemsCount);
                    Log.d("ProfileViewModel", "Total Items Updated: " + itemsCount); // Log statement
                    totalValue.setValue(valueSum);
                    Log.d("ProfileViewModel", "Total Value Updated: $" + valueSum); // Log statement
                })
                .addOnFailureListener(e -> userFetchError.setValue("Error fetching inventories: " + e.getMessage()));
    }


    public MutableLiveData<User> getUserData() { return userData; }
    public MutableLiveData<String> getUserFetchError() { return userFetchError; }
    public MutableLiveData<Integer> getTotalInventories() { return totalInventories; }
    public MutableLiveData<Integer> getTotalItems() { return totalItems; }
    public MutableLiveData<Double> getTotalValue() { return totalValue; }
}
