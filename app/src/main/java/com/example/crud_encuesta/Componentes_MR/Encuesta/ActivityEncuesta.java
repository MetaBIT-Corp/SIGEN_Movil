package com.example.crud_encuesta.Componentes_MR.Encuesta;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.crud_encuesta.DatabaseAccess;
import com.example.crud_encuesta.Dominio;
import com.example.crud_encuesta.R;

import java.util.ArrayList;

public class ActivityEncuesta extends AppCompatActivity {

    private SQLiteDatabase db;
    private DatabaseAccess access;
    private DAOEncuesta dao;
    private AdaptadorEncuesta adaptadorEncuesta;

    private Encuesta encuesta;
    private ArrayList<Encuesta> lista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestas_docente);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final ListView list = (ListView) findViewById(R.id.lv_encuestas_docente);

        dao = new DAOEncuesta(this);
        lista = dao.verTodos(adaptadorEncuesta, list,bundle.getInt("user_id"));
        adaptadorEncuesta = new AdaptadorEncuesta(this, lista, dao,this);
        lista = dao.verTodos(adaptadorEncuesta, list,bundle.getInt("user_id"));
        adaptadorEncuesta = new AdaptadorEncuesta(this, lista, dao,this);

        access = DatabaseAccess.getInstance(ActivityEncuesta.this);
        db = access.open();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        });

    }

}
