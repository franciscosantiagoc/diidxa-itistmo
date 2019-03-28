package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import cz.msebera.android.httpclient.Header;

public class ComprobarConexion {
    CustomDialog cd = new CustomDialog();
    private Boolean r;


    public List<Object> prueba(Activity context){
        List<Object> list = new ArrayList<Object>();
        /*if(isNetDisponible(context)){*/
            //if(isOnlineNet()){
            list.add("true");
            list.add("");
            Log.d("Conexion","EXISTE CONEXION");
            /*}else {
                Log.d("Conexion","NO EXISTE CONEXION");
                list.add("false");
                list.add("Error al conectar con el servidor, intentelo mas tarde");
            }*/
        /*}else{
            list.add("false");
            list.add("Error al conectar con el servidor, verifique su conexion a internet");
        }*/
        return list;
    }
    //Método para comprobar que este activo el wifi o los datos
    private boolean isNetDisponible(Activity cont) {
        ConnectivityManager connectivityManager = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Conexion","EXISTE CONEXION");
            return true;
        }else{
            Log.d("Conexion","NO EXISTE CONEXION");
            return false;
        }
    }
    //Metodo para comprobar el estatus de la conexion con el servidor
   /* public Boolean isOnlineNet() {

        try {
            String d="https://diidxa.itistmo.edu.mx";
            URL url = new URL (d);
            return validaurl (url);
            //return true;//(urlc.getResponseCode() == 200);
        } catch (Exception e) {
            Log.e("Conexion", "Error red no disponible", e);
        }
        return false;
    }

    public boolean validaurl (URL urlObject){
        try{
            Boolean resp;
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) urlObject.openConnection ();
            conn.setReadTimeout (30000); // milisegundos
            conn.setConnectTimeout (3500); // milisegundos
            conn.setRequestMethod ("GET");
            conn.setDoInput (true);
            // Iniciar la conexión
            conn.connect ();
            if(Integer.parseInt(""+conn.getResponseCode())==200){
                conn.disconnect();
                return true;
            }

            //Log.d("Obt",""+);

        }catch (IOException e){

        }
        return  false;
    }
*/
}
