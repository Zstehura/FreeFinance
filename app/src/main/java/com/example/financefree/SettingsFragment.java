package com.example.financefree;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Map<CharSequence, CharSequence> mNames = new HashMap<>();
        String none = MyResources.getRes().getString(R.string.none);
        mNames.put(none, none);
        Thread t = new Thread(() -> {
            List<BankAccount> l = DatabaseManager.getBankAccountDao().getAll();
            for(BankAccount ba: l) {
                mNames.put(ba.name, String.valueOf(ba.bank_id));
            }
        });
        t.start();
        ListPreference lpBank = findPreference(MyResources.getRes().getString(R.string.main_bank_key));
        try {t.join();}
        catch (InterruptedException e) {e.printStackTrace();}
        assert lpBank != null;
        lpBank.setEntries(mNames.keySet().toArray(new CharSequence[0]));
        lpBank.setEntryValues(mNames.values().toArray(new CharSequence[0]));
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if(preference.getKey().equals(getString(R.string.clear_all_data_key))){
            builder.setMessage(getString(R.string.delete_all_warning))
                    .setCancelable(true)
                    .setPositiveButton(R.string.delete, (dialogInterface, i) -> {
                        Toast.makeText(getContext(), R.string.clearing_data, Toast.LENGTH_LONG).show();
                        Thread t = new Thread(DatabaseManager::clearDatabase);
                        t.start();
                        try {t.join();}
                        catch (InterruptedException e){e.printStackTrace();}
                        Toast.makeText(getContext(), R.string.data_cleared, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.cancel,((dialogInterface, i) -> {
                        // Exit without deleting
                        dialogInterface.cancel();
                    }));
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return false;
    }
}