package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    ProgressBar pb;
    int progress=0;
    ComprobarConexion cc=new ComprobarConexion();
    CustomDialog cd = new CustomDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pb = (ProgressBar)findViewById(R.id.progressBar2) ;
        pb.setProgress(progress);
        setProgressValue(progress);
        //createDialog("Titulo de prueba","Esta es una prueba de dialogos personalizados");
    }

    private void setProgressValue(final int progress) {

        // set the progress
        pb.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    setProgressValue(progress + 10);
                    if (progress==100){
                        //  if(cc.isOnline(getApplicationContext())) {
                        if(cc.prueba(getApplicationContext())){
                            //if(cc.isOnlineNet(getApplicationContext())) {
                            Intent intent = new Intent(getApplicationContext(), MainActivityEs.class);
                            startActivity(intent);
                            finish();
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT).show();
                                    cd.createDialog(getResources().getString(R.string.Serv),getResources().getString(R.string.ConexionServ),true,SplashScreen.this);
                                }
                            });

                        }
                    }//
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
