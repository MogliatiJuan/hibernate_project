package model;

import java.sql.Date;

public class Persona {

    private String apellido;
    private String nombre;
    private Integer dni;
    private Date fechaNac;
    private Ciudad ciudad;

    public Persona() {
    }

    public Persona(String apellido, String nombre, Integer dni, Date fechaNac, Ciudad ciudad) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
        this.fechaNac = fechaNac;
        this.ciudad = ciudad;
    }

    // constructor overload
    public Persona(String apellido, String nombre, Integer dni, String fechaNacYmd, Ciudad ciudad) {
        this(apellido, nombre, dni,
                (fechaNacYmd == null || fechaNacYmd.isEmpty()) ? null
                : Date.valueOf(fechaNacYmd.replace('/', '-')),
                ciudad);
    }

    // helper
    private static Date parseSqlDate(String ymd) {
        if (ymd == null || ymd.isEmpty()) {
            return null;
        }
        return Date.valueOf(ymd.replace('/', '-')); // require "yyyy-MM-dd"
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "{Persona[" + "apellido:" + apellido + ", nombre:" + nombre + ", dni:" + dni + ", fechaNac:" + fechaNac + ", ciudad:" + ciudad + "]}";
    }

}
