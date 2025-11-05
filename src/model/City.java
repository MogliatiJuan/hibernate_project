package model;

public class City {

    private String name;
    private Integer idCity;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdCity() {
        return idCity;
    }

    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    @Override
    public String toString() {
        return "{City: " + name + "}";
    }

}
