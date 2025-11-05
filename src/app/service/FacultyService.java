package app.service;

import model.Facultad;
import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<Facultad> list();
    
    Optional<Facultad> findById(Integer id);
    
    Optional<Facultad> findByName(String nombre);
    
    Facultad create(String nombre, Integer idCiudad);
    
    void update(Facultad facultad);
    
    void delete(Facultad facultad);
}
