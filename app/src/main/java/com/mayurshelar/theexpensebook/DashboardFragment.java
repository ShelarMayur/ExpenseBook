package com.mayurshelar.theexpensebook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mayurshelar.theexpensebook.Model.Data;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment {
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;


    //Firebase
    FirebaseAuth auth;
    DatabaseReference incomeDatabase;
    DatabaseReference expenseDatabase;


    //Floating Button textView

    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    private boolean isOpen = false;

    private TextView incomeResult, expenseResult;

    //Animation
    private Animation fadeOpen, fadeClose;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        String uid = user.getUid();

        incomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);
        expenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);


        //Result TextViews
        incomeResult = myView.findViewById(R.id.income_set_result);
        expenseResult = myView.findViewById(R.id.expense_set_result);

        //find ids for animation
        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        //Find ids for each
        FloatingActionButton fab_main = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myView.findViewById(R.id.expense_ft_btn);

        fab_income_txt = myView.findViewById(R.id.income_ft_text);
        fab_expense_txt = myView.findViewById(R.id.expense_ft_text);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                if (isOpen) {
                    fab_income_btn.startAnimation(fadeClose);
                    fab_expense_btn.startAnimation(fadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(fadeClose);
                    fab_expense_txt.startAnimation(fadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);

                    isOpen = false;
                } else {
                    fab_income_btn.startAnimation(fadeOpen);
                    fab_expense_btn.startAnimation(fadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(fadeOpen);
                    fab_expense_txt.startAnimation(fadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    isOpen = true;
                }
            }
        });

        incomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total_income = 0;
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Data data = snapshot1.getValue(Data.class);
                    total_income += data.getAmount();
                    String stResult = String.valueOf(total_income);
                    incomeResult.setText(stResult);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        expenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total_expense = 0;
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Data data = snapshot1.getValue(Data.class);
                    total_expense += data.getAmount();
                    String stResult = String.valueOf(total_expense);
                    expenseResult.setText(stResult);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myView;
    }

    private void animation(){
        if (isOpen) {
            fab_income_btn.startAnimation(fadeClose);
            fab_expense_btn.startAnimation(fadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(fadeClose);
            fab_expense_txt.startAnimation(fadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);

            isOpen = false;
        } else {
            fab_income_btn.startAnimation(fadeOpen);
            fab_expense_btn.startAnimation(fadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(fadeOpen);
            fab_expense_txt.startAnimation(fadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

            isOpen = true;
        }
    }
    private void insertData() {

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    public void incomeDataInsert() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myView = inflater.inflate(R.layout.content_layout_for_data_insertion, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        EditText edtAmount = myView.findViewById(R.id.amount);
        EditText edtType = myView.findViewById(R.id.type);
        EditText edtNote = myView.findViewById(R.id.note);

        Button btnSave = myView.findViewById(R.id.save_btn);
        Button btnCancel = myView.findViewById(R.id.cancel_btn);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtAmount.getText().toString().trim();
                String type = edtType.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (amount.isEmpty()) {
                    edtAmount.setError("Enter Amount.");
                    return;
                }
                if (type.isEmpty()) {
                    edtNote.setError("Enter Type.");
                    return;
                }
                if (note.isEmpty()) {
                    edtNote.setError("Enter Note.");
                    return;
                }

                int curAmount = Integer.parseInt(amount);

                String id = incomeDatabase.push().getKey();

                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(curAmount, type, note, date, id);

                incomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();

                animation();
                dialog.dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void expenseDataInsert(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myView = inflater.inflate(R.layout.content_layout_for_data_insertion, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        EditText edtAmount = myView.findViewById(R.id.amount);
        EditText edtType = myView.findViewById(R.id.type);
        EditText edtNote = myView.findViewById(R.id.note);

        Button btnSave = myView.findViewById(R.id.save_btn);
        Button btnCancel = myView.findViewById(R.id.cancel_btn);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtAmount.getText().toString().trim();
                String type = edtType.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (amount.isEmpty()) {
                    edtAmount.setError("Enter Amount.");
                    return;
                }
                if (type.isEmpty()) {
                    edtNote.setError("Enter Type.");
                    return;
                }
                if (note.isEmpty()) {
                    edtNote.setError("Enter Note.");
                    return;
                }

                int curAmount = Integer.parseInt(amount);

                String id = expenseDatabase.push().getKey();

                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(curAmount, type, note, date, id);

                expenseDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(),"Data Added",Toast.LENGTH_SHORT).show();

                animation();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}