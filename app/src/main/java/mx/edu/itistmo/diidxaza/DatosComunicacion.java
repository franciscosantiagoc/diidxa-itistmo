package mx.edu.itistmo.diidxaza;

public class DatosComunicacion {
    private String esp;
    private String zap;
    private String img;

    public DatosComunicacion(String esp, String zap, String img) {
        this.esp = esp;
        this.zap = zap;
        this.img = img;
    }

    public String getEsp() { return esp; }

    public String getZap() {
        return zap;
    }

    public String getImg() { return img; }
}
