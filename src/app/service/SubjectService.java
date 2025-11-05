package app.service;

import model.Materia;
import java.util.List;
import java.util.Optional;

public interface SubjectService {
    List<Materia> list();
    
    List<Materia> listByLevel(int nivel);
    
    Optional<Materia> findById(Integer id);
    
    Optional<Materia> findByName(String nombre);
    
    Materia create(String nombre, Integer nivel, Integer orden, Integer dniProfesor, Integer idCarrera);
    
    void update(Materia materia);
    
    void delete(Materia materia);
}
