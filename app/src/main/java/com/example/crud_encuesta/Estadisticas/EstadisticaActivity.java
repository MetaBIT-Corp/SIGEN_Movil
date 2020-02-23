package com.example.crud_encuesta.Estadisticas;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crud_encuesta.Dominio;
import com.example.crud_encuesta.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class EstadisticaActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    BarChart grafico;
    ListView info;
    int idEva;
    String nomEva;
    private String url_base="";
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;

    TextView tx;
    TextView tx1;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*---------ASIGNANDO VALOR A URL_BASE------------*/
        url_base= Dominio.getInstance(this).getDominio()+"/api/";

        requestQueue= Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Grafico...");
        progressDialog.show();

        setContentView(R.layout.activity_estadistica);
        info=findViewById(R.id.listEstadistic);
        tx=findViewById(R.id.txEstadistica);
        tx1=findViewById(R.id.txEstadistica2);

        grafico = findViewById(R.id.barras);
        idEva=getIntent().getExtras().getInt("id_eva");
        nomEva=getIntent().getExtras().getString("nom_eva");

        tx1.setText(R.string.txesta_eva);
        tx1.append(" "+nomEva);
        tx1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //Configuraciones previas
        grafico.setPinchZoom(false);//No permitir zoom con gesto
        grafico.setDoubleTapToZoomEnabled(false);//No permitir zoom con doble touch
        grafico.setDrawGridBackground(false);//No mostrara grid de cada eje
        grafico.setDragEnabled(true);
        grafico.setScaleEnabled(false);
        grafico.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(EstadisticaActivity.this,e.getData()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //Se verifica si hay conexion al dominio
        if (Dominio.getInstance(this).getDominio()!=null){
            //Llamada al WS
            getEstadisticasWS(idEva);
        }else{
            progressDialog.cancel();
            Toast.makeText(this,"No hay conexion con el servidor.",Toast.LENGTH_SHORT).show();
        }
    }

    private void drawGrafico(int[] resultados){
        if (resultados!=null){
            ArrayList<IBarDataSet> barDataSets = barDataSets(resultados);

            //Creando objeto BarData y asignandolo al grafico
            BarData barData = new BarData(barDataSets);
            barData.setBarWidth(0.8f);
            grafico.setData(barData);

            //Leyenda
            Legend legend = grafico.getLegend();

            //Agregando leyenda personalizada
            ArrayList<LegendEntry> leyendas = new ArrayList<>();
            LegendEntry l1 = new LegendEntry("Aprobados", Legend.LegendForm.CIRCLE, 10f, 0.0f, null, ColorTemplate.COLORFUL_COLORS[0]);
            LegendEntry l2 = new LegendEntry("Reprobados", Legend.LegendForm.CIRCLE, 10f, 0.0f, null, ColorTemplate.COLORFUL_COLORS[1]);
            LegendEntry l3 = new LegendEntry("No Evaluados", Legend.LegendForm.CIRCLE, 10f, 0.0f, null, ColorTemplate.COLORFUL_COLORS[2]);
            LegendEntry l4 = new LegendEntry("Evaluados", Legend.LegendForm.CIRCLE, 10f, 0.0f, null, ColorTemplate.COLORFUL_COLORS[3]);

            leyendas.add(l1);
            leyendas.add(l2);
            leyendas.add(l3);
            leyendas.add(l4);


            //Setiando la leyenda si se comenta queda la leyenda del dataset
            legend.setCustom(leyendas);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(true);
            legend.setXEntrySpace(55);


            //Quitando la descripcion
            Description d = grafico.getDescription();
            d.setText("");


            //Configuraciones del Eje X
            XAxis xAxis = grafico.getXAxis();
            xAxis.setValueFormatter(new XAxisValueFormatter(new String[]{"Aprobados","Reprobados","No Evaluados","Evaluados"}));
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            xAxis.setLabelCount(resultados.length);
            xAxis.setCenterAxisLabels(true);
            xAxis.setGranularity(1);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMinimum(0);


            //Ocualtando YAxisRigth
            YAxis yAxis = grafico.getAxisRight();
            yAxis.setEnabled(false);
            yAxis = grafico.getAxisLeft();
            yAxis.setAxisMaximum(110);
            yAxis.setGranularity(1);
            yAxis.setAxisMinimum(0);


            //Para actualizacion
            grafico.setFitBars(true);//Centra las barras
            grafico.animateXY(2000, 2000);
            grafico.invalidate();
            List<String> lista =new ArrayList<>();
            lista.add("Aprobados: "+resultados[0]+"%");
            lista.add("Reprobados: "+resultados[1]+"%");
            lista.add("Evaluados: "+resultados[3]+"%");
            lista.add("No evaluados: "+resultados[2]+"%");
            ArrayAdapter adapter=new ArrayAdapter(EstadisticaActivity.this,android.R.layout.simple_list_item_1,lista);
            info.setAdapter(adapter);
        }else {
            grafico.setVisibility(View.GONE);
        }

    }

    private ArrayList<IBarDataSet> barDataSets(int[] valores) {
        ArrayList<BarEntry> barEntries;
        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        float conta = 0.5f;
        barEntries = new ArrayList<>();
        for (int c = 0; c < valores.length; c++) {

            BarEntry bar;
            bar = new BarEntry(conta, valores[c],valores[c]+"% de los estudiantes");
            barEntries.add(bar);
            conta++;
        }
            BarDataSet barDataSet = new BarDataSet(barEntries, "");
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            barDataSets.add(barDataSet);

        return barDataSets;
    }

    private void getEstadisticasWS(int idEva){
        String url=url_base+"estadistica/evaluacion/"+idEva;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        tx.setText(R.string.estadistica);
        Toast.makeText(EstadisticaActivity.this,"Error: "+error.toString(),Toast.LENGTH_SHORT).show();
        Log.d("ERROR",error.toString());
        progressDialog.cancel();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if(!response.has("info")){
                int[] resultados=new int[]{
                        response.getInt("porcentaje_aprobados"),
                        response.getInt("porcentaje_reprobados"),
                        response.getInt("porcentaje_no_evaluados"),
                        response.getInt("porcentaje_evaluados")
                };

                drawGrafico(resultados);
            }else{
                if(response.getInt("info")==0){
                    tx.setText(R.string.info0_estadistica);
                }else{
                    tx.setText(R.string.info1_estadistica);
                }
            }
            progressDialog.cancel();

        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }
}
