package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Suggestions extends AppCompatActivity {
    private static String BASE_URL = "https://diidxa.itistmo.edu.mx/";

    static String POST_URL = BASE_URL+"webservice/sugerencias.php";
    View focusView = null;
    TextView palabra;
    EditText traduccion, nombre, correo;
    Button btnsceptar,btncancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        palabra = (TextView)findViewById(R.id.tve_palabra);
        traduccion = (EditText)findViewById(R.id.ete_traduccion);
        nombre = (EditText)findViewById(R.id.etes_nombre);
        correo = (EditText)findViewById(R.id.etes_email);
        btnsceptar = (Button)findViewById(R.id.btnes_aceptar);
        btnsceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    /**DicEsFragment dicesfragment = new DicEsFragment();
                     final FragmentTransaction fr = getFragmentManager().beginTransaction();
                     fr.replace(R.id.content_main,dicesfragment);
                     fr.commit();**/
                }else{
                    /**RequestParams params = new RequestParams();
                     params.put("palabra",palabra.getText().toString());
                     params.put("traduccion",traduccion.getText().toString());
                     params.put("nombre",nombre.getText().toString());
                     params.put("correo",correo.getText().toString());
                     enviarSug(params);**/
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

        private boolean validate(){
            if(traduccion.getText().toString().trim().equals("")) {
                //traduccion.setError(getString(R.string.errores_traduccion));
                focusView = traduccion;
                return false;
            }
            else if(nombre.getText().toString().trim().equals("")) {
                //nombre.setError(getString(R.string.errores_nombre));
                focusView = nombre;
                return false;
            }
            else if(correo.getText().toString().trim().equals("")) {
                //correo.setError(getString(R.string.errores_correov));
                focusView = correo;
                return false;
            }
            else if (!isEmailValid(correo.getText().toString().trim())) {
                //correo.setError(getString(R.string.errores_correo));
                focusView = correo;
                return false;
            }
            else
                return true;
        }

        private boolean isEmailValid(String email) {
            //TODO: Replace this with your own logic
            return email.contains("@");
        }
        /**
         private void enviarSug(RequestParams params){
         AsyncHttpClient cli = new AsyncHttpClient();

         cli.post(POST_URL,params,new JsonHttpResponseHandler());
         Toast.makeText(getContext(),"Enviado",Toast.LENGTH_LONG).show();


         }
         **/

}
