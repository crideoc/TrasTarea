package com.example.trastarea;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class FragmentoNuevaTarea2 extends Fragment {

    private CompartirViewModel compartirViewModel;
    Button btnVolver;
    Button btnGuardar;
    EditText txtDescripcion;
    String titulo, progreso, descripcion;
    int progresoNum;
    boolean prioritaria;
    Date fechaIni, fechaFinish;
    int dias;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Tarea tarea;

    public FragmentoNuevaTarea2() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //instanciamos nuestra clase CompartirViewModel
        compartirViewModel = new ViewModelProvider(requireActivity()).get(CompartirViewModel.class);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //esta vista sera la que usaremos a la hora de
        // hacer cualquier cosa dentro de FragmentoNuevoTarea2
        View vistaApoyo = inflater.inflate(R.layout.fragment_fragmento_nueva_tarea2, container, false);

        txtDescripcion = vistaApoyo.findViewById(R.id.txtDescripcion);
        btnVolver = vistaApoyo.findViewById(R.id.btnVolver);
        btnGuardar = vistaApoyo.findViewById(R.id.btnGuardar);

        btnVolver.setOnClickListener(v -> {
            compartirViewModel.setDescripcion(txtDescripcion.getText().toString());
            getFragmentManager().popBackStack();
        });

        btnGuardar.setOnClickListener(v -> {

            CargadorValores(compartirViewModel);
            //Aqui vamos a crear nuestra nueva tarea
            tarea = new Tarea(titulo, dias, fechaFinish,fechaIni, progresoNum, descripcion, prioritaria);
            comunicador.agregarTarea(tarea);

        });

        // Inflate the layout for this fragment
        return vistaApoyo;
    }

    private void CargadorValores(CompartirViewModel compartirViewModel) {
        //Aqui vamos a obtner los valores de nuestro CompartirViewmodel
        //que son los mismos datos que hemos introducido desde FragmentoNuevaTarea
        titulo = compartirViewModel.getTitulo().getValue();
        fechaIni = compartirViewModel.getFechaInicio().getValue();
        fechaFinish = compartirViewModel.getFechaFin().getValue();
        progreso = compartirViewModel.getProgreso().getValue();
        descripcion = compartirViewModel.getDescripcion().getValue();
        prioritaria = compartirViewModel.getPrioritaria().getValue();

        //comprobamos si descripcion esta vacio o no
        if (!txtDescripcion.getText().toString().isEmpty()) {
            descripcion = txtDescripcion.getText().toString();
        }


        //vamos a comprobar el progreso y dependiendo de la opcion
        // elegida en FragmentoNuevaTarea tendra un valor o otro
        if (progreso.equals(getString(R.string.no_iniciada))) {
            progresoNum = 0;
        } else if (progreso.equals(getString(R.string.iniciada))) {
            progresoNum = 25;
        } else if (progreso.equals(getString(R.string.avanzada))) {
            progresoNum = 50;
        } else if (progreso.equals(getString(R.string.casi_finalizada))) {
            progresoNum = 75;
        } else {
            progresoNum = 100;
        }

        Date fechaActual = new Date();

        long diferenciaEnMilisegundos = fechaFinish.getTime() - fechaActual.getTime();
        long diasDiferencia = TimeUnit.DAYS.convert(diferenciaEnMilisegundos, TimeUnit.MILLISECONDS);
        dias = (int) diasDiferencia;

        Log.i("MostrarDatos", dias + "");
    }


    //1º paso creamos una interfaz
    public interface ComunicacionFA {

        void agregarTarea(Tarea tarea);
    }

    //2º DEFINIMOS OBJETO CON INTERFAZ DE COMUNICACION
    private ComunicacionFA comunicador;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicacionFA) {
            comunicador = (ComunicacionFA) context;
        } else {
            throw new ClassCastException(context + " falta implmentar la comunicacion");
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("fragmento2","2");
    }
}