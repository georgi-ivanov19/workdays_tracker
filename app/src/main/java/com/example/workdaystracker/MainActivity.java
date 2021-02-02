package com.example.workdaystracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.workdaystracker.Adapter.DateAdapter;
import com.example.workdaystracker.Model.DateModel;
import com.example.workdaystracker.Utils.DBHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  DialogCloseListener{

    private RecyclerView datesRecyclerView;
    private DateAdapter datesAdapter;

    private List<DateModel> datesList;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DBHandler(this);
        db.openDataBase();

        datesList = new ArrayList<DateModel>();

        datesRecyclerView=findViewById(R.id.datesRecyclerView);
        datesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datesAdapter = new DateAdapter(this);
        datesRecyclerView.setAdapter(datesAdapter);

        datesList=db.getAllDates();
        Collections.reverse(datesList);
        datesAdapter.setDate(datesList);
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        datesList = db.getAllDates();
        Collections.reverse(datesList);
        datesAdapter.setDate(datesList);
        datesAdapter.notifyDataSetChanged();
    }
}