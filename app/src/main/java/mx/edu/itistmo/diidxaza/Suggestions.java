package mx.edu.itistmo.diidxaza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mx.edu.itistmo.diidxaza.Datos.DatosError;
import mx.edu.itistmo.diidxaza.Datos.DatosSugerencia;
import mx.edu.itistmo.diidxaza.Funciones.EnvioEr_Sug;

public class Suggestions extends AppCompatActivity {
    private static String TAG="SugerenciasES";

    View focusView = null;
    EditText palabra, traduccion, nombre, correo;
    Button btnsceptar,btncancelar;

    private String host="https://diidxa.itistmo.edu.mx/webservice/";
    //private String host="http://10.0.2.2/diidxa-server-itistmo/";

    private CustomDialog cd = new CustomDialog();
    private DatosError DE;
    private EnvioEr_Sug EnvE;
    private DatosSugerencia DS;

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
                startActivity(new Intent(getBaseContext(), MainActivityEs.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        });

    }

    private void enviarDatos(String esp, String zap, String nombre, String correo) {
        try{
            Time today=new Time(Time.getCurrentTimezone());
            today.setToNow();
            String fecha=today.year+"-"+(today.month+1)+"-"+today.monthDay;
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Cargando Datos...");
            pd.show();
            DS = new DatosSugerencia(esp,zap,nombre,correo);
            EnvE = new EnvioEr_Sug("Sugerencias",DS);
            EnvE.RegistrarSug();
            pd.dismiss();
            cd.createDialog(getString(R.string.sug_tit_es),getString(R.string.sug_desc_es),false,Suggestions.this);
            limpiar();
        }catch (Exception e){
            //Log.d("Respuesta","Error al enviar sugerencia "+e);
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            EnvE = new EnvioEr_Sug("Servidor",DE);
            EnvE.CompExistError();
            cd.createDialog(getResources().getString(R.string.Serv),getString(R.string.ConexionServ),true, Suggestions.this);
        }
    }

    public void limpiar(){
        palabra.setText("");
        traduccion.setText("");
        nombre.setText("");
        correo.setText("");
    }

    private boolean validate(){
        if(palabra.getText().toString().trim().equals("")) {
            palabra.setError(getString(R.string.SugCampo_V));
            focusView = palabra;
            return false;
        }else if(traduccion.getText().toString().trim().equals("")) {
                traduccion.setError(getString(R.string.SugCampo_V));
                focusView = traduccion;
                return false;
            }
            else if(nombre.getText().toString().trim().equals("")) {
                nombre.setError(getString(R.string.SugCampo_V));
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
        return email.contains("@")&&(email.contains("gmail")||email.contains("Gmail"))&&email.contains(".com");
    }
}
