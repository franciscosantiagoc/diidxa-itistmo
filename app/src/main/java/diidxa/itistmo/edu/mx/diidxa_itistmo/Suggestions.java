package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Suggestions extends AppCompatActivity {
    //private static String BASE_URL = "https://diidxa.itistmo.edu.mx/";

    //static String POST_URL = BASE_URL+"webservice/sugerencias.php";
    View focusView = null;

    EditText palabra, traduccion, nombre, correo;
    Button btnsceptar,btncancelar;
    RelativeLayout f1,f2;


    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/";
    private String archivo = "Sugerencias.php";
    private CustomDialog cd = new CustomDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        palabra = (EditText)findViewById(R.id.tve_palabra);
        traduccion = (EditText)findViewById(R.id.ete_traduccion);
        nombre = (EditText)findViewById(R.id.etes_nombre);
        correo = (EditText)findViewById(R.id.etes_email);
        btnsceptar = (Button)findViewById(R.id.btnes_aceptar);
        btnsceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    enviarDatos(palabra.getText().toString(),traduccion.getText().toString(),nombre.getText().toString(),correo.getText().toString());
                }
            }
        });
        btncancelar = (Button)findViewById(R.id.btnes_cancelar);
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void enviarDatos(String esp, String zap, String nombre, String correo) {
        try{
            SimpleDateFormat DF= new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
            Date date=new Date();
            String fecha=DF.format(date);
            AsyncHttpClient ahc = new AsyncHttpClient();
            //http://localhost/diidxa-server-itistmo/Sugerencias.php?pal=cabeza&tra=ique&nom=fran&email=tripley_hhh@hotmail.com&fecha=2019-03-16
            ahc.get(host+"webservice/"+archivo+"?pal="+esp+"&tra="+zap+"&nom="+nombre+"&email="+correo+"&fecha="+fecha, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // FirebaseCrash.log("Error al conectar con el servidor: DiccionarioEsp");
                   cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,Suggestions.this);
                }
            });

        }catch (Exception e){
            //FirebaseCrash.log("Error al realizar funcion busqueda DiccionarioEsp");
        }

    }

    private boolean validate(){

        if(palabra.getText().toString().trim().equals("")) {
            palabra.setError(getString(R.string.SugCampo_V));
            focusView = palabra;
            return false;
        }
        if(traduccion.getText().toString().trim().equals("")) {
            traduccion.setError(getString(R.string.SugCampo_V));
                focusView = traduccion;
                return false;
            }
            else if(nombre.getText().toString().trim().equals("")) {
                nombre.setError(getString(R.string.SugCampo_V));
                //nombre.setError(getString(R.string.errores_nombre));
                focusView = nombre;
                return false;
            }
            else if(correo.getText().toString().trim().equals("")) {
                correo.setError(getString(R.string.SugCampo_V));
                focusView = correo;
                return false;
            }
            else if (!isEmailValid(correo.getText().toString().trim())) {
                correo.setError(getString(R.string.Sug_inv));
                focusView = correo;
                return false;
            }
            else
                return true;
        }



    private boolean isEmailValid(String email) {
            //TODO: Replace this with your own logic
            return email.contains("@")&&email.contains(".com");
        }


}
