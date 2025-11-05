package model;

public class Facultad {
    
    private Integer idFacultad;
    private String nombre;
    private Ciudad ciudad;

    public Facultad() {
    }

    public Facultad(String nombre, Ciudad ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
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
        return "{Facultad[" + "nombre:" + nombre + ", ciudad:" + ciudad + "]}";
    }

}
