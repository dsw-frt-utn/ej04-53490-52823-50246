

package domain;

public class Marca {
    private String Nombre;
    private String Pais;

    public Marca(String Nombre, String Pais) {
        this.Nombre = Nombre;
        this.Pais = Pais;
    }
    
    public String getNombre() {
        return Nombre;
    }

    public String getPais() {
        return Pais;
    }
    
}
