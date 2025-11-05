package model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Student extends Person {

    private Integer studentNumber;
    private int enrollmentYear;
    private Set<Subject> subjects = new HashSet<>();

    public Student() {
    }

    public Student(String lastName, String firstName, Integer dni, Date birthDate, City city,
            Integer studentNumber, int enrollmentYear, Set<Subject> subjects) {
        super(lastName, firstName, dni, birthDate, city);
        this.studentNumber = studentNumber;
        this.enrollmentYear = enrollmentYear;
        if (subjects != null) {
            this.subjects = subjects;
        }
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject m) {
        if (m == null) {
            return;
        }
        this.subjects.add(m);
        m.getStudents().add(this);
    }

    public void removeSubject(Subject m) {
        if (m == null) {
            return;
        }
        this.subjects.remove(m);
        m.getStudents().remove(this);
    }

    @Override
    public String toString() {
        return "Student{studentNumber=" + studentNumber
                + ", enrollmentYear=" + enrollmentYear
                + ", subjects=" + (subjects != null ? subjects.size() : 0)
                + "} " + super.toString();
    }
}
