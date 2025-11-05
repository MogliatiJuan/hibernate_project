package app.service;

import model.Ciudad;
import java.util.List;
import java.util.Optional;

public interface CityService {
    List<Ciudad> list();
    
    Optional<Ciudad> findById(Integer id);
    
    Optional<Ciudad> findByName(String nombre);
    
    Ciudad create(String nombre);
    
    void update(Ciudad ciudad);
    
    void delete(Ciudad ciudad);
}
