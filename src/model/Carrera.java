package model;

import java.util.ArrayList;
import java.util.List;

public class Carrera {

    private Integer idCarrera;
    private String nombre;
    private Facultad facultad;
    private List<Materia> materias = new ArrayList<>();

    public Carrera() {
    }

    public Carrera(String nombre, Facultad facultad, List<Materia> materias) {
        this.nombre = nombre;
        this.facultad = facultad;
        if (materias != null) {
            this.materias = materias;
        }
    }

    public Carrera(String nombre, Facultad facultad) {
        this.nombre = nombre;
        this.facultad = facultad;
        this.materias = new ArrayList<>();
    }

    // getters/setters
    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        return "Carrera{"
                + "idCarrera=" + idCarrera
                + ", nombre='" + nombre + '\''
                + ", facultad=" + (facultad != null ? facultad.getNombre() : "null")
                + ", materias=" + (materias != null ? materias.size() : 0)
                + '}';
    }
}
