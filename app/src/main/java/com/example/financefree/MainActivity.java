package com.example.financefree;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.financefree.databaseClasses.AppDatabase;
import com.example.financefree.databaseClasses.DatabaseAccessor;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "finance_data";
    AppBarConfiguration appBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        DrawerLayout dl = findViewById(R.id.drawer_layout);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(dl)
                .build();
        NavigationUI.setupWithNavController(tb, navController, appBarConfiguration);

        navigationView.setNavigationItemSelectedListener(item -> {
            try {
                navController.navigate(item.getItemId());
                dl.closeDrawers();
                return true;
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        DatabaseAccessor.db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
    }


    @Override
    protected void onStop() {
        super.onStop();
        DatabaseAccessor.db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

}
