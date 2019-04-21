package mx.edu.itistmo.diidxaza.Funciones;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.edu.itistmo.diidxaza.Datos.DatosError;

public class EnvioErrores {
    private String Child;
    private DatosError DE;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    public EnvioErrores(String Child, DatosError DE) {
        this.Child = Child;
        this.DE = DE;
        myRef = database.getReference("message");
    }

    public void CompExistError(){
        Log.d("Respuesta","Entrando a envio de errores");
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
                            // Log.d("Respuesta","Resultado "+snapshot);
                        }
                    }
                    if(!r){
                        String id=myRef.push().getKey();
                        myRef.child(Child).child(DE.getFecha()).child(id).setValue(DE);
                        Log.d("Respuesta","Se ha registrado el error correctamente");
                    }else
                        Log.d("Respuesta","Existe error");

                }catch (Exception e){
                    Log.d("Respuesta"," ERROR EnvioErrores " + e);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });

    }
}
