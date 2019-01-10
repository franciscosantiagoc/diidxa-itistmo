package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BusEsFragment extends Fragment {
    private Button sugesBtnE;
    private ListView listView;
    EditText entradaPalabra;
    ArrayList palabraZ = new ArrayList();
    ArrayList palabraE = new ArrayList();
    ArrayList imagen =new ArrayList();

    private String host="http://10.0.2.2" /*"192.168.0.10""https://diidxa.itistmo.edu.mx""https://dev.porgeeks.com"String host= "http://172.19.1.231"*/,archivo = "pruebaBusqueda.php";

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
            //final ProgressDialog pd = new ProgressDialog(getContext());
            //pd.setMessage("Cargando Datos...");
            //pd.show();
        if(s.equals("")) {

        }else {
            AsyncHttpClient ahc = new AsyncHttpClient();
            //ahc.get("https://"+host+"/webservice/busqueda.php?id="+s, new AsyncHttpResponseHandler() {
            ahc.get(host + "//diidxa-server-itistmo/" + archivo + "?id=" + s, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        //pd.dismiss();
                        try {
                            palabraZ.clear();
                            palabraE.clear();
                            imagen.clear();
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                palabraE.add(jsonArray.getJSONObject(i).get("español"));
                                palabraZ.add(jsonArray.getJSONObject(i).getString("zapoteco"));
                                imagen.add(jsonArray.getJSONObject(i).get("imagen"));
                            }
                            Log.d("Datos", "Consulta realizada correctamente");
                            listView.setAdapter(new BusEsFragment.ImagenAdapter(getContext()));
                        } catch (JSONException e) {
                            Log.d("Error", "Error al realizar consulta " + e);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewGroup viewG = (ViewGroup)layoutInflater.inflate(R.layout.desing_item_bus, null);
            smartImageView = (SmartImageView)viewG.findViewById(R.id.imagen1Bus);
            español = (TextView)viewG.findViewById(R.id.tvEspañolBus);
            zapoteco = (TextView)viewG.findViewById(R.id.tvZapotecoBus);
            String urlFinal = host+"/app/capturista/traduccion/images/"+imagen.get(i).toString();

            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());
            /**/
            /**/
            smartImageView.setImageUrl(urlFinal, rect);
            español.setText(palabraE.get(i).toString());
            zapoteco.setText(palabraZ.get(i).toString());
            return viewG;
        }
    }



}
