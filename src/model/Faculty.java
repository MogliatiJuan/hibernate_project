package model;

public class Faculty {
    
    private Integer idFaculty;
    private String name;
    private City city;

    public Faculty() {
    }

    public Faculty(String name, City city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(Integer idFaculty) {
        this.idFaculty = idFaculty;
    }

    @Override
    public String toString() {
        return "{Faculty[" + "name:" + name + ", city:" + city + "]}";
    }

}
