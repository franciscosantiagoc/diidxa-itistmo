package mx.edu.itistmo.diidxaza.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import mx.edu.itistmo.diidxaza.Datos.DatosBusqueda;
import mx.edu.itistmo.diidxaza.Funciones.AdapterList;
import mx.edu.itistmo.diidxaza.CustomDialog;
import mx.edu.itistmo.diidxaza.Datos.DatosComunicacion;
import mx.edu.itistmo.diidxaza.Datos.DatosError;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mx.edu.itistmo.diidxaza.Funciones.EnvioEr_Sug;
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
    //private String host="https://172.31.0.8/";
    private String host="https://diidxa.itistmo.edu.mx/";
    // private String host="http://10.0.2.2";
    private String archivo = "diccionarioDZ.php";

    //TODO: librerias
    private JSONArray jsonArray;
    private JSONObject json;
    private String esp,zap,img,aud,eje,sig,audej;

    ArrayList<DatosBusqueda> datos = new ArrayList<>();

    private CustomDialog cd = new CustomDialog();
    private EventBus recibe = EventBus.getDefault();
    private DatosError DE;
    private EnvioEr_Sug EnvE;

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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!entradaPalabra.equals("") || entradaPalabra.length()>0){
                    descargarImagenes(entradaPalabra.getText().toString(), false, "");
                }
            }
        });
        btnsuges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Suggestions.class);
                intent.putExtra("palabra",entradaPalabra.getText().toString());
                getActivity().startActivity(intent);
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
                datos.clear();
                AsyncHttpClient ahc = new AsyncHttpClient(true,80,443);
                ahc.get(host + "webservice/" + archivo + "?id=" + s, new AsyncHttpResponseHandler() {
                //ahc.get(host + "/diidxa-server-itistmo/"+archivo + "?id=" + s, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        pd.dismiss();
                        if (statusCode == 200) {
                            pd.dismiss();

                            try {
                                String respuesta = new String(responseBody);
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
                                        if (condiccional) {
                                            if (esp.equals(s) && zap.equals(Parzap)) {
                                                datos.add(new DatosBusqueda(esp,zap,img,aud,eje,sig,audej));
                                                mostrar();
                                            } else {
                                                noExiste();
                                            }
                                        } else {
                                            datos.add(new DatosBusqueda(esp,zap,img,aud,eje,sig,audej));
                                            mostrar();
                                        }
                                    }
                                    listView.setAdapter(new AdapterList(datos,false,getActivity().getApplicationContext()));
                                }
                            } catch (Exception e) {
                                noExiste();
                                DE = new DatosError(TAG, getResources().getString(R.string.ConexionServ).toString() + " conversion de JSON", 0, e.toString());
                                EnvE = new EnvioEr_Sug("JSON",DE);
                                EnvE.CompExistError();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        pd.dismiss();
                        DE = new DatosError(TAG, getResources().getString(R.string.ConexionServ).toString(), statusCode, error.toString());
                        EnvE = new EnvioEr_Sug("Servidor",DE);
                        EnvE.CompExistError();
                        cd.createDialog(getResources().getString(R.string.Serv), getResources().getString(R.string.ConexionServ).toString(), true, getActivity());
                    }
                });

            }
        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            EnvE = new EnvioEr_Sug("Servidor",DE);
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
