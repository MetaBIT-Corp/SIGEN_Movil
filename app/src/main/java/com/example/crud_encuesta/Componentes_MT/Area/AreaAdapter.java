package com.example.crud_encuesta.Componentes_MT.Area;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crud_encuesta.Componentes_DC.Activities.GpoEmpActivity;
import com.example.crud_encuesta.DatabaseAccess;
import com.example.crud_encuesta.R;

import java.util.ArrayList;
import java.util.List;

public class AreaAdapter extends BaseAdapter{
    private LayoutInflater inflater = null;

    Context context;
    List<Area> areas = new ArrayList<>();
    DAOArea daoArea;
    int id_materia;
    int id_docente;
    Area area;
    int[] iconos;
    Activity a;
    String[] modalidad = {"Opcion multiple", "Verdader/Falso", "Emparejamiento", "Respuesta corta"};

    public AreaAdapter(Context context, List<Area> areas, DAOArea daoArea, int id_materia, int id_docente, int[] iconos, Activity a){
        this.context = context;
        this.areas = areas;
        this.daoArea = daoArea;
        this.id_materia = id_materia;
        this.id_docente = id_docente;
        this.iconos = iconos;
        this.a = a;

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View mView = inflater.inflate(R.layout.elemento_list_area, null);
        TextView tituloArea = (TextView)mView.findViewById(R.id.elemento_area);
        TextView modArea = (TextView)mView.findViewById(R.id.modalidad_area);
        ImageView imgEdit = (ImageView)mView.findViewById(R.id.img_edit);
        ImageView imgDelete = (ImageView)mView.findViewById(R.id.img_delete);
        Button btnAddGrupoEmp = (Button) mView.findViewById(R.id.btn_grupo_emp);

        tituloArea.setText("Titulo: "+areas.get(position).titulo);
        modArea.setText("Modalidad: "+modalidad[areas.get(position).id_tipo_itemm-1]);
        imgEdit.setImageResource(iconos[0]);
        imgDelete.setImageResource(iconos[1]);

        imgEdit.setTag(position);
        imgDelete.setTag(position);
        btnAddGrupoEmp.setTag(position);

        btnAddGrupoEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int i = Integer.parseInt(v.getTag().toString());
                int id = areas.get(i).id;
                Intent in = new Intent(context, GpoEmpActivity.class);
                in.putExtra("id_area",id);

                //inicio
                int id_tipo_item=obtener_tipo_item(id,v.getContext());
                in.putExtra("id_tipo_item",id_tipo_item);
                in.putExtra("accion",0);
                //fin
                context.startActivity(in);
                //a.finish();
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final int i = Integer.parseInt(v.getTag().toString());
                final View mView = inflater.inflate(R.layout.dialogo_area, null);
                EditText edt = (EditText)mView.findViewById(R.id.etArea);
                Spinner sp = (Spinner)mView.findViewById(R.id.spModalidad);
                sp.setVisibility(View.GONE);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

                TextView txt = mView.findViewById(R.id.msj);
                txt.setText(R.string.mt_editar);
                edt.setText(areas.get(i).titulo);
                mBuilder.setPositiveButton(R.string.mt_actualizar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editar_area(i, mView);
                        areas = daoArea.getAreas(id_materia, id_docente);
                        notifyDataSetChanged();
                        /*Intent i = new Intent(context, AreaActivity.class);
                        context.startActivity(i);*/
                    }
                });

                mBuilder.setNegativeButton(R.string.mt_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {
                final int i = Integer.parseInt(v.getTag().toString());

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

                mBuilder.setMessage(R.string.mt_eliminar_area);
                mBuilder.setIcon(R.drawable.ic_delete);
                mBuilder.setTitle(R.string.mt_eliminar);

                mBuilder.setPositiveButton(R.string.mt_eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminar_area(i);
                        areas = daoArea.getAreas(id_materia, id_docente);
                        notifyDataSetChanged();
                        Toast.makeText(context, R.string.mt_eliminado_msj, Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setNegativeButton(R.string.mt_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        return mView;
    }

    public int obtener_tipo_item(int id, Context ct){
        int id_tipo_item=0;
        try{

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ct);
            SQLiteDatabase db = databaseAccess.open();
            Cursor cursor = db.rawQuery("SELECT ID_TIPO_ITEM FROM area WHERE ID_AREA="+id, null);
            cursor.moveToFirst();
            id_tipo_item=cursor.getInt(0);

        }catch (Exception e){
            Log.d("Error","Ocurrio error");
        }
        return id_tipo_item;
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void eliminar_area(int i){
        DatabaseAccess database = DatabaseAccess.getInstance(context);
        SQLiteDatabase db = database.open();

        int ide = areas.get(i).id;
        db.delete("area","id_area="+ide, null);

    }

    public void editar_area(int i, View v){
        EditText edt = (EditText)v.findViewById(R.id.etArea);
        int id = areas.get(i).id;

        DatabaseAccess database = DatabaseAccess.getInstance(context);
        SQLiteDatabase db = database.open();

        ContentValues registro = new ContentValues();
        String titulo = edt.getText().toString();

        if(!titulo.isEmpty()){
            registro.put("titulo", titulo);
            db.update("area", registro, "id_area="+id, null);
        }else{
            Toast.makeText(context, "El area debe tener un titulo", Toast.LENGTH_SHORT).show();
        }
    }
}
