package com.example.workdaystracker.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import com.example.workdaystracker.Model.DateModel;
import com.example.workdaystracker.R;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "WorkdaysDB";
    private static final String WORKDAYS_TABLE = "workdays";
    private static final String ID = "id";
    private static final String WORKDAY = "workday";
    private static final String STATUS = "status";
    private static final String CREATE_DATE_TABLE = "CREATE TABLE " + WORKDAYS_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  WORKDAY + " TEXT NOT NULL,"  +
            STATUS +  " INTEGER);";

    private SQLiteDatabase db;

    public DBHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the old tables
        db.execSQL("DROP TABLE IF EXISTS " + WORKDAYS_TABLE);
        //Create new table
        onCreate(db);
    }

    public void openDataBase(){
        db = this.getWritableDatabase();
    }

    public void insertDate(DateModel date){
        ContentValues cv = new ContentValues();
        cv.put(WORKDAY, date.getDate());
        cv.put(STATUS,0);
        db.insert(WORKDAYS_TABLE,null,cv);
        int totalDays = totalDaysWorked();
        int paidDays = totalDaysPaid();

    }

    public List<DateModel> getAllDates(){
        List<DateModel> datesList = new ArrayList<DateModel>();
        Cursor c = null;
        db.beginTransaction();
        try{
            c = db.query(WORKDAYS_TABLE, null, null, null, null, null, null,null);
            if(c!=null) {
                if(c.moveToFirst()){
                    do{
                        DateModel date = new DateModel();
                        date.setId(c.getInt(c.getColumnIndex(ID)));
                        date.setDate(c.getString(c.getColumnIndex(WORKDAY)));
                        date.setStatus((c.getInt(c.getColumnIndex(STATUS))));
                        datesList.add(date);
                    } while(c.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            c.close();
        }
        return  datesList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(WORKDAYS_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});

    }
    public void updateDate(int id, String date){
        ContentValues cv = new ContentValues();
        cv.put(WORKDAY, date);
        db.update(WORKDAYS_TABLE,cv, ID+"=?",new String[]{String.valueOf(id)});
    }

    public void deleteDate(int id){
        db.delete(WORKDAYS_TABLE,ID+"=?", new String[]{String.valueOf(id)});
    }

    public int totalDaysWorked(){
        return getAllDates().size();
    }


    public int totalDaysPaid(){
        int paidDays = 0;
        for(DateModel dm : getAllDates()){
            if(dm.getStatus()!=0){
                paidDays++;
            }
        }
        return paidDays;
    }
}
