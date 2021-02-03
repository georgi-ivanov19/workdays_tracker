package com.example.workdaystracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.workdaystracker.Adapter.DateAdapter;
import com.example.workdaystracker.Model.DateModel;
import com.example.workdaystracker.Utils.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  DialogCloseListener{

    private RecyclerView datesRecyclerView;
    private DateAdapter datesAdapter;
    private FloatingActionButton fab;
    Thread thread = new Thread(){
        @Override
        public void run(){
            try {
                while(!thread.isInterrupted()){
                    Thread.sleep(10);
                    runOnUiThread(() -> {
                        TextView paidDays = findViewById(R.id.paidDays);
                        paidDays.setText("Paid days: " + db.totalDaysPaid());
                        TextView totalWorkdays = findViewById(R.id.totalDays);
                        totalWorkdays.setText("Total workdays: " + db.totalDaysWorked());
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };



    private List<DateModel> datesList;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thread.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DBHandler(this);
        db.openDataBase();

        datesList = new ArrayList<DateModel>();

        datesRecyclerView=findViewById(R.id.datesRecyclerView);
        datesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datesAdapter = new DateAdapter(db,this);
        datesRecyclerView.setAdapter(datesAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(datesAdapter));
        itemTouchHelper.attachToRecyclerView(datesRecyclerView);

        fab = findViewById(R.id.fab);

        datesList=db.getAllDates();
        Collections.reverse(datesList);
        datesAdapter.setDate(datesList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        datesList = db.getAllDates();
        Collections.reverse(datesList);
        datesAdapter.setDate(datesList);
        datesAdapter.notifyDataSetChanged();
    }


}