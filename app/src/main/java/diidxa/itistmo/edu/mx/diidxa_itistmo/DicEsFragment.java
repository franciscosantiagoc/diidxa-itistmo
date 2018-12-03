package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;
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


import com.github.snowdream.android.widget.SmartImageView;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.IOException;
import java.util.ArrayList;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class DicEsFragment extends Fragment implements View.OnClickListener {
    //TODO: componentes
    private Button btnsuges, search;
    private ListView listView;
    private TextView sug, nosearch;
    private EditText entradaPalabra;
    private ArrayList palabraZ = new ArrayList();
    private ArrayList palabraE = new ArrayList();
    private ArrayList imagen =new ArrayList();
    private ArrayList audioZa =new ArrayList();
    private ArrayList audioEjZa =new ArrayList();

    //TODO:Variables
    private String host= "https://diidxa.itistmo.edu.mx"/*"https://dev.porgeeks.com"String host= "http://172.19.1.231"*/,archivo = "diccionarioDZ.php";
    private String A;//,audio;
    private int positioni;
    //private int nf=0;
    //TODO: librerias
    private JSONArray jsonArray;
    private InputMethodManager imm;
    //private TextToSpeech TTS;   //libreria para leer texto

    private MediaPlayer mp;



    public DicEsFragment() {
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
        btnsuges.setOnClickListener(this);
        search.setOnClickListener(this);

        return view;
    }

   //TODO: Acciones de boton

    public void onClick(View v) {

        switch (v.getId()) {
           case R.id.sugEspBtn:

                Toast.makeText(getActivity(),"Evento detectado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), Suggestions.class);
                getActivity().startActivity(intent);

                break;

            case R.id.BTNTextEspañolSearch:
                //imm.hideSoftInputFromWindow(entradaPalabra.getWindowToken(), 0);

                if (!entradaPalabra.equals("") || entradaPalabra.length()>0){
                    descargarImagenes(entradaPalabra.getText().toString());
                }
                break;
        }
    }

    //TODO: comienza proceso

    public class ImagenAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView español,zapoteco;
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewGroup viewG = (ViewGroup)layoutInflater.inflate(R.layout.desing_item_dic, null);
            Log.d("paso 4", "implementando vista de listview No."+i);
            smartImageView = (SmartImageView)viewG.findViewById(R.id.imagen1);
            español = (TextView)viewG.findViewById(R.id.tvEspañol);
            zapoteco = (TextView)viewG.findViewById(R.id.tvZapoteco);
            btnsoundZapotec = (Button)viewG.findViewById(R.id.buttonPlE);
            btnsoundEjZapotec = (Button)viewG.findViewById(R.id.buttonPEJE);
            //String urlFinal = "https://"+host+"/app/capturista/traduccion/images/"+imagen.get(i).toString();
            String urlFinal = host+"/app/capturista/traduccion/images/"+imagen.get(i).toString();

            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            smartImageView.setImageUrl(urlFinal, rect);
            español.setText(palabraE.get(i).toString());
            zapoteco.setText(palabraZ.get(i).toString());
            positioni = i;
            //Boton implementado en listview para reproducir audio en zapoteco
            btnsoundZapotec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int tam = audioZa.get(positioni).toString().length();
                    if (tam==0){
                        //TTS.speak("El audio aun no esta disponible", TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(getActivity(),"El audio no esta disponible",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            mp = new MediaPlayer();
                            //mp.setDataSource("https://"+host+"/app/capturista/traduccion/zapoteco/"+audio);
                            mp.setDataSource(host + "/app/capturista/traduccion/zapoteco/" + audioZa.get(positioni));
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            //TTS.speak("El audio no esta disponible.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("ObtencionaudioZapoteco","Error al obtener el audio de la palabra en zapoteco NO."+(positioni+1));
                        }
                    }

                }
            });
            /*Boton implementado en listview para reproducir audio de ejemplo
            de palabra en zapoteco*/
            btnsoundEjZapotec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getActivity(),"Audio de ejemplo No."+(positioni+1),Toast.LENGTH_SHORT).show();

                }
            });
            return viewG;
        }
    }


    private void descargarImagenes(String s) {
        try{
        A=s;
        if(A==s){
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("Cargando Datos...");
            pd.show();
            AsyncHttpClient ahc = new AsyncHttpClient();
            //ahc.get("https://"+host+"/webservice/"+archivo+"?id="+s, new AsyncHttpResponseHandler() {
            ahc.get(host+"/webservice/"+archivo+"?id="+s, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d("paso 2", "solicitud a servidor aceptada, status: "+statusCode);
                    if (statusCode == 200) {
                        pd.dismiss();
                        try {
                            palabraZ.clear();
                            palabraE.clear();
                            imagen.clear();
                            audioZa.clear();
                            //audio = "";
                            Log.d("paso 3", "Funcionando correctamente");

                            jsonArray = new JSONArray(new String(responseBody));
                            //String tam = Integer.toString(jsonArray.length());
                            //Toast.makeText(getContext(),tam,Toast.LENGTH_SHORT).show();
                            if (jsonArray.length()>1){
                                nosearch.setVisibility(View.VISIBLE);
                               // btnTextEspañolPlay.setVisibility(View.INVISIBLE);
                            }
                            //Toast.makeText(getContext(),jsonArray.getJSONObject(0).get("español").toString(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getContext(),entradaPalabra.getText().toString(),Toast.LENGTH_SHORT).show();

                            /*f (jsonArray.length()==1){
                                if (entradaPalabra.getText().toString().equals(jsonArray.getJSONObject(0).get("español").toString())){
                                    nosearch.setVisibility(View.INVISIBLE);
                                    //audioZa.add(jsonArray.getJSONObject(0).get("audio").toString());
                                    //btnTextEspañolPlay.setVisibility(View.VISIBLE);
                                }
                                else if (entradaPalabra.getText().toString().equals(jsonArray.getJSONObject(0).get("zapoteco").toString())){
                                    nosearch.setVisibility(View.INVISIBLE);
                                    //audioZa.add(jsonArray.getJSONObject(0).get("audio").toString());
                                    //btnTextEspañolPlay.setVisibility(View.VISIBLE);
                                }else if (jsonArray.getJSONObject(0).get("español").toString().equals("No existe coincidencia")){
                                    nosearch.setVisibility(View.INVISIBLE);
                                    //btnTextEspañolPlay.setVisibility(View.INVISIBLE);
                                    nf=1;
                                }else{
                                    nosearch.setVisibility(View.VISIBLE);
                                    //btnTextEspañolPlay.setVisibility(View.INVISIBLE);
                                }

                            }*/
                            Log.d("funcion 2","agregando resultados a compnentes");
                            Log.d("funcion 2","Datos-----------------------");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("funcion 2","agregando resultados de compnente "+(i+1));
                                Object d1,d2,d3,d4;
                                d1=jsonArray.getJSONObject(i).get("español");
                                d2=jsonArray.getJSONObject(i).getString("zapoteco");
                                d3= jsonArray.getJSONObject(i).get("imagen");
                                d4=jsonArray.getJSONObject(0).get("audio").toString();
                                //Log.d("Palabra df","A la palabra "+d1+" le faltan datos");
                                /*if(d2.equals("")||d3.equals("")||d4.equals("")){
                                    //condicional para evitar devolver algo vacio, por lo tantosi hay algun dato vacio será reportado en firebase
                                    listView.setVisibility(View.INVISIBLE);
                                    nosearch.setVisibility(View.INVISIBLE);
                                    sug.setVisibility(View.VISIBLE);
                                    btnsuges.setVisibility(View.VISIBLE);
                                    //nf=0;
                                }else{*/
                                    palabraE.add(d1);
                                    palabraZ.add(d2);
                                    imagen.add(d3);
                                    audioZa.add(d4);

                                    Log.d("funcion 2","palabra español "+d1);
                                    Log.d("funcion 2","palabra Zapoteco "+d2);
                                    Log.d("funcion 2","palabra imagen "+d3);
                                    Log.d("funcion 2","audio palabra"+d4);
                                //}
                            }
                            if (audioZa.size()>0) {
                                nosearch.setVisibility(View.VISIBLE);
                                sug.setVisibility(View.INVISIBLE);
                                btnsuges.setVisibility(View.INVISIBLE);
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(new DicEsFragment.ImagenAdapter(getContext()));
                            }else{
                                listView.setVisibility(View.INVISIBLE);
                                nosearch.setVisibility(View.INVISIBLE);
                                sug.setVisibility(View.VISIBLE);
                                btnsuges.setVisibility(View.VISIBLE);
                                //nf=0;
                            }
                        } catch (JSONException e) {
                           Log.d("Error JSON","Error"+e);
                        } catch(Exception e){
                            Log.d("Error", "Error "+e);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("paso 1", "solicitud a servidor denegada");
                }
            });
        }else{
            A=s;
        }}catch (Exception e){
            Log.d("Error obtencion","Error al realizar funcion "+e);
        }

    }



}
