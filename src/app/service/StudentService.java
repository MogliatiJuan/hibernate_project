package app.service;

import model.Alumno;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Alumno> list();
    
    Optional<Alumno> findById(Integer dni);
    
    Optional<Alumno> findByLastNameAndFirstName(String apellido, String nombre);
    
    Alumno create(String apellido, String nombre, Integer dni, Date fechaNac, Integer idCiudad, Integer numLegajo, Integer anioIngreso, Integer idMateria);
    
    void update(Alumno alumno);
    
    void delete(Alumno alumno);
    
    void addSubject(Integer dniAlumno, Integer idMateria);
    
    void removeSubject(Integer dniAlumno, Integer idMateria);
}
