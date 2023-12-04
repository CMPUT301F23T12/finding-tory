package com.example.finding_tory.ui.profile;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finding_tory.FirestoreDB;
import com.example.finding_tory.Inventory;
import com.example.finding_tory.User;
import com.example.finding_tory.Item;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<String> userFetchError = new MutableLiveData<>();
    private MutableLiveData<Integer> totalInventories = new MutableLiveData<>();
    private MutableLiveData<Integer> totalItems = new MutableLiveData<>();
    private MutableLiveData<Double> totalValue = new MutableLiveData<>();
    private String username;

    public ProfileViewModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        fetchUserData();
        updateInventoryData();
    }

    public void fetchUserData() {
        FirebaseFirestore db = FirestoreDB.getDb();
        db.collection("users").document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    userData.setValue(user);
                    Log.d("ProfileViewModel", "User Data Updated: " + user);
                })
                .addOnFailureListener(e -> userFetchError.setValue("Error fetching user data: " + e.getMessage()));
    }


    public void updateInventoryData() {
        if (username == null || username.isEmpty()) {
            userFetchError.setValue("Username not set.");
            return;
        }

        FirebaseFirestore db = FirestoreDB.getDb();
        db.collection("users").document(username).collection("inventories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Inventory> inventories = queryDocumentSnapshots.toObjects(Inventory.class);
                    totalInventories.setValue(inventories.size());
                    Log.d("ProfileViewModel", "Total Inventories Updated: " + inventories.size());

                    int itemsCount = 0;
                    double valueSum = 0;
                    for (Inventory inventory : inventories) {
                        itemsCount += inventory.getItems().size();
                        for (Item item : inventory.getItems()) {
                            valueSum += item.getEstimatedValue();
                        }
                    }
                    totalItems.setValue(itemsCount);
                    Log.d("ProfileViewModel", "Total Items Updated: " + itemsCount);
                    totalValue.setValue(valueSum);
                    Log.d("ProfileViewModel", "Total Value Updated: $" + valueSum);
                })
                .addOnFailureListener(e -> userFetchError.setValue("Error fetching inventories: " + e.getMessage()));
    }



    public MutableLiveData<User> getUserData() { return userData; }
    public MutableLiveData<String> getUserFetchError() { return userFetchError; }
    public MutableLiveData<Integer> getTotalInventories() { return totalInventories; }
    public MutableLiveData<Integer> getTotalItems() { return totalItems; }
    public MutableLiveData<Double> getTotalValue() { return totalValue; }
}
