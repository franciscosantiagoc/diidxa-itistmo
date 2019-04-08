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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Suggestionsza extends AppCompatActivity {
    private static String TAG="SugerenciasZA";
    View focusView = null;
    EditText palabraz,traduccionz, nombrez, correoz;
    Button btnsceptarz,btncancelarz;
    private String host="https://diidxa.itistmo.edu.mx/";
    //private String host="http://10.0.2.2/diidxa-server-itistmo/";
    private String archivo = "Sugerencias.php";

    //static String POST_URL = BASE_URL+"webservice/sugerencias.php";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private CustomDialog cd = new CustomDialog();
    DatosError DE;
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
                startActivity(new Intent(getBaseContext(), MainActivityZa.class)
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
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Cargando Datos...");
            pd.show();
            ahc.get(host+archivo+"?pal="+esp+"&tra="+zap+"&nom="+nombre+"&email="+correo+"&fecha="+fecha, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d("Respuesta","Status: "+statusCode);
                    pd.dismiss();//sug_tit_es   sug_desc_es
                    cd.createDialog(getString(R.string.sug_tit_za),getString(R.string.sug_desc_za),false, Suggestionsza.this);
                    limpiar();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    myRef.setValue(getResources().getString(R.string.ConexionServ).toString()+" "+TAG, "Status: "+statusCode);
                    // Log.d("Respuesta", String.valueOf(error));
                    cd.createDialog(getResources().getString(R.string.Serv),getString(R.string.ConexionServza),true, Suggestionsza.this);

                }
            });
        }catch (Exception e){
            //FirebaseCrash.log("Error al realizar funcion busqueda DiccionarioEsp");
            myRef.child("Error").setValue(getResources().getString(R.string.ConexionServ).toString()+" "+TAG, e);
            //Log.d("Respuesta", String.valueOf(e));
            cd.createDialog(getResources().getString(R.string.Serv),getString(R.string.ConexionServza),true, Suggestionsza.this);
        }
    }

    public void limpiar(){
        palabraz.setText("");
        traduccionz.setText("");
        nombrez.setText("");
        correoz.setText("");
    }

    private boolean validate(){
        if(palabraz.getText().toString().trim().equals("")) {
            palabraz.setError(getString(R.string.SugZaCampo_V));
            focusView = palabraz;
            return false;
        }
        if(traduccionz.getText().toString().trim().equals("")) {
            traduccionz.setError(getString(R.string.SugZaCampo_V));
            focusView = traduccionz;
            return false;
        }
        else if(nombrez.getText().toString().trim().equals("")) {
            nombrez.setError(getString(R.string.SugZaCampo_V));
            focusView = nombrez;
            return false;
        }
        else if(correoz.getText().toString().trim().equals("")) {
            correoz.setError(getString(R.string.SugZaCampo_V));;
            focusView = correoz;
            return false;
        }
        else if (!isEmailValid(correoz.getText().toString().trim())) {
            correoz.setError(getString(R.string.Sugza_inv));
            focusView = correoz;
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
