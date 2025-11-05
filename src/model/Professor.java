package model;

public class Professor extends Person {

    private Integer seniority;

    public Professor() {
    }

    public Professor(Integer seniority) {
        this.seniority = seniority;
    }

    public Professor(Integer seniority,
            String lastName, String firstName,
            Integer dni, String birthDateYmd,
            City city) {
        super(lastName, firstName, dni, birthDateYmd, city);
        this.seniority = seniority;
    }

    public Integer getSeniority() {
        return seniority;
    }

    public void setSeniority(Integer seniority) {
        this.seniority = seniority;
    }

    @Override
    public String toString() {
        return "{Professor[" + "seniority:" + seniority + "]}";
    }

}
