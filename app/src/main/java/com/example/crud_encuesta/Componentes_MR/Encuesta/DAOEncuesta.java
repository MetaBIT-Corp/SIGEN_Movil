package com.example.crud_encuesta.Componentes_MR.Encuesta;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DAOEncuesta implements Response.Listener<JSONObject>, Response.ErrorListener{

    private SQLiteDatabase cx;
    private ArrayList<Encuesta> lista = new ArrayList<>();
    private Encuesta encuesta;
    private Context ct;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;

    Dominio dominio = Dominio.getInstance(ct);
    final String host = dominio.getDominio();

    public DAOEncuesta(Context ct){

        this.ct = ct;
        DatabaseAccess dba = DatabaseAccess.getInstance(ct);
        cx = dba.open();

        progressDialog = new ProgressDialog(ct);
        progressDialog.setMessage("Cargando...");

    }

    public ArrayList<Encuesta> verTodos(AdaptadorEncuesta adaptadorEncuesta, ListView listView, int user_id){

        requestQueue = Volley.newRequestQueue(ct);
        todosConsulta(adaptadorEncuesta,listView,user_id);
        return lista;

    }

    public void todosConsulta(final AdaptadorEncuesta adaptadorEncuesta, final ListView listView, int user_id){

//        progressDialog.show();
        String url = host+"/api/encuestas-docente/"+user_id;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray json = response.optJSONArray("encuestas");
                    lista.clear();

                    for (int i = 0; i<json.length();i++){
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        lista.add(encuesta = new Encuesta(
                                jsonObject.optInt("id"),
                                jsonObject.optInt("id_docente"),
                                jsonObject.optString("titulo_encuesta"),
                                jsonObject.optString("descripcion_encuesta"),
                                jsonObject.optString("fecha_inicio_encuesta"),
                                jsonObject.optString("fecha_final_encuesta"),
                                jsonObject.optInt("visible"),
                                jsonObject.optString("ruta")
                        ));
                    }

                    listView.setAdapter(adaptadorEncuesta);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
        progressDialog.hide();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(ct,  R.string.ws_error + error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(ct, R.string.ws_exito, Toast.LENGTH_SHORT).show();
    }
}
