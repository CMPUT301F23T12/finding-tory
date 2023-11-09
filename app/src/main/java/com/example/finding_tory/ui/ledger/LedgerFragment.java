package com.example.finding_tory.ui.ledger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finding_tory.databinding.FragmentLedgerBinding;
import com.google.android.material.snackbar.Snackbar;

public class LedgerFragment extends Fragment {

    private FragmentLedgerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LedgerViewModel ledgerViewModel =
                new ViewModelProvider(this).get(LedgerViewModel.class);

        binding = FragmentLedgerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.addInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView textView = binding.textLedger;
        ledgerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}