package app.service;

import model.Faculty;
import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<Faculty> list();
    
    Optional<Faculty> findById(Integer id);
    
    Optional<Faculty> findByName(String nombre);
    
    Faculty create(String nombre, Integer idCiudad);
    
    void update(Faculty facultad);
    
    void delete(Faculty facultad);
}
