package mx.edu.itistmo.diidxaza.Datos;

public class DatosSugerencia {
    private String palabra;
    private String traduccion;
    private String nombre;
    private String email;

    public DatosSugerencia(String palabra, String traduccion, String nombre, String email) {
        this.palabra = palabra;
        this.traduccion = traduccion;
        this.nombre = nombre;
        this.email = email;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getTraduccion() {
        return traduccion;
    }

    public void setTraduccion(String traduccion) {
        this.traduccion = traduccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
