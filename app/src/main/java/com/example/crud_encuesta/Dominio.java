package com.example.crud_encuesta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dominio {

    private static Dominio instance;
    private DatabaseAccess dba;
    private SQLiteDatabase cx;

    private Dominio(Context context){
        dba = DatabaseAccess.getInstance(context);
    }

    public static Dominio getInstance(Context context){
        if (instance == null){
            instance = new Dominio(context);
        }
        return instance;
    }

    public String getDominio(){
        String dominio = null;
        cx = dba.open();
        Cursor c = cx.rawQuery("SELECT DOMINIO,PUERTO FROM DOMINIO WHERE ID_DOMINIO=1", null);

        if(c.moveToFirst()){
            int puerto = c.getInt(c.getColumnIndex("PUERTO"));
            String name = c.getString(c.getColumnIndex("DOMINIO"));

            if(isDominioAvailable(name)){
                if (puerto == 443)
                    dominio = "https://" + name + ":" + puerto;
                else
                    dominio = "http://" + name + ":" + puerto;
            }else
                return null;
        }

        //dba.close();

        return dominio;
    }

    public String getName(){
        String name = null;

        cx = dba.open();
        Cursor c = cx.rawQuery("SELECT DOMINIO FROM DOMINIO WHERE ID_DOMINIO=1", null);

        if(c.moveToFirst()){
            name = c.getString(c.getColumnIndex("DOMINIO"));
        }

        dba.close();

        return name;
    }

    public int getPort(){
        int port = 0;

        cx = dba.open();
        Cursor c = cx.rawQuery("SELECT PUERTO FROM DOMINIO WHERE ID_DOMINIO=1", null);

        if(c.moveToFirst()){
            port = c.getInt(c.getColumnIndex("PUERTO"));
        }

        dba.close();

        return port;
    }

    public void setName(String name){
        cx = dba.open();
        if(name != ""){
            ContentValues contenedor = new ContentValues();
            contenedor.put("DOMINIO", name);
            cx.update("DOMINIO", contenedor,"ID_DOMINIO=1", null);
        }
        dba.close();
    }

    public void setPort(int port){
        cx = dba.open();
        if(port > 0){
            ContentValues contenedor = new ContentValues();
            contenedor.put("PUERTO", port);
            cx.update("DOMINIO", contenedor,"ID_DOMINIO=1", null);
        }
        dba.close();
    }

    public boolean isDominioAvailable(String name) {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 " + name);
            return p.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
