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
import org.json.JSONObject;


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
    private ArrayList imagen =new ArrayList();
    private ArrayList palabraE = new ArrayList();
    private ArrayList palabraZ = new ArrayList();
    private ArrayList ejemZ =new ArrayList();
    private ArrayList audioZa =new ArrayList();
    private ArrayList sigej =new ArrayList();
    private ArrayList audioEjZa =new ArrayList();
    private ArrayList jsonresp =new ArrayList();

    //TODO:Variables
    private String host="http://10.0.2.2" /*"192.168.0.10""https://diidxa.itistmo.edu.mx""https://dev.porgeeks.com"String host= "http://172.19.1.231"*/,archivo = "pruebaDiccionario.php";
    private String A;//,audio;
    private int positioni;
    //private int nf=0;
    //TODO: librerias
    private JSONArray jsonArray;
    private JSONObject json;
    //private InputMethodManager imm;
    //private TextToSpeech TTS;   //libreria para leer texto
    private MediaPlayer mp;
    private String esp,zap,img,aud,eje,sig,audej;


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
                Intent intent = new Intent(getActivity(), Suggestions.class);
                getActivity().startActivity(intent);
                break;
            case R.id.BTNTextEspañolSearch:
                if (!entradaPalabra.equals("") || entradaPalabra.length()>0){
                    descargarImagenes(entradaPalabra.getText().toString());
                }
                break;
        }
    }
    //TODO: comienza proceso


    private void descargarImagenes(String s) {
        try{
        A=s;
        if(A==s){
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("Cargando Datos...");
            pd.show();

            AsyncHttpClient ahc = new AsyncHttpClient();
            //ahc.get("https://"+host+"/webservice/"+archivo+"?id="+s, new AsyncHttpResponseHandler() {
            ahc.get(host+"/diidxa-server-itistmo/"+archivo+"?id="+s, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    pd.dismiss();
                    if (statusCode == 200) {
                        pd.dismiss();
                        try {
                            Log.d("Paso1 ","Consulta realizada");
                            imagen.clear();
                            palabraE.clear();
                            palabraZ.clear();
                            ejemZ.clear();
                            audioZa.clear();
                            audioEjZa.clear();
                            sigej.clear();
                            jsonresp.add(new String(responseBody));

                           for (int i = 0; i < jsonresp.size(); i++) {

                               if(jsonresp.size()==1){

                               }else{
                                   String dat= jsonresp.get(i).toString();
                                   json= new JSONObject(dat.substring(dat.indexOf("{"), dat.lastIndexOf("}") + 1));
                                   esp=json.getString("palabra");
                                   zap=json.getString("palabraz");
                                   aud=json.getString("audio");
                                   img= json.getString("imagen");
                                   eje=json.getString("ejemplo");
                                   sig=json.getString("significado");
                                   audej=json.getString("audioej");
                                   palabraE.add(esp);
                                   palabraZ.add(zap);
                                   imagen.add(img);
                                   audioZa.add(aud);
                                   ejemZ.add(eje);
                                   sigej.add(sig);
                                   audioEjZa.add(audej);
                               }
                           }
                        } catch (JSONException e) {
                           Log.d("JSON"," Respuesta Error"+e);
                        } catch(Exception e){
                            Log.d("Error", "Error "+e);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("paso 1", "solicitud a servidor denegada error: "+error);
                }
            });
        }else{
            A=s;
        }}catch (Exception e){
            Log.d("Error obtencion","Error al realizar funcion "+e);
        }

    }





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
                    int tam = audioZa.get(positioni).toString().length();
                    if (tam==0){
                        //TTS.speak("El audio aun no esta disponible", TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(getActivity(),"El audio no esta disponible",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            mp = new MediaPlayer();
                            //mp.setDataSource("https://"+host+"/app/capturista/traduccion/zapoteco/"+audio);
                            mp.setDataSource(host + "/app/capturista/traduccion/ejemplos/" + audioZa.get(positioni));
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            //TTS.speak("El audio no esta disponible.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("ObtencionaudioZapoteco","Error al obtener el audio de la palabra en zapoteco NO."+(positioni+1));
                        }
                    }

                }
            });
            return viewG;
        }
    }



}
