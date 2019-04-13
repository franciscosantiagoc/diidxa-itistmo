package mx.edu.itistmo.diidxaza;

public class DatosError {
   /* FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");*/

    private String TAG;
    private String descripcion;
    private int Status;
    private String error;

    public DatosError(String TAG, String descripcion, int status, String error) {
        this.TAG = TAG;
        this.descripcion = descripcion;
        Status = status;
        this.error = error;
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




}
