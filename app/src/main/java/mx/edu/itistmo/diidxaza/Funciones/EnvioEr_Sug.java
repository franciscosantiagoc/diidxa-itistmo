package mx.edu.itistmo.diidxaza.Funciones;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.edu.itistmo.diidxaza.Datos.DatosError;
import mx.edu.itistmo.diidxaza.Datos.DatosSugerencia;

public class EnvioEr_Sug {
    private String Child;
    private DatosError DE;
    private DatosSugerencia DS;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    public EnvioEr_Sug(String Child, DatosSugerencia DS) {
        this.Child = Child;
        this.DS = DS;
        myRef = database.getReference("message");
    }

    public EnvioEr_Sug(String Child, DatosError DE) {
        this.Child = Child;
        this.DE = DE;
        myRef = database.getReference("message");
    }

    public void CompExistError(){
        //Log.d("Respuesta","Entrando a envio de errores");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r=false;
                try {

                    for(DataSnapshot snap:dataSnapshot.child(Child).getChildren()) {
                        for (DataSnapshot snapshot : snap.getChildren()) {
                            String desc = snapshot.child("descripcion").getValue().toString();
                            String ta = snapshot.child("tag").getValue().toString();
                            String er = snapshot.child("error").getValue().toString();
                            if (desc.equals(DE.getDescripcion()) && ta.equals(DE.getTAG()) && er.equals(DE.getError())) {
                                r = true;
                            }
                        }
                    }
                    if(!r){
                        String id=myRef.push().getKey();
                        myRef.child(Child).child(DE.getFecha()).child(id).setValue(DE);
                        //Log.d("Respuesta","Se ha registrado el error correctamente");
                    }//else
                        //Log.d("Respuesta","Existe error");

                }catch (Exception e){
                   // Log.d("Respuesta"," ERROR EnvioEr_Sug " + e);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d("Respuesta"," ERROR Envio Error " + databaseError);
            }

        });

    }

    public void RegistrarSug() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r=false;
                for(DataSnapshot snapshot:dataSnapshot.child(Child).getChildren()){
                    //Log.d("RespSug",snapshot.toString());
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d("Respuesta"," ERROR Envio Sugerencia " + databaseError);
            }

        });
    }


}
