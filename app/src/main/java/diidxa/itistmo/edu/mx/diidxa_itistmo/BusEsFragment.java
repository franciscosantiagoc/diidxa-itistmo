package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.content.Context;
import android.graphics.Rect;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BusEsFragment extends Fragment {
    private static String TAG="BusquedaEs";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

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
    DatosError DE;
    private boolean resComp;

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
                AsyncHttpClient ahc = new AsyncHttpClient();
                ahc.get(host+"webservice/"+ archivo + "?id=" + s, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {

                            try {
                                palabraZ.clear();
                                palabraE.clear();
                                imagen.clear();
                                String resp=new String(responseBody);
                                Log.d("RespuestaB","Respuesta: " + resp);

                                if(resp.contains("No existe")){
                                    listView.setVisibility(View.INVISIBLE);
                                }else {
                                    listView.setVisibility(View.VISIBLE);
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
                                                DatosComunicacion d = new DatosComunicacion(palabraE.get(pos).toString(), palabraZ.get(pos).toString(), imagen.get(pos).toString());
                                                envio.post(d);
                                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.sel_item_bus_es1) + palabraE.get(pos) + " " + getString(R.string.sel_item_bus_es2), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                }
                            } catch (JSONException e) {
                                Log.d("Respuesta","Error "+e);
                                //DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString()+" conversion de JSON",0,e.toString());
                                //CompExistError("JSON");
                                //myRef.child("JSON").child(id).setValue(DE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),statusCode,error.toString());
                        CompExistError("Servidor");
                        cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,getActivity());
                    }
                });
            }
        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            CompExistError("Servidor");
            cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,getActivity());
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
        }
        @Override
        public Object getItem(int i) {
            return i;
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

            Context con = getActivity();
            Picasso.get().load(urlFinal).error(R.drawable.imagenerror).fit().centerInside().into(smartImageView, new Callback(){
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {
                    try {
                        DE = new DatosError(TAG, "imagen '" + imagen.get(i) + "' de palabra " + palabraE.get(i) + " no detectada", 200, e.toString());
                        CompExistError("Imagenes");
                    }catch (Exception ex){
                        Log.d("Respuesta","Imagen error "+ex);
                    }
                }
            });

            español.setText(palabraE.get(i).toString());
            zapoteco.setText(palabraZ.get(i).toString());
            return viewG;
        }
    }


    public void CompExistError(final String Child){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r=false;
                for(DataSnapshot snapshot:dataSnapshot.child(Child).getChildren()){
                    String desc=snapshot.child("descripcion").getValue().toString();
                    String ta=snapshot.child("tag").getValue().toString();
                    String er=snapshot.child("error").getValue().toString();
                    if(desc.equals(DE.getDescripcion())&&ta.equals(DE.getTAG())&&er.equals(DE.getError())){
                       r=true;
                    }
                }
                if(!r){
                    String id=myRef.push().getKey();
                    myRef.child(Child).child(id).setValue(DE);
                    Log.d("Respuesta","Se ha registrado el error correctamente");
                }else
                    Log.d("Respuesta","Existe error");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });

    }



}
