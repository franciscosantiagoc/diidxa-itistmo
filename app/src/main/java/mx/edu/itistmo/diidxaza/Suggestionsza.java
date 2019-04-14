package mx.edu.itistmo.diidxaza;

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
import mx.edu.itistmo.diidxaza.R;

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

    //static String POST_URL = BASE_URL+"webservice/sugerencias.php";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private CustomDialog cd = new CustomDialog();
    DatosError DE;
    DatosSugerencia DS;
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
            AsyncHttpClient ahc = new AsyncHttpClient(true,80,443);
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Cargando Datos...");
            pd.show();
            DS = new DatosSugerencia(esp,zap,nombre,correo);
            CompExistSugerencia("Sugerencias");
            pd.dismiss();
            cd.createDialog(getString(R.string.sug_tit_za),getString(R.string.sug_desc_za),false, Suggestionsza.this);
            limpiar();
        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            CompExistError("Servidor");
            cd.createDialog(getResources().getString(R.string.Serv),getString(R.string.ConexionServza),true, Suggestionsza.this);
        }
    }

    private void CompExistSugerencia(final String Child) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r=false;
                for(DataSnapshot snapshot:dataSnapshot.child(Child).getChildren()){
                    String desc=snapshot.child("palabra").getValue().toString();
                    String ta=snapshot.child("traduccion").getValue().toString();
                    String er=snapshot.child("nombre").getValue().toString();
                    if(desc.equals(DS.getPalabra())&&ta.equals(DS.getTraduccion())&&er.equals(DS.getNombre())){
                        r=true;
                    }
                }
                if(!r){
                    String id=myRef.push().getKey();
                    myRef.child(Child).child(id).setValue(DS);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });
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
        return email.contains("@")&&(email.contains("gmail")||email.contains("Gmail"))&&email.contains(".com");
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
