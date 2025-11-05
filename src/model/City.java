package model;

public class City {

    private String nombre;
    private Integer idCiudad;

    public City() {
    }

    public City(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    @Override
    public String toString() {
        return "{City: " + nombre + "}";
    }

}

