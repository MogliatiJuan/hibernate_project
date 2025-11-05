package model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Alumno extends Persona {

    private Integer numLegajo;
    private int anioIngreso;
    private Set<Materia> materias = new HashSet<>();

    public Alumno() {
    }

    public Alumno(String apellido, String nombre, Integer dni, Date fechaNac, Ciudad ciudad,
            Integer numLegajo, int anioIngreso, Set<Materia> materias) {
        super(apellido, nombre, dni, fechaNac, ciudad);
        this.numLegajo = numLegajo;
        this.anioIngreso = anioIngreso;
        if (materias != null) {
            this.materias = materias;
        }
    }

    // Getters/Setters
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

    public Set<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }

    public void addMateria(Materia m) {
        if (m == null) {
            return;
        }
        this.materias.add(m);
        m.getAlumnos().add(this);
    }

    public void removeMateria(Materia m) {
        if (m == null) {
            return;
        }
        this.materias.remove(m);
        m.getAlumnos().remove(this);
    }

    @Override
    public String toString() {
        return "Alumno{numLegajo=" + numLegajo
                + ", anioIngreso=" + anioIngreso
                + ", materias=" + (materias != null ? materias.size() : 0)
                + "} " + super.toString();
    }
}
