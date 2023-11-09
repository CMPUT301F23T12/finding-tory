package com.example.finding_tory.ui.ledger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LedgerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LedgerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ledger fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}