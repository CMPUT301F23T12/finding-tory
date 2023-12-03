package com.example.finding_tory.ui.profile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finding_tory.FirestoreDB;
import com.example.finding_tory.databinding.FragmentProfileBinding;
import com.example.finding_tory.ui.profile.ProfileViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observe the LiveData from the ViewModel
        profileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.textProfileName.setText(user.getName());
                binding.textUserid.setText(user.getUsername());
            }
        });

        profileViewModel.getTotalInventories().observe(getViewLifecycleOwner(), total -> {
            binding.textNumInventories.setText("Inventories created: " + total);
        });

        profileViewModel.getTotalItems().observe(getViewLifecycleOwner(), total -> {
            binding.textNumItems.setText("Total items: " + total);
        });

        profileViewModel.getTotalValue().observe(getViewLifecycleOwner(), total -> {
            binding.textTotalValue.setText("Total value: $" + String.format("%.2f", total));
        });

        profileViewModel.getUserFetchError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the edit profile action
                // For example, you could navigate to an EditProfileFragment
                // or show a dialog to edit the profile
                editProfile();
            }
        });

        binding.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the change password action
                // For example, you could navigate to a ChangePasswordFragment
                // or show a dialog to change the password
                changePassword();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateProfileInFirestore(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = profileViewModel.getUsername();
        FirestoreDB.getUsersRef().document(username)
                .update("name", newName)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void updatePasswordInFirestore(String oldPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(getContext(), "New Password and Confirm New Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = profileViewModel.getUsername();
        FirestoreDB.getUsersRef().document(username).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String storedPassword = documentSnapshot.getString("password");
                if (storedPassword != null && storedPassword.equals(oldPassword)) {
                    FirestoreDB.getUsersRef().document(username)
                            .update("password", newPassword)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(getContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void editProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Profile");

        final EditText inputName = new EditText(getContext());
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputName.setHint("Name");

        builder.setView(inputName);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = inputName.getText().toString();
                updateProfileInFirestore(newName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Password");

        // Set up the Old Password input
        final EditText inputOldPassword = new EditText(getContext());
        inputOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputOldPassword.setHint("Old Password");

        // Set up the New Password input
        final EditText inputNewPassword = new EditText(getContext());
        inputNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputNewPassword.setHint("New Password");

        // Set up the Confirm New Password input
        final EditText inputConfirmNewPassword = new EditText(getContext());
        inputConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputConfirmNewPassword.setHint("Confirm New Password");

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(inputOldPassword);
        ll.addView(inputNewPassword);
        ll.addView(inputConfirmNewPassword);
        builder.setView(ll);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPassword = inputOldPassword.getText().toString();
                String newPassword = inputNewPassword.getText().toString();
                String confirmNewPassword = inputConfirmNewPassword.getText().toString();

                updatePasswordInFirestore(oldPassword, newPassword, confirmNewPassword);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

