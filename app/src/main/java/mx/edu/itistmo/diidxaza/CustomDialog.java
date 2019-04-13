package mx.edu.itistmo.diidxaza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

public class CustomDialog extends DialogFragment {
    public void createDialog(String tit, String Desc, final boolean cerrar, Activity cont){
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            // versiones con android 6.0 o superior
            //Toast.makeText(cont.getApplicationContext(),"Version igual o superior a 6.0", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle(tit);
        builder.setMessage(Desc);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cerrar){
                    System.exit(0);
                    dismiss();
                    //onDestroy();
                }else {
                    //dismiss();
                }
            }
        });
        builder.create();
        builder.show();
    }

    /*public void  DIAG_CURRENT_VS(String tit, String Desc, final boolean cerrar, Activity cont){
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        LayoutInflater inflater = cont.getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_desing, null);
        builder.setView(v);
        Button acep=(Button)v.findViewById(R.id.ButtonAlert);
        acep.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }
        );


    }
    public void DIAG_PREVIOUS_VS(String tit, String Desc, final boolean cerrar, Context cont){
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
    }*/
}
