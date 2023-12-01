package com.example.finding_tory.ui.profile;
package com.example.finding_tory;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<String> userFetchError = new MutableLiveData<>();


    public ProfileViewModel() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            FirebaseFirestore db = FirestoreDB.getDb();
            db.collection("users").document(currentUserId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    userData.setValue(user);
                } else {
                    userFetchError.setValue("No user data found.");
                }
            }).addOnFailureListener(e -> {
                userFetchError.setValue("Error fetching user data: " + e.getMessage());
            });
        } else {
            userFetchError.setValue("User not logged in.");
        }
    }


    public MutableLiveData<User> getUserData() {
        return userData;
    }


    public MutableLiveData<String> getUserFetchError() {
        return userFetchError;
    }
}

