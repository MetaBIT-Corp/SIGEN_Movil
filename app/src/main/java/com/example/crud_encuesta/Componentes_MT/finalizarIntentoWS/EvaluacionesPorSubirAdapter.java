package com.example.crud_encuesta.Componentes_MT.finalizarIntentoWS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crud_encuesta.Componentes_MT.Intento.IntentoConsultasDB;
import com.example.crud_encuesta.DatabaseAccess;
import com.example.crud_encuesta.Dominio;
import com.example.crud_encuesta.R;

import java.util.ArrayList;
import java.util.List;

public class EvaluacionesPorSubirAdapter  extends BaseAdapter implements AdapterView.OnItemSelectedListener{
    private LayoutInflater inflater = null;
    private Context context;
    private List<EvaluacionesPorSubir> evaluaciones = new ArrayList<>();
    private DAOEvasPorSibir daoEvasPorSibir;
    private int materia_id;
    private Dominio dominio;

    public EvaluacionesPorSubirAdapter(Context context, List<EvaluacionesPorSubir> evaluaciones, DAOEvasPorSibir daoEvasPorSibir, int materia_id){
        this.context = context;
        this.evaluaciones = evaluaciones;
        this.daoEvasPorSibir = daoEvasPorSibir;
        this.materia_id = materia_id;
        dominio = Dominio.getInstance(context);

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if(getCount()<1){
            Toast.makeText(context, "No hay evaluaciones pendientes por subir", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = inflater.inflate(R.layout.item_evas_por_subir, null);

        TextView txt_titulo = mView.findViewById(R.id.txt_titulo_eva);
        ImageView cargar = mView.findViewById(R.id.img_sync);

        txt_titulo.setText(evaluaciones.get(i).nombre_evalacion);
        cargar.setTag(i);

        final  int intento_id = evaluaciones.get(i).getIntento_id();

        cargar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(dominio.getDominio() != null){
                    subirEvaluacion(intento_id);
                    evaluaciones = daoEvasPorSibir.getEvaluacionesPorSubir(materia_id);
                    notifyDataSetChanged();

                }else{
                    Toast.makeText(context, "No hay conexión con el servidor.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return mView;
    }

    @Override
    public int getCount() {
        return evaluaciones.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void subirEvaluacion(int intento_id){
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        SQLiteDatabase db = databaseAccess.open();
        ContentValues contenedor = new ContentValues();
        int id_opcion;
        int id_pregunta;
        int total;
        int es_rc;
        int actual=0;
        String txt_respuesta;

        Toast.makeText(context, "La evaluación se está subiendo, favor mantener la conexión a internet", Toast.LENGTH_SHORT).show();
        try{
            Cursor cursor_respuesta = db.rawQuery("SELECT * FROM RESPUESTA WHERE ID_INTENTO="+intento_id, null);
            total = cursor_respuesta.getCount();

            while(cursor_respuesta.moveToNext()){
                es_rc = 0;
                id_opcion = cursor_respuesta.getInt(1);
                id_pregunta = cursor_respuesta.getInt(3);
                txt_respuesta = cursor_respuesta.getString(4);

                if(txt_respuesta != null){
                    es_rc = 1;
                }

                actual++;

                if(txt_respuesta==null)txt_respuesta="";
                RespuestaWS respuestaWS = new RespuestaWS(context, id_opcion, id_pregunta, intento_id, total, txt_respuesta, 0, es_rc, actual);
            }

            contenedor.put("SUBIDO",1);
            db.update("INTENTO", contenedor, "ID_INTENTO="+intento_id, null);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
