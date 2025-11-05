package model;

import java.util.HashSet;
import java.util.Set;

public class Subject {

    private Integer idSubject;
    private String name;
    private Integer level;
    private Integer order;
    private Professor professor;
    private Career career;
    private Set<Student> students = new HashSet<>();

    public Subject() {
    }

    public Subject(String name, Integer level, Integer order,
            Professor professor, Career career, Set<Student> students) {
        this.name = name;
        this.level = level;
        this.order = order;
        this.professor = professor;
        this.career = career;
        if (students != null) {
            this.students = students;
        }
    }

    public Integer getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Integer idSubject) {
        this.idSubject = idSubject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(Student a) {
        if (a == null) {
            return;
        }
        this.students.add(a);
        a.getSubjects().add(this);
    }

    public void removeStudent(Student a) {
        if (a == null) {
            return;
        }
        this.students.remove(a);
        a.getSubjects().remove(this);
    }

    @Override
    public String toString() {
        return "Subject{name=" + name
                + ", level=" + level
                + ", order=" + order
                + ", professor=" + (professor != null ? professor.getDni() : null)
                + ", career=" + (career != null ? career.getIdCareer() : null)
                + ", students=" + (students != null ? students.size() : 0)
                + "}";
    }
}
