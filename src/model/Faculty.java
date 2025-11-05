package model;

public class Faculty {
    
    private Integer idFacultad;
    private String nombre;
    private City ciudad;

    public Faculty() {
    }

    public Faculty(String nombre, City ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public City getCiudad() {
        return ciudad;
    }

    public void setCiudad(City ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(Integer idFacultad) {
        this.idFacultad = idFacultad;
    }

    
    
    @Override
    public String toString() {
        return "{Faculty[" + "nombre:" + nombre + ", ciudad:" + ciudad + "]}";
    }

}

