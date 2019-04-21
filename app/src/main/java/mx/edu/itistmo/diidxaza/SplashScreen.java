package mx.edu.itistmo.diidxaza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import mx.edu.itistmo.diidxaza.Funciones.ComprobarConexion;

import java.util.List;

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
                        //f(cc.isOnline(getApplicationContext())) {
                       final List list =cc.prueba(SplashScreen.this);
                       String r=list.get(0).toString();
                       final String text=list.get(1).toString();
                        if(r.equals("true")){
                            //if(cc.isOnlineNet(getApplicationContext())) {
                            Intent intent = new Intent(getApplicationContext(), MainActivityEs.class);
                            startActivity(intent);
                            finish();
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT).show();
                                    cd.createDialog(getResources().getString(R.string.Serv),text,true,SplashScreen.this);
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
