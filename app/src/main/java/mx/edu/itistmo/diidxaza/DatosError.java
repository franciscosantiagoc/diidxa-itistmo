package mx.edu.itistmo.diidxaza;

import android.text.format.Time;

public class DatosError {

    private String TAG;
    private String descripcion;
    private int Status;
    private String error;
    private String fecha;

    public DatosError(String TAG, String descripcion, int status, String error) {
        this.TAG = TAG;
        this.descripcion = descripcion;
        Status = status;
        this.error = error;

        Time today=new Time(Time.getCurrentTimezone());
        today.setToNow();
        this.fecha=today.year+"-"+(today.month+1)+"-"+today.monthDay;
    }



    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
