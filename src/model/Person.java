package model;

import java.sql.Date;

public class Person {

    private String lastName;
    private String firstName;
    private Integer dni;
    private Date birthDate;
    private City city;

    public Person() {
    }

    public Person(String lastName, String firstName, Integer dni, Date birthDate, City city) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dni = dni;
        this.birthDate = birthDate;
        this.city = city;
    }

    public Person(String lastName, String firstName, Integer dni, String birthDateYmd, City city) {
        this(lastName, firstName, dni,
                (birthDateYmd == null || birthDateYmd.isEmpty()) ? null
                : Date.valueOf(birthDateYmd.replace('/', '-')),
                city);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "{Person[" + "lastName:" + lastName + ", firstName:" + firstName + ", dni:" + dni + ", birthDate:" + birthDate + ", city:" + city + "]}";
    }

}
