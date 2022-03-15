package com.example.financefree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// TODO: Add functionality

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankAccountsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankAccountsFragment extends Fragment {
    public BankAccountsFragment(){}

    public static BankAccountsFragment newInstance() {
        BankAccountsFragment fragment = new BankAccountsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_accounts, container, false);

        return view;
    }
}