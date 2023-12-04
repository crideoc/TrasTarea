package com.example.trastarea;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener escuchadorCalendario;

    public static DatePickerFragment nuevoCalendario(DatePickerDialog.OnDateSetListener escuchadorCalendario){
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setListener(escuchadorCalendario);
        return fragment;
    }

    private void setListener(DatePickerDialog.OnDateSetListener escuchadorCalendario) {
        this.escuchadorCalendario=escuchadorCalendario;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c=Calendar.getInstance();
        int anio=c.get(Calendar.YEAR);
        int mes=c.get(Calendar.MONTH);
        int dia=c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),escuchadorCalendario,anio,mes,dia);
    }
}
