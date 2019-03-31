package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Suggestionsza extends AppCompatActivity {

    View focusView = null;
    EditText palabraz,traduccionz, nombrez, correoz;
    Button btnsceptarz,btncancelarz;
    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/";
    private String archivo = "Sugerencias.php";
    private CustomDialog cd = new CustomDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestionsza);

        palabraz = (EditText) findViewById(R.id.tve_palabraza);
        traduccionz = (EditText)findViewById(R.id.ete_traduccionza);
        nombrez = (EditText)findViewById(R.id.etes_nombreza);
        correoz = (EditText)findViewById(R.id.etes_emailza);
        btnsceptarz = (Button)findViewById(R.id.btnes_aceptarza);
        btnsceptarz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    enviarDatos(palabraz.getText().toString(),traduccionz.getText().toString(),nombrez.getText().toString(),correoz.getText().toString());
                }
            }
        });
        btncancelarz = (Button)findViewById(R.id.btnes_cancelarza);
        btncancelarz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private boolean validate(){
        if(palabraz.getText().toString().trim().equals("")) {
            focusView = palabraz;
            return false;
        }
        if(traduccionz.getText().toString().trim().equals("")) {
            focusView = traduccionz;
            return false;
        }
        else if(nombrez.getText().toString().trim().equals("")) {
            //nombre.setError(getString(R.string.errores_nombre));
            focusView = nombrez;
            return false;
        }
        else if(correoz.getText().toString().trim().equals("")) {
            //correo.setError(getString(R.string.errores_correov));
            focusView = correoz;
            return false;
        }
        else if (!isEmailValid(correoz.getText().toString().trim())) {
            //correo.setError(getString(R.string.errores_correo));
            focusView = correoz;
            return false;
        }
        else
            return true;
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
                    cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,Suggestionsza.this);
                }
            });

        }catch (Exception e){
            //FirebaseCrash.log("Error al realizar funcion busqueda DiccionarioEsp");
        }

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains(".com");
    }
}
