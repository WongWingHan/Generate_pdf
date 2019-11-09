package com.example.wongw.generate_pdf;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateFragment2 extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year2 = c.get(Calendar.YEAR);
        int month2 = c.get(Calendar.MONTH);
        int day2 = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getContext(),this,year2,month2,day2);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year2, int month2, int day2) {
        MainActivity ma = (MainActivity) getActivity();
        ma.setDate2(year2,month2,day2);
    }
}
