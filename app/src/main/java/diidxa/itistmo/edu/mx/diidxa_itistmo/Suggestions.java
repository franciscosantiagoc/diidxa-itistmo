package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Suggestions extends AppCompatActivity {
    //private static String BASE_URL = "https://diidxa.itistmo.edu.mx/";
    private static String TAG="SugerenciasES";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    View focusView = null;
    EditText palabra, traduccion, nombre, correo;
    Button btnsceptar,btncancelar;
    RelativeLayout f1,f2;


    private String host="https://diidxa.itistmo.edu.mx/webservice/";
    //private String host="http://10.0.2.2/diidxa-server-itistmo/";
    private String archivo = "Sugerencias.php";
    private CustomDialog cd = new CustomDialog();
    DatosError DE;

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
            AsyncHttpClient ahc = new AsyncHttpClient();
            //http://localhost/diidxa-server-itistmo/Sugerencias.php?pal=cabeza&tra=ique&nom=fran&email=tripley_hhh@hotmail.com&fecha=2019-03-16
            //Log.d("Respuesta", host+archivo+"?pal="+esp+"&tra="+zap+"&nom="+nombre+"&email="+correo+"&fecha="+fecha);
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Cargando Datos...");
            pd.show();
           ahc.get(host+archivo+"?pal="+esp+"&tra="+zap+"&nom="+nombre+"&email="+correo+"&fecha="+fecha, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d("Respuesta","Status: "+statusCode);
                    pd.dismiss();
                    cd.createDialog(getString(R.string.sug_tit_es),getString(R.string.sug_desc_es),false,Suggestions.this);
                    limpiar();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),statusCode,error.toString());
                    CompExistError("Servidor");
                    cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,Suggestions.this);
                   //Log.d("Respuesta", String.valueOf(error));
                }
            });
            //Toast.makeText(getApplicationContext(),"Fecha actual: "+fecha,Toast.LENGTH_LONG).show();

        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            CompExistError("Servidor");
            cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ).toString(),true,Suggestions.this);
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
