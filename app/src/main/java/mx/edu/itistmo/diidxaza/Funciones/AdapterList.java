package mx.edu.itistmo.diidxaza.Funciones;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import mx.edu.itistmo.diidxaza.Datos.DatosBusqueda;
import mx.edu.itistmo.diidxaza.Datos.DatosError;
import mx.edu.itistmo.diidxaza.R;

public class AdapterList extends BaseAdapter {
    private ArrayList<DatosBusqueda> datos = new ArrayList<>();
    private boolean tipo;
    private Context cont;

    private LayoutInflater layoutInflater;
    private SmartImageView smartImageView;
    private TextView español,zapoteco,ejemplo,significado;
    private Button btnaudio, btnaudioej;

    private String purl;
    private URL url;
    private MediaPlayer mp =new MediaPlayer();

    private DatosError DE;
    private EnvioEr_Sug EnvE;

    private String host="https://diidxa.itistmo.edu.mx/";
    private String TAG;

    public AdapterList(ArrayList<DatosBusqueda> datos, boolean tipo, Context cont) {
        this.datos = datos;
        this.tipo = tipo;
        this.cont = cont;
        layoutInflater=(LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return datos.size();
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
        ViewGroup viewG;
        if(tipo) {//busqueda
            viewG = (ViewGroup) layoutInflater.inflate(R.layout.desing_item_bus, null);
            smartImageView = (SmartImageView)viewG.findViewById(R.id.imagen1Bus);
            español = (TextView)viewG.findViewById(R.id.tvEspañolBus);
            zapoteco = (TextView)viewG.findViewById(R.id.tvZapotecoBus);
            TAG = "Busqueda";
        }else {// diccionario
            TAG = "Diccionario";
            viewG = (ViewGroup) layoutInflater.inflate(R.layout.desing_item_dic, null);
            smartImageView = (SmartImageView)viewG.findViewById(R.id.imagen1);
            español = (TextView)viewG.findViewById(R.id.tvEspañol);
            zapoteco = (TextView)viewG.findViewById(R.id.tvZapoteco);
            btnaudio = (Button)viewG.findViewById(R.id.buttonPron);
            btnaudioej = (Button)viewG.findViewById(R.id.buttonEJE);
            ejemplo = (TextView)viewG.findViewById(R.id.tvEjemplo);
            significado = (TextView)viewG.findViewById(R.id.tvSignificado);


            ejemplo.setText(datos.get(i).getEjem());
            significado.setText(datos.get(i).getSig());
            btnaudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(mp.isPlaying()) {
                            mp.stop();
                        }

                        if(datos.get(i).getAud().contains(".aac")) {
                            purl = host + "/app/capturista/traduccion/zapoteco/"+ datos.get(i).getAud().toString();
                            url = new URL(purl);
                            //Log.d("Respuesta", "url: " + purl);
                            mp = new MediaPlayer();
                            mp.setDataSource(String.valueOf(url));
                            mp.prepare();
                            mp.start();
                        }else{
                            mp = MediaPlayer.create(cont.getApplicationContext(), R.raw.audionodisponible);
                            mp.start();
                            DE = new DatosError(TAG, "audio '" + datos.get(i).getAud() + "' de palabra " + datos.get(i).getPal() + " no detectado", 200, "");
                            EnvE = new EnvioEr_Sug("Audios",DE);
                            EnvE.CompExistError();
                        }
                    } catch (Exception e) {
                        DE = new DatosError(TAG, "audio '" + datos.get(i).getAud() + "' de palabra " + datos.get(i).getPal() + " no detectado", 200, e.toString());
                        EnvE = new EnvioEr_Sug("Audios",DE);
                        EnvE.CompExistError();
                        mp.stop();
                        mp = MediaPlayer.create(cont.getApplicationContext(), R.raw.audionodisponible);
                        mp.start();
                    }
                }
            });
            btnaudioej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(mp.isPlaying()) {
                            mp.stop();
                        }else{
                            if(datos.get(i).getAudej().toString().contains(".aac")) {
                                mp = new MediaPlayer();
                                mp.setDataSource(host + "/app/capturista/traduccion/ejemplosz/" + datos.get(i).getAudej());
                                mp.prepare();
                                mp.start();
                            }else{
                                DE = new DatosError(TAG,"audio '"+datos.get(i).getAudej()+"' de palabra "+ datos.get(i).getPal() +" no detectado",200,"");
                                EnvE = new EnvioEr_Sug("AudiosEj",DE);
                                EnvE.CompExistError();
                                mp = MediaPlayer.create(cont.getApplicationContext(), R.raw.audionodisponible);
                                mp.start();
                            }
                        }
                    } catch (Exception e) {
                        DE = new DatosError(TAG,"audio '"+datos.get(i).getAudej()+"' de palabra "+ datos.get(i).getPal() +" no detectado",200,e.toString());
                        EnvE = new EnvioEr_Sug("AudiosEj",DE);
                        EnvE.CompExistError();
                        mp.stop();
                        mp=MediaPlayer.create(cont.getApplicationContext(),R.raw.audionodisponible);
                        mp.start();
                        //Log.d("Respuesta","Error al obtener el audio "+audioEjZa.get(i)+" del ejemplo de la palabra " + palabraE.get(i).toString()+" "+e);
                    }

                }
            });
        }

        String urlFinal = host+"/app/capturista/traduccion/images/"+datos.get(i).getImg();
        Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());

        Picasso.get().load(urlFinal).error(R.drawable.imagenerror).fit().centerInside().into(smartImageView, new Callback(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(Exception e) {
                try {
                    DE = new DatosError(TAG, "imagen '" + datos.get(i).getImg() + "' de palabra " + datos.get(i).getPal() + " no detectada", 200, e.toString());
                    EnvE = new EnvioEr_Sug("Imagenes",DE);
                    EnvE.CompExistError();
                }catch (Exception ex){
                    //Log.d("Respuesta","Imagen error "+ex);
                }
            }
        });

        español.setText(datos.get(i).getPal());
        zapoteco.setText(datos.get(i).getTrad());

        return viewG;
    }

}
