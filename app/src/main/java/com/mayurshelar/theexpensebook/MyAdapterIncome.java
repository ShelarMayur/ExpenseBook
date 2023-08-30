package com.mayurshelar.theexpensebook;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mayurshelar.theexpensebook.Model.Data;
import java.util.ArrayList;
public class MyAdapterIncome extends RecyclerView.Adapter<MyAdapterIncome.MyViewHolder> {
    private static ArrayList<Data> dataList;
    private static OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onItemClick(Data data);
    }
    public MyAdapterIncome(ArrayList<Data> dataList1, OnItemClickListener clickListener1){
        dataList = dataList1;
        clickListener = clickListener1;
    }
    @NonNull
    @Override
    public MyAdapterIncome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.income_recycler_data, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyAdapterIncome.MyViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.bind(data);
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, amount, note, type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_txt_income);
            amount = itemView.findViewById(R.id.amount_txt_income);
            type = itemView.findViewById(R.id.type_txt_income);
            note = itemView.findViewById(R.id.note_txt_income);
            itemView.setOnClickListener(this);
        }
        public void bind(Data data){
            date.setText(data.getDate());
            note.setText(data.getNote());
            type.setText(data.getType());
            amount.setText(String.valueOf(data.getAmount()));
        }
        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Data data = dataList.get(position);
                clickListener.onItemClick(data);
            }
        }
    }
}
