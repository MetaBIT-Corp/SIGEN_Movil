package com.example.crud_encuesta.Componentes_EL;

public class EstructuraTablas {
    //Modelo Escuela
    private int id;
    private String nombre;

    //Constantes de nombres de tabla Escuela
    public static final String ESCUELA_TABLA_NAME = "ESCUELA";
    public static final String COL_0 = "ID_ESCUELA";
    public static final String COL_1 = "ID_FACULTAD";
    public static final String COL_2 = "NOMBRE_ESCUELA";
    public static final String COL_3 = "CODIGO_ESCUELA";

    //Constantes de Tabla Carrera
    public static final String CARRERA_TABLA_NAME = "CARRERA";
    public static final String COL_0_CARRERA = "ID_CARRERA";
    public static final String COL_1_CARRERA = "ID_ESCUELA";
    public static final String COL_2_CARRERA = "NOMBRE_CARRERA";

    //Constantes de Tabla Materia
    public static final String MATERIA_TABLA_NAME = "CAT_MAT_MATERIA";
    public static final String COL_0_MATERIA = "ID_CAT_MAT";
    public static final String COL_1_MATERIA = "ID_PENUM";
    public static final String COL_2_MATERIA = "ID_CARRERA";
    public static final String COL_3_MATERIA = "CODIGO_MAT";
    public static final String COL_4_MATERIA = "NOMBRE_MAR";
    public static final String COL_5_MATERIA = "ES_ELECTIVA";
    public static final String COL_6_MATERIA = "MAXIMO_CANT_PREGUNTAS";

    //Constantes de Tabla Pensum
    public static final String PENSUM_TABLA_NAME = "PENSUM";
    public static final String COL_0_PENSUM = "ID_PENUM";
    public static final String COL_1_PENSUM = "ANIO_PENSUM";

    //Constantes de Tabla Encuesta
    public static final String ENCUESTA_TABLA_NAME = "ENCUESTA";
    public static final String COL_0_ENCUESTA = "ID_ENCUESTA";
    public static final String COL_1_ENCUESTA = "ID_PDG_DCN";
    public static final String COL_2_ENCUESTA = "TITULO_ENCUESTA";
    public static final String COL_3_ENCUESTA = "DESCRIPCION_ENCUESTA";
    public static final String COL_4_ENCUESTA = "FECHA_INICIO_ENCUESTA";
    public static final String COL_5_ENCUESTA = "FECHA_FINAL_ENCUESTA";

    //Constante de Tabla de Docente
    public static final String DOCENTE_TABLE_NAME = "PDG_DCN_DOCENTE";
    public static final String COL_0_DOCENTE = "ID_PDG_DCN";

    //Constantes de Tabla de Facultad
    public static final String FACULTAD_TABLE_NAME = "FACULTAD";
    public static final String COL_0_FACULTAD = "ID_FACULTAD";
    public static final String COL_1_FACULTAD = "NOMBRE_FACULTAD";

}
