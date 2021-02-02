package com.example.workdaystracker.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workdaystracker.MainActivity;
import com.example.workdaystracker.Model.DateModel;
import com.example.workdaystracker.R;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private List<DateModel> dateList;
    private MainActivity activity;

    public DateAdapter(MainActivity activity){
        this.activity=activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_layout,parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position){
        DateModel item = dateList.get(position);
        viewHolder.date.setText(item.getDate());
        viewHolder.date.setChecked(intToBoolean(item.getStatus()));
    }

    public int getItemCount(){
        return dateList.size();
    }

    private boolean intToBoolean(int number){
        return number!=0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox date;
        ViewHolder(View view){
            super(view);
            date=view.findViewById(R.id.dateCheckBox);
        }
    }
}
