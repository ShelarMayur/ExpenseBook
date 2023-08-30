package com.mayurshelar.theexpensebook;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

public class IncomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapterIncome myAdapterIncome;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    TextView income_result;


    //TextViews for updating our data
    EditText edtAmount, edtType, edtNote;
    Button deleteBtn, updateBtn;

    String type, note, post_key;
    int amount;
    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        recyclerView = view.findViewById(R.id.recycler_id_income);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);

        income_result = view.findViewById(R.id.income_txt_result);

        fetchData();
        return view;
    }

    private void updateAmount(Data data){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myView = inflater.inflate(R.layout.update_data_item,null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        dialog.show();


        edtAmount = myView.findViewById(R.id.amount_update);
        edtType = myView.findViewById(R.id.type);
        edtNote = myView.findViewById(R.id.note);
        deleteBtn = myView.findViewById(R.id.delete_btn);
        updateBtn = myView.findViewById(R.id.update_btn);

        type = data.getType();
        note = data.getNote();
        amount = data.getAmount();

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(data.getId()).removeValue();
                dialog.dismiss();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = edtType.getText().toString().trim();
                note = edtNote.getText().toString().trim();
                amount = Integer.parseInt(edtAmount.getText().toString());

                String Date = DateFormat.getDateInstance().format(new Date());

                Data updatedData= new Data(amount,type,note,Date, data.getId());

                databaseReference.child(data.getId()).setValue(updatedData);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void fetchData(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Data> datas = new ArrayList<>();
                int totalValue = 0;

                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Data data = snapshot1.getValue(Data.class);
                    datas.add(data);
                    totalValue += data.getAmount();

                }
                String totalAmount = String.valueOf(totalValue);
                income_result.setText(totalAmount);
                myAdapterIncome = new MyAdapterIncome(datas, new MyAdapterIncome.OnItemClickListener(){
                    @Override
                    public void onItemClick(Data data) {
                        updateAmount(data);
                        Toast.makeText(getActivity(),"Item Clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(myAdapterIncome);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DatabaseError databaseError = null;
                Log.d("IncomeFragment", databaseError.getMessage());
            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }
}