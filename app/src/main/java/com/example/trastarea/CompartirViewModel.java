package com.example.trastarea;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class CompartirViewModel extends ViewModel {

    private final MutableLiveData<String>titulo=new MutableLiveData<>();
    private final MutableLiveData<java.util.Date>fechaInicio=new MutableLiveData<>();
    private final MutableLiveData<java.util.Date>fechaFin=new MutableLiveData<>();
    private final MutableLiveData<String>progreso=new MutableLiveData<>();
    private final MutableLiveData<String>descripcion=new MutableLiveData<>();
    private final MutableLiveData<Boolean>prioritaria=new MutableLiveData<>();

    public void setTitulo(String titu){
        titulo.setValue(titu);
    }

    public void setFechaInicio(Date fechaIni){
        fechaInicio.setValue(fechaIni);
    }

    public void setFechaFin( Date fechaF){
        fechaFin.setValue(fechaF);
    }

    public void setProgreso(String progres){
        progreso.setValue(progres);
    }
    public void setDescripcion(String descrip){
        descripcion.setValue(descrip);
    }

    public void setPrioritaria(Boolean prio){
        prioritaria.setValue(prio);
    }

    public MutableLiveData<String> getTitulo() {
        return titulo;
    }

    public MutableLiveData<Date> getFechaInicio() {
        return fechaInicio;
    }

    public MutableLiveData<Date> getFechaFin() {
        return fechaFin;
    }
    public MutableLiveData<String> getDescripcion() {
        return descripcion;
    }

    public MutableLiveData<String> getProgreso() {
        return progreso;
    }

    public MutableLiveData<Boolean> getPrioritaria() {
        return prioritaria;
    }
}
