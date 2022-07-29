package com.example.financefree;

import android.content.SharedPreferences;
import android.icu.number.NumberFormatter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Statement;

import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String REPLACE_BANK_NAME = "[acctName]";
    private static final String REPLACE_BANK_BAL = "[balance]";

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

    
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set navigation destinations
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
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.toolFragment);
        });

        // Display bank account (if set)
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        boolean dispBank = pref.getBoolean(MyResources.getRes().getString(R.string.disp_main_bank_key), false);
        String bankId = pref.getString(MyResources.getRes().getString(R.string.main_bank_key), "");
        AtomicReference<String> bankName = new AtomicReference<>();
        AtomicReference<Double> balance = new AtomicReference<>();
        if(dispBank && !bankId.equals("None")) {
            Thread t = new Thread(() -> {
                List<Statement> l = DatabaseManager.getStatementsForDay(DateParser.getLong(new GregorianCalendar()));
                for(Statement s: l) {
                    if (s.bankId == Long.parseLong(bankId)) {
                        balance.set(s.amount);
                        bankName.set(s.bankName);
                    }
                }
            });
            t.start();
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            String brobotSays = MyResources.getRes().getString(R.string.brobot_greeting_w_bank);
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
            brobotSays = brobotSays.replace(REPLACE_BANK_NAME, bankName.get());
            brobotSays = brobotSays.replace(REPLACE_BANK_BAL, nf.format(balance.get()));
            TextView tv = view.findViewById(R.id.brobot_says);
            tv.setText(brobotSays);
        }

        // Inflate the layout for this fragment
        return view;
    }
}