package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends DialogFragment {
TextView titu,des;
Button btnDial;
    public void createDialog(String tit, String Desc, final boolean cerrar, Context cont){

        final Dialog cusDial = new Dialog(cont);
        cusDial.setContentView(R.layout.alert_desing);
        titu = (TextView)cusDial.findViewById(R.id.textAlertTit);
        des = (TextView)cusDial.findViewById(R.id.textAlertDesc);
        btnDial = (Button)cusDial.findViewById(R.id.ButtonAlert);
        titu.setText(tit);
        des.setText(Desc);
        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cusDial.dismiss();
                if(cerrar){
                    System.exit(0);
                }
            }
        });
        cusDial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cusDial.show();


    }
}
