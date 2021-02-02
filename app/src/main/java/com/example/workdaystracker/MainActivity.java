package com.example.workdaystracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.workdaystracker.Adapter.DateAdapter;
import com.example.workdaystracker.Model.DateModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView datesRecyclerView;
    private DateAdapter datesAdapter;

    private List<DateModel> datesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        datesList = new ArrayList<DateModel>();

        datesRecyclerView=findViewById(R.id.datesRecyclerView);
        datesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datesAdapter = new DateAdapter(this);
        datesRecyclerView.setAdapter(datesAdapter);

        //dummy value for test
        DateModel dummy = new DateModel();
        dummy.setDate("This is a dummy!");
        dummy.setStatus(0);
        dummy.setId(1);

        datesList.add(dummy);
        datesList.add(dummy);
        datesList.add(dummy);
        datesList.add(dummy);
        datesList.add(dummy);

        datesAdapter.setDate(datesList);
    }
}