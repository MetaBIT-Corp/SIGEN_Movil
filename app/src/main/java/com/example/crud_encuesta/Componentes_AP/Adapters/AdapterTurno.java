package com.example.crud_encuesta.Componentes_AP.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crud_encuesta.Componentes_AP.DAO.DAOTurno;
import com.example.crud_encuesta.Componentes_AP.DAO.DAOUsuario;
import com.example.crud_encuesta.Componentes_AP.Models.Turno;
import com.example.crud_encuesta.Componentes_AP.Models.Usuario;
import com.example.crud_encuesta.Componentes_DC.WebServices.Descargar;
import com.example.crud_encuesta.Componentes_MT.Clave.ClaveActivity;
import com.example.crud_encuesta.Dominio;
import com.example.crud_encuesta.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterTurno extends BaseAdapter {
    private int anio, mes, dia, hora, minuto;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    ProgressDialog progressDialog;

    private Descargar descargar_ws;

    ArrayList<Turno> turnos = new ArrayList<>();
    Turno turno;
    DAOTurno daoTurno;
    Activity activity; //la actividad donde va a mostrarse el listview
    Context context;


    //variables que nos ayudara a manejar los id de turno y de valauacion
    int idTurno = 0;
    int idEvaluacion = 0;


    //constructos del adapter
    public AdapterTurno(ArrayList<Turno> turnos, DAOTurno daoTurno, Activity activity,Context context) {
        this.turnos = turnos;
        this.daoTurno = daoTurno;
        this.activity = activity;
        this.context = context;

    }

    //getter y setter
    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }



    //metodos

    @Override
    public int getCount() {
        return turnos.size();
    }

    @Override
    public Turno getItem(int position) {
        return turnos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return turnos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //retorna el view de una fila x del listview
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_formato, null); //inflamos la fila con el item
        }

        //vinculamos el arraylist de contactos con el listview
        turno = turnos.get(position);

        TextView tv_item = (TextView) view.findViewById(R.id.ap_tv_item);
        //ImageView editar = (ImageView) view.findViewById(R.id.ap_editar_item);
        //ImageView eliminar = (ImageView) view.findViewById(R.id.ap_eliminar_item);
        ImageView info = (ImageView) view.findViewById(R.id.ap_info_item);
        ImageView turnoi = (ImageView) view.findViewById(R.id.ap_turno_item);
        ImageView descargar = (ImageView) view.findViewById(R.id.btn_descargar);
        ImageView publicar = (ImageView) view.findViewById(R.id.ap_publicar_turno);

        String fechainicial = turno.getDateInicial();
        String[] pfechainicial = fechainicial.split(" ");

        String fechafinal = turno.getDateFinal();
        String[] pfechafinal = fechafinal.split(" ");

        tv_item.setText("Turno " + (position + 1));

        //Declaracion de boton para GRAFICOS By Ricardo Estupinian
        Button graficos= view.findViewById(R.id.btn_grafico);
        graficos.setVisibility(View.INVISIBLE);


        //ocultados de acuerdo a rol
        DAOUsuario daoUsuario = new DAOUsuario(context);
        Usuario usuario = daoUsuario.getUsuarioLogueado();
        if(usuario.getROL()== 0 || usuario.getROL()==2){
           /* editar.setVisibility(View.INVISIBLE);
            eliminar.setVisibility(View.INVISIBLE);*/
            turnoi.setVisibility(View.INVISIBLE);
            info.setVisibility(View.INVISIBLE);
            publicar.setVisibility(View.INVISIBLE);
        }

        //utilizamos setTag para que al presionar editar o eliminar, android sepa cuál registro queremos afectar
        /*editar.setTag(position);
        eliminar.setTag(position);*/
        info.setTag(position);
        turnoi.setTag(position);
        publicar.setTag(position);

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 *
                 * LOGICA DE WEBSERVICES
                 *
                 * */
                //Si hay internet se va a realizar la consulta al ws
                if(isInternetAvailable()){
                    progress("Cargando...", view.getContext());
                    Dominio dominio = Dominio.getInstance(view.getContext());
                    String UrlBase = dominio.getDominio();

                    //recuperamos la posicion del registro
                    int pos = Integer.parseInt(view.getTag().toString());

                    //recuperamos el registro de la lista y seteamos el id
                    turno = turnos.get(pos);
                    setIdTurno(turno.getId());
                    publicarTurnoWS(turno.getId(),UrlBase,view.getContext());
                }

                /*
                 *
                 * LOGICA DE WEBSERVICES
                 *
                 * */
            }
        });

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(daoTurno.PoseeTurnoDescargadoSinResponder(daoTurno.getIdIntentoTurno(turno.getId(),daoTurno.getIdEstudiante()))){
                    Toast.makeText(v.getContext(),"Ya posee una evaluación descargada",Toast.LENGTH_LONG).show();
                }else {
                    descargar_ws = new Descargar(context);
                    int turno_id = turno.getId();
                    int estudiante_id = daoTurno.getIdEstudiante();
                    //Pasar params reales
                    descargar_ws.descargar_turno(turno_id, estudiante_id);

                }



            }
        });

        //TODO: OnCLICKLISTENER DE OPCIONES
        //inicio de listener editar
        /*editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogo de editar

                //recuperamos la posicion del registro
                int pos = Integer.parseInt(v.getTag().toString());

                //recuperamos el registro de la lista y seteamos el id
                turno = turnos.get(pos);
                setIdTurno(turno.getId());
                setIdEvaluacion(turno.getIdEvaluacion());

                //creamos dialogo
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//para quitar el titulo
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogo_turno);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                dialog.show();

                //enlazamos views del dialogo turno
                final TextView titulo = (TextView) dialog.findViewById(R.id.ap_tv_title_dialogo_turno);
                final EditText dateinicial = (EditText) dialog.findViewById(R.id.ap_edt_dateinicial);
                final EditText datefinal = (EditText) dialog.findViewById(R.id.ap_edt_datefinal);
                final EditText timeinicial = (EditText) dialog.findViewById(R.id.ap_edt_timeinicial);
                final EditText timefinal = (EditText) dialog.findViewById(R.id.ap_edt_timefinal);
                final EditText contrasenia = (EditText) dialog.findViewById(R.id.ap_edt_pass_turno);
                final CheckBox visible = (CheckBox) dialog.findViewById(R.id.ap_cb_visible);

                Button btCrear = (Button) dialog.findViewById(R.id.d_agregar_turno);
                Button btCancelar = (Button) dialog.findViewById(R.id.d_cancelar_turno);
                ImageView imv_dateinicial = (ImageView) dialog.findViewById(R.id.ap_imv_dateinicial);
                ImageView imv_timeinicial = (ImageView) dialog.findViewById(R.id.ap_imv_timeinicial);
                ImageView imv_datefinal = (ImageView) dialog.findViewById(R.id.ap_imv_datefinal);
                ImageView imv_timefinal = (ImageView) dialog.findViewById(R.id.ap_imv_timefinal);

                //;)
                String fechainicial = turno.getDateInicial();
                String[] pfechainicial = fechainicial.split(" ");

                String fechafinal = turno.getDateFinal();
                String[] pfechafinal = fechafinal.split(" ");

                //seteamos valores de views
                btCrear.setText(v.getResources().getString(R.string.btn_guardar));
                titulo.setText(v.getResources().getString(R.string.mt_editar));
                dateinicial.setText(pfechainicial[0]);
                datefinal.setText(pfechafinal[0]);
                timeinicial.setText(pfechainicial[1]);
                timefinal.setText(pfechafinal[1]);
                contrasenia.setText(turno.getContrasenia());
                if (turno.getVisible() == 1) {
                    visible.setChecked(true);
                } else {
                    visible.setChecked(false);
                }

                //programamos metodo crear y cancelar y pickers

                imv_dateinicial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker(dateinicial, v.getContext());
                    }
                });

                imv_timefinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timepicker(timeinicial, v.getContext());
                    }
                });

                imv_datefinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker(datefinal, v.getContext());
                    }
                });

                imv_timefinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timepicker(timefinal, v.getContext());
                    }
                });


                //Inicio listener de crear
                btCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int ver = 0;
                        if (visible.isChecked()) {
                            ver = 1;
                        }
                        if (!dateinicial.getText().toString().isEmpty() && !datefinal.getText().toString().isEmpty()
                                && !timeinicial.getText().toString().isEmpty() && !timefinal.getText().toString().isEmpty()
                                && !contrasenia.getText().toString().isEmpty()) {
                            if(validarFecha(dateinicial.getText().toString(),
                                    timeinicial.getText().toString(),
                                    datefinal.getText().toString(),
                                    timefinal.getText().toString())) {
                                try {
                                    turno = new Turno(
                                            getIdTurno(),
                                            getIdEvaluacion(),
                                            dateinicial.getText().toString() + " " + timeinicial.getText().toString(),
                                            datefinal.getText().toString() + " " + timefinal.getText().toString(),
                                            ver,
                                            contrasenia.getText().toString()
                                    );
                                    //editamos registro
                                    daoTurno.Editar(turno);
                                    //refrescamos la lista
                                    turnos = daoTurno.verTodos(getIdEvaluacion());
                                    //como ya estamos dentro de la clase adaptador simplemente ejecutamos el metodo
                                    notifyDataSetChanged();
                                    //cerramos el dialogo
                                    dialog.dismiss();


                                } catch (Exception e) {
                                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(
                                        v.getContext(),
                                        v.getResources().getString(R.string.ap_fecha_no_valida),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(
                                    v.getContext(),
                                    v.getResources().getString(R.string.rellene_v),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //Final listener de crear

                //Inicio de listener cancelar
                btCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //final de listener cancelar
            }
        });
        //final de listener editar

        //Inicio de listener eliminar
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialogo de eliminar

                //recuperamos la posicion del registro
                int pos = Integer.parseInt(v.getTag().toString());

                //recuperamos el contacto de la lista y seteamos id
                turno = turnos.get(pos);
                setIdTurno(turno.getId());
                setIdEvaluacion(turno.getIdEvaluacion());

                //creamos Alertdialogo

                AlertDialog.Builder delete_emergente = new AlertDialog.Builder(activity);
                delete_emergente.setMessage(v.getResources().getString(R.string.ap_delete_turno));
                delete_emergente.setCancelable(true);

                //Caso positivo

                delete_emergente.setPositiveButton(v.getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //elimina el registro, actualiza la lista y notifica el cambio al adaptador
                        daoTurno.Eliminar(getIdTurno());
                        turnos = daoTurno.verTodos(getIdEvaluacion());
                        notifyDataSetChanged();

                    }
                });

                //Caso negativo

                delete_emergente.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // no esperamos que haga nada al cerrar, solo se cierra
                    }
                });
                delete_emergente.show(); //mostrar alerta
            }
        });*/
        //Final de listener eliminar

        //Inicio de listener info
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialogo de info
                //recuperamos la posicion del registro
                int pos = Integer.parseInt(v.getTag().toString());

                //recuperamos el contacto de la lista y seteamos el id
                turno = turnos.get(pos);
                setIdTurno(turno.getId());

                //creamos dialogo
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//para quitar el titulo
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.vista_turno);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                //enlazamos views del dialogo turno
                final TextView tv_dateinicial = (TextView) dialog.findViewById(R.id.ap_tv_dateinit_turno);
                final TextView tv_datefinal = (TextView) dialog.findViewById(R.id.ap_tv_datefinish_turno);
                final TextView tv_pass = (TextView) dialog.findViewById(R.id.ap_tv_pass_turno);
                final TextView tv_visible = (TextView) dialog.findViewById(R.id.ap_tv_visible_turno);

                Button btn_OK = (Button) dialog.findViewById(R.id.ap_btn_cerrar_vista_turno);

                //seteamos views con los valores que tiene el registro
                tv_dateinicial.setText(v.getResources().getString(R.string.ap_dateinit) + " : " +turno.getDateInicial());
                tv_datefinal.setText(v.getResources().getString(R.string.ap_datefinish) +" : "+ turno.getDateFinal());
                tv_pass.setText(v.getResources().getString(R.string.ap_hint_password) + " : " + turno.getContrasenia());
                if (turno.getVisible() == 1) {
                    tv_visible.setText("Visible: "+ v.getResources().getString(R.string.si));
                } else {
                    tv_visible.setText("Visible: "+ v.getResources().getString(R.string.no));
                }

                btn_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        turnoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperamos la posicion del registro
                int pos = Integer.parseInt(v.getTag().toString());
                int id_turno = turnos.get(pos).getId();

                Intent intent = new Intent(context, ClaveActivity.class);
                //enviamos parametro
                intent.putExtra("id_turno",id_turno);
                context.startActivity(intent);
            }
        });
        //final de listener info
        return view;
    }

    public void datePicker(final EditText editText, Context context) {

        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        anio = c.get(Calendar.YEAR);


        //creamos instancia de clase DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                },
                anio,
                mes,
                dia
        );
        datePickerDialog.show();
    }

    public void timepicker(final EditText editText, Context context) {
        final Calendar c = Calendar.getInstance();
        hora=c.get(Calendar.HOUR_OF_DAY);
        minuto=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editText.setText(hourOfDay + ":" + minute);
                    }
                },
                hora,
                minuto,
                false
        );
        timePickerDialog.show();

    }

    public Boolean validarFecha(String di, String ti, String df, String tf){
        Boolean validado = false;
        int anioi,mesi,diai,horai,minutoi;
        int aniof,mesf,diaf,horaf,minutof;

        anioi = Integer.parseInt(di.split("/")[0]);
        mesi = Integer.parseInt(di.split("/")[1]);
        diai = Integer.parseInt(di.split("/")[2]);

        aniof = Integer.parseInt(df.split("/")[0]);
        mesf = Integer.parseInt(df.split("/")[1]);
        diaf = Integer.parseInt(df.split("/")[2]);

        horai = Integer.parseInt(ti.split(":")[0]);
        minutoi =  Integer.parseInt(ti.split(":")[1]);

        horaf = Integer.parseInt(tf.split(":")[0]);
        minutof =  Integer.parseInt(tf.split(":")[1]);

        if (aniof>anioi){
            validado=true;
        }
        if (aniof==anioi){
            if (mesf>mesi){
                validado = true;
            }
            if (mesf==mesi){
                if(diaf>diai){
                    validado=true;
                }
                if(diaf==diai){
                    if(horaf>horai){
                        validado = true;
                    }
                    if(horaf==horai){
                        if(minutof>=minutoi){
                            validado = true;
                        }
                    }
                }
            }
        }

        return  validado;
    }


    public boolean isInternetAvailable() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            return p.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void publicarTurnoWS(int id_turno, String UrlBase, final Context context) {
        requestQueue = Volley.newRequestQueue(context);
        String url = UrlBase + "/api/evaluaciones_m/publicar_turno/"+id_turno;
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try  {
                            Toast.makeText(context, response.getString("resultado"), Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                        }catch(Exception e){
                            Toast.makeText(context, "Error: por favor vuelve a ingresar",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error: " + error,Toast.LENGTH_LONG).show();
                    }
                });
        jsonObjectRequest.setRetryPolicy( new DefaultRetryPolicy(8000,1,1));
        requestQueue.add(jsonObjectRequest);
    }

    public void progress(String mensaje, Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mensaje);
        progressDialog.show();
    }

}