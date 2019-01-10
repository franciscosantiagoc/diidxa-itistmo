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
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

//import com.github.snowdream.android.widget.SmartImageView;
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

    //TODO:Variables
    private String host="http://10.0.2.2" /*"192.168.0.10""https://diidxa.itistmo.edu.mx""https://dev.porgeeks.com"String host= "http://172.19.1.231"*/,archivo = "pruebaDiccionario.php";
    private String A,audioz,audioez;
    private int nf=0;
    //TODO: librerias
    private JSONArray jsonArray;
    private JSONObject json;
    //private InputMethodManager imm;
    //private TextToSpeech TTS;   //libreria para leer texto
    private MediaPlayer mp;
    private String esp,zap,img,aud,eje,sig,audej;
    CustomDialog cd = new CustomDialog();


    public DicEsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_dic_es, container, false);
        entradaPalabra = (EditText)view.findViewById(R.id.entradaPalabra);
        search = (Button) view.findViewById(R.id.BTNTextEspañolSearch);
        sug = (TextView)view.findViewById(R.id.sugMsjE);
        btnsuges = (Button) view.findViewById(R.id.sugEspBtn);
        nosearch = (TextView) view.findViewById(R.id.nosearch);
        //btnsuges = (Button)view.findViewById(R.id.sugEspBtn);
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
                    palabraE.clear();
                    palabraZ.clear();
                    imagen.clear();
                    ejemZ.clear();
                    sigej.clear();
                    audioZa.clear();
                    audioEjZa.clear();

                }
                break;
        }
    }
    //TODO: comienza proceso


    private void descargarImagenes(String s) {
        try{
        A=s;
        if(A.equals("adf")) {
            //cd.createDialog(getResources().getString(R.string.ErrorCons),getResources().getString(R.string.CamposVacios),false,getActivity().getApplicationContext());
            Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.ErrorCons)+","+getResources().getString(R.string.CamposVacios),Toast.LENGTH_LONG).show();
        }else{

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
                            String respuesta= new String(responseBody);
                            Log.d("Datos",respuesta);
                            /*respuesta=respuesta.replace("[","");
                            respuesta=respuesta.replace("]","");*/

                            //JsonArray jsonArray = (JsonArray) jsonParser.parse(data);
                            jsonArray = new JSONArray(respuesta);
                            Log.d("Datos","Conversion exitosa");

                           for (int i = 0; i < jsonArray.length(); i++) {
                               json = jsonArray.getJSONObject(i);
                               //String dat = jsonresp.get(i).toString();
                               //json = new JSONObject(dat.substring(dat.indexOf("{"), dat.lastIndexOf("}") + 1));
                               esp = json.getString("español");
                               zap = json.getString("zapoteco");
                               img = json.getString("imagen");
                               aud = json.getString("audio");
                               eje = json.getString("ejemplo");
                               sig = json.getString("significado");
                               audej = json.getString("audioej");
                               /*Log.d("Datos",esp);
                               Log.d("Datos",zap);
                               Log.d("Datos",img);
                               Log.d("Datos",aud);
                               Log.d("Datos",eje);
                               Log.d("Datos",sig);
                               Log.d("Datos",audej);
                               Log.d("Datos","--------------------------------------------a");*/
                               if (esp.equals("No existe coincidencia") && zap.equals("con ningun idioma") && img.equals("losentimos.jpg")) {
                                   nosearch.setVisibility(View.INVISIBLE);
                                   listView.setVisibility(View.INVISIBLE);
                                   sug.setVisibility(View.VISIBLE);
                                   btnsuges.setVisibility(View.VISIBLE);
                                   nf = 1;
                                   //Toast.makeText(getActivity(),"No hay coincidencia",Toast.LENGTH_SHORT).show();
                               } else {
                                   nosearch.setVisibility(View.VISIBLE);
                                   listView.setVisibility(View.VISIBLE);
                                   sug.setVisibility(View.INVISIBLE);
                                   btnsuges.setVisibility(View.INVISIBLE);
                                   //Toast.makeText(getActivity(),"Existen coincidencia",Toast.LENGTH_SHORT).show();

                                   palabraE.add(esp);
                                   palabraZ.add(zap);
                                   imagen.add(img);
                                   audioZa.add(aud);
                                   ejemZ.add(eje);
                                   sigej.add(sig);
                                   audioEjZa.add(audej);
                                  /* Log.d("Datos",esp);
                                   Log.d("Datos",zap);
                                   Log.d("Datos",img);
                                   Log.d("Datos",aud);
                                   Log.d("Datos",eje);
                                   Log.d("Datos",sig);
                                   Log.d("Dtos",audej);*/

                                   nf = 0;
                               }
                           }
                           if(nf==0) {
                               /*for(int n=0;n<imagen.size();n++){
                                   Log.d("Audio no"+n,audioZa.get(n).toString());
                                   Log.d("AudioE no"+n,audioEjZa.get(n).toString());
                               }*/
                               listView.setAdapter(new DicEsFragment.ImagenAdapter(getContext()));
                           }

                        } catch (JSONException e) {
                            Log.d("Datosa", " Respuesta Error" + e);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("paso 1", "solicitud a servidor denegada error: "+error);
                    cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ),false,getActivity());
                }
                });
            }
        }catch (Exception e){
            Log.d("Error obtencion","Error al realizar funcion "+e);
        }

    }





    public class ImagenAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView español,zapoteco, ejemplo, significado;
        Button btnsoundZapotec, btnsoundEjZapotec;
        String auza="",auejemplo="";

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
            ViewGroup viewG = (ViewGroup)layoutInflater.inflate(R.layout.desing_item_dic, null);
            //viewG.removeAllViews();
            //Log.d("paso 4", "implementando vista de listview No."+i);
            smartImageView = (SmartImageView)viewG.findViewById(R.id.imagen1);
            español = (TextView)viewG.findViewById(R.id.tvEspañol);
            zapoteco = (TextView)viewG.findViewById(R.id.tvZapoteco);
            ejemplo = (TextView)viewG.findViewById(R.id.tvEjemplo);
            significado = (TextView)viewG.findViewById(R.id.tvSignificado);
            btnsoundZapotec = (Button)viewG.findViewById(R.id.buttonPron);
            btnsoundEjZapotec = (Button)viewG.findViewById(R.id.buttonEJE);
//diidxa.itistmo.edu.mx/app/capturista/traduccion/images/mano.jpg";

            //String urlFinal = "https://"+host+"/app/capturista/traduccion/images/"+imagen.get(i).toString();
            String urlFinal = host+"/app/capturista/traduccion/images/"+imagen.get(i).toString();
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            smartImageView.setImageUrl(urlFinal, rect);
            audioz=audioZa.get(i).toString();
            audioez= audioEjZa.get(i).toString();
            español.setText(palabraE.get(i).toString());
            zapoteco.setText(palabraZ.get(i).toString());
            ejemplo.setText(ejemZ.get(i).toString());
            significado.setText(sigej.get(i).toString());

            //Boton implementado en listview para reproducir audio en zapoteco
            btnsoundZapotec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*int tam = audioZa.get(positioni).toString().length();
                    if (tam==0){
                        //TTS.speak("El audio aun no esta disponible", TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(getActivity(),"El audio no esta disponible",Toast.LENGTH_SHORT).show();
                    }
                    else {*/
                        Toast.makeText(getActivity(),audioZa.get(i).toString(),Toast.LENGTH_SHORT).show();
                        /*try {
                            Toast.makeText(getActivity(),audioZa.get(positioni).toString(),Toast.LENGTH_SHORT).show();
                            mp = new MediaPlayer();
                            mp.stop();
                            //mp.setDataSource("https://"+host+"/app/capturista/traduccion/zapoteco/"+audio);
                            mp.setDataSource(host+"/app/capturista/traduccion/zapoteco/mano.aac");//"+ audioZa.get(positioni));////+
                            mp.prepare();
                            mp.start();

                        } catch (IOException e) {
                            //TTS.speak("El audio no esta disponible.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("Obtencion audio","Error al obtener el audio "+ audioZa.get(positioni)+" de la palabra " + palabraE.get(positioni).toString());
                        }*/
                  //  }
                }
            });
            /*Boton implementado en listview para reproducir audio de ejemplo
            de palabra en zapoteco*/
            btnsoundEjZapotec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* int tam = audioZa.get(positioni).toString().length();
                    if (tam==0){
                        //TTS.speak("El audio aun no esta disponible", TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(getActivity(),"El audio no esta disponible",Toast.LENGTH_SHORT).show();
                    }
                    else {*/

                        Toast.makeText(getActivity(),audioEjZa.get(i).toString(),Toast.LENGTH_SHORT).show();
                        /*try {
                            mp = new MediaPlayer();
                            mp.stop();
                            //mp.setDataSource("https://"+host+"/app/capturista/traduccion/zapoteco/"+audio);
                            mp.setDataSource(host + "/app/capturista/traduccion/ejemplos/" + audioEjZa.get(positioni));
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            //TTS.speak("El audio no esta disponible.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("Obtencion Ejemplo","Error al obtener el audio "+audioEjZa.get(positioni)+" del ejemplo de la palabra " + palabraE.get(positioni).toString());
                        }*/
                    //}

                }
            });
            return viewG;
        }
    }


}
