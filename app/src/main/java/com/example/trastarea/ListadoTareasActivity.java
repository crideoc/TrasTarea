package com.example.trastarea;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//4º IMPLEMENTAMOS INTERFACES DE COMUNICACION DEFINIDAS EN FRAGMENTOS
public class ListadoTareasActivity extends AppCompatActivity implements Serializable {


    RecyclerView recycler;
    RecyclerAdpater adaptador;
    ArrayList<Tarea> listaTareas;
    ArrayList<Tarea> listaTareasNoPrioritarios;
    TextView txtNoRespuestas;
    boolean prioritario=false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaTareas=new ArrayList<>();
        listaTareasNoPrioritarios=new ArrayList<>();

        setContentView(R.layout.activity_listado_tareas);
        txtNoRespuestas = findViewById(R.id.txtNoRespuestas);

        recycler = findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);


        cargarPorDefecto();
        recycler.setLayoutManager(manager);


        adaptador = new RecyclerAdpater(listaTareas, this);

        recycler.setAdapter(adaptador);

        //hara que la aplicacion reconozca el toque largo en la pantalla de listadoTareas
        registerForContextMenu(recycler);

        if (listaTareas.isEmpty()) {
            txtNoRespuestas.setVisibility(View.VISIBLE);
        }


    }


    //creamos el menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_superior, menu);
        return true;
    }

    //este metodo nos dice lo que hara cada opcion
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //recogemos el valor del item que sera el pulsado en menu
        int id = item.getItemId();


        if (id == R.id.MenuAcercaDe) {
            AlertDialog.Builder emergente = new AlertDialog.Builder(this);
            emergente.setTitle(R.string.app_name);
            emergente.setMessage(getResources().getString(R.string.centro) + ": "
                    + getResources().getString(R.string.instituto) + "\n" +
                    getResources().getString(R.string.autorProyecto) + ": "
                    + getResources().getString(R.string.autor) + "\n" +
                    //con esto obtenemos el año actual
                    getResources().getString(R.string.anioActual) + ":" + Calendar.getInstance().get(Calendar.YEAR));

            emergente.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            //mostramos la pantalla
            emergente.show();
        }

        if (id == R.id.MenuSalir) {
            Toast.makeText(this, R.string.hasta_pronto, Toast.LENGTH_LONG).show();
            try {
                //esperamos un segundo para salir del programa
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishAffinity();
        }


        if (id == R.id.MenuAniadir) {
            Intent accesoNuevaTarea = new Intent(this, CrearTareaActivity.class);
            accesoNuevaTarea.putExtra("opcion", "crear");
            milauncher.launch(accesoNuevaTarea);

        }

        if (id == R.id.MenuPrioritarios) {

            //comprobamos todos las tareas que tenemos y vamos comparando por prioridad

            listaTareasNoPrioritarios.clear();
            if (prioritario == true) {

                adaptador = new RecyclerAdpater(listaTareas, this);

                prioritario = false;

            } else {
                prioritario = true;

                for (Tarea t : listaTareas) {
                    if (t.getPrioritaria()) {
                        listaTareasNoPrioritarios.add(t);
                    }
                }
                adaptador = new RecyclerAdpater(listaTareasNoPrioritarios, this);

            }


            recycler.setAdapter(adaptador);

            //hara que la aplicacion reconozca el toque largo en la pantalla de listadoTareas
            registerForContextMenu(recycler);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Tarea tareaSeleccionada = adaptador.getTareaSeleccionada();

        if (id == R.id.mDescripcion) {
            AlertDialog.Builder emergente = new AlertDialog.Builder(this);
            emergente.setTitle(R.string.descripcion);
            emergente.setMessage(tareaSeleccionada.getDescripcion());

            emergente.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            //mostramos la pantalla
            emergente.show();

            Toast.makeText(this, tareaSeleccionada.getTitulo(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mEditar) {

            Intent accesoEditarTarea = new Intent(this, EditarTarea.class);
            accesoEditarTarea.putExtra("opcion", "editar");
            Bundle bundle = new Bundle();
            bundle.putParcelable("TareaAEditar", tareaSeleccionada);
            accesoEditarTarea.putExtras(bundle);
            milauncher.launch(accesoEditarTarea);

        } else if (id == R.id.mBorrar) {

            //eliminamos la tareaSeleccionada y volvemos a cargar nuestra vista
            listaTareas.remove(tareaSeleccionada);
            adaptador = new RecyclerAdpater(listaTareas, this);

            recycler.setAdapter(adaptador);
            registerForContextMenu(recycler);
            Toast.makeText(ListadoTareasActivity.this, "Tarea : " + tareaSeleccionada.getTitulo() + " eliminada con exito", Toast.LENGTH_SHORT).show();

        }

        return super.onContextItemSelected(item);


    }


    private void cargarPorDefecto() {
        //para los años tenemos que empezar a contar a partir de 1900 es decir que el valor año sera el año puesto mas 1900


        listaTareas.add(new Tarea("buenos dias", 0, new Date(2030 - 1900, 11, 2), new Date(2030 - 1900, 11, 2), 20, "Esto es una tarea precargada", false));
        listaTareas.add(new Tarea("buenas tardes", 0, new Date(2018 - 1900, 1, 30), new Date(2030 - 1900, 11, 2), 45, "Esto es una tarea precargada", true));
        listaTareas.add(new Tarea("buenas noches", 0, new Date(2020 - 1900, 9, 2), new Date(2030 - 1900, 11, 2), 60, "Esto es una tarea precargada", false));
        listaTareas.add(new Tarea("Hacer la compra", 0, new Date(2006 - 1900, 10, 14), new Date(2030 - 1900, 11, 2), 90, "descripcion", true));
        listaTareas.add(new Tarea("Hacer deporte", 0, new Date(2023 - 1900, 12, 23), new Date(2030 - 1900, 11, 2), 12, "descripcion", false));
        listaTareas.add(new Tarea("Recoger la casa", 0, new Date(2030 - 1900, 12, 1), new Date(2030 - 1900, 11, 2), 20, "descripcion", false));
        listaTareas.add(new Tarea("Programar", 0, new Date(2018 - 1900, 1, 30), new Date(2030 - 1900, 11, 2), 45, "descripcion", false));
        listaTareas.add(new Tarea("Jugar al lol", 0, new Date(2020 - 1900, 9, 2), new Date(2030 - 1900, 11, 2), 60, "", true));
        listaTareas.add(new Tarea("Estudiar", 0, new Date(2006 - 1900, 10, 14), new Date(2030 - 1900, 11, 2), 90, "descripcion", true));
        listaTareas.add(new Tarea("Ir al chino", 0, new Date(2023 - 1900, 12, 23), new Date(2030 - 1900, 11, 2), 12, "descripcion", false));

    }

    //recogemos los resultados de los intents
    ActivityResultLauncher<Intent> milauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (Objects.equals(result.getData().getExtras().getString("opcion"), "crear")) {

                            Tarea t = result.getData().getExtras().getParcelable("MiNuevaTarea");

                            listaTareas.add(t);
                            adaptador = new RecyclerAdpater(listaTareas, ListadoTareasActivity.this);
                            recycler.setAdapter(adaptador);
                            registerForContextMenu(recycler);

                            //controlamos que se muestre o no nuestro texto en el listado de tareas
                            if (listaTareas.isEmpty()) {
                                txtNoRespuestas.setVisibility(View.VISIBLE);
                            } else {
                                txtNoRespuestas.setVisibility(View.GONE);
                            }
                            //informacion para el usuario
                            Toast.makeText(ListadoTareasActivity.this, "Tarea : " + t.getTitulo() + " añadidad con exito", Toast.LENGTH_SHORT).show();
                        } else if (Objects.equals(result.getData().getExtras().getString("opcion"), "editar")) {

                            //obtenemos la posicion de la tarea seleccionada
                            int posicion = listaTareas.indexOf(adaptador.getTareaSeleccionada());
                            Tarea t = result.getData().getExtras().getParcelable("MiNuevaTarea");
                            listaTareas.set(posicion, t);

                            //nos actualiza el adaptador
                            adaptador.notifyDataSetChanged();
                            Toast.makeText(ListadoTareasActivity.this, "Tarea : " + t.getTitulo() + " actualizada con exito", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

    );

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("prioritario", prioritario);
        outState.putParcelableArrayList("lisdatoTareas",listaTareas);
        outState.putParcelableArrayList("listadoPrioritaria",listaTareasNoPrioritarios);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        prioritario = savedInstanceState.getBoolean("prioritario",true);
        listaTareas = savedInstanceState.getParcelableArrayList("lisdatoTareas");
        listaTareasNoPrioritarios = savedInstanceState.getParcelableArrayList("listadoPrioritaria");

        if(prioritario){
            adaptador = new RecyclerAdpater(listaTareasNoPrioritarios, this);
        }else{
            adaptador = new RecyclerAdpater(listaTareas, this);
        }
        recycler.setAdapter(adaptador);


        if (listaTareas.isEmpty()) {
            txtNoRespuestas.setVisibility(View.VISIBLE);
        } else {
            txtNoRespuestas.setVisibility(View.GONE);
        }
    }
}

