package app.service;

import model.Student;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> list();
    
    Optional<Student> findById(Integer dni);
    
    Optional<Student> findByLastNameAndFirstName(String lastName, String firstName);
    
    Student create(String lastName, String firstName, Integer dni, Date birthDate, Integer idCity, Integer studentNumber, Integer enrollmentYear, Integer idSubject);
    
    void update(Student student);
    
    void delete(Student student);
    
    void addSubject(Integer dniStudent, Integer idSubject);
    
    void removeSubject(Integer dniStudent, Integer idSubject);
}
