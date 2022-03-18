package com.example.financefree;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.dialogs.RecurringPaymentDialog;
import com.example.financefree.fileClasses.BankAccount;
import com.example.financefree.structures.parseDate;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
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
       // ba.bank_id = parseDate.genID();
       // DatabaseAccessor.insertBankAccounts(ba);

        //DatabaseAccessor.getBankAccounts().observeOn(Schedulers.io())
        //        .subscribeOn(AndroidSchedulers.mainThread())
        //        .subscribe(
        //                bankAccounts -> {
        //                    System.out.println("BankAccounts found: " + bankAccounts.size());
        //                },
        //                throwable -> {
        //                    System.out.println("RoomWithRx: "+throwable.getMessage());
        //                }
        //        );

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
            DialogFragment bankAccountDialog = BankAccountDialog.newInstance(1); //ba.bank_id);
            bankAccountDialog.show(getActivity().getSupportFragmentManager(), "bad_test");

            // DatabaseAccessor.getBankAccounts().observeOn(Schedulers.io())
            //         .subscribeOn(AndroidSchedulers.mainThread())
            //         .subscribe(
            //                 bankAccounts -> {
            //                     System.out.println("Room With Rx: "+ "accounts found: " + bankAccounts.size());
            //                 },
            //                 throwable -> {
            //                     System.out.println("Room With Rx: "+ throwable.getMessage());
            //                 }
            //         );
            // Toast.makeText(getContext(), "Account name: " + s, Toast.LENGTH_LONG).show();
            // RecurringPaymentDialog d = RecurringPaymentDialog.newInstance(null);
            // d.show(getParentFragmentManager(), null);
            // Toast.makeText(getContext(),"Name: " + d.name + " | Amount: " + String.valueOf(d.amount), Toast.LENGTH_LONG).show();
        });


        // Inflate the layout for this fragment
        return view;
    }
}