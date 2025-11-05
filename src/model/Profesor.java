package model;

public class Profesor extends Persona {

    private Integer antiguedad;

    public Profesor() {
    }

    public Profesor(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Profesor(Integer antiguedad,
            String apellido, String nombre,
            Integer dni, String fechaNac,
            Ciudad ciudad) {
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
        return "{Profesor[" + "antiguedad:" + antiguedad + "]}";
    }

}
