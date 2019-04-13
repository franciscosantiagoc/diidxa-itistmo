package mx.edu.itistmo.diidxaza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AcercaDe extends AppCompatActivity {
private TextView tvE, tvZ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        tvE = (TextView)findViewById(R.id.MsjAcerEs);
        tvZ = (TextView)findViewById(R.id.MsjAcerZa);
        boolean idi = getIntent().getExtras().getBoolean("Idioma");
        if(idi){
            tvE.setVisibility(View.VISIBLE);
        }else{
            tvZ.setVisibility(View.VISIBLE);
        }
    }
}
