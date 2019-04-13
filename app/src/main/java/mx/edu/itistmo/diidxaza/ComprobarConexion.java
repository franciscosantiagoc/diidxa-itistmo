package mx.edu.itistmo.diidxaza;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ComprobarConexion {
    CustomDialog cd = new CustomDialog();
    private String host="https://diidxa.itistmo.edu.mx";
    private String archivo="diccionarioDZ.php";
    private Boolean r;


    public List<Object> prueba(Activity context){
        List<Object> list = new ArrayList<Object>();
        if(isNetDisponible(context)){
            list.add("true");
            list.add("");
            /*if(isOnlineNet()){
            list.add("true");
            list.add("");
            }else {
                Log.d("Conexion","NO EXISTE CONEXION");
                list.add("false");
                list.add("Error al conectar con el servidor, intentelo mas tarde");
            }*/
        }else{
            list.add("false");
            list.add("Error al conectar con el servidor, verifique su conexion a internet");
        }
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
    /*public Boolean isOnlineNet() {


        return false;
    }*/

}
