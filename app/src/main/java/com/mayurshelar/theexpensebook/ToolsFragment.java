package com.mayurshelar.theexpensebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ToolsFragment extends Fragment {

    CardView calculator, currencyCalculator, tipCalculator, interestCalculator, loanCalculator, creditCard, discount;
    public ToolsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_tools, container, false);

        currencyCalculator = myView.findViewById(R.id.currency_calculator);
        tipCalculator = myView.findViewById(R.id.tip_calculator);
        interestCalculator = myView.findViewById(R.id.interest_calculator);
        loanCalculator = myView.findViewById(R.id.credit_card);
        creditCard = myView.findViewById(R.id.credit_card);
        discount = myView.findViewById(R.id.discount_calculator);
        calculator = myView.findViewById(R.id.calculator);

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
            }
        });

        tipCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TipCalculatorActivity.class));
            }
        });
        return myView;
    }
}