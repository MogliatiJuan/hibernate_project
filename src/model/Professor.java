package model;

public class Professor extends Person {

    private Integer antiguedad;

    public Professor() {
    }

    public Professor(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Professor(Integer antiguedad,
            String apellido, String nombre,
            Integer dni, String fechaNac,
            City ciudad) {
        super(apellido, nombre, dni, fechaNac, ciudad);
        this.antiguedad = antiguedad;
    }

    public Integer getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    @Override
    public String toString() {
        return "{Professor[" + "antiguedad:" + antiguedad + "]}";
    }

}

