package com.example.finding_tory.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ProfileViewModel is a ViewModel associated with the ProfileFragment.
 * It manages the data for the ProfileFragment, particularly the text data to be displayed.
 *
 * The ViewModel uses LiveData to ensure that the UI components can observe changes in the data,
 * enabling the application to handle data updates in a lifecycle-aware manner.
 */
public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Constructor for ProfileViewModel.
     * Initializes the MutableLiveData object and sets its value.
     */
    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Test User");
    }

    /**
     * Provides the text data as an observable LiveData object.
     * This allows UI components, such as fragments, to observe the data and update accordingly.
     *
     * @return LiveData object containing String data.
     */
    public LiveData<String> getText() {
        return mText;
    }
}