package mx.edu.itistmo.diidxaza;

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
import mx.edu.itistmo.diidxaza.R;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class DicZaFragment extends Fragment {
    private static String TAG="DiccionarioZa";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
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
    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/";
    private String archivo = "diccionarioDZ.php";
    private int nf=0;//verifica que exista resultados


    //TODO: librerias
    private JSONArray jsonArray;
    private JSONObject json;
    private String purl;
    private URL url;

    private MediaPlayer mp =new MediaPlayer();
    private String esp,zap,img,aud,eje,sig,audej;
    private CustomDialog cd = new CustomDialog();
    private ComprobarConexion cc=new ComprobarConexion();
    private EventBus recibe = EventBus.getDefault();
    DatosError DE;
    public DicZaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dic_za, container, false);
        entradaPalabra = (EditText)view.findViewById(R.id.entradaPalabraz);
        search = (Button) view.findViewById(R.id.BTNTextZapotecoSearch);
        sug = (TextView)view.findViewById(R.id.sugMsjZ);
        nosearch = (TextView) view.findViewById(R.id.nosearchZap);
        listView = (ListView) view.findViewById(R.id.listviewZa);
        btnsuges = (Button) view.findViewById(R.id.sugZapBtn);

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
                    descargarImagenes(entradaPalabra.getText().toString(), false, "","");
                    palabraE.clear();
                    palabraZ.clear();
                    imagen.clear();
                    ejemZ.clear();
                    sigej.clear();
                    audioZa.clear();
                    audioEjZa.clear();
                }
            }
        });
        return view;
    }

    //TODO: comienza proceso
    private void descargarImagenes(final String s, final boolean condiccional, final String Parzap,final String imag) {
        try{
            if(s.equals("")) { }else{
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Cargando Datos...");
                pd.show();
                palabraE.clear(); palabraZ.clear(); imagen.clear();audioZa.clear(); ejemZ.clear();sigej.clear(); audioEjZa.clear();
                AsyncHttpClient ahc = new AsyncHttpClient();
                ahc.get(host+"webservice/"+archivo+"?id="+s, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        pd.dismiss();
                        if (statusCode == 200) {
                            pd.dismiss();

                            try {
                                nf = 1;
                                String respuesta= new String(responseBody);
                                if (respuesta.equals("No existe")) {
                                    noExiste();
                                }else {

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

                                                if (esp.equals(s) && zap.equals(Parzap) &&imag.equals(img)) {
                                                    palabraE.add(esp);
                                                    palabraZ.add(zap);
                                                    imagen.add(img);
                                                    audioZa.add(aud);
                                                    ejemZ.add(eje);
                                                    sigej.add(sig);
                                                    audioEjZa.add(audej);
                                                    nf = 0;
                                                }else {
                                                    noExiste();
                                                }
                                            } else {
                                                palabraE.add(esp);
                                                palabraZ.add(zap);
                                                imagen.add(img);
                                                audioZa.add(aud);
                                                ejemZ.add(eje);
                                                sigej.add(sig);
                                                audioEjZa.add(audej);

                                                nf = 0;
                                            }
                                        }
                                    }
                                    if (nf == 0) {
                                        //Log.d("Respuesta","Imprementando vista");
                                        listView.setAdapter(new DicZaFragment.ImagenAdapter(getContext()));
                                    }
                                }
                            } catch (JSONException e) {
                                nosearch.setVisibility(View.INVISIBLE);
                                listView.setVisibility(View.INVISIBLE);
                                sug.setVisibility(View.VISIBLE);
                                btnsuges.setVisibility(View.VISIBLE);
                                nf = 1;
                                DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString()+" conversion de JSON",0,e.toString());
                                CompExistError("JSON");
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
        TextView español,zapoteco, ejemplo, significado;
        Button btnsoundZapotec, btnsoundEjZapotec;

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
        public Object getItem(int i) {return i;}
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewGroup viewG = (ViewGroup) layoutInflater.inflate(R.layout.desing_item_dic, null);
            smartImageView = (SmartImageView) viewG.findViewById(R.id.imagen1);
            español = (TextView) viewG.findViewById(R.id.tvEspañol);
            zapoteco = (TextView) viewG.findViewById(R.id.tvZapoteco);
            ejemplo = (TextView) viewG.findViewById(R.id.tvEjemplo);
            significado = (TextView) viewG.findViewById(R.id.tvSignificado);
            btnsoundZapotec = (Button) viewG.findViewById(R.id.buttonPron);
            btnsoundEjZapotec = (Button) viewG.findViewById(R.id.buttonEJE);

            String urlimg = host + "/app/capturista/traduccion/images/" + imagen.get(i).toString();
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            Context con = getActivity();
            Picasso.get().load(urlimg).error(R.drawable.imagenerror).fit().centerInside().into(smartImageView, new Callback(){
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {
                    DE = new DatosError(TAG,"imagen '"+imagen.get(i)+"' de palabra "+ palabraE.get(i) +"no detectada",200,e.toString());
                    CompExistError("Imagenes");
                }

            });
            español.setText(palabraE.get(i).toString());
            zapoteco.setText(palabraZ.get(i).toString());
            ejemplo.setText(ejemZ.get(i).toString());
            significado.setText(sigej.get(i).toString());
            btnsoundZapotec.setText(getString(R.string.za_btnaudio));
            btnsoundEjZapotec.setText(getString(R.string.za_btnejaudio));
            //Boton implementado en listview para reproducir audio en zapoteco
            btnsoundZapotec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mp.isPlaying()) {
                        mp.stop();
                    }
                    try {
                        purl = host + "/app/capturista/traduccion/zapoteco/"+ audioZa.get(i).toString();
                        url = new URL(purl);
                        //Log.d("Respuesta", "url: " + purl);
                        mp = new MediaPlayer();
                        mp.setDataSource(String.valueOf(url));
                        mp.prepare();
                        mp.start();

                    } catch (Exception e) {
                        DE = new DatosError(TAG,"audio '"+audioZa.get(i)+"' de palabra "+ palabraE.get(i) +"no detectado",200,e.toString());
                        CompExistError("Audios");
                        mp.stop();
                        mp=MediaPlayer.create(getActivity().getApplicationContext(),R.raw.audionodisponible);
                        mp.start();
                    }
                }
            });

            btnsoundEjZapotec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  Toast.makeText(getActivity(),audioEjZa.get(i).toString(),Toast.LENGTH_SHORT).show();
                    try {
                        if(mp.isPlaying()) {
                            mp.stop();
                        }else{
                            mp = new MediaPlayer();
                            mp.setDataSource(host + "/app/capturista/traduccion/ejemplosz/"+ audioEjZa.get(i).toString());
                            mp.prepare();
                            mp.start();
                        }
                    } catch (Exception e) {
                        DE = new DatosError(TAG,"audio '"+audioEjZa.get(i)+"' de palabra "+ palabraE.get(i) +"no detectado",200,e.toString());
                        CompExistError("AudiosEj");
                        mp.stop();
                        mp=MediaPlayer.create(getActivity().getApplicationContext(),R.raw.audionodisponible);
                        mp.start();
                        //Log.d("Respuesta","Error al obtener el audio "+audioEjZa.get(i)+" del ejemplo de la palabra " + palabraE.get(i).toString()+" "+e);

                    }
                }
            });
            return viewG;
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
        nf = 1;
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
    public void ejecu(DatosComunicacion d){
        entradaPalabra.setText(d.getEsp());
        //Toast.makeText(getActivity().getApplicationContext(),d.getEsp(),Toast.LENGTH_LONG).show();
        descargarImagenes(d.getEsp(),true,d.getZap(),d.getImg());
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
