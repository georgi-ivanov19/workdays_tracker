package com.example.workdaystracker.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workdaystracker.AddNewTask;
import com.example.workdaystracker.MainActivity;
import com.example.workdaystracker.Model.DateModel;
import com.example.workdaystracker.R;
import com.example.workdaystracker.Utils.DBHandler;

import java.util.Date;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private List<DateModel> dateList;
    private MainActivity activity;
    private DBHandler db;

    public DateAdapter(DBHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_layout,parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position){
        db.openDataBase();
        DateModel item = dateList.get(position);
        viewHolder.date.setText(item.getDate());
        viewHolder.date.setChecked(intToBoolean(item.getStatus()));
        viewHolder.date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount(){
        return dateList.size();
    }

    private boolean intToBoolean(int number){
        return number!=0;
    }

    public void setDate(List<DateModel> datesList){
        this.dateList = datesList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void editItem(int position){
        DateModel item = dateList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("date", item.getDate());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox date;
        ViewHolder(View view){
            super(view);
            date=view.findViewById(R.id.dateCheckBox);
        }
    }
}
