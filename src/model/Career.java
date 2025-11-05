package model;

import java.util.ArrayList;
import java.util.List;

public class Career {

    private Integer idCareer;
    private String name;
    private Faculty faculty;
    private List<Subject> subjects = new ArrayList<>();

    public Career() {
    }

    public Career(String name, Faculty faculty, List<Subject> subjects) {
        this.name = name;
        this.faculty = faculty;
        if (subjects != null) {
            this.subjects = subjects;
        }
    }

    public Career(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
        this.subjects = new ArrayList<>();
    }

    public Integer getIdCareer() {
        return idCareer;
    }

    public void setIdCareer(Integer idCareer) {
        this.idCareer = idCareer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "Career{"
                + "idCareer=" + idCareer
                + ", name='" + name + '\''
                + ", faculty=" + (faculty != null ? faculty.getName() : "null")
                + ", subjects=" + (subjects != null ? subjects.size() : 0)
                + '}';
    }
}
