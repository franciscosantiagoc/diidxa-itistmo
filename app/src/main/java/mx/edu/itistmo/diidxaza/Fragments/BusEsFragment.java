package mx.edu.itistmo.diidxaza.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import mx.edu.itistmo.diidxaza.Funciones.AdapterList;
import mx.edu.itistmo.diidxaza.Funciones.ComprobarConexion;
import mx.edu.itistmo.diidxaza.CustomDialog;
import mx.edu.itistmo.diidxaza.Datos.DatosBusqueda;
import mx.edu.itistmo.diidxaza.Datos.DatosComunicacion;
import mx.edu.itistmo.diidxaza.Datos.DatosError;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mx.edu.itistmo.diidxaza.Funciones.EnvioErrores;
import mx.edu.itistmo.diidxaza.R;


public class BusEsFragment extends Fragment {
    private static String TAG="BusquedaEs";
/*    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");*/

    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/";
    private String archivo = "busqueda.php";

    private Button sugesBtnE;
    private ListView listView;
    EditText entradaPalabra;

    private JSONObject json;
    ArrayList<DatosBusqueda> datos = new ArrayList<>();

    /*ArrayList palabraZ = new ArrayList();
    ArrayList palabraE = new ArrayList();
    ArrayList imagen =new ArrayList();*/

    private CustomDialog cd = new CustomDialog();
    private ComprobarConexion cc=new ComprobarConexion();
    private EventBus envio = EventBus.getDefault();
    private DatosError DE;
    private EnvioErrores EnvE;
    Comunicador com;


    public BusEsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_bus_es, container, false);
        // Inflate the layout for this fragment
        listView = (ListView)view.findViewById(R.id.listviewBE);
        entradaPalabra = (EditText)view.findViewById(R.id.entradaPalBE);
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
                AsyncHttpClient ahc = new AsyncHttpClient(true,80,443);
                ahc.get(host+"webservice/"+ archivo + "?id=" + s, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {

                            try {
                                /*palabraZ.clear();
                                palabraE.clear();
                                imagen.clear();*/
                                datos.clear();
                                String resp=new String(responseBody);
                                //Log.d("RespuestaB","Respuesta: " + resp);

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
                                        Log.d("Datos", "Consulta realizada correctamente");

                                        listView.setAdapter(new AdapterList(datos,true,getActivity().getApplicationContext()));
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            //detecta el click a un item
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                                //com.envio();
                                                DatosComunicacion d = new DatosComunicacion(datos.get(pos).getPal(), datos.get(pos).getTrad());
                                                envio.post(d);
                                                com.envio();
                                                //Toast.makeText(getActivity().getApplicationContext(), getString(R.string.sel_item_bus_es1) + palabraE.get(pos) + " " + getString(R.string.sel_item_bus_es2), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                }
                            } catch (JSONException e) {
                                Log.d("Respuesta","Error "+e);
                                DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString()+" conversion de JSON",0,e.toString());
                                //CompExistError("JSON");
                                EnvE = new EnvioErrores("JSON",DE);
                                EnvE.CompExistError();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),statusCode,error.toString());
                        //CompExistError("Servidor");
                        EnvE = new EnvioErrores("Servidor",DE);
                        EnvE.CompExistError();
                        cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,getActivity());
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

    /*public void CompExistError(final String Child){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r=false;
                for(DataSnapshot snap:dataSnapshot.child(Child).getChildren()) {
                    for (DataSnapshot snapshot : snap.getChildren()) {
                        String desc = snapshot.child("descripcion").getValue().toString();
                        String ta = snapshot.child("tag").getValue().toString();
                        String er = snapshot.child("error").getValue().toString();
                        if (desc.equals(DE.getDescripcion()) && ta.equals(DE.getTAG()) && er.equals(DE.getError())) {
                            r = true;
                        }
                       // Log.d("Respuesta","Resultado "+snapshot);
                    }
                }
                if(!r){
                    String id=myRef.push().getKey();
                    myRef.child(Child).child(DE.getFecha()).child(id).setValue(DE);
                    Log.d("Respuesta","Se ha registrado el error correctamente");
                }else
                    Log.d("Respuesta","Existe error");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });

    }
    */


    //sobreescritura de metodos para la interaccion entre busqueda y diccionario
    public void onAttach (Context cont){
        super.onAttach(cont);
        com=(Comunicador) cont;
    }
    public interface Comunicador {
        public void envio();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        com=null;
    }
}