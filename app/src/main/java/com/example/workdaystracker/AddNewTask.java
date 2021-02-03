package com.example.workdaystracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.workdaystracker.Model.DateModel;
import com.example.workdaystracker.Utils.DBHandler;
import com.example.workdaystracker.Adapter.DateAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.UserDataHandler;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newDateText;
    private Button newDateSaveButton;
    private DBHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_date,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newDateText = getView().findViewById(R.id.new_date_text);
        newDateSaveButton = getView().findViewById(R.id.newDateButton);

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle!=null){
            isUpdate=true;
            String date = bundle.getString("date");
            newDateText.setText(date);
            if(date.length()>0){
                newDateSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }
        }

        db = new DBHandler(getActivity());
        db.openDataBase();

        newDateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newDateSaveButton.setEnabled(false);
                    newDateSaveButton.setTextColor(Color.GRAY);
                } else {
                    newDateSaveButton.setEnabled(true);
                    newDateSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        boolean finalIsUpdate = isUpdate;
        newDateSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String date = newDateText.getText().toString();
                if(finalIsUpdate){
                    db.updateDate(bundle.getInt("id"), date);
                } else {
                    DateModel dateModel = new DateModel();
                    dateModel.setDate(date);
                    dateModel.setStatus(0);
                    db.insertDate(dateModel);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof  DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
