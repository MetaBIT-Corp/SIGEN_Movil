package com.example.crud_encuesta.Componentes_MR.Encuesta;

public class Encuesta {

    private int
            id,
            id_docente,
            visible;

    private String
            titulo_encuesta,
            descripcion_encuesta,
            fecha_inicio_encuesta,
            fecha_final_encuesta,
            ruta;

    public Encuesta() {
    }

    public Encuesta(int id, int id_docente, String titulo_encuesta, String descripcion_encuesta, String fecha_inicio_encuesta, String fecha_final_encuesta, int visible, String ruta) {
        this.id = id;
        this.id_docente = id_docente;
        this.titulo_encuesta = titulo_encuesta;
        this.descripcion_encuesta = descripcion_encuesta;
        this.fecha_inicio_encuesta = fecha_inicio_encuesta;
        this.fecha_final_encuesta = fecha_final_encuesta;
        this.visible = visible;
        this.ruta = ruta;
    }

    public Encuesta(int id_docente, String titulo_encuesta, String descripcion_encuesta, String fecha_inicio_encuesta, String fecha_final_encuesta, int visible, String ruta) {
        this.id_docente = id_docente;
        this.titulo_encuesta = titulo_encuesta;
        this.descripcion_encuesta = descripcion_encuesta;
        this.fecha_inicio_encuesta = fecha_inicio_encuesta;
        this.fecha_final_encuesta = fecha_final_encuesta;
        this.visible = visible;
        this.ruta = ruta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_docente() {
        return id_docente;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public String getTitulo_encuesta() {
        return titulo_encuesta;
    }

    public void setTitulo_encuesta(String titulo_encuesta) {
        this.titulo_encuesta = titulo_encuesta;
    }

    public String getDescripcion_encuesta() {
        return descripcion_encuesta;
    }

    public void setDescripcion_encuesta(String descripcion_encuesta) {
        this.descripcion_encuesta = descripcion_encuesta;
    }

    public String getFecha_inicio_encuesta() {
        return fecha_inicio_encuesta;
    }

    public void setFecha_inicio_encuesta(String fecha_inicio_encuesta) {
        this.fecha_inicio_encuesta = fecha_inicio_encuesta;
    }

    public String getFecha_final_encuesta() {
        return fecha_final_encuesta;
    }

    public void setFecha_final_encuesta(String fecha_final_encuesta) {
        this.fecha_final_encuesta = fecha_final_encuesta;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

}
