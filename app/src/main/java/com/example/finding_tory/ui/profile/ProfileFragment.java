package com.example.finding_tory.ui.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finding_tory.databinding.FragmentProfileBinding;
import com.example.finding_tory.ui.profile.ProfileViewModel;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
