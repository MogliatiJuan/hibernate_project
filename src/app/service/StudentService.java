package app.service;

import model.Student;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> list();
    
    Optional<Student> findById(Integer dni);
    
    Optional<Student> findByLastNameAndFirstName(String apellido, String nombre);
    
    Student create(String apellido, String nombre, Integer dni, Date fechaNac, Integer idCiudad, Integer numLegajo, Integer anioIngreso, Integer idMateria);
    
    void update(Student alumno);
    
    void delete(Student alumno);
    
    void addSubject(Integer dniStudent, Integer idMateria);
    
    void removeSubject(Integer dniStudent, Integer idMateria);
}
