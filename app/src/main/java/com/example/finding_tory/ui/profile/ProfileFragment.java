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
    private ProfileViewModel profileViewModel;


    /**
     * Inflates the fragment layout, initializes the ViewModel, and sets up data binding.
     * Observes changes in the profile data through the ViewModel and updates the UI accordingly.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Returns the root view for the fragment's layout.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        profileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.textProfileName.setText(user.getName());
                // Populate other profile data views...
            }
        });


        profileViewModel.getUserFetchError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Handle the error. For example, show a Toast or a Snackbar
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                // You might want to navigate the user to a login screen or error page.
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

