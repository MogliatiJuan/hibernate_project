package model;

import java.util.ArrayList;
import java.util.List;

public class Career {

    private Integer idCarrera;
    private String nombre;
    private Faculty facultad;
    private List<Subject> materias = new ArrayList<>();

    public Career() {
    }

    public Career(String nombre, Faculty facultad, List<Subject> materias) {
        this.nombre = nombre;
        this.facultad = facultad;
        if (materias != null) {
            this.materias = materias;
        }
    }

    public Career(String nombre, Faculty facultad) {
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

    public Faculty getFacultad() {
        return facultad;
    }

    public void setFacultad(Faculty facultad) {
        this.facultad = facultad;
    }

    public List<Subject> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Subject> materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        return "Career{"
                + "idCarrera=" + idCarrera
                + ", nombre='" + nombre + '\''
                + ", facultad=" + (facultad != null ? facultad.getNombre() : "null")
                + ", materias=" + (materias != null ? materias.size() : 0)
                + '}';
    }
}

