package com.example.trastarea;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Tarea implements Parcelable {

    //private Date dias;
    private int dias;
    private Date fecha;
    private Date fechaInicio;
    private int progreso;
    private String titulo;
    private String descripcion;
    private boolean prioritaria;

    public Tarea(String titulo, int dias, Date fecha,Date fechaInicio, int progreso, String descripcion,boolean prioritaria) {
        this.titulo=titulo;
        this.dias = dias;
        this.fecha = fecha;
        this.fechaInicio=fechaInicio;
        this.progreso = progreso;
        this.descripcion = descripcion;
        this.prioritaria=prioritaria;
    }

    protected Tarea(Parcel in) {
        titulo = in.readString();
        dias=in.readInt();
        long tmpFecha=in.readLong();
        fecha=tmpFecha!=-1?new Date(tmpFecha):null;
        long tmpFechaInicio=in.readLong();
        fechaInicio=tmpFechaInicio!=-1?new Date(tmpFechaInicio):null;
        progreso = in.readInt();
        descripcion = in.readString();
        prioritaria=in.readBoolean();
    }

    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };

    public boolean getPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeInt(dias);
        dest.writeLong(fecha!=null?fecha.getTime():-1L);
        dest.writeLong(fechaInicio!=null?fechaInicio.getTime():-1L);
        dest.writeInt(progreso);
        dest.writeString(descripcion);
        dest.writeBoolean(prioritaria);

    }
}
