package com.example.financefree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.financefree.databaseClasses.DatabaseAccessor;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if(preference.getKey().equals("clear_all_data")){
            builder.setMessage("Caution! This will delete ALL data currently contained on this app. " +
                    "You will not be able to recover any of it after confirming.")
                    .setCancelable(true)
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        Toast.makeText(getContext(), "Clearing Data...", Toast.LENGTH_LONG).show();
                        DatabaseAccessor.clearData(0);
                        Toast.makeText(getContext(), "Data cleared",Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel",((dialogInterface, i) -> {
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