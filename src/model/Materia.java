package model;

import java.util.HashSet;
import java.util.Set;

public class Materia {

    private Integer idMateria;
    private String nombre;
    private Integer nivel;
    private Integer orden;
    private Profesor profesor;
    private Carrera carrera;
    private Set<Alumno> alumnos = new HashSet<>();

    public Materia() {
    }

    public Materia(String nombre, Integer nivel, Integer orden,
            Profesor profesor, Carrera carrera, Set<Alumno> alumnos) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.orden = orden;
        this.profesor = profesor;
        this.carrera = carrera;
        if (alumnos != null) {
            this.alumnos = alumnos;
        }
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Set<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    // helpers
    public void addAlumno(Alumno a) {
        if (a == null) {
            return;
        }
        this.alumnos.add(a);
        a.getMaterias().add(this);
    }

    public void removeAlumno(Alumno a) {
        if (a == null) {
            return;
        }
        this.alumnos.remove(a);
        a.getMaterias().remove(this);
    }

    @Override
    public String toString() {
        return "Materia{nombre=" + nombre
                + ", nivel=" + nivel
                + ", orden=" + orden
                + ", profesor=" + (profesor != null ? profesor.getDni() : null)
                + ", carrera=" + (carrera != null ? carrera.getIdCarrera() : null)
                + ", alumnos=" + (alumnos != null ? alumnos.size() : 0)
                + "}";
    }
}
