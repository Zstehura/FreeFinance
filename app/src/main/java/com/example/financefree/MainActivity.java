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

import com.example.financefree.databaseClasses.AppDatabase;
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

        // try {
        //     DataManager.readData(sp.getString(APP_DATA,"null"));
        // } catch (IOException | JSONException | CustomDate.DateErrorException e) {
        //     e.printStackTrace();
        // }
/*
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();

        BankAccount ba = db.bankAccountDao().testDb();
        if(ba == null) {
            ba = new BankAccount();
            ba.accountName = "New Bank Account";
            ba.notes = "The first test bank account. Try making some changes or adding your own!";
            db.bankAccountDao().insertAll(ba);
        }
        SinglePayment sp = db.singlePaymentDao().testDb();
        if(sp == null){
            sp = new SinglePayment();
            sp.date = parseDate.getLong(3,4,2022);
            sp.name = "First payment - Netflix";
            sp.amount = -8.99;
            sp.bank_id = ba.bank_id;
            db.singlePaymentDao().insertAll(sp);
        }
        RecurringPayment rp = db.recurringPaymentDao().testDb();
        if(rp == null){
            rp = new RecurringPayment();
            rp.name = "Example Paycheck";
            rp.frequencyType = RecurringPayment.EVERY;
            rp.frequency = 14;
            rp.startDate = parseDate.getLong(1,7,2022);
            rp.endDate = parseDate.getLong(1,1,2099);
            rp.amount = 500;
            rp.bankId = ba.bank_id;
            rp.notes = "This is a sample paycheck. It's set to repeat every 2 weeks, but you can change that to " +
                    "however often you get paid!";
            db.recurringPaymentDao().insertAll(rp);
        }
        BankStatement bs = db.bankStatementDao().testDb();
        if(bs == null) {
            bs = new BankStatement();
            bs.amount = 100;
            bs.bank_id = ba.bank_id;
            bs.date = parseDate.getLong(3,5,2022);
            db.bankStatementDao().insertAll(bs);
        }
        PaymentEdit pe = db.paymentEditDao().testDb();
        if(pe == null){
            pe = new PaymentEdit();
            pe.rp_id = rp.rp_id;
            pe.edit_date = parseDate.getLong(3,18,2022);
            pe.action = PaymentEdit.ACTION_MOVE_DATE;
            pe.move_to_date = parseDate.getLong(3,17,2022);
            db.paymentEditDao().insertAll(pe);
        }
*/
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

}
