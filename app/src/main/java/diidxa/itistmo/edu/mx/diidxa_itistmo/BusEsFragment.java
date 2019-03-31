package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BusEsFragment extends Fragment {
    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/";
    private String archivo = "busqueda.php";
    private Button sugesBtnE;
    private ListView listView;
    EditText entradaPalabra;
    private JSONObject json;
    ArrayList palabraZ = new ArrayList();
    ArrayList palabraE = new ArrayList();
    ArrayList imagen =new ArrayList();
    private CustomDialog cd = new CustomDialog();
    private ComprobarConexion cc=new ComprobarConexion();
    private EventBus envio = EventBus.getDefault();
    private int contador=0;
    //Comunicador com;

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

        if(s.equals("")) {

        }else {
            AsyncHttpClient ahc = new AsyncHttpClient();
            //ahc.get("https://"+host+"/webservice/busqueda.php?id="+s, new AsyncHttpResponseHandler() {
            ahc.get(host + "/webservice/" + archivo + "?id=" + s, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        //pd.dismiss();
                        try {
                            palabraZ.clear();
                            palabraE.clear();
                            imagen.clear();
                            Log.d("RespuestaB","Respuesta: " + new String(responseBody));
                            String resp=new String(responseBody);
                            if(resp.equals("No existe")){

                            }else {
                                JSONArray jsonArray = new JSONArray(resp);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    json = jsonArray.getJSONObject(i);
                                    palabraE.add(json.get("español"));
                                    palabraZ.add(json.getString("zapoteco"));
                                    imagen.add(json.get("imagen"));
                                }
                                Log.d("Datos", "Consulta realizada correctamente");
                                listView.setAdapter(new BusEsFragment.ImagenAdapter(getContext()));
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    //detecta el click a un item
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                        //com.envio();
                                        DatosComunicacion d=new DatosComunicacion(palabraE.get(pos).toString(), palabraZ.get(pos).toString(),imagen.get(pos).toString());
                                        envio.post(d);
                                        if(contador==0){
                                            Toast.makeText(getActivity().getApplicationContext(),getString(R.string.sel_item_bus_es1)+palabraE.get(pos)+" "+getString(R.string.sel_item_bus_es2),Toast.LENGTH_LONG).show();
                                            contador++;
                                        }else {
                                            contador++;
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Log.d("RespuestaB", "Error al realizar consulta " + e);
                            //FirebaseCrash.log("Error al convertir JSON: BusquedaEsp");
                        }
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("RespuestaB","Error al conectar con el servidor: BusquedaEsp");
                    cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,getActivity());
                }
            });
        }

    }

    public class ImagenAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView español,zapoteco;
        public ImagenAdapter (Context app){
            this.ctx=app;
            layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return imagen.size();
            //return 0;
        }

        @Override
        public Object getItem(int i) {
            return i;
            //return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewGroup viewG = (ViewGroup)layoutInflater.inflate(R.layout.desing_item_bus, null);
            smartImageView = (SmartImageView)viewG.findViewById(R.id.imagen1Bus);
            español = (TextView)viewG.findViewById(R.id.tvEspañolBus);
            zapoteco = (TextView)viewG.findViewById(R.id.tvZapotecoBus);
            String urlFinal = host+"/app/capturista/traduccion/images/"+imagen.get(i).toString();

            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            /**/
            /**/
            Context con = getActivity();
            Picasso.get().load(urlFinal).error(R.drawable.imagenerror).fit().centerInside().into(smartImageView, new Callback(){
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError(Exception e) {
                    //FirebaseCrash.log("Error al obtener imagen: "+imagen.get(i) + " de palabra "+palabraE.get(i));
                    //   Log.d("Respuesta","Imagen invalida");

                }

            });

            español.setText(palabraE.get(i).toString());
            zapoteco.setText(palabraZ.get(i).toString());
            return viewG;
        }
    }
//permite el envio de los datos del item seleccionado a diccionario
    /*public void onAttach (Context cont){
        super.onAttach(cont);
        com=(Comunicador) cont;
    }
    public interface Comunicador {
        public void envio();
    }*/


}
