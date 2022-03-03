package com.example.financefree.preferenceManagement;

import com.example.financefree.datahandlers.CustomDate;

public class RecurringPayment {
    public static final int ACTION_MOVE_DATE = 0;
    public static final int ACTION_SKIP_DATE = 1;
    public static final int ACTION_CHANGE_AMOUNT = 2;

    public static class PaymentEdit{
        public CustomDate edit_date;
        public CustomDate new_date;
        public double new_amount;
        public int action;
    }



}
