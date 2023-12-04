package com.example.trastarea;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

public class RecyclerAdpater extends RecyclerView.Adapter<RecyclerAdpater.RecyclerHolder> {

    //lista que guardara cada una de las tareas
    private List<Tarea> listaTareas;

    private Context contexto;

    private Tarea tareaSeleccionada;

    //damos un formato para poder usar la fecha
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private String fechaFormateada;

    public RecyclerAdpater(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //creamos un view el cual contenga nuestra tarea_layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_layout, parent, false);
        return new RecyclerHolder(view);
    }

    //este metodo se repetira tantas veces como numero que devuelva getItemCount()
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        holder.titulo.setText(tarea.getTitulo());

        //vuelvo a calcular los dias para obtener los dias de los cargados por defecto
        long diasRestantes = (long) Math.floor((tarea.getFecha().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24) + 1);
        if (diasRestantes < 0) {
            holder.dias.setTextColor(Color.RED);
        }
        holder.dias.setText(diasRestantes + "");
        //obtenemos la fecha mediante un formateo
        fechaFormateada = sdf.format(tarea.getFecha());
        holder.fecha.setText(fechaFormateada);
        holder.progreso.setProgress(tarea.getProgreso());

        //si la tarea es prioritaria le cambiamos el icono
        if (tarea.getPrioritaria()) {
            holder.titulo.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.star_big_on, 0, 0, 0);
        }

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                //creamos una instancia de Resources para poder acceder a nuestro string.xml
                Resources resources = holder.itemView.getResources();
                MenuInflater inflater = new MenuInflater(contexto);
                inflater.inflate(R.menu.menu_contextual, contextMenu);

                tareaSeleccionada = listaTareas.get(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        //nos devolvera el tamaño de la lista

        return listaTareas.size();

    }

    public RecyclerAdpater(List<Tarea> listaTareas, Context contexto) {
        this.listaTareas = listaTareas;
        this.contexto = contexto;
    }

    public Tarea getTareaSeleccionada() {
        return tareaSeleccionada;
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView dias;
        private TextView fecha;
        private ProgressBar progreso;

        private TextView titulo;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
            dias = itemView.findViewById(R.id.txtDias);
            fecha = itemView.findViewById(R.id.txtFecha);
            progreso = itemView.findViewById(R.id.progressBar);

        }


    }
}
