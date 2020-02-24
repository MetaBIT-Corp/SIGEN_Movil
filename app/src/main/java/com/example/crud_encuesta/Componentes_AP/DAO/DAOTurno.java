package com.example.crud_encuesta.Componentes_AP.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.crud_encuesta.Componentes_AP.Models.Turno;
import com.example.crud_encuesta.DatabaseAccess;

import java.util.ArrayList;

public class DAOTurno {
    SQLiteDatabase baseDeDatos;
    ArrayList<Turno> turnos = new ArrayList<>();
    Turno turno;
    Context contexto;
    DatabaseAccess dba;
    String nombreBD= "proy_aplicacion";


    //constructor
    public DAOTurno(Context context){
        this.contexto = context;

        this.dba = DatabaseAccess.getInstance(context);
        baseDeDatos = this.dba.open();

        /*
         *Abrir base de datos
         * DatabaseAccess dba = DatabaseAccess.getInstance(context);
         * baseDeDatos = dba.open();
         *
         * */
    }


    public Boolean Insertar(Turno turno){
        baseDeDatos = this.dba.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_EVALUACION", turno.getIdEvaluacion());
        contentValues.put("FECHA_INICIO_TURNO",turno.getDateInicial());
        contentValues.put("FECHA_FINAL_TURNO",turno.getDateFinal());
        contentValues.put("VISIBILIDAD",turno.getVisible());
        contentValues.put("CONTRASENIA",turno.getContrasenia());

        return (baseDeDatos.insert("TURNO",null,contentValues)>0);
    }
    public Boolean InsertarWS(Turno turno){
        baseDeDatos = this.dba.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_TURNO", turno.getId());
        contentValues.put("ID_EVALUACION", turno.getIdEvaluacion());
        contentValues.put("FECHA_INICIO_TURNO",turno.getDateInicial());
        contentValues.put("FECHA_FINAL_TURNO",turno.getDateFinal());
        contentValues.put("VISIBILIDAD",turno.getVisible());
        contentValues.put("CONTRASENIA",turno.getContrasenia());

        return (baseDeDatos.insert("TURNO",null,contentValues)>0);
    }

    public Boolean Eliminar(Integer id){
        baseDeDatos = this.dba.open();
        return (baseDeDatos.delete("TURNO","ID_TURNO="+id,null)>0);
    }

    public Boolean Editar(Turno turno){
        baseDeDatos = this.dba.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_EVALUACION", turno.getIdEvaluacion());
        contentValues.put("FECHA_INICIO_TURNO",turno.getDateInicial());
        contentValues.put("FECHA_FINAL_TURNO",turno.getDateFinal());
        contentValues.put("VISIBILIDAD",turno.getVisible());
        contentValues.put("CONTRASENIA",turno.getContrasenia());

        return (baseDeDatos.update("TURNO",contentValues,"ID_TURNO =" + turno.getId(),null)>0);
    }

    public ArrayList<Turno> verTodos(Integer id_evaluacion){
        baseDeDatos = this.dba.open();
        turnos.clear(); //limpiamos lista del adapter
        Cursor cursor  = baseDeDatos.rawQuery("Select * FROM TURNO WHERE ID_EVALUACION =" +id_evaluacion,null);
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            do{
                turnos.add(new Turno(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5)
                ));

            }while (cursor.moveToNext());
        }
        return turnos;
    }

    //retorna la lista pero solo con el elemento buscado
    public ArrayList<Turno> verUno(int id, int id_evaluacion){
        baseDeDatos = this.dba.open();
        turnos.clear();
        Cursor cursor  = baseDeDatos.rawQuery("Select * FROM TURNO WHERE ID_TURNO = " + id + " AND ID_EVALUACION =" +id_evaluacion,
                null);
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            turnos.add(new Turno(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5)
            ));
        }
        return turnos;
    }

    public int getIdEstudiante(){
        baseDeDatos = this.dba.open();
        int idEstudiante= 0;
        Cursor cursor  = baseDeDatos.rawQuery("Select ID_EST FROM ESTUDIANTE ",
                null);
        if(cursor.moveToFirst()){
            idEstudiante=cursor.getInt(0);
        }
        return idEstudiante;
    }

    public int getIdIntentoTurno(int id_turno, int id_est){
        baseDeDatos = this.dba.open();
        int id_clave= 0;
        int id_intento = 0;
        Cursor cursorClave = baseDeDatos.rawQuery("Select ID_CLAVE FROM CLAVE WHERE ID_TURNO ="+ id_turno,
                null);
        if(cursorClave.moveToFirst()){
            id_clave = cursorClave.getInt(0);
        }

        Cursor cursorIntento  = baseDeDatos.rawQuery("Select ID_INTENTO FROM INTENTO WHERE ID_EST=" + id_est+ " AND ID_CLAVE = " + id_clave,
                null);
        if(cursorIntento.moveToFirst()){
            id_intento =cursorIntento.getInt(0);
        }
        return id_intento;
    }

    public Boolean PoseeTurnoDescargadoSinResponder(int id_intento){
        baseDeDatos = this.dba.open();
        Boolean poseeTurnoDescargado = false;

        Cursor cursorIntento = baseDeDatos.rawQuery("Select FECHA_FINAL_INTENTO FROM INTENTO WHERE ID_INTENTO ="+ id_intento,
                null);
        if(cursorIntento.getCount()>0){
            if(cursorIntento.moveToFirst()){
                if(cursorIntento.getString(0) == null){
                        poseeTurnoDescargado = true;
                }
            }
        }
        return poseeTurnoDescargado;
    }

    public int getNumeroIntento(int id_intento){
        baseDeDatos = this.dba.open();
        int numeroIntento = 0;

        Cursor cursorIntento = baseDeDatos.rawQuery("Select NUMERO_INTENTO FROM INTENTO WHERE ID_INTENTO ="+ id_intento,
                null);
        if(cursorIntento.getCount()>0){
            if(cursorIntento.moveToFirst()){
                numeroIntento = cursorIntento.getInt(0);
            }
        }
        return numeroIntento;
    }





}