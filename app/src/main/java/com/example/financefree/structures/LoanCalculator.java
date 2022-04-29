package com.example.financefree.structures;

import com.example.financefree.database.entities.Loan;

public final class LoanCalculator {

    
    public static double balanceOn(Loan loan, long date) {
        double bal = loan.principle;
        long cDate = loan.start_date;
        while(cDate < date){
            if(Frequency.occursOn(loan, cDate)){
                bal *= (1 + (loan.apr / Frequency.paymentsPerYear(loan)));
            }

            cDate++;
        }

        return bal;
    }

    public static double projectedBalanceOn(Loan loan, long date, double payments) {
        long cDate = 0;
        for(long l: loan.payments.keySet()){
            if(l < date && l > cDate) cDate = l;
        }

        double bal = balanceOn(loan, cDate);

        return bal;
    }

    public static double getPayment(Loan loan) {
        double r = loan.apr;
        double n = Frequency.paymentsPerYear(loan);
        double t = loan.term_Length;
        double p = loan.principle;

        final double pow = Math.pow((1 + r), (n * t));
        double r1n1 = pow - 1;
        double r1rn = r * pow;
        return (p / r1n1 / r1rn);
    }

}
