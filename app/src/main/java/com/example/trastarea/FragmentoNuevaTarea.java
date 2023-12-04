package com.example.trastarea;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentoNuevaTarea extends Fragment {


    Tarea tareaAEditar;
    boolean editar=false;
    private CompartirViewModel compartirViewModel;
    FragmentoNuevaTarea2 fragmento2;
    Spinner spProgreso;
    EditText txtTitulo;
    EditText txtFechaCreacion;
    EditText txtFechaObjetivo;
    Button btnSiguienteFragmento;
    CheckBox cbPrioritario;

    int flag=0;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compartirViewModel=new ViewModelProvider(requireActivity()).get(CompartirViewModel.class);

        //comprobamos si nos llega algo a traves de un fragmentManager
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentByTag("fragmentoEditar");

        if(fragment !=null){
            tareaAEditar=getArguments().getParcelable("TareaAEditar");
            editar=true;
        }

    }

    public static FragmentoNuevaTarea newInstance() {
        return new FragmentoNuevaTarea();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vistaApoyo=inflater.inflate(R.layout.fragment_fragmento_crear_tarea, container, false);


        spProgreso=(Spinner)vistaApoyo.findViewById(R.id.spProgreso);
        txtFechaCreacion =vistaApoyo.findViewById(R.id.txtFechaCreacion);
        txtFechaObjetivo =vistaApoyo.findViewById(R.id.txtFechaObjetivo);
        txtTitulo=vistaApoyo.findViewById(R.id.txtTitulo);
        btnSiguienteFragmento=vistaApoyo.findViewById(R.id.btnSiguienteFragment);
        cbPrioritario=vistaApoyo.findViewById(R.id.cbPrioritario);

        //creacion de las opciones de nuestro spinner
        ArrayList<String> opciones=new ArrayList<>();

        opciones.add(getResources().getString(R.string.no_iniciada));
        opciones.add(getResources().getString(R.string.iniciada));
        opciones.add(getResources().getString(R.string.avanzada));
        opciones.add(getResources().getString(R.string.casi_finalizada));
        opciones.add(getResources().getString(R.string.finalizada));

        ArrayAdapter adapter=new ArrayAdapter(vistaApoyo.getContext(), android.R.layout.simple_list_item_1,opciones);

        spProgreso.setAdapter(adapter);


        //cargamos los datos de nuestra tareaAEditar para trabajar con esos datos desde la vista
        if(editar){
            spProgreso.setSelection(obtenerIndice(spProgreso,String.valueOf(tareaAEditar.getProgreso())));
            txtFechaCreacion.setText(sdf.format(tareaAEditar.getFechaInicio()));
            txtFechaObjetivo.setText(sdf.format(tareaAEditar.getFecha()));
            txtTitulo.setText(tareaAEditar.getTitulo());
            cbPrioritario.setChecked(tareaAEditar.getPrioritaria());
        }

        if(flag==1){
            CargarDatos(adapter);
        }



        //enviamos los valores seleccionados al ViewModel, en este caso se llama compartirViewModel
        btnSiguienteFragmento.setOnClickListener(View->{
            boolean checkado=false;

            flag++;
            //comprobamos si se han introducido datos
            if(txtTitulo.getText().toString().equals("") ||
                txtFechaCreacion.getText().toString().equals("")||
                txtFechaObjetivo.getText().toString().equals("")){

                AlertDialog.Builder emergente=new AlertDialog.Builder(vistaApoyo.getContext());
                emergente.setTitle(R.string.errorDatos);
                emergente.setMessage(R.string.rellenarDatos);
                emergente.setIcon(R.drawable.baseline_error_outline_24);
                emergente.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                //mostramos la pantalla
                emergente.show();
            }else{
                //damos valor a titulo en el viewModel
                Date fechaini,fechafin;
                try {
                    fechaini=sdf.parse(txtFechaCreacion.getText().toString());
                    fechafin=sdf.parse(txtFechaObjetivo.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                compartirViewModel.setTitulo(txtTitulo.getText().toString());//titulo
                compartirViewModel.setFechaInicio(fechaini);//fecha creacion
                compartirViewModel.setFechaFin(fechafin);//fecha fin
                compartirViewModel.setProgreso(spProgreso.getSelectedItem().toString());//progreso

                if(cbPrioritario.isChecked()){
                    checkado=true;
                }
                compartirViewModel.setPrioritaria(checkado);//prioridad

                //creamos un fragmento nuevo y hacemos que aparezca en el contenedorFragmentos
                fragmento2=new FragmentoNuevaTarea2();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.contenedorFragmentos,fragmento2);
                //esto es para que el fragmento se guarde en la pila de nuestro fragment Manager
                //y poder volver a este fragmento
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //esto es para ver los datos que se enviando desde el Logcat
                Log.i("EnvioDatos",txtTitulo.getText().toString());
                Log.i("EnvioDatos",txtFechaCreacion.getText().toString());
                Log.i("EnvioDatos",txtFechaObjetivo.getText().toString());
                Log.i("EnvioDatos",spProgreso.getSelectedItem().toString());
                Log.i("EnvioDatos",checkado+"");

            }

        });


        txtFechaObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDatePicker(view);
            }
        });


        txtFechaCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                llamarDatePicker(view);

            }
        });

        // Inflate the layout for this fragment
        return vistaApoyo;
    }

    private int obtenerIndice(Spinner spProgreso, String progreso) {
        for (int i = 0; i < spProgreso.getCount(); i++) {
            if (spProgreso.getItemAtPosition(i).toString().equalsIgnoreCase(progreso)){
                return i;
            }
        }
        return 0;
    }

    private void CargarDatos(ArrayAdapter adapter) {
        //leemos los valores guardados en el ViewModel
        txtTitulo.setText(compartirViewModel.getTitulo().getValue());
        txtFechaObjetivo.setText(compartirViewModel.getFechaFin().getValue().toString());
        txtFechaCreacion.setText(compartirViewModel.getFechaInicio().getValue().toString());
        compartirViewModel.getProgreso().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int posicion=adapter.getPosition(s);
                if(posicion!=-1){
                    spProgreso.setSelection(posicion);
                }
            }
        });

        if(compartirViewModel.getPrioritaria().getValue()!=null){
            cbPrioritario.setChecked(true);
        }else{
            cbPrioritario.setChecked(false);
        }


    }

    private void llamarDatePicker(View v) {
        DatePickerFragment newFragment=DatePickerFragment.nuevoCalendario(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int dia, int mes, int anio) {

                int id=v.getId();

                final String diaSeleccionado=dia+"/"+(mes+1)+"/"+anio;

                if(id==R.id.txtFechaCreacion){
                    txtFechaCreacion.setText(diaSeleccionado);
                }else if(id==R.id.txtFechaObjetivo){
                    txtFechaObjetivo.setText(diaSeleccionado);
                }


            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(),"datePicker");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("fragmento1","1");
    }



}