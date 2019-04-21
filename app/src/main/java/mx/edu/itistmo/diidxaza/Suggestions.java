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
import android.widget.RelativeLayout;

import mx.edu.itistmo.diidxaza.Datos.DatosError;
import mx.edu.itistmo.diidxaza.Datos.DatosSugerencia;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;

public class Suggestions extends AppCompatActivity {
    private static String TAG="SugerenciasES";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    View focusView = null;
    EditText palabra, traduccion, nombre, correo;
    Button btnsceptar,btncancelar;
    RelativeLayout f1,f2;


    private String host="https://diidxa.itistmo.edu.mx/webservice/";
    //private String host="http://10.0.2.2/diidxa-server-itistmo/";
    private CustomDialog cd = new CustomDialog();
    DatosError DE;
    DatosSugerencia DS;

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
            AsyncHttpClient ahc = new AsyncHttpClient(true,80,443);
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Cargando Datos...");
            pd.show();
            DS = new DatosSugerencia(esp,zap,nombre,correo);
            CompExistSugerencia("Sugerencias");
            pd.dismiss();
            cd.createDialog(getString(R.string.sug_tit_es),getString(R.string.sug_desc_es),false,Suggestions.this);
            limpiar();

        }catch (Exception e){
            DE = new DatosError(TAG,getResources().getString(R.string.ConexionServ).toString(),0,e.toString());
            CompExistError("Servidor");
            cd.createDialog(getResources().getString(R.string.Serv),getString(R.string.ConexionServ),true, Suggestions.this);
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
