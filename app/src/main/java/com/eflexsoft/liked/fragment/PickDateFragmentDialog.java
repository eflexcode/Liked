package com.eflexsoft.liked.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class PickDateFragmentDialog extends DialogFragment {

    int age;

    public PickDateFragmentDialog(int age) {

        this.age = age;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR) - age;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}
