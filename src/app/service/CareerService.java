package app.service;

import model.Career;
import java.util.List;
import java.util.Optional;

public interface CareerService {
    List<Career> list();
    
    Optional<Career> findById(Integer id);
    
    Optional<Career> findByName(String nombre);
    
    Career create(String nombre, Integer idFacultad);
    
    void update(Career carrera);
    
    void delete(Career carrera);
}
