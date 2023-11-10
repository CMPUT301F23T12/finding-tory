package com.example.finding_tory.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finding_tory.databinding.FragmentProfileBinding;

/**
 * ProfileFragment is a subclass of Fragment that represents the user profile section in the application.
 * It handles the user interface for displaying profile-related information.
 *
 * The class uses data binding to manage the UI components and integrates with a ViewModel to observe and display
 * profile information.
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    /**
     * ProfileFragment is a subclass of Fragment that represents the user profile section in the application.
     * It handles the user interface for displaying profile-related information.
     *
     * The class uses data binding to manage the UI components and integrates with a ViewModel to observe and display
     * profile information.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    /**
     * Called when the view hierarchy associated with the fragment is being destroyed.
     * It cleans up the binding to prevent memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}