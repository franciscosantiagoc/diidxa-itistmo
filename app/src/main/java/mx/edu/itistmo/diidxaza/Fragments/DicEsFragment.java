package mx.edu.itistmo.diidxaza.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import mx.edu.itistmo.diidxaza.Datos.DatosBusqueda;
import mx.edu.itistmo.diidxaza.Funciones.AdapterList;
import mx.edu.itistmo.diidxaza.Funciones.ComprobarConexion;
import mx.edu.itistmo.diidxaza.CustomDialog;
import mx.edu.itistmo.diidxaza.Datos.DatosComunicacion;
import mx.edu.itistmo.diidxaza.Datos.DatosError;

import com.github.snowdream.android.widget.SmartImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mx.edu.itistmo.diidxaza.Funciones.EnvioErrores;
import mx.edu.itistmo.diidxaza.R;
import mx.edu.itistmo.diidxaza.Suggestions;


public class DicEsFragment extends Fragment  {
    private static String TAG="DiccionarioEs";
    //TODO: componentes
    private Button btnsuges, search;
    private ListView listView;
    private TextView sug, nosearch;
    private EditText entradaPalabra;
    //TODO:Variables
    private String host="https://diidxa.itistmo.edu.mx/";
    // private String host="http://10.0.2.2";
    private String archivo = "diccionarioDZ.php";

    //TODO: librerias
    private JSONArray jsonArray;
    private JSONObject json;
    private MediaPlayer mp =new MediaPlayer();
    private String esp,zap,img,aud,eje,sig,audej;

    ArrayList<DatosBusqueda> datos = new ArrayList<>();

    private CustomDialog cd = new CustomDialog();
    private EventBus recibe = EventBus.getDefault();
    private DatosError DE;
    private EnvioErrores EnvE;

    public DicEsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_dic_es, container, false);
        entradaPalabra = (EditText)view.findViewById(R.id.entradaPalabra);
        search = (Button) view.findViewById(R.id.BTNTextEspañolSearch);
        sug = (TextView)view.findViewById(R.id.sugMsjE);
        btnsuges = (Button) view.findViewById(R.id.sugEspBtn);
        nosearch = (TextView) view.findViewById(R.id.nosearch);
        listView = (ListView) view.findViewById(R.id.listview);
        btnsuges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Suggestions.class);
                intent.putExtra("palabra",entradaPalabra.getText());
                getActivity().startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!entradaPalabra.equals("") || entradaPalabra.length()>0){
                    descargarImagenes(entradaPalabra.getText().toString(), false, "");
                    /*palabraE.clear();
                    palabraZ.clear();
                    imagen.clear();
                    ejemZ.clear();
                    sigej.clear();
                    audioZa.clear();
                    audioEjZa.clear();*/
                }
            }
        });
        return view;
    }
    //TODO: comienza proceso
    private void descargarImagenes(final String s, final boolean condiccional, final String Parzap) {

        try{
        if(s.equals("")) { }else {
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("Cargando Datos...");
            pd.show();
            //palabraE.clear();palabraZ.clear();imagen.clear();audioZa.clear();ejemZ.clear();sigej.clear();audioEjZa.clear();
            datos.clear();
                AsyncHttpClient ahc = new AsyncHttpClient(true,80,443);
                 ahc.get(host + "webservice/" + archivo + "?id=" + s, new AsyncHttpResponseHandler() {
            //ahc.get(host + "/diidxa-server-itistmo/"+archivo + "?id=" + s, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        pd.dismiss();
                        Log.d("Respuesta","Conexion "+statusCode);
                        if (statusCode == 200) {
                            pd.dismiss();

                            try {
                                String respuesta = new String(responseBody);
                                //Log.d("Respuesta",respuesta);
                                if (respuesta.equals("No existe")) {
                                    noExiste();
                                } else {
                                    jsonArray = new JSONArray(respuesta);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        json = jsonArray.getJSONObject(i);
                                        esp = json.getString("español");
                                        zap = json.getString("zapoteco");
                                        img = json.getString("imagen");
                                        aud = json.getString("audio");
                                        eje = json.getString("ejemplo");
                                        sig = json.getString("significado");
                                        audej = json.getString("audioej");
                                        if (esp.equals("No existe coincidencia") && zap.equals("con ningun idioma") && img.equals("losentimos.jpg")) {
                                            noExiste();
                                            // Log.d("Pasos", "CONSULTA no hay resultados");
                                        } else {
                                            mostrar();
                                            // Log.d("Pasos", "CONSULTA hay coincidencias");
                                            if (condiccional) {

                                                if (esp.equals(s) && zap.equals(Parzap)) {
                                                    //String pal, String trad, String img, String aud, String ejem, String sig, String aude
                                                    datos.add(new DatosBusqueda(esp,zap,img,aud,eje,sig,audej));
                                                    /*palabraE.add(esp);
                                                    palabraZ.add(zap);
                                                    imagen.add(img);
                                                    audioZa.add(aud);
                                                    ejemZ.add(eje);
                                                    sigej.add(sig);
                                                    audioEjZa.add(audej);
                                                    nf = 0;*/
                                                } else {
                                                    noExiste();
                                                }
                                            } else {
                                                datos.add(new DatosBusqueda(esp,zap,img,aud,eje,sig,audej));
                                                /*palabraE.add(esp);
                                                palabraZ.add(zap);
                                                imagen.add(img);
                                                audioZa.add(aud);
                                                ejemZ.add(eje);
                                                sigej.add(sig);
                                                audioEjZa.add(audej);*/

                                            }
                                        }
                                    }
                                    listView.setAdapter(new AdapterList(datos,false,getActivity().getApplicationContext()));
                                     //listView.setAdapter(new DicEsFragment.ImagenAdapter(getContext()));

                                }
                            } catch (Exception e) {
                                noExiste();
                                DE = new DatosError(TAG, getResources().getString(R.string.ConexionServ).toString() + " conversion de JSON", 0, e.toString());
                                EnvE = new EnvioErrores("JSON",DE);
                                EnvE.CompExistError();
                            }

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("Respuesta","Error "+error.getMessage());
                        pd.dismiss();
                        DE = new DatosError(TAG, getResources().getString(R.string.ConexionServ).toString(), statusCode, error.toString());
                        EnvE = new EnvioErrores("Servidor",DE);
                        EnvE.CompExistError();
                        cd.createDialog(getResources().getString(R.string.Serv), getResources().getString(R.string.ConexionServ).toString(), true, getActivity());
                    }
                });

            }
        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            EnvE = new EnvioErrores("Servidor",DE);
            EnvE.CompExistError();
            cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,getActivity());
        }

    }

    public void mostrar(){
        nosearch.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        sug.setVisibility(View.INVISIBLE);
        btnsuges.setVisibility(View.INVISIBLE);
    }
    public void noExiste(){
        nosearch.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        sug.setVisibility(View.VISIBLE);
        btnsuges.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        recibe.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        recibe.unregister(this);
    }
    @Subscribe
    public void ejecutar(DatosComunicacion d){
        entradaPalabra.setText(d.getEsp());
        descargarImagenes(d.getEsp(),true,d.getZap());
    }

}
