package com.example.financefree;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financefree.database.entities.BankAccount;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {



    public HomeFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        BankAccount ba = new BankAccount();
        ba.name = "Test Account";
        ba.notes = "";

        view.findViewById(R.id.btnMonthly).setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.calendar);
        });
        view.findViewById(R.id.btnBanks).setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.bankAccountsFragment);
        });
        view.findViewById(R.id.btnPayments).setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.recurringPaymentsFragment);
        });
        view.findViewById(R.id.btnSettings).setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.settingsFragment);
        });

        // TODO: Add 5th option
        view.findViewById(R.id.btnOther).setOnClickListener(view1 -> {

        });


        // Inflate the layout for this fragment
        return view;
    }
}