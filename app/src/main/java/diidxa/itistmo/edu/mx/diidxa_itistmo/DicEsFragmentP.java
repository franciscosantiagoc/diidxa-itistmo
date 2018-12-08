package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;


public class DicEsFragmentP extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    //TODO: componentes
    private Button btnsuges, search;
    private ListView listView;
    private TextView sug, nosearch;
    private EditText entradaPalabra;
    private ArrayList imagen =new ArrayList();
    private ArrayList palabraE = new ArrayList();
    private ArrayList palabraZ = new ArrayList();
    private ArrayList ejemZ =new ArrayList();
    private ArrayList audioZa =new ArrayList();
    private ArrayList sigej =new ArrayList();
    private ArrayList audioEjZa =new ArrayList();

    //TODO:Variables
    private String host="http://172.31.0.153:80" /*"https://diidxa.itistmo.edu.mx""https://dev.porgeeks.com"String host= "http://172.19.1.231"*/,archivo = "pruebaDiccionario.php";
    private String A;//,audio;
    private int positioni;
    //private int nf=0;
    //TODO: librerias
    private JSONArray jsonArray;
    private InputMethodManager imm;
    //private TextToSpeech TTS;   //libreria para leer texto

    private MediaPlayer mp;
    JsonObjectRequest jsonObjectRequest;


    public DicEsFragmentP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_dic_es, container, false);
        entradaPalabra = (EditText)view.findViewById(R.id.entradaPalabra);
        btnsuges = (Button) view.findViewById(R.id.sugEspBtn);
        search = (Button) view.findViewById(R.id.BTNTextEspañolSearch);
        nosearch = (TextView) view.findViewById(R.id.nosearch);
        sug = (TextView)view.findViewById(R.id.sugMsjE);
        btnsuges = (Button)view.findViewById(R.id.sugEspBtn);
        listView = (ListView) view.findViewById(R.id.listview);
        btnsuges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Suggestions.class);
                getActivity().startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!entradaPalabra.equals("") || entradaPalabra.length()>0){
                    descargarImagenes(entradaPalabra.getText().toString());
                }
            }
        });

        return view;
    }


    //TODO: comienza proceso


    private void descargarImagenes(String s) {
        String parametros="id="+s;
        A = s;
           if (A == s) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Cargando Datos...");
                pd.show();
                String url=host+"/webservice2/"+archivo+"?id="+s;
               jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
               // request.add(jsonObjectRequest);
               VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
           }

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("paso con","Error al conectar con el servidor Error:" + error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json=response.optJSONArray("Resultado");
        try {

            for (int i=0;i<json.length();i++){
                String esp, zap,imagen,audio, ejemplo,significado,audioej;
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                esp = jsonObject.optString("español");
                zap = jsonObject.optString("zapoteco");
                imagen = jsonObject.optString("imagen");
                audio = jsonObject.optString("audio");
                ejemplo = jsonObject.optString("ejemplo");
                significado = jsonObject.optString("significado");
                audioej = jsonObject.optString("audioej");

                Log.d("ESP","español: "+esp);
                Log.d("Zap","zapoteco: "+zap);
                Log.d("img","imagen: "+imagen);
                Log.d("aud","audio: "+audio);
                Log.d("ejem","ejemplo: "+ejemplo);
                Log.d("sig","significiado: "+significado);
                Log.d("audioej","audio ejemplo: "+audioej);
            }

            /*
            *  private ArrayList imagen =new ArrayList();
                private ArrayList palabraE = new ArrayList();
                private ArrayList palabraZ = new ArrayList();
                private ArrayList ejemZ =new ArrayList();
                private ArrayList audioZa =new ArrayList();
                private ArrayList sigej =new ArrayList();
                private ArrayList audioEjZa =new ArrayList();
            * */

        } catch (JSONException e) {
            e.printStackTrace();
                Log.d("Paso con", "No se ha podido establecer conexión con el servidor");
        }

    }







}
