package com.example.financefree;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.financefree.datahandlers.CustomDate;
import com.example.financefree.datahandlers.DataManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {

//    public static final int PROFILE_FRAG_ID = R.id.profileFragment;
//    public static final int HOME_FRAG_ID = R.id.homeFragment;
//    public static final int SETTINGS_FRAG_ID = R.id.settingsFragment;
//    public static final int RECPAY_FRAG_ID = R.id.recurringPaymentsFragment;
//    public static final int CAL_FRAG_ID = R.id.calendar;

    /*
    public static final int[][] CAL_NUM_IDS = {{R.id.Box1_1, R.id.Box1_2, R.id.Box1_3,R.id.Box1_4,R.id.Box1_5,R.id.Box1_6,R.id.Box1_7},
        {R.id.Box2_1, R.id.Box2_2, R.id.Box2_3,R.id.Box2_4,R.id.Box2_5,R.id.Box2_6,R.id.Box2_7},
        {R.id.Box3_1, R.id.Box3_2, R.id.Box3_3,R.id.Box3_4,R.id.Box3_5,R.id.Box3_6,R.id.Box3_7},
        {R.id.Box4_1, R.id.Box4_2, R.id.Box4_3,R.id.Box4_4,R.id.Box4_5,R.id.Box4_6,R.id.Box4_7},
        {R.id.Box5_1, R.id.Box5_2, R.id.Box5_3,R.id.Box5_4,R.id.Box5_5,R.id.Box5_6,R.id.Box5_7},
        {R.id.Box6_1, R.id.Box6_2, R.id.Box6_3,R.id.Box6_4,R.id.Box6_5,R.id.Box6_6,R.id.Box6_7}};
*/

    public static final String APP_DATA = "finance_data";
    AppBarConfiguration appBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(findViewById(R.id.drawer_layout))
                .build();
        NavigationUI.setupWithNavController(tb, navController, appBarConfiguration);


        navigationView.setNavigationItemSelectedListener(item -> {
            try {
                navController.navigate(item.getItemId());
                return true;
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            DataManager.readData(sp.getString(APP_DATA,"null"));
        } catch (IOException | JSONException | CustomDate.DateErrorException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

}
