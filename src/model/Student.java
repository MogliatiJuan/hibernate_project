package model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Student extends Person {

    private Integer numLegajo;
    private int anioIngreso;
    private Set<Subject> materias = new HashSet<>();

    public Student() {
    }

    public Student(String apellido, String nombre, Integer dni, Date fechaNac, City ciudad,
            Integer numLegajo, int anioIngreso, Set<Subject> materias) {
        super(apellido, nombre, dni, fechaNac, ciudad);
        this.numLegajo = numLegajo;
        this.anioIngreso = anioIngreso;
        if (materias != null) {
            this.materias = materias;
        }
    }

    public Integer getNumLegajo() {
        return numLegajo;
    }

    public void setNumLegajo(Integer numLegajo) {
        this.numLegajo = numLegajo;
    }

    public int getAnioIngreso() {
        return anioIngreso;
    }

    public void setAnioIngreso(int anioIngreso) {
        this.anioIngreso = anioIngreso;
    }

    public Set<Subject> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<Subject> materias) {
        this.materias = materias;
    }

    public void addMateria(Subject m) {
        if (m == null) {
            return;
        }
        this.materias.add(m);
        m.getAlumnos().add(this);
    }

    public void removeMateria(Subject m) {
        if (m == null) {
            return;
        }
        this.materias.remove(m);
        m.getAlumnos().remove(this);
    }

    @Override
    public String toString() {
        return "Student{numLegajo=" + numLegajo
                + ", anioIngreso=" + anioIngreso
                + ", materias=" + (materias != null ? materias.size() : 0)
                + "} " + super.toString();
    }
}

