package model;

import java.sql.Date;

public class Person {

    private String apellido;
    private String nombre;
    private Integer dni;
    private Date fechaNac;
    private City ciudad;

    public Person() {
    }

    public Person(String apellido, String nombre, Integer dni, Date fechaNac, City ciudad) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
        this.fechaNac = fechaNac;
        this.ciudad = ciudad;
    }

    public Person(String apellido, String nombre, Integer dni, String fechaNacYmd, City ciudad) {
        this(apellido, nombre, dni,
                (fechaNacYmd == null || fechaNacYmd.isEmpty()) ? null
                : Date.valueOf(fechaNacYmd.replace('/', '-')),
                ciudad);
    }

    private static Date parseSqlDate(String ymd) {
        if (ymd == null || ymd.isEmpty()) {
            return null;
        }
        return Date.valueOf(ymd.replace('/', '-'));
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

    public City getCiudad() {
        return ciudad;
    }

    public void setCiudad(City ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "{Person[" + "apellido:" + apellido + ", nombre:" + nombre + ", dni:" + dni + ", fechaNac:" + fechaNac + ", ciudad:" + ciudad + "]}";
    }

}

