package app.service;

import model.Subject;
import java.util.List;
import java.util.Optional;

public interface SubjectService {
    List<Subject> list();
    
    List<Subject> listByLevel(int nivel);
    
    Optional<Subject> findById(Integer id);
    
    Optional<Subject> findByName(String nombre);
    
    Subject create(String nombre, Integer nivel, Integer orden, Integer dniProfesor, Integer idCarrera);
    
    void update(Subject materia);
    
    void delete(Subject materia);
}
