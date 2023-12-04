package com.example.trastarea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CrearTareaActivity extends AppCompatActivity implements FragmentoNuevaTarea2.ComunicacionFA{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        //cambiamos el titulo que aparece en la parte superior de la ventana
        this.setTitle(getResources().getString(R.string.crear_tarea));

        //llamamos al fragmentNuevaTarea y lo mostramos en el activity
        Fragment fragment=new FragmentoNuevaTarea();
        FragmentManager manejadorFragmentos=getSupportFragmentManager();
        manejadorFragmentos.beginTransaction().replace(R.id.contenedorFragmentos,fragment).commit();

    }

    @Override
    public void agregarTarea(Tarea tareaRecibida) {

        //obtenemos el objeto tareaRecibida
        Intent intento=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("opcion","crear");
        bundle.putParcelable("MiNuevaTarea",tareaRecibida);
        intento.putExtras(bundle);
        setResult(RESULT_OK,intento);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

       
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }
}