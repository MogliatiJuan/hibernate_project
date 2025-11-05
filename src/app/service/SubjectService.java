package app.service;

import model.Subject;
import java.util.List;
import java.util.Optional;

public interface SubjectService {
    List<Subject> list();
    
    List<Subject> listByLevel(int level);
    
    Optional<Subject> findById(Integer id);
    
    Optional<Subject> findByName(String name);
    
    Subject create(String name, Integer level, Integer order, Integer dniProfessor, Integer idCareer);
    
    void update(Subject subject);
    
    void delete(Subject subject);
}
