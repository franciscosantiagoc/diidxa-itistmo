package mx.edu.itistmo.diidxaza.Datos;

public class DatosBusqueda {
    private String pal;
    private String trad;
    private String img;
    private String aud;
    private String ejem;
    private String sig;
    private String audej;

    public DatosBusqueda() {
    }

    public DatosBusqueda(String pal, String trad, String img) {
        this.pal = pal;
        this.trad = trad;
        this.img = img;
    }

    public DatosBusqueda(String pal, String trad, String img, String aud, String ejem, String sig, String audej) {
        this.pal = pal;
        this.trad = trad;
        this.img = img;
        this.aud = aud;
        this.ejem = ejem;
        this.sig = sig;
        this.audej = audej;
    }


    public String getPal() {
        return pal;
    }

    public String getTrad() {
        return trad;
    }

    public String getImg() {
        return img;
    }

    public String getAud() {
        return aud;
    }

    public String getEjem() {
        return ejem;
    }

    public String getSig() {
        return sig;
    }

    public String getAudej() {
        return audej;
    }
}
