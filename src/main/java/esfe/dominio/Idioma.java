package esfe.dominio;

public class Idioma {
    private int id;
    private String nombre;
    private String familia;
    private int numeroHablantes;

    public Idioma() {

    }

    public Idioma(int id, String nombre, String familia, int numeroHablantes){
        this.id = id;
        this.nombre = nombre;
        this.familia = familia;
        this.numeroHablantes = numeroHablantes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public int getNumeroHablantes() {
        return numeroHablantes;
    }

    public void setNumeroHablantes(int numeroHablantes) {
        this.numeroHablantes = numeroHablantes;
    }
}
