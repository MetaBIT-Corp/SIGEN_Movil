package com.example.crud_encuesta.Componentes_EL;

import com.example.crud_encuesta.Componentes_MR.Docente.Docente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.crud_encuesta.Componentes_EL.Carrera.Carrera;
import com.example.crud_encuesta.Componentes_EL.Encuesta.Encuesta;
import com.example.crud_encuesta.Componentes_EL.Escuela.Escuela;
import com.example.crud_encuesta.Componentes_EL.Materia.Materia;
import com.example.crud_encuesta.Componentes_EL.ModelosAdicionales.Facultad;
import com.example.crud_encuesta.Componentes_EL.ModelosAdicionales.Pensum;
import com.example.crud_encuesta.R;

import java.util.ArrayList;

public class Operaciones_CRUD {

    public static final Toast insertar(SQLiteDatabase db, ContentValues c, Context context, String table_name) {
        try {
            db.insert(table_name, null, c);
            return Toast.makeText(context, R.string.men_guardar, Toast.LENGTH_SHORT);

        } catch (Exception e) {
            return Toast.makeText(context, "Error", Toast.LENGTH_SHORT);
        }
    }

    public static void eliminar(final SQLiteDatabase db, final Context context, final String table_name, String column_name, int id) {
        final String condicion = column_name + " = ?";
        final String[] argumentos = {"" + id};
        db.delete(table_name, condicion, argumentos);
        Toast.makeText(context, R.string.men_eliminar, Toast.LENGTH_SHORT).show();
    }

    public static final Toast actualizar(SQLiteDatabase db, ContentValues c, Context context, String table_name, String column_name, int id) {
        try {
            final String condicion = column_name + " = ?";
            final String[] argumentos = {"" + id};
            db.update(table_name, c, condicion, argumentos);
            return Toast.makeText(context, R.string.men_actualizar, Toast.LENGTH_SHORT);

        } catch (Exception e) {
            return Toast.makeText(context, "Error", Toast.LENGTH_SHORT);
        }
    }

    public static ArrayList<Escuela> todosEscuela(String table_name, SQLiteDatabase db) {
        ArrayList<Escuela> lista = new ArrayList<>();
        Cursor c = db.rawQuery(" SELECT * FROM " + table_name, null);
        if (c.moveToFirst()) {
            c.moveToFirst();
            Escuela e;
            do {
                e = new Escuela();
                e.setId(c.getInt(0));
                e.setFacultad(c.getInt(1));
                e.setNombre(c.getString(2));
                e.setCod(c.getString(3));
                lista.add(e);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Escuela> todosEscuela(String table_name,SQLiteDatabase db, String parametro){
        ArrayList<Escuela> lista = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT *FROM "+table_name+" WHERE "+EstructuraTablas.COL_2+" LIKE '%"+parametro+"%'", null);

        if (c.moveToFirst()) {
            c.moveToFirst();
            Escuela e;
            do {
                e = new Escuela();
                e.setId(c.getInt(0));
                e.setFacultad(c.getInt(1));
                e.setNombre(c.getString(2));
                e.setCod(c.getString(3));
                lista.add(e);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Facultad> todosFacultad(SQLiteDatabase db){
        ArrayList<Facultad> lista = new ArrayList<>();
        Cursor cu = db.rawQuery(" SELECT * FROM " + EstructuraTablas.FACULTAD_TABLE_NAME, null);

        if (cu.moveToFirst()) {
            Facultad f;
            do {
                f=new Facultad();
                f.setId(cu.getInt(0));
                f.setNombre(cu.getString(1));
                lista.add(f);
            }while (cu.moveToNext());
        }

        return lista;
    }

    public static ArrayList<Carrera> todosCarrera(SQLiteDatabase db, ArrayList<Escuela> escuelas) {
        ArrayList<Carrera> lista = new ArrayList<>();
        Cursor cu = db.rawQuery(" SELECT * FROM " + EstructuraTablas.CARRERA_TABLA_NAME, null);
        if (cu.moveToFirst()) {
            Carrera c;
            do {
                c = new Carrera();
                c.setId(cu.getInt(0));
                for (int i = 0; i < escuelas.size(); i++) {
                    if (cu.getInt(1) == escuelas.get(i).getId()) c.setEscuela(escuelas.get(i));
                }
                c.setNombre(cu.getString(2));
                lista.add(c);
            } while (cu.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Carrera> todosCarrera(SQLiteDatabase db, ArrayList<Escuela> escuelas,String parametro) {
        ArrayList<Carrera> lista = new ArrayList<>();
        Cursor cu = db.rawQuery("SELECT *FROM "+EstructuraTablas.CARRERA_TABLA_NAME+" WHERE "+EstructuraTablas.COL_2_CARRERA+" LIKE '%"+parametro+"%'", null);
        if (cu.moveToFirst()) {
            Carrera c;
            do {
                c = new Carrera();
                c.setId(cu.getInt(0));
                for (int i = 0; i < escuelas.size(); i++) {
                    if (cu.getInt(1) == escuelas.get(i).getId()) c.setEscuela(escuelas.get(i));
                }
                c.setNombre(cu.getString(2));
                lista.add(c);
            } while (cu.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Pensum> todosPensum(SQLiteDatabase db) {
        ArrayList<Pensum> lista = new ArrayList<>();
        Cursor cu = db.rawQuery(" SELECT * FROM " + EstructuraTablas.PENSUM_TABLA_NAME, null);
        if (cu.moveToFirst()) {
            Pensum p;
            do {
                p = new Pensum();
                p.setId(cu.getInt(0));
                p.setAño(cu.getInt(1));
                lista.add(p);
            } while (cu.moveToNext());

        }
        return lista;
    }

    public static ArrayList<Materia> todosMateria(SQLiteDatabase db) {
        ArrayList<Materia> lista = new ArrayList<>();
        Cursor cu = db.rawQuery(" SELECT * FROM " + EstructuraTablas.MATERIA_TABLA_NAME, null);

        if (cu.moveToFirst()) {
            Materia m;
            do {
                m = new Materia();
                m.setId(cu.getInt(0));
                m.setCodigo_materia(cu.getString(1));
                m.setNombre(cu.getString(2));
                m.setElectiva(cu.getInt(3));
                m.setMaximo_preguntas(cu.getInt(4));
                lista.add(m);
            }while (cu.moveToNext());
        }

        return lista;
    }

    public static ArrayList<Materia> todosMateria(SQLiteDatabase db,String parametro) {
        ArrayList<Materia> lista = new ArrayList<>();
        Cursor cu = db.rawQuery("SELECT *FROM "+EstructuraTablas.MATERIA_TABLA_NAME+
                " WHERE "+EstructuraTablas.COL_4_MATERIA+" " +
                "LIKE '%"+parametro+"%'",null);

        if (cu.moveToFirst()) {
            Materia m;
            do {
                m = new Materia();
                m.setId(cu.getInt(0));
                m.setCodigo_materia(cu.getString(1));
                m.setNombre(cu.getString(2));
                m.setElectiva(cu.getInt(3));
                m.setMaximo_preguntas(cu.getInt(4));
                lista.add(m);
            }while (cu.moveToNext());
        }

        return lista;
    }

    public static ArrayList<Materia> todosMateriaSpeech(SQLiteDatabase db,String[] parametro) {
        String where = " WHERE " +EstructuraTablas.COL_4_MATERIA+ " LIKE '%";
        String where2 = "%' OR " +EstructuraTablas.COL_4_MATERIA+ " LIKE '%";
        ArrayList<Materia> lista = new ArrayList<>();
        Cursor cu = db.rawQuery("SELECT * FROM "+EstructuraTablas.MATERIA_TABLA_NAME+
                where + TextUtils.join(where2, parametro),
                null);

        if (cu.moveToFirst()) {
            Materia m;
            do {
                m = new Materia();
                m.setId(cu.getInt(0));
                m.setCodigo_materia(cu.getString(1));
                m.setNombre(cu.getString(2));
                m.setElectiva(cu.getInt(3));
                m.setMaximo_preguntas(cu.getInt(4));
                lista.add(m);
            }while (cu.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Encuesta> todosEncuestaSpeech(SQLiteDatabase db,ArrayList<Docente> d,String[] parametro) {
        String where = " WHERE " +EstructuraTablas.COL_2_ENCUESTA+ " LIKE '%";
        String where2 = "%' OR " +EstructuraTablas.COL_2_ENCUESTA+ " LIKE '%";
        ArrayList<Encuesta> lista = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+EstructuraTablas.ENCUESTA_TABLA_NAME+
                        where + TextUtils.join(where2, parametro),
                null);

        if (c.moveToFirst()) {
            Encuesta e;
            do {
                e = new Encuesta();
                e.setId(c.getInt(0));
                for (int i = 0; i < d.size(); i++) {
                    if (c.getInt(1) == d.get(i).getId()) e.setId_docente(d.get(i));
                }
                e.setTitulo(c.getString(2));
                e.setDescripcion(c.getString(3));
                e.setFecha_in(c.getString(4));
                e.setFecha_fin(c.getString(5));
                lista.add(e);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Materia> todosMateria(SQLiteDatabase db,int rol, int id) {
        ArrayList<Materia> lista = new ArrayList<>();
        Cursor cu = null;
        cu=db.rawQuery("Select * from cat_mat_materia",null);
        if (cu.moveToFirst()) {
            Materia m;
            do {
                m = new Materia();
                m.setId(cu.getInt(0));
                m.setCodigo_materia(cu.getString(1));
                m.setNombre(cu.getString(2));
                m.setElectiva(cu.getInt(3));
                m.setMaximo_preguntas(cu.getInt(4));
                lista.add(m);
            }while (cu.moveToNext());
        }

        return lista;
    }

    public static ArrayList<Encuesta> todosEncuesta(SQLiteDatabase db, ArrayList<Docente> d) {
        ArrayList<Encuesta> lista = new ArrayList<>();
        Cursor c = db.rawQuery(" SELECT * FROM " + EstructuraTablas.ENCUESTA_TABLA_NAME, null);

        if (c.moveToFirst()) {
            Encuesta e;
            do {
                e = new Encuesta();
                e.setId(c.getInt(0));
                for (int i = 0; i < d.size(); i++) {
                    if (c.getInt(1) == d.get(i).getId()) e.setId_docente(d.get(i));
                }
                e.setTitulo(c.getString(2));
                e.setDescripcion(c.getString(3));
                e.setFecha_in(c.getString(4));
                e.setFecha_fin(c.getString(5));
                lista.add(e);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Encuesta> todosEncuesta(SQLiteDatabase db, ArrayList<Docente> d,String parametro) {
        ArrayList<Encuesta> lista = new ArrayList<>();
        Cursor c= db.rawQuery("SELECT *FROM "+EstructuraTablas.ENCUESTA_TABLA_NAME+" WHERE "+EstructuraTablas.COL_2_ENCUESTA+" LIKE '%"+parametro+"%'",null);

        if (c.moveToFirst()) {
            Encuesta e;
            do {
                e = new Encuesta();
                e.setId(c.getInt(0));
                for (int i = 0; i < d.size(); i++) {
                    if (c.getInt(1) == d.get(i).getId()) e.setId_docente(d.get(i));
                }
                e.setTitulo(c.getString(2));
                e.setDescripcion(c.getString(3));
                e.setFecha_in(c.getString(4));
                e.setFecha_fin(c.getString(5));
                lista.add(e);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Encuesta> todosEncuesta(SQLiteDatabase db, ArrayList<Docente> d, int id) {
        ArrayList<Encuesta> lista = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT\n" +
                "ENCUESTA.ID_ENCUESTA,\n" +
                "ENCUESTA.ID_PDG_DCN,\n" +
                "ENCUESTA.TITULO_ENCUESTA,\n" +
                "ENCUESTA.DESCRIPCION_ENCUESTA,\n" +
                "ENCUESTA.FECHA_INICIO_ENCUESTA,\n" +
                "ENCUESTA.FECHA_FINAL_ENCUESTA\n" +
                "FROM ENCUESTA\n" +
                "INNER JOIN PDG_DCN_DOCENTE ON\n" +
                "ENCUESTA.ID_PDG_DCN=PDG_DCN_DOCENTE.ID_PDG_DCN\n" +
                "WHERE PDG_DCN_DOCENTE.IDUSUARIO=" +id, null);

        if (c.moveToFirst()) {
            Encuesta e;
            do {
                e = new Encuesta();
                e.setId(c.getInt(0));
                for (int i = 0; i < d.size(); i++) {
                    if (c.getInt(1) == d.get(i).getId()) e.setId_docente(d.get(i));
                }
                e.setTitulo(c.getString(2));
                e.setDescripcion(c.getString(3));
                e.setFecha_in(c.getString(4));
                e.setFecha_fin(c.getString(5));
                lista.add(e);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static ArrayList<Docente> todosDocente(SQLiteDatabase db) {

        ArrayList<Docente> lista = new ArrayList<>();
        Cursor c = db.rawQuery(" SELECT * FROM " + EstructuraTablas.DOCENTE_TABLE_NAME, null);

        if (c.moveToFirst()) {
            Docente d;
            do {
                d = new Docente();
                d.setId(c.getInt(0));
                d.setId_escuela(c.getInt(1));
                d.setCarnet(c.getString(2));
                d.setAnio_titulo(c.getString(3));
                d.setActivo(c.getInt(4));
                d.setTipo_jornada(c.getInt(5));
                d.setDescripcion(c.getString(6));
                d.setCargo_actual(c.getInt(7));
                d.setCargo_secundario(c.getInt(8));
                d.setNombre(c.getString(9));
                //d.setIdUser(c.getInt(10));
                lista.add(d);
            } while (c.moveToNext());
        }
        return lista;
    }

    public static int docenteEncuesta(SQLiteDatabase db,int idusuario){
        Cursor c = db.rawQuery(" SELECT * FROM " + EstructuraTablas.DOCENTE_TABLE_NAME+" WHERE IDUSUARIO= "+idusuario, null);
        c.moveToFirst();
        return c.getInt(0);
    }
}
