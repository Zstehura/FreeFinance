package com.example.financefree;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.NotificationInfo;
import com.example.financefree.notifier.NotificationReceiver;

import java.util.Date;

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
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    
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
        view.findViewById(R.id.btnTools).setOnClickListener(view1 -> {
            NotificationInfo ni = new NotificationInfo();
            ni.notif_id = 1;
            Date d = new Date();
            ni.notif_time = d.getTime() + (90 * 1000);
            ni.title = "This is a test!";
            ni.message = "Testing... Testing... 123\ntest";
            NotificationReceiver.scheduleNotification(this.getContext(), ni);
            Thread t = new Thread(() -> DatabaseManager.getNotificationInfoDao().insert(ni));
            t.start();
            //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            //navController.navigate(R.id.toolFragment);
        });


        // Inflate the layout for this fragment
        return view;
    }
}