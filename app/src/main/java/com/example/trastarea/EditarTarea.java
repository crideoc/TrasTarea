package com.example.trastarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class EditarTarea extends AppCompatActivity implements FragmentoNuevaTarea2.ComunicacionFA{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        //cambiamos el titulo que aparece en la parte superior de la ventana
        this.setTitle(getResources().getString(R.string.editar_tarea));

        //recogemos la tarea que enviamos desde el activity ListadoTareas
        Tarea tarea=getIntent().getExtras().getParcelable("TareaAEditar");

        //creamos un bunlde para guardar tarea y enviarselo a nuestro fragmento
        Bundle bundle=new Bundle();
        bundle.putParcelable("TareaAEditar",tarea);

        //llamamos al fragmentNuevaTarea y lo mostramos en el activity
        Fragment fragment=new FragmentoNuevaTarea();

        //mandamos el bundle creado con tarea al fragmento
        fragment.setArguments(bundle);
        FragmentManager manejadorFragmentos=getSupportFragmentManager();
        manejadorFragmentos.beginTransaction().replace(R.id.contenedorFragmentos,fragment,"fragmentoEditar").commit();
    }


    @Override
    public void agregarTarea(Tarea tareaRecibida) {

        //obtenemos el objeto tareaRecibida
        Intent intento=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("opcion","editar");
        bundle.putParcelable("MiNuevaTarea",tareaRecibida);
        intento.putExtras(bundle);
        setResult(RESULT_OK,intento);
        finish();
    }
}