package mx.edu.itistmo.diidxaza.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mx.edu.itistmo.diidxaza.CustomDialog;
import mx.edu.itistmo.diidxaza.Datos.DatosBusqueda;
import mx.edu.itistmo.diidxaza.Datos.DatosComunicacion;
import mx.edu.itistmo.diidxaza.Datos.DatosError;
import mx.edu.itistmo.diidxaza.Funciones.AdapterList;
import mx.edu.itistmo.diidxaza.Funciones.EnvioEr_Sug;
import mx.edu.itistmo.diidxaza.R;

public class BusZaFragment extends Fragment {
    private static String TAG="BusquedaZa";

    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/";
    private String archivo = "busqueda.php";

    private ListView listView;
    EditText entradaPalabra;

    private JSONObject json;
    ArrayList <DatosBusqueda> datos = new ArrayList<>();

    private CustomDialog cd = new CustomDialog();
    private EventBus envio = EventBus.getDefault();
    DatosError DE;
    private EnvioEr_Sug EnvE;
    BusZaFragment.Comunicador com;

    public BusZaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_bus_za, container, false);
        listView = (ListView)view.findViewById(R.id.listviewz);
        entradaPalabra = (EditText)view.findViewById(R.id.entradaPalabraZ);
        entradaPalabra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!entradaPalabra.equals("") || entradaPalabra.length()>0){
                    descargarImagenes(entradaPalabra.getText().toString());
                }
            }
        });

        return view;
    }


    private void descargarImagenes(String s) {
        try{
            if(s.equals("")) {

            }else {
                AsyncHttpClient ahc = new AsyncHttpClient(false,80,443);
                //ahc.get("https://"+host+"/webservice/busqueda.php?id="+s, new AsyncHttpResponseHandler() {
                ahc.get(host + "/webservice/" + archivo + "?id=" + s, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {

                            try {
                                String resp=new String(responseBody);

                                if(resp.contains("No existe")){
                                    listView.setVisibility(View.INVISIBLE);
                                }else {
                                    listView.setVisibility(View.VISIBLE);
                                    JSONArray jsonArray = new JSONArray(resp);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        json = jsonArray.getJSONObject(i);
                                        String esp = json.getString("español");
                                        String trad = json.getString("zapoteco");
                                        String img = json.getString("imagen");
                                        datos.add(new DatosBusqueda(esp,trad,img));
                                    }

                                    listView.setAdapter(new AdapterList(datos,true,getActivity().getApplicationContext()));
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        //detecta el click a un item
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                            DatosComunicacion d = new DatosComunicacion(datos.get(pos).getPal(), datos.get(pos).getTrad());
                                            envio.post(d);
                                            com.envio();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString()+" conversion de JSON",0,e.toString());
                                EnvE = new EnvioEr_Sug("JSON",DE);
                                EnvE.CompExistError();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),statusCode,error.toString());
                        EnvE = new EnvioEr_Sug("Servidor",DE);
                        EnvE.CompExistError();
                        cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServza).toString(),true,getActivity());
                    }
                });
            }
        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            EnvE = new EnvioEr_Sug("Servidor",DE);
            EnvE.CompExistError();
            cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServza).toString(),true,getActivity());
        }

    }

    //sobreescritura de metodos para la interaccion entre busqueda y diccionario
    public void onAttach (Context cont){
        super.onAttach(cont);
        com=(BusZaFragment.Comunicador) cont;
    }
    public interface Comunicador {
        void envio();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        com=null;
    }
}
