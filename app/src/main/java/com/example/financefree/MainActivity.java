package com.example.financefree;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.recyclers.BankAccountRVContent;
import com.google.android.material.navigation.NavigationView;

/**
 *
 * TODO: Add floating calculator <<<<
 *
 */

public class MainActivity extends AppCompatActivity implements BankAccountDialog.BankAccountDialogListener {

    public static final String DATABASE_NAME = "finance_data";
    AppBarConfiguration appBarConfiguration;

    public static DatabaseManager dm;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize Files
        // try {
        //     DataManager.initData(this);
        // } catch (IOException | JSONException e) {
        //     e.printStackTrace();
        // }

        // Get database
        dm = new DatabaseManager(this.getApplication());

        // Set navigation
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
      //  dba = new DatabaseAccessor(getApplication());

        //dbView = new DatabaseViewModel(this.getApplication());
       // dbView.getBankAccounts().subscribeOn(Schedulers.computation())
       //         .observeOn(AndroidSchedulers.mainThread())
       //         .subscribe(modelClasses -> {
       //             System.out.println("RoomWithRx: " + modelClasses.size());
       //         }, e -> System.out.println("RoomWithRx: " + e.getMessage()));

        //DatabaseAccessor.db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();

        /*
        if(DatabaseAccessor.db.bankAccountDao().getAll().size() < 1){
            BankAccount ba = new BankAccount();
            ba.accountName = "New Bank";
            ba.bank_id = 0;
            ba.notes = "Example of a new bank!";
            DatabaseAccessor.db.bankAccountDao().insertAll(ba);

            BankStatement bs = new BankStatement();
            bs.amount = 100;
            bs.s_id = 0;
            bs.bank_id = ba.bank_id;
            bs.date = parseDate.getLong(new GregorianCalendar());
            DatabaseAccessor.db.bankStatementDao().insertAll(bs);

            RecurringPayment rp = new RecurringPayment();
            rp.name = "My Paycheck";
            rp.rp_id = 0;
            rp.bankId = ba.bank_id;
            rp.frequency = 1;
            rp.frequencyType = RecurringPayment.ON;
            rp.amount = 500;
            rp.startDate = parseDate.getLong( new GregorianCalendar());
            rp.endDate = parseDate.getLong(1,1,2100);
            rp.notes = "Example of a recurring payment. This is a positive number so it will add to " +
                    "your account! Try creating a recurring payment with a negative amount for bills!";
            DatabaseAccessor.db.recurringPaymentDao().insertAll(rp);

            PaymentEdit pe = new PaymentEdit();
            pe.rp_id = rp.rp_id;
            pe.edit_id = 0;
            pe.edit_date = parseDate.getLong(new GregorianCalendar());
            pe.action = PaymentEdit.ACTION_CHANGE_AMOUNT;
            pe.new_amount = 523.52;
            DatabaseAccessor.db.paymentEditDao().insertAll(pe);

            SinglePayment sp = new SinglePayment();
            sp.bank_id = ba.bank_id;
            sp.sp_id = 0;
            sp.name = "Netflix";
            sp.amount = 12;
            sp.date = parseDate.getLong(new GregorianCalendar());
            DatabaseAccessor.db.singlePaymentDao().insertAll(sp);
        }
*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBADialogPositiveClick(BankAccountDialog dialog) {
        if(dialog != null) {
            BankAccount ba = new BankAccount();
            Thread t;
            if(dialog.isNew) t = new Thread(() -> DatabaseManager.getBankAccountDao().insertAll(ba));
            else t = new Thread(() -> DatabaseManager.getBankAccountDao().update(ba));
            ba.notes = ((BankAccountDialog) dialog).bnkNotes.getText().toString();
            ba.name = ((BankAccountDialog) dialog).bnkName.getText().toString();
            ba.bank_id = ((BankAccountDialog) dialog).bankId;
            t.start();

            //if(dialog.isNew) barva.notifyItemRangeInserted(dialog.position, 1);
            //else barva.notifyItemChanged(dialog.position);
            BankAccountRVContent.addItem(ba);

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBADialogNegativeClick(BankAccountDialog dialog) {

    }
}
