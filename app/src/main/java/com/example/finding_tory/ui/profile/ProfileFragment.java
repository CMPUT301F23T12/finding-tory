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

import com.example.finding_tory.FirestoreDB;
import com.example.finding_tory.Inventory;
import com.example.finding_tory.Item;
import com.example.finding_tory.Ledger;
import com.example.finding_tory.databinding.FragmentProfileBinding;

import org.mindrot.jbcrypt.BCrypt;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private String username;
    private String name;
    private Ledger gloabalLedger;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.editProfileButton.setOnClickListener(v -> editProfile());
        binding.changePasswordButton.setOnClickListener(v -> changePassword());
        gloabalLedger = Ledger.getInstance();

        updateNames();
        updateTotals();

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        binding.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return root;
    }

    public void updateNames() {
        username = gloabalLedger.getUsrIDName();
        name = gloabalLedger.getUsrName();
        binding.textProfileName.setText(name);
        binding.textUserid.setText("@" + username);
    }

    public void updateTotals() {
        int totalInventories = 0;
        int totalItems = 0;
        double totalValue = 0;
        for (Inventory inv : gloabalLedger.getInventories()) {
            totalInventories++;
            for (Item it : inv.getItems()) {
                totalItems++;
                totalValue += it.getEstimatedValue();
            }
        }
        binding.textNumInventories.setText("Inventories created: " + totalInventories);
        binding.textNumItems.setText("Total items: " + totalItems);
        binding.textTotalValue.setText("Total value: $" + String.format("%.2f", totalValue));
    }

    private void updateProfileInFirestore(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        FirestoreDB.getUsersRef().document(username)
                .update("name", newName)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updatePasswordInFirestore(String username, String oldPassword, String newPassword) {
        FirestoreDB.getUsersRef().document(username).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String storedHashedPassword = documentSnapshot.getString("password");
                if (storedHashedPassword != null && BCrypt.checkpw(oldPassword, storedHashedPassword)) {
                    String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    FirestoreDB.getUsersRef().document(username)
                            .update("password", newHashedPassword)
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
                Ledger.getInstance().setUserNames(newName, username);
                updateNames();
                updateNames();
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

                // Check if new password is different from the old password
                if (newPassword.equals(oldPassword)) {

                    Toast.makeText(getContext(), "New password must be different from the old password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make sure the new passwords match
                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(getContext(), "New Password and Confirm New Password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                updatePasswordInFirestore(username, oldPassword, newPassword);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
