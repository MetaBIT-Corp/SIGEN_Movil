package com.example.crud_encuesta.Componentes_EL.ModelosAdicionales;

public class Pensum {
    private int id;
    private int año;

    public Pensum() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String toString(){
        return año+"";
    }
}
