package model;

import java.util.HashSet;
import java.util.Set;

public class Subject {

    private Integer idMateria;
    private String nombre;
    private Integer nivel;
    private Integer orden;
    private Professor profesor;
    private Career carrera;
    private Set<Student> alumnos = new HashSet<>();

    public Subject() {
    }

    public Subject(String nombre, Integer nivel, Integer orden,
            Professor profesor, Career carrera, Set<Student> alumnos) {
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

    public Professor getProfesor() {
        return profesor;
    }

    public void setProfesor(Professor profesor) {
        this.profesor = profesor;
    }

    public Career getCarrera() {
        return carrera;
    }

    public void setCarrera(Career carrera) {
        this.carrera = carrera;
    }

    public Set<Student> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Student> alumnos) {
        this.alumnos = alumnos;
    }

    public void addAlumno(Student a) {
        if (a == null) {
            return;
        }
        this.alumnos.add(a);
        a.getMaterias().add(this);
    }

    public void removeAlumno(Student a) {
        if (a == null) {
            return;
        }
        this.alumnos.remove(a);
        a.getMaterias().remove(this);
    }

    @Override
    public String toString() {
        return "Subject{nombre=" + nombre
                + ", nivel=" + nivel
                + ", orden=" + orden
                + ", profesor=" + (profesor != null ? profesor.getDni() : null)
                + ", carrera=" + (carrera != null ? carrera.getIdCarrera() : null)
                + ", alumnos=" + (alumnos != null ? alumnos.size() : 0)
                + "}";
    }
}

