package com.example.crud_encuesta.Componentes_MR.Encuesta;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crud_encuesta.DatabaseAccess;
import com.example.crud_encuesta.Dominio;
import com.example.crud_encuesta.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class AdaptadorEncuesta extends BaseAdapter {

    private Activity activity;

    private SQLiteDatabase db;
    private DatabaseAccess access;
    private DAOEncuesta dao;

    private Encuesta encuesta;
    private ArrayList<Encuesta> lista;
    private int id = 0;

    Context context;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public AdaptadorEncuesta(Activity activity, ArrayList<Encuesta> lista, DAOEncuesta dao,Context context){
        this.activity = activity;
        this.lista = lista;
        this.dao = dao;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Encuesta getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view==null){
            LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.item_encuestas_docente,null);
        }

        TextView titulo = (TextView) view.findViewById(R.id.tv_titulo_encuesta);
        TextView fecha = (TextView) view.findViewById(R.id.tv_fecha_encuesta);
        TextView publicado = (TextView) view.findViewById(R.id.tv_publicado);
        final Button publicar = (Button) view.findViewById(R.id.btn_publicar_encuesta);
        Button ver = (Button) view.findViewById(R.id.btn_info_encuesta);

        encuesta = lista.get(position);


        titulo.setText(encuesta.getTitulo_encuesta());
        fecha.setText(encuesta.getFecha_inicio_encuesta());

        access = DatabaseAccess.getInstance(view.getContext());
        db = access.open();

        if(encuesta.getVisible()==1){
            publicar.setVisibility(view.INVISIBLE);
            publicado.setText("Encuesta Publicada.");
        }else{
            publicado.setText("Encuesta aún no Publicada.");
        }

        publicar.setTag(position);
        ver.setTag(position);

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());

                final Dialog dialogo = new Dialog(activity);
                dialogo.setTitle("Ver Encuesta");
                dialogo.setCancelable(true);
                dialogo.setContentView(R.layout.vista_encuesta_docente);
                dialogo.show();
                dialogo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                final TextView titulo = (TextView) dialogo.findViewById(R.id.tv_titulo_encuesta);
                final TextView descripcion = (TextView) dialogo.findViewById(R.id.tv_descripcion_encuesta);
                final TextView inicio_encuesta = (TextView) dialogo.findViewById(R.id.tv_fecha_inicio);
                final TextView final_encuesta = (TextView) dialogo.findViewById(R.id.tv_fecha_final);

                Button salir = (Button) dialogo.findViewById(R.id.btn_salir);

                encuesta = lista.get(pos);

                titulo.setText(encuesta.getTitulo_encuesta());
                descripcion.setText(encuesta.getDescripcion_encuesta());
                inicio_encuesta.setText(encuesta.getFecha_inicio_encuesta());
                final_encuesta.setText(encuesta.getFecha_final_encuesta());

                salir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.dismiss();
                    }
                });
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                encuesta = lista.get(pos);
                final int id_encuesta=encuesta.getId();
                Dominio dominio = Dominio.getInstance(context);

                final String host = dominio.getDominio();
                String url = host+"/api/publicar-encuesta/"+id_encuesta;

                requestQueue = Volley.newRequestQueue(context);
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try  {
                                    Toast.makeText(context, response.getString("resultado"), Toast.LENGTH_LONG).show();
                                }catch(Exception e){
                                    Toast.makeText(context, "Error: por favor vuelve a ingresar",Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error: " + error,Toast.LENGTH_LONG).show();
                    }
                }

                );
                jsonObjectRequest.setRetryPolicy( new DefaultRetryPolicy(10000,1,1));
                requestQueue.add(jsonObjectRequest);
            }

        });

        return view;
    }
}
